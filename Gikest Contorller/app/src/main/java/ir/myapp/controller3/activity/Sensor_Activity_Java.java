package ir.myapp.controller3.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import ir.myapp.controller3.R;
import ir.myapp.controller3.db.AppDatabase;
import ir.myapp.controller3.entity.Sensor_Struct;
import ir.myapp.controller3.entity.System_Struct;
import ir.myapp.controller3.service.Sensor_Activity_Service;

public class Sensor_Activity_Java extends AppCompatActivity {

    int System_SensorNum = 1;
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

    public void SensorActivity_Refresh() {
        AppDatabase db = AppDatabase.getInstance(getBaseContext());
        System_Struct system = db.System_Dao().GetLast();
        if (db.Sensor_Dao().Data_Count() != Sensor_Data_Count) {
            Sensor_Data_Count = db.Sensor_Dao().Data_Count();
            Sensor_LinearLayout.removeAllViews();
            for (int i = 0; i < System_SensorNum; i++) {
                View relay_view = getLayoutInflater().inflate(R.layout.sensor_fragment_view_xml, Sensor_LinearLayout, false);
                Sensor_LinearLayout.addView(relay_view); // the upper 2 line is to add the new layout par sensor's available

                Sensor_Struct sensor = db.Sensor_Dao().GetLast_WithPlace(i + 1);  //getting info of the related number
                int flag = 0;
                ViewGroup sensor_veiwgrp = (ViewGroup) Sensor_LinearLayout.getChildAt(i); //converting the newly added layout to apply info

                for (int j = 0; j < sensor_veiwgrp.getChildCount(); j++) {
                    if (sensor_veiwgrp.getChildAt(j) instanceof ImageView) {     //setting image for sensor icon
                        ImageView image = (ImageView) sensor_veiwgrp.getChildAt(j);
                        switch (sensor.Get_Mode()) {
                            case "Normal":
                                if (sensor.Value_T == 0)
                                    image.setImageResource(R.drawable.white_notselected_png);
                                else
                                    image.setImageResource(R.drawable.white_normstate_gif);
                                break;
                            case "Error":
                                image.setImageResource(R.drawable.yellow_errorstate_png);
                                break;
                            case "Disconnected":
                                image.setImageResource(R.drawable.red_failedstate_png);
                                break;
                        }
                    }
                    if (sensor_veiwgrp.getChildAt(j) instanceof TextView) {
                        TextView txt = (TextView) sensor_veiwgrp.getChildAt(j);
                        switch (flag) {
                            case 0:     //specify Sensor number
                                txt.setText(String.valueOf(i + 1));
                                flag++;
                                break;
                            case 1:      //specify the Values Temperature
                                txt.setText(sensor.Get_ValueT() + "°C");
                                flag++;
                                break;
                            case 2:      //specify the Values Humidity
                                txt.setText(sensor.Get_ValueH() + "%");
                                flag++;
                                break;
                        }

                    }
                }
            }
        }

    }
}