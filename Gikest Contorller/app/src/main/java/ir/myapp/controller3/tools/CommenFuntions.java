package ir.myapp.controller3.tools;

import static java.lang.Thread.sleep;

import android.content.Context;
import android.content.Intent;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import ir.myapp.controller3.db.AppDatabase;
import ir.myapp.controller3.entity.Relay_Struct;
import ir.myapp.controller3.entity.Sensor_Struct;
import ir.myapp.controller3.entity.System_Struct;
import ir.myapp.controller3.receiver.Sms_Detector;
import ir.myapp.controller3.service.WifiDataTransfer_Service;
import ir.myapp.controller3.service.SmsSender_Service;

public class CommenFuntions {
    public static String ControllerIPAdress = "192.168.43.1";
    //public static String ControllerIPAdress = "192.168.4.1";
    public static int ControllerPort = 8888;
    public static int SensorsNum = 3;
    public static int RelaysNum = 5;


    Thread WifiSocketServer_Thread;
    Thread Wifi_Clint;

    public CommenFuntions ()
    {

    }
    public Relay_Struct getRelay(Context cont, int relayPlace){
        AppDatabase db = AppDatabase.getInstance(cont);
        return db.Relay_Dao().getLast_WithPlace(relayPlace);
    }

    public Sensor_Struct getSensor(Context cont, int sensorPlace){
        AppDatabase db = AppDatabase.getInstance(cont);
        return db.Sensor_Dao().getLast_WithPlace(sensorPlace);
    }

    public float avgSensors(Context cont, String [] Sensors_str,boolean isHum) {
        float avg = 0;
        for (int i = 0; i < Sensors_str.length; i++)
            if (isHum)
                avg += getSensor(cont, Integer.parseInt(Sensors_str[i])).Get_ValueH();
            else
                avg += getSensor(cont, Integer.parseInt(Sensors_str[i])).Get_ValueT();

        avg /= Sensors_str.length;
        return avg;
    }

    public float getRelayAvg(Context cont,int relayPlace, boolean isHum) {
        String[] Sensors_str = getRelay(cont, relayPlace).Sensors.split("-");
        return avgSensors(cont, Sensors_str, isHum);
    }

    public String getCurrentTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public String getSystemInfo(Context cont)
    {
        AppDatabase db = AppDatabase.getInstance(cont);
        System_Struct SystemDb=db.System_Dao().GetLast();

        StringBuilder InfoMsg=new StringBuilder();
        //System,Rate,num1,num2,num3,GTemp,GHum,Htol,Ttol.
        InfoMsg.append("System")
                .append(":")
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
    public String getRelayInfo(Context cont){
        StringBuilder InfoMsg = new StringBuilder();

        InfoMsg.append("Relay")
                .append(":");
        for (int i =0; i< RelaysNum; i++) {

            Relay_Struct RelayDb = getRelay(cont, i+1);

            switch (RelayDb.Get_Name()) {
                case "Light":
                    InfoMsg.append("L");
                    break;
                case "Vaporizer":
                    InfoMsg.append("V");
                    break;
                case "Circulator":
                    InfoMsg.append("C");
                    break;
                case "Heater":
                    InfoMsg.append("H");
                    break;
            }
            InfoMsg.append(",");

            if (RelayDb.Get_Mode().equals("Auto"))
                InfoMsg.append("A");
            else if (RelayDb.Get_Mode().equals("Timer"))
                InfoMsg.append("T");
            InfoMsg.append(",");

            char [] Sensors = RelayDb.Get_Sensors().toCharArray();
            int SensorData = 0;
            for (int j = 0; j < Sensors.length; j+=2) {
                int Num = Integer.parseInt(String.valueOf(Sensors[j]));
                SensorData += Math.pow(2, Num - 1);

            }
            InfoMsg.append(SensorData)
                    .append(",");

        }
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
