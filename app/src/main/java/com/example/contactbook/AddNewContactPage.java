package com.example.contactbook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;

import com.example.contactbook.data.dbHandler;
import com.example.contactbook.model.Contact;

import java.io.IOException;
import java.net.URI;

public class AddNewContactPage extends AppCompatActivity {

    private final int IMAGE_PICK_CODE = 10;
    private EditText inputName, inputNumber;
    private Button btnsave;
    private RecyclerViewAdapter recyclerViewAdapter;
    private ImageView imagePicker, imagePickerIcon, addContactToolbarBackButton;
    private Bitmap image;
    private String name, number;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_add_new_contact_page);

        dbHandler db = new dbHandler(this);

        inputName = findViewById(R.id.inputName);
        inputNumber = findViewById(R.id.inputNumber);
        btnsave = findViewById(R.id.btnsave);

        addContactToolbarBackButton = findViewById(R.id.addContactToolbarBackButton);
        addContactToolbarBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addContactToolbarBackButton.startAnimation(AnimationUtils.loadAnimation(addContactToolbarBackButton.getContext(), R.anim.call_button));
                finish();
            }
        });

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = inputName.getText().toString();
                number = inputNumber.getText().toString();
                if(name.isEmpty() || number.isEmpty()) {
                    Toast.makeText(AddNewContactPage.this, "Please Enter Values!", Toast.LENGTH_SHORT).show();
                } else {
                    if (image == null) {
                        image = BitmapFactory.decodeResource(getResources(), R.drawable.contact_logo);
                    }
                    Contact contact = new Contact(name, number, image);
                    db.addContact(contact);

                    recyclerViewAdapter.notifyDataSetChanged();
                    Toast.makeText(AddNewContactPage.this, "Contact Saved!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

        //image picker
        imagePicker = findViewById(R.id.image_picker);
        imagePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkStoragePermissionAndPickPhoto();
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
                imagePicker.setImageBitmap(image);
                imagePickerIcon = findViewById(R.id.imagePickerIcon);
                imagePickerIcon.setVisibility(View.INVISIBLE);
//                imagePicker.setImageURI(data.getData());
            }
        }
    }

    //pick photo from galary
    private void pickPhoto() {
        Intent imagePick = new Intent(Intent.ACTION_PICK);
        imagePick.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(imagePick, IMAGE_PICK_CODE);
    }

    //check permission for storage
    private void checkStoragePermissionAndPickPhoto() {
        if((ActivityCompat.checkSelfPermission(AddNewContactPage.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) &&
                (ActivityCompat.checkSelfPermission(AddNewContactPage.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(AddNewContactPage.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},IMAGE_PICK_CODE);
        } else {
            pickPhoto();
        }
    }
    
    //permission result

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == IMAGE_PICK_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickPhoto();
            } else {
                Toast.makeText(this, "You can't Pick images", Toast.LENGTH_SHORT).show();
            }
        }
    }
}