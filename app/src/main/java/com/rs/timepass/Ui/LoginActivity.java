package com.rs.timepass.Ui;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.rs.timepass.Backend.LoginAPI;
import com.rs.timepass.Backend.LoginWithFbAPI;
import com.rs.timepass.Backend.ResponseListener;
import com.rs.timepass.Bean.UserBean;
import com.rs.timepass.R;
import com.rs.timepass.Uc.AlertDailogView;
import com.rs.timepass.Uc.OnPopUpDialogButoonClickListener;
import com.rs.timepass.Utils.Config;
import com.rs.timepass.Utils.Log;
import com.rs.timepass.Utils.Utils;

import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by admin on 2/5/16.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener, OnPopUpDialogButoonClickListener {

    private EditText edtEmial;
    private EditText edtPassword;
    private TextView txtlogin;
    private Intent intent;
    private TextView txtForgot;
    private ImageView imgFacebook;
    private ImageView imgInsta;
    private TextView txtRegisterNow;
    public static Activity activity;
    private LoginAPI loginAPI;
    private LoginWithFbAPI loginWithFbAPI;
    private UserBean userBean;
    private Context context;
    private View view_email;
    private View view_password;
    private LinearLayout myprogressBar;
    private CallbackManager callbackManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_login);
        activity = this;
        context = this;
        txtlogin = (TextView) findViewById(R.id.txtlogin);
        txtRegisterNow = (TextView) findViewById(R.id.txtRegisterNow);
        txtForgot = (TextView) findViewById(R.id.txtForgot);

        myprogressBar = (LinearLayout) findViewById(R.id.myprogressBar);

        edtEmial = (EditText) findViewById(R.id.edtEmial);
        edtPassword = (EditText) findViewById(R.id.edtPassword);

        view_email = (View) findViewById(R.id.view_email);
        view_password = (View) findViewById(R.id.view_password);

        imgFacebook = (ImageView) findViewById(R.id.imgFacebook);
        imgInsta = (ImageView) findViewById(R.id.imgInsta);

        Utils.setDefaultRoundImage(LoginActivity.this, imgInsta, R.drawable.insta);
        Utils.setDefaultRoundImage(LoginActivity.this, imgFacebook, R.drawable.facebook);


        txtlogin.setOnClickListener(this);
        txtRegisterNow.setOnClickListener(this);
        txtForgot.setOnClickListener(this);
        imgFacebook.setOnClickListener(this);

        edtEmial.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() > 0) {
                    view_email.setBackgroundColor(getResources().getColor(R.color.color_whitedark));
                } else {
                    view_email.setBackgroundColor(getResources().getColor(R.color.color_red));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    view_password.setBackgroundColor(getResources().getColor(R.color.color_whitedark));
                } else {
                    view_password.setBackgroundColor(getResources().getColor(R.color.color_red));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult result) {
                        System.out.println("onSuccess");
                        GraphRequest request = GraphRequest.newMeRequest(
                                result.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object,
                                                            GraphResponse response) {
                                        try {
                                            Log.print("Response===========" + object.toString());
                                            userBean = new UserBean();
                                            userBean.username = object.getString("email").substring(0, object.getString("email").indexOf("@"));
                                            userBean.email = object.getString("email");
                                            userBean.fbid = object.getString("id");
                                            userBean.name = object.getString("name");
                                            userBean.latlong = "";
                                            facebookLogout();

                                            if (userBean.fbid != null && !userBean.fbid.equalsIgnoreCase("")) {
                                                myprogressBar.setVisibility(View.VISIBLE);
                                                loginWithFbAPI = new LoginWithFbAPI(context, responseListener, userBean);
                                                loginWithFbAPI.execute();
                                            }

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email,picture");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onError(FacebookException exception) {

                    }

                    @Override
                    public void onCancel() {

                    }
                });


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.txtlogin:
                String valid = validation();
                if (valid == null) {
                    if (Utils.isOnline(context)) {
                        myprogressBar.setVisibility(View.VISIBLE);
                        userBean = new UserBean();
                        userBean.username = edtEmial.getText().toString().trim();
                        userBean.password = edtPassword.getText().toString().trim();
                        userBean.latlong = "";
                        loginAPI = new LoginAPI(context, responseListener, userBean);
                        loginAPI.execute();
                    } else {
                        AlertDailogView.showAlert(context, "Internet Error", "Internet not available", "Cancel", true, "Try again", this, 0).show();
                    }
                }

                break;

            case R.id.txtRegisterNow:
                intent = new Intent(context, RegistrationActivity.class);
                startActivity(intent);
                break;

            case R.id.txtForgot:
//                intent = new Intent(context, HomeActivity.class);
//                startActivity(intent);
//                finish();
                break;

            case R.id.imgFacebook:
                if (Utils.isOnline(context)) {
                    LoginManager.getInstance().logInWithReadPermissions(
                            LoginActivity.this,
                            Arrays.asList("email", "public_profile"));
                } else {
                    AlertDailogView.showAlert(context, "Internet Error", "Internet not available", "Cancel", true, "Try again", this, 0).show();
                }
                break;


        }
    }

    public String validation() {
        String valid = null;
        Animation shake = AnimationUtils.loadAnimation(context, R.anim.shake_animation);
        if (edtEmial.getText().toString().trim().equals("")
                || edtEmial.getText().toString().trim().equals(null)) {
            valid = getResources().getString(R.string.validusername);
            this.edtEmial.requestFocus();
            this.edtEmial.setSelection(this.edtEmial.length());
            ObjectAnimator
                    .ofFloat(edtEmial, "translationX", 0, 25, -25, 25, -25, 15, -15, 6, -6, 0)
                    .setDuration(1000)
                    .start();
            view_email.setBackgroundColor(getResources().getColor(R.color.color_red));

        } else if (edtPassword.getText().toString().trim().equals("")
                || edtPassword.getText().toString().trim().equals(null)) {
            valid = getResources().getString(R.string.validblankpassword);
            this.edtPassword.requestFocus();
            this.edtPassword.setSelection(this.edtPassword.length());
            ObjectAnimator
                    .ofFloat(edtPassword, "translationX", 0, 25, -25, 25, -25, 15, -15, 6, -6, 0)
                    .setDuration(1000)
                    .start();
            view_password.setBackgroundColor(getResources().getColor(R.color.color_red));
        }
        return valid;
    }

    private ResponseListener responseListener = new ResponseListener() {
        @Override
        public void onResponce(String tag, int result, Object obj, Object obj1) {

        }

        public void onResponce(String tag, int result, Object obj) {
            myprogressBar.setVisibility(View.GONE);
            if (result == Config.API_SUCCESS) {
                if (tag == Config.TAG_LOGIN) {
                    intent = new Intent(context, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            } else {
                AlertDailogView.showAlert(context, obj.toString()).show();
            }
        }
    };

    @Override
    public void OnButtonClick(int tag, int buttonIndex, String input) {

        if (tag == 0) {
            if (buttonIndex == 2) {
                txtlogin.performClick();
            }

            if (buttonIndex == 1) {
                finish();
            }
        }

    }

    void facebookLogout() {
        LoginManager.getInstance().logOut();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

}
