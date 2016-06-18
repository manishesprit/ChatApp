package com.esp.chatapp.Ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.esp.chatapp.Utils.Pref;
import com.esp.chatapp.Utils.Utils;

import java.util.ArrayList;

/**
 * Created by admin on 2/5/16.
 */
public class ProfileFragment extends Fragment {

    private RecyclerView recyclerView;
    private View mView;
    private PostBean postBean;
    private UserBean userBean;
    private ArrayList<PostBean> postBeanArrayList;
    private FeedListAPI feedListAPI;
    private ProfileRecyclerAdapter profileRecyclerAdapter;
    private LikeUnlikeAPI likeUnlikeAPI;
    private LinearLayout myprogressBar;
    private SwipeRefreshLayout swipeContainer;
    private int limit = 30;
    private int offset = 0;
    private LinearLayoutManager linearLayoutManager;

    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_profile, container, false);

        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (feedListAPI != null) {
            feedListAPI.doCancel();
        }

        myprogressBar = (LinearLayout) mView.findViewById(R.id.myprogressBar);
        swipeContainer = (SwipeRefreshLayout) mView.findViewById(R.id.swipeContainer);
        swipeContainer.setColorScheme(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);

        recyclerView = (RecyclerView) mView.findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        CallFeedList();

        myprogressBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeContainer.setRefreshing(false);
                offset = 0;
                CallFeedList();
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
                            CallFeedList();
                        }
                    }

                }
            }
        });
    }

    private void CallFeedList() {
        if (Utils.isOnline(getContext())) {

            myprogressBar.setVisibility(View.VISIBLE);
            userBean = new UserBean();
            userBean.userid = Pref.getValue(getContext(), Config.PREF_USER_ID, 0);
            userBean.myFeed = true;
            userBean.offset = offset;
            userBean.limit = limit;
            feedListAPI = new FeedListAPI(getContext(), responseListener, userBean);
            feedListAPI.execute();
        }
    }

    private ResponseListener responseListener = new ResponseListener() {
        @Override
        public void onResponce(String tag, int result, Object obj, Object obj1) {
            myprogressBar.setVisibility(View.GONE);
            if (result == Config.API_SUCCESS) {
                if (tag.equals(Config.TAG_FEED_LIST)) {

                    if (offset <= 0) {
                        postBean = new PostBean();
                        postBean.userid = Pref.getValue(getContext(), Config.PREF_USER_ID, 0);
                        postBean.name = Pref.getValue(getContext(), Config.PREF_NAME, "");
                        postBean.avatar = Pref.getValue(getContext(), Config.PREF_AVATAR, "");
                        postBean.email = Pref.getValue(getContext(), Config.PREF_EMAIL, "");
                        postBean.mobile = Pref.getValue(getContext(), Config.PREF_MOBILE, "");
                        postBean.noOfpost = Pref.getValue(getContext(), Config.PREF_NOOFPOST, 0);
                        postBean.noOffollowers = Pref.getValue(getContext(), Config.PREF_NOOFFOLLOWER, 0);
                        postBean.noOffollowing = Pref.getValue(getContext(), Config.PREF_NOOFFOLLING, 0);
                        postBean.status = Pref.getValue(getContext(), Config.PREF_STATUS, "");

                        postBeanArrayList = new ArrayList<>();
                        postBeanArrayList.add(postBean);
                        profileRecyclerAdapter = new ProfileRecyclerAdapter(getContext(), postBeanArrayList, myOnClickListner);
                        recyclerView.setAdapter(profileRecyclerAdapter);
                    }

                    postBeanArrayList.get(0).userid = ((PostBean) obj1).userid;
                    postBeanArrayList.get(0).name = ((PostBean) obj1).name;
                    postBeanArrayList.get(0).avatar = ((PostBean) obj1).avatar;
                    postBeanArrayList.get(0).status = ((PostBean) obj1).status;
                    postBeanArrayList.get(0).noOffollowers = ((PostBean) obj1).noOffollowers;
                    postBeanArrayList.get(0).noOffollowing = ((PostBean) obj1).noOffollowing;
                    postBeanArrayList.get(0).noOfpost = ((PostBean) obj1).noOfpost;
                    postBeanArrayList.get(0).mobile = ((PostBean) obj1).mobile;
                    postBeanArrayList.get(0).email = ((PostBean) obj1).email;

                    ArrayList<PostBean> postBeanArrayList1 = (ArrayList<PostBean>) obj;
                    if (postBeanArrayList1.size() > 0) {
                        offset = offset + limit;
                        loading = true;
                        postBeanArrayList.addAll(postBeanArrayList1);

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
