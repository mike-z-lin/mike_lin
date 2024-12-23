package edu.uottawa.SEG2105.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import edu.uottawa.SEG2105.AdminCategoryActivity;
import edu.uottawa.SEG2105.AdminUserActivity;
import edu.uottawa.SEG2105.LessorItemActivity;
import edu.uottawa.SEG2105.RenterItemActivity;
import edu.uottawa.SEG2105.data.model.UserManager;
import edu.uottawa.SEG2105.R;

public class PostLoginActivity extends AppCompatActivity {
    ActivityResultLauncher<Intent> postLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        postLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                        }
                    }
                });



        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_post_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        UserManager ul =new UserManager();
        String title = "Welcome " + ul.getName() +"!\n";
               title += "You are logged in as \"" + ul.getRole() +"\".\n";
        String msg = "";
        //if (ul.isAdmin())
        //    msg += "Here is the user list.\n"+ul.toString();

        TextView prompt = findViewById(R.id.title);
        TextView msgBox = findViewById(R.id.msgbox);

        prompt.setText(title);
        //msgBox.setText(msg);

        Button buttonManageUsers = (Button) findViewById(R.id.ManageUsersButton);
        Button buttonManageCategories = (Button) findViewById(R.id.ManageCatagoriesButton);
        Button buttonManageItems = (Button) findViewById(R.id.ManageItemsButton);
        Button buttonRentItems = (Button) findViewById(R.id.RentItemsButton);



        if (ul.isAdmin()){
            buttonManageUsers.setVisibility(View.VISIBLE);
            buttonManageCategories.setVisibility(View.VISIBLE);
            buttonManageItems.setVisibility(View.INVISIBLE);
            buttonRentItems.setVisibility(View.INVISIBLE);

            buttonManageUsers.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    goToUsers();
                }
            });

            buttonManageCategories.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    goToCategories();
                }
            });
        }
        else if (ul.isLessor()){
            buttonManageUsers.setVisibility(View.INVISIBLE);
            buttonManageCategories.setVisibility(View.INVISIBLE);
            buttonManageItems.setVisibility(View.VISIBLE);
            buttonRentItems.setVisibility(View.INVISIBLE);

            buttonManageItems.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    goToLessor();
                }
            });

        }
        else if (ul.isRenter()){
            buttonManageUsers.setVisibility(View.INVISIBLE);
            buttonManageCategories.setVisibility(View.INVISIBLE);
            buttonManageItems.setVisibility(View.INVISIBLE);
            buttonRentItems.setVisibility(View.VISIBLE);

            buttonRentItems.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    goToRenter();
                }
            });

        }

        else {
            buttonManageUsers.setVisibility(View.INVISIBLE);
            buttonManageCategories.setVisibility(View.INVISIBLE);
        }

        Button logoutButton = findViewById(R.id.logout);

        // Set an OnClickListener to close the activity when the button is clicked
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close the current activity
                finish();
            }
        });

    }
    private void goToLessor() {
        Intent intent = new Intent(this, LessorItemActivity.class);
        postLauncher.launch(intent);
    }

    private void goToRenter() {
        Intent intent = new Intent(this, RenterItemActivity.class);
        postLauncher.launch(intent);
    }


    private void goToUsers() {
        Intent intent = new Intent(this, AdminUserActivity.class);
        postLauncher.launch(intent);
    }

    private void goToCategories() {
        Intent intent = new Intent(this, AdminCategoryActivity.class);
        postLauncher.launch(intent);
    }

}