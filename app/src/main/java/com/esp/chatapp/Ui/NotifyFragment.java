package com.esp.chatapp.Ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.esp.chatapp.R;

/**
 * Created by admin on 2/5/16.
 */
public class NotifyFragment extends Fragment {

    private RecyclerView recyclerView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_notify, container, false);
//        setupRecyclerView();
        return recyclerView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
