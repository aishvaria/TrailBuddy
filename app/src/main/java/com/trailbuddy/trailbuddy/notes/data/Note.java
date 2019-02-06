package com.trailbuddy.trailbuddy.notes.data;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.trailbuddy.trailbuddy.app.Constants;

import java.io.Serializable;

@Entity(tableName = Constants.NOTES_TABLE)
public class Note implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public int note_id;

    public String content;

    public Note(String content) {
        this.content = content;
    }
}
