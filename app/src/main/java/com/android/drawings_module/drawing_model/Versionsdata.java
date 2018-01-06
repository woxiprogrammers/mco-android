package com.android.drawings_module.drawing_model;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class Versionsdata extends RealmObject{

	@SerializedName("versions")
	private RealmList<VersionsItem> versions;

	@SerializedName("path")
	private String path;
	public void setVersions(RealmList<VersionsItem> versions){
		this.versions = versions;
	}

	public RealmList<VersionsItem> getVersions(){
		return versions;
	}


	public void setPath(String path){
		this.path = path;
	}

	public String getPath(){
		return path;
	}
}