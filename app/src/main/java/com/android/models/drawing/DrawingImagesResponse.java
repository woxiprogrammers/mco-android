package com.android.models.drawing;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class DrawingImagesResponse extends RealmObject{

	@SerializedName("data")
	private ImageListDrawing imageListDrawing;

	@SerializedName("message")
	private String message;

	public void setImageListDrawing(ImageListDrawing imageListDrawing){
		this.imageListDrawing = imageListDrawing;
	}

	public ImageListDrawing getImageListDrawing(){
		return imageListDrawing;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}
}