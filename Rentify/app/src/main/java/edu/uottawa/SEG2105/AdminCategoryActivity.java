package edu.uottawa.SEG2105;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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

import edu.uottawa.SEG2105.data.model.Category;
import edu.uottawa.SEG2105.data.model.CategoryList;

public class AdminCategoryActivity extends AppCompatActivity {
    EditText editCategoryName;
    EditText editCategoryDesc;
    Button buttonAddCategory;
    ListView listViewCategories;

    List<Category> categories;
    DatabaseReference dbCategories;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_category);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editCategoryName = (EditText) findViewById(R.id.editCategoryName);
        editCategoryDesc = (EditText) findViewById(R.id.editCategoryDesc);
        listViewCategories = (ListView) findViewById(R.id.listViewCategories);
        buttonAddCategory = (Button) findViewById(R.id.addCategoryButton);

        categories = new ArrayList<>();

        //adding an onclicklistener to button
        buttonAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCategory();
            }
        });

        listViewCategories.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Category cg = categories.get(i);
                showUpdateDeleteDialog(cg._id, cg._name);
                return true;
            }
        });

        dbCategories = FirebaseDatabase.getInstance().getReference("categories");
        // Attach a listener to read the data at our posts reference
        dbCategories.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                categories.clear();
                // Iterate over each child node under "categories"
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    // Convert each child node to a User object
                    Category cg = ds.getValue(Category.class);

                    categories.add(cg);  // Add each user to the list
                }

                CategoryList categoriesAdapter = new CategoryList(AdminCategoryActivity.this, categories);
                listViewCategories.setAdapter(categoriesAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });


        editCategoryName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() < 2)
                    editCategoryName.setError("name should be more than 2 chars.");
            }
        });

        TextWatcher descListener = new TextWatcher() {
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
                if (s.toString().length() < 2)
                    editCategoryDesc.setError("description should be more than 3 chars.");
            }
        };

        editCategoryDesc.addTextChangedListener(descListener);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    private void showUpdateDeleteDialog(final String categoryId, String categoryName) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_category_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextName = (EditText) dialogView.findViewById(R.id.editTextName);
        final EditText editTextPrice  = (EditText) dialogView.findViewById(R.id.editTextDesc);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdateCategory);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.buttonDeleteCategory);

        dialogBuilder.setTitle(categoryName);
        final AlertDialog b = dialogBuilder.create();
        b.show();

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editCategoryName.getText().toString().trim();
                String desc = editCategoryDesc.getText().toString();
                if (!TextUtils.isEmpty(name)) {
                    updateCategory(categoryId, name, desc);
                    b.dismiss();
                }
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteCategory(categoryId);
                b.dismiss();
            }
        });
    }

    private void updateCategory(String id, String name, String desc) {
        //ML's impplementatoin
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("categories").child(id);
        Category cg = new Category(id, name, desc);
        dR.setValue(cg);

        Toast.makeText(getApplicationContext(), "Category Updated", Toast.LENGTH_LONG).show();
    }

    private void deleteCategory(String id) {
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("categories").child(id);
        dR.removeValue();
        Toast.makeText(getApplicationContext(), "Category Deleted", Toast.LENGTH_LONG).show();
    }

    private void addCategory() {
        //ML's impplementatoin
        String name = editCategoryName.getText().toString().trim();
        String desc = editCategoryDesc.getText().toString().trim();

        if (!TextUtils.isEmpty(name)) {
            String id = dbCategories.push().getKey();
            Category cg = new Category(id, name, desc);

            dbCategories.child(id).setValue(cg);
            editCategoryName.setText("");
            editCategoryDesc.setText("");

            Toast.makeText(getApplicationContext(), "Category added", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Please enter a name", Toast.LENGTH_LONG).show();
        }

    }

}