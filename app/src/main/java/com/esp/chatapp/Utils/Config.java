package com.esp.chatapp.Utils;

import android.app.Activity;
import android.os.Environment;

import java.util.HashMap;

public class Config {
    public static String TAG = "ChatApp";
    public static String DB_NAME = "chatapp.db";
    public static String APP_HOME = Environment.getExternalStorageDirectory().getPath() + "/" + TAG;
    public static String DIR_LOG = APP_HOME + "/log";

    // preference file name
    public static final String PREF_FILE = TAG + "_PREF";
    public static String DIR_USERDATA = APP_HOME + "/userdata";
    public static String DIR_MAGIC = DIR_USERDATA;
    /*
     * API BASIC INFO
     */
    public static String API_KEY = "INKSKILL@#2016!";
    public static String API_VERSION = "v1";

    public static String ORIGINAL_HOST = "http://manikom.net23.net/"; //http://manikom.net23.net/login.php

    public static String HOST = "http://manikom.net23.net/";
    public static String HOST_IMAGE = ORIGINAL_HOST + "/public/media/";

//    public static String IMAGE_PATH_WEB_AVATARS = HOST_IMAGE + "avatars/";
//    public static String IMAGE_PATH_WEB_WALLPAPERS = HOST_IMAGE + "wallpapers/";
//    public static String IMAGE_FEED_PATH = HOST_IMAGE + "feeds/";
//    public static String IMAGE_FEED_STYLE = HOST_IMAGE + "feed_style/";


    /* API CALL & TAG */
    public static String API_REGISTRATION = "";
    public static String API_REGISTRATION_JSON = "registration.php?";
    public static String API_REGISTRATION_FB_JSON = "registration.php?";
    public static String TAG_REGISTRATION = "TAG_REGISTRATION";

    public static String API_LOGIN = "";
    public static String API_LOGIN_JSON = "login.php?";
    public static String API_LOGIN_FB_JSON = "login.php?";
    public static String TAG_LOGIN = "TAG_LOGIN";

    public static String API_FEED_LIST = "";
    public static String API_FEED_LIST_JSON = "feed.php?";
    public static String TAG_FEED_LIST = "TAG_FEED_LIST";

    /*
     * Error and Warnings
     */
    public static String ERROR_NETWORK = "ERROR_NETWORK";
    public static String ERROR_API = "ERROR_API";
    public static String ERROR_UNKNOWN = "ERROR_UNKNOWN";

    /* Backendless Service */
    public static final String APPLICATION_ID = "0DB0FD81-3366-E46E-FF8D-760EEAD81D00";
    public static final String SECRET_KEY = "EC70529D-D8E6-D70E-FFE3-7E5BDD55D300";
    public static final String VERSION = "v1";
    public static final String SENDER_ID = "778172556242";
    public static final String CHANNEL_NAME = "default";

    public static int API_SUCCESS = 0;
    public static int API_FAIL = 1;

    // 1 = Android, 2 = Ios
    public static int DEVICE_TYPE = 1;

    // connection timeout is set to 20 seconds
    public static int TIMEOUT_CONNECTION = 20000;

    // SOCKET TIMEOUT IS SET TO 30 SECONDS
    public static int TIMEOUT_SOCKET = 60000;
    /*
     * Cookie and SESSION
     */
    public static String PREF_SESSION_COOKIE = "sessionid";
    public static String SET_COOKIE_KEY = "Set-Cookie";
    public static String COOKIE_KEY = "Cookie";
    public static String SESSION_COOKIE = "sessionid";
    /*
     * GCM push notification.
     */
    public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PREF_PUSH_ID = "PREF_PUSH_ID";

    public static HashMap<String, Activity> screenStack;

    public static String PREF_USER_ID = "PREF_USER_ID";
    public static String PREF_USERNAME = "PREF_USERNAME";
    public static String PREF_NAME = "PREF_NAME";
    public static String PREF_EMAIL = "PREF_EMAIL";
    public static String PREF_NOOFPOST = "PREF_NOOFPOST";
    public static String PREF_NOOFFOLLOWERS = "PREF_NOOFFOLLOWERS";
    public static String PREF_NOOFFOLLING = "PREF_NOOFFOLLING";
    public static String PREF_AVATAR = "PREF_AVATAR";

    //    UnFollowerAPI
    public static String code = "code";
    public static String message = "message";
    public static String username = "username";
    public static String password = "password";
    public static String email = "email";
    public static String mobile = "mobile";
    public static String udid = "udid";
    public static String latlong = "latlong";
    public static String no_followers = "no_followers";
    public static String no_following = "no_following";
    public static String no_posts = "artist_posts";
    public static String no_comment = "no_comment";
    public static String no_like = "no_like";
    public static String avatar = "avatar";
    public static String islike = "islike";
    public static String posttime = "posttime";
    public static String id = "id";
    public static String pageid = "pageid";
    public static String name = "name";
    public static String feedlist = "feedlist";
    public static String userid = "userid";
    public static String feedid = "feedid";
    public static String image_url = "image_url";
    public static String caption = "caption";

}