package edu.uottawa.SEG2105.data.model;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {

    private String userId;
    private String displayName;
    private String role;
    private boolean isAdmin;
    public LoggedInUser(String userId, String displayName,String role,boolean isAdmin) {
        this.userId = userId;
        this.displayName = displayName;
        this.role = role;
        this.isAdmin = isAdmin;
    }

    public String getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }
    public String getRoleName() {
        return role;
    }
}