package com.trailbuddy.trailbuddy;

import android.content.Intent;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.trailbuddy.trailbuddy.app.Constants;
import com.trailbuddy.trailbuddy.photos.adapters.TrailPhotosPagerAdapter;
import com.trailbuddy.trailbuddy.photos.ui.TrailPhotosActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static com.trailbuddy.trailbuddy.MockTestData.getMockTrailDetails;
import static com.trailbuddy.trailbuddy.MockTestData.getMockTrailname;
import static com.trailbuddy.trailbuddy.MockTestData.getMockUser;
import static com.trailbuddy.trailbuddy.MockTestData.getMocktrailListItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.O_MR1)
public class TrailPhotosScreenTest {

    TrailPhotosActivity trailPhotosActivity;

    @Before
    public void setup() {
        Intent intent = new Intent();
        intent.putExtra(Constants.APP_USER_TAG, getMockUser());
        intent.putExtra(Constants.TRAIL_ITEM_BUNDLE_TAG, getMocktrailListItem());
        intent.putExtra(Constants.TRAIL_DETAILS_TAG, getMockTrailDetails());
        trailPhotosActivity = Robolectric.buildActivity(TrailPhotosActivity.class, intent).create().get();
    }

    @Test
    public void verifyTrailPhotosScreen() {
        assertNotNull(trailPhotosActivity);
    }

    @Test
    public void verifyTrailPhotosTitleIsDisplayed() {
        Toolbar toolbar = trailPhotosActivity.findViewById(R.id.trail_photos_toolbar);
        assertEquals(toolbar.getTitle().toString(), "Photos: " + getMockTrailname());
    }

    @Test
    public void verifyAddPhotosButtonIsDisplayed() {
        FloatingActionButton fab = trailPhotosActivity.findViewById(R.id.add_photo_fab);
        assertNotNull(fab);
    }

    @Test
    public void verifyPhotosViewPagerIsDisplayed() {
        ViewPager viewPager = trailPhotosActivity.findViewById(R.id.trail_photos_view_pager);
        assertNotNull(viewPager);
    }

    @Test
    public void verifyPhotosPagerAdapter() {
        ViewPager viewPager = trailPhotosActivity.findViewById(R.id.trail_photos_view_pager);
        TrailPhotosPagerAdapter trailPhotosPagerAdapter = new TrailPhotosPagerAdapter(trailPhotosActivity, getMockTrailDetails());
        viewPager.setAdapter(trailPhotosPagerAdapter);
        assertNotNull(viewPager.getAdapter());
        assertEquals(viewPager.getAdapter().getCount(), 1);
    }
}
