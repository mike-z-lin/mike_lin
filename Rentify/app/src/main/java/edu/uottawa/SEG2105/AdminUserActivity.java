package edu.uottawa.SEG2105;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import edu.uottawa.SEG2105.data.model.User;
import edu.uottawa.SEG2105.data.model.UserList;

public class AdminUserActivity extends AppCompatActivity {
    ListView listViewUsers;

    List<User> users;
    DatabaseReference dbUsers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_user);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        listViewUsers = (ListView) findViewById(R.id.listViewUsers);

        users = new ArrayList<>();

        listViewUsers.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                User cg = users.get(i);
                showUpdateDeleteDialog(cg.id, cg.name);
                return true;
            }
        });

        dbUsers = FirebaseDatabase.getInstance().getReference("accounts");
        // Attach a listener to read the data at our posts reference
        dbUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                users.clear();;
                // Iterate over each child node under "users"
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    // Convert each child node to a User object
                    User cg = ds.getValue(User.class);

                    users.add(cg);  // Add each user to the list
                }

                UserList usersAdapter = new UserList(AdminUserActivity.this, users);
                listViewUsers.setAdapter(usersAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    private void showUpdateDeleteDialog(final String userId, String userName) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_user_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextName = (EditText) dialogView.findViewById(R.id.editTextName);
        final EditText editTextPrice  = (EditText) dialogView.findViewById(R.id.editTextDesc);
        final Button buttonDisable = (Button) dialogView.findViewById(R.id.buttonDisableUser);
        final Button buttonEnable = (Button) dialogView.findViewById(R.id.buttonEnableUser);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.buttonDeleteUser);

        dialogBuilder.setTitle(userName);
        final AlertDialog b = dialogBuilder.create();
        b.show();

        buttonDisable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableUser(userId);
                b.dismiss();
            }
        });

        buttonEnable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enableUser(userId);
                b.dismiss();
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteUser(userId);
                b.dismiss();
            }
        });
    }

    private void disableUser(String id) {
        //ML's impplementatoin
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("accounts").child(id);
        for (User u: users) {
            if (u.id.equals(id)) {
                u.disabled = true;
                dR.setValue(u);
            }
        }

        Toast.makeText(getApplicationContext(), "User Updated", Toast.LENGTH_LONG).show();
    }

    private void enableUser(String id) {
        //ML's impplementatoin
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("accounts").child(id);
        for (User u: users) {
            if (u.id.equals(id)) {
                u.disabled = false;
                dR.setValue(u);
            }
        }

        Toast.makeText(getApplicationContext(), "User Updated", Toast.LENGTH_LONG).show();
    }

    private void deleteUser(String id) {
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("accounts").child(id);
        dR.removeValue();
        Toast.makeText(getApplicationContext(), "User Deleted", Toast.LENGTH_LONG).show();
    }

}