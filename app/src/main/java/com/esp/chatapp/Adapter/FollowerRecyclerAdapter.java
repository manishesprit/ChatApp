package com.esp.chatapp.Adapter;

import android.content.Context;
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
import com.esp.chatapp.Bean.UserBean;
import com.esp.chatapp.R;
import com.esp.chatapp.Uc.OnPopUpDialogButoonClickListener;
import com.esp.chatapp.Utils.Config;
import com.esp.chatapp.Utils.Utils;

import java.util.ArrayList;


public class FollowerRecyclerAdapter extends RecyclerView.Adapter<FollowerRecyclerAdapter.LikeBeanHolder> implements OnPopUpDialogButoonClickListener {

    private ArrayList<UserBean> mItemList;
    private Context context;
    private UserBean userBean;
    private MyOnClickListner myOnClickListner;

    public FollowerRecyclerAdapter(Context context, ArrayList<UserBean> itemList, MyOnClickListner myOnClickListner) {
        this.mItemList = itemList;
        this.context = context;
        this.myOnClickListner = myOnClickListner;
    }

    @Override
    public LikeBeanHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_follower, parent, false);
        LikeBeanHolder vh = new LikeBeanHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final LikeBeanHolder holder, final int position) {
        userBean = mItemList.get(position);

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

        holder.txtUserName.setText(userBean.name);
        holder.txtUserName.setTag(userBean);
        holder.txtUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (((PostBean) holder.txtUserName.getTag()).userid != Pref.getValue(context, Config.PREF_USER_ID, 0)) {
//                    Intent intent = new Intent(context, ProfileActivity.class);
//                    intent.putExtra("beanData", (PostBean) holder.txtUserName.getTag());
//                    context.startActivity(intent);
//                }
            }
        });

        holder.imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (((PostBean) holder.txtUserName.getTag()).userid != Pref.getValue(context, Config.PREF_USER_ID, 0)) {
//                    Intent intent = new Intent(context, ProfileActivity.class);
//                    intent.putExtra("beanData", (PostBean) holder.txtUserName.getTag());
//                    context.startActivity(intent);
//                }
            }
        });

        holder.txtfollowUnfollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myOnClickListner.IsClick(R.id.txtfollowUnfollow, holder.txtUserName.getTag());
            }
        });
    }


    @Override
    public int getItemCount() {
        return mItemList == null ? 0 : mItemList.size();
    }

    @Override
    public void OnButtonClick(int tag, int buttonIndex, String input) {

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
