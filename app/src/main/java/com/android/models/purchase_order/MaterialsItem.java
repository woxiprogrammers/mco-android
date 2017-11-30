package com.android.models.purchase_order;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class MaterialsItem extends RealmObject {

	@SerializedName("unit_name")
	private String unitName;

	@SerializedName("quotation_images")
	private RealmList<QuotationImagesItem> quotationImages;

	@SerializedName("quantity")
	private String quantity;

	@SerializedName("client_approval_images")
	private RealmList<ClientApprovalImagesItem> clientApprovalImages;

	@SerializedName("name")
	private String name;

	@SerializedName("unit_id")
	private int unitId;

	@SerializedName("material_request_component_id")
	private int materialRequestComponentId;

	@SerializedName("rate_per_unit")
	private String ratePerUnit;

	public int getMaterialRequestComponentId() {
		return materialRequestComponentId;
	}

	public void setMaterialRequestComponentId(int materialRequestComponentId) {
		this.materialRequestComponentId = materialRequestComponentId;
	}

	public void setUnitName(String unitName){
		this.unitName = unitName;
	}

	public String getRatePerUnit() {
		return ratePerUnit;
	}

	public void setRatePerUnit(String ratePerUnit) {
		this.ratePerUnit = ratePerUnit;
	}

	public String getUnitName(){
		return unitName;
	}

	public void setQuotationImages(RealmList<QuotationImagesItem> quotationImages){
		this.quotationImages = quotationImages;
	}

	public RealmList<QuotationImagesItem> getQuotationImages(){
		return quotationImages;
	}

	public void setQuantity(String quantity){
		this.quantity = quantity;
	}

	public String getQuantity(){
		return quantity;
	}

	public void setClientApprovalImages(RealmList<ClientApprovalImagesItem> clientApprovalImages){
		this.clientApprovalImages = clientApprovalImages;
	}

	public RealmList<ClientApprovalImagesItem> getClientApprovalImages(){
		return clientApprovalImages;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setUnitId(int unitId){
		this.unitId = unitId;
	}

	public int getUnitId(){
		return unitId;
	}
}