package com.android.purchase_module.purchase_request;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class RequestmaterialsData extends RealmObject{

	@SerializedName("purchase_order_request_list")
	private RealmList<RequestMaterialListItem> requestMaterialList;

	public void setRequestMaterialList(RealmList<RequestMaterialListItem> requestMaterialList){
		this.requestMaterialList = requestMaterialList;
	}

	public RealmList<RequestMaterialListItem> getRequestMaterialList(){
		return requestMaterialList;
	}
}