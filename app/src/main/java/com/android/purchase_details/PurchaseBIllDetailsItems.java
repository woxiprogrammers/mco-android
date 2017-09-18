package com.android.purchase_details;

import io.realm.RealmObject;

/**
 * Created by woxi-007 on 18/9/17.
 */

public class PurchaseBIllDetailsItems extends RealmObject {

    private int id;
    private String materialName;
    private int quantity;
    private String unit;
    private  String challanNumber;
    private String vehicleNumber;

    private String inTime;
    private String outTime;
    private int BillAmount;

    public PurchaseBIllDetailsItems(){
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getChallanNumber() {
        return challanNumber;
    }

    public void setChallanNumber(String challanNumber) {
        this.challanNumber = challanNumber;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getInTime() {
        return inTime;
    }

    public void setInTime(String inTime) {
        this.inTime = inTime;
    }

    public String getOutTime() {
        return outTime;
    }

    public void setOutTime(String outTime) {
        this.outTime = outTime;
    }

    public int getBillAmount() {
        return BillAmount;
    }

    public void setBillAmount(int billAmount) {
        BillAmount = billAmount;
    }
}
