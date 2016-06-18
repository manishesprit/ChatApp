package com.rs.timepass.Backend;

public interface ResponseListener {
	// API Response Listener
	public void onResponce(String tag, int result, Object obj);
	public void onResponce(String tag, int result, Object obj,Object obj1);
}
