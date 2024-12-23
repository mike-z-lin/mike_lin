package edu.uottawa.SEG2105.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Patterns;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edu.uottawa.SEG2105.data.LoginRepository;
import edu.uottawa.SEG2105.data.Result;
import edu.uottawa.SEG2105.data.model.LoggedInUser;
import edu.uottawa.SEG2105.R;
import edu.uottawa.SEG2105.data.model.*;

public class LoginViewModel extends ViewModel {

    UserManager ul;

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;
    private FirebaseDatabase fd;
    private boolean hasAdmin;

    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
        fd = FirebaseDatabase.getInstance();
        ul = new UserManager();
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String username, String password) {
        // can be launched in a separate asynchronous job
        Result<LoggedInUser> result = loginRepository.login(username, password);

        if (result instanceof Result.Success) {
            LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
            loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName(), data.getRoleName())));
        } else {
            loginResult.setValue(new LoginResult("Login Failed, wrong user and password."));
        }
    }
    public void registerLogin(String username, String email, String role,String password) {
        ul.currentUser = username;

        if (email.isEmpty()){
            login(username,password);
        }
        else {
            register(username, email, role,password);
        }
    }
    public void register(String username, String email, String role,String password) {
        if (ul.hasAdmin() && role.equals("Admin")) {
            loginResult.setValue(new LoginResult("Admin role existed already."));
        }
        else {
            DatabaseReference ref = fd.getReference("accounts/");
            String id = ref.push().getKey();
            User u = new User(id, username, password, email, role);
            ref.child(id).setValue(u);

            loginResult.setValue(new LoginResult(new LoggedInUserView(username, role)));
        }

    }

    public void setUsersListener() {
        DatabaseReference ref = fd.getReference("accounts/");
        ul = new UserManager();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ul.users.clear();
                // Iterate over each child node under "users"
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    // Convert each child node to a User object
                    User user = userSnapshot.getValue(User.class);
                    ul.users.add( user);  // Add each user to the list
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

    }

    public boolean hasAdmin() {
        return  hasAdmin = ul.hasAdmin();
    }


    public void loginDataChanged(String username,String email,  String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username,null, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password,null));
        } else if (!isEMailValid(email)) {
            loginFormState.setValue(new LoginFormState(null, null, R.string.invalid_email));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        return username.trim().length() > 0;
    }

    private boolean isEMailValid(String email) {
        if (email == null) {
            return false;
        }
        if (!email.trim().isEmpty()) {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches();
        } else {
            return true;
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}