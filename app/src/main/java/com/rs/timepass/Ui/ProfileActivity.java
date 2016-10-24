package com.rs.timepass.Ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.rs.timepass.Adapter.MyOnClickListner;
import com.rs.timepass.Adapter.ProfileRecyclerAdapter;
import com.rs.timepass.Backend.DeleteFeedAPI;
import com.rs.timepass.Backend.FeedListAPI;
import com.rs.timepass.Backend.FollowingUnfollowingAPI;
import com.rs.timepass.Backend.LikeUnlikeAPI;
import com.rs.timepass.Backend.ResponseListener;
import com.rs.timepass.Bean.PostBean;
import com.rs.timepass.Bean.UserBean;
import com.rs.timepass.R;
import com.rs.timepass.Uc.AlertDailogView;
import com.rs.timepass.Uc.OnPopUpDialogButoonClickListener;
import com.rs.timepass.Utils.Config;
import com.rs.timepass.Utils.Utils;

import java.util.ArrayList;

/**
 * Created by admin on 2/5/16.
 */
public class ProfileActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private ProfileRecyclerAdapter profileRecyclerAdapter;
    private Context context;
    private ArrayList<PostBean> postlist;
    private PostBean postBean;
    private UserBean userBean;
    private FeedListAPI feedListAPI;
    private LikeUnlikeAPI likeUnlikeAPI;
    private LinearLayout myprogressBar;
    private int limit = 30;
    private int offset = 0;
    private SwipeRefreshLayout swipeContainer;
    private PostBean postBeanTemp;
    private FollowingUnfollowingAPI followingUnfollowingAPI;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        context = this;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Profile");

        if (getIntent().getExtras() != null) {
            postBean = (PostBean) getIntent().getSerializableExtra("beanData");
        } else {
            finish();
        }

        myprogressBar = (LinearLayout) findViewById(R.id.myprogressBar);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setColorScheme(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        postlist = new ArrayList<>();

        postlist.add(postBean);
        profileRecyclerAdapter = new ProfileRecyclerAdapter(context, postlist, myOnClickListner);
        recyclerView.setAdapter(profileRecyclerAdapter);

        CallFeedList();

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeContainer.setRefreshing(false);
                offset = 0;
                CallFeedList();
            }
        });


        myprogressBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void CallFeedList() {
        if (Utils.isOnline(context)) {
            myprogressBar.setVisibility(View.VISIBLE);
            userBean = new UserBean();
            userBean.userid = postBean.userid;
            userBean.myFeed = true;
            userBean.offset = offset;
            userBean.limit = limit;
            feedListAPI = new FeedListAPI(context, responseListener, userBean);
            feedListAPI.execute();
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

    private ResponseListener responseListener = new ResponseListener() {
        public void onResponce(String tag, int result, Object obj, Object obj1) {
            myprogressBar.setVisibility(View.GONE);
            if (result == Config.API_SUCCESS) {
                if (tag.equals(Config.TAG_FEED_LIST)) {

                    postlist.get(0).userid = ((PostBean) obj1).userid;
                    postlist.get(0).name = ((PostBean) obj1).name;
                    postlist.get(0).avatar = ((PostBean) obj1).avatar;
                    postlist.get(0).status = ((PostBean) obj1).status;
                    postlist.get(0).noOffollowers = ((PostBean) obj1).noOffollowers;
                    postlist.get(0).noOffollowing = ((PostBean) obj1).noOffollowing;
                    postlist.get(0).noOfpost = ((PostBean) obj1).noOfpost;
                    postlist.get(0).mobile = ((PostBean) obj1).mobile;
                    postlist.get(0).email = ((PostBean) obj1).email;
                    postlist.get(0).isFollowing = ((PostBean) obj1).isFollowing;

                    ArrayList<PostBean> postBeanArrayList = (ArrayList<PostBean>) obj;
                    if (postBeanArrayList.size() > 0) {
                        offset = offset + limit;
                        postlist.addAll(postBeanArrayList);

                    }
                    profileRecyclerAdapter.notifyDataSetChanged();
                } else if (tag.equals(Config.TAG_LIKEUNLIKE)) {
                    postBeanTemp.islike = (int) obj == 0 ? false : true;
                    postBeanTemp.noOflike = (int) obj1;
                    profileRecyclerAdapter.notifyDataSetChanged();
                }
            }
        }

        public void onResponce(String tag, int result, Object obj) {
            myprogressBar.setVisibility(View.GONE);
            if (result == Config.API_SUCCESS) {
                if (tag == Config.TAG_FOLLOWING_UNFOLLOWING) {
                    postlist.get(0).isFollowing = postBeanTemp.isFollowing == true ? false : true;
                    profileRecyclerAdapter.notifyDataSetChanged();
                }
            }
        }
    };

    MyOnClickListner myOnClickListner = new MyOnClickListner() {
        @Override
        public boolean IsClick(int id, Object object) {
            if (id == R.id.imgLikeUnlike) {
                if (Utils.isOnline(context)) {
                    myprogressBar.setVisibility(View.VISIBLE);
                    postBeanTemp = (PostBean) object;
                    likeUnlikeAPI = new LikeUnlikeAPI(context, responseListener, postBeanTemp.feedid);
                    likeUnlikeAPI.execute();
                }
            } else if (id == R.id.txtfollowUnfollow) {
                postBeanTemp = (PostBean) object;
                AlertDailogView.showAlert(context, "Alert", postBeanTemp.name + (postBeanTemp.isFollowing == true ? " unfollow ?" : " follow ?"), "Cancel", true, (postBeanTemp.isFollowing == true ? "Unfollow" : "Follow"), onPopUpDialogButoonClickListener, 1).show();
            }
            return false;
        }

    };

    private OnPopUpDialogButoonClickListener onPopUpDialogButoonClickListener = new OnPopUpDialogButoonClickListener() {
        @Override
        public void OnButtonClick(int tag, int buttonIndex, String input) {
            if (tag == 1) {
                if (buttonIndex == 2) {
                    if (Utils.isOnline(context)) {
                        myprogressBar.setVisibility(View.VISIBLE);
                        followingUnfollowingAPI = new FollowingUnfollowingAPI(context, responseListener, postBeanTemp.userid, postBeanTemp.isFollowing == true ? 1 : 0);
                        followingUnfollowingAPI.execute();
                    }
                }
            }
        }
    };

    @Override
    public void onBackPressed() {
        if (myprogressBar.getVisibility() == View.GONE)
            super.onBackPressed();
    }
}
