package com.rs.timepass.Ui;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
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
import com.rs.timepass.Backend.RegistrationAPI;
import com.rs.timepass.Backend.RegistrationWithFbAPI;
import com.rs.timepass.Backend.ResponseListener;
import com.rs.timepass.Bean.UserBean;
import com.rs.timepass.R;
import com.rs.timepass.Uc.AlertDailogView;
import com.rs.timepass.Utils.Config;
import com.rs.timepass.Utils.Log;
import com.rs.timepass.Utils.Pref;
import com.rs.timepass.Utils.Utils;

import org.json.JSONObject;

import java.util.Arrays;


/**
 * Created by admin on 2/5/16.
 */
public class RegistrationActivity extends AppCompatActivity {

    private EditText edtUsername;
    private EditText edtPassword;
    private EditText edtRePassword;
    private EditText edtEmail;
    private EditText edtMobile;
    private TextView txtsignup;
    private Intent intent;
    private ImageView imgFacebook;
    private ImageView imgInsta;
    private UserBean userBean;
    private RegistrationAPI registrationAPI;
    private RegistrationWithFbAPI registrationWithFbAPI;
    private Context context;

    private View view_name;
    private View view_password;
    private View view_repassword;
    private View view_email;
    private View view_mobile;
    private LinearLayout myprogressBar;
    private CallbackManager callbackManager;

//    private ImageView imgFb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_signup);
        context = this;
        imgFacebook = (ImageView) findViewById(R.id.imgFacebook);
        imgInsta = (ImageView) findViewById(R.id.imgInsta);
//        imgFb = (ImageView) findViewById(R.id.imgFb);

        edtUsername = (EditText) findViewById(R.id.edtUsername);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtRePassword = (EditText) findViewById(R.id.edtRePassword);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtMobile = (EditText) findViewById(R.id.edtMobile);

        view_name = (View) findViewById(R.id.view_name);
        view_password = (View) findViewById(R.id.view_password);
        view_repassword = (View) findViewById(R.id.view_repassword);
        view_email = (View) findViewById(R.id.view_email);
        view_mobile = (View) findViewById(R.id.view_mobile);

        myprogressBar = (LinearLayout) findViewById(R.id.myprogressBar);


        if (Utils.isOnline(context)) {
            if (Pref.getValue(context, Config.PREF_PUSH_ID, "") == null || Pref.getValue(context, Config.PREF_PUSH_ID, "").equals("")) {
                Utils.setPushId(getApplication());
            }
        } else {
            AlertDailogView.showAlert(context, "Internet Error", "Internet not available", "Try again").show();
        }

        Utils.setDefaultRoundImage(context, imgInsta, R.drawable.insta);
        Utils.setDefaultRoundImage(context, imgFacebook, R.drawable.facebook);

        edtUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() > 0) {
                    view_name.setBackgroundColor(getResources().getColor(R.color.color_whitedark));
                } else {
                    view_name.setBackgroundColor(getResources().getColor(R.color.color_red));
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
                if (s.toString().trim().length() > 0) {
                    view_password.setBackgroundColor(getResources().getColor(R.color.color_whitedark));
                } else {
                    view_password.setBackgroundColor(getResources().getColor(R.color.color_red));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edtRePassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() > 0) {
                    view_repassword.setBackgroundColor(getResources().getColor(R.color.color_whitedark));
                } else {
                    view_repassword.setBackgroundColor(getResources().getColor(R.color.color_red));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edtEmail.addTextChangedListener(new TextWatcher() {
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

        edtMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() > 0) {
                    view_mobile.setBackgroundColor(getResources().getColor(R.color.color_whitedark));
                } else {
                    view_mobile.setBackgroundColor(getResources().getColor(R.color.color_red));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        txtsignup = (TextView) findViewById(R.id.txtsignup);
        txtsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String valid = validation();
                if (valid == null) {
                    if (Utils.isOnline(context)) {
                        myprogressBar.setVisibility(View.VISIBLE);
                        userBean = new UserBean();
                        userBean.username = edtUsername.getText().toString().trim();
                        userBean.password = edtPassword.getText().toString().trim();
                        userBean.email = edtEmail.getText().toString().trim();
                        userBean.mobile = edtMobile.getText().toString().trim();
                        userBean.latlong = "";
                        registrationAPI = new RegistrationAPI(context, responseListener, userBean);
                        registrationAPI.execute();
                    } else {
                        AlertDailogView.showAlert(context, "Internet Error", "Internet not available", "Try again").show();
                    }
                } else {
//                    AlertDailogView.showAlert(context, valid).show();
                }

            }
        });

        imgFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isOnline(context)) {
                    LoginManager.getInstance().logInWithReadPermissions(
                            RegistrationActivity.this,
                            Arrays.asList("email", "public_profile"));
                } else {
                    AlertDailogView.showAlert(context, "Internet Error", "Internet not available", "Try again").show();
                }
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
                                            JSONObject jsonObject_picture = object.getJSONObject("picture");
                                            JSONObject jsonObject_data = jsonObject_picture.getJSONObject("data");
                                            userBean.avatar = jsonObject_data.getString("url");
                                            userBean.latlong = "";
                                            facebookLogout();


                                            if (userBean.fbid != null && !userBean.fbid.equalsIgnoreCase("")) {
                                                myprogressBar.setVisibility(View.VISIBLE);
                                                registrationWithFbAPI = new RegistrationWithFbAPI(context, responseListener, userBean);
                                                registrationWithFbAPI.execute();
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

    public String validation() {
        String valid = null;
        if (edtUsername.getText().toString().trim().equals("")
                || edtUsername.getText().toString().trim().equals(null)) {
            valid = getResources().getString(R.string.validusername);
            this.edtUsername.requestFocus();
            this.edtUsername.setSelection(this.edtUsername.length());
            ObjectAnimator
                    .ofFloat(edtUsername, "translationX", 0, 25, -25, 25, -25, 15, -15, 6, -6, 0)
                    .setDuration(1000)
                    .start();
            view_name.setBackgroundColor(getResources().getColor(R.color.color_red));
        } else if (edtUsername.getText().toString().trim().length() < 8 || edtUsername.getText().toString().trim().length() > 20) {
            valid = getResources().getString(R.string.validlengthusername);
            this.edtUsername.requestFocus();
            this.edtUsername.setSelection(this.edtUsername.length());
            ObjectAnimator
                    .ofFloat(edtUsername, "translationX", 0, 25, -25, 25, -25, 15, -15, 6, -6, 0)
                    .setDuration(1000)
                    .start();
            view_name.setBackgroundColor(getResources().getColor(R.color.color_red));
            AlertDailogView.showAlert(context, valid).show();
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
        } else if (edtPassword.getText().toString().trim().length() < 8 || edtPassword.getText().toString().trim().length() > 20) {
            valid = getResources().getString(R.string.validlengthpassword);
            this.edtPassword.requestFocus();
            this.edtPassword.setSelection(this.edtPassword.length());
            AlertDailogView.showAlert(context, valid).show();
        } else if (edtPassword.getText().toString().trim().contains(" ")) {
            valid = getResources().getString(R.string.invalidspacepassword);
            this.edtPassword.requestFocus();
            this.edtPassword.setSelection(this.edtPassword.length());
            AlertDailogView.showAlert(context, valid).show();
        } else if (edtRePassword.getText().toString().trim().equals("")
                || edtRePassword.getText().toString().trim().equals(null)) {
            valid = getResources().getString(R.string.validblankconfirmpassword);
            this.edtRePassword.requestFocus();
            this.edtRePassword.setSelection(this.edtRePassword.length());
            ObjectAnimator
                    .ofFloat(edtRePassword, "translationX", 0, 25, -25, 25, -25, 15, -15, 6, -6, 0)
                    .setDuration(1000)
                    .start();
            view_repassword.setBackgroundColor(getResources().getColor(R.color.color_red));
        } else if (edtRePassword.getText().toString().trim().length() < 8 || edtRePassword.getText().toString().trim().length() > 20) {
            valid = getResources().getString(R.string.validlengthconfirmpassword);
            this.edtRePassword.requestFocus();
            this.edtRePassword.setSelection(this.edtRePassword.length());
            AlertDailogView.showAlert(context, valid).show();
        } else if (edtRePassword.getText().toString().trim().contains(" ")) {
            valid = getResources().getString(R.string.invalidspacepassword);
            this.edtRePassword.requestFocus();
            this.edtRePassword.setSelection(this.edtRePassword.length());
            AlertDailogView.showAlert(context, valid).show();
        } else if (!edtPassword.getText().toString().trim().equals(edtRePassword.getText().toString().trim())) {
            valid = getResources().getString(R.string.notmatchconfirmpassword);
            this.edtRePassword.requestFocus();
            this.edtRePassword.setSelection(this.edtRePassword.length());

            ObjectAnimator
                    .ofFloat(edtRePassword, "translationX", 0, 25, -25, 25, -25, 15, -15, 6, -6, 0)
                    .setDuration(1000)
                    .start();
            view_repassword.setBackgroundColor(getResources().getColor(R.color.color_red));

            AlertDailogView.showAlert(context, valid).show();
        } else if (edtEmail.getText().toString().trim().equals("")
                || edtEmail.getText().toString().trim().equals(null)) {
            valid = getResources().getString(R.string.validblankemail);
            this.edtEmail.requestFocus();
            this.edtEmail.setSelection(this.edtEmail.length());

            ObjectAnimator
                    .ofFloat(edtEmail, "translationX", 0, 25, -25, 25, -25, 15, -15, 6, -6, 0)
                    .setDuration(1000)
                    .start();
            view_email.setBackgroundColor(getResources().getColor(R.color.color_red));

        } else if (!Utils.EMAIL_ADDRESS_PATTERN.matcher(
                edtEmail.getText().toString()).matches()) {
            valid = getResources().getString(R.string.invalidblankemail);
            this.edtEmail.requestFocus();
            this.edtEmail.setSelection(this.edtEmail.length());

            ObjectAnimator
                    .ofFloat(edtEmail, "translationX", 0, 25, -25, 25, -25, 15, -15, 6, -6, 0)
                    .setDuration(1000)
                    .start();
            view_email.setBackgroundColor(getResources().getColor(R.color.color_red));

        } else if (edtMobile.getText().toString().trim().equals("")
                || edtMobile.getText().toString().trim().equals(null)) {
            valid = getResources().getString(R.string.validblankmobile);
            this.edtMobile.requestFocus();
            this.edtMobile.setSelection(this.edtMobile.length());

            ObjectAnimator
                    .ofFloat(edtMobile, "translationX", 0, 25, -25, 25, -25, 15, -15, 6, -6, 0)
                    .setDuration(1000)
                    .start();
            view_mobile.setBackgroundColor(getResources().getColor(R.color.color_red));
        }

        return valid;
    }

    private ResponseListener responseListener = new ResponseListener() {

        public void onResponce(String tag, int result, Object obj) {
            myprogressBar.setVisibility(View.GONE);
            if (result == Config.API_SUCCESS) {
                if (tag == Config.TAG_REGISTRATION) {
                    intent = new Intent(context, HomeActivity.class);
                    startActivity(intent);
                    LoginActivity.activity.finish();
                    finish();
                }
            } else {
                AlertDailogView.showAlert(context, obj.toString()).show();
            }
        }

        @Override
        public void onResponce(String tag, int result, Object obj, Object obj1) {

        }
    };


    void facebookLogout() {
        LoginManager.getInstance().logOut();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if (myprogressBar.getVisibility() == View.GONE)
            super.onBackPressed();
    }
}
