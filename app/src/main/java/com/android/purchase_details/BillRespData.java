package com.android.purchase_details;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class BillRespData{

	@SerializedName("purchase_bill_list")
	private List<PurchaseBillListItem> purchaseBillList;

	public void setPurchaseBillList(List<PurchaseBillListItem> purchaseBillList){
		this.purchaseBillList = purchaseBillList;
	}

	public List<PurchaseBillListItem> getPurchaseBillList(){
		return purchaseBillList;
	}
}