package com.trailbuddy.trailbuddy;

import android.content.Intent;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.trailbuddy.trailbuddy.app.Constants;
import com.trailbuddy.trailbuddy.reviews.adapters.TrailReviewsAdapter;
import com.trailbuddy.trailbuddy.reviews.ui.AddReviewActivity;
import com.trailbuddy.trailbuddy.reviews.ui.TrailReviewsActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowIntent;

import static com.trailbuddy.trailbuddy.MockTestData.getMockTrailDetails;
import static com.trailbuddy.trailbuddy.MockTestData.getMockTrailname;
import static com.trailbuddy.trailbuddy.MockTestData.getMockUser;
import static com.trailbuddy.trailbuddy.MockTestData.getMocktrailListItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.O_MR1)
public class TrailReviewsScreenTest {

    TrailReviewsActivity trailReviewsActivity;

    @Before
    public void setup() {
        Intent intent = new Intent();
        intent.putExtra(Constants.APP_USER_TAG, getMockUser());
        intent.putExtra(Constants.TRAIL_ITEM_BUNDLE_TAG, getMocktrailListItem());
        intent.putExtra(Constants.TRAIL_DETAILS_TAG, getMockTrailDetails());
        trailReviewsActivity = Robolectric.buildActivity(TrailReviewsActivity.class, intent).create().get();
    }

    @Test
    public void verifyTrailReviewsScreen() {
        assertNotNull(trailReviewsActivity);
    }

    @Test
    public void verifyAddReviewButtonIsDisplayed() {
        FloatingActionButton reviewFab = trailReviewsActivity.findViewById(R.id.add_review_fab);
        assertNotNull(reviewFab);
    }

    @Test
    public void verifyAddReviewButtonAction() {
        trailReviewsActivity.findViewById(R.id.add_review_fab).performClick();
        ShadowActivity shadowActivity = Shadows.shadowOf(trailReviewsActivity);
        Intent startedIntent = shadowActivity.getNextStartedActivity();
        ShadowIntent shadowIntent = Shadows.shadowOf(startedIntent);

        assertEquals(shadowIntent.getIntentClass(), AddReviewActivity.class);
    }

    @Test
    public void verifyTrailTitleIsDisplayed() {
        Toolbar reviewstoolbar = trailReviewsActivity.findViewById(R.id.trail_reviews_toolbar);
        assertEquals(reviewstoolbar.getTitle().toString(), "Reviews: " + getMockTrailname());
    }

    @Test
    public void verifyReviewsListIsDisplayed() {
        RecyclerView recyclerView = trailReviewsActivity.findViewById(R.id.trail_reviews_list);
        assertNotNull(recyclerView);
    }

    @Test
    public void verifyReviewsListAdapter() {
        RecyclerView recyclerView = trailReviewsActivity.findViewById(R.id.trail_reviews_list);
        TrailReviewsAdapter trailReviewsAdapter = new TrailReviewsAdapter(getMockTrailDetails());
        recyclerView.setAdapter(trailReviewsAdapter);
        assertNotNull(recyclerView.getAdapter());
        assertEquals(recyclerView.getAdapter().getItemCount(), 1);
    }
}
