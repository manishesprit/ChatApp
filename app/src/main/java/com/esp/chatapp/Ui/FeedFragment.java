package com.esp.chatapp.Ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.esp.chatapp.Adapter.FeedRecyclerAdapter;
import com.esp.chatapp.Backend.FeedListAPI;
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
    private UserBean userBean;



    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mview = inflater.inflate(R.layout.fragment_feed, container, false);
        return mview;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recyclerView = (RecyclerView) mview.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        postlist = new ArrayList<>();
        feedRecyclerAdapter = new FeedRecyclerAdapter(getContext(), postlist);
        recyclerView.setAdapter(feedRecyclerAdapter);
        if (Utils.isOnline(getContext())) {
            userBean=new UserBean();
            userBean.userid = Pref.getValue(getContext(), Config.PREF_USER_ID, 0);
            userBean.pageno = 0;
            userBean.myFeed = false;
            feedListAPI = new FeedListAPI(getContext(), responseListener, userBean);
//            feedListAPI.execute();
        }

    }

    @Override
    public void onClick(View v) {

    }

    private ResponseListener responseListener = new ResponseListener() {
        @Override
        public void onResponce(String tag, int result, Object obj) {
            if (result == Config.API_SUCCESS) {
                if (tag.equals(Config.TAG_FEED_LIST)) {

                    ArrayList<PostBean> postBeanArrayList = (ArrayList<PostBean>) obj;
                    if (postBeanArrayList.size() > 0) {
                        postlist.addAll(postBeanArrayList);
                        feedRecyclerAdapter.notifyDataSetChanged();
                    } else {

                    }
                }
            }
        }
    };
}
