package ir.myapp.controller3.service;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import ir.myapp.controller3.R;
import ir.myapp.controller3.db.AppDatabase;
import ir.myapp.controller3.entity.Relay_Struct;
import ir.myapp.controller3.entity.Sensor_Struct;
import ir.myapp.controller3.entity.System_Struct;
import pl.droidsonroids.gif.GifImageView;

public class MainActivity_Service {
    public static class Refresher_Service extends Thread {

        int System_RelayNum = 5, System_SensorNum = 3;
        int Relay_Data_Count = 0, Sensor_Data_Count = 0;

        Context cont;
        TextView Hum_Val, Temp_Val;
        ViewGroup Dash_Relay;

        AppDatabase db;

        public Refresher_Service(Context cont, TextView Temp_Val, TextView Hum_Val, ViewGroup Dash_Relay) {
            this.cont = cont;
            this.Temp_Val = Temp_Val;
            this.Hum_Val = Hum_Val;
            this.Dash_Relay = Dash_Relay;

            db = AppDatabase.getInstance(cont);
        }

        @Override
        public void run() {

            try {
                sleep(200);
                while (true) {
                    sleep(200);
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
                            Dash_Relay_Refresher();
                            Dash_Sensor_Refresher();
                        } catch (Exception e) {

                        }
                    }

                });
            } catch (Exception e) {

            }
        }

        private void Dash_Relay_Refresher() {

            if (db.Relay_Dao().Data_Count() != Relay_Data_Count) {
                Relay_Data_Count = db.Relay_Dao().Data_Count();
                for (int i = 0; i < Dash_Relay.getChildCount(); i++) {

                    if (Dash_Relay.getChildAt(i) instanceof LinearLayout) {
                        LinearLayout linear = (LinearLayout) Dash_Relay.getChildAt(i);
                        linear.removeAllViews();
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(linear.getWidth(), linear.getHeight());
                        params.weight = 1;
                        for (int j = 0; j < System_RelayNum; j++) {
                            GifImageView image = new GifImageView(cont);

                            Relay_Struct relay = db.Relay_Dao().getLast_WithPlace(j + 1);
                            if (relay.Get_Name().equals("None"))
                                image.setImageResource(R.drawable.white_notselected_png);
                            else if (relay.Get_State()) {
                                switch (relay.Get_Name()) {
                                    case "Heater":
                                        image.setImageResource(R.drawable.white_fire_gif);
                                        break;
                                    case "Vaporizer":
                                        image.setImageResource(R.drawable.white_vapor_gif);
                                        break;
                                    case "Circulator":
                                        image.setImageResource(R.drawable.white_fan_gif);
                                        break;
                                }
                            } else {
                                switch (relay.Get_Name()) {
                                    case "Heater":
                                        image.setImageResource(R.drawable.white_fire_png);
                                        break;
                                    case "Vaporizer":
                                        image.setImageResource(R.drawable.white_vapor_png);
                                        break;
                                    case "Circulator":
                                        image.setImageResource(R.drawable.white_fan_png);
                                        break;
                                }
                            }

                            image.setLayoutParams(params);
                            linear.addView(image);

                        }
                    }
                }

            }
        }

        private void Dash_Sensor_Refresher() {
            int Tmp_Avg = 0, Hum_Avg = 0;
            if (db.Sensor_Dao().Data_Count() != Sensor_Data_Count) {
                Sensor_Data_Count = db.Sensor_Dao().Data_Count();
                System_Struct system = db.System_Dao().GetLast();
                List<Sensor_Struct> Temp_List = db.Sensor_Dao().Temperature_Avg(System_SensorNum);
                List<Sensor_Struct> Hum_List = db.Sensor_Dao().Humidity_Avg(System_SensorNum);
                for (int i = 0; i < System_SensorNum; i++) {
                    Sensor_Struct Temp=Temp_List.get(i);
                    Tmp_Avg+=Temp.Get_ValueT();

                    Sensor_Struct Hum=Hum_List.get(i);
                    Hum_Avg+=Hum.Get_ValueH();
                }
                Temp_Val.setText(Tmp_Avg/3 + "°C");
                Hum_Val.setText(Hum_Avg/3 + "%");
            }
        }
    }
}
