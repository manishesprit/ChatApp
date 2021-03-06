package com.rs.timepass.Backend;

import android.content.Context;
import android.os.AsyncTask;

import com.rs.timepass.Bean.PostBean;
import com.rs.timepass.Utils.Config;
import com.rs.timepass.Utils.Log;
import com.rs.timepass.Utils.Pref;

import org.json.JSONObject;

import java.io.File;

public class CreateFeedAPI extends AsyncTask<Void, Void, Integer> {
    public Context context;
    public ResponseListener responseListener;
    public String mesg;
    public MultipartRequest multipartReq;
    public File file = null;
    public PostBean postBean;

    public CreateFeedAPI(Context context, ResponseListener responseListener, PostBean postBean) {
        this.context = context;
        this.responseListener = responseListener;
        this.postBean = postBean;
    }

    @Override
    protected Integer doInBackground(Void... params) {
        int result = -1;
        try {
            multipartReq = new MultipartRequest(context);
            if (this.postBean.image_url != null && this.postBean.image_url != "" ) {
                file = new File(Config.DIR_FEEDDATA + "/" + postBean.image_url);
                if (file.exists()) {
                    multipartReq.addFile(Config.feedfile, file.toString(), file.getName());
                }
            }

            multipartReq.addString(Config.caption, postBean.caption);
            multipartReq.addString(Config.userid, String.valueOf(Pref.getValue(context, Config.PREF_USER_ID, 0)));


            Config.API_CREATE_FEED = Config.HOST + Config.API_CREATE_FEED_JSON;
            result = parse(multipartReq.execute(Config.API_CREATE_FEED));
        } catch (Exception e) {
            result = -1;
            mesg = "Unable to upload please try again.";
            Log.print(e);
            Log.error(this.getClass() + "", e);
        }
        return result;
    }

    protected void onPostExecute(Integer result) {

        if (result == 0) {
            // successful
            this.responseListener.onResponce(Config.TAG_CREATE_FEED,
                    Config.API_SUCCESS, mesg);
        } else {
            this.responseListener.onResponce(Config.TAG_CREATE_FEED,
                    Config.API_FAIL, mesg);
        }
    }

    public int parse(String response) {
        int code = 0;
        JSONObject jsonObject = null;
        try {
            Log.print("=========== RESPONSE ========" + response);
            jsonObject = new JSONObject(response);
            code = jsonObject.getInt(Config.code);
            mesg = jsonObject.getString(Config.message);
            if (code == 0) {
//                Pref.setValue(context, Config.PREF_USER_ID, jsonObject.getInt(Config.userid));
//                Pref.setValue(context, Config.PREF_NOOFPOST, jsonObject.getInt(Config.no_post));
//                Pref.setValue(context, Config.PREF_NOOFFOLLOWER, jsonObject.getString(Config.no_follower).toString().equalsIgnoreCase("") ? 0 : jsonObject.getString(Config.no_follower).split(",").length);
//                Pref.setValue(context, Config.PREF_NOOFFOLLING, jsonObject.getString(Config.no_following).toString().equalsIgnoreCase("") ? 0 : jsonObject.getString(Config.no_following).split(",").length);
//                Pref.setValue(context, Config.PREF_AVATAR, jsonObject.getString(Config.avatar).toString());
            }
        } catch (Exception e) {
            code = -4;
            Log.error(this.getClass() + " :: parse()", e);
            e.printStackTrace();
        } finally {
            response = null;
            jsonObject = null;
        }
        return code;
    }
}