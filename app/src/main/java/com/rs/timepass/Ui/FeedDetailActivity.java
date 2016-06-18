package com.rs.timepass.Ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.rs.timepass.Adapter.CommentRecyclerAdapter;
import com.rs.timepass.Backend.AddCommentAPI;
import com.rs.timepass.Backend.FeedDetailAPI;
import com.rs.timepass.Backend.LikeUnlikeAPI;
import com.rs.timepass.Backend.ResponseListener;
import com.rs.timepass.Bean.CommentBean;
import com.rs.timepass.Bean.PostBean;
import com.rs.timepass.R;
import com.rs.timepass.Uc.AlertDailogView;
import com.rs.timepass.Uc.OnPopUpDialogButoonClickListener;
import com.rs.timepass.Utils.Config;
import com.rs.timepass.Utils.Pref;
import com.rs.timepass.Utils.Utils;

import java.util.ArrayList;

public class FeedDetailActivity extends AppCompatActivity implements View.OnClickListener, OnPopUpDialogButoonClickListener {

    private Intent intent;
    private Toolbar toolbar;
    private Context context;
    private FeedDetailAPI feedDetailAPI;
    private LikeUnlikeAPI likeUnlikeAPI;
    private RecyclerView recyclerView;
    private CommentRecyclerAdapter commentRecyclerAdapter;
    private ArrayList<CommentBean> commentBeanArrayList;
    private LinearLayout myprogressBar;
    private PostBean postBean;

