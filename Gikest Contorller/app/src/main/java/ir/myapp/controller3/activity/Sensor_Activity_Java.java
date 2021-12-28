package ir.myapp.controller3.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import ir.myapp.controller3.R;
import ir.myapp.controller3.db.AppDatabase;
import ir.myapp.controller3.entity.Sensor_Struct;
import ir.myapp.controller3.entity.System_Struct;
import ir.myapp.controller3.service.Sensor_Activity_Service;

public class Sensor_Activity_Java extends AppCompatActivity {

    int System_SensorNum = 2;
    int Sensor_Data_Count = 0;

    LinearLayout Sensor_LinearLayout;
    Thread SensorActivity_Service_Thread;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensor_activity_xml);


        Sensor_LinearLayout = findViewById(R.id.linear_sensor);

        SensorActivity_Service_Thread = new Sensor_Activity_Service(this, getLayoutInflater(), Sensor_LinearLayout);

        try {
            SensorActivity_Service_Thread.start();
        } catch (Exception e) {
        }

    }
}