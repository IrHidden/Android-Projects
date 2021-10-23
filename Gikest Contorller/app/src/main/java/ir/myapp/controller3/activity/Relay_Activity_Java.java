package ir.myapp.controller3.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import org.jetbrains.annotations.NotNull;

import ir.myapp.controller3.R;
import ir.myapp.controller3.db.AppDatabase;
import ir.myapp.controller3.entity.Relay_Struct;
import ir.myapp.controller3.entity.System_Struct;
import ir.myapp.controller3.service.Relay_Activity_Service;

public class Relay_Activity_Java extends AppCompatActivity {

    int System_RelayNum = 5;

    LinearLayout Relay_LinearLayout;
    Button Relay_Edit_btn;
    Thread RelayActivity_Service_Thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.relay_activity_xml);

        Relay_LinearLayout = findViewById(R.id.linear_relay);

        Relay_Edit_btn=findViewById(R.id.btn_relayedit);
        Relay_Edit_btn.setOnClickListener(Btn_Relay_Edit_Click);

        RelayActivity_Service_Thread = new Relay_Activity_Service(this, getLayoutInflater(), Relay_LinearLayout);

        try {
            RelayActivity_Service_Thread.start();
        } catch (Exception e) {
        }

    }

    private final View.OnClickListener  Btn_Relay_Edit_Click=new View.OnClickListener() {
        @Override
        public void onClick(@NotNull View v) {
            Intent intent = new Intent(getApplicationContext(), RelayEdit_Activity_Java.class);
            Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.presses_state);
            v.startAnimation(anim);
            startActivity(intent);
        }
    };

    public void RelayActivity_Refresh() {
        AppDatabase db = AppDatabase.getInstance(this);
        System_Struct system = db.System_Dao().GetLast();
        Relay_LinearLayout.removeAllViews();
        for (int i = 0; i < System_RelayNum; i++) {
            View relay_view = getLayoutInflater().inflate(R.layout.relay_fragment_view_xml, Relay_LinearLayout, false);
            Relay_LinearLayout.addView(relay_view); // the upper 2 line is to add the new layout par relay's available

            Relay_Struct relay = db.Relay_Dao().GetLast_WithPlace(i + 1);  //getting info of the related number
            int flag = 0;

            ViewGroup relay_veiwgrp = (ViewGroup) Relay_LinearLayout.getChildAt(i); //converting the newly added layout to apply info

            for (int j = 0; j < relay_veiwgrp.getChildCount(); j++) {
                if (relay_veiwgrp.getChildAt(j) instanceof AppCompatSpinner) {      //setting data for spinner view
                    AppCompatSpinner cb_attch_sensors = (AppCompatSpinner) relay_veiwgrp.getChildAt(j);
                    String[] sensors_attached;
                    if (relay.Mode.equals("Auto"))
                        sensors_attached = relay.Get_Sensors().split("-");
                    else {
                        sensors_attached = new String[1];
                        sensors_attached[0] = this.getString(R.string.None_Sensor);
                    }
                    ArrayAdapter<String> cb_array = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, sensors_attached);
                    cb_attch_sensors.setAdapter(cb_array);
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
                                    txt.setText(this.getString(R.string.Auto) + ": ");
                                    break;
                                case "Timer":
                                    txt.setText(this.getString(R.string.Timer) + ": ");
                                    break;
                                case "None":
                                    txt.setText(this.getString(R.string.None_Mode) + ": ");
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