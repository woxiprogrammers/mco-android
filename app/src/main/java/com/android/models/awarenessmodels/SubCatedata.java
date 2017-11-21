package com.android.models.awarenessmodels;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class SubCatedata extends RealmObject{

	@SerializedName("sub_categories")
	private RealmList<SubCategoriesItem> subCategories;

	public void setSubCategories(RealmList<SubCategoriesItem> subCategories){
		this.subCategories = subCategories;
	}

	public RealmList<SubCategoriesItem> getSubCategories(){
		return subCategories;
	}
}