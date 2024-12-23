package edu.uottawa.SEG2105.ui.login;

import android.app.Activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import edu.uottawa.SEG2105.R;
import edu.uottawa.SEG2105.data.model.UserManager;
import edu.uottawa.SEG2105.databinding.ActivityLoginRegisterBinding;

public class LoginRegisterActivity extends AppCompatActivity {
    ActivityResultLauncher<Intent> postLauncher;
    private LoginViewModel loginViewModel;
    private ActivityLoginRegisterBinding binding;
    private String role = "noRole";;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        postLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            int avatarId = data.getIntExtra("avatarId", 0);
                            if (avatarId != 0) {
                                //logoImageView.setImageResource(avatarId);
                            }
                        }
                    }
                });

        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        final EditText usernameEditText = binding.username;
        final EditText emailEditText = binding.email;
        final Button loginButton = binding.login;
        final RadioButton adminButton = binding.admin;
        final RadioButton lessorButton = binding.lessor;
        final RadioButton renterButton = binding.renter;
        final EditText passwordEditText = binding.password;
        final Button registerButton = binding.register;
        final ProgressBar loadingProgressBar = binding.loading;

        loginViewModel.setUsersListener();
        RadioGroup radioGroup = findViewById(R.id.roleGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // This will be called when the user selects any RadioButton
                switch (checkedId) {
                    case R.id.admin:
                        // Handle RadioButton 1 selected
                            role = "Admin";
                        break;
                    case R.id.lessor:
                        // Handle RadioButton 2 selected
                        role = "Lessor";
                        break;
                    case R.id.renter:
                        // Handle RadioButton 2 selected
                        role = "Renter";
                        break;
                    // Add more cases if you have more buttons
                }
            }
        });

        lessorButton.setChecked(true);
        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }

                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
                if (loginFormState.getEmailError() != null) {
                    emailEditText.setError(getString(loginFormState.getEmailError()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                }
                setResult(Activity.RESULT_OK);

                //Complete and destroy login activity once successful
                //finish();
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(), emailEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };

        usernameEditText.addTextChangedListener(afterTextChangedListener);
        emailEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.register(usernameEditText.getText().toString(),emailEditText.getText().toString(),role,
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                loginViewModel.login(usernameEditText.getText().toString(), passwordEditText.getText().toString());
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                loginViewModel.register(usernameEditText.getText().toString(),
                        emailEditText.getText().toString(),role, passwordEditText.getText().toString());
            }
        });
    }



    private void updateUiWithUser(LoggedInUserView model) {
        //displayLoginResult(title,msg);
        UserManager ul =new UserManager(model.getDisplayName(),model.getRoleName());

        Intent intent = new Intent(this, PostLoginActivity.class);
        postLauncher.launch(intent);
        //String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        //Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(String errorString) {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginRegisterActivity.this);
        // Set title, message, and button actions
        builder.setTitle("Error")
                .setMessage(errorString)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Action when OK is pressed
                        Toast.makeText(getApplicationContext(), "try again.", Toast.LENGTH_SHORT).show();
                    }
                });
        // Create and show the dialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}