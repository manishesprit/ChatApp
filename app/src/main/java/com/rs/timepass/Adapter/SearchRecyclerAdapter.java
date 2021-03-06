package com.rs.timepass.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.rs.timepass.Bean.PostBean;
import com.rs.timepass.Bean.UserBean;
import com.rs.timepass.R;
import com.rs.timepass.Ui.ProfileActivity;
import com.rs.timepass.Utils.Config;
import com.rs.timepass.Utils.Pref;
import com.rs.timepass.Utils.Utils;

import java.util.ArrayList;


public class SearchRecyclerAdapter extends RecyclerView.Adapter<SearchRecyclerAdapter.LikeBeanHolder> {

    private ArrayList<UserBean> mItemList;
    private Context context;
    private UserBean userBean;

    public SearchRecyclerAdapter(Context context, ArrayList<UserBean> itemList) {
        this.mItemList = itemList;
        this.context = context;
    }

    @Override
    public LikeBeanHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_people, parent, false);
        LikeBeanHolder vh = new LikeBeanHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final LikeBeanHolder holder, final int position) {
        userBean = mItemList.get(position);

        holder.txtUserName.setTag(userBean);
        holder.txtUserName.setText(userBean.name);
        holder.txtfollowUnfollow.setText(userBean.isFollowing == true ? "Unfollow" : "Follow");
        Utils.setDefaultRoundImage(context, holder.imgAvatar, R.drawable.default_user);
        if (!userBean.avatar.toString().trim().equalsIgnoreCase("")) {

            Glide.with(context).load(Config.IMAGE_PATH_WEB_AVATARS + userBean.avatar)
                    .asBitmap()
                    .error(R.drawable.default_user).placeholder(R.drawable.default_user).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                    RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    holder.imgAvatar.setImageDrawable(circularBitmapDrawable);
                }
            });
        }


        holder.txtUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((UserBean) holder.txtUserName.getTag()).userid != Pref.getValue(context, Config.PREF_USER_ID, 0)) {
                    Intent intent = new Intent(context, ProfileActivity.class);
                    PostBean postBean = new PostBean();
                    postBean.userid = ((UserBean) holder.txtUserName.getTag()).userid;
                    postBean.name=((UserBean) holder.txtUserName.getTag()).name;
                    postBean.avatar=((UserBean) holder.txtUserName.getTag()).avatar;
                    intent.putExtra("beanData", postBean);
                    context.startActivity(intent);
                }
            }
        });

        holder.imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((UserBean) holder.txtUserName.getTag()).userid != Pref.getValue(context, Config.PREF_USER_ID, 0)) {
                    Intent intent = new Intent(context, ProfileActivity.class);
                    PostBean postBean = new PostBean();
                    postBean.userid = ((UserBean) holder.txtUserName.getTag()).userid;
                    postBean.name=((UserBean) holder.txtUserName.getTag()).name;
                    postBean.avatar=((UserBean) holder.txtUserName.getTag()).avatar;
                    intent.putExtra("beanData", postBean);
                    context.startActivity(intent);
                }
            }
        });
        holder.txtfollowUnfollow.setTag(userBean);
        holder.txtfollowUnfollow.setOnClickListener((View.OnClickListener) context);

    }


    @Override
    public int getItemCount() {
        return mItemList == null ? 0 : mItemList.size();
    }

    class LikeBeanHolder extends RecyclerView.ViewHolder {

        private ImageView imgAvatar;
        private TextView txtUserName;
        private TextView txtfollowUnfollow;

        public LikeBeanHolder(View itemView) {
            super(itemView);
            imgAvatar = (ImageView) itemView.findViewById(R.id.imgAvatar);
            txtUserName = (TextView) itemView.findViewById(R.id.txtUserName);
            txtfollowUnfollow = (TextView) itemView.findViewById(R.id.txtfollowUnfollow);
        }

    }


}
