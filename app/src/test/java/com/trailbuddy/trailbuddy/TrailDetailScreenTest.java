package com.trailbuddy.trailbuddy;

import android.content.Intent;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.trailbuddy.trailbuddy.app.Constants;
import com.trailbuddy.trailbuddy.photos.ui.TrailPhotosActivity;
import com.trailbuddy.trailbuddy.reviews.ui.TrailReviewsActivity;
import com.trailbuddy.trailbuddy.trail.ui.TrailDetailActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowIntent;

import static com.trailbuddy.trailbuddy.MockTestData.getMockTrailname;
import static com.trailbuddy.trailbuddy.MockTestData.getMockUser;
import static com.trailbuddy.trailbuddy.MockTestData.getMocktrailListItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.O_MR1)

public class TrailDetailScreenTest {

    TrailDetailActivity trailDetailActivity;

    @Before
    public void setup() {
        Intent intent = new Intent();
        intent.putExtra(Constants.APP_USER_TAG, getMockUser());
        intent.putExtra(Constants.TRAIL_ITEM_BUNDLE_TAG, getMocktrailListItem());
        trailDetailActivity = Robolectric.buildActivity(TrailDetailActivity.class, intent).create().get();
        trailDetailActivity.onFetchComplete(null);
    }

    @Test
    public void verifyTrailDetailScreen() {
        assertNotNull(trailDetailActivity);
    }

    @Test
    public void verifyTrailDetailsAreDisplayed() {
        TextView id = trailDetailActivity.findViewById(R.id.trail_detail_id);
        assertEquals(id.getText().toString(), "7000130");

        TextView name = trailDetailActivity.findViewById(R.id.trail_detail_name);
        assertEquals(name.getText().toString(), "Bear Peak");

        TextView summary = trailDetailActivity.findViewById(R.id.trail_detail_summary);
        assertEquals(summary.getText().toString(), "A must-do hike for Boulder locals and visitors alike!");

        TextView stars = trailDetailActivity.findViewById(R.id.trail_detail_stars);
        assertEquals(stars.getText().toString(), "Rating: " + "4.5" + "/5 stars");

        TextView location = trailDetailActivity.findViewById(R.id.trail_detail_location);
        assertEquals(location.getText().toString(), "Boulder, Colorado");

        TextView length = trailDetailActivity.findViewById(R.id.trail_detail_length);
        assertEquals(length.getText().toString(), "Trail length: " + "5.7" + " miles");

        ImageView imgMedium = trailDetailActivity.findViewById(R.id.trail_detail_image);
        assertNotNull(imgMedium);

        TextView altitudeLow = trailDetailActivity.findViewById(R.id.trail_detail_alt_low);
        assertEquals(altitudeLow.getText().toString(), "Altitude low: " + "6113" + " feet");

        TextView altitudeHigh = trailDetailActivity.findViewById(R.id.trail_detail_alt_high);
        assertEquals(altitudeHigh.getText().toString(), "Altitude high: " + "8370" + " feet");

        ImageView map = trailDetailActivity.findViewById(R.id.trail_detail_map);
        assertNotNull(map);
    }

    @Test
    public void verifyReviewsButtonIsDisplayed() {
        FloatingActionButton reviewsFab = trailDetailActivity.findViewById(R.id.trail_reviews_fab);
        assertNotNull(reviewsFab);
    }

    @Test
    public void verifyReviewsButtonAction() {
        trailDetailActivity.findViewById(R.id.trail_reviews_fab).performClick();
        ShadowActivity shadowActivity = Shadows.shadowOf(trailDetailActivity);
        Intent startedIntent = shadowActivity.getNextStartedActivity();
        ShadowIntent shadowIntent = Shadows.shadowOf(startedIntent);

        assertEquals(shadowIntent.getIntentClass(), TrailReviewsActivity.class);
    }

    @Test
    public void verifyTrailPhotosClickAction() {
        trailDetailActivity.findViewById(R.id.trail_detail_image).performClick();
        ShadowActivity shadowActivity = Shadows.shadowOf(trailDetailActivity);
        Intent startedIntent = shadowActivity.getNextStartedActivity();
        ShadowIntent shadowIntent = Shadows.shadowOf(startedIntent);

        assertEquals(shadowIntent.getIntentClass(), TrailPhotosActivity.class);
    }

    @Test
    public void verifyTrailMapClickAction() {
        trailDetailActivity.findViewById(R.id.trail_detail_map).performClick();
        ShadowActivity shadowActivity = Shadows.shadowOf(trailDetailActivity);
        Intent startedIntent = shadowActivity.getNextStartedActivity();

        assertEquals(startedIntent.getAction(), Intent.ACTION_VIEW);
        assertEquals(startedIntent.getPackage(), "com.google.android.apps.maps");
    }

    @Test
    public void verifyTrailTitleIsDisplayed() {
        Toolbar reviewstoolbar = trailDetailActivity.findViewById(R.id.trail_detail_toolbar);
        assertEquals(reviewstoolbar.getTitle().toString(), getMockTrailname());
    }
}
