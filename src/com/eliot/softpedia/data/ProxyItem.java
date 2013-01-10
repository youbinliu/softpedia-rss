package com.eliot.softpedia.data;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Property;

@Entity("proxies")
public class ProxyItem {
	public ProxyItem(){}
	@Id
	private String id;
	@Property("url")
	private String url;
	public int successCount = 0;
	@Property("__v")
	private String _v;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String get_v() {
		return _v;
	}
	public void set_v(String _v) {
		this._v = _v;
	}
}