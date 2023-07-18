package com.example.contactbook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.contactbook.data.dbHandler;
import com.example.contactbook.model.Contact;

public class ContactDetails extends AppCompatActivity {

    private final int CALL_PERMISSON_CODE = 1;
    private final int MESSAGE_PERMISSON_CODE = 2;
    private String phoneNmber;
    TextView txtPersonName, txtcall;
    ImageView toolbarBackButton, iconCall, iconMessage, toolbarEditButton, toolbarDeleteButton, profile_image;
    TextView toolbarpageName;
    LinearLayout layoutCall, layoutMesssage;
    RecyclerViewAdapter recyclerViewAdapter;
    Contact contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_contact_details);

        //database object
        dbHandler db = new dbHandler(ContactDetails.this);
        //contact obj
        contact = (Contact) getIntent().getSerializableExtra("contactObj");

        toolbarpageName = findViewById(R.id.toolbarPageName);
        toolbarpageName.setText("Contact info");

        txtPersonName = findViewById(R.id.txtPersonName);
        txtPersonName.setText(contact.getName());
        phoneNmber = contact.getNumber();

        //back button
        toolbarBackButton = findViewById(R.id.toolbarBackButton);
        toolbarBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbarBackButton.startAnimation(AnimationUtils.loadAnimation(toolbarBackButton.getContext(), R.anim.call_button));
                finish();
            }
        });

        //profile image
        profile_image = findViewById(R.id.profile_image);
        contact.setImage(BitmapFunctions.getImage(getIntent().getByteArrayExtra("contactProfile")));
        profile_image.setImageBitmap(contact.getImage());

        //call button
        layoutCall = findViewById(R.id.layoutCall);
        layoutCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutCall.startAnimation(AnimationUtils.loadAnimation(layoutCall.getContext(), R.anim.call_button));
                checkCallPermissionAndMakeCall();
            }
        });

        //message button
        layoutMesssage = findViewById(R.id.layoutMessage);
        layoutMesssage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutMesssage.startAnimation(AnimationUtils.loadAnimation(layoutMesssage.getContext(), R.anim.call_button));
                checkCallPermissionAndMakeCall();
            }
        });

        //alert dialog
        Dialog alertDialog = new Dialog(this);
        alertDialog.setContentView(R.layout.alert_dialog);
//        alertDialog.setCancelable(false);

        //Delete button
        toolbarDeleteButton = findViewById(R.id.toolbarDeleteButton);
        toolbarDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbarDeleteButton.startAnimation(AnimationUtils.loadAnimation(toolbarDeleteButton.getContext(), R.anim.call_button));

                Button alertDeleteButton = alertDialog.findViewById(R.id.alertDeleteButton);
                alertDeleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        db.deleteContact(contact);
                        recyclerViewAdapter.notifyDataSetChanged();
                        Toast.makeText(ContactDetails.this, contact.getName() + "Contact Deleted", Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                        finish();
                    }
                });

                TextView alertCancelButton = alertDialog.findViewById(R.id.alertCancelButton);
                alertCancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertCancelButton.startAnimation(AnimationUtils.loadAnimation(alertCancelButton.getContext(), R.anim.call_button));
                        alertDialog.dismiss();
                    }
                });

                alertDialog.show();
            }
        });

        //edit button
        toolbarEditButton = findViewById(R.id.toolbarEditButton);
        toolbarEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbarEditButton.startAnimation(AnimationUtils.loadAnimation(toolbarEditButton.getContext(), R.anim.call_button));
                Intent updateContact = new Intent(ContactDetails.this, updateContact.class);
                updateContact.putExtra("updateContactProfile",BitmapFunctions.getBytes(contact.getImage()));
                updateContact.putExtra("updateContactObj", contact);
                finish();
                startActivity(updateContact);
            }
        });
    }

    private void makePhoneCall() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phoneNmber));
        startActivity(callIntent);
    }

    private void checkCallPermissionAndMakeCall() {
        if (ActivityCompat.checkSelfPermission(ContactDetails.this,
                android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ContactDetails.this,
                    new String[]{Manifest.permission.CALL_PHONE}, CALL_PERMISSON_CODE);
        } else {
            makePhoneCall();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == CALL_PERMISSON_CODE) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                makePhoneCall();
            } else {
                Toast.makeText(this, "Phone Call Permission Not Granted.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}