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
import com.esp.chatapp.Bean.PostBean;
import com.esp.chatapp.Bean.UserBean;
import com.esp.chatapp.R;
import com.esp.chatapp.Utils.Config;
import com.esp.chatapp.Utils.Log;

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


    public FeedListAPI(Context context, ResponseListener responseListener, UserBean userBean) {
        this.context = context;
        this.mParams = new HashMap<String, String>();
        Config.API_FEED_LIST = Config.HOST + Config.API_FEED_LIST_JSON;
        mParams.put(Config.id, String.valueOf(userBean.userid));
        mParams.put(Config.myfeed, userBean.myFeed == false ? "0" : "1");
        mParams.put(Config.pageid, String.valueOf(userBean.pageno));

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
        doCallBack(code, mesg, postBeanArrayList);

        /** release variables */
        response = null;
        jsonObject = null;
    }

    /*
     * Send control back to the caller which includes
     *
     * Status: Successful or Failure Message: Its an Object, if required
     */
    private void doCallBack(int code, String mesg, ArrayList<PostBean> postBeanArrayList) {
        try {
            if (code == 0) {
                responseListener.onResponce(Config.TAG_FEED_LIST,
                        Config.API_SUCCESS, postBeanArrayList);
            } else if (code > 0) {
                responseListener.onResponce(Config.TAG_FEED_LIST,
                        Config.API_FAIL, mesg);
            } else if (code < 0) {
                responseListener.onResponce(Config.TAG_FEED_LIST,
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
            mAdapter.doCancel(Config.TAG_FEED_LIST);
        }
    }
}