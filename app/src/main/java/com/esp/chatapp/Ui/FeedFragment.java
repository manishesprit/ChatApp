package com.esp.chatapp.Ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.esp.chatapp.Adapter.FeedRecyclerAdapter;
import com.esp.chatapp.Adapter.MyOnClickListner;
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
        recyclerView = (RecyclerView) mview.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        postlist = new ArrayList<>();
        feedRecyclerAdapter = new FeedRecyclerAdapter(getContext(), postlist, myOnClickListner);
        recyclerView.setAdapter(feedRecyclerAdapter);


        if (Utils.isOnline(getContext())) {
            myprogressBar.setVisibility(View.VISIBLE);
            userBean = new UserBean();
            userBean.userid = Pref.getValue(getContext(), Config.PREF_USER_ID, 0);
            userBean.pageno = 0;
            userBean.myFeed = false;
            feedListAPI = new FeedListAPI(getContext(), responseListener, userBean);
            feedListAPI.execute();
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
        @Override
        public void onResponce(String tag, int result, Object obj) {

        }

        public void onResponce(String tag, int result, Object obj, Object obj1) {
            myprogressBar.setVisibility(View.GONE);
            if (result == Config.API_SUCCESS) {
                if (tag.equals(Config.TAG_FEED_LIST)) {
                    ArrayList<PostBean> postBeanArrayList = (ArrayList<PostBean>) obj;
                    if (postBeanArrayList.size() > 0) {
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
        public boolean IsClick(int id, PostBean postBean1) {
            if (id == R.id.imgLikeUnlike) {
                if (Utils.isOnline(getContext())) {
                    myprogressBar.setVisibility(View.VISIBLE);
                    postBean = postBean1;
                    likeUnlikeAPI = new LikeUnlikeAPI(getContext(), responseListener, postBean.feedid);
                    likeUnlikeAPI.execute();
                }
            }
            return false;
        }

    };

}
