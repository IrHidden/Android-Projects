package ir.myapp.controller3.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import ir.myapp.controller3.tools.SmsDigest_Func;

public class Sms_Detector extends BroadcastReceiver {
    private Bundle bundle;
    private SmsMessage currentSMS;
    private String message;

    TextView Hum_Val,Temp_Val;
    ViewGroup Dash_Relay;
    public void Sms_Detector_arg(TextView Hum_Val, TextView Temp_Val, ViewGroup Dash_Relay)
    {
        this.Hum_Val=Hum_Val;
        this.Temp_Val=Temp_Val;
        this.Dash_Relay=Dash_Relay;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
            String System_PhoneNumber = "+989148225636";
            if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
                bundle = intent.getExtras();
                if (bundle != null) {
                    Object[] pdu_Objects = (Object[]) bundle.get("pdus");

                    if (pdu_Objects != null) {

                        for (Object aObject : pdu_Objects) {
                            currentSMS = getIncomingMessage(aObject, bundle);
                            String SenderNum = currentSMS.getDisplayOriginatingAddress();
                            message = currentSMS.getDisplayMessageBody();

                            if (SenderNum.equals(System_PhoneNumber)) {
                                new SmsDigest_Func(message, context);
                                //Refresh_Service.MainPage_Refresh Ref_Main=new Refresh_Service.MainPage_Refresh(context, Hum_Val,Temp_Val,Dash_Relay);
                                //Ref_Main.execute();
                                this.abortBroadcast();
                                //DataReciver_Service.Sms_Reciver Sms_Service= new DataReciver_Service.Sms_Reciver(context, intent, Hum_Val, Temp_Val, Dash_Relay);
                                //Sms_Service.execute();
                            }

                        }

                    }
                }


            }
    }
    private SmsMessage getIncomingMessage(Object aObject, Bundle bundle) {
        SmsMessage currentSMS;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String format = bundle.getString("format");
            currentSMS = SmsMessage.createFromPdu((byte[]) aObject, format);
        } else {
            currentSMS = SmsMessage.createFromPdu((byte[]) aObject);
        }
        return currentSMS;
    }
}
