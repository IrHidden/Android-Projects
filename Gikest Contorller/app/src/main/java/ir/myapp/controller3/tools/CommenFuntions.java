package ir.myapp.controller3.tools;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import ir.myapp.controller3.receiver.Sms_Detector;
import ir.myapp.controller3.service.DataReciver_Service;
import ir.myapp.controller3.service.SmsSender_Service;

public class CommenFuntions {

    Context cont;

    Thread WifiSocketServer_Thread;



    public CommenFuntions ()
    {
        this.cont=cont;
    }

    public String getCurrentTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }
    public void StartWifi(Context cont)
    {
        WifiSocketServer_Thread=new DataReciver_Service.WifiSocketServer(cont);
        WifiSocketServer_Thread.start();
    }

    public void SendSms(Context cont, Intent intent, String PhoneNumber, String Sms_Text)
    {
        SmsSender_Service Sender=new  SmsSender_Service(cont,intent,PhoneNumber,Sms_Text);
        new Sms_Detector().onReceive(cont, intent);
    }
    public String StringValueOfInputStream(InputStream Input)
    {
        try {
            BufferedInputStream bis = new BufferedInputStream(Input);
            ByteArrayOutputStream buf = new ByteArrayOutputStream();
            for (int result = bis.read(); result != -1 ; result = bis.read()) {
                if((char) result!='.')
                    buf.write((byte) result);
                else
                    break;
            }
            return String.valueOf(buf);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

}
