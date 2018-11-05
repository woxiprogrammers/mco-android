package com.android.awareness_module.awareness_model;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class AwarenesListData extends RealmObject{

	@SerializedName("path")
	private String path;

	@SerializedName("file_details")
	private RealmList<FileDetailsItem> fileDetails;

	public void setPath(String path){
		this.path = path;
	}

	public String getPath(){
		return path;
	}

	public void setFileDetails(RealmList<FileDetailsItem> fileDetails){
		this.fileDetails = fileDetails;
	}

	public RealmList<FileDetailsItem> getFileDetails(){
		return fileDetails;
	}
}