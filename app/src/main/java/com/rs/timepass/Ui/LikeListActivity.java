package com.rs.timepass.Ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rs.timepass.Adapter.LikeRecyclerAdapter;
import com.rs.timepass.Backend.LikeListAPI;
import com.rs.timepass.Backend.ResponseListener;
import com.rs.timepass.Bean.LikeBean;
import com.rs.timepass.R;
import com.rs.timepass.Uc.AlertDailogView;
import com.rs.timepass.Uc.OnPopUpDialogButoonClickListener;
import com.rs.timepass.Utils.Config;
import com.rs.timepass.Utils.Utils;

import java.util.ArrayList;

/**
 * Created by admin on 2/5/16.
 */
public class LikeListActivity extends Activity implements OnPopUpDialogButoonClickListener {


    private Intent intent;
    private Context context;
    private LikeListAPI likeListAPI;

    private RecyclerView recyclerView;
    private LikeRecyclerAdapter likeRecyclerAdapter;
    private ArrayList<LikeBean> likeBeanArrayList;
    private LinearLayout myprogressBar;
    private TextView txtNooflikes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_likelist);
        context = this;


        myprogressBar = (LinearLayout) findViewById(R.id.myprogressBar);
        txtNooflikes = (TextView) findViewById(R.id.txtNooflikes);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        Call_Like();

        myprogressBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private void Call_Like() {
        if (Utils.isOnline(context)) {
            myprogressBar.setVisibility(View.VISIBLE);
            likeListAPI = new LikeListAPI(context, responseListener, getIntent().getIntExtra("feedid", 0));
            likeListAPI.execute();
        } else {
            AlertDailogView.showAlert(context, "Internet Error", "Internet not available", "Cancel", true, "Try again", this, 0).show();
        }
    }

    private ResponseListener responseListener = new ResponseListener() {
        public void onResponce(String tag, int result, Object obj, Object obj1) {

        }

        public void onResponce(String tag, int result, Object obj) {
            myprogressBar.setVisibility(View.GONE);
            if (result == Config.API_SUCCESS) {
                if (tag == Config.TAG_LIKE_LIST) {

                    likeBeanArrayList = (ArrayList<LikeBean>) obj;
                    if (likeBeanArrayList.size() > 0) {
                        likeRecyclerAdapter = new LikeRecyclerAdapter(context, likeBeanArrayList);
                        recyclerView.setAdapter(likeRecyclerAdapter);
                        txtNooflikes.setText(likeBeanArrayList.size() + " People like");
                    } else {

                    }
                }
            } else {

            }
        }
    };

    @Override
    public void OnButtonClick(int tag, int buttonIndex, String input) {
        if (tag == 0) {
            if (buttonIndex == 2) {
                Call_Like();
            }

            if (buttonIndex == 1) {
                finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (myprogressBar.getVisibility() == View.GONE)
            super.onBackPressed();
    }
}
