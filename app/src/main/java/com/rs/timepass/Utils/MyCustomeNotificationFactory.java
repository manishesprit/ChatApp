package com.rs.timepass.Utils;

import android.app.Notification;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

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

//        userid,username,avatar,feedid,feedpic,meesage

        System.out.println("=====pushMessage MSG======" + pushMessage.getAlert());
        Bundle bundle = pushMessage.getPushBundle();
        System.out.println("==========Bundle=======");
        System.out.println(bundle.toString());
        System.out.println("==========End Bundle=======");

//        if (bundle != null)
//        {
//            if (Pref.getValue(getContext(), Config.PREF_USER_ID, 0) != 0 && Pref.getValue(getContext(), Config.PREF_USER_ID, 0) != Integer.parseInt(bundle.getString(Config.uid)))
//            {
//                if (bundle.getString(Config.action).equals(Config.cf))
//                {
//
//                    feedBean = new FeedBean();
//                    feedBean.id = Integer.parseInt(bundle.getString(Config.fid));
//                    feedBean.userId = Integer.parseInt(bundle.getString(Config.uid));
//                    feedBean.username = bundle.getString(Config.uname);
//                    feedBean.fileName = bundle.getString(Config.fimg);
//                    feedBean.avatar = Config.IMAGE_PATH_WEB_AVATARS + bundle.getString(Config.avatar).toString();
//                    feedBean.description = bundle.getString(Config.msg).toString();
//
//                    if (Pref.getValue(getContext(), Config.PREF_USER_ID, 0) != feedBean.userId)
//                    {
//                        ArrayList<FeedBean> oldList = Pref.getNotificationArray(getContext(), Config.PREF_NOTIFICATION_LIST, null);
//                        if (oldList == null)
//                        {
//                            oldList = new ArrayList<FeedBean>();
//                        }
//                        oldList.add(feedBean);
//                        Pref.setNotificationArray(getContext(), Config.PREF_NOTIFICATION_LIST, oldList);
//
//                        Intent notificationIntent= new Intent(getContext(), DummyNotificationActivity.class);
//                        notificationIntent.putExtra("NOTIFICATION", 1);
//                        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//                        PendingIntent intent = PendingIntent.getActivity(getContext(), 111, notificationIntent, 0);
//
//                        builder = new NotificationCompat.Builder(getContext());
//                        builder.setContentTitle(getContext().getString(R.string.app_name)).setContentText(feedBean.description)
//                                .setStyle(new NotificationCompat.BigTextStyle().bigText(feedBean.description))
//                                .setSmallIcon(R.mipmap.ic_launcher)
//                                .setAutoCancel(true)
//                                .setColor(Color.parseColor("#00000000")).setPriority(0)
//                                .setDefaults(Notification.DEFAULT_SOUND)
//                                .setContentIntent(intent).build();
//
//
//                        if (Pref.getValue(getContext(), Config.PREF_IS_HOME_RUNNING, 0) == 1)
//                        {
//                            Intent notificationIntent1 = new Intent(getContext(), DummyNotificationActivity.class);
//                            notificationIntent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            getContext().startActivity(notificationIntent1);
//                        }
//                    }
//
//                }
//            }
//        }

        builder.extend(createNotificationActionsExtender(pushMessage, 0));

        return builder.build();
    }

    @Override
    public int getNextId(@NonNull PushMessage pushMessage) {
        return 0;
    }


}
