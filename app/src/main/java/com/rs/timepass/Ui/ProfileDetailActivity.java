package com.rs.timepass.Ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.rs.timepass.Bean.PostBean;
import com.rs.timepass.R;
import com.rs.timepass.Utils.Config;
import com.rs.timepass.Utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by admin on 2/5/16.
 */
public class ProfileDetailActivity extends AppCompatActivity {

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ImageView image;
    private PostBean postBean;
    private Context context;
    private TextView txtMobile;
    private TextView txtStatus;
    private Bitmap bitmap;
    private LinearLayout llDownload;
    private TextView txtDownload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActivityTransitions();
        setContentView(R.layout.activity_profiledetail);
        context = this;

        if (getIntent().getExtras() != null) {
            postBean = (PostBean) getIntent().getSerializableExtra("beanData");
        } else {
            finish();
        }

        ViewCompat.setTransitionName(findViewById(R.id.app_bar_layout), "com.antonioleiva.materializeyourapp.extraImage");
        supportPostponeEnterTransition();

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(postBean.name);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.white));
        image = (ImageView) findViewById(R.id.image);
        llDownload = (LinearLayout) findViewById(R.id.llDownload);
        txtDownload = (TextView) findViewById(R.id.txtDownload);

        txtMobile = (TextView) findViewById(R.id.txtMobile);
        txtStatus = (TextView) findViewById(R.id.txtStatus);


        Glide.with(ProfileDetailActivity.this).load(Config.IMAGE_PATH_WEB_AVATARS + postBean.avatar)
                .asBitmap()
                .error(R.drawable.default_user).placeholder(R.drawable.default_user).error(R.drawable.default_user).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                image.setImageBitmap(resource);
                bitmap = resource;
                Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
                    public void onGenerated(Palette palette) {

                        applyPalette(palette);
                    }
                });
            }
        });

        image.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                llDownload.setVisibility(View.VISIBLE);
                return false;
            }
        });

        llDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llDownload.setVisibility(View.GONE);
            }
        });


        txtDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llDownload.setVisibility(View.GONE);
                Utils.DownloadImage(context, bitmap, "Image_" + new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()).toString() + ".jpg", Config.DIR_USERDATA);
            }
        });


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

    @Override
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        try {
            return super.dispatchTouchEvent(motionEvent);
        } catch (NullPointerException e) {
            return false;
        }
    }

    private void initActivityTransitions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Slide transition = new Slide();
            transition.excludeTarget(android.R.id.statusBarBackground, true);
            getWindow().setEnterTransition(transition);
            getWindow().setReturnTransition(transition);
        }
    }

    private void applyPalette(Palette palette) {
        int vibrantColor = palette.getVibrantColor(getResources().getColor(R.color.color_dark));
        int vibrantDarkColor = palette.getDarkVibrantColor(getResources().getColor(R.color.color_graydark));

//        int primaryDark = getResources().getColor(R.color.color_dark);
//        int primary = getResources().getColor(R.color.color_graydark);

        collapsingToolbarLayout.setContentScrimColor(vibrantColor);
        collapsingToolbarLayout.setStatusBarScrimColor(vibrantDarkColor);

        supportStartPostponedEnterTransition();
    }


}
