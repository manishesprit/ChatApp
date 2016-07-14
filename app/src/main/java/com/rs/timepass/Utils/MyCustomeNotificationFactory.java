package com.rs.timepass.Utils;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.rs.timepass.R;
import com.rs.timepass.Ui.HomeActivity;
import com.urbanairship.push.PushMessage;
import com.urbanairship.push.notifications.NotificationFactory;
import com.urbanairship.util.UAStringUtil;

/**
 * Created by hardikjani on 6/10/16.
 */
public class MyCustomeNotificationFactory extends NotificationFactory {

    //    private FeedBean feedBean;
//    private ViewLiteAPI viewLiteAPI;
    private NotificationCompat.Builder builder;

    public MyCustomeNotificationFactory(Context context) {
        super(context);
    }

    @Nullable
    @Override
    public Notification createNotification(@NonNull PushMessage pushMessage, int i) {

        if (UAStringUtil.isEmpty(pushMessage.getAlert())) {
            return null;
        }

        System.out.println("=====pushMessage MSG======" + pushMessage.getAlert());
        Bundle bundle = pushMessage.getPushBundle();
        System.out.println("==========Bundle=======");
        System.out.println(bundle.toString());
        System.out.println("==========End Bundle=======");

        if (Pref.getValue(getContext(), Config.PREF_USER_ID, 0) != 0) {

            Intent notificationIntent = new Intent(getContext(), HomeActivity.class);
            notificationIntent.putExtra("NOTIFICATION", 1);
            notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent intent = PendingIntent.getActivity(getContext(), 111, notificationIntent, 0);
            builder = new NotificationCompat.Builder(getContext());
            builder.setContentTitle(getContext().getString(R.string.app_name)).setContentText(pushMessage.getAlert())
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(pushMessage.getAlert()))
                    .setSmallIcon(R.drawable.large_icon)
                    .setAutoCancel(true)
                    .setColor(Color.parseColor("#00000000")).setPriority(0)
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setContentIntent(intent).build();
        }
        builder.extend(createNotificationActionsExtender(pushMessage, 0));

        return builder.build();
    }

    @Override
    public int getNextId(@NonNull PushMessage pushMessage) {
        return 0;
    }


}
