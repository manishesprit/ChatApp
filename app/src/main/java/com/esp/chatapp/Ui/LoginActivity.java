package com.esp.chatapp.Ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.esp.chatapp.Backend.LoginAPI;
import com.esp.chatapp.Backend.ResponseListener;
import com.esp.chatapp.Bean.UserBean;
import com.esp.chatapp.R;
import com.esp.chatapp.Uc.AlertDailogView;
import com.esp.chatapp.Uc.OnPopUpDialogButoonClickListener;
import com.esp.chatapp.Utils.Config;
import com.esp.chatapp.Utils.Utils;

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
    private UserBean userBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        activity = this;
        txtlogin = (TextView) findViewById(R.id.txtlogin);
        txtRegisterNow = (TextView) findViewById(R.id.txtRegisterNow);
        txtForgot = (TextView) findViewById(R.id.txtForgot);

        edtEmial = (EditText) findViewById(R.id.edtEmial);
        edtPassword = (EditText) findViewById(R.id.edtPassword);

        imgFacebook = (ImageView) findViewById(R.id.imgFacebook);
        imgInsta = (ImageView) findViewById(R.id.imgInsta);

        Utils.setDefaultRoundImage(LoginActivity.this, imgInsta, R.drawable.insta);
        Utils.setDefaultRoundImage(LoginActivity.this, imgFacebook, R.drawable.facebook);


        txtlogin.setOnClickListener(this);
        txtRegisterNow.setOnClickListener(this);
        txtForgot.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.txtlogin:
                String valid = validation();
                if (valid == null) {
                    if (Utils.isOnline(LoginActivity.this)) {
                        userBean = new UserBean();
                        userBean.username = edtEmial.getText().toString().trim();
                        userBean.password = edtPassword.getText().toString().trim();
                        userBean.udID = "";
                        userBean.latlong = "";
                        loginAPI = new LoginAPI(LoginActivity.this, responseListener, userBean);
                        loginAPI.execute();
                    } else {
                        AlertDailogView.showAlert(LoginActivity.this, "Internet not available").show();
                    }
                } else {
                    AlertDailogView.showAlert(LoginActivity.this, valid).show();
                }

                break;

            case R.id.txtRegisterNow:
                intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
                break;

            case R.id.txtForgot:
                intent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    public String validation() {
        String valid = null;
        if (edtEmial.getText().toString().trim().equals("")
                || edtEmial.getText().toString().trim().equals(null)) {
            valid = getResources().getString(R.string.validusername);
            this.edtEmial.requestFocus();
            this.edtEmial.setSelection(this.edtEmial.length());
        } else if (edtPassword.getText().toString().trim().equals("")
                || edtPassword.getText().toString().trim().equals(null)) {
            valid = getResources().getString(R.string.validblankpassword);
            this.edtPassword.requestFocus();
            this.edtPassword.setSelection(this.edtPassword.length());
        }
        return valid;
    }

    private ResponseListener responseListener = new ResponseListener() {
        @Override
        public void onResponce(String tag, int result, Object obj) {

            if (result == Config.API_SUCCESS) {
                if (tag == Config.TAG_LOGIN) {
                    intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            } else {

            }
        }
    };

    @Override
    public void OnButtonClick(int tag, int buttonIndex, String input) {

    }
}
