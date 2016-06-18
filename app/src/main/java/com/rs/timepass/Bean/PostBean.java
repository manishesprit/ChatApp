package com.rs.timepass.Bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by admin on 4/5/16.
 */
public class PostBean extends UserBean implements Serializable {
    public int feedid;
    public String image_url;
    public String caption;
    public boolean islike;
    public int noOflike;
    public int noOfcomment;
    public String posttime;

    public ArrayList<CommentBean> commentBeanArrayList;
}
