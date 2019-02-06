package com.trailbuddy.trailbuddy.login;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.trailbuddy.trailbuddy.app.Constants;
import com.trailbuddy.trailbuddy.R;
import com.trailbuddy.trailbuddy.trail.ui.TrailListActivity;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private GoogleSignInClient googleSignInClient;
    private FirebaseAuth auth;

    private Button loginButton;
    private TextView loginUserText;
    private TextView submitFeedback;

    private static boolean AUTH_STATE = false;
    private User appUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.login_toolbar);
        toolbar.setTitle(R.string.title_activity_main);
        setSupportActionBar(toolbar);

        loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(this);
        loginUserText = findViewById(R.id.login_user_text);
        submitFeedback = findViewById(R.id.login_submit_feedback);

        FirebaseApp.initializeApp(getApplicationContext());

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(getApplicationContext(), gso);
        auth = FirebaseAuth.getInstance();

        appUser = (User) getIntent().getSerializableExtra(Constants.APP_USER_TAG);
        if (appUser != null) {
            AUTH_STATE = true;
            displayUser();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.LOGIN_REQUEST_CODE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(Objects.requireNonNull(account));
            } catch (ApiException e) {
                updateUI(null);
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                            updateUI(user);
                        } else {
                            updateUI(null);
                        }
                    }
                });
    }

    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, Constants.LOGIN_REQUEST_CODE);
    }

    private void signOut() {
        auth.signOut();

        googleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI(null);
                    }
                });
    }

    @Override
    public void onClick(View v) {
        if (AUTH_STATE) {
            signOut();
            updateUI(null);
        } else {
            signIn();
        }
    }

    private void updateUI(FirebaseUser user) {
        if (user == null) {
            AUTH_STATE = false;
            loginButton.setText(R.string.login);
            loginUserText.setVisibility(View.GONE);
            submitFeedback.setVisibility(View.GONE);
        } else {
            AUTH_STATE = true;
            loginButton.setText(R.string.logout);
            appUser = saveAppUserInfo(user);
            goToTrailListActivity();
        }
    }

    private User saveAppUserInfo(FirebaseUser user) {
        appUser = new User();
        appUser.displayName = user.getDisplayName();
        appUser.photoUrl = Objects.requireNonNull(user.getPhotoUrl()).toString();
        appUser.email = user.getEmail();
        appUser.UID = user.getUid();
        appUser.phoneNumber = user.getPhoneNumber();
        return appUser;
    }

    private void displayUser() {
        loginUserText.setVisibility(View.VISIBLE);
        submitFeedback.setVisibility(View.VISIBLE);

        String userText = appUser.displayName;
        loginUserText.setText(userText);
        loginButton.setText(R.string.logout);
        submitFeedback.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                launchSubmitFeedbackEmailIntent();
            }
        });
    }

    private void goToTrailListActivity() {
        Intent intent = new Intent(this, TrailListActivity.class);
        intent.putExtra(Constants.APP_USER_TAG, appUser);
        finish();
        startActivity(intent);
    }

    private void launchSubmitFeedbackEmailIntent() {
        String mailto = "mailto:trailbuddy780@gmail.com" +
                "?cc=" + "gayatri.pise@gmail.com;" + "laturkaraishvaria@gmail.com" +
                "&subject=" + Uri.encode("Feedback for TrailBuddy from " + appUser.displayName) +
                "&body=" + Uri.encode("Thank you for using TrailBuddy.\n\nPlease write your feedback below.");

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse(mailto));

        try {
            startActivity(emailIntent);
        } catch (ActivityNotFoundException e) {
            showSnackBarMessage("Unable to submit feedback...");
        }
    }

    private void showSnackBarMessage(String message) {
        Snackbar.make(findViewById(R.id.login_activity_layout), message, Snackbar.LENGTH_LONG).show();
    }
}
