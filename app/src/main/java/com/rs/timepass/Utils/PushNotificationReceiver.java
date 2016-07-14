package com.rs.timepass.Utils;

import android.content.Context;
import android.support.annotation.NonNull;

import com.urbanairship.push.BaseIntentReceiver;
import com.urbanairship.push.PushMessage;

/**
 * Created by admin on 4/6/16.
 */
public class PushNotificationReceiver extends BaseIntentReceiver {


    @Override
    protected void onChannelRegistrationSucceeded(@NonNull Context context, @NonNull String s) {
        Log.print("=============onChannelRegistrationSucceeded===============");
    }

    @Override
    protected void onChannelRegistrationFailed(@NonNull Context context) {

    }

    @Override
    protected void onPushReceived(@NonNull Context context, @NonNull PushMessage pushMessage, int i) {
        Log.print("=============onPushReceived===============");

    }

    @Override
    protected void onBackgroundPushReceived(@NonNull Context context, @NonNull PushMessage pushMessage) {
        Log.print("=============onBackgroundPushReceived===============");

    }

    @Override
    protected boolean onNotificationOpened(@NonNull Context context, @NonNull PushMessage pushMessage, int i) {
        Log.print("=============onNotificationOpened===============");
        return false;
    }

    @Override
    protected boolean onNotificationActionOpened(@NonNull Context context, @NonNull PushMessage pushMessage, int i, @NonNull String s, boolean b) {
        Log.print("=============onNotificationActionOpened===============");
        return false;
    }
}

