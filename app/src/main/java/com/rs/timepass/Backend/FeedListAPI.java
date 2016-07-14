package com.rs.timepass.Backend;

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
import com.rs.timepass.Adapter.Adapter;
import com.rs.timepass.Bean.PostBean;
import com.rs.timepass.Bean.UserBean;
import com.rs.timepass.R;
import com.rs.timepass.Utils.Config;
import com.rs.timepass.Utils.Log;
import com.rs.timepass.Utils.Pref;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class FeedListAPI {
    private Context context;
    private HashMap<String, String> mParams = null;
    private Adapter mAdapter = null;
    private ResponseListener responseListener;
    private PostBean postBean;
    private ArrayList<PostBean> postBeanArrayList;
    private UserBean userBean;

    public FeedListAPI(Context context, ResponseListener responseListener, UserBean userBean) {
        this.context = context;
        this.userBean = userBean;
        this.mParams = new HashMap<String, String>();
        Config.API_FEED_LIST = Config.HOST + Config.API_FEED_LIST_JSON + Config.userid + "=" + Pref.getValue(context,Config.PREF_USER_ID,0) + "&"+ Config.friendid + "=" + userBean.userid + "&" + Config.myfeed + "=" + (userBean.myFeed == false ? 0 : 1) + "&" + Config.offset + "=" + userBean.offset+ "&" + Config.limit + "=" + userBean.limit;

        Log.print(":::: API_FEED_LIST ::::" + Config.API_FEED_LIST);
        this.responseListener = responseListener;
    }

    public void execute() {
        this.mAdapter = new Adapter(this.context);
        this.mAdapter.doGet(Config.TAG_FEED_LIST, Config.API_FEED_LIST, mParams,
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
                        responseListener.onResponce(Config.TAG_FEED_LIST, Config.API_FAIL, context.getResources()
                                .getString(
                                        R.string.connectionErrorMessage), null);
                    }
                });
    }

    /*
     * Parse the response and prepare for callback
     */
    private void parse(String response) {
        Log.print("===========response============"+response);
        int code = 0;
        String mesg = null;
        JSONObject jsonObject = null;
        PostBean postBean1 = null;
        try {

            jsonObject = new JSONObject(response);
            code = jsonObject.getInt(Config.code);
            mesg = jsonObject.getString(Config.message);
            if (code == 0) {
                if (userBean.myFeed == true && userBean.userid == Pref.getValue(context, Config.PREF_USER_ID, 0)) {

                    postBean1 = new PostBean();
                    postBean1.userid = userBean.userid;
                    postBean1.name = jsonObject.getString(Config.name);
                    postBean1.avatar = jsonObject.getString(Config.avatar);
                    postBean1.status = jsonObject.getString(Config.status);
                    postBean1.noOffollowers = jsonObject.getString(Config.no_follower).toString().equalsIgnoreCase("") ? 0 : jsonObject.getString(Config.no_follower).split(",").length;
                    postBean1.noOffollowing = jsonObject.getString(Config.no_following).toString().equalsIgnoreCase("") ? 0 : jsonObject.getString(Config.no_following).split(",").length;
                    postBean1.noOfpost = jsonObject.getInt(Config.no_post);
                    postBean1.mobile = jsonObject.getString(Config.mobile);
                    postBean1.email = jsonObject.getString(Config.email);

                    Pref.setValue(context, Config.PREF_STATUS, jsonObject.getString(Config.status));
                    Pref.setValue(context, Config.PREF_EMAIL, jsonObject.getString(Config.email));
                    Pref.setValue(context, Config.PREF_MOBILE, jsonObject.getString(Config.mobile));
                    Pref.setValue(context, Config.PREF_NAME, jsonObject.getString(Config.name));
                    Pref.setValue(context, Config.PREF_NOOFPOST, jsonObject.getInt(Config.no_post));
                    Pref.setValue(context, Config.PREF_NOOFFOLLOWER, jsonObject.getString(Config.no_follower).toString().equalsIgnoreCase("") ? 0 : jsonObject.getString(Config.no_follower).split(",").length);
                    Pref.setValue(context, Config.PREF_NOOFFOLLING, jsonObject.getString(Config.no_following).toString().equalsIgnoreCase("") ? 0 : jsonObject.getString(Config.no_following).split(",").length);
                    Pref.setValue(context, Config.PREF_AVATAR, jsonObject.getString(Config.avatar).toString());

                }
                else if (userBean.myFeed == true) {
                    postBean1 = new PostBean();
                    postBean1.userid = userBean.userid;
                    postBean1.name = jsonObject.getString(Config.name);
                    postBean1.avatar = jsonObject.getString(Config.avatar);
                    postBean1.status = jsonObject.getString(Config.status);
                    postBean1.noOffollowers = jsonObject.getString(Config.no_follower).toString().equalsIgnoreCase("") ? 0 : jsonObject.getString(Config.no_follower).split(",").length;
                    postBean1.noOffollowing = jsonObject.getString(Config.no_following).toString().equalsIgnoreCase("") ? 0 : jsonObject.getString(Config.no_following).split(",").length;
                    postBean1.noOfpost = jsonObject.getInt(Config.no_post);
                    postBean1.mobile = jsonObject.getString(Config.mobile);
                    postBean1.email = jsonObject.getString(Config.email);

                }
                else
                {
                    Pref.setValue(context, Config.PREF_STATUS, jsonObject.getString(Config.status));
                    Pref.setValue(context, Config.PREF_EMAIL, jsonObject.getString(Config.email));
                    Pref.setValue(context, Config.PREF_MOBILE, jsonObject.getString(Config.mobile));
                    Pref.setValue(context, Config.PREF_NAME, jsonObject.getString(Config.name));
                    Pref.setValue(context, Config.PREF_NOOFPOST, jsonObject.getInt(Config.no_post));
                    Pref.setValue(context, Config.PREF_NOOFFOLLOWER, jsonObject.getString(Config.no_follower).toString().equalsIgnoreCase("") ? 0 : jsonObject.getString(Config.no_follower).split(",").length);
                    Pref.setValue(context, Config.PREF_NOOFFOLLING, jsonObject.getString(Config.no_following).toString().equalsIgnoreCase("") ? 0 : jsonObject.getString(Config.no_following).split(",").length);
                    Pref.setValue(context, Config.PREF_AVATAR, jsonObject.getString(Config.avatar).toString());
                }

                postBeanArrayList = new ArrayList<>();
                JSONArray feedListArray = jsonObject.getJSONArray(Config.feedlist);
                if (feedListArray != null && feedListArray.length() > 0) {
                    for (int i = 0; i < feedListArray.length(); i++) {
                        JSONObject jsonObject1 = feedListArray.getJSONObject(i);
                        postBean = new PostBean();
                        postBean.feedid = jsonObject1.getInt(Config.feedid);
                        postBean.userid = jsonObject1.getInt(Config.userid);
                        postBean.name = jsonObject1.getString(Config.name);
                        postBean.image_url = jsonObject1.getString(Config.image_url);
                        postBean.caption = jsonObject1.getString(Config.caption);
                        postBean.avatar = jsonObject1.getString(Config.avatar);
                        postBean.noOfcomment = jsonObject1.getInt(Config.no_comment);
                        postBean.noOflike = jsonObject1.getInt(Config.no_like);
                        postBean.islike = jsonObject1.getInt(Config.islike) == 0 ? false : true;
                        postBean.posttime = jsonObject1.getString(Config.posttime);
                        postBeanArrayList.add(postBean);
                    }
                }
            }

        } catch (Exception e) {
            code = -1;
            mesg = "Exception :: " + this.getClass() + " :: parse() :: "
                    + e.getLocalizedMessage();
            Log.error(this.getClass() + " :: Exception :: ", e);
            Log.print(this.getClass() + " :: Exception :: ", e);
        }
        doCallBack(code, mesg, postBeanArrayList, postBean1);

        /** release variables */
        response = null;
        jsonObject = null;
    }

    /*
     * Send control back to the caller which includes
     *
     * Status: Successful or Failure Message: Its an Object, if required
     */
    private void doCallBack(int code, String mesg, ArrayList<PostBean> postBeanArrayList, PostBean postBean) {
        try {
            if (code == 0) {
                responseListener.onResponce(Config.TAG_FEED_LIST,
                        Config.API_SUCCESS, postBeanArrayList, postBean);
            } else if (code > 0) {
                responseListener.onResponce(Config.TAG_FEED_LIST,
                        Config.API_FAIL, mesg, null);
            } else if (code < 0) {
                responseListener.onResponce(Config.TAG_FEED_LIST,
                        Config.API_FAIL, mesg, null);
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
            mAdapter.doCancel(Config.TAG_FEED_LIST);
        }
    }
}