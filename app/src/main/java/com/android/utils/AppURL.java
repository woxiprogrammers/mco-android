package com.android.utils;

import com.android.constro360.BuildConfig;

/**
 * <b></b>
 * <p>This class is used to </p>
 * Created by Rohit.
 */
public class AppURL {
    public static final String API_PURCHASE_ORDER = "http://www.mocky.io/v2/59ba5d100f000094016227ea";
    //http://www.mocky.io/v2/59a67e10100000e30408fc23
    private static final String BASE_URL = BuildConfig.BASE_URL;
    public static final String API_PURCHASE_REQUEST_LIST = BASE_URL + "purchase/purchase-request/listing?token=";
    public static final String API_USER_LOGIN = BASE_URL + "login";
    public static final String API_USER_DASHBOARD = BASE_URL + "dashboard?token=";
    //    public static final String API_USER_LOGIN = "http://www.mocky.io/v2/599c5b5729000020012110ad";
    public static final String API_PURCHASE_ORDER_LIST = BASE_URL + "purchase/purchase-order/listing?token=";
    public static final String API_PURCHASE_BILL_LIST = BASE_URL + "purchase/purchase-order/bill-listing?token=";
    public static final String API_MATERIAL_LISTING_URL = BASE_URL + "inventory/material/listing?token=";
    public static final String API_ASSETS_DATA_URL = BASE_URL + "inventory/asset/listing?token=";
    public static final String API_ASSET_SUMMARY_LIST_URL = BASE_URL + "inventory/asset/summary-listing?token=";
    //public static final String API_REQUEST_USERS_WITH_APPROVE_ACL = "http://www.mocky.io/v2/59bb7e190f00009101ff85bb";
    public static final String API_REQUEST_USERS_WITH_APPROVE_ACL = BASE_URL + "users/purchase/purchase-request/approval-acl?token=";
    //Purchase Details
    public static final String API_PURCHASE_SUMMARY = BASE_URL + "purchase/purchase-request/detail-listing?token=";
    public static final String API_SUBMIT_MATERIAL_REQUEST = BASE_URL + "purchase/material-request/create?token=";
    public static final String API_PURCHASE_MATERIAL_UNITS_IMAGES_URL = BASE_URL + "purchase/purchase-order/material-listing?token=";
    public static final String API_AUTO_SUGGEST_COMMON = BASE_URL + "auto-suggest";
    public static final String API_CHANGE_STATUS_MATERIAL = BASE_URL + "purchase/material-request/change-status?token=";
    public static final String API_REQUESTED_MATERIAL_LIST = BASE_URL + "purchase/material-request/listing?token=";
    public static final String API_SUBMIT_PURCHASE_REQUEST = BASE_URL + "purchase/purchase-request/create?token=";
    public static final String API_IMAGE_UPLOAD_INDEPENDENT = BASE_URL + "save-image?token=";
    public static final String API_PURCHASE_REQUEST_CHANGE_STATUS = BASE_URL + "purchase/purchase-request/change-status?token=";
    public static final String API_ASSET_REQUEST_MAINTENANCE = BASE_URL + "inventory/asset/request-maintenance?token=";
    public static final String API_MATERIAL_MOVE_IN_OUT = BASE_URL + "inventory/create-transfer?token=";
    public static final String API_PURCHASE_ORDER_BILL_TRANSACTION = BASE_URL + "purchase/purchase-order/bill-transaction?token=";
    public static final String API_PURCHAE_ORDER_BILL_PAYMENT = BASE_URL + "purchase/purchase-order/bill-payment?token=";
    public static final String API_PURCHASE_ORDER_BILL_EDIT = BASE_URL + "purchase/purchase-order/edit-bill-transaction?token=";
    public static final String API_MATERIAL_REQUEST_AVAILABLE_QUANTITY=BASE_URL + "purchase/material-request/check-available-quantity?token=";
    public static final String API_AUTO_SUGGEST_FOR_PETICASH_EMPLOYEE=BASE_URL + "peticash/employee-salary/auto-suggest";
}
