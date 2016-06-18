package com.rs.timepass.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

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

//    public static ArrayList<StyleBean> getArrayValue(Context context, String key, ArrayList<StyleBean> defaultValue) {
//        Pref.openPref(context);
//        ArrayList<StyleBean> set = null;
//        try {
//            set = (ArrayList<StyleBean>) ObjectSerializer
//                    .deserialize(Pref.sharedPreferences.getString(key,
//                            ObjectSerializer.serialize(defaultValue)));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        Pref.sharedPreferences = null;
//        return set;
//    }
//
//    public static void setArrayValue(Context context, String key, ArrayList<StyleBean> value) {
//        Pref.openPref(context);
//        Editor prefsPrivateEditor = Pref.sharedPreferences.edit();
//        ArrayList<StyleBean> s = new ArrayList<StyleBean>();
//        s.addAll(value);
//
//        try {
//            prefsPrivateEditor.putString(key, ObjectSerializer.serialize(s));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        prefsPrivateEditor.commit();
//        prefsPrivateEditor = null;
//        Pref.sharedPreferences = null;
//    }
//
//    public static ArrayList<FeedBean> getNotificationArray(Context context, String key, ArrayList<FeedBean> defaultValue) {
//        Pref.openPref(context);
//        ArrayList<FeedBean> set = null;
//        try {
//            set = (ArrayList<FeedBean>) ObjectSerializer
//                    .deserialize(Pref.sharedPreferences.getString(key,
//                            ObjectSerializer.serialize(defaultValue)));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        Pref.sharedPreferences = null;
//        return set;
//    }
//
//    public static void setNotificationArray(Context context, String key, ArrayList<FeedBean> value) {
//        Pref.openPref(context);
//        Editor prefsPrivateEditor = Pref.sharedPreferences.edit();
//        ArrayList<FeedBean> s = new ArrayList<FeedBean>();
//        s.addAll(value);
//
//        try {
//            prefsPrivateEditor.putString(key, ObjectSerializer.serialize(s));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        prefsPrivateEditor.commit();
//        prefsPrivateEditor = null;
//        Pref.sharedPreferences = null;
//    }

    public static <T> Set<T> copy(Set<T> source) {
        return new HashSet<T>(source);
    }
}