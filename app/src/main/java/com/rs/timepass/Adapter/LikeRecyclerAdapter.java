package com.rs.timepass.Adapter;

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
import com.rs.timepass.Bean.LikeBean;
import com.rs.timepass.R;
import com.rs.timepass.Utils.Config;
import com.rs.timepass.Utils.Utils;

import java.util.ArrayList;


public class LikeRecyclerAdapter extends RecyclerView.Adapter<LikeRecyclerAdapter.LikeBeanHolder> {

    private ArrayList<LikeBean> mItemList;
    private Context context;
    private LikeBean likeBean;

    public LikeRecyclerAdapter(Context context, ArrayList<LikeBean> itemList) {
        this.mItemList = itemList;
        this.context = context;
    }

    @Override
    public LikeBeanHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_like, parent, false);
        LikeBeanHolder vh = new LikeBeanHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final LikeBeanHolder holder, final int position) {
        likeBean = mItemList.get(position);

        Utils.setDefaultRoundImage(context, holder.imgAvatar, R.drawable.default_user);
        if (!likeBean.avatar.toString().trim().equalsIgnoreCase("")) {
            Glide.with(context).load(Config.IMAGE_PATH_WEB_AVATARS+likeBean.avatar)
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

        holder.txtUserName.setText(likeBean.name);
        holder.txtUserName.setTag(likeBean);
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
    }


    @Override
    public int getItemCount() {
        return mItemList == null ? 0 : mItemList.size();
    }

    class LikeBeanHolder extends RecyclerView.ViewHolder {

        private ImageView imgAvatar;
        private TextView txtUserName;

        public LikeBeanHolder(View itemView) {
            super(itemView);
            imgAvatar = (ImageView) itemView.findViewById(R.id.imgAvatar);
            txtUserName = (TextView) itemView.findViewById(R.id.txtUserName);
        }

    }


}
