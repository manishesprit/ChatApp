package com.esp.chatapp.Backend;

import android.app.Activity;
import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.esp.chatapp.Adapter.Adapter;
import com.esp.chatapp.Bean.UserBean;
import com.esp.chatapp.R;
import com.esp.chatapp.Utils.Config;
import com.esp.chatapp.Utils.Log;
import com.esp.chatapp.Utils.Pref;
import com.esp.chatapp.Utils.Utils;

import org.json.JSONObject;

import java.util.HashMap;

public class LoginAPI {
    private Context context;
    private HashMap<String, String> mParams = null;
    private Adapter mAdapter = null;
    private ResponseListener responseListener;
    private UserBean userBean;


    public LoginAPI(Context context, ResponseListener responseListener, UserBean userBean) {
        this.context = context;
        this.mParams = new HashMap<String, String>();
        Config.API_LOGIN = Config.HOST + Config.API_LOGIN_JSON+Config.username+"="+userBean.username+"&"+Config.password+"="+userBean.password+"&"+Config.latlong+"="+userBean.latlong+"&"+Config.udid+"="+ Utils.getDeviceID(context);
        this.userBean = userBean;
        Log.print(":::: API_LOGIN ::::" + Config.API_LOGIN);
        this.responseListener = responseListener;
    }

    public void execute() {
        this.mAdapter = new Adapter(this.context);
        this.mAdapter.doGet(Config.TAG_LOGIN, Config.API_LOGIN, mParams,
                new APIResponseListener() {

                    @Override
                    public void onResponse(String response) {
                        mParams = null;
                        // Parse Response and Proceed Further
                        parse(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mParams = null;
                        if (error instanceof TimeoutError
                                || error instanceof NoConnectionError) {
                            if (!((Activity) context).isFinishing()) {
//                                AlertDailogView
//                                        .showAlert(
//                                                context,
//                                                context.getResources()
//                                                        .getString(
//                                                                R.string.connectionErrorMessage),
//                                                context.getResources()
//                                                        .getString(
//                                                                R.string.tryAgain),
//                                                false).show();
                            }
                        } else if (error instanceof AuthFailureError) {
                            //
                        } else if (error instanceof ServerError) {
                            //
                        } else if (error instanceof NetworkError) {
                            //
                        } else if (error instanceof ParseError) {
                            //
                        }
                        // Inform Caller that the API call is failed
                        responseListener.onResponce(Config.TAG_LOGIN, Config.API_FAIL, context.getResources()
                                .getString(
                                        R.string.connectionErrorMessage));
                    }
                });
    }

    /*
     * Parse the response and prepare for callback
     */
    private void parse(String response) {
        int code = 0;
        String mesg = null;
        JSONObject jsonObject = null;
        try {

            jsonObject = new JSONObject(response);
            code = jsonObject.getInt(Config.code);
            mesg = jsonObject.getString(Config.message);
            if (code == 0) {
                Pref.setValue(context, Config.PREF_USER_ID, jsonObject.getInt(Config.userid));
                Pref.setValue(context, Config.PREF_USERNAME, userBean.username);
                Pref.setValue(context, Config.PREF_STATUS, jsonObject.getString(Config.status));
                Pref.setValue(context, Config.PREF_EMAIL, jsonObject.getString(Config.email));
                Pref.setValue(context, Config.PREF_NAME, jsonObject.getString(Config.name).toString().equals("") ? userBean.username : jsonObject.getString(Config.name).toString());
                Pref.setValue(context, Config.PREF_NOOFPOST, jsonObject.getInt(Config.no_post));
                Pref.setValue(context, Config.PREF_NOOFFOLLOWER, jsonObject.getString(Config.no_follower).toString().equalsIgnoreCase("")?0:jsonObject.getString(Config.no_follower).split(",").length);
                Pref.setValue(context, Config.PREF_NOOFFOLLING, jsonObject.getString(Config.no_following).toString().equalsIgnoreCase("")?0:jsonObject.getString(Config.no_following).split(",").length);
                Pref.setValue(context, Config.PREF_AVATAR, jsonObject.getString(Config.avatar).toString());
            }

        } catch (Exception e) {
            code = -1;
            mesg = "Exception :: " + this.getClass() + " :: parse() :: "
                    + e.getLocalizedMessage();
            Log.error(this.getClass() + " :: Exception :: ", e);
            Log.print(this.getClass() + " :: Exception :: ", e);
        }
        doCallBack(code, mesg, userBean);

        /** release variables */
        response = null;
        jsonObject = null;
    }

    /*
     * Send control back to the caller which includes
     *
     * Status: Successful or Failure Message: Its an Object, if required
     */
    private void doCallBack(int code, String mesg, UserBean userBean) {
        try {
            if (code == 0) {
                responseListener.onResponce(Config.TAG_LOGIN,
                        Config.API_SUCCESS, userBean);
            } else if (code > 0) {
                responseListener.onResponce(Config.TAG_LOGIN,
                        Config.API_FAIL, mesg);
            } else if (code < 0) {
                responseListener.onResponce(Config.TAG_LOGIN,
                        Config.API_FAIL, mesg);
            }
        } catch (Exception e) {
            Log.error(this.getClass() + " :: Exception :: ", e);
            Log.print(this.getClass() + " :: Exception :: ", e);
        }
    }

    /*
     * Cancel API Request
     */
    public void doCancel() {
        if (mAdapter != null) {
            mAdapter.doCancel(Config.TAG_LOGIN);
        }
    }
}