package com.android.purchase;

import java.util.ArrayList;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * <b></b>
 * <p>This class is used to </p>
 * Created by Rohit.
 */
class PurchaseMaterialListItem extends RealmObject {
    @PrimaryKey
    private int indexId;
    private String item_name, item_quantity, item_unit, approved_status, item_category;
    private boolean is_diesel;
    private ArrayList<String> list_of_images;

    public int getIndexId() {
        return indexId;
    }

    public void setIndexId(int indexId) {
        this.indexId = indexId;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_quantity() {
        return item_quantity;
    }

    public void setItem_quantity(String item_quantity) {
        this.item_quantity = item_quantity;
    }

    public String getItem_unit() {
        return item_unit;
    }

    public void setItem_unit(String item_unit) {
        this.item_unit = item_unit;
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

    public ArrayList<String> getList_of_images() {
        return list_of_images;
    }

    public void setList_of_images(ArrayList<String> list_of_images) {
        this.list_of_images = list_of_images;
    }
}
