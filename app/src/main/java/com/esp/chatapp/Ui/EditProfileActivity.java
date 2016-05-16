package com.esp.chatapp.Ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.esp.chatapp.Backend.ResponseListener;
import com.esp.chatapp.Backend.UpdateProfileAPI;
import com.esp.chatapp.Bean.PostBean;
import com.esp.chatapp.Bean.UserBean;
import com.esp.chatapp.R;
import com.esp.chatapp.Uc.AlertDailogView;
import com.esp.chatapp.Uc.OnPopUpDialogButoonClickListener;
import com.esp.chatapp.Utils.Config;
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
    private PostBean postBean;
    private Context context;
    private UpdateProfileAPI updateProfileAPI;

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

        if (getIntent().getExtras() != null) {
            postBean = (PostBean) getIntent().getSerializableExtra("beanData");
        } else {
            finish();
        }

        edtName.setText(postBean.name);
        edtEmail.setText(postBean.email);
        edtMobile.setText(postBean.mobile);
        edtCity.setText(postBean.city);
        edtStatus.setText(postBean.status);

        txtupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String valid = validation();
                if (valid == null) {
                    if (Utils.isOnline(context)) {
                        userBean = new UserBean();
                        userBean.name = edtName.getText().toString().trim();
                        userBean.email = edtEmail.getText().toString().trim();
                        userBean.mobile = edtMobile.getText().toString().trim();
                        userBean.city = edtCity.getText().toString().trim();
                        userBean.status = edtStatus.getText().toString().trim();
                        userBean.udID = "";
                        userBean.latlong = "";

                        updateProfileAPI = new UpdateProfileAPI(context, responseListener, userBean);
                        updateProfileAPI.execute();
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
        if (edtEmail.getText().toString().trim().equals("")
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

            if (result == Config.API_SUCCESS) {
                if (tag == Config.TAG_REGISTRATION) {
                    intent = new Intent(EditProfileActivity.this, HomeActivity.class);
                    startActivity(intent);
                    LoginActivity.activity.finish();
                    finish();
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
