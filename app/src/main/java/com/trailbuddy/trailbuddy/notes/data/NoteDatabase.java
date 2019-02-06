package com.trailbuddy.trailbuddy.notes.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.trailbuddy.trailbuddy.app.Constants;

@Database(entities = {Note.class}, version = 1)
public abstract class NoteDatabase extends RoomDatabase {

    public abstract NoteDao getNoteDao();

    private static NoteDatabase noteDatabase;

    public static NoteDatabase getInstance(Context context) {
        if (noteDatabase == null) {
            noteDatabase = buildDatabaseInstance(context);
        }
        return noteDatabase;
    }

    private static NoteDatabase buildDatabaseInstance(Context context) {
        return Room.databaseBuilder(context.getApplicationContext(),
                NoteDatabase.class,
                Constants.NOTES_DB_NAME)
                .allowMainThreadQueries().build();
    }
}
