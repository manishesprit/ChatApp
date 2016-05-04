package com.esp.chatapp.Adapter;

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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.esp.chatapp.Bean.PostBean;
import com.esp.chatapp.R;
import com.esp.chatapp.UI.MoreActivity;
import com.esp.chatapp.Utils.Utils;

import java.util.ArrayList;


public class FeedRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<PostBean> mItemList;
    private RecyclerItemViewHolder holder;
    private Context context;

    public FeedRecyclerAdapter(Context context, ArrayList<PostBean> itemList) {
        this.mItemList = itemList;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_feed, parent, false);
        RecyclerItemViewHolder vh = new RecyclerItemViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        holder = (RecyclerItemViewHolder) viewHolder;

        Utils.setDefaultRoundImage(context, holder.imgAvatar);

//        if (Utils.isOnline(context)) {
        Glide.with(context).load(mItemList.get(position).avatar)
                .asBitmap()
                .error(R.drawable.default_user).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                holder.imgAvatar.setImageDrawable(circularBitmapDrawable);
            }
        });
//        }

        holder.txtUserName.setText(mItemList.get(position).username);
        holder.txtUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MoreActivity.class);
                context.startActivity(intent);
            }
        });
        holder.txtFeedTime.setText(mItemList.get(position).posttime);
        if (mItemList.get(position).caption.trim().toString().equalsIgnoreCase("")) {
            holder.txtCaption.setVisibility(View.GONE);
        } else {
            holder.txtCaption.setVisibility(View.VISIBLE);
            holder.txtCaption.setText(mItemList.get(position).caption);
        }

//        if (Utils.isOnline(context)) {
        Glide.with(context).load(mItemList.get(position).post_url).asBitmap().error(R.drawable.ravi).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {

                int intendedWidth = holder.imgFeed.getWidth();

                int originalWidth = resource.getWidth();
                int originalHeight = resource.getHeight();

                float scale = (float) intendedWidth / originalWidth;
                int newHeight = (int) Math.round(originalHeight * scale);


                holder.imgFeed.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                holder.imgFeed.getLayoutParams().width = intendedWidth;
                holder.imgFeed.getLayoutParams().height = newHeight;

                holder.imgFeed.setImageBitmap(resource);
            }
        });
//        }


        holder.imgLikeUnlike.setImageResource(mItemList.get(position).islike == true ? R.drawable.love_white_filled : R.drawable.love_gray);
        holder.imgLikeUnlike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemList.get(position).islike == true) {
                    mItemList.get(position).islike = false;
                    holder.imgLikeUnlike.setImageResource(R.drawable.love_gray);
                } else {
                    mItemList.get(position).islike = true;
                    holder.imgLikeUnlike.setImageResource(R.drawable.love_white_filled);
                }
            }
        });

        holder.txtNolike.setText("" + mItemList.get(position).noOflike);
        holder.txtNoComment.setText("" + mItemList.get(position).noOfcomment);
    }

    @Override
    public int getItemCount() {
        return mItemList == null ? 0 : mItemList.size();
    }


    class RecyclerItemViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imgAvatar;
        private final TextView txtUserName;
        private final TextView txtFeedTime;
        private final TextView txtCaption;
        private final ImageView imgFeed;
        private final ImageView imgLikeUnlike;
        private final TextView txtNoComment;
        private final TextView txtNolike;

        public RecyclerItemViewHolder(View itemView) {
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
