package com.rs.timepass.Ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rs.timepass.Backend.ChangePasswordAPI;
import com.rs.timepass.Backend.ResponseListener;
import com.rs.timepass.Bean.UserBean;
import com.rs.timepass.R;
import com.rs.timepass.Uc.AlertDailogView;
import com.rs.timepass.Uc.OnPopUpDialogButoonClickListener;
import com.rs.timepass.Utils.Config;
import com.rs.timepass.Utils.Utils;


public class ChangePasswordActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText edtOldpassword;
    private EditText edtNewpassword;
    private EditText edtConfirmpassword;
    private TextView txtupdate;
    private Intent intent;
    private UserBean userBean;
    private Context context;
    private ChangePasswordAPI changePasswordAPI;
    private LinearLayout myprogressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        context = this;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Change Password");

        edtOldpassword = (EditText) findViewById(R.id.edtOldpassword);
        edtNewpassword = (EditText) findViewById(R.id.edtNewpassword);
        edtConfirmpassword = (EditText) findViewById(R.id.edtConfirmpassword);
        txtupdate = (TextView) findViewById(R.id.txtupdate);
        myprogressBar = (LinearLayout) findViewById(R.id.myprogressBar);


        txtupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String valid = validation();
                if (valid == null) {
                    if (Utils.isOnline(context)) {
                        myprogressBar.setVisibility(View.VISIBLE);
                        userBean = new UserBean();
                        userBean.password = edtOldpassword.getText().toString().trim();
                        userBean.newpassword = edtNewpassword.getText().toString().trim();

                        changePasswordAPI = new ChangePasswordAPI(context, responseListener, userBean);
                        changePasswordAPI.execute();
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
        if (edtOldpassword.getText().toString().trim().equals("")
                || edtOldpassword.getText().toString().trim().equals(null)) {
            valid = getResources().getString(R.string.validblankoldpassword);
            this.edtOldpassword.requestFocus();
            this.edtOldpassword.setSelection(this.edtOldpassword.length());
        } else if (edtOldpassword.getText().toString().trim().length() < 8 || edtOldpassword.getText().toString().trim().length() > 20) {
            valid = getResources().getString(R.string.validlengtholdpassword);
            this.edtOldpassword.requestFocus();
            this.edtOldpassword.setSelection(this.edtOldpassword.length());
        } else if (edtOldpassword.getText().toString().trim().contains(" ")) {
            valid = getResources().getString(R.string.invalidspacepassword);
            this.edtOldpassword.requestFocus();
            this.edtOldpassword.setSelection(this.edtOldpassword.length());
        } else if (edtNewpassword.getText().toString().trim().equals("")
                || edtNewpassword.getText().length() > 15) {
            valid = getResources().getString(R.string.validblanknewpassword);
            this.edtNewpassword.requestFocus();
            this.edtNewpassword.setSelection(this.edtNewpassword.length());
        } else if (edtNewpassword.getText().toString().trim().length() < 8 || edtNewpassword.getText().toString().trim().length() > 20) {
            valid = getResources().getString(R.string.validlengthnewpassword);
            this.edtNewpassword.requestFocus();
            this.edtNewpassword.setSelection(this.edtNewpassword.length());
        } else if (edtNewpassword.getText().toString().trim().contains(" ")) {
            valid = getResources().getString(R.string.invalidspacepassword);
            this.edtNewpassword.requestFocus();
            this.edtNewpassword.setSelection(this.edtNewpassword.length());
        } else if (edtConfirmpassword.getText().toString().trim().equals("")
                || edtConfirmpassword.getText().toString().trim().equals(null)) {
            valid = getResources().getString(R.string.validblankconfirmpassword);
            this.edtConfirmpassword.requestFocus();
            this.edtConfirmpassword.setSelection(this.edtConfirmpassword.length());
        } else if (edtConfirmpassword.getText().toString().trim().length() < 8 || edtConfirmpassword.getText().toString().trim().length() > 20) {
            valid = getResources().getString(R.string.validlengthconfirmpassword);
            this.edtConfirmpassword.requestFocus();
            this.edtConfirmpassword.setSelection(this.edtConfirmpassword.length());
        } else if (edtConfirmpassword.getText().toString().trim().contains(" ")) {
            valid = getResources().getString(R.string.invalidspacepassword);
            this.edtConfirmpassword.requestFocus();
            this.edtConfirmpassword.setSelection(this.edtConfirmpassword.length());
        } else if (edtNewpassword.getText().toString().trim().equals(edtConfirmpassword.getText().toString().trim())) {
            valid = getResources().getString(R.string.notmatchconfirmpassword);
            this.edtConfirmpassword.requestFocus();
            this.edtConfirmpassword.setSelection(this.edtConfirmpassword.length());
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
                if (tag == Config.TAG_CHANGE_PASSWORD) {
                    AlertDailogView.showAlert(ChangePasswordActivity.this, "TimePass", "Successfull update password", "Ok", onPopUpDialogButoonClickListener, 1).show();
                }
            } else {
                AlertDailogView.showAlert(ChangePasswordActivity.this, obj.toString()).show();
            }
        }

        @Override
        public void onResponce(String tag, int result, Object obj, Object obj1) {

        }
    };

    private OnPopUpDialogButoonClickListener onPopUpDialogButoonClickListener = new OnPopUpDialogButoonClickListener() {
        @Override
        public void OnButtonClick(int tag, int buttonIndex, String input) {
            if (tag == 1) {
                finish();
            }
        }
    };

    @Override
    public void onBackPressed() {
        if (myprogressBar.getVisibility() == View.GONE)
            super.onBackPressed();
    }
}
