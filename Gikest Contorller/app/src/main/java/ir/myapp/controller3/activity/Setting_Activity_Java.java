package ir.myapp.controller3.activity;

import android.app.PendingIntent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import ir.myapp.controller3.R;
import ir.myapp.controller3.db.AppDatabase;

public class Setting_Activity_Java extends AppCompatActivity {

    Button DataClean_Btn,AddPhoneNum_Btn,DelPhoneNum_Btn;
    TextView PhoneNum_Text,TargetPhoneNum_Text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity_xml);

        DataClean_Btn=findViewById(R.id.btn_dataclean);
        DataClean_Btn.setOnClickListener(Btn_DataClean_Click);
        AddPhoneNum_Btn=findViewById(R.id.btn_addphonenum);
        AddPhoneNum_Btn.setOnClickListener(Btn_AddPhoneNum_Click);
        DelPhoneNum_Btn=findViewById(R.id.btn_deletephonenum);
        DelPhoneNum_Btn.setOnClickListener(Btn_DelPhoneNum_Click);


        PhoneNum_Text=findViewById(R.id.txt_phonenum);
        TargetPhoneNum_Text=findViewById(R.id.txt_targetphonenum);
    }
    private final View.OnClickListener Btn_DataClean_Click = new View.OnClickListener()
    {
        @Override
        public void onClick(View v) {
            AppDatabase db=AppDatabase.getInstance(getApplicationContext());
            db.Relay_Dao().Delete_All();
            db.Sensor_Dao().Delete_All();
            Toast.makeText(Setting_Activity_Java.this, "All Data Has been Cleaned", Toast.LENGTH_SHORT).show();
        }
    };
    private final View.OnClickListener Btn_AddPhoneNum_Click=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            StringBuilder temp=new StringBuilder(TargetPhoneNum_Text.toString());
            temp.deleteCharAt(0);
            String Sms_Text="N"+temp.toString();
            PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, getIntent(), 0);
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(PhoneNum_Text.toString(), null,Sms_Text , pi, null);

        }
    };
    private final View.OnClickListener Btn_DelPhoneNum_Click=new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            StringBuilder temp=new StringBuilder(TargetPhoneNum_Text.toString());
            temp.deleteCharAt(0);
            String Sms_Text="D"+temp.toString();
            PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, getIntent(), 0);
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(PhoneNum_Text.toString(), null,Sms_Text , pi, null);
        }
    };
}