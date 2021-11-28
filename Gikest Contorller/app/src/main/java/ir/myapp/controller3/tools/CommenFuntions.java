package ir.myapp.controller3.tools;

import static java.lang.Thread.sleep;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import ir.myapp.controller3.db.AppDatabase;
import ir.myapp.controller3.entity.System_Struct;
import ir.myapp.controller3.receiver.Sms_Detector;
import ir.myapp.controller3.service.WifiDataTransfer_Service;
import ir.myapp.controller3.service.SmsSender_Service;

public class CommenFuntions {
    public static String ControllerIPAdress ="192.168.4.1";
    public static int ControllerPort =8888;


    Thread WifiSocketServer_Thread;
    Thread Wifi_Clint;

    public CommenFuntions ()
    {

    }

    public String getCurrentTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }


    public String GetSystemInfo(Context cont)
    {
        AppDatabase db = AppDatabase.getInstance(cont);
        System_Struct SystemDb=db.System_Dao().GetLast();

        StringBuilder InfoMsg=new StringBuilder();
        //System,Rate,num1,num2,num3,GTemp,GHum,Htol,Ttol.
        InfoMsg.append("System")
                .append(",")
                .append((char) SystemDb.Get_DataRefreshRate())
                .append(',')
                .append(SystemDb.Get_PhoneNumber1())
                .append(',')
                .append(SystemDb.Get_PhoneNumber2())
                .append(',')
                .append(SystemDb.Get_PhoneNumber3())
                .append(',')
                .append( (char)SystemDb.Get_TempValue())
                .append(',')
                .append( (char)SystemDb.Get_HumValue())
                .append(',')
                .append( (char)SystemDb.Get_TempTolerance())
                .append(',')
                .append( (char)SystemDb.Get_HumTolerance())
                .append('.');
        return InfoMsg.toString();
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


    //Wifi Functions
    public void StartWifi_Server(Context cont)
    {
        WifiSocketServer_Thread=new WifiDataTransfer_Service.WifiSocketServer(cont);
        WifiSocketServer_Thread.start();
    }

    public void StartWifi_Client(Context cont)
    {
        Wifi_Clint=new WifiDataTransfer_Service.WifiSocketClint(cont);
        Wifi_Clint.start();
    }


    //Sms Funtions
    public void SendSms(Context cont, Intent intent, String PhoneNumber, String Sms_Text)
    {
        SmsSender_Service Sender=new  SmsSender_Service(cont,intent,PhoneNumber,Sms_Text);
        new Sms_Detector().onReceive(cont, intent);
    }







}
