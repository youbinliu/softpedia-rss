package com.eliot.softpedia;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.eliot.softpedia.data.DataHelp;
import com.eliot.softpedia.data.SoftItem;
import com.eliot.softpedia.data.UrlItem;
import com.google.code.morphia.Datastore;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

public class ExecuteWorker implements Runnable {

	 private final ExecutorCounter counter;
	 private final ThreadPoolExecutor executor;
	 private final Config config;
	 private final UserProxy proxy;
	 private final UrlItem item;
	 private final Logger log = Logger.getLogger(ExecuteWorker.class);
	 private final static Datastore ds = DataHelp.getDatastore();
	 private static int  okCount = 0;
	 
	 public ExecuteWorker(UrlItem item,ExecutorCounter counter,ThreadPoolExecutor threadPoolExecutor,Config config,UserProxy proxy){
		 this.item = item;
		 this.counter = counter;
		 this.executor = threadPoolExecutor;
		 this.config = config;
		 this.proxy = proxy;
		 counter.increase();
	 }
	@Override
	public void run() {
		if(item.getIsFeeds()){
			processFeeds();
		}else {
			processSoft();
		}
		
	}
	
	private static final String user_agent = "Mozilla/5.0";
	private String inputStream2String(InputStream stream){
      String rtnValue="";
      try {
    	  BufferedReader reader = new BufferedReader(new InputStreamReader(stream,"UTF-8"));
          String readStr=reader.readLine();  
          
          while(readStr!=null){
        	  rtnValue+=readStr;
              readStr=reader.readLine();  
          } 
          
          reader.close();
      } catch (IOException ex) {
      		log.error("inputStream2String:"+ex.getMessage());
      }
      return rtnValue;
	}
	
	private URLConnection buildConnection(String url){
		String proxyIp = proxy.getOneProxy();
		
		URLConnection urlConnection = null;
		
		try {			
			if(!"".equals(proxyIp)){
				SocketAddress addr = new InetSocketAddress(
						proxyIp.split(":")[0],
						Integer.parseInt(proxyIp.split(":")[1]));

				Proxy typeProxy = new Proxy(Proxy.Type.HTTP, addr);
				urlConnection = new URL(url).openConnection(typeProxy);
			}else {
				urlConnection = new URL(url).openConnection();
			}
			
			urlConnection.setRequestProperty("User-Agent", user_agent);			
			urlConnection.setConnectTimeout(5000);
			
		} catch (IOException e) {			
			log.error("buildConnection��"+e.getMessage());
		}			
		
		return urlConnection;
	}
	
	private void processFeeds(){
		try {		
			log.info("the "+item.retryCount+"th  connect "+item.getUrl());
			
			SyndFeedInput si = new SyndFeedInput();
			SyndFeed feed = si.build(new XmlReader(buildConnection(item.getUrl())));
	        Iterator<?> entryIter = feed.getEntries().iterator();
	        
	        while (entryIter.hasNext()) {
	            SyndEntry entry = (SyndEntry) entryIter.next();
	            //addSoftItem(entry);  
	            SoftItem softItem = new SoftItem();
	            softItem.setCate_1(item.getCat_1());
	            softItem.setCate_2(item.getCat_2());
	            softItem.setName(entry.getTitle());
	            softItem.setDescription(entry.getDescription().getValue());
	            softItem.setUrl(entry.getLink());
	            softItem.setUpdateDate(""+entry.getPublishedDate());
	            ds.save(softItem);
	        }
	        log.info(++okCount+" finshed feeds url:"+item.getUrl());
	        

		} catch (SocketTimeoutException e) {
			
			if(++item.retryCount < config.getRetryCount()){
				executor.execute(new ExecuteWorker(item,counter, executor, config,proxy));
			}
		}catch (FeedException e) {
			log.info("FeedException:"+e.getMessage());
		}catch(IOException e){
			log.info("IOException:"+e.getMessage());
		}catch(Exception e){
			
		}finally{
			counter.decrease();
		}
	}
	
	private void addSoftItem(SyndEntry entry){
		UrlItem tmp = new UrlItem();
        tmp.setName(entry.getTitle());
        tmp.setIsFeeds(false);
        tmp.setUrl(entry.getLink());
        tmp.setCat_1(item.getCat_1());
        tmp.setCat_2(item.getCat_2());
        executor.execute(new ExecuteWorker(tmp,counter, executor, config,proxy));
	}
	
	private void processSoft(){
		String soft = "";
		try {
			URLConnection conn = buildConnection(item.getUrl());
			
			Document doc = Jsoup.parse(inputStream2String(conn.getInputStream()));
			Elements es = doc.select("span[itemprop=name]");  
			if(!es.isEmpty()){
				soft += "author:"+es.first().html();
			}
			es = doc.select("a[itemprop=downloadURL]");
			if(!es.isEmpty()){
				soft += " download:"+es.first().attr("href");
			}
			
			log.info("finshed soft url:"+item.getUrl());
			log.info(soft);
			counter.decrease();
		} catch (Exception e) {
			log.error("processSoft:"+e.getMessage());
			if(++item.retryCount < 3)
				executor.execute(new ExecuteWorker(item,counter, executor, config,proxy));	   
		}
		
		
		
	}

}
