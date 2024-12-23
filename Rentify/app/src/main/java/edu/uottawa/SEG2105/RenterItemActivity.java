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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import android.widget.ArrayAdapter;
import java.time.LocalDate;
import edu.uottawa.SEG2105.data.model.Category;
import edu.uottawa.SEG2105.data.model.Item;
import edu.uottawa.SEG2105.data.model.ItemList;
import edu.uottawa.SEG2105.data.model.UserManager;

public class RenterItemActivity extends AppCompatActivity {
    Spinner spinnerItemCategory;
    String editItemCategory;
    EditText editItemName;

    Button buttonSearchItem;
    ListView listViewItems;
    String currentUser;
    List<Item> items;
    DatabaseReference dbItems;
    ValueEventListener listener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_renter_item);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        spinnerItemCategory = (Spinner) findViewById(R.id.editItemCategory);
        editItemName = (EditText) findViewById(R.id.editItemName);
        listViewItems = (ListView) findViewById(R.id.listViewItems);
        buttonSearchItem = (Button) findViewById(R.id.searchItemButton);
        currentUser = new UserManager().currentUser;
        List<String> categories = new ArrayList<>();;
        DatabaseReference dbCategories;
        dbCategories = FirebaseDatabase.getInstance().getReference("categories");
        // Attach a listener to read the data at our posts reference
        dbCategories.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                categories.clear();
                categories.add("ALL");

                // Iterate over each child node under "categories"
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    // Convert each child node to a User object
                    Category cg = ds.getValue(Category.class);

                    categories.add(cg._name);  // Add each user to the list
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(RenterItemActivity.this, android.R.layout.simple_spinner_dropdown_item, categories);

                spinnerItemCategory.setAdapter(adapter);
                spinnerItemCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view,
                                               int position, long id) {
                        editItemCategory =  (String) parent.getItemAtPosition(position);
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // TODO Auto-generated method stub
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
        //Sleep(1);



        items = new ArrayList<>();

        buttonSearchItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // add dummy and remove to trigger onDataChange()
                String id = dbItems.push().getKey();
                Item cg = new Item(id, "category", "name", "desc", "fee", "begin", "end", "currentUser");

                dbItems.child(id).setValue(cg);

                dbItems.child(id).removeValue();


            }
        });

        listViewItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Item cg = items.get(i);
                showRentDialog(cg._id, cg);
                return true;
            }
        });

        dbItems = FirebaseDatabase.getInstance().getReference("items");
        // Attach a listener to read the data at our posts reference
        listener = dbItems.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                items.clear();
                // Iterate over each child node under "items"
                boolean filter = editItemCategory != null && !editItemCategory.equals("ALL");
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    // Convert each child node to a User object
                    Item cg = ds.getValue(Item.class);
                    if (filter){
                        if (editItemCategory.equals(cg._category) && cg._name.contains(editItemName.getText()))
                            items.add(cg);  // Add each user to the list
                    }else {
                        items.add(cg);
                    }
                }

                ItemList itemsAdapter = new ItemList(RenterItemActivity.this, items);
                listViewItems.setAdapter(itemsAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
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
                if (s.toString().length() < 2)
                    editItemName.setError("name should be more than 2 chars.");
            }
        };

        editItemName.addTextChangedListener(afterTextChangedListener);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    private void showRentDialog(final String itemId, Item item) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.rent_item_dialog, null);
        dialogBuilder.setView(dialogView);

        final TextView editTextName = (TextView) dialogView.findViewById(R.id.editTextName);
        final TextView editTextCategory  = (TextView) dialogView.findViewById(R.id.editTextCategory);
        final TextView editTextDesc = (TextView) dialogView.findViewById(R.id.editTextDesc);
        final TextView editTextFee  = (TextView) dialogView.findViewById(R.id.editTextFee);
        final EditText editTextBegin = (EditText) dialogView.findViewById(R.id.editTextBegin);
        final EditText editTextEnd  = (EditText) dialogView.findViewById(R.id.editTextEnd);
        final TextView textRequest = (TextView) dialogView.findViewById(R.id.textRequest);
        final TextView textResponse  = (TextView) dialogView.findViewById(R.id.textResponse);

        editTextName.setText(item._name);
        editTextCategory.setText(item._category);
        editTextDesc.setText(item._desc);
        editTextFee.setText(item._fee);
        editTextBegin.setText(item._begin);
        editTextEnd.setText(item._end);
        textRequest.setText( "requested by: " + item._Renter);
        textResponse.setText(item._Response);

        final Button buttonRent = (Button) dialogView.findViewById(R.id.buttonRentItem);

        LocalDate today = LocalDate.now();
        // Define the desired format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        // Format the date
        String formattedDate = today.format(formatter);
        int now = Integer.parseInt(formattedDate);
        int begin = Integer.parseInt(editTextBegin.getText().toString().trim());
        int end = Integer.parseInt(editTextEnd.getText().toString().trim());



        // Check if today falls within the range (inclusive)
        if (now <= end && textResponse.getText().toString().isEmpty()) {
            buttonRent.setEnabled(true);
        }
        else{
            buttonRent.setEnabled(false);
        }
        dialogBuilder.setTitle(item._name);
        final AlertDialog b = dialogBuilder.create();
        b.show();

        buttonRent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String begin = editTextBegin.getText().toString().trim();;
                String end = editTextEnd.getText().toString().trim();

                rentItem(itemId, item, currentUser + ":" + begin + "-" + end);
                b.dismiss();
            }
        });

    }

    private void rentItem(String id,Item cg, String rent) {
        //ML's impplementatoin
        DatabaseReference di = FirebaseDatabase.getInstance().getReference("items").child(id);
        cg._Renter = rent;
        cg._Response = "";
        di.setValue(cg);

        Toast.makeText(getApplicationContext(), "Item Rented", Toast.LENGTH_LONG).show();
    }


}