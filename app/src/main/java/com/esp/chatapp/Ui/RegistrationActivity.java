package com.esp.chatapp.Ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.esp.chatapp.Backend.RegistrationAPI;
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
public class RegistrationActivity extends AppCompatActivity implements OnPopUpDialogButoonClickListener {

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
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        context = this;
        imgFacebook = (ImageView) findViewById(R.id.imgFacebook);
        imgInsta = (ImageView) findViewById(R.id.imgInsta);

        edtUsername = (EditText) findViewById(R.id.edtUsername);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtRePassword = (EditText) findViewById(R.id.edtRePassword);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtMobile = (EditText) findViewById(R.id.edtMobile);


        Utils.setDefaultRoundImage(context, imgInsta, R.drawable.insta);
        Utils.setDefaultRoundImage(context, imgFacebook, R.drawable.facebook);

        txtsignup = (TextView) findViewById(R.id.txtsignup);
        txtsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String valid = validation();
                if (valid == null) {
                    if (Utils.isOnline(context)) {

                        userBean = new UserBean();
                        userBean.username = edtUsername.getText().toString().trim();
                        userBean.password = edtPassword.getText().toString().trim();
                        userBean.email = edtEmail.getText().toString().trim();
                        userBean.mobile = edtMobile.getText().toString().trim();
                        userBean.latlong = "";
                        registrationAPI = new RegistrationAPI(context, responseListener, userBean);
                        registrationAPI.execute();
                    } else {
                        AlertDailogView.showAlert(context, "Internet not available").show();
                    }
                } else {
                    AlertDailogView.showAlert(context, valid).show();
                }

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
        } else if (edtUsername.getText().toString().trim().length() < 8 || edtUsername.getText().toString().trim().length() > 20) {
            valid = getResources().getString(R.string.validlengthusername);
            this.edtUsername.requestFocus();
            this.edtUsername.setSelection(this.edtUsername.length());
        } else if (edtPassword.getText().toString().trim().equals("")
                || edtPassword.getText().toString().trim().equals(null)) {
            valid = getResources().getString(R.string.validblankpassword);
            this.edtPassword.requestFocus();
            this.edtPassword.setSelection(this.edtPassword.length());
        } else if (edtRePassword.getText().toString().trim().equals("")
                || edtRePassword.getText().toString().trim().equals(null)) {
            valid = getResources().getString(R.string.validblankconfirmpassword);
            this.edtRePassword.requestFocus();
            this.edtRePassword.setSelection(this.edtRePassword.length());
        } else if (!edtPassword.getText().toString().trim().equals(edtRePassword.getText().toString().trim())) {
            valid = getResources().getString(R.string.notmatchconfirmpassword);
            this.edtRePassword.requestFocus();
            this.edtRePassword.setSelection(this.edtRePassword.length());
        } else if (edtEmail.getText().toString().trim().equals("")
                || edtEmail.getText().toString().trim().equals(null)) {
            valid = getResources().getString(R.string.validblankemail);
            this.edtEmail.requestFocus();
            this.edtEmail.setSelection(this.edtEmail.length());
        } else if (!Utils.EMAIL_ADDRESS_PATTERN.matcher(
                edtEmail.getText().toString()).matches()) {
            valid = getResources().getString(R.string.invalidblankemail);
            this.edtEmail.requestFocus();
            this.edtEmail.setSelection(this.edtEmail.length());
        } else if (edtMobile.getText().toString().trim().equals("")
                || edtMobile.getText().toString().trim().equals(null)) {
            valid = getResources().getString(R.string.validblankmobile);
            this.edtMobile.requestFocus();
            this.edtMobile.setSelection(this.edtMobile.length());
        }

        return valid;
    }

    private ResponseListener responseListener = new ResponseListener() {

        public void onResponce(String tag, int result, Object obj) {
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

    @Override
    public void OnButtonClick(int tag, int buttonIndex, String input) {

    }
}
