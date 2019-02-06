package com.trailbuddy.trailbuddy.reviews.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.trailbuddy.trailbuddy.app.Constants;
import com.trailbuddy.trailbuddy.R;
import com.trailbuddy.trailbuddy.trail.data.TrailListItem;
import com.trailbuddy.trailbuddy.login.User;
import com.trailbuddy.trailbuddy.reviews.data.Review;

import java.util.Objects;

public class AddReviewActivity extends AppCompatActivity {

    private TextInputEditText textInputEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_review);

        TrailListItem trailListItem = (TrailListItem) getIntent().getSerializableExtra(Constants.TRAIL_ITEM_BUNDLE_TAG);
        final User appUser = (User) getIntent().getSerializableExtra(Constants.APP_USER_TAG);

        Toolbar toolbar = findViewById(R.id.add_review_toolbar);
        toolbar.setTitle("Post New Review: " + trailListItem.name);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        TextView username = findViewById(R.id.add_review_user_name);
        username.setText(appUser.displayName);

        textInputEditText = findViewById(R.id.add_review_edit_text);

        Button button = findViewById(R.id.add_review_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Review review = new Review();
                review.name = appUser.displayName;
                review.text = Objects.requireNonNull(textInputEditText.getText()).toString();
                Intent intent = new Intent();
                intent.putExtra(Constants.ADD_REVIEW_TAG, review);
                setResult(0, intent);
                AddReviewActivity.this.finish();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
