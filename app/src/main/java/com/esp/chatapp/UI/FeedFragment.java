package com.esp.chatapp.UI;

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
public class FeedFragment extends Fragment {


    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;
    private FeedRecyclerAdapter feedRecyclerAdapter;
    private ArrayList<String> postlist;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_feed, container, false);
        setupRecyclerView();
        return recyclerView;
    }

    private void setupRecyclerView() {
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        feedRecyclerAdapter = new FeedRecyclerAdapter(getContext(), Utils.getPOstlist());
        recyclerView.setAdapter(feedRecyclerAdapter);

    }
}
