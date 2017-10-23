package com.android.models.purchase_request;

import com.android.purchase_request.PurchaseMaterialListItem;
import com.android.utils.AppUtils;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class UsersWithAclResponse extends RealmObject {
    @PrimaryKey
    int id = 0;
    @SerializedName("available_users")
    private RealmList<AvailableUsersItem> availableUsers;
    @SerializedName("material_list")
    private RealmList<PurchaseMaterialListItem> materialList;
    @SerializedName("message")
    private String message;
    private int currentSiteId = AppUtils.getInstance().getInt("projectId", -1);

    public RealmList<AvailableUsersItem> getAvailableUsers() {
        return availableUsers;
    }

    public void setAvailableUsers(RealmList<AvailableUsersItem> availableUsers) {
        this.availableUsers = availableUsers;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public RealmList<PurchaseMaterialListItem> getMaterialList() {
        return materialList;
    }

    public void setMaterialList(RealmList<PurchaseMaterialListItem> materialList) {
        this.materialList = materialList;
    }
}