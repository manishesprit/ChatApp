package com.esp.chatapp.UI;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

import com.esp.chatapp.R;
import com.esp.chatapp.UC.MultiTouchListener;

/**
 * Created by admin on 2/5/16.
 */
public class MoreActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private FloatingActionButton txtme;
    private RelativeLayout rlmain;
    private int MeasurateWidth;
    private int MeasurateHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("More");


        txtme = (FloatingActionButton) findViewById(R.id.txtme);
        rlmain = (RelativeLayout) findViewById(R.id.rlmain);


        ViewTreeObserver vto = rlmain.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                rlmain.getViewTreeObserver().removeOnPreDrawListener(this);
                MeasurateHeight = rlmain.getMeasuredHeight();
                MeasurateWidth = rlmain.getMeasuredWidth();

                System.out.println("=====MeasurateWidth===" + MeasurateWidth + "======MeasurateHeight===" + MeasurateHeight);
                return true;
            }
        });

        MultiTouchListener touchListener = new MultiTouchListener(MoreActivity.this);
        txtme.setOnTouchListener(touchListener);
    }
}
