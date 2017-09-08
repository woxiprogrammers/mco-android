package com.android.purchase;

import io.realm.RealmObject;

/**
 * <b></b>
 * <p>This class is used to </p>
 * Created by Rohit.
 */
public class MaterialImageItem extends RealmObject {
    private int indexId;
    private String material_image;

    public MaterialImageItem() {
    }

    public int getIndexId() {
        return indexId;
    }

    public void setIndexId(int indexId) {
        this.indexId = indexId;
    }

    public String getMaterial_image() {
        return material_image;
    }

    public void setMaterial_image(String material_image) {
        this.material_image = material_image;
    }
}
