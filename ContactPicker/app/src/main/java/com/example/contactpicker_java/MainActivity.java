package com.example.contactpicker_java;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    ImageView image;
    TextView nametxt, detailstxt;
    Button pick;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        image =findViewById(R.id.contact_img);
        nametxt =findViewById(R.id.name_txt);
        detailstxt =findViewById(R.id.number_txt);
    pick =findViewById(R.id.pick_btn);

        RequestPermission();

        pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent,2);
            }
        });

    }
    void RequestPermission()
    {
        String [] permission={Manifest.permission.READ_CONTACTS};
        ActivityCompat.requestPermissions(this,permission, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        nametxt.setText("");
        detailstxt.setText("");

        Cursor cursor1,cursor2;

        cursor1=getContentResolver().query(data.getData(),null,null,null,null);

        if(cursor1.moveToFirst())
        {
            String contactId=cursor1.getString(
                    cursor1.getColumnIndex(
                            ContactsContract.Contacts._ID));

            String contactName=cursor1.getString(
                    cursor1.getColumnIndex(
                            ContactsContract.Contacts.DISPLAY_NAME));

            String contactImg=cursor1.getString(
                    cursor1.getColumnIndex(
                            ContactsContract.Contacts.PHOTO_THUMBNAIL_URI));

            int idResult=Integer.parseInt(
                    cursor1.getString(
                            cursor1.getColumnIndex(
                                    ContactsContract.Contacts.HAS_PHONE_NUMBER)));

            nametxt.setText(contactName);

            if(contactImg!=null)
                image.setImageURI(
                        Uri.parse(contactImg));

            else
                image.setImageURI(null);

            if(idResult==1)
            {
                cursor2=getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID+" = "+contactId,
                        null,
                        null);
                while(cursor2.moveToNext())
                {
                    String contactNumber=cursor2.getString(
                            cursor2.getColumnIndex(
                                    ContactsContract.CommonDataKinds.Phone.NUMBER));
                    detailstxt.append(contactNumber+"\n");
                }
                cursor2.close();

            }
            cursor1.close();
        }
    }
}