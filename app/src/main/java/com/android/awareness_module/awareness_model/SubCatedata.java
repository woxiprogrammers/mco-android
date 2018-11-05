package com.android.awareness_module.awareness_model;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class SubCatedata extends RealmObject{

	@SerializedName("sub_categories")
	private RealmList<AwarenessSubCategoriesItem> subCategories;

	public void setSubCategories(RealmList<AwarenessSubCategoriesItem> subCategories){
		this.subCategories = subCategories;
	}

	public RealmList<AwarenessSubCategoriesItem> getSubCategories(){
		return subCategories;
	}
}