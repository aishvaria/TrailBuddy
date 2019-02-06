package com.trailbuddy.trailbuddy.photos.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import com.fxn.pix.Pix;
import com.trailbuddy.trailbuddy.R;
import com.trailbuddy.trailbuddy.app.Constants;
import com.trailbuddy.trailbuddy.login.User;
import com.trailbuddy.trailbuddy.photos.adapters.TrailPhotosPagerAdapter;
import com.trailbuddy.trailbuddy.photos.storage.StorageUtils;
import com.trailbuddy.trailbuddy.trail.data.TrailDetails;
import com.trailbuddy.trailbuddy.trail.data.TrailListItem;
import com.trailbuddy.trailbuddy.trail.firestore.FirestoreUtils;

import java.util.Objects;

public class TrailPhotosActivity extends AppCompatActivity {

    TrailDetails trailDetails;
    TrailListItem trailListItem;
    User appUser;

    TrailPhotosPagerAdapter trailPhotosPagerAdapter;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trail_photos);

        trailListItem = (TrailListItem) getIntent().getSerializableExtra(Constants.TRAIL_ITEM_BUNDLE_TAG);
        trailDetails = (TrailDetails) getIntent().getSerializableExtra(Constants.TRAIL_DETAILS_TAG);
        appUser = (User) getIntent().getSerializableExtra(Constants.APP_USER_TAG);

        Toolbar toolbar = findViewById(R.id.trail_photos_toolbar);
        String toolbarTitle = "Photos: " + trailListItem.name;
        toolbar.setTitle(toolbarTitle);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        progressBar = findViewById(R.id.photos_progress_bar);
        progressBar.setVisibility(View.GONE);

        ViewPager viewPager = findViewById(R.id.trail_photos_view_pager);
        trailPhotosPagerAdapter = new TrailPhotosPagerAdapter(this, trailDetails);
        viewPager.setAdapter(trailPhotosPagerAdapter);

        FloatingActionButton fab = findViewById(R.id.add_photo_fab);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.PHOTOS_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            progressBar.setVisibility(View.VISIBLE);
            String filePath = data.getStringArrayListExtra(Pix.IMAGE_RESULTS).get(0);
            String uniqueFileName = appUser.UID + System.currentTimeMillis() + ".webp";
            StorageUtils.uploadImageToCloud(filePath, uniqueFileName, new StorageUtils.OnImageUploadActivityListener() {

                @Override
                public void onComplete(Uri downloadUri) {
                    trailDetails.photos.add(downloadUri.toString());
                    trailPhotosPagerAdapter.notifyDataSetChanged();
                    FirestoreUtils.updateTrailDetailsInFirestone(trailListItem.id, trailDetails);
                    progressBar.setVisibility(View.GONE);
                    showSnackBarMessage("New photo posted...");
                }

                @Override
                public void onError() {
                    progressBar.setVisibility(View.GONE);
                    showSnackBarMessage("Error uploading photo...");
                }
            });

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case Constants.STORAGE_PERMISSIONS_REQUEST_CODE: {
                if (grantResults.length == 3
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED
                        && grantResults[2] == PackageManager.PERMISSION_GRANTED)
                    chooseImage();
                else
                    showSnackBarMessage("Storage and camera permissions required to upload pictures.");
            }
        }
    }

    private void chooseImage() {
        if (hasStorageAndCameraPermissions())
            Pix.start(this, Constants.PHOTOS_REQUEST_CODE);
        else
            requestStorageAndCameraPermissions();
    }

    private boolean hasStorageAndCameraPermissions() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void requestStorageAndCameraPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA
                },
                Constants.STORAGE_PERMISSIONS_REQUEST_CODE);
    }

    private void showSnackBarMessage(String message) {
        Snackbar.make(findViewById(R.id.photos_activity_layout), message, Snackbar.LENGTH_LONG).show();
    }
}
