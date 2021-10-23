package ir.myapp.controller3.service;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.widget.Toast;

public class SmsSender_Service extends Thread {
    
    Context _cont;
    Intent _intent;
    String PhoneNum,SmsText;
    public SmsSender_Service(Context cont, Intent intent,String PhoneNum, String SmsText)
    {
        _cont=cont;
        _intent=intent;
        this.PhoneNum=PhoneNum;
        this.SmsText=SmsText;
        run();
    }
    public void run() {
        try {
            sleep(20);
            SmsSender(PhoneNum,SmsText);
        } catch (Exception e) {

        }
    }
    private boolean SmsSender(String PhoneNum, String SmsText)
    {
        try {
            PendingIntent pi = PendingIntent.getActivity(_cont, 0, _intent, 0);
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(PhoneNum, null, SmsText, pi, null);
            Toast.makeText(_cont, "Sms Sended!", Toast.LENGTH_SHORT).show();
            return true;
        }catch(Exception e)
            {
                Toast.makeText(_cont, e.getMessage(), Toast.LENGTH_LONG).show();
                return false;
            }
    }
    
}
