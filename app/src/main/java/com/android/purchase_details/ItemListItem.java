package com.android.purchase_details;

import java.util.List;

import com.android.purchase_request.MaterialImageItem;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ItemListItem extends RealmObject{



	/*"material_request_component_id": 42,
			"material_request_component_format_id": "MRM21710071",
			"material_request_id": 23,
			"material_request_format": "MR21710071",
			"name": "AGGREGATE 10 MM",
			"quantity": "1",
			"unit_id": 2,
			"unit_name": "BRASS(AGGREGATE)",

			"list_of_images": [
	{
		"image_url": null
	}*/



	@SerializedName("material_request_component_format_id")
	private String materialRequestComponentFormatId;

	@SerializedName("material_request_id")
	private int materialRequestId;

	@SerializedName("unit_id")
	private int unitId;

	@SerializedName("material_request_format")
	private String materialRequestFormat;

	@SerializedName("unit_name")
	private String itemUnit;

	@SerializedName("list_of_images")
	private RealmList<ImageItem> listOfImages;

	@SerializedName("name")
	private String itemName;

	@PrimaryKey
	@SerializedName("material_request_component_id")
	private int id;

	@SerializedName("quantity")
	private float itemQuantity;

	public void setPurchaseRequestId(int purchaseRequestId){
		this.unitId = purchaseRequestId;
	}

	public int getPurchaseRequestId(){
		return unitId;
	}

	public void setItemUnit(String itemUnit){
		this.itemUnit = itemUnit;
	}

	public String getItemUnit(){
		return itemUnit;
	}

	public void setListOfImages(RealmList<ImageItem> listOfImages){
		this.listOfImages = listOfImages;
	}

	public RealmList<ImageItem> getListOfImages(){
		return listOfImages;
	}

	public void setItemName(String itemName){
		this.itemName = itemName;
	}

	public String getItemName(){
		return itemName;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setItemQuantity(float itemQuantity){
		this.itemQuantity = itemQuantity;
	}

	public float getItemQuantity(){
		return itemQuantity;
	}

	public int getMaterialRequestId() {
		return materialRequestId;
	}

	public void setMaterialRequestId(int materialRequestId) {
		this.materialRequestId = materialRequestId;
	}

	public int getUnitId() {
		return unitId;
	}

	public void setUnitId(int unitId) {
		this.unitId = unitId;
	}

	public String getMaterialRequestFormat() {
		return materialRequestFormat;
	}

	public void setMaterialRequestFormat(String materialRequestFormat) {
		this.materialRequestFormat = materialRequestFormat;
	}

	public String getMaterialRequestComponentFormatId() {
		return materialRequestComponentFormatId;
	}

	public void setMaterialRequestComponentFormatId(String materialRequestComponentFormatId) {
		this.materialRequestComponentFormatId = materialRequestComponentFormatId;
	}
}