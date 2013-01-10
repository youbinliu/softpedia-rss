package com.eliot.softpedia.data;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.mongodb.Mongo;

public class DataHelp {
	private static  String server = "211.69.198.224";
	private static int port = 27017;		
	private static Morphia morphia = null;
	private static Mongo mongo = null;
	private static Datastore datastore = null;
	public static String Database = "softpedia";
	private static String username = "";
	private static String password = "";
	
	public DataHelp(){
		
	}
	
	public static Morphia getMorphia()
	{				
		if (morphia == null) {
			morphia = new Morphia();			
			morphia.map(UrlItem.class);		
			morphia.map(ProxyItem.class);
			morphia.map(SoftItem.class);
		}				
		return morphia;
	}
	
	public static  Mongo getMongo()
	{
		if (mongo == null) {
			try {			
				mongo = new Mongo(server,port);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}		
		return mongo;
	}	
	
	public static Datastore getDatastore()
	{
		if (datastore == null) {
			Mongo mongo = getMongo();
			Morphia morphia = getMorphia();
			datastore = morphia.createDatastore(mongo,Database,username,password.toCharArray());
		}
		return datastore;
	}
	
}
