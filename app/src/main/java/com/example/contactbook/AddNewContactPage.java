package com.example.contactbook;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.contactbook.data.dbHandler;
import com.example.contactbook.model.Contact;

public class AddNewContactPage extends AppCompatActivity {

    private EditText inputName;
    private EditText inputNumber;
    private Button btnsave;
    private RecyclerViewAdapter recyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_contact_page);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add New Contact");

        dbHandler db = new dbHandler(this);

        inputName = findViewById(R.id.inputName);
        inputNumber = findViewById(R.id.inputNumber);
        btnsave = findViewById(R.id.btnsave);

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = inputName.getText().toString();
                String number = inputNumber.getText().toString();

                Contact contact = new Contact(name, number);
                db.addContact(contact);
                
                recyclerViewAdapter.notifyDataSetChanged();
                Toast.makeText(AddNewContactPage.this, "Contact Saved!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}