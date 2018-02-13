package com.android.firebase;

import android.text.TextUtils;
import android.widget.Toast;

import com.android.utils.AppConstants;
import com.android.utils.AppUtils;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import timber.log.Timber;

/**
 * <b></b>
 * <p>This class is used to </p>
 * Created by Rohit.
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Timber.d("Refreshed token: " + refreshedToken);
        Toast.makeText(getBaseContext(), "FCM Token Refreshed", Toast.LENGTH_SHORT).show();
        AppUtils.getInstance().put(AppConstants.PREFS_FIREBASE_REFRESH_TOKEN, refreshedToken);
        boolean isLoggedIn = AppUtils.getInstance().getBoolean(AppConstants.PREFS_IS_LOGGED_IN, false);
        if (isLoggedIn && !TextUtils.isEmpty(AppUtils.getInstance().getCurrentToken())) {
            AppUtils.getInstance().sendRegistrationToServer();
        }
    }
}
