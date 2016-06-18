package com.esp.chatapp.Adapter;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.esp.chatapp.Backend.APIResponseListener;
import com.esp.chatapp.ChatAppApplication;
import com.esp.chatapp.Utils.Config;
import com.esp.chatapp.Utils.Log;
import com.esp.chatapp.Utils.Pref;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class Adapter {

	public Context caller;
	public int httpStatusCode;

	public Adapter(Context caller) {
		this.caller = caller;
	}

	public void doPost(final String tag, String url,
			HashMap<String, String> mParams,
			final APIResponseListener responseListener,
			final ErrorListener errorListener) {
		this.doPost(tag, url, mParams, responseListener, errorListener, true);
	}

	public void doPost(final String tag, String url,
			HashMap<String, String> mParams,
			final APIResponseListener responseListener,
			final ErrorListener errorListener, final boolean showAlert) {
		ApiRequest apiReq = null;
		try {

			apiReq = new ApiRequest(Method.POST, url, mParams,
					new Listener<String>() {

						@Override
						public void onResponse(String response) {
							Log.debug(this.getClass() + "::" + tag, response);
							Log.print(this.getClass()
									+ ":: Response BACKEND:: " + tag, response);

							responseListener.onResponse(response);
						}
					}, new ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {

							Log.print(this.getClass() + " :: Error :: " + tag
									+ ":: " + error.toString());

							if (showAlert) {
								if (error.networkResponse != null) {

									Log.print(this.getClass()
											+ " :: Network Error :: Status :: "
											+ tag + ":: "
											+ error.networkResponse.statusCode);

									if (error.networkResponse.statusCode == 403) {
//										AlertDailogView
//												.showAlert(
//														caller,
//														caller.getResources()
//																.getString(
//																		R.string.error_forbidden),
//														caller.getResources()
//																.getString(
//																		R.string.ok),
//														true).show();
										/**
										 * The request is for something
										 * forbidden. Authorization will not
										 * help
										 */
									} else if (error.networkResponse.statusCode == 404) {
//										AlertDailogView
//												.showAlert(
//														caller,
//														caller.getResources()
//																.getString(
//																		R.string.error_invalid_URI),
//														caller.getResources()
//																.getString(
//																		R.string.ok),
//														true).show();
										/**
										 * The server has not found anything
										 * matching the URI given
										 */
									} else if (error.networkResponse.statusCode == 408) {
//										AlertDailogView
//												.showAlert(
//														caller,
//														caller.getResources()
//																.getString(
//																		R.string.error_timeout),
//														caller.getResources()
//																.getString(
//																		R.string.ok),
//														true).show();
										/**
										 * Request Timeout The client did not
										 * produce a request within the time
										 * that the server was prepared to wait.
										 * The client MAY repeat the request
										 * without modifications at any later
										 * time.
										 */

									} else if (error.networkResponse.statusCode == 500) {
//										AlertDailogView
//												.showAlert(
//														caller,
//														caller.getResources()
//																.getString(
//																		R.string.error_unexpected),
//														caller.getResources()
//																.getString(
//																		R.string.ok),
//														true).show();
										/**
										 * The server encountered an unexpected
										 * condition
										 */
									} else if (error.networkResponse.statusCode == 503) {
//										AlertDailogView
//												.showAlert(
//														caller,
//														caller.getResources()
//																.getString(
//																		R.string.error_server_unavailable),
//														caller.getResources()
//																.getString(
//																		R.string.ok),
//														true).show();
										/**
										 * 503 Gateway Timeout The server is
										 * currently unable to handle the
										 * request due to a temporary
										 * overloading or maintenance of the
										 * serve
										 */
									} else if (error.networkResponse.statusCode == 502
											|| error.networkResponse.statusCode == 504) {
//										AlertDailogView
//												.showAlert(
//														caller,
//														caller.getResources()
//																.getString(
//																		R.string.error_gateway_timeout),
//														caller.getResources()
//																.getString(
//																		R.string.ok),
//														true).show();
										/**
										 * 502 Bad Gateway 504 Gateway Timeout
										 * The server, while acting as a gateway
										 * or proxy, did not receive a timely
										 * response from the upstream server
										 * specified by the URI
										 */
									} else {
										Log.print(this.getClass()
												+ " :: Error Message :: "
												+ tag
												+ ":: "
												+ (error.getMessage() == null ? "NULL - no message"
														: error.getMessage()));

										if (error.getMessage() == null) {
											/**
											 * Unable to get response from the
											 * server, please try again
											 */
											Log.print(this.getClass()
													+ " :: Error :: " + tag
													+ ":: " + error.toString());
//											AlertDailogView
//													.showAlert(
//															caller,
//															caller.getResources()
//																	.getString(
//																			R.string.error_unable_response),
//															caller.getResources()
//																	.getString(
//																			R.string.ok),
//															true).show();
										} else {
											if (error
													.getMessage()
													.indexOf(
															"java.net.ConnectException") != -1
													|| error.getMessage()
															.indexOf(
																	"java.net.SocketException") != -1
													|| error.getMessage()
															.indexOf(
																	"java.io.EOFException") != -1
													|| error.getMessage()
															.indexOf(
																	"java.io.InterruptedIOException") != -1
													|| error.getMessage()
															.indexOf(
																	"java.net.UnknownHostException") != -1) {
//												AlertDailogView
//														.showAlert(
//																caller,
//																caller.getResources()
//																		.getString(
//																				R.string.error_timeout),
//																caller.getResources()
//																		.getString(
//																				R.string.ok),
//																true).show();
												/**
												 * Request Timeout The client
												 * did not produce a request
												 * within the time that the
												 * server was prepared to wait.
												 * The client MAY repeat the
												 * request without modifications
												 * at any later time.
												 */
											} else {
//												AlertDailogView.showAlert(
//														caller,
//														error.getMessage(),
//														"OK", true).show();
											}
										}
									}
								} else {
									Log.print(this.getClass()
											+ " :: Error Message :: "
											+ tag
											+ ":: "
											+ (error.getMessage() == null ? "NULL - no message"
													: error.getMessage()));

									if (error.getMessage() == null) {
										/**
										 * Unable to get response from the
										 * server, please try again
										 */

										Log.print(this.getClass()
												+ " :: Error :: " + tag + ":: "
												+ error.toString());
//										AlertDailogView
//												.showAlert(
//														caller,
//														caller.getResources()
//																.getString(
//																		R.string.error_unable_response),
//														caller.getResources()
//																.getString(
//																		R.string.ok),
//														true).show();
									} else {
										if (error.getMessage().indexOf(
												"java.net.ConnectException") != -1
												|| error.getMessage()
														.indexOf(
																"java.net.SocketException") != -1
												|| error.getMessage().indexOf(
														"java.io.EOFException") != -1
												|| error.getMessage()
														.indexOf(
																"java.io.InterruptedIOException") != -1
												|| error.getMessage()
														.indexOf(
																"java.net.UnknownHostException") != -1) {
//											AlertDailogView
//													.showAlert(
//															caller,
//															caller.getResources()
//																	.getString(
//																			R.string.error_timeout),
//															caller.getResources()
//																	.getString(
//																			R.string.ok),
//															true).show();
											/**
											 * Request Timeout The client did
											 * not produce a request within the
											 * time that the server was prepared
											 * to wait. The client MAY repeat
											 * the request without modifications
											 * at any later time.
											 */
										} else {
//											AlertDailogView.showAlert(caller,
//													error.getMessage(), "OK",
//													true).show();
										}
									}
								}
							}

							errorListener.onErrorResponse(error);
						}
					});

			// Adding request to request queue
			ChatAppApplication.getInstance().addToRequestQueue(apiReq,
					tag);
		} catch (Exception e) {
			Log.error("Exception", e);
		}
	}

	public void doPut(final String tag, String url,
			HashMap<String, String> mParams,
			final APIResponseListener responseListener,
			final ErrorListener errorListener) {
		ApiRequest apiReq = null;
		try {
			Log.print(" PARAMS ::: " + mParams);

			apiReq = new ApiRequest(Method.PUT, url, mParams,
					new Listener<String>() {

						@Override
						public void onResponse(String response) {
							Log.debug(tag, response);
							Log.print(tag, response);

							responseListener.onResponse(response);
						}
					}, new ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {

							Log.print("Error :: " + tag + ":: "
									+ error.toString());

							if (error.getMessage() == null) {
								/**
								 * Unable to get response from the server,
								 * please try again
								 */
//								AlertDailogView
//										.showAlert(
//												caller,
//												" Unable to get response from the server, please try again",
//												"OK", true);
							} else {
								if (error.networkResponse != null) {
									if (error.networkResponse.statusCode == 403) {
//										AlertDailogView
//												.showAlert(
//														caller,
//														"The request is for something forbidden. Authorization will not help",
//														"OK", true);
										/**
										 * The request is for something
										 * forbidden. Authorization will not
										 * help
										 */
									} else if (error.networkResponse.statusCode == 404) {
//										AlertDailogView
//												.showAlert(
//														caller,
//														"The server has not found anything matching the URI given",
//														"OK", true);
										/**
										 * The server has not found anything
										 * matching the URI given
										 */
									} else if (error.networkResponse.statusCode == 500) {
//										AlertDailogView
//												.showAlert(
//														caller,
//														" The server encountered an unexpected condition",
//														"OK", true);
										/**
										 * The server encountered an unexpected
										 * condition
										 */
									} else {
//										AlertDailogView.showAlert(caller,
//												error.getMessage(), "OK", true);
										/** error.getMessage() */
									}
								}
							}

							Log.error("Error :: " + tag, error.getMessage());
							errorListener.onErrorResponse(error);
						}
					});

			// Adding request to request queue
			ChatAppApplication.getInstance().addToRequestQueue(apiReq,
					tag);
		} catch (Exception e) {
			Log.error("Exception", e);
			System.gc();
		} finally {
			apiReq = null;
		}
	}

	public void doGet(final String tag, String url,
			HashMap<String, String> mParams,
			final APIResponseListener responseListener,
			final ErrorListener errorListener) {
		try {
			ApiRequest apiReq = new ApiRequest(Method.GET, url, mParams,
					new Listener<String>() {

						@Override
						public void onResponse(String response) {
							// Log.debug(tag, response);
							// Log.print(tag, response);
							responseListener.statusCode = httpStatusCode;
							Log.print(": Adpter responseListener.statusCode :: "
									+ responseListener.statusCode);
							Log.print(":: Adapter :: " + response);
							responseListener.onResponse(response);
						}
					}, new ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							error.printStackTrace();
							Log.error("Error :: " + tag, error.getMessage());
							Log.print("Error :: " + tag, error.getMessage());

							if (error.getMessage() == null) {
								/**
								 * Unable to get response from the server,
								 * please try again
								 */
							} else {
								if (error.networkResponse != null) {
									if (error.networkResponse.statusCode == 403) {
										/**
										 * The request is for something
										 * forbidden. Authorization will not
										 * help
										 */
									} else if (error.networkResponse.statusCode == 404) {
										/**
										 * The server has not found anything
										 * matching the URI given
										 */
									} else if (error.networkResponse.statusCode == 500) {
										/**
										 * The server encountered an unexpected
										 * condition
										 */
									} else {
										/** error.getMessage() */
									}
								}
							}

							errorListener.onErrorResponse(error);
						}
					});

			// Adding request to request queue
			ChatAppApplication.getInstance().addToRequestQueue(apiReq,
					tag);
		} catch (Exception e) {
			Log.error("Exception", e);
			e.printStackTrace();
		}
	}

	public void doCancel(String tag) {
		try {
			ChatAppApplication.getInstance().cancelPendingRequests(tag);
		} catch (Exception e) {
			Log.error("Exception", e);
		}
	}

	public class ApiRequest extends StringRequest {

		private Map<String, String> mParams;

		public ApiRequest(int method, String url,
				HashMap<String, String> mParams,
				Listener<String> responseListener, ErrorListener errorListener) {
			super(method, url, responseListener, errorListener);

			Log.print("mParams ::" + mParams);
			Log.print("url ::" + url);
			this.mParams = mParams;

			// this.mParams.put("key", Config.API_KEY);
			// this.mParams.put("version", Config.API_VERSION);
			// this.mParams.put("lang", Utils.getCurrentLanguage());
			// Log.print(" API ::: " + mParams);
			/*
			 * SET - Timeout - Number of attemptes - Default 1
			 */
			this.setRetryPolicy(new DefaultRetryPolicy(Config.TIMEOUT_SOCKET,
					DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
					DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		}

		@Override
		public Map<String, String> getParams() {
			return mParams;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.android.volley.toolbox.StringRequest#parseNetworkResponse(com
		 * .android.volley.NetworkResponse)
		 */
		@Override
		protected Response<String> parseNetworkResponse(NetworkResponse response) {
			// since we don't know which of the two underlying network vehicles
			// will Volley use, we have to handle and store session cookies
			// manually
			// this.checkSessionCookie(response.headers);

			httpStatusCode = response.statusCode;

			return super.parseNetworkResponse(response);
		}

		/**
		 * Checks the response headers for session cookie and saves it if it
		 * finds it.
		 * 
		 * @param headers
		 *            Response Headers.
		 */
		public void checkSessionCookie(Map<String, String> headers) {
			if (headers.containsKey(Config.SET_COOKIE_KEY)
					&& headers.get(Config.SET_COOKIE_KEY).startsWith(
							Config.SESSION_COOKIE)) {
				String cookie = headers.get(Config.SET_COOKIE_KEY);
				if (cookie.length() > 0) {
					String[] splitCookie = cookie.split(";");
					String[] splitSessionId = splitCookie[0].split("=");
					cookie = splitSessionId[1];
					Pref.setValue(caller, Config.PREF_SESSION_COOKIE, cookie);
				}
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.android.volley.Request#getHeaders()
		 */
		@Override
		public Map<String, String> getHeaders() throws AuthFailureError {
			Map<String, String> headers = super.getHeaders();

			if (headers == null || headers.equals(Collections.emptyMap())) {
				headers = new HashMap<String, String>();
			} else {
			}
			headers.put("AUTH-KEY", Config.API_KEY);

			return headers;
		}

		/**
		 * Adds session cookie to headers if exists.
		 * 
		 * @param headers
		 */
		public final void addSessionCookie(Map<String, String> headers) {
			String sessionId = Pref.getValue(caller,
					Config.PREF_SESSION_COOKIE, "");
			if (sessionId.length() > 0) {
				StringBuilder builder = new StringBuilder();
				builder.append(Config.PREF_SESSION_COOKIE);
				builder.append("=");
				builder.append(sessionId);
				if (headers.containsKey(Config.COOKIE_KEY)) {
					builder.append("; ");
					builder.append(headers.get(Config.COOKIE_KEY));
				}
				headers.put(Config.COOKIE_KEY, builder.toString());
			}
		}

	}
}
