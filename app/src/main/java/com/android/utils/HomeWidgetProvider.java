package com.android.utils;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.android.constro360.R;
import com.android.constro360.SplashActivity;
import com.android.dashboard.notification_model.NotificationCountSiteResponse;
import com.android.dashboard.notification_model.ProjectsNotificationCountItem;
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
        for (int appWidgetId : appWidgetIds) {
            boolean isAlreadyLogin;
            try {
                AppUtils.getInstance().getCurrentToken();
                isAlreadyLogin = true;
            } catch (Exception e) {
                Timber.e("Exception Occurred 1: ", e);
                isAlreadyLogin = false;
            }
            if (isAlreadyLogin) {
                getTotalNotificationCount(context, appWidgetId, appWidgetManager);
            } else {
                Timber.d("Exception Occurred 4");
            }
        }
    }

    private void getTotalNotificationCount(final Context context, final int appWidgetId, final AppWidgetManager appWidgetManager) {
        AndroidNetworking.post(AppURL.API_SITE_COUNT_URL + AppUtils.getInstance().getCurrentToken())
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setTag("getTotalNotificationCount")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObject(NotificationCountSiteResponse.class, new ParsedRequestListener<NotificationCountSiteResponse>() {
                    @Override
                    public void onResponse(NotificationCountSiteResponse response) {
                        int notificationCount = 0;
                        try {
                            for (ProjectsNotificationCountItem projectsNotificationCountItem :
                                    response.getNotificationCountData().getProjectsNotificationCount()) {
                                notificationCount = notificationCount + projectsNotificationCountItem.getNotificationCount();
                            }
                        } catch (Exception e) {
                            Timber.e("Exception Occurred 2: ", e);
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
                            Timber.e("Exception Occurred 3: ", e);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        AppUtils.getInstance().logApiError(anError, "getTotalNotificationCount");
                    }
                });
    }
}