package com.esp.chatapp.Ui;

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

import com.esp.chatapp.Adapter.FollowerRecyclerAdapter;
import com.esp.chatapp.Adapter.MyOnClickListner;
import com.esp.chatapp.Backend.FollowerListAPI;
import com.esp.chatapp.Backend.FollowerUnfollowerAPI;
import com.esp.chatapp.Backend.ResponseListener;
import com.esp.chatapp.Bean.UserBean;
import com.esp.chatapp.R;
import com.esp.chatapp.Uc.AlertDailogView;
import com.esp.chatapp.Uc.OnPopUpDialogButoonClickListener;
import com.esp.chatapp.Utils.Config;
import com.esp.chatapp.Utils.Utils;

import java.util.ArrayList;

public class FollowerListActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Intent intent;
    private Context context;
    private FollowerListAPI followerListAPI;

    private RecyclerView recyclerView;
    private FollowerRecyclerAdapter followerRecyclerAdapter;
    private ArrayList<UserBean> followerBeanArrayList;
    private LinearLayout myprogressBar;
    private TextView txtnoSearchdata;
    private FollowerUnfollowerAPI followerUnfollowerAPI;
    private UserBean userBean;

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
        setTitle("Follower");

        myprogressBar = (LinearLayout) findViewById(R.id.myprogressBar);
        txtnoSearchdata = (TextView) findViewById(R.id.txtnoSearchdata);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        followerBeanArrayList = new ArrayList<>();

        followerRecyclerAdapter = new FollowerRecyclerAdapter(context, followerBeanArrayList, myOnClickListner);
        recyclerView.setAdapter(followerRecyclerAdapter);
        Call_Follower();

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
                            Call_Follower();
                        }
                    }

                }
            }
        });

    }

    private void Call_Follower() {
        if (Utils.isOnline(context)) {
            myprogressBar.setVisibility(View.VISIBLE);
            followerListAPI = new FollowerListAPI(context, responseListener, getIntent().getIntExtra("userid", 0), offset, limit);
            followerListAPI.execute();
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
                if (tag == Config.TAG_FOLLOWER_LIST) {

                    ArrayList<UserBean> followerlist = (ArrayList<UserBean>) obj;
                    if (followerlist.size() > 0) {
                        offset = offset + limit;
                        loading = true;
                        followerBeanArrayList.addAll(followerlist);
                    }

                    if (followerBeanArrayList.size() > 0) {
                        followerRecyclerAdapter.notifyDataSetChanged();
                        txtnoSearchdata.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    } else {
                        txtnoSearchdata.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }
                }

                if (tag == Config.TAG_FOLLOWER_UNFOLLOWER) {
                    userBean.isFollower = userBean.isFollower == true ? false : true;
                    followerRecyclerAdapter.notifyDataSetChanged();
                }

            }
        }
    };

    private OnPopUpDialogButoonClickListener onPopUpDialogButoonClickListener = new OnPopUpDialogButoonClickListener() {
        @Override
        public void OnButtonClick(int tag, int buttonIndex, String input) {
            if (tag == 0) {
                if (buttonIndex == 2) {
                    Call_Follower();
                }
            }

            if (tag == 1) {
                if (buttonIndex == 2) {
                    if (Utils.isOnline(context)) {
                        myprogressBar.setVisibility(View.VISIBLE);
                        followerUnfollowerAPI = new FollowerUnfollowerAPI(context, responseListener, userBean.userid, userBean.isFollower == true ? 1 : 0);
                        followerUnfollowerAPI.execute();
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
                AlertDailogView.showAlert(context, "Alert", userBean.name + " unfollow ?", "Cancel", true, "Unfollow", onPopUpDialogButoonClickListener, 1).show();
            }
            return false;
        }

    };
}
