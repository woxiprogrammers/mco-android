package com.android.inventory_module.inventory_model;

import com.android.utils.AppUtils;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class RequestComponentListingItem extends RealmObject {
    /*
	"inventory_component_transfer_id": 3,
			"project_site_from": "Konark Orchid School",
			"project_site_to": "Mohar Pratima-Talegaon",
			"component_name": "Cement Ppc",
			"quantity": "10",
			"unit": "Bags"*/
    @SerializedName("project_site_to")
    private String projectSiteTo;
    @SerializedName("unit")
    private String unit;
    @SerializedName("quantity")
    private String quantity;
    @PrimaryKey
    @SerializedName("inventory_component_transfer_id")
    private int inventoryComponentTransferId;
    @SerializedName("component_name")
    private String componentName;
    @SerializedName("project_site_from")
    private String projectSiteFrom;
    private int currentSiteId = AppUtils.getInstance().getInt("projectId", -1);

    public String getProjectSiteTo() {
        return projectSiteTo;
    }

    public void setProjectSiteTo(String projectSiteTo) {
        this.projectSiteTo = projectSiteTo;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public int getInventoryComponentTransferId() {
        return inventoryComponentTransferId;
    }

    public void setInventoryComponentTransferId(int inventoryComponentTransferId) {
        this.inventoryComponentTransferId = inventoryComponentTransferId;
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public String getProjectSiteFrom() {
        return projectSiteFrom;
    }

    public void setProjectSiteFrom(String projectSiteFrom) {
        this.projectSiteFrom = projectSiteFrom;
    }
}