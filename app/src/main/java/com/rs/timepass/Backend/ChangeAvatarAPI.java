package com.rs.timepass.Backend;

import android.content.Context;
import android.os.AsyncTask;

import com.rs.timepass.Utils.Config;
import com.rs.timepass.Utils.Log;
import com.rs.timepass.Utils.Pref;

import org.json.JSONObject;

import java.io.File;

public class ChangeAvatarAPI extends AsyncTask<Void, Void, Integer> {
    public Context context;
    public ResponseListener handler;
    public String mesg;
    public MultipartRequest multipartReq;
    public File file = null;
    public String avatarName;

    public ChangeAvatarAPI(Context context, String avatarName,
                           ResponseListener handler) {
        this.context = context;
        this.handler = handler;
        this.avatarName = avatarName;
    }

    @Override
    protected Integer doInBackground(Void... params) {
        int result = -1;
        try {
            multipartReq = new MultipartRequest(context);

            if (this.avatarName != null && (this.avatarName != "" || !this.avatarName.equals("") || !this.avatarName.equals("null"))) {
                file = new File(Config.DIR_USERDATA + "/" + avatarName);
                if (file.exists()) {
                    multipartReq.addFile(Config.avatarfile, file.toString(),
                            file.getName());
                }
            }

            multipartReq.addString(Config.userid, String.valueOf(Pref.getValue(context, Config.PREF_USER_ID, 0)));

            Config.API_CHANGE_AVATAR = Config.HOST + Config.API_CHANGE_AVATAR_JSON;
            result = parse(multipartReq.execute(Config.API_CHANGE_AVATAR));
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
            this.handler.onResponce(Config.TAG_CHANGE_AVATAR,
                    Config.API_SUCCESS, mesg);
        } else {
            //WebInterface.showAPIErrorAlert(mCaller, "Alert", this.mesg);
            this.handler.onResponce(Config.TAG_CHANGE_AVATAR,
                    Config.API_FAIL, mesg);
        }
    }

    public int parse(String response) {
        int code = 0;
        JSONObject jsonDoc = null;
        try {
            Log.print("=========== RESPONSE ========" + response);
            jsonDoc = new JSONObject(response);
            code = jsonDoc.getInt(Config.code);
            mesg = jsonDoc.getString(Config.message);
            if (code == 0) {
                Pref.setValue(context, Config.PREF_AVATAR, avatarName);
                file = null;
            }
        } catch (Exception e) {
            code = -4;
            Log.error(this.getClass() + " :: parse()", e);
            e.printStackTrace();
        } finally {
            response = null;
            /** release variables */
            jsonDoc = null;
        }
        return code;
    }
}