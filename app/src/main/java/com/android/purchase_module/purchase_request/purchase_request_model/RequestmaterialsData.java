package com.android.purchase_module.purchase_request.purchase_request_model;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class RequestmaterialsData extends RealmObject{

	@SerializedName("purchase_order_request_list")
	private RealmList<RequestMaterialListItem> requestMaterialList;

	@SerializedName("pdf_thumbnail_url")
	private String pdfThumbnailUrl;

	public void setRequestMaterialList(RealmList<RequestMaterialListItem> requestMaterialList){
		this.requestMaterialList = requestMaterialList;
	}

	public RealmList<RequestMaterialListItem> getRequestMaterialList(){
		return requestMaterialList;
	}

	public String getPdfThumbnailUrl() {
		return pdfThumbnailUrl;
	}

	public void setPdfThumbnailUrl(String pdfThumbnailUrl) {
		this.pdfThumbnailUrl = pdfThumbnailUrl;
	}
}