    private ImageView imgAvatar;
    private TextView txtUserName;
    private TextView txtFeedTime;
    private TextView txtCaption;
    private ImageView imgFeed;
    private ImageView imgLikeUnlike;
    private TextView txtNoComment;
    private TextView txtNolike;
    private LinearLayout llComment;
    private LinearLayout llLike;
    private EditText edtComment;
    private ImageView imgAdd;
    private AddCommentAPI addCommentAPI;
    private TextView txtNooflikes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_detail);
        context = this;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Feed");

        myprogressBar = (LinearLayout) findViewById(R.id.myprogressBar);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        txtNooflikes = (TextView) findViewById(R.id.txtNooflikes);

        imgAvatar = (ImageView) findViewById(R.id.imgAvatar);
        txtUserName = (TextView) findViewById(R.id.txtUserName);
        txtFeedTime = (TextView) findViewById(R.id.txtFeedTime);
        txtCaption = (TextView) findViewById(R.id.txtCaption);
        imgFeed = (ImageView) findViewById(R.id.imgFeed);
        imgLikeUnlike = (ImageView) findViewById(R.id.imgLikeUnlike);
        txtNoComment = (TextView) findViewById(R.id.txtNoComment);
        txtNolike = (TextView) findViewById(R.id.txtNolike);
        llComment = (LinearLayout) findViewById(R.id.llComment);
        llLike = (LinearLayout) findViewById(R.id.llLike);

        edtComment = (EditText) findViewById(R.id.edtComment);
        imgAdd = (ImageView) findViewById(R.id.imgAddComment);

        Utils.setDefaultRoundImage(context, imgAvatar, R.drawable.default_user);

        Call_Detail();

        myprogressBar.setOnClickListener(this);
        txtUserName.setOnClickListener(this);
        imgAvatar.setOnClickListener(this);
        imgLikeUnlike.setOnClickListener(this);
        llLike.setOnClickListener(this);
        imgAdd.setOnClickListener(this);

        edtComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() > 0) {
                    imgAdd.setImageResource(R.drawable.send_light);
                } else {
                    imgAdd.setImageResource(R.drawable.send_dark);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    private void Call_Detail() {
        if (Utils.isOnline(context)) {
            myprogressBar.setVisibility(View.VISIBLE);
            feedDetailAPI = new FeedDetailAPI(context, responseListener, getIntent().getIntExtra("feedid", 0));
            feedDetailAPI.execute();
        } else {
            AlertDailogView.showAlert(context, "Internet Error", "Internet not available", "Cancel", true, "Try again", this, 0).show();
        }
    }

    private ResponseListener responseListener = new ResponseListener() {
        public void onResponce(String tag, int result, Object obj, Object obj1) {
            myprogressBar.setVisibility(View.GONE);
            if (result == Config.API_SUCCESS) {
                if (tag.equals(Config.TAG_LIKEUNLIKE)) {
                    postBean.islike = (int) obj == 0 ? false : true;
                    postBean.noOflike = (int) obj1;
                    txtNolike.setText("" + postBean.noOflike);
                    imgLikeUnlike.setImageResource(postBean.islike == true ? R.drawable.love_white_filled : R.drawable.love_gray);
                }
            } else {

            }
        }

        public void onResponce(String tag, int result, Object obj) {
            myprogressBar.setVisibility(View.GONE);
            if (result == Config.API_SUCCESS) {
                if (tag == Config.TAG_FEED_DETAIL) {
                    postBean = (PostBean) obj;
                    setdata();
                }
                if (tag == Config.TAG_ADD_COMMENT) {
                    edtComment.setText("");
                    imgAdd.setImageResource(R.drawable.send_dark);
                    postBean.commentBeanArrayList = (ArrayList<CommentBean>) obj;
                    txtNoComment.setText("" + postBean.commentBeanArrayList.size());
                    if (postBean.commentBeanArrayList.size() > 0) {
                        txtNooflikes.setVisibility(View.VISIBLE);
                        txtNooflikes.setText(postBean.commentBeanArrayList.size() + " People comment");
                        commentRecyclerAdapter = new CommentRecyclerAdapter(context, postBean.commentBeanArrayList);
                        recyclerView.setAdapter(commentRecyclerAdapter);
                    } else {
                        txtNooflikes.setVisibility(View.GONE);
                    }
                }
            } else {

            }
        }
    };

    private void setdata() {

        if (!postBean.avatar.toString().trim().equalsIgnoreCase("")) {
            Glide.with(context).load(Config.IMAGE_PATH_WEB_AVATARS + postBean.avatar)
                    .asBitmap()
                    .error(R.drawable.default_user).placeholder(R.drawable.default_user).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                    RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    imgAvatar.setImageDrawable(circularBitmapDrawable);
                }
            });
        }

        txtUserName.setText(postBean.name);
        txtUserName.setTag(postBean);
        txtFeedTime.setText(postBean.posttime);
        txtNolike.setText("" + postBean.noOflike);
        txtNoComment.setText("" + postBean.commentBeanArrayList.size());


        if (postBean.caption.trim().toString().equalsIgnoreCase("")) {
            txtCaption.setVisibility(View.GONE);
        } else {
            txtCaption.setVisibility(View.VISIBLE);
            txtCaption.setText(postBean.caption);
        }

        if (!postBean.image_url.toString().equals("")) {
            imgFeed.setVisibility(View.VISIBLE);
            Glide.with(context).load(Config.IMAGE_PATH_WEB_FEED + postBean.image_url).asBitmap().error(R.drawable.default_user).placeholder(R.drawable.default_user).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                    imgFeed.setImageBitmap(resource);
                }
            });
        } else {
            imgFeed.setVisibility(View.GONE);
        }

        imgLikeUnlike.setSoundEffectsEnabled(false);
        imgLikeUnlike.setImageResource(postBean.islike == true ? R.drawable.love_white_filled : R.drawable.love_gray);

        if (postBean.commentBeanArrayList.size() > 0) {
            txtNooflikes.setVisibility(View.VISIBLE);
            txtNooflikes.setText(postBean.commentBeanArrayList.size() + " People comment");
            commentRecyclerAdapter = new CommentRecyclerAdapter(context, postBean.commentBeanArrayList);
            recyclerView.setAdapter(commentRecyclerAdapter);
        } else {
            txtNooflikes.setVisibility(View.GONE);
        }

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
    public void OnButtonClick(int tag, int buttonIndex, String input) {
        if (tag == 0) {
            if (buttonIndex == 2) {
                Call_Detail();
            }

            if (buttonIndex == 1) {
                finish();
            }
        }

        if (tag == 1) {
            if (buttonIndex == 2) {
                imgLikeUnlike.performClick();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.myprogressBar:
                break;

            case R.id.txtUserName:

                if (((PostBean) txtUserName.getTag()).userid != Pref.getValue(context, Config.PREF_USER_ID, 0)) {
                    Intent intent = new Intent(context, ProfileActivity.class);
                    intent.putExtra("beanData", (PostBean) txtUserName.getTag());
                    context.startActivity(intent);
                }

                break;

            case R.id.imgAvatar:

                if (((PostBean) txtUserName.getTag()).userid != Pref.getValue(context, Config.PREF_USER_ID, 0)) {
                    Intent intent = new Intent(context, ProfileActivity.class);
                    intent.putExtra("beanData", (PostBean) txtUserName.getTag());
                    context.startActivity(intent);
                }

                break;

            case R.id.imgLikeUnlike:

                if (Utils.isOnline(context)) {
                    myprogressBar.setVisibility(View.VISIBLE);
                    likeUnlikeAPI = new LikeUnlikeAPI(context, responseListener, ((PostBean) txtUserName.getTag()).feedid);
                    likeUnlikeAPI.execute();
                } else {
                    AlertDailogView.showAlert(context, "Internet Error", "Internet not available", "Cancel", true, "Try again", this, 1).show();
                }

                break;

            case R.id.llLike:

                if (((PostBean) txtUserName.getTag()).noOflike > 0) {
                    Intent intent = new Intent(context, LikeListActivity.class);
                    intent.putExtra("feedid", ((PostBean) txtUserName.getTag()).feedid);
                    context.startActivity(intent);
                }

                break;

            case R.id.imgAddComment:
                if (edtComment.getText().toString().trim().length() > 0) {
                    if (Utils.isOnline(context)) {
                        myprogressBar.setVisibility(View.VISIBLE);
                        addCommentAPI = new AddCommentAPI(context, responseListener, ((PostBean) txtUserName.getTag()).feedid, Uri.encode(edtComment.getText().toString().trim()));
                        addCommentAPI.execute();
                    }
                }

                break;
        }
    }
}
