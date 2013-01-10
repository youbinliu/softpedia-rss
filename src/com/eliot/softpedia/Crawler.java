package com.eliot.softpedia;

import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.eliot.softpedia.data.UrlItem;

public class Crawler {
	private final Logger log = Logger.getLogger(Crawler.class);
	private final Config config;
	private static UserProxy proxy = UserProxy.getInstance();
	
	public Crawler(final Config config){
		this.config = config;
	}
	
	public void run(){
		ThreadPoolExecutor executor = new ThreadPoolExecutor(
				config.minPoolSize(), config.maxPoolSize(), 
				config.keepAliveMilliseconds(), TimeUnit.MILLISECONDS, 
				new Queue(config.requestDelayMilliseconds()));
		
		final ExecutorCounter counter = new ExecutorCounter();
		
		if ("".equals(proxy.getOneProxy())) {
			log.info("user proxy is not work,please check it");
		}
		
		try {
			Iterator<UrlItem> iterator = config.getInitItems().iterator();
			while (iterator.hasNext()) {
				UrlItem item = iterator.next();
				executor.execute(new ExecuteWorker(item,counter, executor, this.config,proxy));
			}			

			while (counter.value() != 0) {
				log.info("executors that finished: " + executor.getCompletedTaskCount());
				log.info("Number of Executors alive: " + counter.value());
				sleep();
			}
		} finally {
			executor.shutdown();
		}
	}
	
	private void sleep() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			throw new RuntimeException("main thread died. ", e);
		}

	}
	
}
