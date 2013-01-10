package com.eliot.softpedia;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.eliot.softpedia.data.DataHelp;
import com.eliot.softpedia.data.ProxyItem;
import com.google.code.morphia.Datastore;


public class UserProxy {
	
	private static UserProxy instance;
	private int count = 0;
	private static ArrayList<ProxyItem> ips;
	
	
	
	private UserProxy(){
		ips = updateIpsFromDb();
	}
	private String inputStream2String(InputStream stream){
	      String rtnValue="";
	      try {
	    	  BufferedReader reader = new BufferedReader(new InputStreamReader(stream,"UTF-8"));
	          String readStr=reader.readLine();  
	          
	          while(readStr!=null){
	        	  rtnValue+=readStr;
	        	  rtnValue+="\n";
	              readStr=reader.readLine();  
	          } 
	          
	          reader.close();
	      } catch (IOException ex) {
	      		System.out.println(ex.getMessage());
	      }
	      return rtnValue;
	}
	private static final String user_agent = "Mozilla/5.0";
	private URLConnection buildConnection(String url){	
		
		URLConnection urlConnection = null;
		try {
			urlConnection = new URL(url).openConnection();
		} catch (IOException e) {
			
			e.printStackTrace();
		}			
		urlConnection.setRequestProperty("User-Agent", user_agent);
		
		urlConnection.setConnectTimeout(5000);
		
		return urlConnection;
	}
	
	private ArrayList<ProxyItem> updateIpsFromDb(){
		Datastore ds = DataHelp.getDatastore();
		ArrayList<ProxyItem> ipList = new ArrayList<ProxyItem>();		
		ipList.addAll(ds.find(ProxyItem.class).asList());
		count = ipList.size();
		return ipList;
	}
	
	private ArrayList<String> updateIps(){
		ArrayList<String> ipList = new ArrayList<String>();
		URLConnection conn = buildConnection("http://www.site-digger.com/html/articles/20110516/proxieslist.html");
		String siteDigger = "";
		try {
			siteDigger = inputStream2String(conn.getInputStream());
		} catch (IOException e) {
			
		}
		Pattern pattern = Pattern.compile("<textarea.+?textarea>",Pattern.DOTALL);
		Matcher matcher = pattern.matcher(siteDigger);
		if(matcher.find()){		
			String textarea = matcher.group();
			String[] ips = textarea.split("\n");
			this.count = ips.length-2;
			for (int i = 1; i < ips.length-1; i++) {
				ipList.add(ips[i]);
			}
		}
		return ipList;
		
	}
	
	public static UserProxy getInstance(){
		if(instance==null)
			instance = new UserProxy();
		return instance;
	}
	
	private boolean testProxy(String proxyIp){
		try {			
			URLConnection urlConnection = null;
			SocketAddress addr = new InetSocketAddress(
					proxyIp.split(":")[0],
					Integer.parseInt(proxyIp.split(":")[1]));

			Proxy typeProxy = new Proxy(Proxy.Type.HTTP, addr);
			
			urlConnection = new URL("http://www.baidu.com").openConnection(typeProxy);
			
			urlConnection.setRequestProperty("User-Agent", user_agent);			
			urlConnection.setConnectTimeout(5000);
			if(urlConnection.getInputStream() != null){
				return true;
			}else {
				return false;
			}
			
		} catch (IOException e) {
			return false;
		}			
		
	}
	
	public String getOneProxy(){
		return "";
//		int cur = (int)(Math.random()*count);
//		ProxyItem proxyItem = ips.get(cur);
//		
//		if(proxyItem.successCount>0)return proxyItem.getUrl();
//		
//		while(true){
//			if (testProxy(proxyItem.getUrl())) {
//				++proxyItem.successCount;
//				return ips.get(cur).getUrl();
//			}
//		}
		
	}
	
	public int getProxyCount(){
		return count;
	}
	public static void main(String[] args) {
		UserProxy proxy = UserProxy.getInstance();
		System.out.println(proxy.getOneProxy());
	}
}

