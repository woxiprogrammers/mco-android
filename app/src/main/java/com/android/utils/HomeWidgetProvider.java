package com.android.utils;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.android.constro360.R;
import com.android.constro360.SplashActivity;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONObject;

import java.util.Random;

import timber.log.Timber;

/**
 * Created by RohitSS on 23-02-2018.
 */
public class HomeWidgetProvider extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Toast.makeText(context, "Hi", Toast.LENGTH_SHORT).show();
//        final int count = appWidgetIds.length;
        for (int widgetId : appWidgetIds) {
            String number = String.format("%03d", (new Random().nextInt(900) + 100));
            getTotalNotificationCount();
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.layout_home_widget);
            remoteViews.setTextViewText(R.id.textView_widget_count, number);
            Intent intent = new Intent(context, SplashActivity.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                    0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.frameLayout_home_widget, pendingIntent);
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }

    private void getTotalNotificationCount() {
        AndroidNetworking.get(AppURL.API_SITE_COUNT_URL + AppUtils.getInstance().getCurrentToken())
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setTag("getTotalNotificationCount")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Timber.d(String.valueOf(response));
                    }

                    @Override
                    public void onError(ANError anError) {
                        AppUtils.getInstance().logApiError(anError, "getTotalNotificationCount");
                    }
                });
    }
}