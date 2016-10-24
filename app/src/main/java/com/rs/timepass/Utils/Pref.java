package com.rs.timepass.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.rs.timepass.Bean.NotificationBean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Pref {
    private static SharedPreferences sharedPreferences = null;

    public static void openPref(Context context) {
        sharedPreferences = context.getSharedPreferences(Config.PREF_FILE,
                Context.MODE_PRIVATE);
    }

    public static String getValue(Context context, String key,
                                  String defaultValue) {
        Pref.openPref(context);
        String result = Pref.sharedPreferences.getString(key, defaultValue);
        Pref.sharedPreferences = null;
        return result;
    }

    public static void setValue(Context context, String key, String value) {
        Pref.openPref(context);
        Editor prefsPrivateEditor = Pref.sharedPreferences.edit();
        prefsPrivateEditor.putString(key, value);
        prefsPrivateEditor.commit();
        prefsPrivateEditor = null;
        Pref.sharedPreferences = null;
    }

    public static int getValue(Context context, String key, int defaultValue) {
        Pref.openPref(context);
        int result = Pref.sharedPreferences.getInt(key, defaultValue);
        Pref.sharedPreferences = null;
        return result;
    }

    public static void setValue(Context context, String key, int value) {
        Pref.openPref(context);
        Editor prefsPrivateEditor = Pref.sharedPreferences.edit();
        prefsPrivateEditor.putInt(key, value);
        prefsPrivateEditor.commit();
        prefsPrivateEditor = null;
        Pref.sharedPreferences = null;
    }

//
    public static ArrayList<NotificationBean> getNotificationArray(Context context, String key, ArrayList<NotificationBean> defaultValue) {
        Pref.openPref(context);
        ArrayList<NotificationBean> set = null;
        try {
            set = (ArrayList<NotificationBean>) ObjectSerializer
                    .deserialize(Pref.sharedPreferences.getString(key,
                            ObjectSerializer.serialize(defaultValue)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Pref.sharedPreferences = null;
        return set;
    }

    public static void setNotificationArray(Context context, String key, ArrayList<NotificationBean> value) {
        Pref.openPref(context);
        Editor prefsPrivateEditor = Pref.sharedPreferences.edit();
        ArrayList<NotificationBean> s = new ArrayList<NotificationBean>();
        s.addAll(value);

        try {
            prefsPrivateEditor.putString(key, ObjectSerializer.serialize(s));
        } catch (IOException e) {
            e.printStackTrace();
        }
        prefsPrivateEditor.commit();
        prefsPrivateEditor = null;
        Pref.sharedPreferences = null;
    }

    public static <T> Set<T> copy(Set<T> source) {
        return new HashSet<T>(source);
    }
}