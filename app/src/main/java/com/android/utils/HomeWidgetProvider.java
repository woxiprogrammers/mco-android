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
import com.android.dashboard.NotificationCountResponseSite;
import com.android.dashboard.ProjectsNotificationCountItem;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;

import timber.log.Timber;

/**
 * Created by RohitSS on 23-02-2018.
 */
public class HomeWidgetProvider extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Toast.makeText(context, "Hi", Toast.LENGTH_SHORT).show();
//        final int count = appWidgetIds.length;
        /*for (int widgetId : appWidgetIds) {
            getTotalNotificationCount(context);
//            String number = String.format("%d", 1*//*(new Random().nextInt(900) + 100)*//*);
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.layout_home_widget);
//            remoteViews.setTextViewText(R.id.textView_widget_count, number);
            Intent intent = new Intent(context, SplashActivity.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                    0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.frameLayout_home_widget, pendingIntent);
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
            //
        }*/
//        final int N = appWidgetIds.length;
        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int appWidgetId : appWidgetIds) {
            // Create an Intent to launch ExampleActivity
            // Get the layout for the App Widget and attach an on-click listener
            // to the button
            // Tell the AppWidgetManager to perform an update on the current app widget
            getTotalNotificationCount(context, appWidgetId, appWidgetManager);
        }
    }

    private void getTotalNotificationCount(final Context context, final int appWidgetId, final AppWidgetManager appWidgetManager) {
        AndroidNetworking.post(AppURL.API_SITE_COUNT_URL + AppUtils.getInstance().getCurrentToken())
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setTag("getTotalNotificationCount")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObject(NotificationCountResponseSite.class, new ParsedRequestListener<NotificationCountResponseSite>() {
                    @Override
                    public void onResponse(NotificationCountResponseSite response) {
                        int notificationCount = 0;
                        try {
                            for (ProjectsNotificationCountItem projectsNotificationCountItem :
                                    response.getNotificationCountData().getProjectsNotificationCount()) {
                                notificationCount = notificationCount + projectsNotificationCountItem.getNotificationCount();
                            }
                        } catch (Exception e) {
                            Timber.e("Exception Occurred: ", e);
                        }
                        try {
                            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.layout_home_widget);
                            Intent intent = new Intent(context, SplashActivity.class);
                            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
                            String number = String.format("%d", notificationCount);
                            remoteViews.setTextViewText(R.id.textView_widget_count, number);
                            remoteViews.setOnClickPendingIntent(R.id.frameLayout_home_widget, pendingIntent);
                            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
                        } catch (Exception e) {
                            Timber.e("Exception Occurred: ", e);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        AppUtils.getInstance().logApiError(anError, "getTotalNotificationCount");
                    }
                });
    }
}