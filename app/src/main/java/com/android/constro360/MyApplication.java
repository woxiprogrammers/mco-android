package com.android.constro360;

import android.app.Application;

import com.android.utils.AppConstants;
import com.android.utils.AppUtils;
import com.androidnetworking.AndroidNetworking;

import de.jonasrottmann.realmbrowser.RealmBrowser;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import timber.log.Timber;

/**
 * <b></b>
 * <p>This class is used to </p>
 * Created by Rohit.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AndroidNetworking.initialize(getApplicationContext());
        // initialize Realm
        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name(getString(R.string.realm_database_name))
                .schemaVersion(AppConstants.PREFS_REALM_DATABASE_VERSION)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
        if (BuildConfig.DEBUG) {
            //Realm Browser Notification Shortcut
            RealmBrowser.addFilesShortcut(getApplicationContext());
            //Enable Timber Log for DebugTree
            Timber.plant(new Timber.DebugTree());
        }
        AppUtils.initialize(getApplicationContext(), "app_preferences");
//        new UCEHandler.Builder(this).build();
    }
}