package com.rs.timepass.Utils;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.text.Html;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.rs.timepass.Bean.NotificationBean;
import com.rs.timepass.R;
import com.rs.timepass.Ui.HomeActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Belal on 5/27/2016.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private Intent intent;
    private String msg = "";
    private int notificationid;
    private ArrayList<NotificationBean> notificationBeanArrayList;
    private NotificationBean notificationBean;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Displaying data in log
        //It is optional
        System.out.println("From: " + remoteMessage.getFrom());

        //Calling method to generate notification

        Map<String, String> ss = remoteMessage.getData();
        try {
            JSONObject jsonObject = new JSONObject(remoteMessage.getData().get("data"));
            if (jsonObject != null) {
                Log.print("==data===" + jsonObject);
                sendNotification(jsonObject);
            }
        } catch (Exception e) {

        }


        //This method is only generating push notification
        //It is same as we did in earlier posts
    }

    private void sendNotification(JSONObject jsonObject) {
        try {
            if (Pref.getValue(this, Config.PREF_USER_ID, 0) != 0) {

                if (jsonObject.getInt("type") == 1) {
                    notificationBeanArrayList = Pref.getNotificationArray(this, Config.PREF_NOTIFICATION, new ArrayList<NotificationBean>());
                    notificationBean = new NotificationBean();
                    notificationBean.feedid = Integer.parseInt(jsonObject.getString("feedid"));
                    notificationBean.msg = jsonObject.getString("message");
                    notificationBean.notiid = Integer.parseInt(jsonObject.getString("feedid") + "" + 111);
                    notificationBean.avatar = jsonObject.getString("image");
                    notificationBean.type = jsonObject.getInt("type");
                    notificationBeanArrayList.add(notificationBean);
                    Pref.setNotificationArray(this, Config.PREF_NOTIFICATION, notificationBeanArrayList);

                    intent = new Intent(this, HomeActivity.class);
                    intent.putExtra("type", jsonObject.getInt("type"));

                    msg = "<b>" + jsonObject.getString("message") + "</b>" + " comment your post";
                } else if (jsonObject.getInt("type") == 2) {

                    notificationBeanArrayList = Pref.getNotificationArray(this, Config.PREF_NOTIFICATION, new ArrayList<NotificationBean>());
                    notificationBean = new NotificationBean();
                    notificationBean.userid = Integer.parseInt(jsonObject.getString("senderid"));
                    notificationBean.msg = jsonObject.getString("message");
                    notificationBean.notiid = Integer.parseInt(jsonObject.getString("senderid") + "" + 222);
                    notificationBean.avatar = jsonObject.getString("image");
                    notificationBean.type = jsonObject.getInt("type");
                    notificationBeanArrayList.add(notificationBean);
                    Pref.setNotificationArray(this, Config.PREF_NOTIFICATION, notificationBeanArrayList);


//                    PostBean postBean = new PostBean();
//                    postBean.userid = Integer.parseInt(jsonObject.getString("senderid"));
//                    notificationid = Integer.parseInt(jsonObject.getString("senderid") + "" + 222);
//                    postBean.name = jsonObject.getString("message");
//                    postBean.avatar = jsonObject.getString("image");
//                    postBean.noOfpost = 0;
//                    postBean.noOffollowers = 0;
//                    postBean.noOffollowing = 0;
//                    intent.putExtra("beanData", postBean);
                    intent = new Intent(this, HomeActivity.class);
                    intent.putExtra("type", jsonObject.getInt("type"));
                    msg = "<b>" + jsonObject.getString("message") + "</b>" + " starting follow you";
                }
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.large_icon)
                        .setContentTitle("TimePass")
                        .setContentText(Html.fromHtml(msg))
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                notificationManager.notify(notificationid, notificationBuilder.build());
            } else {
                Log.print("===Userid===Not");
            }
        } catch (Exception e) {
            Log.print("===error===" + e.toString());
        }
    }
}
