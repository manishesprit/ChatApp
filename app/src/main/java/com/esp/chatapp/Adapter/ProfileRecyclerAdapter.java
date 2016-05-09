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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.esp.chatapp.Bean.PostBean;
import com.esp.chatapp.R;
import com.esp.chatapp.Ui.ProfileDetailActivity;
import com.esp.chatapp.Utils.Utils;

import java.util.ArrayList;


public class ProfileRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<PostBean> mItemList;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private Context context;
    private PostBean postBean;

    public ProfileRecyclerAdapter(Context context, ArrayList<PostBean> itemList) {
        mItemList = new ArrayList<>();
        postBean = new PostBean();
        mItemList.add(postBean);
        mItemList.addAll(itemList);
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_profile_header, parent, false);
            return new HeaderViewHolder(v);
        } else if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_feed, parent, false);
            return new PostBeanHolder(v);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder mholder, int position) {
        if (mholder instanceof HeaderViewHolder) {
            final HeaderViewHolder headerHolder = (HeaderViewHolder) mholder;

//            Utils.setDefaultRoundImage(context, headerHolder.imgProfileAvatar, R.drawable.default_user);
            Glide.with(context).load(Utils.avarat_sundar)
                    .asBitmap()
                    .error(R.drawable.default_user).placeholder(R.drawable.default_user).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                    RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    headerHolder.imgProfileAvatar.setImageDrawable(circularBitmapDrawable);
                }
            });
            headerHolder.imgProfileAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ProfileDetailActivity.class);
                    context.startActivity(intent);
                }
            });


            headerHolder.txtUsername.setText("Sachin Tendulkar");

        } else if (mholder instanceof PostBeanHolder) {
            final PostBeanHolder holder = (PostBeanHolder) mholder;

            postBean = mItemList.get(position);

//            Utils.setDefaultRoundImage(context, holder.imgAvatar, R.drawable.default_user);
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

            holder.txtUserName.setText(postBean.username);
            holder.txtFeedTime.setText(postBean.posttime);
            if (postBean.caption.trim().toString().equalsIgnoreCase("")) {
                holder.txtCaption.setVisibility(View.GONE);
            } else {
                holder.txtCaption.setVisibility(View.VISIBLE);
                holder.txtCaption.setText(postBean.caption);
            }

            Glide.with(context).load(postBean.image_url).asBitmap().error(R.drawable.ravi).placeholder(R.drawable.ravi).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                    holder.imgFeed.setImageBitmap(resource);
                }
            });


            holder.imgLikeUnlike.setImageResource(postBean.islike == true ? R.drawable.love_white_filled : R.drawable.love_gray);
            holder.imgLikeUnlike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (postBean.islike == true) {
                        postBean.islike = false;
                        holder.imgLikeUnlike.setImageResource(R.drawable.love_gray);
                    } else {
                        postBean.islike = true;
                        holder.imgLikeUnlike.setImageResource(R.drawable.love_white_filled);
                    }
                }
            });

            holder.txtNolike.setText("" + postBean.noOflike);
            holder.txtNoComment.setText("" + postBean.noOfcomment);

        }
    }

    //    need to override this method
    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position)) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgProfileAvatar;
        private TextView txtUsername;

        public HeaderViewHolder(View itemView) {
            super(itemView);

            imgProfileAvatar = (ImageView) itemView.findViewById(R.id.imgProfileAvatar);
            txtUsername = (TextView) itemView.findViewById(R.id.txtUsername);
        }
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
