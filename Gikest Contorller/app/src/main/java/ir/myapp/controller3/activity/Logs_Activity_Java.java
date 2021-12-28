package ir.myapp.controller3.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

import ir.myapp.controller3.R;
import ir.myapp.controller3.db.AppDatabase;
import ir.myapp.controller3.entity.Relay_Struct;
import ir.myapp.controller3.entity.Sensor_Struct;
import ir.myapp.controller3.tools.CommenFuntions;

public class Logs_Activity_Java extends AppCompatActivity {


    ViewGroup Linear_Relay, Linear_Sensor;
    Button Relay_Sensor_btn;

    ArrayList<String> Recycle_Items = new ArrayList<>();
    ArrayList<String> Sensor_Items = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logs_activity_xml);

        Linear_Relay = findViewById(R.id.linear_log_relay);
        Linear_Sensor = findViewById(R.id.linear_log_sensor);

        Relay_Sensor_btn = findViewById(R.id.btn_relay_sensor);
        Relay_Sensor_btn.setOnClickListener(Relay_Sensor_Click);

        Getting_Logs();

    }

    public void Getting_Logs() {
        AppDatabase db = AppDatabase.getInstance(this);

        ArrayList<Relay_Struct> relays = new ArrayList<>(db.Relay_Dao().Get_All());
        for (int i = relays.size() - 1; i >= 0; i--) {
            Relay_Struct relay = relays.get(i);
            String log_text = null;
            if (relay.Get_Mode().equals("Auto")) {
                log_text = relay.Get_DataTime() +
                        "\nRelay Num: " + relay.Get_Place()
                        + " Value: " + relay.Get_ValueG()
                        + " State: " + relay.Get_State() + "\n";
            } else if (relay.Get_Mode().equals("Auto")) {
                log_text = relay.Get_DataTime() +
                        "\nRelay Num: " + relay.Get_Place()
                        + " Goal Value: " + relay.Get_OnTime() + " " + relay.Get_OffTime()
                        + " State: " + relay.Get_State() + "\n";
            }
            TextView View = new TextView(Linear_Relay.getContext());
            View.setText(log_text);
            View.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.White));
            Linear_Relay.addView(View);
        }
        ArrayList<Sensor_Struct> sensors = new ArrayList<>(db.Sensor_Dao().Get_All());
        for (int i = sensors.size() - 1; i >= 0; i--) {
            Sensor_Struct sensor = sensors.get(i);
            String log_text = sensor.Get_DataTime() + "\nSensor Num: " + sensor.Get_Place()
                    + " Humidity: " + sensor.Get_ValueH() + " Temperature " + sensor.Get_ValueT() + "\n";
            TextView View = new TextView(Linear_Sensor.getContext());
            View.setText(log_text);
            View.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.White));
            Linear_Sensor.addView(View);
        }
    }
    View.OnClickListener Relay_Sensor_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (Linear_Relay.getVisibility() == View.VISIBLE) {
                Linear_Relay.setVisibility(View.GONE);
                Linear_Sensor.setVisibility(View.VISIBLE);
            } else if (Linear_Sensor.getVisibility() == View.VISIBLE) {
                Linear_Sensor.setVisibility(View.GONE);
                Linear_Relay.setVisibility(View.VISIBLE);
            }
        }
    };
}
