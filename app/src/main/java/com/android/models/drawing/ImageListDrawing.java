package com.android.models.drawing;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class ImageListDrawing extends RealmObject{

	@SerializedName("images")
	private RealmList<ImagesListDrawingItem> imagesListDrawing;

	public void setImagesListDrawing(RealmList<ImagesListDrawingItem> imagesListDrawing){
		this.imagesListDrawing = imagesListDrawing;
	}

	public RealmList<ImagesListDrawingItem> getImagesListDrawing(){
		return imagesListDrawing;
	}
}