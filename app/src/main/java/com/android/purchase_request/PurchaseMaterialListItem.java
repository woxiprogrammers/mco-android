package com.android.purchase_request;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * <b></b>
 * <p>This class is used to </p>
 * Created by Rohit.
 */
public class PurchaseMaterialListItem extends RealmObject {
    @SerializedName("component_type")
    private String componentType;
    //    @SerializedName("unit")
//    private String unit;
//    @SerializedName("quantity")
//    private String quantity;
//    @SerializedName("component_status")
//    private String componentStatus;
    //    @SerializedName("component_type_id")
//    private int componentTypeId;
    //    @SerializedName("name")
//    private String name;
//    @SerializedName("component_status_id")
//    private int componentStatusId;
    //    @SerializedName("unit_id")
//    private int unitId;
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
    @PrimaryKey
    @SerializedName("material_request_component_id")
    private int materialRequestComponentId;
    /////////////////////////
    private String item_category;
    private String materialRequestComponentTypeSlug;
    private boolean is_diesel;
    private RealmList<MaterialImageItem> list_of_images;

    public PurchaseMaterialListItem() {
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

    public String getItem_category() {
        return item_category;
    }

    public void setItem_category(String item_category) {
        this.item_category = item_category;
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

    public int getMaterialRequestComponentTypeId() {
        return component_type_id;
    }

    public void setMaterialRequestComponentTypeId(int materialRequestComponentTypeId) {
        this.component_type_id = materialRequestComponentTypeId;
    }

    public String getItem_unit_name() {
        return item_unit_name;
    }

    public void setItem_unit_name(String item_unit_name) {
        this.item_unit_name = item_unit_name;
    }
}
