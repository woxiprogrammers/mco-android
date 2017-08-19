package com.android.models.inventory;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class MaterialListItem extends RealmObject{

	@SerializedName("quantity_out")
	private String quantityOut;

	@SerializedName("id")
	private int id;

	@SerializedName("quantity_in")
	private String quantityIn;

	@SerializedName("material_name")
	private String materialName;

	@SerializedName("quantity_available")
	private String quantityAvailable;

	private boolean isSelected;

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean selected) {
		isSelected = selected;
	}

	public void setQuantityOut(String quantityOut){
		this.quantityOut = quantityOut;
	}

	public String getQuantityOut(){
		return quantityOut;
	}

	public void setQuantityIn(String quantityIn){
		this.quantityIn = quantityIn;
	}

	public String getQuantityIn(){
		return quantityIn;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setMaterialName(String materialName){
		this.materialName = materialName;
	}

	public String getMaterialName(){
		return materialName;
	}

	public void setQuantityAvailable(String quantityAvailable){
		this.quantityAvailable = quantityAvailable;
	}

	public String getQuantityAvailable(){
		return quantityAvailable;
	}

}