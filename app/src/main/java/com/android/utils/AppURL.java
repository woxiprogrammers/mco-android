package com.android.utils;

import com.android.constro360.BuildConfig;

/**
 * <b></b>
 * <p>This class is used to </p>
 * Created by Rohit.
 */
public class AppURL {
    //http://www.mocky.io/v2/59a67e10100000e30408fc23
    private static final String BASE_URL = BuildConfig.BASE_URL;
    public static final String API_PURCHASE_REQUEST_LIST = BASE_URL + "purchase/purchase-request/listing?token=";
    public static final String API_USER_LOGIN = BASE_URL + "login";
    public static final String API_USER_DASHBOARD = BASE_URL + "dashboard?token=";
    //    public static final String API_USER_LOGIN = "http://www.mocky.io/v2/599c5b5729000020012110ad";
    public static final String API_PURCHASE_ORDER_LIST = BASE_URL + "purchase/purchase-order/listing?token=";
    public static final String API_PURCHASE_BILL_LIST = "http://www.mocky.io/v2/59c23f1712000068009c0a47";
    public static final String API_INVENTORY_DATA_URL = "http://www.mocky.io/v2/599c5a5229000011012110ac";
    public static final String API_MATERIAL_LISTING_URL = BASE_URL + "inventory/material/listing?token=";
    public static final String API_ASSETS_DATA_URL = BASE_URL + "inventory/asset/listing?token=";
    public static final String API_PURCHASE_REQUEST_SUBMIT = "";
    public static final String API_ASSET_SUMMARY_LIST_URL = BASE_URL + "inventory/asset/summary-listing?token=";
    //    public static final String API_REQUEST_USERS_WITH_APPROVE_ACL = "http://www.mocky.io/v2/59bb7e190f00009101ff85bb";
    public static final String API_REQUEST_USERS_WITH_APPROVE_ACL = BASE_URL + "users/purchase/purchase-request/approval-acl?token=";
    //Purchase Details
    public static final String API_PURCHASE_SUMMARY = BASE_URL + "purchase/purchase-request/detail-listing?token=";
    public static final String API_PURCHASE_ORDER = "http://www.mocky.io/v2/59ba5d100f000094016227ea";
    public static final String API_SUBMIT_MATERIAL_REQUEST = BASE_URL + "purchase/material-request/create?token=";
    public static final String API_PURCHASE_MATERIAL_UNITS_IMAGES_URL = "http://www.mocky.io/v2/59bf69611100004100fa0151";
    public static final String API_AUTO_SUGGEST_COMMON = BASE_URL + "auto-suggest";
    public static final String API_CHANGE_STATUS_MATERIAL = BASE_URL + "purchase/material-request/change-status?token=";
    public static final String API_REQUESTED_MATERIAL_LIST = BASE_URL + "purchase/material-request/listing?token=";
    public static final String API_SUBMIT_PURCHASE_REQUEST = BASE_URL + "purchase/purchase-request/create?token=";
    public static final String API_IMAGE_UPLOAD_INDEPENDENT = BASE_URL + "save-image?token=";
    public static final String API_PURCHASE_REQUEST_CHANGE_STATUS= BASE_URL + "purchase/purchase-request/change-status?token=";
    public static final String API_ASSET_REQUEST_MAINTENANCE =BASE_URL + "inventory/asset/request-maintenance?token=";
    public static final String API_MATERIAL_MOVE_IN_OUT=BASE_URL + "inventory/create-transfer?token=";
}
