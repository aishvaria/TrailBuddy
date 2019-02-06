package com.trailbuddy.trailbuddy.trail.network;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.trailbuddy.trailbuddy.app.AppNetworkRequestQueue;
import com.trailbuddy.trailbuddy.app.Constants;
import com.trailbuddy.trailbuddy.R;
import com.trailbuddy.trailbuddy.trail.data.LocationCoordinates;
import com.trailbuddy.trailbuddy.trail.data.TrailListData;
import com.trailbuddy.trailbuddy.trail.adapters.TrailListAdapter;

public class FetchAndLoadTrailListData extends AsyncTask<String, Void, String> {

    private Activity activity;
    private TrailListAdapter adapter;
    private LocationCoordinates locationCoordinates;

    public FetchAndLoadTrailListData(Activity activity, TrailListAdapter adapter, LocationCoordinates locationCoordinates) {
        this.activity = activity;
        this.adapter = adapter;
        this.locationCoordinates = locationCoordinates;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        adapter.showProgressBar(true);
    }

    @Override
    protected String doInBackground(String... strings) {
        RequestQueue queue = AppNetworkRequestQueue.getInstance(activity.getApplicationContext()).getRequestQueue();
        String url = Constants.BASE_URL
                + "get-trails?maxResults=" + Constants.TRAILS_LIST_LIMIT
                + "&maxDistance=" + Constants.TRAILS_DISTANCE_LIMIT
                + "&lat=" + locationCoordinates.latitude
                + "&lon=" + locationCoordinates.longitude
                + "&key=" + Constants.HIKING_PROJECT_API_KEY;
        TrailListData.clearItems();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        TrailListData trailListData = gson.fromJson(response, TrailListData.class);
                        TrailListData.addAll(trailListData.trailListItems);
                        if (adapter != null && trailListData.trailListItems.size() > 0) {
                            adapter.refreshData(trailListData.trailListItems);
                            adapter.notifyDataSetChanged();
                        } else if (trailListData.trailListItems.size() == 0) {
                            showSnackBarMessage("No trails available around this area.");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(Constants.LOG_TAG, error.getLocalizedMessage());
                showSnackBarMessage("Something went wrong. Please try again.");
            }
        });

        queue.add(stringRequest);
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        adapter.showProgressBar(false);
    }

    private void showSnackBarMessage(String message) {
        Snackbar.make(activity.findViewById(R.id.main_activity_layout), message, Snackbar.LENGTH_LONG).show();
    }
}
