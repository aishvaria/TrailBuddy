package com.trailbuddy.trailbuddy;

import android.content.Intent;
import android.os.Build;
import android.widget.Button;
import android.widget.TextView;

import com.trailbuddy.trailbuddy.app.Constants;
import com.trailbuddy.trailbuddy.login.LoginActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;

import static com.trailbuddy.trailbuddy.MockTestData.getMockFeedbackIntent;
import static com.trailbuddy.trailbuddy.MockTestData.getMockUser;
import static com.trailbuddy.trailbuddy.MockTestData.getMockUsername;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.O_MR1)
public class AuthenticatedLoginScreenTest {

    LoginActivity loginActivity;

    @Before
    public void setup() {
        Intent intent = new Intent();
        intent.putExtra(Constants.APP_USER_TAG, getMockUser());
        loginActivity = Robolectric.buildActivity(LoginActivity.class, intent).create().get();
    }

    @Test
    public void verifyAuthenticatedLoginScreen() {
        assertNotNull(loginActivity);
    }

    @Test
    public void verifyFeedbackIntent() {
        loginActivity.findViewById(R.id.login_submit_feedback).performClick();
        ShadowActivity shadowActivity = Shadows.shadowOf(loginActivity);
        Intent actualIntent = shadowActivity.getNextStartedActivity();
        assertTrue(actualIntent.filterEquals(getMockFeedbackIntent()));
    }

    @Test
    public void verifyUsernameIsDisplayed() {
        TextView username = loginActivity.findViewById(R.id.login_user_text);
        assertEquals(username.getText(), getMockUsername());
    }

    @Test
    public void verifyLogoutButtonIsDisplayed() {
        Button logout = loginActivity.findViewById(R.id.login_button);
        assertEquals(logout.getText().toString(), loginActivity.getResources().getString(R.string.logout));
    }
}
