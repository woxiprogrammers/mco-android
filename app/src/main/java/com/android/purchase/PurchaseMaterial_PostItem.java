package com.android.purchase;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * <b></b>
 * <p>This class is used to </p>
 * Created by Rohit.
 */
public class PurchaseMaterial_PostItem extends RealmObject {
    private String request_assigned_to;
    private RealmList<PurchaseMaterialListItem> arrPurchaseMaterialList;

    public PurchaseMaterial_PostItem() {
    }

    public String getRequest_assigned_to() {
        return request_assigned_to;
    }

    public void setRequest_assigned_to(String request_assigned_to) {
        this.request_assigned_to = request_assigned_to;
    }

    public RealmList<PurchaseMaterialListItem> getArrPurchaseMaterialList() {
        return arrPurchaseMaterialList;
    }

    public void setArrPurchaseMaterialList(RealmList<PurchaseMaterialListItem> arrPurchaseMaterialList) {
        this.arrPurchaseMaterialList = arrPurchaseMaterialList;
    }
}
