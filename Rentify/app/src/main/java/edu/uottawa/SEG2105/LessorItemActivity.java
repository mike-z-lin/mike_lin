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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import android.widget.ArrayAdapter;

import edu.uottawa.SEG2105.data.model.Category;
import edu.uottawa.SEG2105.data.model.Item;
import edu.uottawa.SEG2105.data.model.ItemList;
import edu.uottawa.SEG2105.data.model.UserManager;

public class LessorItemActivity extends AppCompatActivity {
    Spinner spinnerItemCategory;
    String editItemCategory;
    EditText editItemName;
    EditText editItemDesc;
    EditText editItemFee;
    EditText editItemBegin;
    EditText editItemEnd;
    Button buttonAddItem;
    ListView listViewItems;
    String currentUser;
    List<Item> items;
    DatabaseReference dbItems;

    private static final java.text.SimpleDateFormat sdf =  new java.text.SimpleDateFormat("yyyyMMdd");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lessor_item);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        spinnerItemCategory = (Spinner) findViewById(R.id.editItemCategory);
        editItemName = (EditText) findViewById(R.id.editItemName);
        editItemDesc = (EditText) findViewById(R.id.editItemDesc);
        editItemFee = (EditText) findViewById(R.id.editItemFee);
        editItemBegin = (EditText) findViewById(R.id.editItemBegin);
        editItemEnd = (EditText) findViewById(R.id.editItemEnd);
        listViewItems = (ListView) findViewById(R.id.listViewItems);
        buttonAddItem = (Button) findViewById(R.id.addItemButton);


        editItemName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() < 2)
                    editItemName.setError("description should be more than 3 chars.");
            }
        });
        editItemDesc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() < 4)
                    editItemDesc.setError("description should be more than 3 chars.");
            }
        });
        editItemFee.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                try {
                    Double.parseDouble(s.toString());

                } catch(NumberFormatException e){
                    editItemFee.setError("should be all digits.");
                }
            }
        });
        editItemBegin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if (s != null) {
                    try {
                        java.util.Date ret = sdf.parse(s.toString().trim());
                        if (sdf.format(ret).equals(s.toString().trim())) {
                            return;
                        }
                        else
                            editItemBegin.setError("should be all date in the format of yyyymmdd.");
                    } catch (ParseException e) {
                        editItemBegin.setError("date in the format of yyyymmdd.");
                    }
                }
            }
        });
        editItemEnd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if (s != null) {
                    try {
                        java.util.Date ret = sdf.parse(s.toString().trim());
                        if (sdf.format(ret).equals(s.toString().trim())) {
                            return;
                        }
                        else
                            editItemBegin.setError("should be all date in the format of yyyymmdd.");
                    } catch (ParseException e) {
                        editItemBegin.setError("date in the format of yyyymmdd.");
                    }
                }
            }
        });


        currentUser = new UserManager().currentUser;
        List<String> categories = new ArrayList<>();;
        DatabaseReference dbCategories;
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

                    categories.add(cg._name);  // Add each user to the list
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(LessorItemActivity.this, android.R.layout.simple_spinner_dropdown_item, categories);

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

        buttonAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItem();
            }
        });

        listViewItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Item cg = items.get(i);
                showUpdateDeleteDialog(cg._id, cg);
                return true;
            }
        });

        dbItems = FirebaseDatabase.getInstance().getReference("items");
        // Attach a listener to read the data at our posts reference
        dbItems.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                items.clear();
                // Iterate over each child node under "items"
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    // Convert each child node to a User object
                    Item cg = ds.getValue(Item.class);
                    if (cg._Lessor.equals(currentUser.toString()))
                        items.add(cg);  // Add each user to the list
                }

                ItemList itemsAdapter = new ItemList(LessorItemActivity.this, items);
                listViewItems.setAdapter(itemsAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() < 2)
                    editItemName.setError("name should be more than 2 chars.");
            }
        };

        editItemName.addTextChangedListener(afterTextChangedListener);

        TextWatcher descListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() < 2)
                    editItemDesc.setError("description should be more than 3 chars.");
            }
        };

        editItemDesc.addTextChangedListener(descListener);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    private void showUpdateDeleteDialog(final String itemId, Item item) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_item_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextName = (EditText) dialogView.findViewById(R.id.editTextName);
        final TextView editTextCategory  = (TextView) dialogView.findViewById(R.id.editTextCategory);
        final EditText editTextDesc = (EditText) dialogView.findViewById(R.id.editTextDesc);
        final EditText editTextFee  = (EditText) dialogView.findViewById(R.id.editTextFee);
        final EditText editTextBegin = (EditText) dialogView.findViewById(R.id.editTextBegin);
        final EditText editTextEnd  = (EditText) dialogView.findViewById(R.id.editTextEnd);
        final TextView textRequest = (TextView) dialogView.findViewById(R.id.textRequest);
        final TextView textResponse  = (TextView) dialogView.findViewById(R.id.textResponse);

        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdateItem);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.buttonDeleteItem);
        final Button buttonAccept = (Button) dialogView.findViewById(R.id.buttonAcceptItem);
        final Button buttonDeny   = (Button) dialogView.findViewById(R.id.buttonDenyItem);

        editTextName.setText(item._name);
        editTextCategory.setText(item._category);
        editTextDesc.setText(item._desc);
        editTextFee.setText(item._fee);
        editTextBegin.setText(item._begin);
        editTextEnd.setText(item._end);
        if (item._Renter.length() > 0 )
            textRequest.setText( "requested by: " + item._Renter);
        else {
            textRequest.setText("");
            buttonAccept.setEnabled(false);
            buttonDeny.setEnabled(false);

        }
        textResponse.setText(item._Response);



        dialogBuilder.setTitle(item._name);
        final AlertDialog b = dialogBuilder.create();
        b.show();

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String category = editTextCategory.getText().toString().trim();
                String name = editTextName.getText().toString().trim();
                String desc = editTextDesc.getText().toString();
                String fee = editTextFee.getText().toString().trim();
                String begin = editTextBegin.getText().toString().trim();
                String end = editTextEnd.getText().toString().trim();
                if (!TextUtils.isEmpty(name)) {
                    updateItem(itemId, item, category, name, desc, fee, begin, end);
                    b.dismiss();
                }
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteItem(itemId);
                b.dismiss();
            }
        });

        buttonAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acceptItem(itemId, item);
                b.dismiss();
            }
        });

        buttonDeny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                denyItem(itemId, item);
                b.dismiss();
            }
        });

    }

    private void updateItem(String id, Item item, String category, String name, String desc, String fee, String begin, String end) {
        //ML's impplementatoin
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("items").child(id);
        Item cg = new Item(id, category, name, desc, fee, begin, end, currentUser);
        cg._Renter = item._Renter;
        cg._Response = item._Response;
        dR.setValue(cg);

        Toast.makeText(getApplicationContext(), "Item Updated", Toast.LENGTH_LONG).show();
    }

    private void deleteItem(String id) {
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("items").child(id);
        dR.removeValue();
        Toast.makeText(getApplicationContext(), "Item Deleted", Toast.LENGTH_LONG).show();
    }

    private void addItem() {
        String category = editItemCategory.trim();
        String name = editItemName.getText().toString().trim();
        String desc = editItemDesc.getText().toString();
        String fee = editItemFee.getText().toString().trim();
        String begin = editItemBegin.getText().toString().trim();
        String end = editItemEnd.getText().toString().trim();

        if (!TextUtils.isEmpty(name)) {
            String id = dbItems.push().getKey();
            Item cg = new Item(id, category, name, desc, fee, begin, end, currentUser);

            dbItems.child(id).setValue(cg);
            //editItemCategory.setText("");
            editItemName.setText("");
            editItemDesc.setText("");
            editItemFee.setText("");
            editItemBegin.setText("");
            editItemEnd.setText("");

            Toast.makeText(getApplicationContext(), "Item added", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Please enter a name", Toast.LENGTH_LONG).show();
        }

    }

    private void acceptItem(String id,Item cg) {
        DatabaseReference di = FirebaseDatabase.getInstance().getReference("items").child(id);
        cg._Response = "ACCEPTED";
        di.setValue(cg);

        Toast.makeText(getApplicationContext(), "Item Rented", Toast.LENGTH_LONG).show();
    }

    private void denyItem(String id,Item cg) {
        DatabaseReference di = FirebaseDatabase.getInstance().getReference("items").child(id);
        cg._Response = "DENIED";;
        di.setValue(cg);

        Toast.makeText(getApplicationContext(), "Item Rented", Toast.LENGTH_LONG).show();
    }

}