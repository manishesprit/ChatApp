package com.esp.chatapp.Ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.esp.chatapp.Backend.ChangeProfileAPI;
import com.esp.chatapp.Backend.ResponseListener;
import com.esp.chatapp.Bean.UserBean;
import com.esp.chatapp.R;
import com.esp.chatapp.Uc.AlertDailogView;
import com.esp.chatapp.Uc.OnPopUpDialogButoonClickListener;
import com.esp.chatapp.Utils.Config;
import com.esp.chatapp.Utils.Pref;
import com.esp.chatapp.Utils.Utils;

public class EditProfileActivity extends AppCompatActivity implements OnPopUpDialogButoonClickListener {

    private Toolbar toolbar;
    private EditText edtName;
    private EditText edtEmail;
    private EditText edtMobile;
    private EditText edtCity;
    private EditText edtStatus;
    private TextView txtupdate;
    private Intent intent;
    private UserBean userBean;
    private Context context;
    private ChangeProfileAPI changeProfileAPI;
    private LinearLayout myprogressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        context = this;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Edit Profile");

        edtName = (EditText) findViewById(R.id.edtName);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtMobile = (EditText) findViewById(R.id.edtMobile);
        edtCity = (EditText) findViewById(R.id.edtCity);
        edtStatus = (EditText) findViewById(R.id.edtStatus);
        txtupdate = (TextView) findViewById(R.id.txtupdate);
        myprogressBar = (LinearLayout) findViewById(R.id.myprogressBar);


        edtName.setText(Pref.getValue(context, Config.PREF_NAME, ""));
        edtEmail.setText(Pref.getValue(context, Config.PREF_EMAIL, ""));
        edtMobile.setText(Pref.getValue(context, Config.PREF_MOBILE, ""));
        edtCity.setText(Pref.getValue(context, Config.PREF_CITY, ""));
        edtStatus.setText(Pref.getValue(context, Config.PREF_STATUS, ""));

        txtupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String valid = validation();
                if (valid == null) {
                    if (Utils.isOnline(context)) {
                        myprogressBar.setVisibility(View.VISIBLE);
                        userBean = new UserBean();
                        userBean.name = Uri.encode(edtName.getText().toString().trim());
                        userBean.email = edtEmail.getText().toString().trim();
                        userBean.mobile = edtMobile.getText().toString().trim();
                        userBean.city = Uri.encode(edtCity.getText().toString().trim());
                        userBean.status = Uri.encode(edtStatus.getText().toString().trim());
                        userBean.udID = "";
                        userBean.latlong = "";

                        changeProfileAPI = new ChangeProfileAPI(context, responseListener, userBean);
                        changeProfileAPI.execute();
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
        if (edtName.getText().toString().trim().equals("")
                || edtName.getText().toString().trim().equals(null)) {
            valid = getResources().getString(R.string.validname);
            this.edtName.requestFocus();
            this.edtName.setSelection(this.edtName.length());
        }else if (edtName.getText().toString().trim().equals("")
                || edtName.getText().length() > 15) {
            valid = getResources().getString(R.string.validlengthname);
            this.edtName.requestFocus();
            this.edtName.setSelection(this.edtName.length());
        } else if (edtMobile.getText().toString().trim().equals("")
                || edtMobile.getText().toString().trim().equals(null)) {
            valid = getResources().getString(R.string.validblankmobile);
            this.edtMobile.requestFocus();
            this.edtMobile.setSelection(this.edtMobile.length());
        }

        return valid;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private ResponseListener responseListener = new ResponseListener() {

        public void onResponce(String tag, int result, Object obj) {
            myprogressBar.setVisibility(View.GONE);
            if (result == Config.API_SUCCESS) {
                if (tag == Config.TAG_CHANGE_PROFILE) {
                    AlertDailogView.showAlert(EditProfileActivity.this, "Successfull update profile").show();
                }
            } else {
                AlertDailogView.showAlert(EditProfileActivity.this, obj.toString()).show();
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
