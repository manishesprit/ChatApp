package com.esp.chatapp.Utils;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageView;

import com.esp.chatapp.Bean.PostBean;

import java.util.ArrayList;

/**
 * Created by admin on 4/5/16.
 */
public class Utils {

    public static String avarar_sachin = "http://www.iloveindia.com/sports/pics/sachin-tendulkar.jpg";
    public static String avarat_sundar = "https://pbs.twimg.com/profile_images/481231649128980480/9hpv14pc.jpeg";
    public static boolean isOnline(Context context) {
        try {
            if (context == null)
                return false;

            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            if (cm != null) {
                if (cm.getActiveNetworkInfo() != null) {
                    return cm.getActiveNetworkInfo().isConnected();
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            System.out.println("Exception " + e);
            return false;
        }
    }


    public static ArrayList<PostBean> getPOstlist() {



        String post_url1 = "http://static2.businessinsider.com/image/51f9558c6bb3f71b2d000006/insider-bill-gates-will-not-be-the-next-microsoft-ceo.jpg";
        String post_url2 = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRkWg913jAlDmdUb1IYn0vMCWqwQdK8y583pHjJMAsW8fnaGxjjrQ";
        String post_url3 = "http://www.trbimg.com/img-540a42b5/turbine/la-fi-hy-autos-2016-mercedes-amg-gt-photos-201-001/650/650x366";

        ArrayList<PostBean> itemList = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            PostBean postBean = new PostBean();
            postBean.postid = i;
            postBean.userid = i * 3;
            postBean.username = "User " + i;
            postBean.avatar = (i % 2) == 0 ? avarat_sundar : avarar_sachin;
            postBean.post_url = (i % 2) == 0 ? post_url2:post_url3;
            postBean.noOfcomment = i * 4;
            postBean.noOflike = i * 7;
            postBean.islike = ((i * 5) % 8) == 0 ? true : false;
            postBean.caption = (i % 3) == 0 ? "" : "Nice one this app. i like this app...";
            postBean.posttime = "2 min";
            itemList.add(postBean);
        }
        return itemList;
    }

    public static void setDefaultRoundImage(Context context, ImageView imageView, int imageResource) {
        RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), BitmapFactory.decodeResource(context.getResources(), imageResource));
        circularBitmapDrawable.setCircular(true);
        imageView.setImageDrawable(null);
        imageView.setImageDrawable(circularBitmapDrawable);
    }
}
