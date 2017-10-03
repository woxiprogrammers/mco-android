package com.android.purchase_details;

import java.util.List;

import com.android.purchase_request.MaterialImageItem;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ItemListItem extends RealmObject{

	@SerializedName("unit_id")
	private int unitId;

	@SerializedName("unit_name")
	private String itemUnit;

	@SerializedName("list_of_images")
	private RealmList<ImageItem> listOfImages;

	@SerializedName("name")
	private String itemName;

	@PrimaryKey
	@SerializedName("id")
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
}