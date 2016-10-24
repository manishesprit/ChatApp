package com.rs.timepass.Ui;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.rs.timepass.Bean.NotificationBean;
import com.rs.timepass.R;
import com.rs.timepass.Utils.Config;
import com.rs.timepass.Utils.Pref;

import java.util.ArrayList;


/**
 * Created by admin on 2/5/16.
 */
public class NotificationActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private Intent intent;
    private ArrayList<NotificationBean> notificationArrayList;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        context = this;
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.cancel();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Notification");

        notificationArrayList = Pref.getNotificationArray(context, Config.PREF_NOTIFICATION, new ArrayList<NotificationBean>());
        if (notificationArrayList != null && notificationArrayList.size() > 0) {
            for (int i = 0; i < notificationArrayList.size(); i++) {
                notificationManager.cancel(notificationArrayList.get(i).notiid);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

    }
}
