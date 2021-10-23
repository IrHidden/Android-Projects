package ir.myapp.controller3.service;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ir.myapp.controller3.R;
import ir.myapp.controller3.db.AppDatabase;
import ir.myapp.controller3.entity.Sensor_Struct;
import ir.myapp.controller3.entity.System_Struct;

public class Sensor_Activity_Service extends Thread {
    int System_SensorNum = 1;
    int Sensor_Data_Count = 0;

    Context cont;
    LayoutInflater Sensor_Inflater;
    LinearLayout Sensor_LinearLayout;

    public Sensor_Activity_Service(Context cont, LayoutInflater Sensor_Inflater, LinearLayout Sensor_LinearLayout) {
        this.cont = cont;
        this.Sensor_Inflater = Sensor_Inflater;
        this.Sensor_LinearLayout = Sensor_LinearLayout;
    }

    @Override
    public void run() {
        try {
            sleep(20);
            while (true)
                Refersher_UiThread();
        } catch (Exception e) {

        }
    }
    private void Refersher_UiThread() {
        try {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    try {
                        SensorActivity_Refresh();
                    } catch (Exception e) {

                    }
                }

            });
            Thread.sleep(1500);
        } catch (Exception e) {

        }
    }

    public void SensorActivity_Refresh() {
        AppDatabase db = AppDatabase.getInstance(cont);
        System_Struct system = db.System_Dao().GetLast();

        if (db.Sensor_Dao().Data_Count() != Sensor_Data_Count) {
            Sensor_Data_Count = db.Sensor_Dao().Data_Count();
            Sensor_LinearLayout.removeAllViews();

            for (int i = 0; i < System_SensorNum; i++) {
                View relay_view = Sensor_Inflater.inflate(R.layout.sensor_fragment_view_xml, Sensor_LinearLayout, false);
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
                                txt.setText(sensor.Get_ValueT() + "°c");
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