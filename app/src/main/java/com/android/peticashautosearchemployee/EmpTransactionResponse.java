package com.android.peticashautosearchemployee;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class EmpTransactionResponse extends RealmObject{

	@SerializedName("data")
	private TransactionsData transactionsData;

	@SerializedName("message")
	private String message;

	public void setTransactionsData(TransactionsData transactionsData){
		this.transactionsData = transactionsData;
	}

	public TransactionsData getTransactionsData(){
		return transactionsData;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}
}