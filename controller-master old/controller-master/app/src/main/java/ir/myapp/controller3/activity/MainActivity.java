package ir.myapp.controller3.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import ir.myapp.controller3.R;
import ir.myapp.controller3.comunication.Information;
import ir.myapp.controller3.comunication.SMSInformation;
import ir.myapp.controller3.db.AppDatabase;
import ir.myapp.controller3.entity.HumidityHistory;
import ir.myapp.controller3.service.SMSService;
import ir.myapp.controller3.settings.Logger;
import ir.myapp.controller3.settings.Settings;
import ir.myapp.controller3.tools.Utils;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;



    Information information;
    Information backupInformation;
    Logger logger;

//    Switch onOffSwitchAirCondition ;
//    Switch onOffSwitchTemperature ;
//    Switch onOffSwitchLight;
//    Switch onOffSwitchHumidity;

    CardView dashboardTemperature;
    CardView dashboardHumidity;
    CardView dashboardLight ;
    CardView dashboardAirCondition;
//    CardView dashboardSensors;

//    TextView onOffTextTemperature ;
//    TextView onOffTextHumidity;
    TextView onOffTextLight;
//    TextView onOffTextAirCondition;

    TextView expectedValueTemperature;
    TextView expectedValueHumidity;

    TextView realValueTemperature;
    TextView realValueHumidity;

//    TextView valueLight ;
    TextView valueAirCondition ;
    TextView valueSensors;


    TextView nameTemperature ;
    TextView nameHumidity;
    TextView nameLight;
    TextView nameAirCondition;
//    TextView nameSensors;

    SwipeRefreshLayout swipeRefreshLayout;
    BroadcastReceiver refreshReceiver;
    boolean init = true;

    FloatingActionButton fab;

    Intent smsServiceIntent;

    TextView logsView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.main_menu);

        smsServiceIntent = new Intent(MainActivity.this, SMSService.class);
        if (!Utils.isMyServiceRunning(SMSService.class, this)) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                startForegroundService(smsServiceIntent);
            } else {
                startService(smsServiceIntent);
            }
        }

        checkPermissions();



        information = new SMSInformation(this, Information.PreferenceNames.New);
        backupInformation = new SMSInformation(this, Information.PreferenceNames.Backup);
        Utils.copySharedPreferences(backupInformation.getPreferences(), information.getPreferences());
        logger = new Logger(this);

        swipeRefreshLayout = findViewById(R.id.swiperefresh);

//        onOffSwitchAirCondition = findViewById(R.id.on_or_off_switch_air_conditioning);
//        onOffSwitchTemperature = findViewById(R.id.on_or_off_switch_temperature);
//        onOffSwitchLight = findViewById(R.id.on_or_off_switch_light);
//        onOffSwitchHumidity = findViewById(R.id.on_or_off_switch_humidity);

        dashboardTemperature = findViewById(R.id.dashboard_temperature);
        dashboardHumidity = findViewById(R.id.dashboard_humidity);
        dashboardLight = findViewById(R.id.dashboard_light);
        dashboardAirCondition = findViewById(R.id.dashboard_air_conditioning);
//        dashboardSensors = findViewById(R.id.dashboard_sensors);

//        onOffTextTemperature = findViewById(R.id.on_or_off_text_temperature);
//        onOffTextHumidity = findViewById(R.id.on_or_off_text_humidity);
        onOffTextLight = findViewById(R.id.on_or_off_text_light);
//        onOffTextAirCondition = findViewById(R.id.on_or_off_text_air_conditioning);

        expectedValueTemperature = findViewById(R.id.value_temperature_expected);
        expectedValueHumidity = findViewById(R.id.value_humidity_expected);
        realValueTemperature = findViewById(R.id.value_temperature_real);
        realValueHumidity = findViewById(R.id.value_humidity_real);
//        valueLight = findViewById(R.id.value_light);
        valueAirCondition = findViewById(R.id.value_air_conditioning);
        valueSensors = findViewById(R.id.sensors_count);


        nameTemperature = findViewById(R.id.property_name_temperature);
        nameHumidity = findViewById(R.id.property_name_humidity);
        nameLight = findViewById(R.id.property_name_light);
        nameAirCondition = findViewById(R.id.property_name_air_conditioning);
