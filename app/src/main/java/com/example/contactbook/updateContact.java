package com.example.contactbook;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.contactbook.data.dbHandler;
import com.example.contactbook.model.Contact;

import java.io.IOException;

public class updateContact extends AppCompatActivity {

    private final int IMAGE_PICK_CODE = 10;
    private Bitmap image;
    RecyclerViewAdapter recyclerViewAdapter;
    ImageView updateContactToolbarBackButton, update_image_picker;
    Button update;
    EditText update_inputNumber, update_inputName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_update_contact);

        //database object
        dbHandler db = new dbHandler(updateContact.this);
        //contact obj
        Contact contact = (Contact) getIntent().getSerializableExtra("updateContactObj");

        //profile image
        update_image_picker = findViewById(R.id.update_image_picker);
        contact.setImage(BitmapFunctions.getImage(getIntent().getByteArrayExtra("updateContactProfile")));
        image = contact.getImage();
        update_image_picker.setImageBitmap(contact.getImage());

        update_image_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent imagePick = new Intent(Intent.ACTION_PICK);
                imagePick.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(imagePick, IMAGE_PICK_CODE);
            }
        });

        //set text data
        update_inputName = findViewById(R.id.update_inputName);
        update_inputName.setText(contact.getName());

        update_inputNumber = findViewById(R.id.update_inputNumber);
        update_inputNumber.setText(contact.getNumber());

        updateContactToolbarBackButton = findViewById(R.id.updateContactToolbarBackButton);
        updateContactToolbarBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateContactToolbarBackButton.startAnimation(AnimationUtils.loadAnimation(updateContactToolbarBackButton.getContext(), R.anim.call_button));
                finish();
            }
        });

        //update contact
        update = findViewById(R.id.btnupdate);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = update_inputName.getText().toString();
                String number = update_inputNumber.getText().toString();
                if(name.isEmpty() || number.isEmpty()) {
                    Toast.makeText(updateContact.this, "Please Enter Values!", Toast.LENGTH_SHORT).show();
                } else {
                    contact.setName(update_inputName.getText().toString());
                    contact.setNumber(update_inputNumber.getText().toString());
                    contact.setImage(image);
                    db.updateContact(contact);

                    recyclerViewAdapter.notifyDataSetChanged();
                    Toast.makeText(updateContact.this, "Contact Updated!", Toast.LENGTH_SHORT).show();
                    Intent home = new Intent(updateContact.this, MainActivity.class);
                    finish();
                    startActivity(home);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            if(requestCode == IMAGE_PICK_CODE) {
                Uri imageUri = data.getData();
                try {
                    image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                update_image_picker.setImageBitmap(image);
            }
        }
    }
}