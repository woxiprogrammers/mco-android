package com.android.peticash_module.peticash.peticash_models;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class BanksItem extends RealmObject{

	@SerializedName("account_number")
	private String accountNumber;

	@SerializedName("bank_id")
	private int bankId;

	@SerializedName("balance_amount")
	private String balanceAmount;

	@SerializedName("bank_name")
	private String bankName;

	public void setAccountNumber(String accountNumber){
		this.accountNumber = accountNumber;
	}

	public String getAccountNumber(){
		return accountNumber;
	}

	public void setBankId(int bankId){
		this.bankId = bankId;
	}

	public int getBankId(){
		return bankId;
	}

	public void setBalanceAmount(String balanceAmount){
		this.balanceAmount = balanceAmount;
	}

	public String getBalanceAmount(){
		return balanceAmount;
	}

	public void setBankName(String bankName){
		this.bankName = bankName;
	}

	public String getBankName(){
		return bankName;
	}
}