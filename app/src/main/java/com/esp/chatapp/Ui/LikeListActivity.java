package com.esp.chatapp.Ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.esp.chatapp.Adapter.LikeRecyclerAdapter;
import com.esp.chatapp.Backend.LikeListAPI;
import com.esp.chatapp.Backend.ResponseListener;
import com.esp.chatapp.Bean.LikeBean;
import com.esp.chatapp.R;
import com.esp.chatapp.Utils.Config;
import com.esp.chatapp.Utils.Utils;

import java.util.ArrayList;

/**
 * Created by admin on 2/5/16.
 */
public class LikeListActivity extends Activity implements View.OnClickListener {


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

        if (Utils.isOnline(context)) {
            myprogressBar.setVisibility(View.VISIBLE);
            likeListAPI = new LikeListAPI(context, responseListener, getIntent().getIntExtra("feedid", 0));
            likeListAPI.execute();
        }

        myprogressBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }


    @Override
    public void onClick(View v) {

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
                        txtNooflikes.setText(likeBeanArrayList.size()+" People like");
                    } else {

                    }
                }
            } else {

            }
        }
    };
}
