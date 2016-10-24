package com.rs.timepass.Ui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.widget.RelativeLayout;

import com.rs.timepass.R;
import com.rs.timepass.Uc.AlertDailogView;
import com.rs.timepass.Uc.OnPopUpDialogButoonClickListener;
import com.rs.timepass.Utils.Config;
import com.rs.timepass.Utils.Log;
import com.rs.timepass.Utils.Pref;
import com.rs.timepass.Utils.Utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SplashActivity extends AppCompatActivity implements OnPopUpDialogButoonClickListener {

    private Intent intent;
    private Context context;
    private RelativeLayout rlSplashMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        context = this;

        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.esp.chatapp", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.print("KeyHash:" + Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }


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
