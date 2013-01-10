package com.eliot.softpedia;


import java.util.List;

import com.eliot.softpedia.data.DataHelp;
import com.eliot.softpedia.data.UrlItem;
import com.google.code.morphia.Datastore;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Datastore ds = DataHelp.getDatastore();
	
		List<UrlItem> initFeedsList = ds.find(UrlItem.class).asList();
	
		Config cfg = new Config(initFeedsList);
		
		Crawler crawler = new Crawler(cfg);
		crawler.run();
		
		
	}

}
