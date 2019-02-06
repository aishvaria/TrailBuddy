package com.trailbuddy.trailbuddy;

import android.content.Intent;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.trailbuddy.trailbuddy.app.Constants;
import com.trailbuddy.trailbuddy.compass.CompassActivity;
import com.trailbuddy.trailbuddy.login.LoginActivity;
import com.trailbuddy.trailbuddy.notes.ui.TrailNotesActivity;
import com.trailbuddy.trailbuddy.trail.adapters.TrailListAdapter;
import com.trailbuddy.trailbuddy.trail.ui.TrailListActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.fakes.RoboMenuItem;
import org.robolectric.shadow.api.Shadow;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowIntent;
import org.robolectric.shadows.gms.common.ShadowGoogleApiAvailability;

import static com.trailbuddy.trailbuddy.MockTestData.getMockPlaceSearchIntent;
import static com.trailbuddy.trailbuddy.MockTestData.getMockTrailsList;
import static com.trailbuddy.trailbuddy.MockTestData.getMockUser;
import static com.trailbuddy.trailbuddy.MockTestData.getMockUsername;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.O_MR1, shadows = {ShadowGoogleApiAvailability.class})

public class TrailListScreenTest {

    TrailListActivity trailListActivity;

    @Before
    public void setup() {
        Intent intent = new Intent();
        intent.putExtra(Constants.APP_USER_TAG, getMockUser());
        trailListActivity = Robolectric.buildActivity(TrailListActivity.class, intent).create().get();
    }

    @Test
    public void verifyTrailListScreen() {
        assertNotNull(trailListActivity);
    }

    @Test
    public void verifyDefaultTitleIsDisplayed() {
        Toolbar toolbar = trailListActivity.findViewById(R.id.main_toolbar);
        assertEquals(toolbar.getTitle().toString(), "Hello, " + getMockUsername());
    }

    @Test
    public void verifyPlaceSearchButtonIsDisplayed() {
        FloatingActionButton searchFab = trailListActivity.findViewById(R.id.place_search_fab);
        assertNotNull(searchFab);
    }

    @Test
    public void verifyCompassButtonIsDisplayed() {
        MenuItem compassMenuItem = new RoboMenuItem(R.id.action_compass);
        assertNotNull(compassMenuItem);
    }

    @Test
    public void verifyAccountButtonIsDisplayed() {
        MenuItem accountMenuItem = new RoboMenuItem(R.id.action_account);
        assertNotNull(accountMenuItem);
    }

    @Test
    public void verifyNotesButtonIsDisplayed() {
        MenuItem NotesMenuItem = new RoboMenuItem(R.id.action_notes);
        assertNotNull(NotesMenuItem);
    }

    @Test
    public void verifyPlaceSearchButtonAction() {
        final ShadowGoogleApiAvailability shadowGoogleApiAvailability
                = Shadow.extract(GoogleApiAvailability.getInstance());
        shadowGoogleApiAvailability.setIsGooglePlayServicesAvailable(ConnectionResult.SUCCESS);

        trailListActivity.findViewById(R.id.place_search_fab).performClick();

        ShadowActivity shadowActivity = Shadows.shadowOf(trailListActivity);
        Intent startedIntent = shadowActivity.getNextStartedActivity();

        assertEquals(startedIntent, getMockPlaceSearchIntent(trailListActivity));
    }

    @Test
    public void verifyCompassMenuButtonAction() {
        MenuItem menuItem = new RoboMenuItem(R.id.action_compass);
        trailListActivity.onOptionsItemSelected(menuItem);
        ShadowActivity shadowActivity = Shadows.shadowOf(trailListActivity);
        Intent startedIntent = shadowActivity.getNextStartedActivity();
        ShadowIntent shadowIntent = Shadows.shadowOf(startedIntent);

        assertEquals(shadowIntent.getIntentClass(), CompassActivity.class);
    }

    @Test
    public void verifyAccountMenuButtonAction() {
        MenuItem menuItem = new RoboMenuItem(R.id.action_account);
        trailListActivity.onOptionsItemSelected(menuItem);
        ShadowActivity shadowActivity = Shadows.shadowOf(trailListActivity);
        Intent startedIntent = shadowActivity.getNextStartedActivity();
        ShadowIntent shadowIntent = Shadows.shadowOf(startedIntent);

        assertEquals(shadowIntent.getIntentClass(), LoginActivity.class);
    }

    @Test
    public void verifyNotesMenuButtonAction() {
        MenuItem menuItem = new RoboMenuItem(R.id.action_notes);
        trailListActivity.onOptionsItemSelected(menuItem);
        ShadowActivity shadowActivity = Shadows.shadowOf(trailListActivity);
        Intent startedIntent = shadowActivity.getNextStartedActivity();
        ShadowIntent shadowIntent = Shadows.shadowOf(startedIntent);

        assertEquals(shadowIntent.getIntentClass(), TrailNotesActivity.class);
    }

    @Test
    public void verifyTrailsListIsDisplayed() {
        RecyclerView recyclerView = trailListActivity.findViewById(R.id.list);
        assertNotNull(recyclerView);
    }

    @Test
    public void verifyTrailsListAdapter() {
        RecyclerView recyclerView = trailListActivity.findViewById(R.id.list);
        TrailListAdapter trailListAdapter = new TrailListAdapter(getMockTrailsList(), null);
        recyclerView.setAdapter(trailListAdapter);
        assertNotNull(recyclerView.getAdapter());
        assertEquals(recyclerView.getAdapter().getItemCount(), 1);
    }
}
