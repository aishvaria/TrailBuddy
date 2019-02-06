package com.trailbuddy.trailbuddy;

import android.content.Intent;
import android.os.Build;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.TextView;

import com.trailbuddy.trailbuddy.app.Constants;
import com.trailbuddy.trailbuddy.reviews.data.Review;
import com.trailbuddy.trailbuddy.reviews.ui.AddReviewActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;

import static com.trailbuddy.trailbuddy.MockTestData.getMockReviewText;
import static com.trailbuddy.trailbuddy.MockTestData.getMockTrailname;
import static com.trailbuddy.trailbuddy.MockTestData.getMockUser;
import static com.trailbuddy.trailbuddy.MockTestData.getMockUsername;
import static com.trailbuddy.trailbuddy.MockTestData.getMocktrailListItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.O_MR1)
public class AddReviewsScreenTest {

    AddReviewActivity addReviewActivity;

    @Before
    public void setup() {
        Intent intent = new Intent();
        intent.putExtra(Constants.APP_USER_TAG, getMockUser());
        intent.putExtra(Constants.TRAIL_ITEM_BUNDLE_TAG, getMocktrailListItem());
        addReviewActivity = Robolectric.buildActivity(AddReviewActivity.class, intent).create().get();
    }

    @Test
    public void verifyAddReviewsScreen() {
        assertNotNull(addReviewActivity);
    }

    @Test
    public void verifyAddReviewTitleIsDisplayed() {
        Toolbar toolbar = addReviewActivity.findViewById(R.id.add_review_toolbar);
        assertEquals(toolbar.getTitle().toString(), "Post New Review: " + getMockTrailname());
    }

    @Test
    public void verifyPostNewReviewButtonIsDisplayed() {
        Button button = addReviewActivity.findViewById(R.id.add_review_button);
        assertNotNull(button);
    }

    @Test
    public void verifyPostNewReviewButtonAction() {
        TextInputEditText textInputEditText = addReviewActivity.findViewById(R.id.add_review_edit_text);
        textInputEditText.setText(getMockReviewText());
        addReviewActivity.findViewById(R.id.add_review_button).performClick();
        ShadowActivity shadowActivity = Shadows.shadowOf(addReviewActivity);
        assertEquals(shadowActivity.getResultCode(), 0);
        assertNotNull(shadowActivity.getResultIntent().getExtras());
        Review shadowReview = (Review) shadowActivity.getResultIntent().getExtras().get(Constants.ADD_REVIEW_TAG);
        assertNotNull(shadowReview);
        assertEquals(shadowReview.name, getMockUsername());
        assertEquals(shadowReview.text, getMockReviewText());
    }

    @Test
    public void verifyAddReviewEditTextIsDisplayed() {
        TextInputEditText textInputEditText = addReviewActivity.findViewById(R.id.add_review_edit_text);
        assertNotNull(textInputEditText);
    }

    @Test
    public void verifyUserNameIsDisplayed() {
        TextView username = addReviewActivity.findViewById(R.id.add_review_user_name);
        assertEquals(username.getText(), getMockUsername());
    }


}
