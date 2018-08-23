package com.android.drawings_module.drawing_model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class DrawingCommentsResponse extends RealmObject{

	@SerializedName("message")
	private String message;

	@SerializedName("data")
	private CommentsData commentsData;

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public void setCommentsData(CommentsData commentsData){
		this.commentsData = commentsData;
	}

	public CommentsData getCommentsData(){
		return commentsData;
	}
}