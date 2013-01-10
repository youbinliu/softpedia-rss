package com.eliot.softpedia;

import java.util.List;

import com.eliot.softpedia.data.UrlItem;

public class Config {
	private int minPoolSize;
	private int maxPoolSize;
	private long keepAliveMilliseconds;
	private int requestDelayMilliseconds;
	private List<UrlItem> items;
	private int retryCount;
	
	public int getRetryCount() {
		return retryCount;
	}

	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}

	public Config(List<UrlItem> items) {
		minPoolSize = 15;
		maxPoolSize = 30;
		keepAliveMilliseconds = 1000*60*60;
		requestDelayMilliseconds = 3000;
		this.items = items;
		retryCount = 3;
	}
	
	public List<UrlItem> getInitItems(){
		return items;
	}
	
	public void setInitItems(List<UrlItem> items){
		this.items = items;
	}
	
	public int minPoolSize() {
		return minPoolSize;
	}

	public int maxPoolSize() {
		return maxPoolSize;
	}

	public long keepAliveMilliseconds() {
		return keepAliveMilliseconds;
	}

	public int requestDelayMilliseconds() {
		return requestDelayMilliseconds;
	}

	public void minPoolSize(final int minPoolSize) {
		this.minPoolSize = minPoolSize;
	}

	public void maxPoolSize(final int maxPoolSize) {
		this.maxPoolSize = maxPoolSize;

	}

	public void keepAliveMilliseconds(final int keepAliveMilliseconds) {
		this.keepAliveMilliseconds = keepAliveMilliseconds;

	}

	public void requestDelayMilliseconds(final int requestDelayMilliseconds) {
		this.requestDelayMilliseconds = requestDelayMilliseconds;
	}

}
