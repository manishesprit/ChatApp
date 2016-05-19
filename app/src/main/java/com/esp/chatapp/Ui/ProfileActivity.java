package com.esp.chatapp.Ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.esp.chatapp.Adapter.MyOnClickListner;
import com.esp.chatapp.Adapter.ProfileRecyclerAdapter;
import com.esp.chatapp.Backend.FeedListAPI;
import com.esp.chatapp.Backend.LikeUnlikeAPI;
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
    private LikeUnlikeAPI likeUnlikeAPI;
    private LinearLayout myprogressBar;

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
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        postlist = new ArrayList<>();

        postlist.add(postBean);
        profileRecyclerAdapter = new ProfileRecyclerAdapter(context, postlist, myOnClickListner);
        recyclerView.setAdapter(profileRecyclerAdapter);

        if (Utils.isOnline(context)) {
            myprogressBar.setVisibility(View.VISIBLE);
            userBean = new UserBean();
            userBean.userid = postBean.userid;
            userBean.myFeed = true;
            userBean.pageno = 0;
            feedListAPI = new FeedListAPI(context, responseListener, userBean);
            feedListAPI.execute();
        }

        myprogressBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

                    ArrayList<PostBean> postBeanArrayList = (ArrayList<PostBean>) obj;
                    if (postBeanArrayList.size() > 0) {
                        postlist.addAll(postBeanArrayList);

                    }
                    profileRecyclerAdapter.notifyDataSetChanged();
                }

                if (tag.equals(Config.TAG_LIKEUNLIKE)) {
                    postBean.islike = (int) obj == 0 ? false : true;
                    postBean.noOflike = (int) obj1;
                    profileRecyclerAdapter.notifyDataSetChanged();
                }
            }
        }

        public void onResponce(String tag, int result, Object obj) {

        }
    };

    MyOnClickListner myOnClickListner = new MyOnClickListner() {
        @Override
        public boolean IsClick(int id, PostBean postBean1) {
            if (id == R.id.imgLikeUnlike) {
                if (Utils.isOnline(context)) {
                    myprogressBar.setVisibility(View.VISIBLE);
                    postBean = postBean1;
                    likeUnlikeAPI = new LikeUnlikeAPI(context, responseListener, postBean.feedid);
                    likeUnlikeAPI.execute();
                }
            }
            return false;
        }

    };
}
