package com.android.purchase_module.purchase_request.purchase_request_model;

import android.media.Image;

import com.android.purchase_module.purchase_request.purchase_request_model.bill_model.ImagesItem;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class VendorsItem extends RealmObject{


	@SerializedName("images")
	private RealmList<ImagePurchaseOrderRequest> imagePurchaseOrderRequests;

	@SerializedName("images")
	private RealmList<PdfPurchaseOrderRequest> pdfPurchaseOrderRequests;

	@SerializedName("rate_without_tax")
	private String rate;

	@SerializedName("vendor_id")
	private int vendorId;

	@SerializedName("component_vendor_relation_id")
	private int componentVendorRelationId;

	public int getOrderRequestComponentId() {
		return orderRequestComponentId;
	}

	public void setOrderRequestComponentId(int orderRequestComponentId) {
		this.orderRequestComponentId = orderRequestComponentId;
	}

	@SerializedName("vendor_name")
	private String vendorName;

	@SerializedName("rate_with_tax")
	private String ratePerTax;

	@SerializedName("total_with_tax")
	private String totalRatePerTax;

	@SerializedName("purchase_order_request_component_id")
	private int orderRequestComponentId;

	public String getExpectedDeliveryDate() {
		return expectedDeliveryDate;
	}

	public void setExpectedDeliveryDate(String expectedDeliveryDate) {
		this.expectedDeliveryDate = expectedDeliveryDate;
	}

	public String getTransportationAmount() {
		return transportationAmount;
	}

	public void setTransportationAmount(String transportationAmount) {
		this.transportationAmount = transportationAmount;
	}

	public String getTotalTransportationAmount() {
		return totalTransportationAmount;
	}

	public void setTotalTransportationAmount(String totalTransportationAmount) {
		this.totalTransportationAmount = totalTransportationAmount;
	}

	@SerializedName("expected_delivery_date")
	private String expectedDeliveryDate;

	@SerializedName("transportation_amount")
	private String transportationAmount;

	@SerializedName("total_transportation_amount")
	private String totalTransportationAmount;

	@SerializedName("gst")
	private String gst;

	@SerializedName("transportation_gst")
	private String transportationGst;

	public void setRate(String rate){
		this.rate = rate;
	}

	public String getRate(){
		return rate;
	}

	public void setVendorId(int vendorId){
		this.vendorId = vendorId;
	}

	public String getGst() {
		return gst;
	}

	public void setGst(String gst) {
		this.gst = gst;
	}

	public String getTransportationGst() {
		return transportationGst;
	}

	public void setTransportationGst(String transportationGst) {
		this.transportationGst = transportationGst;
	}

	public int getVendorId(){
		return vendorId;
	}

	public void setVendorName(String vendorName){
		this.vendorName = vendorName;
	}

	public String getVendorName(){
		return vendorName;
	}

	public void setRatePerTax(String ratePerTax){
		this.ratePerTax = ratePerTax;
	}

	public String getRatePerTax(){
		return ratePerTax;
	}

	public void setTotalRatePerTax(String totalRatePerTax){
		this.totalRatePerTax = totalRatePerTax;
	}

	public String getTotalRatePerTax(){
		return totalRatePerTax;
	}

	public void setImagePurchaseOrderRequests(RealmList<ImagePurchaseOrderRequest> vendors) {
		this.imagePurchaseOrderRequests = vendors;
	}

	public RealmList<ImagePurchaseOrderRequest> getImagePurchaseOrderRequests() {
		return imagePurchaseOrderRequests;
	}

	public void setPdfPurchaseOrderRequests(RealmList<PdfPurchaseOrderRequest> pdfPurchaseOrderRequests) {
		this.pdfPurchaseOrderRequests = pdfPurchaseOrderRequests;
	}

	public RealmList<PdfPurchaseOrderRequest> getPdfPurchaseOrderRequests() {
		return pdfPurchaseOrderRequests;
	}



}