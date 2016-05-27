package com.esp.chatapp.Ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;

import com.esp.chatapp.R;
import com.esp.chatapp.Uc.AlertDailogView;
import com.esp.chatapp.Uc.OnPopUpDialogButoonClickListener;
import com.esp.chatapp.Utils.Config;
import com.esp.chatapp.Utils.Pref;
import com.esp.chatapp.Utils.Utils;

public class SplashActivity extends AppCompatActivity implements OnPopUpDialogButoonClickListener {

    private Intent intent;
    private Context context;
    private RelativeLayout rlSplashMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        context = this;
        rlSplashMain = (RelativeLayout) findViewById(R.id.rlSplashMain);
        setHandler();
    }

    public void setHandler() {
        if (Utils.isOnline(context)) {
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
        } else {

            AlertDailogView.showAlert(context, "Internet Error", "Internet not available", "Cancel", true, "Try again", this, 0).show();
        }
    }

    @Override
    public void OnButtonClick(int tag, int buttonIndex, String input) {
        if (tag == 0) {
            if (buttonIndex == 2) {
                setHandler();
            }

            if (buttonIndex == 1) {
                finish();
            }
        }
    }
}
