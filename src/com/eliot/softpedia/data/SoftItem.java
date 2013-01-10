package com.eliot.softpedia.data;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Property;

@Entity(value="softs",noClassnameStored=true)
public class SoftItem {
	@Id
	private String id;
	@Property("name")
	private String name;
	@Property("cate_1")
	private String cate_1;
	@Property("cate_2")
	private String cate_2;
	public String getCate_1() {
		return cate_1;
	}
	public void setCate_1(String cate_1) {
		this.cate_1 = cate_1;
	}
	public String getCate_2() {
		return cate_2;
	}
	public void setCate_2(String cate_2) {
		this.cate_2 = cate_2;
	}
	@Property("url")
	private String url;	
	@Property("updateDate")
	private String updateDate;
	@Property("description")
	private String description;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}
