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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.rs.timepass.Bean.PostBean;
import com.rs.timepass.R;
import com.rs.timepass.Ui.ChangeAvatarActivity;
import com.rs.timepass.Ui.EditProfileActivity;
import com.rs.timepass.Ui.FeedDetailActivity;
import com.rs.timepass.Ui.FollowerListActivity;
import com.rs.timepass.Ui.FollowingListActivity;
import com.rs.timepass.Ui.LikeListActivity;
import com.rs.timepass.Ui.ProfileDetailActivity;
import com.rs.timepass.Utils.Config;
import com.rs.timepass.Utils.Pref;
import com.rs.timepass.Utils.Utils;

import java.util.ArrayList;


public class ProfileRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<PostBean> mItemList;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private Context context;
    private PostBean postBean;
    private MyOnClickListner myOnClickListner;

    public ProfileRecyclerAdapter(Context context, ArrayList<PostBean> itemList, MyOnClickListner myOnClickListner) {
        mItemList = itemList;
        this.context = context;
        this.myOnClickListner = myOnClickListner;
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
    public void onBindViewHolder(final RecyclerView.ViewHolder mholder, final int position) {

        if (mholder instanceof HeaderViewHolder) {
            final HeaderViewHolder headerHolder = (HeaderViewHolder) mholder;
            postBean = mItemList.get(position);

            headerHolder.txtName.setText(postBean.name);
            headerHolder.txtName.setTag(postBean);

            Utils.setDefaultRoundImage(context, headerHolder.imgProfileAvatar, R.drawable.default_user);
            if (!((PostBean) headerHolder.txtName.getTag()).avatar.toString().equalsIgnoreCase("")) {
                Glide.with(context).load(Config.IMAGE_PATH_WEB_AVATARS + ((PostBean) headerHolder.txtName.getTag()).avatar.toString())
                        .asBitmap()
                        .error(R.drawable.default_user).placeholder(R.drawable.default_user).into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                        RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        headerHolder.imgProfileAvatar.setImageDrawable(circularBitmapDrawable);
                    }
                });
            }


            headerHolder.imgProfileAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (((PostBean) headerHolder.txtName.getTag()).userid == Pref.getValue(context, Config.PREF_USER_ID, 0)) {
                        Intent intent = new Intent(context, ChangeAvatarActivity.class);
                        context.startActivity(intent);
                    } else {
                        Intent intent = new Intent(context, ProfileDetailActivity.class);
                        intent.putExtra("beanData", (PostBean) headerHolder.txtName.getTag());
                        context.startActivity(intent);
                    }
                }
            });


            headerHolder.txtName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (((PostBean) headerHolder.txtName.getTag()).userid == Pref.getValue(context, Config.PREF_USER_ID, 0)) {
                        Intent intent = new Intent(context, EditProfileActivity.class);
                        context.startActivity(intent);
                    } else {
                        Intent intent = new Intent(context, ProfileDetailActivity.class);
                        intent.putExtra("beanData", (PostBean) headerHolder.txtName.getTag());
                        context.startActivity(intent);
                    }
                }
            });

            headerHolder.llFollower.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if (((PostBean) headerHolder.txtName.getTag()).userid == Pref.getValue(context, Config.PREF_USER_ID, 0)) {
                    if (((PostBean) headerHolder.txtName.getTag()).noOffollowers > 0) {
                        Intent intent = new Intent(context, FollowerListActivity.class);
                        intent.putExtra("userid", ((PostBean) headerHolder.txtName.getTag()).userid);
                        context.startActivity(intent);
                    }
//                    }
                }
            });

            headerHolder.llFollowing.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if (((PostBean) headerHolder.txtName.getTag()).userid == Pref.getValue(context, Config.PREF_USER_ID, 0)) {
                    if (((PostBean) headerHolder.txtName.getTag()).noOffollowers > 0) {
                        Intent intent = new Intent(context, FollowingListActivity.class);
                        intent.putExtra("userid", ((PostBean) headerHolder.txtName.getTag()).userid);
                        context.startActivity(intent);
                    }
//                    }
                }
            });

            headerHolder.txtNoofPost.setText("" + postBean.noOfpost);
            headerHolder.txtNoofFollowers.setText("" + postBean.noOffollowers);
            headerHolder.txtNoofFollowing.setText("" + postBean.noOffollowing);

        } else if (mholder instanceof PostBeanHolder) {
            final PostBeanHolder holder = (PostBeanHolder) mholder;

            postBean = mItemList.get(position);


            holder.txtUserName.setTag(postBean);
            holder.txtUserName.setText(((PostBean) holder.txtUserName.getTag()).name);
            holder.txtFeedTime.setText(((PostBean) holder.txtUserName.getTag()).posttime);

            holder.txtNolike.setText("" + ((PostBean) holder.txtUserName.getTag()).noOflike);
            holder.txtNoComment.setText("" + ((PostBean) holder.txtUserName.getTag()).noOfcomment);

            Utils.setDefaultRoundImage(context, holder.imgAvatar, R.drawable.default_user);
            if (!((PostBean) holder.txtUserName.getTag()).avatar.equalsIgnoreCase("")) {
                Glide.with(context).load(Config.IMAGE_PATH_WEB_AVATARS + ((PostBean) holder.txtUserName.getTag()).avatar).asBitmap().error(R.drawable.default_user).placeholder(R.drawable.default_user).into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                        RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        holder.imgAvatar.setImageDrawable(circularBitmapDrawable);
                    }
                });
            }


            if (((PostBean) holder.txtUserName.getTag()).caption.trim().toString().equalsIgnoreCase("")) {
                holder.txtCaption.setVisibility(View.GONE);
            } else {
                holder.txtCaption.setVisibility(View.VISIBLE);
                holder.txtCaption.setText(((PostBean) holder.txtUserName.getTag()).caption);
            }
            if (!((PostBean) holder.txtUserName.getTag()).image_url.equalsIgnoreCase("")) {
                holder.imgFeed.setVisibility(View.VISIBLE);
                Glide.with(context).load(Config.IMAGE_PATH_WEB_FEED + ((PostBean) holder.txtUserName.getTag()).image_url).asBitmap().error(R.drawable.default_user).placeholder(R.drawable.default_user).into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                        holder.imgFeed.setImageBitmap(resource);
                    }
                });
            } else {
                holder.imgFeed.setVisibility(View.GONE);
            }

            holder.imgLikeUnlike.setImageResource(((PostBean) holder.txtUserName.getTag()).islike == true ? R.drawable.love_white_filled : R.drawable.love_gray);
            holder.imgLikeUnlike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myOnClickListner.IsClick(R.id.imgLikeUnlike, holder.txtUserName.getTag());
                }
            });

            holder.llLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (((PostBean) holder.txtUserName.getTag()).noOflike > 0) {
                        Intent intent = new Intent(context, LikeListActivity.class);
                        intent.putExtra("feedid", ((PostBean) holder.txtUserName.getTag()).feedid);
                        context.startActivity(intent);
                    }
                }
            });

            holder.llComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, FeedDetailActivity.class);
                    intent.putExtra("feedid", ((PostBean) holder.txtUserName.getTag()).feedid);
                    context.startActivity(intent);
                }
            });

            holder.txtCaption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, FeedDetailActivity.class);
                    intent.putExtra("feedid", ((PostBean) holder.txtUserName.getTag()).feedid);
                    context.startActivity(intent);
                }
            });

            holder.imgFeed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, FeedDetailActivity.class);
                    intent.putExtra("feedid", ((PostBean) holder.txtUserName.getTag()).feedid);
                    context.startActivity(intent);

                }
            });

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
        private TextView txtName;
        private TextView txtNoofPost;
        private TextView txtNoofFollowers;
        private TextView txtNoofFollowing;
        private LinearLayout llPost;
        private LinearLayout llFollower;
        private LinearLayout llFollowing;


        public HeaderViewHolder(View itemView) {
            super(itemView);

            imgProfileAvatar = (ImageView) itemView.findViewById(R.id.imgProfileAvatar);
            txtName = (TextView) itemView.findViewById(R.id.txtName);
            txtNoofPost = (TextView) itemView.findViewById(R.id.txtNoofPost);
            txtNoofFollowers = (TextView) itemView.findViewById(R.id.txtNoofFollowers);
            txtNoofFollowing = (TextView) itemView.findViewById(R.id.txtNoofFollowing);
            llPost = (LinearLayout) itemView.findViewById(R.id.llPost);
            llFollower = (LinearLayout) itemView.findViewById(R.id.llFollower);
            llFollowing = (LinearLayout) itemView.findViewById(R.id.llFollowing);
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
        private LinearLayout llComment;
        private LinearLayout llLike;

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
            llComment = (LinearLayout) itemView.findViewById(R.id.llComment);
            llLike = (LinearLayout) itemView.findViewById(R.id.llLike);
        }

    }


}
