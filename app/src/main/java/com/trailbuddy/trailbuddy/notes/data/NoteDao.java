package com.trailbuddy.trailbuddy.notes.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.trailbuddy.trailbuddy.app.Constants;

import java.util.List;

@Dao
public interface NoteDao {

    @Query("SELECT * FROM " + Constants.NOTES_TABLE)
    List<Note> getAll();

    @Insert
    void insert(Note note);

    @Delete
    void delete(Note note);
}
