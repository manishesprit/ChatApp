package com.esp.chatapp.Ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.esp.chatapp.Bean.PostBean;
import com.esp.chatapp.R;
import com.esp.chatapp.Utils.Utils;

public class ChangeAvatarActivity extends AppCompatActivity {

    private Intent intent;
    private Toolbar toolbar;
    private ImageView imgProfileAvatar;
    private TextView txtMobile;
    private TextView txtStatus;
    private Context context;
    private PostBean postBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_avatar);
        context = this;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Change Profile");

        imgProfileAvatar = (ImageView) findViewById(R.id.imgProfileAvatar);
        txtMobile = (TextView) findViewById(R.id.txtMobile);
        txtStatus = (TextView) findViewById(R.id.txtStatus);


        if (getIntent().getExtras() != null) {
            postBean = (PostBean) getIntent().getSerializableExtra("beanData");
        } else {
            finish();
        }

        Utils.setDefaultRoundImage(context, imgProfileAvatar, R.drawable.default_user);

        if (!postBean.avatar.toString().trim().equalsIgnoreCase("")) {
            Glide.with(context).load(postBean.avatar)
                    .asBitmap()
                    .error(R.drawable.default_user).placeholder(R.drawable.default_user).error(R.drawable.default_user).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                    RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    imgProfileAvatar.setImageDrawable(circularBitmapDrawable);
                }
            });
        }

        txtMobile.setText(postBean.mobile);
        txtStatus.setText(postBean.status);

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
}
