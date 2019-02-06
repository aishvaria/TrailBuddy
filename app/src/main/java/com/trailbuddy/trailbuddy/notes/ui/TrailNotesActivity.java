package com.trailbuddy.trailbuddy.notes.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.trailbuddy.trailbuddy.R;
import com.trailbuddy.trailbuddy.app.Constants;
import com.trailbuddy.trailbuddy.notes.SwipeToDeleteCallback;
import com.trailbuddy.trailbuddy.notes.adapters.NotesAdapter;
import com.trailbuddy.trailbuddy.notes.data.Note;
import com.trailbuddy.trailbuddy.notes.data.NoteDatabase;

import java.util.Objects;

public class TrailNotesActivity extends AppCompatActivity implements NotesAdapter.OnListInteractionListener {

    NotesAdapter notesAdapter;
    NoteDatabase noteDatabase;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trail_notes);

        Toolbar toolbar = findViewById(R.id.trail_notes_toolbar);
        String toolbarTitle = "My Notes";
        toolbar.setTitle(toolbarTitle);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.notes_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton fab = findViewById(R.id.add_note_fab);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                goToAddNoteActivity();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        noteDatabase = NoteDatabase.getInstance(this);
        notesAdapter = new NotesAdapter(noteDatabase.getNoteDao().getAll(), this);
        recyclerView.setAdapter(notesAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(notesAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.NOTES_REQUEST_CODE && data != null) {
            Note note = (Note) data.getSerializableExtra(Constants.ADD_NOTES_TAG);
            addNote(note);
        }
    }

    @Override
    public void onSwipeToDelete(Note note, int position) {
        deleteNote(note, position);
    }

    @Override
    public void onEmptyList() {
        showSnackBarMessage("No notes available...");
    }

    private void goToAddNoteActivity() {
        Intent intent = new Intent(this, AddNoteActivity.class);
        startActivityForResult(intent, Constants.NOTES_REQUEST_CODE);
    }

    private void addNote(Note note) {
        noteDatabase.getNoteDao().insert(note);
        int position = notesAdapter.addNote(note);
        notesAdapter.notifyItemInserted(position);
        showSnackBarMessage("Note added...");
    }

    private void deleteNote(Note note, int position) {
        noteDatabase.getNoteDao().delete(note);
        notesAdapter.notifyItemRemoved(position);
        showSnackBarMessage("Note deleted...");
    }

    private void showSnackBarMessage(String message) {
        Snackbar.make(findViewById(R.id.notes_activity_layout), message, Snackbar.LENGTH_LONG).show();
    }
}
