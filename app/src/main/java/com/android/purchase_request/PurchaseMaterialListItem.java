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

    ///////////////////////////////
    /*

            "material_request_format": "MR21710071",
            "name": "AGGREGATE 10 MM",
            "quantity": "1",
            "unit_id": 2,
            "unit": "BRASS(AGGREGATE)",
            "component_type_id": 3,
            "component_type": "Structure Material",
            "component_status_id": 8,
            "component_status": "p-r-assigned",
            "created_at": "2017-10-07 12:09:00"*/

//////////////////////////////////////////////







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

    @SerializedName("material_request_component_format")
    private String materialRequestComponentFormat;

    @SerializedName("material_request_id")
    private int materialRequestId;

    public String getComponentType() {
        return componentType;
    }

    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }

    public String getMaterialRequestFormat() {
        return materialRequestFormat;
    }

    public void setMaterialRequestFormat(String materialRequestFormat) {
        this.materialRequestFormat = materialRequestFormat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public int getUnit_id() {
        return unit_id;
    }

    public void setUnit_id(int unit_id) {
        this.unit_id = unit_id;
    }

    public int getComponent_type_id() {
        return component_type_id;
    }

    public void setComponent_type_id(int component_type_id) {
        this.component_type_id = component_type_id;
    }

    public String getMaterialRequestComponentFormat() {
        return materialRequestComponentFormat;
    }

    public void setMaterialRequestComponentFormat(String materialRequestComponentFormat) {
        this.materialRequestComponentFormat = materialRequestComponentFormat;
    }

    public int getMaterialRequestId() {
        return materialRequestId;
    }

    public void setMaterialRequestId(int materialRequestId) {
        this.materialRequestId = materialRequestId;
    }

    @SerializedName("created_at")
    private String createdAt;
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
}
