package com.esp.chatapp.Ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.esp.chatapp.Adapter.ProfileRecyclerAdapter;
import com.esp.chatapp.Backend.FeedListAPI;
import com.esp.chatapp.Backend.ResponseListener;
import com.esp.chatapp.Bean.PostBean;
import com.esp.chatapp.Bean.UserBean;
import com.esp.chatapp.R;
import com.esp.chatapp.Utils.Config;
import com.esp.chatapp.Utils.Utils;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        context = this;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Profile");

        postBean = (PostBean) getIntent().getSerializableExtra("postData");

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        postlist = new ArrayList<>();

        postlist.add(postBean);
        profileRecyclerAdapter = new ProfileRecyclerAdapter(context, postlist);
        recyclerView.setAdapter(profileRecyclerAdapter);

        if (Utils.isOnline(context)) {
            userBean = new UserBean();
            userBean.userid = postBean.userid;
            userBean.myFeed = true;
            userBean.pageno = 0;
            feedListAPI = new FeedListAPI(context, responseListener, userBean);
//            feedListAPI.execute();
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
        @Override
        public void onResponce(String tag, int result, Object obj) {
            if (result == Config.API_SUCCESS) {
                if (tag.equals(Config.TAG_FEED_LIST)) {

                    ArrayList<PostBean> postBeanArrayList = (ArrayList<PostBean>) obj;
                    if (postBeanArrayList.size() > 0) {
                        postlist.addAll(postBeanArrayList);
                        profileRecyclerAdapter.notifyDataSetChanged();
                    } else {

                    }
                }
            }
        }
    };
}
