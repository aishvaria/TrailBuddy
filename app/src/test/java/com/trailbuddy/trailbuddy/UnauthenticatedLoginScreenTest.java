package com.trailbuddy.trailbuddy;

import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.internal.SignInHubActivity;
import com.trailbuddy.trailbuddy.login.LoginActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowIntent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.O_MR1)
public class UnauthenticatedLoginScreenTest {

    LoginActivity loginActivity;

    @Before
    public void setup() {
        loginActivity = Robolectric.buildActivity(LoginActivity.class).create().get();
    }

    @Test
    public void verifyUnauthenticatedLoginScreen() {
        assertNotNull(loginActivity);
    }

    @Test
    public void verifyUsernameIsNotDisplayed() {
        TextView username = loginActivity.findViewById(R.id.login_user_text);
        assertEquals(username.getVisibility(), View.GONE);
    }

    @Test
    public void verifyLoginButtonIsDisplayed() {
        Button login = loginActivity.findViewById(R.id.login_button);
        assertEquals(login.getText().toString(), loginActivity.getResources().getString(R.string.login));
    }

    @Test
    public void verifyFeedbackIsNotDisplayed() {
        TextView feedback = loginActivity.findViewById(R.id.login_submit_feedback);
        assertEquals(feedback.getVisibility(), View.GONE);
    }

}
