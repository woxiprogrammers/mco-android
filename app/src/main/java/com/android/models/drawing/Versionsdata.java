package com.android.models.drawing;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class Versionsdata extends RealmObject{

	@SerializedName("versions")
	private RealmList<VersionsItem> versions;

	public void setVersions(RealmList<VersionsItem> versions){
		this.versions = versions;
	}

	public RealmList<VersionsItem> getVersions(){
		return versions;
	}
}