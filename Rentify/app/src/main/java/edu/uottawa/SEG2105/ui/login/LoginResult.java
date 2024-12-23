package edu.uottawa.SEG2105.ui.login;

import androidx.annotation.Nullable;

/**
 * Authentication result : success (user details) or error message.
 */
class LoginResult {
    @Nullable
    private LoggedInUserView success;
    @Nullable
    private String error;

    LoginResult(@Nullable String error) {
        this.success = null;
        this.error = error;
    }

    LoginResult(@Nullable LoggedInUserView success) {
        this.error = null;
        this.success = success;
    }

    @Nullable
    LoggedInUserView getSuccess() {
        return success;
    }

    @Nullable
    String getError() {
        return error;
    }
}