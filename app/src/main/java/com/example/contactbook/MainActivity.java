package com.example.contactbook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.contactbook.data.dbHandler;
import com.example.contactbook.model.Contact;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SelectListener {

    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private ArrayList<Contact> contactsArrayList;
    private SearchView searchContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        //creating database
        dbHandler db = new dbHandler(MainActivity.this);

        //search view
        searchContact = findViewById(R.id.searchContact);
        searchContact.clearFocus();
        searchContact.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });

        //FAB intant
        FloatingActionButton addContactFAB = findViewById(R.id.addContactFAB);
        addContactFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AddNewContactPage.class);
                startActivity(i);
            }
        });

        //geting all contacts from db
        contactsArrayList = new ArrayList<>();
        contactsArrayList = db.getAllContacts();

        TextView homeScreenText = findViewById(R.id.homeScreenText);
        if(contactsArrayList.isEmpty()) {
            homeScreenText.setVisibility(View.VISIBLE);
        }

        //recycler view
        recyclerView = findViewById(R.id.contactlist);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerViewAdapter = new RecyclerViewAdapter(MainActivity.this, contactsArrayList, this);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    //search items
    private void filterList(String text) {
        ArrayList<Contact> filterList = new ArrayList<>();
        for(Contact contact : contactsArrayList) {
            if(contact.getName().toLowerCase().contains(text.toLowerCase())) {
                filterList.add(contact);
            }
        }

        if(filterList.isEmpty()) {
            Toast.makeText(this, "No Contact Found", Toast.LENGTH_SHORT).show();
        } else {
            recyclerViewAdapter.setFilterdList(filterList);
        }
    }

    @Override
    public void onItemClicked(Contact contact) {
        Intent contactDetail = new Intent(MainActivity.this, ContactDetails.class);
//        contactDetail.putExtra("personID", String.valueOf(contact.getId()));
//        contactDetail.putExtra("personName", contact.getName());
//        contactDetail.putExtra("personNumber", contact.getNumber());
        contactDetail.putExtra("contactProfile", BitmapFunctions.getBytes(contact.getImage()));
        contactDetail.putExtra("contactObj", contact);
        startActivity(contactDetail);
    }
}