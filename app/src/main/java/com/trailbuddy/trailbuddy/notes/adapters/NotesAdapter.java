package com.trailbuddy.trailbuddy.notes.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.trailbuddy.trailbuddy.R;
import com.trailbuddy.trailbuddy.notes.data.Note;

import java.util.ArrayList;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {

    private List<Note> notes;
    private OnListInteractionListener listener;

    public NotesAdapter(List<Note> notes, OnListInteractionListener listener) {
        this.notes = notes;
        this.listener = listener;
        if(notes == null || notes.size() == 0)
            listener.onEmptyList();
    }

    public int addNote(Note note) {
        if (notes == null)
            notes = new ArrayList<>();
        notes.add(note);
        return getItemCount();
    }

    public void deleteNote(int position) {
        if (position <= getItemCount()) {
            listener.onSwipeToDelete(notes.get(position), position);
            notes.remove(position);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        if (position <= getItemCount()) {
            holder.note = notes.get(position);
            String displayString = "Note " + (position + 1) + ": " + holder.note.content;
            holder.text.setText(displayString);
        }
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView text;
        Note note;

        ViewHolder(View view) {
            super(view);
            mView = view;
            text = view.findViewById(R.id.note_item_text);
        }
    }

    public interface OnListInteractionListener {
        void onSwipeToDelete(Note note, int position);
        void onEmptyList();
    }
}
