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
    public static final String API_MATERIAL_REQUEST_AVAILABLE_QUANTITY = BASE_URL + "purchase/material-request/check-available-quantity?token=";
    ////////Peticash
    public static final String API_TRANSACTION_STATS = BASE_URL + "peticash/statistics?token=";
    public static final String API_PETICASH_LIST = BASE_URL + "peticash/transaction/listing?token=";
    public static final String API_AUTO_SUGGEST_FOR_PETICASH_EMPLOYEE = BASE_URL + "peticash/employee-salary/auto-suggest";
    public static final String API_CREATE_SALARY_FOR_EMPLOYEE = BASE_URL + "peticash/employee-salary/create?token=";
    public static final String API_EMP_TRANSATIONS = BASE_URL + "peticash/employee-salary/employee-detail?token=";
    public static final String API_GENERATE_GRN_PETICASH = BASE_URL + "peticash/purchase/create?token=";
    public static final String API_PURCHASE_PETICASH_TRANSACTION_DETAIL = BASE_URL + "peticash/purchase/transaction-detail?token=";
    public static final String API_PETICASH_EMP_SALARY_TRANS_DETAILS = BASE_URL + "peticash/employee-salary/transaction-detail?token=";
    //Inventory
    public static final String API_ASSET_SUMMARY_LIST_URL = BASE_URL + "inventory/asset/summary-listing?token=";
    public static final String API_ASSET_READINGS_DAY_MONTHWISE_LIST_URL = BASE_URL + "inventory/asset/readings/listing?token=";
    public static final String API_CREATE_FUEL_READING = BASE_URL + "inventory/asset/readings/add?token=";
    public static final String API_PETICASH_BILL_PAYMENT = BASE_URL + "peticash/purchase/bill-payment?token=";
    public static final String API_GET_SYSTEM_SITES = BASE_URL + "system-project-sites";
    public static final String API_SYSTEM_UNITS = BASE_URL + "system-units";
    public static final String API_CHECKLIST_CATEGORY_LIST = BASE_URL + "checklist/category?token=";
    public static final String API_INVENTORY_CHECK_AVAILABLE_FOR_UNITS = BASE_URL + "inventory/material/check-availability?token=";
    public static final String API_REQUEST_GENRATE_GRN_PURCHASE_ORDER_PAY = BASE_URL + "purchase/purchase-order/generate-grn?token=";
    public static final String API_PURCHASE_ORDER_PAYMENT_URL = BASE_URL + "purchase/purchase-order/bill-transaction?token=";
    public static final String API_GET_CHECKPOINTS_URL = BASE_URL + "checklist/checkpoint-listing?token=";
    public static final String API_CHECKLIST_SUBMIT_CHECKPOINT_STATUS = BASE_URL + "";
    public static final String API_PURCHASE_ORDER_MATERIAL_DETAIL = BASE_URL + "purchase/purchase-order/detail?token=";
    public static final String API_AWARENES_CATEGORY_DATA = BASE_URL + "awareness/get-main-categories?token=";
    public static final String API_AWARENES_SUB_CATEGORY_DATA = BASE_URL + "awareness/get-sub-categories?token=";
    public static final String API_AWARENES_LISTING = BASE_URL + "awareness/listing?token=";
    //    public static final String API_CHECKLIST_ASSIGNED_LIST = "http://www.mocky.io/v2/5a17cb0a2c00000714596c11";
    public static final String API_CHECKLIST_ASSIGNED_LIST = BASE_URL + "checklist/listing?token=";
    public static final String API_CHECKLIST_FLOOR_LIST = BASE_URL + "checklist/floor?token=";
    public static final String API_CHECKLIST_TITLE_LIST = BASE_URL + "checklist/title?token=";
    public static final String API_CHECKLIST_SUBMIT_REQUEST = BASE_URL + "checklist/assign?token=";
    public static final String API_CHECKLIST_USERS_WITH_ACL = BASE_URL + "checklist/get-user-with-assign-acl?token=";
    //////Drawing
    public static final String API_DRAWING_CATEGORY_DATA = BASE_URL + "drawing/get-main-categories?token=";
    public static final String API_DRAWING_SUB_CAT_DATA = BASE_URL + "drawing/get-sub-categories?token=";
    public static final String API_IMAGE_LIST_DRAWING = BASE_URL + "drawing/get-current-version-images?token=";
    public static final String API_DRAWING_COMMENTS_LIST = "http://www.mocky.io/v2/5a1e8d002f0000b622ee2e0a";
    public static final String API_DRAWING_ADD_COMMENT = BASE_URL + "drawing/add-comment?token=";
}
