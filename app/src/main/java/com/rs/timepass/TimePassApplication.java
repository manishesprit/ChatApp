package com.rs.timepass;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.rs.timepass.Utils.MyCustomeNotificationFactory;
import com.urbanairship.UAirship;

import java.io.File;

/**
 * Class that stores global application state
 */
public class TimePassApplication extends Application {
    private static final String TAG = TimePassApplication.class
            .getSimpleName();
    private RequestQueue mRequestQueue = null;
    private static TimePassApplication mInstance = null;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        UAirship.takeOff(this, new UAirship.OnReadyCallback() {
            @Override
            public void onAirshipReady(UAirship uAirship) {
                uAirship.shared().getPushManager().setUserNotificationsEnabled(true);
            }
        });

        String channelId = UAirship.shared().getPushManager().getChannelId();
        System.out.println("============My Application Channel ID: ============" + channelId);

        MyCustomeNotificationFactory myCustomeNotificationFactory = new MyCustomeNotificationFactory(this);
        UAirship.shared().getPushManager().setNotificationFactory(myCustomeNotificationFactory);

    }

    public static synchronized TimePassApplication getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public File getCacheDir() {
        // NOTE: this method is used in Android 2.2 and higher
        File cachePath = getExternalCachePath();
        return cachePath != null ? cachePath : super.getCacheDir();
    }

    private File getExternalCachePath() {
        try {
            File savePath = new File(getFilesDir() + "/timepasscache");
            if (!savePath.exists()) {
                savePath.mkdirs();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        File extStorageAppCachePath = new File(getFilesDir(), "timepasscache");
        return extStorageAppCachePath;
    }
}