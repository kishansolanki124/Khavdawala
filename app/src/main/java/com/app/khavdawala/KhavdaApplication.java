package com.app.khavdawala;

import android.app.Application;

public class KhavdaApplication extends Application {

    //private static final String ONESIGNAL_APP_ID = "68f70451-9f1d-4101-a3e1-bbbfe5f58ac1";
    private static KhavdaApplication application;

    public static KhavdaApplication getApplication() {
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;

//        // Setting timeout globally for the download network requests:
//        PRDownloaderConfig config = PRDownloaderConfig.newBuilder()
//                //.setReadTimeout(30_000)
//                //.setConnectTimeout(30_000)
//                .build();
//        PRDownloader.initialize(getApplicationContext(), config);
        //MultiDex.install(this);
        //initOneSignal();
    }

//    private void initOneSignal() {
//        // Enable verbose OneSignal logging to debug issues if needed.
//        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
//        OneSignal.initWithContext(this);
//        OneSignal.setAppId(ONESIGNAL_APP_ID);
//
//        OneSignal.setNotificationOpenedHandler(
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
//    }
}