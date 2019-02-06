package com.trailbuddy.trailbuddy.notes.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.trailbuddy.trailbuddy.R;
import com.trailbuddy.trailbuddy.app.Constants;
import com.trailbuddy.trailbuddy.notes.data.Note;

import java.util.Objects;

public class AddNoteActivity extends AppCompatActivity {

    private TextInputEditText textInputEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        Toolbar toolbar = findViewById(R.id.trail_notes_toolbar);
        toolbar.setTitle("Add Note");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        textInputEditText = findViewById(R.id.add_note_edit_text);

        Button button = findViewById(R.id.add_note_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Note note = new Note(Objects.requireNonNull(textInputEditText.getText()).toString());
                Intent intent = new Intent();
                intent.putExtra(Constants.ADD_NOTES_TAG, note);
                setResult(0, intent);
                AddNoteActivity.this.finish();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
