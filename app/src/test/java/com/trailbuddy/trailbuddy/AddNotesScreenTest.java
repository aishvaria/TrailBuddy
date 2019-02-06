package com.trailbuddy.trailbuddy;

import android.content.Intent;
import android.os.Build;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.Toolbar;
import android.widget.Button;

import com.trailbuddy.trailbuddy.app.Constants;
import com.trailbuddy.trailbuddy.notes.data.Note;
import com.trailbuddy.trailbuddy.notes.ui.AddNoteActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;

import static com.trailbuddy.trailbuddy.MockTestData.getMockNoteText;
import static com.trailbuddy.trailbuddy.MockTestData.getMocktrailListItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.O_MR1)
public class AddNotesScreenTest {

    AddNoteActivity addNoteActivity;

    @Before
    public void setup() {
        Intent intent = new Intent();
        intent.putExtra(Constants.TRAIL_ITEM_BUNDLE_TAG, getMocktrailListItem());
        addNoteActivity = Robolectric.buildActivity(AddNoteActivity.class, intent).create().get();
    }

    @Test
    public void verifyAddNotesScreen() {
        assertNotNull(addNoteActivity);
    }

    @Test
    public void verifyAddNotesTitleIsDisplayed() {
        Toolbar notestoolbar = addNoteActivity.findViewById(R.id.trail_notes_toolbar);
        assertEquals(notestoolbar.getTitle().toString(), "Add Note");
    }

    @Test
    public void verifySaveButtonIsDisplayed() {
        Button save = addNoteActivity.findViewById(R.id.add_note_button);
        assertNotNull(save);
    }

    @Test
    public void verifyAddNoteEditTextIsDisplayed() {
        TextInputEditText textInputEditText = addNoteActivity.findViewById(R.id.add_note_edit_text);
        assertNotNull(textInputEditText);
    }

    @Test
    public void verifySaveButtonAction() {
        TextInputEditText textInputEditText = addNoteActivity.findViewById(R.id.add_note_edit_text);
        textInputEditText.setText(getMockNoteText());
        addNoteActivity.findViewById(R.id.add_note_button).performClick();
        ShadowActivity shadowActivity = Shadows.shadowOf(addNoteActivity);
        assertEquals(shadowActivity.getResultCode(), 0);
        assertNotNull(shadowActivity.getResultIntent().getExtras());
        Note shadowNote = (Note) shadowActivity.getResultIntent().getExtras().get(Constants.ADD_NOTES_TAG);
        assertNotNull(shadowNote);
        assertEquals(shadowNote.content, getMockNoteText());
    }
}
