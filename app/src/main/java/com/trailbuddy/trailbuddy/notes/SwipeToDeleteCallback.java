package com.trailbuddy.trailbuddy.notes;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.trailbuddy.trailbuddy.notes.adapters.NotesAdapter;

public class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {

    private NotesAdapter notesAdapter;

    public SwipeToDeleteCallback(NotesAdapter notesAdapter) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.notesAdapter = notesAdapter;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        int position = viewHolder.getAdapterPosition();
        notesAdapter.deleteNote(position);
    }
}
