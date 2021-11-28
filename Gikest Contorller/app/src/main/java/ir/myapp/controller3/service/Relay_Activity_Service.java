package ir.myapp.controller3.service;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatSpinner;

import ir.myapp.controller3.R;
import ir.myapp.controller3.db.AppDatabase;
import ir.myapp.controller3.entity.Relay_Struct;
import ir.myapp.controller3.entity.System_Struct;

public class Relay_Activity_Service extends Thread {
    int System_RelayNum = 5;

    Context cont;
    LayoutInflater Relay_Inflater;
    LinearLayout Relay_LinearLayout;

    int Relay_Data_Count=0;

    public Relay_Activity_Service(Context cont, LayoutInflater Relay_Inflater, LinearLayout linear_layout) {
        this.cont = cont;
        this.Relay_Inflater = Relay_Inflater;
        this.Relay_LinearLayout = linear_layout;
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
            new Handler(Looper.getMainLooper()).post(() -> {
                try {
                    RelayActivity_Refresh();
                } catch (Exception e) {

                }
            });
            Thread.sleep(1500);
        } catch (Exception e) {

        }
    }

    public void RelayActivity_Refresh() {
        AppDatabase db = AppDatabase.getInstance(cont);
        System_Struct system = db.System_Dao().GetLast();

        if (db.Relay_Dao().Data_Count()!=Relay_Data_Count) {
            Relay_Data_Count=db.Relay_Dao().Data_Count();
            Relay_LinearLayout.removeAllViews();

            for (int i = 0; i < System_RelayNum; i++) {
                View relay_view = Relay_Inflater.inflate(R.layout.relay_fragment_view_xml, Relay_LinearLayout, false);

                Relay_LinearLayout.addView(relay_view); // the upper 2 line is to add the new layout par relay's available

                Relay_Struct relay = db.Relay_Dao().GetLast_WithPlace(i + 1);  //getting info of the related number
                int flag = 0;

                ViewGroup relay_veiwgrp = (ViewGroup) Relay_LinearLayout.getChildAt(i); //converting the newly added layout to apply info

                for (int j = 0; j < relay_veiwgrp.getChildCount(); j++) {

                    if (relay_veiwgrp.getChildAt(j) instanceof AppCompatSpinner) {      //setting data for spinner view
                        AppCompatSpinner Sp_attch_sensors = (AppCompatSpinner) relay_veiwgrp.getChildAt(j);
                        String[] sensors_attached;
                        if (relay.Mode.equals("Auto"))
                            sensors_attached = relay.Get_Sensors().split("-");
                        else {
                            sensors_attached = new String[1];
                            sensors_attached[0] = cont.getString(R.string.None_Sensor);
                        }
                        ArrayAdapter<String> cb_array = new ArrayAdapter<String>(cont, android.R.layout.simple_spinner_dropdown_item, sensors_attached);
                        Sp_attch_sensors.setAdapter(cb_array);
                    }

                    if (relay_veiwgrp.getChildAt(j) instanceof ImageView) {     //setting image for relay icon
                        ImageView image = (ImageView) relay_veiwgrp.getChildAt(j);
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

                    }
                    if (relay_veiwgrp.getChildAt(j) instanceof TextView) {
                        TextView txt = (TextView) relay_veiwgrp.getChildAt(j);
                        switch (flag) {
                            case 0:     //specify relay number
                                txt.setText(String.valueOf(i + 1));
                                flag++;
                                break;
                            case 1:      //specify the mode of relay
                                switch (relay.Get_Mode()) {
                                    case "Auto":
                                        txt.setText(cont.getString(R.string.Auto) + ": ");
                                        break;
                                    case "Timer":
                                        txt.setText(cont.getString(R.string.Timer) + ": ");
                                        break;
                                    case "None":
                                        txt.setText(cont.getString(R.string.None_Mode) + ": ");
                                        break;
                                }
                                flag++;
                                break;
                            case 2:         //specify the values for being on or off
                                if (relay.Get_Mode().equals("Auto"))
                                    txt.setText(relay.Get_Name().equals("Heater") ? relay.Value + "°c  ->  " + relay.Value_G + "°c" : relay.Value + "%  ->  " + relay.Value_G + "%");
                                else
                                    txt.setText(relay.Value + "min  -  " + relay.Value_G+"min"  );

                                flag++;
                                break;
                        }
                    }
                }
            }
        }
    }


}
