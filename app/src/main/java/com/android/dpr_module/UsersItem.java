package com.android.dpr_module;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class UsersItem extends RealmObject{

	@SerializedName("cat")
	private String cat;

	@SerializedName("id")
	private int id;

	@SerializedName("no_of_users")
	private int noOfUsers;

	public void setCat(String cat){
		this.cat = cat;
	}

	public String getCat(){
		return cat;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setNoOfUsers(int noOfUsers){
		this.noOfUsers = noOfUsers;
	}

	public int getNoOfUsers(){
		return noOfUsers;
	}
}