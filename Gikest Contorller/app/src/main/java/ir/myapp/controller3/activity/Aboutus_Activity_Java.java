package ir.myapp.controller3.activity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import ir.myapp.controller3.R;

public class Aboutus_Activity_Java extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aboutus_activity_xml);

        FloatingActionButton fab = findViewById(R.id.fab_about_us);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"mahdi.farrokhzadeh@iran.ir"});
                i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(Aboutus_Activity_Java.this, getString(R.string.EmailError), Toast.LENGTH_SHORT).show();
                }
            }
        });

        ImageView icon = findViewById(R.id.about_us_icon);
        icon.setOnClickListener(new View.OnClickListener() {
            int counter = 0;
            @Override
            public void onClick(View view) {
                counter += 1;
                if (counter >= 3){
                    Toast.makeText(getApplicationContext(), "Adding template data to humidity history", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}