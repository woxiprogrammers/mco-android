package com.android.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.constro360.BuildConfig;
import com.android.constro360.R;
import com.android.login_mvp.login_model.LoginResponse;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import io.realm.Realm;
import timber.log.Timber;

/**
 * <b></b>
 * <p>This class is used to </p>
 * Created by Rohit.
 */
public class AppUtils {
    public static AppUtils instance;
    private static Context mContext;
    private static SharedPreferences preference;
    private static SharedPreferences.Editor editor;
    private final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9+._%-+]{1,256}" +
                    "@" +
                    "[a-zA-Z0-9][a-zA-Z0-9-]{0,64}" +
                    "(" +
                    "." +
                    "[a-zA-Z0-9][a-zA-Z0-9-]{0,25}" +
                    ")+"
    );
    private View view;
    private ProgressBar progressBar;
    private String strToken = "";
    private String strLoggedInAt;
    private String strUserRole;
    private int intCurrentUserId;

    /**
     * initialize utils plus library
     *
     * @param context
     * @param prefs_name
     */
    public static void initialize(Context context, String prefs_name) {
        if (prefs_name == null) {
            throw new IllegalArgumentException("Preference name can not be null");
        }
        if (prefs_name.isEmpty()) {
            throw new IllegalArgumentException("Preference name can not be empty");
        }
        if (context == null) {
            throw new IllegalArgumentException("Preference name can not be null");
        }
        mContext = context;
        preference = context.getSharedPreferences(prefs_name, Context.MODE_PRIVATE);
        editor = preference.edit();
    }

    /**
     * initialize Utils Plus Library
     *
     * @param context
     */
    public static void initialize(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("Preference name can not be null");
        }
        String DEFAULT_SHARED_PREFS_NAME = "app_preferences";
        preference = context.getSharedPreferences(DEFAULT_SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        editor = preference.edit();
        mContext = context;
    }

    public AppUtils put(String key, Object obj) {
        // if(obj instanceof Float)
        if (obj instanceof String) {
            editor.putString(key, (String) obj);
            editor.commit();
            return this;
        } else if (obj instanceof Boolean) {
            editor.putBoolean(key, (Boolean) obj);
            editor.commit();
            return this;
        } else if (obj instanceof Integer) {
            editor.putInt(key, (Integer) obj);
            editor.commit();
            return this;
        } else if (obj instanceof Long) {
            editor.putLong(key, (Long) obj);
            editor.commit();
            return this;
        } else if (obj instanceof Float) {
            editor.putFloat(key, (Float) obj);
            editor.commit();
            return this;
        } else {
            throw new ClassCastException(obj.getClass().getName() + " is not allowed type of object.");
        }
    }

    public AppUtils insertStringSet(String key, Set<String> values) {
        editor.putStringSet(key, values);
        return this;
    }

    public Set<String> retrievetStringSet(String key, Set<String> defaultValue) {
        return preference.getStringSet(key, defaultValue);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return preference.getBoolean(key, defaultValue);
    }

    public float getFloat(String key, float defaultValue) {
        return preference.getFloat(key, defaultValue);
    }

    public long getLong(String key, long defaultValue) {
        return preference.getLong(key, defaultValue);
    }

    /**
     * removes specified preference using its name
     *
     * @param key name of the key
     * @return object
     */
    public AppUtils removeKey(String key) {
        editor.remove(key);
        editor.commit();
        return this;
    }

    /**
     * this utlity method clears all preferences
     *
     * @return object
     */
    public AppUtils clear() {
        editor.clear();
        editor.commit();
        return this;
    }

    /**
     * validates email
     *
     * @param email email address
     * @return true or false.
     */
    public boolean checkEmailIsValid(String email) {
        if (email != null && !email.isEmpty()) {
            return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
        } else {
            Log.e("Utils Plus", "email can not be null or empty.");
            return false;
        }
    }

    public void logRealmExecutionError(Throwable error) {
        Timber.d("RealmExecutionError: " + error.getMessage());
    }

    public void loadImageViaGlide(String strUrl, ImageView imageView, Context mContext) {
        //ToDo Imp Sharvari unComment
        Glide.with(mContext).load(BuildConfig.BASE_URL_MEDIA + strUrl)
                .thumbnail(0.1f)
                .crossFade()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);
    }

    public void hideKeyboard(View view, Context context, EditText EditText, boolean isShow) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (isShow) {
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        } else {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public Map<String, String> getApiHeaders() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json; charset=UTF-8");
        return headers;
    }

    public void hideKeyBoard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public int getCurrentSiteId() {
        return AppUtils.getInstance().getInt("projectId", -1);
    }

    public int getInt(String key, int defaultValue) {
        return preference.getInt(key, defaultValue);
    }

    public static synchronized AppUtils getInstance() {
        if (instance == null) {
            return instance = new AppUtils();
        }
        return instance;
    }

    public String getLoggedInAt() {
        Realm realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    LoginResponse loginResponse = realm.where(LoginResponse.class).findFirst();
                    strLoggedInAt = loginResponse.getLoggedInAt().getDate();
                }
            });
        } catch (Exception e) {
            Timber.d(e.getMessage());
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
        return strLoggedInAt;
    }

    public String getUserRole() {
        Realm realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    LoginResponse loginResponse = realm.where(LoginResponse.class).findFirst();
                    strUserRole = loginResponse.getLoginResponseData().getUser_role();
                }
            });
        } catch (Exception e) {
            Timber.d(e.getMessage());
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
        return strUserRole;
    }

    public String getTime(String currentFormat, String expectedFormat, String currentDateTime) {
        final SimpleDateFormat df = new SimpleDateFormat(currentFormat);
        Date dateObj;
        String newDateStr = null;
        try {
            dateObj = df.parse(currentDateTime);
            SimpleDateFormat fd = new SimpleDateFormat(expectedFormat);
            newDateStr = fd.format(dateObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newDateStr;
    }

    public int getCurrentUserId() {
        Realm realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    LoginResponse loginResponse = realm.where(LoginResponse.class).findFirst();
                    intCurrentUserId = loginResponse.getLoginResponseData().getId();
                }
            });
        } catch (Exception e) {
            Timber.d(e.getMessage());
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
        return intCurrentUserId;
    }

    public void sendRegistrationToServer() {
        String refreshedToken = AppUtils.getInstance().getString(AppConstants.PREFS_FIREBASE_REFRESH_TOKEN, "");
        Timber.d("refreshedToken: " + refreshedToken);
        if (!TextUtils.isEmpty(refreshedToken)) {
            if (AppUtils.getInstance().checkNetworkState()) {
                AndroidNetworking.post(AppURL.API_SEND_FIREBASE_REFRESHED_TOKEN + AppUtils.getInstance().getCurrentToken())
                        .addBodyParameter("firebaseRefreshedToken", refreshedToken)
                        .setTag("sendRegistrationToServer")
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Timber.d(String.valueOf(response));
                            }
                            @Override
                            public void onError(ANError anError) {
                                AppUtils.getInstance().logApiError(anError, "sendRegistrationToServer");
                            }
                        });
            } else {
                AppUtils.getInstance().showOfflineMessage("MyFirebaseInstanceIDService");
            }
        } else {
            Toast.makeText(mContext, "Please UNINSTALL & then INSTALL APP for Notifications to work", Toast.LENGTH_LONG).show();
        }
    }

    public String getString(String key, String defaultValue) {
        return preference.getString(key, defaultValue);
    }

    /**
     * Checks whether there is an active network connection or not
     *
     * @return true or false
     */
    public boolean checkNetworkState() {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public String getCurrentToken() {
        Realm realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    LoginResponse loginResponse = realm.where(LoginResponse.class).findFirst();
                    if (loginResponse != null) {
                        strToken = loginResponse.getToken();
                    }
                }
            });
        } catch (Exception e) {
            Timber.d(e.getMessage());
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
        return strToken;
    }

    public void logApiError(ANError anError, String strApiTag) {
        if (anError.getErrorCode() != 0) {
            Timber.tag(strApiTag).d("Api errorCode : " + anError.getErrorCode());
//            Timber.tag(strApiTag).d("Api errorBody : " + anError.getErrorBody());
            Timber.tag(strApiTag).d("Api errorMessage : " + anError.getMessage());
            Timber.tag(strApiTag).d("Api errorDetail : " + anError.getErrorDetail());
        } else {
            Timber.tag(strApiTag).d("onError errorDetail : " + anError.getErrorDetail());
            Timber.tag(strApiTag).d("onError errorMessage : " + anError.getMessage());
        }
    }

    public void showOfflineMessage(String strTag) {
        Timber.tag(strTag).d("App is offline");
        Toast.makeText(mContext, "You are offline.", Toast.LENGTH_SHORT).show();
    }

    public String getVisibleStatus(String strStatus) {
        if (strStatus.equalsIgnoreCase("p-r-assigned")) {
            return "P R Assigned";
        } else if (strStatus.equalsIgnoreCase("pending")) {
            return "Pending";
        } else if (strStatus.equalsIgnoreCase("manager-approved")) {
            return "Manager Approved";
        } else if (strStatus.equalsIgnoreCase("manager-disapproved")) {
            return "Manager Disapproved";
        } else if (strStatus.equalsIgnoreCase("admin-approved")) {
            return "Admin Approved";
        } else if (strStatus.equalsIgnoreCase("admin-disapproved")) {
            return "Admin Disapproved";
        } else if (strStatus.equalsIgnoreCase("in-indent")) {
            return "In Indent";
        } else if (strStatus.equalsIgnoreCase("p-r-manager-approved")) {
            return "P R Manager Approved";
        } else if (strStatus.equalsIgnoreCase("p-r-manager-disapproved")) {
            return "P R Manager Disapproved";
        } else if (strStatus.equalsIgnoreCase("p-r-admin-approved")) {
            return "P R Admin Approved";
        } else if (strStatus.equalsIgnoreCase("p-r-admin-disapproved")) {
            return "P R Admin Disapproved";
        } else if (strStatus.equalsIgnoreCase("purchase-requested")) {
            return "Purchase Requested";
        } else if (strStatus.equalsIgnoreCase("Approved")) {
            return "Approved";
        }
        return "";
    }

    public void initializeProgressBar(ViewGroup viewGroup, Context mContext) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        view = layoutInflater.inflate(R.layout.check_progress_bar, viewGroup, false);
        view.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        progressBar = view.findViewById(R.id.progressBar);
    }

    public void showProgressBar(ViewGroup viewGroup, boolean isToShowProgress) {
        try {
            if (isToShowProgress) {
                if (viewGroup != null) {
                    viewGroup.addView(view);
                }
                progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(mContext, R.color.colorAccentDark), android.graphics.PorterDuff.Mode.MULTIPLY);
                progressBar.setVisibility(View.VISIBLE);

            } else {
                if (viewGroup != null) {
                    viewGroup.removeView(view);
                }
                if(progressBar != null){
                    progressBar.setVisibility(View.GONE);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static <T> T checkNull(String message, T object) {
        if (object == null) {
            throw new NullPointerException(message);
        }
        return object;
    }
}
