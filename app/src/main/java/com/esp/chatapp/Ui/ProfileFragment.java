package com.esp.chatapp.Ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.esp.chatapp.Adapter.ProfileRecyclerAdapter;
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
public class ProfileFragment extends Fragment implements View.OnClickListener {

    private RecyclerView recyclerView;
    private View mView;
    private PostBean postBean;
    private UserBean userBean;
    private ArrayList<PostBean> postBeanArrayList;
    private FeedListAPI feedListAPI;
    private ProfileRecyclerAdapter profileRecyclerAdapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_profile, container, false);
        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView = (RecyclerView) mView.findViewById(R.id.recyclerView);


        postBean = new PostBean();
        postBean.userid = Pref.getValue(getContext(), Config.PREF_USER_ID, 0);
        postBean.avatar = Pref.getValue(getContext(), Config.PREF_AVATAR, "");
        postBean.noOfpost = Pref.getValue(getContext(), Config.PREF_NOOFPOST, 0);
        postBean.noOffollowers = Pref.getValue(getContext(), Config.PREF_NOOFFOLLOWERS, 0);
        postBean.noOffollowing = Pref.getValue(getContext(), Config.PREF_NOOFFOLLING, 0);
        postBean.status = Pref.getValue(getContext(), Config.PREF_STATUS, "");


        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        postBeanArrayList = new ArrayList<>();

        postBeanArrayList.add(postBean);
        profileRecyclerAdapter = new ProfileRecyclerAdapter(getContext(), postBeanArrayList);
        recyclerView.setAdapter(profileRecyclerAdapter);

        if (Utils.isOnline(getContext())) {
            userBean = new UserBean();
            userBean.userid = postBean.userid;
            userBean.myFeed = true;
            userBean.pageno = 0;
            feedListAPI = new FeedListAPI(getContext(), responseListener, userBean);
//            feedListAPI.execute();
        }

    }

    private ResponseListener responseListener = new ResponseListener() {
        @Override
        public void onResponce(String tag, int result, Object obj) {
            if (result == Config.API_SUCCESS) {
                if (tag.equals(Config.TAG_FEED_LIST)) {

                    ArrayList<PostBean> postBeanArrayList = (ArrayList<PostBean>) obj;
                    if (postBeanArrayList.size() > 0) {
                        postBeanArrayList.addAll(postBeanArrayList);
                        profileRecyclerAdapter.notifyDataSetChanged();
                    } else {

                    }
                }
            }
        }
    };

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.imgProfileAvatar:
                Toast.makeText(getContext(),"Click=====",Toast.LENGTH_LONG).show();
                break;
        }
    }
}
