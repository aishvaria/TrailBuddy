package com.trailbuddy.trailbuddy.trail.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.trailbuddy.trailbuddy.R;
import com.trailbuddy.trailbuddy.trail.data.TrailListItem;

import java.util.List;

public class TrailListAdapter extends RecyclerView.Adapter<TrailListAdapter.ViewHolder> {

    private List<TrailListItem> mTrailListItems;
    private final OnListInteractionListener mListener;

    public TrailListAdapter(List<TrailListItem> items, OnListInteractionListener listener) {
        mTrailListItems = items;
        mListener = listener;
    }

    public void refreshData(List<TrailListItem> trailListItems) {
        this.mTrailListItems = trailListItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.traillist_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        if(position <= getItemCount()) {
            holder.mItem = mTrailListItems.get(position);
            holder.mTrailName.setText(holder.mItem.name);
            holder.mTrailLocation.setText(holder.mItem.location);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mListener) {
                        mListener.onItemClick(holder.mItem);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mTrailListItems.size();
    }

    public void showProgressBar(boolean show) {
        mListener.showProgressBar(show);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView mTrailName;
        final TextView mTrailLocation;
        TrailListItem mItem;

        ViewHolder(View view) {
            super(view);
            mView = view;
            mTrailName = view.findViewById(R.id.trail_list_name);
            mTrailLocation = view.findViewById(R.id.trail_list_location);
        }
    }

    public interface OnListInteractionListener {
        void onItemClick(TrailListItem item);
        void showProgressBar(boolean show);
    }
}
