package com.eliot.softpedia.data;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Property;

@Entity(value="feeds", noClassnameStored=true)
public class UrlItem{
			
		public UrlItem(){}
		
		@Id
		private String id;
		
		@Property("name")
		private String name;
		
		@Property("cat_1")
		private String cat_1;
		
		@Property("cat_2")
		private String cat_2;

		@Property("url")
		private String url;
		
		@Property("_v")
		private int _v = 0;
		
		public int retryCount = 0;
		
		private boolean isFeeds ;
		
		
		
		public int get_v() {
			return _v;
		}

		public void set_v(int _v) {
			this._v = _v;
		}

		public boolean getIsFeeds() {
			return isFeeds;
		}

		public void setIsFeeds(boolean isFeeds) {
			this.isFeeds = isFeeds;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

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

		public String getCat_1() {
			return cat_1;
		}

		public void setCat_1(String cat_1) {
			this.cat_1 = cat_1;
		}

		public String getCat_2() {
			return cat_2;
		}

		public void setCat_2(String cat_2) {
			this.cat_2 = cat_2;
		}
	
}
