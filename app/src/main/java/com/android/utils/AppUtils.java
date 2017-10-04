package com.android.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.android.models.login_acl.LoginResponse;
import com.androidnetworking.error.ANError;

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
    public static Context mContext;
    private String strToken;
    private String strLoggedInAt;
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
        preference = context.getSharedPreferences(prefs_name, context.MODE_PRIVATE);
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
        preference = context.getSharedPreferences(DEFAULT_SHARED_PREFS_NAME, context.MODE_PRIVATE);
        editor = preference.edit();
        mContext = context;
    }

    public static synchronized AppUtils getInstance() {
        if (instance == null) {
            return instance = new AppUtils();
        }
        return instance;
    }

    private static <T> T checkNull(String message, T object) {
        if (object == null) {
            throw new NullPointerException(message);
        }
        return object;
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

    public String getString(String key, String defaultValue) {
        return preference.getString(key, defaultValue);
    }

    public int getInt(String key, int defaultValue) {
        return preference.getInt(key, defaultValue);
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
     * Checks whether there is an active network connection or not
     *
     * @return true or false
     */
    public boolean checkNetworkState() {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
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
        Toast.makeText(mContext, "API Error: " + anError.getErrorDetail(), Toast.LENGTH_SHORT).show();
    }

    public void logRealmExecutionError(Throwable error) {
        Timber.d("RealmExecutionError: " + error.getMessage());
    }

    public void showOfflineMessage(String strTag) {
        Timber.tag(strTag).d("App is offline");
        Toast.makeText(mContext, "You are offline.", Toast.LENGTH_SHORT).show();
    }

    public void hideKeyboard(View view, Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public String getCurrentToken() {
        Realm realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    LoginResponse loginResponse = realm.where(LoginResponse.class).findFirst();
                    strToken = loginResponse.getToken();
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
        return 2;
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
}
