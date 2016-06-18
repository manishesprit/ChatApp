package com.rs.timepass.Ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.rs.timepass.Adapter.FeedRecyclerAdapter;
import com.rs.timepass.Adapter.MyOnClickListner;
import com.rs.timepass.Backend.FeedListAPI;
import com.rs.timepass.Backend.LikeUnlikeAPI;
import com.rs.timepass.Backend.ResponseListener;
import com.rs.timepass.Bean.PostBean;
import com.rs.timepass.Bean.UserBean;
import com.rs.timepass.R;
import com.rs.timepass.Utils.Config;
import com.rs.timepass.Utils.Pref;
import com.rs.timepass.Utils.Utils;

import java.util.ArrayList;

/**
 * Created by admin on 2/5/16.
 */
public class FeedFragment extends Fragment implements View.OnClickListener {


    private RecyclerView recyclerView;
    private FeedRecyclerAdapter feedRecyclerAdapter;
    private ArrayList<PostBean> postlist;
    private View mview;
    private FeedListAPI feedListAPI;
    private LikeUnlikeAPI likeUnlikeAPI;
    private UserBean userBean;
    private PostBean postBean;
    private LinearLayout myprogressBar;
    private SwipeRefreshLayout swipeContainer;
    private int limit = 30;
    private int offset = 0;
    private LinearLayoutManager linearLayoutManager;

    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mview = inflater.inflate(R.layout.fragment_feed, container, false);
        return mview;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (feedListAPI != null) {
            feedListAPI.doCancel();
        }
        myprogressBar = (LinearLayout) mview.findViewById(R.id.myprogressBar);
        swipeContainer = (SwipeRefreshLayout) mview.findViewById(R.id.swipeContainer);
        swipeContainer.setColorScheme(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
        recyclerView = (RecyclerView) mview.findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        CallFeedList();

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeContainer.setRefreshing(false);
                offset = 0;
                CallFeedList();
            }
        });

        myprogressBar.setOnClickListener(this);

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
                            CallFeedList();
                        }
                    }

                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.myprogressBar:
                break;
        }
    }

    private void CallFeedList() {
        if (Utils.isOnline(getContext())) {
            myprogressBar.setVisibility(View.VISIBLE);
            userBean = new UserBean();
            userBean.userid = Pref.getValue(getContext(), Config.PREF_USER_ID, 0);
            userBean.offset = offset;
            userBean.limit = limit;
            userBean.myFeed = false;
            feedListAPI = new FeedListAPI(getContext(), responseListener, userBean);
            feedListAPI.execute();
        }
    }

    private ResponseListener responseListener = new ResponseListener() {
        @Override
        public void onResponce(String tag, int result, Object obj) {

        }

        public void onResponce(String tag, int result, Object obj, Object obj1) {
            myprogressBar.setVisibility(View.GONE);
            if (result == Config.API_SUCCESS) {
                if (tag.equals(Config.TAG_FEED_LIST)) {

                    if (offset <= 0) {
                        postlist = new ArrayList<>();
                        feedRecyclerAdapter = new FeedRecyclerAdapter(getContext(), postlist, myOnClickListner);
                        recyclerView.setAdapter(feedRecyclerAdapter);
                    }

                    ArrayList<PostBean> postBeanArrayList = (ArrayList<PostBean>) obj;
                    if (postBeanArrayList.size() > 0) {
                        offset = offset + limit;
                        loading = true;
                        postlist.addAll(postBeanArrayList);
                    }
                    feedRecyclerAdapter.notifyDataSetChanged();
                }

                if (tag.equals(Config.TAG_LIKEUNLIKE)) {
                    postBean.islike = (int) obj == 0 ? false : true;
                    postBean.noOflike = (int) obj1;
                    feedRecyclerAdapter.notifyDataSetChanged();
                }
            }
        }
    };

    MyOnClickListner myOnClickListner = new MyOnClickListner() {
        @Override
        public boolean IsClick(int id, Object object) {
            if (id == R.id.imgLikeUnlike) {
                if (Utils.isOnline(getContext())) {
                    myprogressBar.setVisibility(View.VISIBLE);
                    postBean = (PostBean) object;
                    likeUnlikeAPI = new LikeUnlikeAPI(getContext(), responseListener, postBean.feedid);
                    likeUnlikeAPI.execute();
                }
            }
            return false;
        }

    };

}
