package com.trailbuddy.trailbuddy.reviews.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.trailbuddy.trailbuddy.R;
import com.trailbuddy.trailbuddy.reviews.data.Review;
import com.trailbuddy.trailbuddy.trail.data.TrailDetails;

public class TrailReviewsAdapter extends RecyclerView.Adapter<TrailReviewsAdapter.ViewHolder> {

    private TrailDetails trailDetails;

    public TrailReviewsAdapter(TrailDetails trailDetails) {
        this.trailDetails = trailDetails;
    }

    public TrailDetails addReview(Review review) {
        trailDetails.reviews.add(review);
        return trailDetails;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        if (position <= getItemCount()) {
            holder.review = trailDetails.reviews.get(position);
            holder.name.setText(holder.review.name);
            holder.text.setText(holder.review.text);
        }
    }

    @Override
    public int getItemCount() {
        return trailDetails.reviews.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView name;
        final TextView text;
        Review review;

        ViewHolder(View view) {
            super(view);
            mView = view;
            name = view.findViewById(R.id.review_item_name);
            text = view.findViewById(R.id.review_item_review);
        }
    }
}
