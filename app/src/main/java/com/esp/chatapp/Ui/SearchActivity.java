package com.esp.chatapp.Ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.esp.chatapp.Adapter.SearchRecyclerAdapter;
import com.esp.chatapp.Backend.FollowingUnfollowingAPI;
import com.esp.chatapp.Backend.ResponseListener;
import com.esp.chatapp.Backend.SearchListAPI;
import com.esp.chatapp.Bean.UserBean;
import com.esp.chatapp.R;
import com.esp.chatapp.Uc.AlertDailogView;
import com.esp.chatapp.Uc.OnPopUpDialogButoonClickListener;
import com.esp.chatapp.Utils.Config;
import com.esp.chatapp.Utils.Utils;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener, OnPopUpDialogButoonClickListener {

    private Intent intent;
    private Toolbar toolbar;
    private Context context;
    private SearchListAPI searchListAPI;
    private EditText edtSearch;
    private TextView txtnoSearchdata;
    private TextView txtnoofsearch;
    private RecyclerView recyclerView;
    private SearchRecyclerAdapter searchRecyclerAdapter;
    private ArrayList<UserBean> searchBeanArrayList;
    private LinearLayout myprogressBar;
    private UserBean userBean;
    private FollowingUnfollowingAPI followingUnfollowingAPI;
    private String searchstr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        context = this;

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setBackgroundColor(getResources().getColor(R.color.color_white));
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.color_dark), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edtSearch = (EditText) findViewById(R.id.edtSearch);
        txtnoSearchdata = (TextView) findViewById(R.id.txtnoSearchdata);
        txtnoofsearch = (TextView) findViewById(R.id.txtnoofsearch);
        myprogressBar = (LinearLayout) findViewById(R.id.myprogressBar);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 2));

        Call_Search();

        myprogressBar.setOnClickListener(this);

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 2) {
                    if (searchListAPI != null) {
                        searchListAPI.doCancel();
                    }
                    searchstr = s.toString();
                    Call_Search();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

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

    private ResponseListener responseListener = new ResponseListener() {
        public void onResponce(String tag, int result, Object obj, Object obj1) {

        }

        public void onResponce(String tag, int result, Object obj) {
            myprogressBar.setVisibility(View.GONE);
            if (result == Config.API_SUCCESS) {
                if (tag == Config.TAG_SEARCH_LIST) {

                    searchBeanArrayList = (ArrayList<UserBean>) obj;
                    if (searchBeanArrayList.size() > 0) {
                        txtnoofsearch.setText(searchBeanArrayList.size() + " result found");
                        txtnoofsearch.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.VISIBLE);
                        txtnoSearchdata.setVisibility(View.GONE);
                        searchRecyclerAdapter = new SearchRecyclerAdapter(context, searchBeanArrayList);
                        recyclerView.setAdapter(searchRecyclerAdapter);
                    } else {
                        recyclerView.setVisibility(View.GONE);
                        txtnoSearchdata.setVisibility(View.VISIBLE);
                        txtnoofsearch.setVisibility(View.GONE);
                    }
                }
                if (tag == Config.TAG_FOLLOWING_UNFOLLOWING) {
                    userBean.isFollowing = userBean.isFollowing == true ? false : true;
                    searchRecyclerAdapter.notifyDataSetChanged();
                }
            } else {

            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtfollowUnfollow:
                userBean = (UserBean) v.getTag();

                Call_Follow_Unfollow();

                break;

            case R.id.myprogressBar:
                break;
        }
    }

    private void Call_Search() {
        if (Utils.isOnline(context)) {

            myprogressBar.setVisibility(View.VISIBLE);
            searchListAPI = new SearchListAPI(context, responseListener, searchstr);
            searchListAPI.execute();
        } else {
            AlertDailogView.showAlert(context, "Internet Error", "Internet not available", "Cancel", true, "Try again", this, 1).show();
        }
    }

    private void Call_Follow_Unfollow() {
        if (Utils.isOnline(context)) {
            myprogressBar.setVisibility(View.VISIBLE);
            followingUnfollowingAPI = new FollowingUnfollowingAPI(context, responseListener, userBean.userid, userBean.isFollowing == true ? 1 : 0);
            followingUnfollowingAPI.execute();
        } else {
            AlertDailogView.showAlert(context, "Internet Error", "Internet not available", "Cancel", true, "Try again", this, 0).show();
        }
    }

    @Override
    public void OnButtonClick(int tag, int buttonIndex, String input) {
        if (tag == 0) {
            if (buttonIndex == 2) {
                Call_Follow_Unfollow();
            }
        }

        if (tag == 1) {
            if (buttonIndex == 2) {
                Call_Search();
            }
        }
    }
}
