package com.rs.timepass.Ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rs.timepass.Adapter.FollowingRecyclerAdapter;
import com.rs.timepass.Adapter.MyOnClickListner;
import com.rs.timepass.Backend.FollowingListAPI;
import com.rs.timepass.Backend.FollowingUnfollowingAPI;
import com.rs.timepass.Backend.ResponseListener;
import com.rs.timepass.Bean.UserBean;
import com.rs.timepass.R;
import com.rs.timepass.Uc.AlertDailogView;
import com.rs.timepass.Uc.OnPopUpDialogButoonClickListener;
import com.rs.timepass.Utils.Config;
import com.rs.timepass.Utils.Utils;

import java.util.ArrayList;

public class FollowingListActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Intent intent;
    private Context context;
    private FollowingListAPI followingListAPI;

    private RecyclerView recyclerView;
    private FollowingRecyclerAdapter followingRecyclerAdapter;
    private ArrayList<UserBean> followingBeanArrayList;
    private LinearLayout myprogressBar;
    private TextView txtnoSearchdata;
    private UserBean userBean;
    private FollowingUnfollowingAPI followingUnfollowingAPI;
    private int limit = 50;
    private int offset = 0;

    private LinearLayoutManager linearLayoutManager;
    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follower);
        context = this;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Following");


        myprogressBar = (LinearLayout) findViewById(R.id.myprogressBar);
        txtnoSearchdata = (TextView) findViewById(R.id.txtnoSearchdata);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        followingBeanArrayList = new ArrayList<>();
        followingRecyclerAdapter = new FollowingRecyclerAdapter(context, followingBeanArrayList, myOnClickListner);
        recyclerView.setAdapter(followingRecyclerAdapter);

        Call_Following();

        myprogressBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) //check for scroll down
                {
                    visibleItemCount = linearLayoutManager.getChildCount();
                    totalItemCount = linearLayoutManager.getItemCount();
                    pastVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false;
                            Call_Following();
                        }
                    }

                }
            }
        });

    }

    private void Call_Following() {
        if (Utils.isOnline(context)) {
            myprogressBar.setVisibility(View.VISIBLE);
            followingListAPI = new FollowingListAPI(context, responseListener, getIntent().getIntExtra("userid", 0), offset, limit);
            followingListAPI.execute();
        } else {
            AlertDailogView.showAlert(context, "Internet Error", "Internet not available", "Cancel", true, "Try again", onPopUpDialogButoonClickListener, 0).show();
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

        }

        public void onResponce(String tag, int result, Object obj) {

            myprogressBar.setVisibility(View.GONE);
            if (result == Config.API_SUCCESS) {
                if (tag == Config.TAG_FOLLOWING_LIST) {

                    ArrayList<UserBean> followinglist = (ArrayList<UserBean>) obj;
                    if (followinglist.size() > 0) {
                        offset = offset + limit;
                        loading = true;
                        followingBeanArrayList.addAll(followinglist);
                    }

                    if (followingBeanArrayList.size() > 0) {
                        followingRecyclerAdapter.notifyDataSetChanged();
                        txtnoSearchdata.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    } else {
                        txtnoSearchdata.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }

                }
                if (tag == Config.TAG_FOLLOWING_UNFOLLOWING) {
                    userBean.isFollowing = userBean.isFollowing == true ? false : true;
                    followingRecyclerAdapter.notifyDataSetChanged();
                }
            }
        }
    };

    private OnPopUpDialogButoonClickListener onPopUpDialogButoonClickListener = new OnPopUpDialogButoonClickListener() {
        @Override
        public void OnButtonClick(int tag, int buttonIndex, String input) {
            if (tag == 0) {
                if (buttonIndex == 2) {
                    Call_Following();
                }
            }

            if (tag == 1) {
                if (buttonIndex == 2) {
                    if (Utils.isOnline(context)) {
                        myprogressBar.setVisibility(View.VISIBLE);
                        followingUnfollowingAPI = new FollowingUnfollowingAPI(context, responseListener, userBean.userid, userBean.isFollowing == true ? 1 : 0);
                        followingUnfollowingAPI.execute();
                    }
                }
            }
        }
    };

    MyOnClickListner myOnClickListner = new MyOnClickListner() {
        @Override
        public boolean IsClick(int id, Object object) {

            if (id == R.id.txtfollowUnfollow) {
                userBean = (UserBean) object;
                AlertDailogView.showAlert(context, "Alert", userBean.name + (userBean.isFollowing == true ? " unfollow ?" : " follow ?"), "Cancel", true, (userBean.isFollowing == true ? "Unfollow" : "Follow"), onPopUpDialogButoonClickListener, 1).show();
            }
            return false;
        }

    };

    @Override
    public void onBackPressed() {
        if (myprogressBar.getVisibility() == View.GONE)
            super.onBackPressed();
    }
}
