package com.esp.chatapp.Ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.esp.chatapp.R;
import com.esp.chatapp.Utils.Config;
import com.esp.chatapp.Utils.Pref;

/**
 * Created by admin on 2/5/16.
 */
public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private FloatingActionButton fabButton;
    private Menu menu;
    private Context context;
    public static Activity activityHome;

    private RelativeLayout llfeed;
    private RelativeLayout llprofile;
    private RelativeLayout llnotify;

    private TextView txtFeed;
    private TextView txtProfile;
    private TextView txtNotify;

    private View feedview;
    private View profileview;
    private View notifyview;

    private FrameLayout feedFramlayout;
    private FrameLayout profileFramlayout;
    private FrameLayout notifyFramlayout;

    private FragmentManager fragmentManager;

    private boolean isFeed, isProfile, isNotfy;

    private FeedFragment feedFragment;
    private ProfileFragment profileFragment;
    private NotifyFragment notifyFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        context = this;
        activityHome = this;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.app_name));

        if (Pref.getValue(context, Config.PREF_ISFIRSTTIME, 0) == 0) {
            Intent intent = new Intent(context, SearchActivity.class);
            startActivity(intent);
        }

        fabButton = (FloatingActionButton) findViewById(R.id.fabButton);
        fabButton.setOnClickListener(this);

        llfeed = (RelativeLayout) findViewById(R.id.llfeed);
        llprofile = (RelativeLayout) findViewById(R.id.llprofile);
        llnotify = (RelativeLayout) findViewById(R.id.llnotify);


        txtFeed = (TextView) findViewById(R.id.txtFeed);
        txtProfile = (TextView) findViewById(R.id.txtProfile);
        txtNotify = (TextView) findViewById(R.id.txtNotify);

        feedFramlayout = (FrameLayout) findViewById(R.id.feedFramlayout);
        profileFramlayout = (FrameLayout) findViewById(R.id.profileFramlayout);
        notifyFramlayout = (FrameLayout) findViewById(R.id.notifyFramlayout);

        feedview = (View) findViewById(R.id.feedview);
        profileview = (View) findViewById(R.id.profileview);
        notifyview = (View) findViewById(R.id.notifyview);

        fragmentManager = getSupportFragmentManager();

        setFragment(0);

        llfeed.setOnClickListener(this);
        llprofile.setOnClickListener(this);
        llnotify.setOnClickListener(this);

    }

    private void setFragment(int pos) {
        if (pos == 0) {
            if (!isFeed) {
                feedFragment = new FeedFragment();
                fragmentManager.beginTransaction().add(R.id.feedFramlayout, feedFragment).commit();
                isFeed = true;
            } else {

            }

            feedFramlayout.setVisibility(View.VISIBLE);
            profileFramlayout.setVisibility(View.GONE);
            notifyFramlayout.setVisibility(View.GONE);

            feedview.setVisibility(View.VISIBLE);
            profileview.setVisibility(View.INVISIBLE);
            notifyview.setVisibility(View.INVISIBLE);

            txtFeed.setTextColor(getResources().getColor(R.color.color_white));
            txtProfile.setTextColor(getResources().getColor(R.color.color_whitedark));
            txtNotify.setTextColor(getResources().getColor(R.color.color_whitedark));

        } else if (pos == 1) {

            if (!isProfile) {
                profileFragment = new ProfileFragment();
                fragmentManager.beginTransaction().add(R.id.profileFramlayout, profileFragment).commit();
                isProfile = true;
            } else {

            }

            feedFramlayout.setVisibility(View.GONE);
            profileFramlayout.setVisibility(View.VISIBLE);
            notifyFramlayout.setVisibility(View.GONE);

            feedview.setVisibility(View.INVISIBLE);
            profileview.setVisibility(View.VISIBLE);
            notifyview.setVisibility(View.INVISIBLE);

            txtFeed.setTextColor(getResources().getColor(R.color.color_whitedark));
            txtProfile.setTextColor(getResources().getColor(R.color.color_white));
            txtNotify.setTextColor(getResources().getColor(R.color.color_whitedark));


        } else if (pos == 2) {

            if (!isNotfy) {
                notifyFragment = new NotifyFragment();
                fragmentManager.beginTransaction().add(R.id.notifyFramlayout, notifyFragment).commit();
                isNotfy = true;
            } else {

            }

            feedFramlayout.setVisibility(View.GONE);
            profileFramlayout.setVisibility(View.GONE);
            notifyFramlayout.setVisibility(View.VISIBLE);

            feedview.setVisibility(View.INVISIBLE);
            profileview.setVisibility(View.INVISIBLE);
            notifyview.setVisibility(View.VISIBLE);

            txtFeed.setTextColor(getResources().getColor(R.color.color_whitedark));
            txtProfile.setTextColor(getResources().getColor(R.color.color_whitedark));
            txtNotify.setTextColor(getResources().getColor(R.color.color_white));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabButton:
                Intent intent = new Intent(context, CreatePostActivity.class);
                startActivity(intent);
                break;

            case R.id.llfeed:
                setFragment(0);
                break;

            case R.id.llprofile:
                setFragment(1);
                break;

            case R.id.llnotify:
                setFragment(2);
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu, menu);
        menu.findItem(R.id.action_search).setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_setting:
                intent = new Intent(context, SettingActivity.class);
                startActivity(intent);
                break;

            case R.id.action_search:
                intent = new Intent(context, SearchActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }
}
