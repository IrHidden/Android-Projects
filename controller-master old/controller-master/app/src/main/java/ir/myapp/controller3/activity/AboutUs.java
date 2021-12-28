package ir.myapp.controller3.activity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import ir.myapp.controller3.R;
import ir.myapp.controller3.comunication.Information;
import ir.myapp.controller3.comunication.SMSInformation;
import ir.myapp.controller3.db.AppDatabase;
import ir.myapp.controller3.entity.HumidityHistory;

public class AboutUs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_us);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.aboutus);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        FloatingActionButton fab = findViewById(R.id.fab_about_us);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"tagizaade@gmail.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, "برنامه پایش گلخانه");
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(AboutUs.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
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
                    addTempData();
                }
            }
        });



    }

    public void addTempData(){
        getApplicationContext().deleteDatabase(getString(R.string.app_name));
        long start = new Date().getTime() - 24*60*60*1000+100000;
        AppDatabase db = AppDatabase.getInstance(getApplicationContext());
        List<HumidityHistory> humidityHistories = new ArrayList<>();
        int count = new SMSInformation(getApplicationContext(), Information.PreferenceNames.Backup).getSensorsCount();
        for (int j = 0; j < count; j++) {
            for (int i = 0; i < 24; i++) {
                if (i == 15){
                    continue;
                }
                HumidityHistory h = new HumidityHistory(j, 20 + new Random().nextInt(5) , start + i * 60 * 60 * 1000);
                humidityHistories.add(h);
            }
            db.humidityHistoryDao().insertAll(humidityHistories);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}