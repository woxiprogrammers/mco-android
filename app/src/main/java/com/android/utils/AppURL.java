package com.android.utils;

import com.android.constro360.BuildConfig;

/**
 * <b></b>
 * <p>This class is used to </p>
 * Created by Rohit.
 */
public class AppURL {
    private static final String BASE_URL = BuildConfig.BASE_URL;
    public static final String API_PURCHASE_REQUEST_LIST = BASE_URL + "purchase/purchase-request/listing?token=";
    public static final String API_USER_LOGIN = BASE_URL + "login";
    public static final String API_USER_LOGOUT = BASE_URL + "logout?token=";
    public static final String API_USER_DASHBOARD = BASE_URL + "dashboard?token=";
    public static final String API_PURCHASE_ORDER_LIST = BASE_URL + "purchase/purchase-order/listing?token=";
    public static final String API_PURCHASE_BILL_LIST = BASE_URL + "purchase/purchase-order/transaction-listing?token=";
    public static final String API_MATERIAL_LISTING_URL = BASE_URL + "inventory/material/listing?token=";
    public static final String API_ASSETS_DATA_URL = BASE_URL + "inventory/asset/listing?token=";
    public static final String API_REQUEST_USERS_WITH_APPROVE_ACL = BASE_URL + "users/purchase/purchase-request/approval-acl?token=";
    public static final String API_CLOSE_PURCHASE_ORDER = BASE_URL + "purchase/purchase-order/change-status?token=";
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
    public static final String API_ASSET_REQUEST_MAINTENANCE = BASE_URL + "inventory/asset/maintenance-request/create?token=";
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
    public static final String API_PURCHASE_ORDER_PAYMENT_URL = BASE_URL + "purchase/purchase-order/create-transaction?token=";
    public static final String API_GET_CHECKPOINTS_URL = BASE_URL + "checklist/checkpoint-listing?token=";
    public static final String API_CHECKLIST_SUBMIT_CHECKPOINT_INFO = BASE_URL + "checklist/save-checkpoint-detail?token=";
    public static final String API_PURCHASE_ORDER_MATERIAL_DETAIL = BASE_URL + "purchase/purchase-order/detail?token=";
    public static final String API_AWARENES_CATEGORY_DATA = BASE_URL + "awareness/get-main-categories?token=";
    public static final String API_AWARENES_SUB_CATEGORY_DATA = BASE_URL + "awareness/get-sub-categories?token=";
    public static final String API_AWARENES_LISTING = BASE_URL + "awareness/listing?token=";
    public static final String API_CHECKLIST_ASSIGNED_LIST = BASE_URL + "checklist/listing?token=";
    public static final String API_CHECKLIST_FLOOR_LIST = BASE_URL + "checklist/floor?token=";
    public static final String API_CHECKLIST_TITLE_LIST = BASE_URL + "checklist/title?token=";
    public static final String API_CHECKLIST_SUBMIT_REQUEST = BASE_URL + "checklist/assign?token=";
    public static final String API_CHECKLIST_USERS_WITH_ACL = BASE_URL + "checklist/get-user-with-assign-acl?token=";
    //////Drawing
    public static final String API_DRAWING_CATEGORY_DATA = BASE_URL + "drawing/get-main-categories?token=";
    public static final String API_DRAWING_SUB_CAT_DATA = BASE_URL + "drawing/get-sub-categories?token=";
    public static final String API_IMAGE_LIST_DRAWING = BASE_URL + "drawing/get-current-version-images?token=";
    public static final String API_DRAWING_COMMENTS_LIST = BASE_URL + "drawing/get-comments?token=";
    public static final String API_DRAWING_ADD_COMMENT = BASE_URL + "drawing/add-comment?token=";
    public static final String API_CHECKLIST_CHANGE_STATUS = BASE_URL + "checklist/change-status?token=";
    public static final String API_DRAWING_VERSIONS_LIST = BASE_URL + "drawing/get-all-image-versions?token=";
    public static final String API_GET_MISCELLANEOUS_CATEGORIES = BASE_URL + "system-miscellaneous-categories";
    public static final String API_REASSIGN_CHECKLIST_CHECKPOINTS = BASE_URL + "checklist/recheck-checkpoint?token=";
    public static final String API_GET_PARENTS_CHECKPOINTS = BASE_URL + "checklist/get-parent?token=";
    public static final String API_SEND_FIREBASE_REFRESHED_TOKEN = BASE_URL + "notification/store-fcm-token?token=";
    public static final String API_REQUEST_COMPONENT_LIST = BASE_URL + "inventory/request-component-listing?token=";
    public static final String API_CHANGE_STATUS_INVENTORY_APPROVE = BASE_URL + "inventory/change-status?token=";
    public static final String API_INVENTORY_COMPONENT_AUTO_SUGGEST = BASE_URL + "inventory/component/auto-suggest?token=";
    public static final String API_MATERIAL_REQUEST_HISTORY = BASE_URL + "purchase/get-history?token=";
    public static final String API_SALARY_VIEW_PAYMENT = BASE_URL + "peticash/employee-salary/calulate-payable-amount?token=";
    public static final String API_INVENTORY_GET_GRN_DETAILS = BASE_URL + "inventory/component/get-grn-details?token=";
    public static final String API_DPR_SUB_CONTRACTOR_DATA = BASE_URL + "dpr/subcontractor/listing?token=";
    public static final String API_DPR_SUBCONTRACTOR_CATEGORY_DATA = BASE_URL + "dpr/category/listing?token=";
    public static final String API_NOTIFICATION_DASHBOARD_COUNTS = BASE_URL + "notification/get-counts?token=";
    public static final String API_DPR_SUBCON_SAVE_DETAILS = BASE_URL + "dpr/subcontractor/save-details?token=";
    public static final String API_DPR_LISTING = BASE_URL + "dpr/subcontractor/dpr-detail-listing?token=";
    public static final String API_GENERATE_GRN_ASSET_MAINT = BASE_URL + "inventory/asset/maintenance-request/transaction/generate-grn?token=";
    public static final String API_SUBMIT_MAINENANCE = BASE_URL + "inventory/asset/maintenance-request/transaction/create?token=";
    public static final String API_PURCHASE_ORDER_REQUEST_LIST = BASE_URL + "purchase/purchase-order-request/listing?token=";
    public static final String API_PURCHASE_ORDER_REQUEST_DETAILS_LIST = BASE_URL + "purchase/purchase-order-request/detail?token=";
    public static final String API_PURCHASE_ORDER_REQUEST_CHANGE_STATUS = BASE_URL + "purchase/purchase-order-request/change-status?token=";
    public static final String API_ASSET_MAINTENANCE_REQUEST_LISTING = BASE_URL + "inventory/asset/maintenance-request/listing?token=";
    public static final String API_PETICASH_SALARY_GENERATE_VOUCHER = BASE_URL + "peticash/employee-salary/generate-payment-voucher?token=";
    public static final String API_PETICASH_SALARY_DELETE_VOUCHER = BASE_URL + "peticash/employee-salary/delete-payment-voucher?token=";

    public static final String API_SITE_COUNT_URL = BASE_URL+"notification/project-site/get-count?token=";
    public static final String API_GRN_URL=BASE_URL + "inventory/get-site-out-GRN?token=";
    public static final String API_REQUEST_GENRATE_GRN_SITE_IN = BASE_URL +"inventory/generate-grn?token=";
    public static final String API_SUBMIT_SITE_IN_NEW = BASE_URL + "inventory/post-grn-transfer?token=";
    public static final String API_BANK_INFO_URL=BASE_URL+ "system-banks?token=";
    public static final String API_GET_VERSION_URL=BASE_URL + "app-version";
    public static final String PLAYSTORE_APP_URL="https://play.google.com/store/apps/details?id=com.mcon.android";
    public static final String API_PURCHASE_ORDER_REQUEST_DISAPPROVE=BASE_URL + "purchase/purchase-order-request/disapprove-component?token=";
    public static final String API_CHECK_PO_PASSWORD = BASE_URL + "purchase/purchase-order/authenticate-purchase-order-close?token=";
}