//        nameSensors = findViewById(R.id.property_name_sensors);

        fab = findViewById(R.id.fab_main);

        logsView = findViewById(R.id.log_txt);

        postInit();
    }




    public void checkPermissions(){

        if (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS,
                    Manifest.permission.READ_SMS,
                    Manifest.permission.RECEIVE_SMS,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_SEND_SMS);
        }

    }

    public void refreshTemperature(){
        int colorId = dashboardTemperature.getContext().getResources().getColor(R.color.dashboard_temperature);
        expectedValueTemperature.setText(information.getAVGHeatersExpectedTemp()+"°C");
        realValueTemperature.setText(information.getAVGSensorsTemperatures()+"°C");

//        boolean status = dashboardDetail.getDashTempStatus();
        boolean status = true;
        if (status){

            dashboardTemperature.setCardBackgroundColor(ColorStateList.valueOf(colorId));
//            onOffTextTemperature.setText(R.string.on);
//            onOffTextTemperature.setTextColor(Color.WHITE);
            expectedValueTemperature.setTextColor(Color.WHITE);
            realValueTemperature.setTextColor(Color.WHITE);
            nameTemperature.setTextColor(Color.WHITE);
        }else{
            dashboardTemperature.setCardBackgroundColor(ColorStateList.valueOf(Color.TRANSPARENT));
//            onOffTextTemperature.setText(R.string.off);
//            onOffTextTemperature.setTextColor(Color.LTGRAY);
            expectedValueTemperature.setTextColor(Color.GRAY);
            nameTemperature.setTextColor(Color.GRAY);
        }

    }

    public void refreshHumidity(){
        int colorId = getResources().getColor(R.color.dashboard_humidity);

        expectedValueHumidity.setText(information.getAVGVapoursExpectedHumidity()+"%");
        realValueHumidity.setText(information.getAVGSensorsHumidity()+"%");


//        boolean status = dashboardDetail.getDashHumidityStatus();

        boolean status = true;
        if (status) {
            dashboardHumidity.setCardBackgroundColor(ColorStateList.valueOf(colorId));
//            onOffTextHumidity.setText(R.string.on);
//            onOffTextHumidity.setTextColor(Color.WHITE);
            expectedValueHumidity.setTextColor(Color.WHITE);
            realValueHumidity.setTextColor(Color.WHITE);
            nameHumidity.setTextColor(Color.WHITE);
        }else{
            dashboardHumidity.setCardBackgroundColor(ColorStateList.valueOf(Color.TRANSPARENT));
//            onOffTextHumidity.setText(R.string.off);
//            onOffTextHumidity.setTextColor(Color.LTGRAY);
            expectedValueHumidity.setTextColor(Color.GRAY);
            nameHumidity.setTextColor(Color.GRAY);
        }
    }

    public void refreshAC(){
        int colorId = getResources().getColor(R.color.dashboard_air_condition);

        List<String[]> values = information.getACOnOffTimes();
        Calendar rightNow = Calendar.getInstance();
        int currentHourIn24Format = rightNow.get(Calendar.HOUR_OF_DAY);
        int currentMinute = rightNow.get(Calendar.MINUTE);
        int totalMinutes = currentHourIn24Format * 60 + currentMinute;
        int index = 0;
        int counter = 0;
        for (String[] val: values){
            String h1 = val[0].split(":")[0];
            String m1 = val[0].split(":")[1];
            String h2 = val[1].split(":")[0];
            String m2 = val[1].split(":")[1];
            int minutes1 = Integer.parseInt(h1) * 60 + Integer.parseInt(m1);
            int minutes2 = Integer.parseInt(h2) * 60 + Integer.parseInt(m2);
            if (totalMinutes > minutes1 && minutes2 < totalMinutes) {
                index = counter;
            }
            counter += 1;
        }

        valueAirCondition.setText(values.get(index)[0] + "-" + values.get(index)[1]);

        // boolean status = dashboardDetail.getDashAirConditionStatus();
        boolean status = true;

        if (status) {
            dashboardAirCondition.setCardBackgroundColor(ColorStateList.valueOf(colorId));
//            onOffTextAirCondition.setText(R.string.on);
//            onOffTextAirCondition.setTextColor(Color.WHITE);
            valueAirCondition.setTextColor(Color.WHITE);
            nameAirCondition.setTextColor(Color.WHITE);
        }else{
            dashboardAirCondition.setCardBackgroundColor(ColorStateList.valueOf(Color.TRANSPARENT));
//            onOffTextAirCondition.setText(R.string.off);
//            onOffTextAirCondition.setTextColor(Color.LTGRAY);
            valueAirCondition.setTextColor(Color.GRAY);
            nameAirCondition.setTextColor(Color.GRAY);
        }
    }

    public void refreshLight(){
        int colorId = getResources().getColor(R.color.dashboard_light);
        boolean status = information.getLightOnOrOff();
        if (status) {
            dashboardLight.setCardBackgroundColor(ColorStateList.valueOf(colorId));
            onOffTextLight.setText(R.string.on);
            onOffTextLight.setTextColor(Color.WHITE);
//            valueLight.setTextColor(Color.WHITE);
            nameLight.setTextColor(Color.WHITE);
        }else{
            dashboardLight.setCardBackgroundColor(ColorStateList.valueOf(Color.WHITE));
            onOffTextLight.setText(R.string.off);
            onOffTextLight.setTextColor(Color.LTGRAY);
//            valueLight.setTextColor(Color.GRAY);
            nameLight.setTextColor(Color.GRAY);
        }
    }

    public void refreshLog(){
        logsView.setText("Logs:\n" + logger.getLogs());
        logsView.setBackgroundColor(Color.WHITE);
    }

    public void refreshSensors(){
        int colorId = getResources().getColor(R.color.dashboard_sensors);
        System.out.println("sensors count " + information.getSensorsCount());
        valueSensors.setText(String.valueOf(information.getSensorsCount()));
//        dashboardSensors.setCardBackgroundColor(ColorStateList.valueOf(colorId));
//        nameSensors.setTextColor(Color.WHITE);
        valueSensors.setTextColor(Color.WHITE);
    }
    String logs = "";

    public void checkChanges(){
        logs = "";
        int heatersCount = backupInformation.getHeatersOnOrOff().size();
        List<Boolean> prevHeatersOnOff = backupInformation.getHeatersOnOrOff();
        List<Boolean> currentHeatersOnOff = information.getHeatersOnOrOff();
        for (int i = 0; i < heatersCount; i++){
            if (prevHeatersOnOff.get(i) != currentHeatersOnOff.get(i)){
                String prevStatus = prevHeatersOnOff.get(i) ? getResources().getString(R.string.on) : getResources().getString(R.string.off);
                String currentStatus = currentHeatersOnOff.get(i) ? getResources().getString(R.string.on) : getResources().getString(R.string.off);
                String placeholder = getResources().getString(R.string.heaters_on_off_change) + "";
                logs += String.format(placeholder, (i+1), prevStatus, currentStatus) + "\n";
            }
        }
        List<Integer> prevHeatersValues = backupInformation.getExpectedHeatersTemps();
        List<Integer> currentHeatersValues = information.getExpectedHeatersTemps();
        for (int i = 0; i < heatersCount; i++){
            if (!prevHeatersValues.get(i).equals(currentHeatersValues.get(i))){
                String placeholder = getResources().getString(R.string.heaters_value_change) + "";
                logs += String.format(placeholder, (i+1), prevHeatersValues.get(i), currentHeatersValues.get(i)) + "\n";
            }
        }

        List<Boolean> prevVapoursOnOff = backupInformation.getVapoursOnOrOff();
        List<Boolean> currentVapoursOnOff = information.getVapoursOnOrOff();
        for (int i = 0; i < heatersCount; i++){
            if (prevVapoursOnOff.get(i) != currentVapoursOnOff.get(i)){
                String prevStatus = prevVapoursOnOff.get(i) ? getResources().getString(R.string.on) : getResources().getString(R.string.off);
                String currentStatus = currentVapoursOnOff.get(i) ? getResources().getString(R.string.on) : getResources().getString(R.string.off);
                String placeholder = getResources().getString(R.string.vapours_on_off_change) + "";
                logs += String.format(placeholder, (i+1), prevStatus, currentStatus) + "\n";
            }
        }
        List<Integer> prevVapoursValues = backupInformation.getExpectedVapoursHumidities();
        List<Integer> currentVapoursValues = information.getExpectedVapoursHumidities();
        for (int i = 0; i < heatersCount; i++){
            if (!prevVapoursValues.get(i).equals(currentVapoursValues.get(i))){
                String placeholder = getResources().getString(R.string.vapours_value_change) + "";
                logs += String.format(placeholder, (i+1), prevVapoursValues.get(i), currentVapoursValues.get(i)) + "\n";
            }
        }

        Boolean prevACOnOff = backupInformation.getACOnOrOff();
        Boolean currentACOnOff = information.getACOnOrOff();

        if (prevACOnOff != currentACOnOff){
            String prevStatus = prevACOnOff? getResources().getString(R.string.on) : getResources().getString(R.string.off);
            String currentStatus = currentACOnOff? getResources().getString(R.string.on) : getResources().getString(R.string.off);
            logs += getResources().getString(R.string.ac_status_change, prevStatus, currentStatus) + "\n";
        }

        String prevOnOffTimes = Utils.convertToCommaSeperatedArray(backupInformation.getACOnOffTimes());
        String currentOnOffTimes = Utils.convertToCommaSeperatedArray(information.getACOnOffTimes());

        if (!prevOnOffTimes.equals(currentOnOffTimes)){
            logs += "AC activation times where changed\n";
        }

        if (!logs.equals("")){
            fab.show();
        }else{
            fab.hide();
        }
    }

    public void openDialog() {
        final Dialog dialog = new Dialog(this); // Context, this, etc.
        dialog.setContentView(R.layout.confirm_changes_dialog);
        dialog.setTitle(R.string.changes);
        TextView tv = dialog.findViewById(R.id.changes_to_be_applied);
        tv.setText(logs);
        Button cancel = dialog.findViewById(R.id.discard_changes);
        Button ok = dialog.findViewById(R.id.apply_changes);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logs = "";
                SharedPreferences backup = backupInformation.getPreferences();
                SharedPreferences news = information.getPreferences();
                Utils.copySharedPreferences(backup, news);
                refresh();
                dialog.cancel();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sysPassword = new Settings(getApplicationContext()).getPassword();
                String password = ((EditText)dialog.findViewById(R.id.passwordy)).getText().toString();
                if (password.equals("")){
                    Toast.makeText(getApplicationContext(), "Pleased enter password", Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.d("Password in dialog ", password);
                if (!password.equals(sysPassword)){
                    Toast.makeText(getApplicationContext(), "Password wrong", Toast.LENGTH_SHORT).show();
                    return;
                }
                logs = "";
                SharedPreferences backup = backupInformation.getPreferences();
                SharedPreferences news = information.getPreferences();
                Utils.copySharedPreferences(news, backup);
                refresh();
                backupInformation.sendAll();
                dialog.cancel();

            }
        });
        dialog.show();
    }

    public void refreshPowerOff(){
        boolean status = information.getPowerOnOff();
        TextView myText = (TextView) findViewById(R.id.power_is_off);
        if (status){
            myText.setVisibility(View.GONE);
            myText.clearAnimation();
        }
        else{
            myText.setVisibility(View.VISIBLE);
            Animation anim = new AlphaAnimation(0.0f, 1.0f);
            anim.setDuration(1000); //You can manage the blinking time with this parameter
            anim.setStartOffset(20);
            anim.setRepeatMode(Animation.REVERSE);
            anim.setRepeatCount(Animation.INFINITE);
            myText.startAnimation(anim);
        }
    }

    public void refresh(){
        init = true;
        information = new SMSInformation(this, Information.PreferenceNames.New);

        refreshHumidity();
        refreshTemperature();
        refreshAC();
        refreshLight();
        refreshPowerOff();
//        refreshSensors();
        refreshLog();
        checkChanges();


        if(!information.getRefreshing()){
            swipeRefreshLayout.setRefreshing(false);
        }

        init = false;

    }

    public void postInit(){

        swipeRefreshLayout.setProgressViewOffset(false, 0, 100);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                information.refresh();
                information.setRefreshing(true);
                System.out.println("refreshing");
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });


        /*
        onOffSwitchTemperature.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int colorId = dashboardTemperature.getContext().getResources().getColor(R.color.dashboard_temperature);

                if (isChecked) {
                    dashboardTemperature.setCardBackgroundColor(ColorStateList.valueOf(colorId));
                    onOffTextTemperature.setText(R.string.on);
                    onOffTextTemperature.setTextColor(Color.WHITE);
                    valueTemperature.setTextColor(Color.WHITE);
                    nameTemperature.setTextColor(Color.WHITE);
                }else{
                    dashboardTemperature.setCardBackgroundColor(ColorStateList.valueOf(Color.TRANSPARENT));
                    onOffTextTemperature.setText(R.string.off);
                    onOffTextTemperature.setTextColor(Color.LTGRAY);
                    valueTemperature.setTextColor(Color.GRAY);
                    nameTemperature.setTextColor(Color.GRAY);
                }
                if (!init) {
                    dashboardDetail.setDashTempStatus(isChecked);
                }
            }
        });


        onOffSwitchLight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int colorId = dashboardLight.getContext().getResources().getColor(R.color.dashboard_light);

                if (isChecked) {
                    dashboardLight.setCardBackgroundColor(ColorStateList.valueOf(colorId));
                    onOffTextLight.setText(R.string.on);
                    onOffTextLight.setTextColor(Color.WHITE);
                    valueLight.setTextColor(Color.WHITE);
                    nameLight.setTextColor(Color.WHITE);
                }else{
                    dashboardLight.setCardBackgroundColor(ColorStateList.valueOf(Color.TRANSPARENT));
                    onOffTextLight.setText(R.string.off);
                    onOffTextLight.setTextColor(Color.LTGRAY);
                    valueLight.setTextColor(Color.GRAY);
                    nameLight.setTextColor(Color.GRAY);
                }
                if (!init) {
                    dashboardDetail.setDashLightStatus(isChecked);
                }
            }
        });

        onOffSwitchHumidity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int colorId = dashboardHumidity.getContext().getResources().getColor(R.color.dashboard_humidity);

                if (isChecked) {
                    dashboardHumidity.setCardBackgroundColor(ColorStateList.valueOf(colorId));
                    onOffTextHumidity.setText(R.string.on);
                    onOffTextHumidity.setTextColor(Color.WHITE);
                    valueHumidity.setTextColor(Color.WHITE);
                    nameHumidity.setTextColor(Color.WHITE);
                }else{
                    dashboardHumidity.setCardBackgroundColor(ColorStateList.valueOf(Color.TRANSPARENT));
                    onOffTextHumidity.setText(R.string.off);
                    onOffTextHumidity.setTextColor(Color.LTGRAY);
                    valueHumidity.setTextColor(Color.GRAY);
                    nameHumidity.setTextColor(Color.GRAY);
                }
                if (!init) {
                    dashboardDetail.setDashHumidityStatus(isChecked);
                }
            }
        });

        onOffSwitchAirCondition.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int colorId = dashboardAirCondition.getContext().getResources().getColor(R.color.dashboard_air_condition);

                if (isChecked) {
                    dashboardAirCondition.setCardBackgroundColor(ColorStateList.valueOf(colorId));
                    onOffTextAirCondition.setText(R.string.on);
                    onOffTextAirCondition.setTextColor(Color.WHITE);
                    valueAirCondition.setTextColor(Color.WHITE);
                    nameAirCondition.setTextColor(Color.WHITE);
                }else{
                    dashboardAirCondition.setCardBackgroundColor(ColorStateList.valueOf(Color.TRANSPARENT));
                    onOffTextAirCondition.setText(R.string.off);
                    onOffTextAirCondition.setTextColor(Color.LTGRAY);
                    valueAirCondition.setTextColor(Color.GRAY);
                    nameAirCondition.setTextColor(Color.GRAY);
                }
                if (!init) {
                    dashboardDetail.setDashAirConditionStatus(isChecked);
                }
            }
        });

        */

        refresh();

        dashboardTemperature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TemperatureActivity.class);
                startActivity(intent);
            }
        });

        dashboardHumidity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HumidityActivity.class);
                startActivity(intent);
            }
        });

        dashboardAirCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AirConditionActivity.class);
                startActivity(intent);
            }
        });

