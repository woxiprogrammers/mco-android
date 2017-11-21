package com.android.models.awarenessmodels;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class AwarenessMainCategoryResponse extends RealmObject{

	@SerializedName("page_id")
	private String pageId;

	@SerializedName("data")
	private MainCategoriesData mainCategoriesData;

	@SerializedName("message")
	private String message;

	public void setPageId(String pageId){
		this.pageId = pageId;
	}

	public String getPageId(){
		return pageId;
	}

	public void setMainCategoriesData(MainCategoriesData mainCategoriesData){
		this.mainCategoriesData = mainCategoriesData;
	}

	public MainCategoriesData getMainCategoriesData(){
		return mainCategoriesData;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}
}