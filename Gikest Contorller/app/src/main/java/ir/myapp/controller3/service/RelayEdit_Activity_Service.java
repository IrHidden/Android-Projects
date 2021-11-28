package ir.myapp.controller3.service;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import ir.myapp.controller3.R;
import ir.myapp.controller3.db.AppDatabase;

public class RelayEdit_Activity_Service extends Thread{

    int System_SensorNum = 3;
    int Sensor_Data_Count = 0;

    AppDatabase db;
    Context cont;

    LinearLayout Layout_SelectedSensors;

    public RelayEdit_Activity_Service(Context cont, LinearLayout Layout_SelectedSensors){

        this.cont = cont;
        this.Layout_SelectedSensors=Layout_SelectedSensors;

        db = AppDatabase.getInstance(cont);
    }

    @Override
    public void run() {

        try {
            sleep(200);
            while (true) {
                sleep(100);
                Refersher_UiThread();
            }
        } catch (Exception e) {

        }
    }

    private void Refersher_UiThread() {
        try {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    try {
                        SensorsToSelect_Refresher();
                    } catch (Exception e) {

                    }
                }

            });
        } catch (Exception e) {

        }
    }

    private void SensorsToSelect_Refresher() {
        if (db.Sensor_Dao().Data_Count() != Sensor_Data_Count) {
            Sensor_Data_Count = db.Sensor_Dao().Data_Count();
            Layout_SelectedSensors.removeAllViews();
            for (int i = 0; i < System_SensorNum; i++) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(130, 130);
                ToggleButton tg = new ToggleButton(cont);
                tg.setBackground(cont.getDrawable(R.drawable.circularbtn_theme_pressesstate));
                tg.setText("S " + (i+1));
                tg.setTextOn("S " + (i+1));
                tg.setTextOff("S " + (i+1));
                tg.setTextColor(Color.WHITE);
                tg.setChecked(true);
                tg.setTextSize(10);
                tg.setLayoutParams(params);
                Layout_SelectedSensors.addView(tg);
            }
        }
    }
}