//        dashboardSensors.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, SensorsActivity.class);
//                startActivity(intent);
//            }
//        });

//        dashboardLight.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, LightActivity.class);
//                startActivity(intent);
//            }
//        });

        refreshReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                swipeRefreshLayout.setRefreshing(false);
                refresh();
            }
        };
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            final Dialog dialog = new Dialog(this); // Context, this, etc.
            dialog.setContentView(R.layout.password_dialog);
            dialog.show();
            Button cancel = dialog.findViewById(R.id.discard_changes);
            Button ok = dialog.findViewById(R.id.apply_changes);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.cancel();
                }
            });
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String sysPassword = new Settings(getApplicationContext()).getPassword();
                    String password = ((EditText)dialog.findViewById(R.id.passwordy)).getText().toString();
                    if (password.equals("")){
                        Toast.makeText(getApplicationContext(), "Pleased enter password", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Log.d("Password in dialog ", password);
                    if (!password.equals(sysPassword)){
                        Toast.makeText(getApplicationContext(), "Password wrong", Toast.LENGTH_SHORT).show();
                        return;

                    }
                    Intent intent = new Intent(getApplicationContext(), SettingsActivity2.class);
                    startActivity(intent);
                    dialog.cancel();

                }
            });
        }

        if (id == R.id.action_about_us){
            Intent intent = new Intent(getApplicationContext(), AboutUs.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        registerReceiver(refreshReceiver, new IntentFilter("ir.myapp.controller3.STOP_REFRESH"));
        refresh();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // unregister the receiver
        unregisterReceiver(refreshReceiver);
    }


    @Override
    public void onBackPressed() {
//        MainActivity.this.finishActivity(0);
        finishAffinity();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[]
            permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            Log.d("MainActivity","permission approved");
        }
    }




    @Override
    protected void onDestroy() {
        stopService(smsServiceIntent);
        super.onDestroy();
    }


}
