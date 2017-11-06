package com.android.purchase_request;

import com.android.utils.AppUtils;
import com.google.gson.annotations.SerializedName;

import java.util.Random;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * <b></b>
 * <p>This class is used to </p>
 * Created by Rohit.
 */
public class PurchaseMaterialListItem extends RealmObject {
    @PrimaryKey
    private int primaryKey = new Random().nextInt((999999) + 11) + new Random().nextInt((999999) + 11);
    @SerializedName("component_type")
    private String componentType;
    @SerializedName("material_request_format")
    private String materialRequestFormat;
    @SerializedName("name")
    private String name;
    @SerializedName("unit")
    private String item_unit_name;
    @SerializedName("quantity")
    private float quantity;
    @SerializedName("unit_id")
    private int unit_id;
    @SerializedName("component_type_id")
    private int component_type_id;
    @SerializedName("component_status")
    private String componentStatus;
    //    @PrimaryKey
    @SerializedName("material_request_component_id")
    private int materialRequestComponentId;
    @SerializedName("material_request_component_format")
    private String materialRequestComponentFormat;
    @SerializedName("material_request_id")
    private int materialRequestId;
    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("have_access")
    private String have_access;

    public void setHave_access(String have_access) {
        this.have_access = have_access;
    }
    public String getHave_access() {
        return have_access;
    }


    /////////////////////////
//    private String item_category;
    private String materialRequestComponentTypeSlug;
    private boolean is_diesel;
    private RealmList<MaterialImageItem> list_of_images;
    private int currentSiteId = AppUtils.getInstance().getInt("projectId", -1);

    public PurchaseMaterialListItem() {
    }

    public int getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(int primaryKey) {
        this.primaryKey = primaryKey;
    }

    public String getItem_name() {
        return name;
    }

    public void setItem_name(String item_name) {
        this.name = item_name;
    }

    public String getComponentStatus() {
        return componentStatus;
    }

    public void setComponentStatus(String componentStatus) {
        this.componentStatus = componentStatus;
    }

    public boolean is_diesel() {
        return is_diesel;
    }

    public void setIs_diesel(boolean is_diesel) {
        this.is_diesel = is_diesel;
    }

    public RealmList<MaterialImageItem> getList_of_images() {
        return list_of_images;
    }

    public void setList_of_images(RealmList<MaterialImageItem> list_of_images) {
        this.list_of_images = list_of_images;
    }

    public float getItem_quantity() {
        return quantity;
    }

    public void setItem_quantity(float item_quantity) {
        this.quantity = item_quantity;
    }

    public int getItem_unit_id() {
        return unit_id;
    }

    public void setItem_unit_id(int item_unit_id) {
        this.unit_id = item_unit_id;
    }

    public String getMaterialRequestComponentTypeSlug() {
        return materialRequestComponentTypeSlug;
    }

    public void setMaterialRequestComponentTypeSlug(String materialRequestComponentTypeSlug) {
        this.materialRequestComponentTypeSlug = materialRequestComponentTypeSlug;
    }

    public int getComponentTypeId() {
        return component_type_id;
    }

    public void setComponentTypeId(int materialRequestComponentTypeId) {
        this.component_type_id = materialRequestComponentTypeId;
    }

    public String getItem_unit_name() {
        return item_unit_name;
    }

    public void setItem_unit_name(String item_unit_name) {
        this.item_unit_name = item_unit_name;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getMaterialRequestComponentId() {
        return materialRequestComponentId;
    }

    public void setMaterialRequestComponentId(int materialRequestComponentId) {
        this.materialRequestComponentId = materialRequestComponentId;
    }
    /*// local defined primary key
    @SuppressLint("DefaultLocale")
    public void compoundPrimaryKey() {
        if (!isManaged()) {
            // only un-managed objects needs compound key
            int randomNum = new Random().nextInt((999999) + 11);
            this.primaryKey = String.format("%s%d%d", this.name.replaceAll("[^a-zA-Z]+", ""), this.materialRequestComponentId, randomNum);
        }
    }*/
}
