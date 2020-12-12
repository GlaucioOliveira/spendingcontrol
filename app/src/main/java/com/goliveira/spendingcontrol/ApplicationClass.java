package com.goliveira.spendingcontrol;

import android.app.Application;

import com.onesignal.OneSignal;

public class ApplicationClass extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        String ONESIGNAL_APP_ID = "3bb7570d-33c4-436a-bd6e-1561c6252b0b";

        // Uncomment to enable verbose OneSignal logging to debug issues if needed.
        // OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);
    }
}
