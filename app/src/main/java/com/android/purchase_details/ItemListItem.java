package com.android.purchase_details;

import java.util.List;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class ItemListItem extends RealmObject{

	@SerializedName("purchase_request_id")
	private int purchaseRequestId;

	@SerializedName("item_unit")
	private String itemUnit;

	@SerializedName("list_of_images")
	private List<String> listOfImages;

	@SerializedName("item_name")
	private String itemName;

	@SerializedName("id")
	private int id;

	@SerializedName("item_quantity")
	private String itemQuantity;

	public void setPurchaseRequestId(int purchaseRequestId){
		this.purchaseRequestId = purchaseRequestId;
	}

	public int getPurchaseRequestId(){
		return purchaseRequestId;
	}

	public void setItemUnit(String itemUnit){
		this.itemUnit = itemUnit;
	}

	public String getItemUnit(){
		return itemUnit;
	}

	public void setListOfImages(List<String> listOfImages){
		this.listOfImages = listOfImages;
	}

	public List<String> getListOfImages(){
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

	public void setItemQuantity(String itemQuantity){
		this.itemQuantity = itemQuantity;
	}

	public String getItemQuantity(){
		return itemQuantity;
	}
}