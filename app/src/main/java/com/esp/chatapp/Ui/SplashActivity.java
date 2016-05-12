package com.esp.chatapp.Ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.esp.chatapp.R;
import com.esp.chatapp.Utils.Config;
import com.esp.chatapp.Utils.Pref;

public class SplashActivity extends AppCompatActivity {

    private Intent intent;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        context = this;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Pref.getValue(context, Config.PREF_USER_ID, 0) == 0) {
                    intent = new Intent(context, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    intent = new Intent(context, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 2000);

    }
}
