package com.android.awareness_module.awareness_model;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class SubCategoriesResponse extends RealmObject {

	@SerializedName("page_id")
	private String pageId;

	@SerializedName("data")
	private SubCatedata subCatedata;

	@SerializedName("message")
	private String message;

	public void setPageId(String pageId){
		this.pageId = pageId;
	}

	public String getPageId(){
		return pageId;
	}

	public void setSubCatedata(SubCatedata subCatedata){
		this.subCatedata = subCatedata;
	}

	public SubCatedata getSubCatedata(){
		return subCatedata;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}
}