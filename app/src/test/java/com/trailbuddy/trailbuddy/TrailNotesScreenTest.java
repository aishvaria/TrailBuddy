package com.trailbuddy.trailbuddy;


import android.content.Intent;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.trailbuddy.trailbuddy.app.Constants;
import com.trailbuddy.trailbuddy.notes.adapters.NotesAdapter;
import com.trailbuddy.trailbuddy.notes.ui.AddNoteActivity;
import com.trailbuddy.trailbuddy.notes.ui.TrailNotesActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowIntent;

import static com.trailbuddy.trailbuddy.MockTestData.getMockNotes;
import static com.trailbuddy.trailbuddy.MockTestData.getMocktrailListItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.O_MR1)
public class TrailNotesScreenTest {

    TrailNotesActivity trailNotesActivity;

    @Before
    public void setup() {
        Intent intent = new Intent();
        intent.putExtra(Constants.TRAIL_ITEM_BUNDLE_TAG, getMocktrailListItem());
        trailNotesActivity = Robolectric.buildActivity(TrailNotesActivity.class, intent).create().get();
    }

    @Test
    public void verifyTrailNotesScreen() {
        assertNotNull(trailNotesActivity);
    }

    @Test
    public void verifyNotesTitleIsDisplayed() {
        Toolbar notestoolbar = trailNotesActivity.findViewById(R.id.trail_notes_toolbar);
        assertEquals(notestoolbar.getTitle().toString(), "My Notes");
    }

    @Test
    public void verifyAddNotesButtonIsDisplayed() {
        FloatingActionButton noteFab = trailNotesActivity.findViewById(R.id.add_note_fab);
        assertNotNull(noteFab);
    }

    @Test
    public void verifyAddNotesButtonAction() {
        trailNotesActivity.findViewById(R.id.add_note_fab).performClick();

        ShadowActivity shadowActivity = Shadows.shadowOf(trailNotesActivity);
        Intent startedIntent = shadowActivity.getNextStartedActivity();
        ShadowIntent shadowIntent = Shadows.shadowOf(startedIntent);
        assertEquals(shadowIntent.getIntentClass(), AddNoteActivity.class);
    }

    @Test
    public void verifyNotesListIsDisplayed() {
        RecyclerView recyclerView = trailNotesActivity.findViewById(R.id.notes_list);
        assertNotNull(recyclerView);
    }

    @Test
    public void verifyNotesListAdapter() {
        RecyclerView recyclerView = trailNotesActivity.findViewById(R.id.notes_list);
        NotesAdapter notesAdapter = new NotesAdapter(getMockNotes(), null);
        recyclerView.setAdapter(notesAdapter);
        assertNotNull(recyclerView.getAdapter());
        assertEquals(recyclerView.getAdapter().getItemCount(), 1);
    }
}


