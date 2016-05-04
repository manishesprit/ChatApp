package com.esp.chatapp.UI;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.esp.chatapp.Adapter.PeopleRecyclerAdapter;
import com.esp.chatapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2/5/16.
 */
public class PeopleFragment extends Fragment {

    private PeopleRecyclerAdapter peopleRecyclerAdapter;
    private RecyclerView recyclerView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_people, container, false);
        setupRecyclerView();
        return recyclerView;
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        peopleRecyclerAdapter = new PeopleRecyclerAdapter(createItemList());
        recyclerView.setAdapter(peopleRecyclerAdapter);
    }

    private List<String> createItemList() {
        List<String> itemList = new ArrayList<>();
        for (int i = 0; i < 17; i++) {
            itemList.add("User " + i);
        }
        return itemList;
    }
}
