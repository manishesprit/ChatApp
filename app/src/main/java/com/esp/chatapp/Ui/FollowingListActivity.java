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

import com.esp.chatapp.Adapter.FollowingRecyclerAdapter;
import com.esp.chatapp.Backend.FollowingListAPI;
import com.esp.chatapp.Backend.ResponseListener;
import com.esp.chatapp.Bean.UserBean;
import com.esp.chatapp.R;
import com.esp.chatapp.Uc.AlertDailogView;
import com.esp.chatapp.Uc.OnPopUpDialogButoonClickListener;
import com.esp.chatapp.Utils.Config;
import com.esp.chatapp.Utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class FollowingListActivity extends AppCompatActivity implements OnPopUpDialogButoonClickListener {

    private Toolbar toolbar;
    private Intent intent;
    private Context context;
    private FollowingListAPI followingListAPI;

    private RecyclerView recyclerView;
    private FollowingRecyclerAdapter followingRecyclerAdapter;
    private ArrayList<UserBean> followingBeanArrayList;
    private LinearLayout myprogressBar;
    private TextView txtnoSearchdata;

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
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        Call_Following();

        myprogressBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private void Call_Following() {
        if (Utils.isOnline(context)) {
            myprogressBar.setVisibility(View.VISIBLE);
            followingListAPI = new FollowingListAPI(context, responseListener, getIntent().getIntExtra("userid", 0));
            followingListAPI.execute();
        } else {
            AlertDailogView.showAlert(context, "Internet Error", "Internet not available", "Cancel", true, "Try again", this, 0).show();
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

                    followingBeanArrayList = (ArrayList<UserBean>) obj;
                    if (followingBeanArrayList.size() > 0) {

                        Collections.sort(followingBeanArrayList, new Comparator<UserBean>() {
                                    @Override
                                    public int compare(UserBean lhs, UserBean rhs) {
                                        return lhs.name.compareToIgnoreCase(rhs.name);
                                    }
                                }
                        );

                        followingRecyclerAdapter = new FollowingRecyclerAdapter(context, followingBeanArrayList);
                        recyclerView.setAdapter(followingRecyclerAdapter);
                        txtnoSearchdata.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    } else {
                        txtnoSearchdata.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }
                }
            }
        }
    };

    @Override
    public void OnButtonClick(int tag, int buttonIndex, String input) {
        if (tag == 0) {
            if (buttonIndex == 2) {
                Call_Following();
            }
        }
    }
}
