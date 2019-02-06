package com.trailbuddy.trailbuddy.reviews.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.trailbuddy.trailbuddy.app.Constants;
import com.trailbuddy.trailbuddy.R;
import com.trailbuddy.trailbuddy.trail.data.TrailListItem;
import com.trailbuddy.trailbuddy.trail.firestore.FirestoreUtils;
import com.trailbuddy.trailbuddy.reviews.data.Review;
import com.trailbuddy.trailbuddy.trail.data.TrailDetails;
import com.trailbuddy.trailbuddy.login.User;
import com.trailbuddy.trailbuddy.reviews.adapters.TrailReviewsAdapter;

import java.util.Objects;

public class TrailReviewsActivity extends AppCompatActivity {

    TrailListItem trailListItem;
    User user;

    TrailReviewsAdapter trailReviewsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trail_reviews);

        trailListItem = (TrailListItem) getIntent().getSerializableExtra(Constants.TRAIL_ITEM_BUNDLE_TAG);
        TrailDetails trailDetails = (TrailDetails) getIntent().getSerializableExtra(Constants.TRAIL_DETAILS_TAG);
        user = (User) getIntent().getSerializableExtra(Constants.APP_USER_TAG);

        Toolbar toolbar = findViewById(R.id.trail_reviews_toolbar);
        String toolbarTitle = "Reviews: " + trailListItem.name;
        toolbar.setTitle(toolbarTitle);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        RecyclerView recyclerView = findViewById(R.id.trail_reviews_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (trailDetails.reviews.size() == 0)
            showSnackBarMessage("No reviews available for this trail...");

        trailReviewsAdapter = new TrailReviewsAdapter(trailDetails);
        recyclerView.setAdapter(trailReviewsAdapter);

        FloatingActionButton fab = findViewById(R.id.add_review_fab);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                goToAddReviewActivity();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REVIEWS_REQUEST_CODE && data != null) {
            Review review = (Review) data.getSerializableExtra(Constants.ADD_REVIEW_TAG);
            addReview(review);
        }
    }

    private void goToAddReviewActivity() {
        Intent intent = new Intent(this, AddReviewActivity.class);
        intent.putExtra(Constants.TRAIL_ITEM_BUNDLE_TAG, trailListItem);
        intent.putExtra(Constants.APP_USER_TAG, user);
        startActivityForResult(intent, Constants.REVIEWS_REQUEST_CODE);
    }

    private void addReview(Review review) {
        TrailDetails trailDetails = trailReviewsAdapter.addReview(review);
        trailReviewsAdapter.notifyItemInserted(trailDetails.reviews.size());
        FirestoreUtils.updateTrailDetailsInFirestone(trailListItem.id, trailDetails);
        showSnackBarMessage("New review posted...");
    }

    private void showSnackBarMessage(String message) {
        Snackbar.make(findViewById(R.id.reviews_activity_layout), message, Snackbar.LENGTH_LONG).show();
    }
}
