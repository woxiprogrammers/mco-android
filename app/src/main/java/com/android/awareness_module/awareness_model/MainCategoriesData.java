package com.android.awareness_module.awareness_model;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class MainCategoriesData extends RealmObject {

	@SerializedName("main_categories")
	private RealmList<MainCategoriesItem> mainCategories;

	public void setMainCategories(RealmList<MainCategoriesItem> mainCategories){
		this.mainCategories = mainCategories;
	}

	public RealmList<MainCategoriesItem> getMainCategories(){
		return mainCategories;
	}
}