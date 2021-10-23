package ir.myapp.controller3.activity;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

import ir.myapp.controller3.R;
import ir.myapp.controller3.service.RelayEdit_Activity_Service;
import ir.myapp.controller3.service.SmsSender_Service;
import ir.myapp.controller3.tools.TimePicker_Fragment;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class RelayEdit_Activity_Java extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {


    int Relay_Number;  //Selected Relay Number
    int System_SensorNum = 3;
    String TimePicker_Str = null;

    Spinner Sp_RelayNum;
    TextView Txt_RelayNum,Txt_PhoneNum, Txt_TargetNum;
    Button Btn_RelayApply, Btn_StartTime, Btn_EndTime, Btn_OnTime, Btn_OffTime;
    ToggleButton Btn_RelayMode;
    Switch Sw_StartTime, Sw_EndTime;
    LinearLayout Layout_Timer, Layout_Auto, Layout_Sensors, Layout_Days;
    RadioGroup Rbg_RelayType;

    Thread RelayEdit_Service_Thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.relayedit_activity_xml);

        Sp_RelayNum = findViewById(R.id.sp_relayedit_relaynum);
        Sp_RelayNum.setOnItemSelectedListener(Spinner_RelayEdit_SelectedItem);

        Txt_RelayNum = findViewById(R.id.txt_relayedit_relaynum);
        Txt_PhoneNum=findViewById(R.id.txt_phonenum2);
        Txt_TargetNum=findViewById(R.id.txt_relayedit_target);

        Btn_RelayMode = findViewById(R.id.btn_relayedit_relaymode);
        Btn_RelayMode.setOnCheckedChangeListener(Btn_RelayMode_Click);
        Btn_RelayApply = findViewById(R.id.btn_realyedit_apply);
        Btn_RelayApply.setOnClickListener(Btn_ApplyChanges_Click);
        Btn_StartTime = findViewById(R.id.btn_relayedit_starttime);
        Btn_StartTime.setOnClickListener(Btn_StartTime_Click);
        Btn_EndTime = findViewById(R.id.btn_relayedit_endtime);
        Btn_EndTime.setOnClickListener(Btn_EndTime_Click);
        Btn_OnTime = findViewById(R.id.btn_relayedit_ontime);
        Btn_OnTime.setOnClickListener(Btn_OnTime_Click);
        Btn_OffTime = findViewById(R.id.btn_relayedit_offtime);
        Btn_OffTime.setOnClickListener(Btn_OffTime_Click);

        Sw_StartTime = findViewById(R.id.sw_relayedit_starttime);
        Sw_StartTime.setOnCheckedChangeListener(SW_RelayONTime_CheckChange);
        Sw_EndTime = findViewById(R.id.sw_realyedit_endtime);
        Sw_EndTime.setOnCheckedChangeListener(SW_RelayOFFTime_CheckChange);

        Layout_Timer = findViewById(R.id.layout_realyedit_timer);
        Layout_Auto = findViewById(R.id.layout_realyedit_auto);
        Layout_Sensors = findViewById(R.id.layout_realyedit_selectedsensors);
        Layout_Days = findViewById(R.id.layout_relayedit_days);

        Rbg_RelayType = findViewById(R.id.rbg_realy_edit_type);

        RelayEdit_Service_Thread = new RelayEdit_Activity_Service(this, Layout_Sensors);
        RelayEdit_Service_Thread.start();
    }

    private final AdapterView.OnItemSelectedListener Spinner_RelayEdit_SelectedItem = new OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            int Num = position + 1;
            try {
                Txt_RelayNum.setText("Relay Number: " + Num);
                Relay_Number = Num;
            } catch (Exception e) {
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    private final CompoundButton.OnCheckedChangeListener Btn_RelayMode_Click = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean Auto) {
            if (Auto) {
                Layout_Auto.setVisibility(View.VISIBLE);
                Layout_Timer.setVisibility(View.GONE);
            } else {
                Layout_Auto.setVisibility(View.GONE);
                Layout_Timer.setVisibility(View.VISIBLE);
            }
        }
    };
    private final CompoundButton.OnCheckedChangeListener SW_RelayONTime_CheckChange = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean StartTime_State) {
            Btn_StartTime.setEnabled(StartTime_State);
        }
    };
    private final CompoundButton.OnCheckedChangeListener SW_RelayOFFTime_CheckChange = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean EndTime_State) {
            Btn_EndTime.setEnabled(EndTime_State);
        }
    };
    private final View.OnClickListener Btn_StartTime_Click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            TimePicker_Str = "StartTime";
            DialogFragment TimePicker = new TimePicker_Fragment();
            TimePicker.show(getSupportFragmentManager(), "TimePicker");

        }
    };
    private final View.OnClickListener Btn_EndTime_Click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            TimePicker_Str = "EndTime";
            DialogFragment TimePicker = new TimePicker_Fragment();
            TimePicker.show(getSupportFragmentManager(), "TimePicker");

        }
    };
    private final View.OnClickListener Btn_OnTime_Click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            TimePicker_Str = "OnTime";
            DialogFragment TimePicker = new TimePicker_Fragment();
            TimePicker.show(getSupportFragmentManager(), "TimePicker");

        }
    };
    private final View.OnClickListener Btn_OffTime_Click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            TimePicker_Str = "OffTime";
            DialogFragment TimePicker = new TimePicker_Fragment();
            TimePicker.show(getSupportFragmentManager(), "TimePicker");

        }
    };

    private final View.OnClickListener Btn_ApplyChanges_Click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            try {
                String Rbg_Text;
                if (Rbg_RelayType.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(RelayEdit_Activity_Java.this, "Please Select Relay Type!", Toast.LENGTH_LONG).show();
                } else {
                    int Selected_Rb_id = Rbg_RelayType.getCheckedRadioButtonId();
                    RadioButton Rb_Selected = findViewById(Selected_Rb_id);
                    Rbg_Text = String.valueOf(Rb_Selected.getText());
                   String PhoneNum=Txt_PhoneNum.getText().toString();

                    if (Btn_RelayMode.isChecked()) {
                        StringBuilder Sensor_str = new StringBuilder();
                        String TargetNum =Txt_TargetNum.getText().toString();
                        for (int i = 0; i < Layout_Sensors.getChildCount(); i++) {
                            ToggleButton tg = (ToggleButton) Layout_Sensors.getChildAt(i);
                            if (tg.isChecked())
                                Sensor_str.append(i+1).append("-");
                        }
                        Sensor_str.deleteCharAt(Sensor_str.length() - 1); //to remove '-' at the end of String
                        StringBuilder SmsText=new StringBuilder();
                        SmsText.append(Relay_Number).append(",").append(Sensor_str).append(",").append(TargetNum);
                        new SmsSender_Service(getApplicationContext(),getIntent(),PhoneNum,SmsText.toString());
                    } else {
                        long StartTime, EndTime, OnTime, OffTime;
                        int Time_Year, Time_Month, Time_Day, Time_Hour, Time_Minute;
                        String temp;
                        Calendar Time = Calendar.getInstance();

                        Time_Year = Calendar.getInstance().get(Calendar.YEAR);
                        Time_Month = Calendar.getInstance().get(Calendar.MONTH);
                        Time_Day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

                        if (Sw_StartTime.isChecked()) {
                            temp = Btn_StartTime.getText().toString();
                            Time_Hour = Integer.parseInt(String.valueOf(temp.charAt(0)) + temp.charAt(1));
                            Time_Minute = Integer.parseInt(String.valueOf(temp.charAt(3)) + temp.charAt(4));
                            Time.set(Time_Year, Time_Month, Time_Day, Time_Hour, Time_Minute);
                            StartTime = MILLISECONDS.toSeconds(Time.getTimeInMillis());
                        } else {
                            StartTime = MILLISECONDS.toSeconds(System.currentTimeMillis());
                        }

                        if (Sw_EndTime.isChecked()) {
                            temp = Btn_EndTime.getText().toString();
                            Time_Hour = Integer.parseInt(String.valueOf(temp.charAt(0)) + temp.charAt(1));
                            Time_Minute = Integer.parseInt(String.valueOf(temp.charAt(3)) + temp.charAt(4));
                            Time.set(Time_Year, Time_Month, Time_Day, Time_Hour, Time_Minute);
                            EndTime = MILLISECONDS.toSeconds(Time.getTimeInMillis());
                        } else {
                            Time_Hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                            Time_Minute = Calendar.getInstance().get(Calendar.MINUTE);
                            Time.set(Time_Year + 1, Time_Month, Time_Day, Time_Hour, Time_Minute);
                            EndTime = MILLISECONDS.toSeconds(Time.getTimeInMillis());
                        }

                        temp = Btn_OnTime.getText().toString();
                        Time_Hour = Integer.parseInt(String.valueOf(temp.charAt(0)) + temp.charAt(1));
                        Time_Minute = Integer.parseInt(String.valueOf(temp.charAt(3)) + temp.charAt(4));
                        Time.set(Time_Year, Time_Month, Time_Day, Time_Hour, Time_Minute);
                        OnTime = MILLISECONDS.toSeconds(Time.getTimeInMillis());

                        temp = Btn_OffTime.getText().toString();
                        Time_Hour = Integer.parseInt(String.valueOf(temp.charAt(0)) + temp.charAt(1));
                        Time_Minute = Integer.parseInt(String.valueOf(temp.charAt(3)) + temp.charAt(4));
                        Time.set(Time_Year, Time_Month, Time_Day, Time_Hour, Time_Minute);
                        OffTime = MILLISECONDS.toSeconds(Time.getTimeInMillis());

                        StringBuilder Days_str = new StringBuilder();
                        for (int i = 0; i < Layout_Days.getChildCount(); i++) {
                            ToggleButton tg = (ToggleButton) Layout_Days.getChildAt(i);
                            if (tg.isChecked())
                                Days_str.append(tg.getText()).append("-");
                        }
                        Days_str.deleteCharAt(Days_str.length() - 1);

                        StringBuilder SmsText = new StringBuilder();
                        SmsText.append(Relay_Number).append(",").append(StartTime).append(",").append(EndTime).append(",").append(OnTime).append(",").append(OffTime).append(",").append(Days_str);


                        new SmsSender_Service(getApplicationContext(),getIntent(),PhoneNum,SmsText.toString());

                    }
                }
            } catch (Exception e) {
                Toast.makeText(RelayEdit_Activity_Java.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    };

    @Override
    public void onTimeSet(TimePicker view, int hour, int minute) {
        if (hour < 24) {
            StringBuilder Time = new StringBuilder();
            if (hour < 10)
                Time.append("0").append(hour);
            else
                Time.append(hour);
            Time.append(":");
            if (minute > 10)
                Time.append(minute);
            else
                Time.append("0").append(minute);
            switch (TimePicker_Str) {
                case "StartTime":
                    Btn_StartTime.setText(Time);
                    break;
                case "EndTime":
                    Btn_EndTime.setText(Time);
                    break;
                case "OnTime":
                    Btn_OnTime.setText(Time);
                    break;
                case "OffTime":
                    Btn_OffTime.setText(Time);
                    break;
            }
        } else {
            Toast.makeText(RelayEdit_Activity_Java.this, "Maximum of Time is 23:59", Toast.LENGTH_LONG).show();
        }
    }


}