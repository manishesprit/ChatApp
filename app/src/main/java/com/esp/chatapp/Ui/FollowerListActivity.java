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
import com.esp.chatapp.Backend.FollowerListAPI;
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

public class FollowerListActivity extends AppCompatActivity implements OnPopUpDialogButoonClickListener {

    private Toolbar toolbar;
    private Intent intent;
    private Context context;
    private FollowerListAPI followerListAPI;

    private RecyclerView recyclerView;
    private FollowerRecyclerAdapter followerRecyclerAdapter;
    private ArrayList<UserBean> followerBeanArrayList;
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
        setTitle("Follower");


        myprogressBar = (LinearLayout) findViewById(R.id.myprogressBar);
        txtnoSearchdata = (TextView) findViewById(R.id.txtnoSearchdata);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        Call_Follower();

        myprogressBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private void Call_Follower() {
        if (Utils.isOnline(context)) {
            myprogressBar.setVisibility(View.VISIBLE);
            followerListAPI = new FollowerListAPI(context, responseListener, getIntent().getIntExtra("userid", 0));
            followerListAPI.execute();
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
                if (tag == Config.TAG_FOLLOWER_LIST) {

                    followerBeanArrayList = (ArrayList<UserBean>) obj;
                    if (followerBeanArrayList.size() > 0) {

                        Collections.sort(followerBeanArrayList, new Comparator<UserBean>() {
                                    @Override
                                    public int compare(UserBean lhs, UserBean rhs) {
                                        return lhs.name.compareToIgnoreCase(rhs.name);
                                    }
                                }
                        );

                        followerRecyclerAdapter = new FollowerRecyclerAdapter(context, followerBeanArrayList);
                        recyclerView.setAdapter(followerRecyclerAdapter);
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
                Call_Follower();
            }
        }
    }
}
