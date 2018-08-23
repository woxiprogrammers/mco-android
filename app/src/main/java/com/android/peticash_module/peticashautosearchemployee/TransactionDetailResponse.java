package com.android.peticash_module.peticashautosearchemployee;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class TransactionDetailResponse extends RealmObject{

	@SerializedName("data")
	private TransactionDetailData transactionDetailData;

	@SerializedName("message")
	private String message;

	public void setTransactionDetailData(TransactionDetailData transactionDetailData){
		this.transactionDetailData = transactionDetailData;
	}

	public TransactionDetailData getTransactionDetailData(){
		return transactionDetailData;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}
}