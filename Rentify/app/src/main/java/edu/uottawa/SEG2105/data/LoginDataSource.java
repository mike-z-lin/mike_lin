package edu.uottawa.SEG2105.data;

import edu.uottawa.SEG2105.data.model.*;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public Result<LoggedInUser> login(String username, String password) {

        try {
            // TODO: handle loggedInUser authentication
            UserManager ul = new UserManager();
            if (false == ul.isCorrect(username,password))
                throw new Exception();

            LoggedInUser validUser =
                    new LoggedInUser(
                            java.util.UUID.randomUUID().toString(),
                            username,
                            ul.getRole(username),
                            ul.isAdmin(username));
            return new Result.Success<>(validUser);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public Result<LoggedInUser> register(String username, String password) {

        try {
            // TODO: handle loggedInUser authentication
            UserManager ul = new UserManager();
            LoggedInUser validUser =
                    new LoggedInUser(
                            java.util.UUID.randomUUID().toString(),
                            username,
                            ul.getRole(username),
                            ul.isAdmin(username));
            return new Result.Success<>(validUser);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }
    public void logout() {
        // TODO: revoke authentication
    }
}