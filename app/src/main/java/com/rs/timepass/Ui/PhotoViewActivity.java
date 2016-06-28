package com.rs.timepass.Ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.rs.timepass.Bean.PostBean;
import com.rs.timepass.R;
import com.rs.timepass.Utils.Config;

public class PhotoViewActivity extends AppCompatActivity{

    private Context context;
    private TextView txtFeedTime;
    private TextView txtCaption;
    private ImageView imgFeed;
    private TextView txtNolike;
    private TextView txtNoComment;

    private Bitmap feedBitmap;
    private PostBean postBean;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photoview);
        context = this;

        imgFeed = (ImageView) findViewById(R.id.imgFeed);
        txtCaption = (TextView) findViewById(R.id.txtCaption);
        txtFeedTime = (TextView) findViewById(R.id.txtFeedTime);
        txtNolike = (TextView) findViewById(R.id.txtNolike);
        txtNoComment = (TextView) findViewById(R.id.txtNoComment);

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

                return true;
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
