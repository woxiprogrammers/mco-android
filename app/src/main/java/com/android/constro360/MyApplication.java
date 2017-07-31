package com.android.constro360;

import android.app.Application;

import com.androidnetworking.AndroidNetworking;

import de.jonasrottmann.realmbrowser.RealmBrowser;
import io.realm.Realm;
import io.realm.RealmConfiguration;

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
        Realm.init(getApplicationContext());
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name(getString(R.string.realm_database_name))
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
        if (BuildConfig.DEBUG) {
            //Realm Browser Notification Shortcut
            RealmBrowser.addFilesShortcut(getApplicationContext());
        }
    }
}
