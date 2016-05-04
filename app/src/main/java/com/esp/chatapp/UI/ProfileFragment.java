package com.esp.chatapp.UI;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.esp.chatapp.Adapter.ProfileRecyclerAdapter;
import com.esp.chatapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2/5/16.
 */
public class ProfileFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_profile, container, false);
        setupRecyclerView(recyclerView);
        return recyclerView;
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ProfileRecyclerAdapter profileRecyclerAdapter = new ProfileRecyclerAdapter(getContext(), createItemList());
        recyclerView.setAdapter(profileRecyclerAdapter);

    }

    private List<String> createItemList() {
        List<String> itemList = new ArrayList<>();
        itemList.add("Esprit IT Solution");
        for (int i = 0; i < 15; i++) {
            itemList.add("User " + i);
        }
        return itemList;
    }
}
