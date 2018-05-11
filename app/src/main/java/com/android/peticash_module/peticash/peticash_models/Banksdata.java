package com.android.peticash_module.peticash.peticash_models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class Banksdata extends RealmObject{

	@SerializedName("banks")
	private RealmList<BanksItem> banks;

	public void setBanks(RealmList<BanksItem> banks){
		this.banks = banks;
	}

	public RealmList<BanksItem> getBanks(){
		return banks;
	}
}