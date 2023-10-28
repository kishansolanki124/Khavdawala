package com.app.khavdawala;

import android.app.Application;
import android.content.Intent;

import com.app.khavdawala.ui.activity.SplashActivity;
import com.onesignal.OneSignal;
import com.onesignal.debug.LogLevel;

public class KhavdaApplication extends Application {

    private static final String ONESIGNAL_APP_ID = "f4f617d5-f961-4ea7-8c02-cc3b5f53b3e5";
    private static KhavdaApplication application;

    public static KhavdaApplication getApplication() {
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        initOneSignal();
    }

    private void initOneSignal() {
        // Enable verbose OneSignal logging to debug issues if needed.
        // Verbose Logging set to help debug issues, remove before releasing your app.
        OneSignal.getDebug().setLogLevel(LogLevel.VERBOSE);

        // OneSignal Initialization
        OneSignal.initWithContext(this, ONESIGNAL_APP_ID);

        OneSignal.getNotifications().addClickListener(iNotificationClickEvent ->

                startActivity(new Intent(application,
                        SplashActivity.class)
                        .putExtra("Notification", true)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        );

//                setNotificationOpenedHandler(
//                result -> {
//                    JSONObject jsonObject = result.getNotification().getAdditionalData();
//                    //try {
//                    if (null != jsonObject.opt(AppConstants.ID)) {
//                        startActivity(new Intent(this, NewsDetailsActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                                .putExtra(AppConstants.NEWS_ID, jsonObject.opt(AppConstants.ID).toString()));
//                    } else {
//                        startActivity(new Intent(this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//                    }
//                });
    }
}