package com.rs.timepass.Utils;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.urbanairship.push.BaseIntentReceiver;
import com.urbanairship.push.PushMessage;

/**
 * Created by admin on 4/6/16.
 */
public class PushNotificationReceiver extends BaseIntentReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        System.out.println(" ==============BUNDLE==========================");
        System.out.println(" Bundle:::" + intent.getStringExtra("PushManager.EXTRA_STRING_EXTRA"));
        System.out.println(" ==============================================");

        super.onReceive(context, intent);

    }

    @Override
    protected void onChannelRegistrationSucceeded(@NonNull Context context, @NonNull String s) {

    }

    @Override
    protected void onChannelRegistrationFailed(@NonNull Context context) {

    }

    @Override
    protected void onPushReceived(@NonNull Context context, @NonNull PushMessage pushMessage, int i) {

        System.out.println("===========pushMessage.getAlert()==========" + pushMessage.getAlert());

        System.out.println("=======GET ACTION SIZE======="+pushMessage.getActions().size());

    }

    @Override
    protected void onBackgroundPushReceived(@NonNull Context context, @NonNull PushMessage pushMessage) {

    }

    @Override
    protected boolean onNotificationOpened(@NonNull Context context, @NonNull PushMessage pushMessage, int i) {

//        Intent intent=new Intent(context, ProfileActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(intent);
//        System.out.println("Oepn Apps");

        return false;
    }

    @Override
    protected boolean onNotificationActionOpened(@NonNull Context context, @NonNull PushMessage pushMessage, int i, @NonNull String s, boolean b) {
        return false;
    }
}

