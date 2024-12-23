package edu.uottawa.SEG2105.ui.login;

/**
 * Class exposing authenticated user details to the UI.
 */
class LoggedInUserView {
    private String displayName;
    private String role;

    LoggedInUserView(String displayName, String r) {
        this.displayName = displayName;
        this.role = r;
    }

    String getDisplayName() {
        return displayName;
    }
    String getRoleName() {
        return role;
    }
}