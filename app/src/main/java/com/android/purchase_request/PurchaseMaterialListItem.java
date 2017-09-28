package com.android.purchase_request;

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
    private int indexId;
    private String name, approved_status, item_category, item_unit_name;
    private long quantity;
    private int unit_id;
    private String materialRequestComponentTypeSlug;
    private int component_type_id;
    private boolean is_diesel;
    private RealmList<MaterialImageItem> list_of_images;

    public PurchaseMaterialListItem() {
    }

    public int getIndexId() {
        return indexId;
    }

    public void setIndexId(int indexId) {
        this.indexId = indexId;
    }

    public String getItem_name() {
        return name;
    }

    public void setItem_name(String item_name) {
        this.name = item_name;
    }

    public String getApproved_status() {
        return approved_status;
    }

    public void setApproved_status(String approved_status) {
        this.approved_status = approved_status;
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

    public long getItem_quantity() {
        return quantity;
    }

    public void setItem_quantity(long item_quantity) {
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
