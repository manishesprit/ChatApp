package com.esp.chatapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
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
import com.esp.chatapp.Bean.PostBean;
import com.esp.chatapp.R;
import com.esp.chatapp.Ui.ProfileActivity;
import com.esp.chatapp.Utils.Utils;

import java.util.ArrayList;


public class FeedRecyclerAdapter extends RecyclerView.Adapter<FeedRecyclerAdapter.PostBeanHolder> {

    private ArrayList<PostBean> mItemList;
    private Context context;
    private PostBean postBean;
    private MediaPlayer mpLike;

    public FeedRecyclerAdapter(Context context, ArrayList<PostBean> itemList) {
        this.mItemList = itemList;
        this.context = context;
    }

    @Override
    public PostBeanHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_feed, parent, false);
        PostBeanHolder vh = new PostBeanHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final PostBeanHolder holder, int position) {
        postBean = mItemList.get(position);

        Utils.setDefaultRoundImage(context, holder.imgAvatar, R.drawable.default_user);
        if (!postBean.avatar.toString().trim().equalsIgnoreCase("")) {
            Glide.with(context).load(postBean.avatar)
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

        holder.txtUserName.setText(postBean.name);
        holder.txtUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProfileActivity.class);
                context.startActivity(intent);
            }
        });
        holder.imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProfileActivity.class);
                context.startActivity(intent);
            }
        });
        holder.txtFeedTime.setText(postBean.posttime);

        if (postBean.caption.trim().toString().equalsIgnoreCase("")) {
            holder.txtCaption.setVisibility(View.GONE);
        } else {
            holder.txtCaption.setVisibility(View.VISIBLE);
            holder.txtCaption.setText(postBean.caption);
        }

        if (!postBean.image_url.toString().equals("")) {
            holder.imgFeed.setVisibility(View.VISIBLE);
            Glide.with(context).load(postBean.image_url).asBitmap().error(R.drawable.default_user).placeholder(R.drawable.default_user).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                    holder.imgFeed.setImageBitmap(resource);
                }
            });
        } else {
            holder.imgFeed.setVisibility(View.GONE);
        }

        holder.imgLikeUnlike.setSoundEffectsEnabled(false);
        holder.imgLikeUnlike.setImageResource(postBean.islike == true ? R.drawable.love_white_filled : R.drawable.love_gray);
        holder.imgLikeUnlike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (postBean.islike == true) {
                    postBean.islike = false;
                    holder.imgLikeUnlike.setImageResource(R.drawable.love_gray);
                } else {
                    if (mpLike != null) {
                        if (mpLike.isPlaying()) {
                            mpLike.release();
                            mpLike = null;
                        }
                    }
                    mpLike = MediaPlayer.create(context, R.raw.beep);
                    mpLike.start();
                    postBean.islike = true;
                    holder.imgLikeUnlike.setImageResource(R.drawable.love_white_filled);
                }
            }
        });

        holder.txtNolike.setText("" + postBean.noOflike);
        holder.txtNoComment.setText("" + postBean.noOfcomment);
    }


    @Override
    public int getItemCount() {
        return mItemList == null ? 0 : mItemList.size();
    }

    class PostBeanHolder extends RecyclerView.ViewHolder {

        private ImageView imgAvatar;
        private TextView txtUserName;
        private TextView txtFeedTime;
        private TextView txtCaption;
        private ImageView imgFeed;
        private ImageView imgLikeUnlike;
        private TextView txtNoComment;
        private TextView txtNolike;

        public PostBeanHolder(View itemView) {
            super(itemView);
            imgAvatar = (ImageView) itemView.findViewById(R.id.imgAvatar);
            txtUserName = (TextView) itemView.findViewById(R.id.txtUserName);
            txtFeedTime = (TextView) itemView.findViewById(R.id.txtFeedTime);
            txtCaption = (TextView) itemView.findViewById(R.id.txtCaption);
            imgFeed = (ImageView) itemView.findViewById(R.id.imgFeed);
            imgLikeUnlike = (ImageView) itemView.findViewById(R.id.imgLikeUnlike);
            txtNoComment = (TextView) itemView.findViewById(R.id.txtNoComment);
            txtNolike = (TextView) itemView.findViewById(R.id.txtNolike);
        }

    }


}
