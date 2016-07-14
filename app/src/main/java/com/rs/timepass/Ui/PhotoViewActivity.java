package com.rs.timepass.Ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

public class PhotoViewActivity extends AppCompatActivity {

    private Context context;
    private TextView txtFeedTime;
    private TextView txtCaption;
    private ImageView imgFeed;
    private TextView txtNolike;
    private TextView txtNoComment;

    private Bitmap feedBitmap;
    private PostBean postBean;
    private RelativeLayout rlFeedFooter;
    private Toolbar toolbar;
    private LinearLayout llDownload;
    private TextView txtDownload;


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photoview);
        context = this;

        postBean = (PostBean) getIntent().getSerializableExtra("beanData");

        imgFeed = (ImageView) findViewById(R.id.imgFeed);
        txtCaption = (TextView) findViewById(R.id.txtCaption);
        txtFeedTime = (TextView) findViewById(R.id.txtFeedTime);
        txtNolike = (TextView) findViewById(R.id.txtNolike);
        txtNoComment = (TextView) findViewById(R.id.txtNoComment);
        rlFeedFooter = (RelativeLayout) findViewById(R.id.rlFeedFooter);
        llDownload = (LinearLayout) findViewById(R.id.llDownload);
        txtDownload = (TextView) findViewById(R.id.txtDownload);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(postBean.name);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setStatusBarColor(Color.parseColor("#000000"));
        }


        if (!postBean.image_url.equals("")) {
            Glide.with(context).load(Config.IMAGE_PATH_WEB_FEED + postBean.image_url).asBitmap().error(R.drawable.default_user).placeholder(R.drawable.default_user).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                    feedBitmap = resource;
                    imgFeed.setImageBitmap(resource);
                }
            });
        }

        txtFeedTime.setText(postBean.posttime);
        txtNolike.setText("" + postBean.noOflike);
        txtNoComment.setText("" + postBean.commentBeanArrayList.size());
        txtCaption.setText(postBean.caption);

        imgFeed.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                toolbar.setVisibility(View.GONE);
                rlFeedFooter.setVisibility(View.GONE);
                llDownload.setVisibility(View.VISIBLE);
                return true;
            }
        });
        txtDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llDownload.setVisibility(View.GONE);
                Utils.DownloadImage(context, feedBitmap, "Image_" + new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()).toString() + ".jpg", Config.DIR_FEEDDATA);
            }
        });

        llDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llDownload.setVisibility(View.GONE);
            }
        });

        imgFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toolbar.getVisibility() == View.VISIBLE) {
                    toolbar.setVisibility(View.GONE);
                    rlFeedFooter.setVisibility(View.GONE);
                } else {
                    toolbar.setVisibility(View.VISIBLE);
                    rlFeedFooter.setVisibility(View.VISIBLE);
                }
            }
        });

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
