package com.esp.chatapp.Ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.LinearLayout;

import com.esp.chatapp.R;
import com.esp.chatapp.Instagram.InstagramApp;

import java.util.HashMap;

/**
 * Created by admin on 2/5/16.
 */
public class MoreActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private InstagramApp instagramApp;

    private Button btnConnect, btnViewInfo, btnGetAllImages;
    private LinearLayout llAfterLoginView;
    private HashMap<String, String> userInfoHashmap = new HashMap<String, String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);

        instagramApp = new InstagramApp(this, getResources().getString(R.string.insta_id), getResources().getString(R.string.insta_secrate), "http://www.google.com");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("More");

    }
}
