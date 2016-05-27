package com.esp.chatapp.Bean;

import java.io.Serializable;

/**
 * Created by admin on 4/5/16.
 */
public class UserBean implements Serializable {
    public int userid;
    public String username;
    public String password;
    public String email;
    public int noOfpost;
    public int noOffollowers;
    public int noOffollowing;
    public String avatar;
    public String latlong;
    public String udID;
    public String name;
    public String mobile;
    public String status;
    public String city;
    public int offset;
    public int limit;
    public boolean myFeed;
    public boolean isFollowing;
    public boolean isFollower;
}
