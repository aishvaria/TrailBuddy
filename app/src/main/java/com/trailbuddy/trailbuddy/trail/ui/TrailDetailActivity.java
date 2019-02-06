package com.trailbuddy.trailbuddy.trail.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.trailbuddy.trailbuddy.R;
import com.trailbuddy.trailbuddy.app.Constants;
import com.trailbuddy.trailbuddy.login.User;
import com.trailbuddy.trailbuddy.photos.ui.TrailPhotosActivity;
import com.trailbuddy.trailbuddy.reviews.ui.TrailReviewsActivity;
import com.trailbuddy.trailbuddy.trail.data.TrailDetails;
import com.trailbuddy.trailbuddy.trail.data.TrailListItem;
import com.trailbuddy.trailbuddy.trail.firestore.FirestoneCallbacks;
import com.trailbuddy.trailbuddy.trail.firestore.FirestoreUtils;

import java.util.ArrayList;
import java.util.Objects;

public class TrailDetailActivity extends AppCompatActivity implements FirestoneCallbacks {

    User user;
    TrailListItem trailListItem;
    TrailDetails trailDetails;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trail_detail);
        Toolbar toolbar = findViewById(R.id.trail_detail_toolbar);

        trailListItem = (TrailListItem) getIntent().getSerializableExtra(Constants.TRAIL_ITEM_BUNDLE_TAG);
        user = (User) getIntent().getSerializableExtra(Constants.APP_USER_TAG);

        toolbar.setTitle(trailListItem.name);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        progressBar = findViewById(R.id.trail_detail_progressbar);
        progressBar.setVisibility(View.VISIBLE);

        FloatingActionButton trailReviewsFab = findViewById(R.id.trail_reviews_fab);
        trailReviewsFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToTrailReviewsActivity();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        FirestoreUtils.getTrailDetailsFromFirestone(trailListItem.id, this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void displayTrailDetails() {
        progressBar.setVisibility(View.GONE);

        LinearLayout trailDetailContent = findViewById(R.id.trail_detail_content);
        trailDetailContent.setVisibility(View.VISIBLE);

        TextView id = findViewById(R.id.trail_detail_id);
        id.setText(trailListItem.id);

        TextView length = findViewById(R.id.trail_detail_length);
        String lengthText = "Trail length: <b>" + trailListItem.length + " miles</b>";
        length.setText(Html.fromHtml(lengthText, Html.FROM_HTML_MODE_COMPACT));

        TextView stars = findViewById(R.id.trail_detail_stars);
        String starsText = "Rating: <b>" + trailListItem.stars + "/5 stars</b>";
        stars.setText(Html.fromHtml(starsText, Html.FROM_HTML_MODE_COMPACT));

        TextView name = findViewById(R.id.trail_detail_name);
        name.setText(trailListItem.name);

        TextView duration = findViewById(R.id.trail_detail_location);
        duration.setText(trailListItem.location);

        TextView description = findViewById(R.id.trail_detail_summary);
        description.setText(Html.fromHtml(trailListItem.summary, Html.FROM_HTML_MODE_COMPACT));

        TextView altitudeLow = findViewById(R.id.trail_detail_alt_low);
        String altitudeLowText = "Altitude low: <b>" + trailListItem.altitudeLow + " feet</b>";
        altitudeLow.setText(Html.fromHtml(altitudeLowText, Html.FROM_HTML_MODE_COMPACT));

        TextView altitudeHigh = findViewById(R.id.trail_detail_alt_high);
        String altitudeHighText = "Altitude high: <b>" + trailListItem.altitudeHigh + " feet</b>";
        altitudeHigh.setText(Html.fromHtml(altitudeHighText, Html.FROM_HTML_MODE_COMPACT));

        ImageView image = findViewById(R.id.trail_detail_image);
        Glide.with(this).load(trailListItem.imgMedium).into(image);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToTrailPhotosActivity();
            }
        });

        ImageView imageMap = findViewById(R.id.trail_detail_map);
        String mapUrl = "https://maps.googleapis.com/maps/api/staticmap?center="
                + trailListItem.latitude
                + ","
                + trailListItem.longitude
                + "&zoom=12&scale=2&size=400x400&key="
                + Constants.GOOGLE_API_KEY;
        Glide.with(this).load(mapUrl).into(imageMap);
        imageMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + trailListItem.latitude + "," + trailListItem.longitude);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });
    }

    private void goToTrailReviewsActivity() {
        Intent intent = new Intent(this, TrailReviewsActivity.class);
        intent.putExtra(Constants.TRAIL_ITEM_BUNDLE_TAG, trailListItem);
        intent.putExtra(Constants.APP_USER_TAG, user);
        intent.putExtra(Constants.TRAIL_DETAILS_TAG, trailDetails);
        startActivity(intent);
    }

    private void goToTrailPhotosActivity() {
        Intent intent = new Intent(this, TrailPhotosActivity.class);
        intent.putExtra(Constants.TRAIL_ITEM_BUNDLE_TAG, trailListItem);
        intent.putExtra(Constants.APP_USER_TAG, user);
        intent.putExtra(Constants.TRAIL_DETAILS_TAG, trailDetails);
        startActivity(intent);
    }

    @Override
    public void onFetchComplete(TrailDetails trailDetails) {
        if (trailDetails == null)
            trailDetails = new TrailDetails();

        if (trailDetails.reviews == null)
            trailDetails.reviews = new ArrayList<>();

        if (trailDetails.photos == null)
            trailDetails.photos = new ArrayList<>();

        if (trailDetails.photos.size() == 0)
            trailDetails.photos.add(0, trailListItem.imgMedium);

        this.trailDetails = trailDetails;
        displayTrailDetails();
    }
}
