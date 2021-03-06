package ir.myapp.controller3.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import ir.myapp.controller3.R;
import ir.myapp.controller3.db.AppDatabase;
import ir.myapp.controller3.entity.Relay_Struct;
import ir.myapp.controller3.entity.Sensor_Struct;
import ir.myapp.controller3.entity.System_Struct;
import ir.myapp.controller3.service.MainActivity_Service;
import ir.myapp.controller3.tools.CommenFuntions;
import ir.myapp.controller3.tools.DataTransferMethods;
import ir.myapp.controller3.tools.SoundEffect_Func;
import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;

    GifImageView Dash_Update;
    ViewGroup Dash_Relay, Dash_Sensor, Dash_Setting, Dash_Log, Dash_Plot, Dash_AboutUs;
    TextView Hum_Val, Temp_Val;

    AppDatabase db;

    Thread MainRefresh_Service_Thread;

    CommenFuntions comnFunc;
    SoundEffect_Func ClickSound;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activitymain_xml);

        Dash_Relay = findViewById(R.id.relay_dash);
        Dash_Relay.setOnClickListener(Dash_Relay_Click);

        Dash_Sensor = findViewById(R.id.sensor_dash);
        Dash_Sensor.setOnClickListener(Dash_Sensor_Click);

        Dash_AboutUs = findViewById(R.id.setting_dash);
        Dash_AboutUs.setOnClickListener(Dash_Setting_Click);

        Dash_Setting = findViewById(R.id.aboutus_dash);
        Dash_Setting.setOnClickListener(Dash_AboutUs_Click);

        Dash_Log = findViewById(R.id.log_dash);
        Dash_Log.setOnClickListener(Dash_Log_Click);

        Dash_Plot = findViewById(R.id.plot_dash);

        Dash_Update = findViewById(R.id.update_dash);
        Dash_Update.setOnClickListener(Dash_Update_Click);

        Hum_Val = findViewById(R.id.value_humidity);
        Temp_Val = findViewById(R.id.value_temperature);

        Check_Permissions();

        db = AppDatabase.getInstance(getApplicationContext());

        comnFunc = new CommenFuntions();
        ClickSound=new SoundEffect_Func(getApplicationContext());

        MainRefresh_Service_Thread = new MainActivity_Service.Refresher_Service(getBaseContext(), Temp_Val, Hum_Val, Dash_Relay);
        MainRefresh_Service_Thread.start();

       // ComFuc.StartWifi_Client(getApplicationContext());
        // Toast.makeText(getApplicationContext(), "Wifi on Port:8888 has been Started", Toast.LENGTH_SHORT).show();
    }


    private final View.OnClickListener Dash_Relay_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ClickSound.PlayPressSound();
            Intent intent = new Intent(getApplicationContext(), Relay_Activity_Java.class);
            Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.presses_state);
            v.startAnimation(anim);
            startActivity(intent);
        }
    };

    private final View.OnClickListener Dash_Sensor_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ClickSound.PlayPressSound();
            Intent intent = new Intent(getApplicationContext(), Sensor_Activity_Java.class);
            Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.presses_state);
            v.startAnimation(anim);
            startActivity(intent);
        }
    };

    private final View.OnClickListener Dash_Setting_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ClickSound.PlayPressSound();
            Intent intent = new Intent(getApplicationContext(), Setting_Activity_Java.class);
            Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.presses_state);
            v.startAnimation(anim);
            startActivity(intent);
        }
    };

    private final View.OnClickListener Dash_AboutUs_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ClickSound.PlayPressSound();
            Intent intent = new Intent(getApplicationContext(), Aboutus_Activity_Java.class);
            Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.presses_state);
            v.startAnimation(anim);
            startActivity(intent);
        }
    };

    private final View.OnClickListener Dash_Log_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ClickSound.PlayPressSound();
            Intent intent = new Intent(getApplicationContext(), Logs_Activity_Java.class);
            Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.presses_state);
            v.startAnimation(anim);
            startActivity(intent);
        }
    };

    private final View.OnClickListener Dash_Logout_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ClickSound.PlayPressSound();
            Intent intent = new Intent(getApplicationContext(), Register_Activity_Java.class);
            Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.presses_state);
            v.startAnimation(anim);
            startActivity(intent);
        }
    };
    private final View.OnClickListener Dash_Update_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ClickSound.PlayPressSound();
            //Sms Wifi
            String dataTransferring_Type = "Wifi";
            if (dataTransferring_Type == "Sms") {
                comnFunc.SendSms(getApplicationContext(), getIntent(), "+989148225636", "getAll");
                Toast.makeText(getApplicationContext(), getString(R.string.Data_Request_txt), Toast.LENGTH_SHORT).show();
            } else if (dataTransferring_Type == "Wifi") {

                AppDatabase db = AppDatabase.getInstance(getApplicationContext());
                System_Struct system = new System_Struct("ON",
                        "in",
                        5,
                        "+989214367321",
                        "+989030755965",
                        "+989145530400",
                        1,
                        5,
                        "Things",
                        30,
                        50 ,
                        4,
                        5 );

                for (int i =0; i<5; i++) {
                    Relay_Struct relay = new Relay_Struct(i+1,
                            "Heater",
                            "Auto",
                            true,
                            30,
                            0,
                            0,
                            "1-2-3",
                            comnFunc.getCurrentTime());
                    db.Relay_Dao().Insert(relay);
                }
                for (int i =0;i<3;i++)
                {
                    Sensor_Struct sensor=new Sensor_Struct(i+1,"Normal",30,20, comnFunc.getCurrentTime());
                    db.Sensor_Dao().Insert(sensor);
                }
                db.System_Dao().Insert(system);
                comnFunc.StartWifi_Client(getApplicationContext());
                Toast.makeText(getApplicationContext(), "Wifi on Port:8888 has been Started", Toast.LENGTH_SHORT).show();



            }else if(dataTransferring_Type == "Internet")
            {
                DataTransferMethods p=new DataTransferMethods();
                Toast.makeText(MainActivity.this, p.Send(""), Toast.LENGTH_SHORT).show();
            }
        }
    };


    public void Check_Permissions() {

        if (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS,
                    Manifest.permission.READ_SMS,
                    Manifest.permission.RECEIVE_SMS,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_SEND_SMS);
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[]
            permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            Log.d("MainActivity", "permission approved");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
