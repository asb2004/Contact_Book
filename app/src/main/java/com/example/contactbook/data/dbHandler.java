package com.example.contactbook.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.contactbook.BitmapFunctions;
import com.example.contactbook.MainActivity;
import com.example.contactbook.model.Contact;
import com.example.contactbook.params.Params;

import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;

public class dbHandler extends SQLiteOpenHelper {
    public dbHandler(Context context) {
        super(context, Params.DB_NAME, null, Params.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create = "CREATE TABLE " + Params.TABLE_NAME + "(" + Params.KEY_ID +
                " INTEGER PRIMARY KEY," + Params.KEY_NAME + " TEXT," + Params.KEY_NUMBER + " TEXT," + Params.KEY_IMAGE + " BLOB);";
        db.execSQL(create);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Params.KEY_NAME, contact.getName());
        values.put(Params.KEY_NUMBER, contact.getNumber());
        //convert imagebitmap to bytes
        byte[] image = BitmapFunctions.getBytes(contact.getImage());
        values.put(Params.KEY_IMAGE, image);
        db.insert(Params.TABLE_NAME, null, values);
        db.close();
    }

    public ArrayList<Contact> getAllContacts() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Contact> contactList = new ArrayList<>();

        String select = "SELECT * FROM " + Params.TABLE_NAME + " ORDER BY " + Params.KEY_NAME;
        Cursor cursor = db.rawQuery(select, null);

        if(cursor.moveToFirst()) {
            do{
                Contact contact = new Contact();
                contact.setId(Integer.parseInt(cursor.getString(0)));
                contact.setName(cursor.getString(1));
                contact.setNumber(cursor.getString(2));
                contact.setImage(BitmapFunctions.getImage(cursor.getBlob(3)));
                contactList.add(contact);
            } while (cursor.moveToNext());
        }
        db.close();
        return contactList;
    }

    public void updateContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Params.KEY_NAME, contact.getName());
        values.put(Params.KEY_NUMBER, contact.getNumber());
        //convert imagebitmap to bytes
        byte[] image = BitmapFunctions.getBytes(contact.getImage());
        values.put(Params.KEY_IMAGE, image);

        db.update(Params.TABLE_NAME, values, Params.KEY_ID + "=?", new String[]{String.valueOf(contact.getId())});
        db.close();
    }

    public void deleteContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Params.TABLE_NAME, Params.KEY_ID + "=?", new String[]{String.valueOf(contact.getId())});
        db.close();
    }
}
