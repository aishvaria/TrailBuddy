package com.trailbuddy.trailbuddy.app;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class AppNetworkRequestQueue {

    private static AppNetworkRequestQueue mInstance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;

    private AppNetworkRequestQueue(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized AppNetworkRequestQueue getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new AppNetworkRequestQueue(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }
}
