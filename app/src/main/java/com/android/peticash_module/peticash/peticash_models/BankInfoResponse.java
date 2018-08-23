package com.android.peticash_module.peticash.peticash_models;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class BankInfoResponse extends RealmObject{

	@SerializedName("data")
	private Banksdata banksdata;

	@SerializedName("message")
	private String message;

	public void setBanksdata(Banksdata banksdata){
		this.banksdata = banksdata;
	}

	public Banksdata getBanksdata(){
		return banksdata;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}
}