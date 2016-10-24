package com.rs.timepass.Ui;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.rs.timepass.R;
import com.rs.timepass.Utils.Config;
import com.rs.timepass.Utils.Pref;
import com.rs.timepass.Utils.Utils;


/**
 * Created by admin on 2/5/16.
 */
public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private TextView txtEditProfile;
    private TextView txtChangePassword;
    private TextView txtLogout;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Setting");

        txtEditProfile = (TextView) findViewById(R.id.txtEditProfile);
        txtChangePassword = (TextView) findViewById(R.id.txtChangePassword);
        txtLogout = (TextView) findViewById(R.id.txtLogout);

        if (Pref.getValue(this, Config.PREF_USER_FB_ID, "").equalsIgnoreCase("")) {
            txtChangePassword.setVisibility(View.VISIBLE);
        } else {
            txtChangePassword.setVisibility(View.GONE);
        }

        txtEditProfile.setOnClickListener(this);
        txtChangePassword.setOnClickListener(this);
        txtLogout.setOnClickListener(this);

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtEditProfile:
                intent = new Intent(SettingActivity.this, EditProfileActivity.class);
                startActivity(intent);
                break;

            case R.id.txtChangePassword:
                intent = new Intent(SettingActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
                break;

            case R.id.txtLogout:
                NotificationManager nMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                nMgr.cancelAll();
                Utils.ClearPref(SettingActivity.this);
                HomeActivity.activityHome.finish();
                finish();
                intent = new Intent(SettingActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
        }
    }
}
