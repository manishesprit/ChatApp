package com.esp.chatapp.Ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.esp.chatapp.Adapter.FeedRecyclerAdapter;
import com.esp.chatapp.R;
import com.esp.chatapp.Utils.Utils;

import java.util.ArrayList;

/**
 * Created by admin on 2/5/16.
 */
public class FeedFragment extends Fragment implements View.OnClickListener {


    private RecyclerView recyclerView;
    private FeedRecyclerAdapter feedRecyclerAdapter;
    private ArrayList<String> postlist;
    private View mview;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mview = inflater.inflate(R.layout.fragment_feed, container, false);
        return mview;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recyclerView=(RecyclerView)mview.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        feedRecyclerAdapter = new FeedRecyclerAdapter(getContext(), Utils.getPOstlist());
        recyclerView.setAdapter(feedRecyclerAdapter);
    }

    @Override
    public void onClick(View v) {

    }
}
