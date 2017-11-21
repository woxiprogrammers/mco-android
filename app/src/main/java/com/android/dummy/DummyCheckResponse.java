package com.android.dummy;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class DummyCheckResponse extends RealmObject{

	@SerializedName("page_id")
	private String pageId;

	@SerializedName("data")
	private DummyCheckdata dummyCheckdata;

	@SerializedName("next_url")
	private String nextUrl;

	@SerializedName("message")
	private String message;

	public void setPageId(String pageId){
		this.pageId = pageId;
	}

	public String getPageId(){
		return pageId;
	}

	public void setDummyCheckdata(DummyCheckdata dummyCheckdata){
		this.dummyCheckdata = dummyCheckdata;
	}

	public DummyCheckdata getDummyCheckdata(){
		return dummyCheckdata;
	}

	public void setNextUrl(String nextUrl){
		this.nextUrl = nextUrl;
	}

	public String getNextUrl(){
		return nextUrl;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}
}