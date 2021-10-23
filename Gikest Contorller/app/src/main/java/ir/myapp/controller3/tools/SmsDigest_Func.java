package ir.myapp.controller3.tools;

import android.content.Context;
import android.util.Log;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ir.myapp.controller3.db.AppDatabase;
import ir.myapp.controller3.entity.Relay_Struct;
import ir.myapp.controller3.entity.Sensor_Struct;

public class SmsDigest_Func {

    CommenFuntions ComFuc;

    public SmsDigest_Func(String SMS, Context cont)
    {
        ComFuc=new CommenFuntions();
        int[] Data=new int[3]; //Data[1] for index of Sms, Data[2] for SensorNum, Data[3] for RelayNum
        SMS = SMS.trim();
        char[] SMS_Parts=SMS.toCharArray();
        AppDatabase db=AppDatabase.getInstance(cont);

        //Starting to digest the data for Sensors
        Data=Sensor_Digest(SMS_Parts,db);
        //Now starting to digest the data for relays
        Data=Relay_Digest(SMS_Parts,db,Data);
    }

    private int[] Sensor_Digest(char[] SMS_Parts, AppDatabase db)
    {
        List <Integer> tmp =new ArrayList<>();
        List <Integer> hum=new ArrayList<>();
        int i=0;
        if(SMS_Parts[i]=='H') {
            i += 2; //to pass the H and ':'
            while (SMS_Parts[i] != ';') //if it see's ';' loop will be over and will move to 'T'
            {
                StringBuilder h=new StringBuilder();
                while (SMS_Parts[i] != ',' && SMS_Parts[i] != ';') //the first part is to digest each Hum
                    h.append(SMS_Parts[i++]);

                if(SMS_Parts[i] == ',')
                    i++;
                hum.add(Integer.parseInt(String.valueOf(h)));

            }
            i++;
        }

        if(SMS_Parts[i]=='T') {
            i += 2; //to pass the T and ':'
            while (SMS_Parts[i] != '.') //if it see's ';' loop will be over and will move to ACs'
            {
                StringBuilder t= new StringBuilder();
                while (SMS_Parts[i] != ',' && SMS_Parts[i] != '.') //the first part is to digest each Temp
                    t.append(SMS_Parts[i++]);

                if(SMS_Parts[i] == ',')
                    i++;

                tmp.add(Integer.parseInt(String.valueOf(t)));
            }
        }

        if(SMS_Parts[i]=='.') //Adding Data to Database
        {
            for (int index=0;index<hum.size();index++) {
                Sensor_Struct snc;
                switch (tmp.get(index)) {
                    case 200:
                        snc = new Sensor_Struct(index + 1, "Disconnected", tmp.get(index), hum.get(index),ComFuc.getCurrentTime());
                        break;
                    case 150:
                        snc = new Sensor_Struct(index + 1, "Error", tmp.get(index), hum.get(index),ComFuc.getCurrentTime());
                        break;
                    default:
                        snc = new Sensor_Struct(index + 1, "Normal", tmp.get(index), hum.get(index),ComFuc.getCurrentTime());
                        break;
                }
                db.Sensor_Dao().Insert(snc);
            }
            i++;    //for Moving to the 'A' Char in Next Loop
        }
        int [] Data=new int[]{i,hum.size(),0};
        return Data;

    }

    private int[] Relay_Digest(char [] SMS_Parts,AppDatabase db, int[] Data)
    {
        int i=Data[0];
        List <Relay_Struct> relays=new ArrayList<>();

        while(SMS_Parts[i]!='*') {
            i += 3;  //to pass the A, 'number of act' and ':' like 'A1:'
            Relay_Struct relay = null;
            //Relay Props Start
            int place = 1;
            String nm = "Not Selected";
            boolean st = false;
            String mode = "None";
            int val = 0, val_g = 0, tol = 0;
            StringBuilder sens;
            //Relay Props End

            while (SMS_Parts[i] != '.') {

                if (SMS_Parts[i] == 'R') {
                    relay = new Relay_Struct(place++, "None", "None", false, 0, 0, 0, null,ComFuc.getCurrentTime());
                    db.Relay_Dao().Insert(relay);
                    i += 3; //This is to Cross Over 'Zero' and 'Semicolon' After 'R' Like 'R0;'
                } else {
                    //Specify mode of the Relay
                    switch (SMS_Parts[i]) {
                        case 'H':
                            nm = "Heater";
                            i++;
                            break;
                        case 'V':
                            nm = "Vaporizer";
                            i++;
                            break;
                        case 'C':
                            nm = "Circulator";
                            i++;
                            break;
                    }

                    //Getting to know if Relay if On or Off
                    switch (SMS_Parts[i]) {
                        case '1':
                            st = true;
                            i++;
                            break;
                        case '0':
                            st = false;
                            i++;
                            break;
                    }

                    //Specify mechanism of Relay
                    if (SMS_Parts[i] == 'A') {
                        i++;
                        mode = "Auto";

                        StringBuilder temp = new StringBuilder();
                        while (SMS_Parts[i] != ',') //Value of of Hum Goal or Temp Goal
                            temp.append(SMS_Parts[i++]);
                        i++;
                        val_g = Integer.parseInt(String.valueOf(temp));

                        temp = new StringBuilder();
                        while (SMS_Parts[i] != ',') //Value of Tolerance
                            temp.append(SMS_Parts[i++]);
                        i++;
                        tol = Integer.parseInt(String.valueOf(temp));

                        sens = new StringBuilder();
                        List <Integer> sens_place=new ArrayList<>();
                        while (SMS_Parts[i + 1] != ';') //Collect Sensors' Attached to Relay
                        {
                            sens.append(SMS_Parts[++i]);
                            sens_place.add((Integer.valueOf(SMS_Parts[i])));


                        }
                        i += 2;
                        for (int index = 0; index < sens_place.size(); index++) {
                            if (nm=="Vaporizer")
                                val+=db.Sensor_Dao().GetLast_WithPlace(sens_place.get(index)).Get_ValueH();
                            else if(nm=="Circulator" || nm=="Heater")
                                val+=db.Sensor_Dao().GetLast_WithPlace(sens_place.get(index)).Get_ValueT();
                        }
                        val/=sens_place.size();
                        relay = new Relay_Struct(place++, nm, mode, st, val, val_g, tol, String.valueOf(sens), ComFuc.getCurrentTime());
                    } else if (SMS_Parts[i] == 'T') {
                        i++;
                        mode = "Timer";
                        StringBuilder temp = new StringBuilder();
                        while (SMS_Parts[i] != '-') //Val of Timer
                            temp.append(SMS_Parts[i++]);
                        i++;
                        val = Integer.parseInt(String.valueOf(temp));

                        temp = new StringBuilder();
                        while (SMS_Parts[i] != ';') //Val_G of Timer
                            temp.append(SMS_Parts[i++]);
                        i++;
                        val_g = Integer.parseInt(String.valueOf(temp));
                        relay = new Relay_Struct(place++, nm, mode, st, val, val_g, tol, "None", ComFuc.getCurrentTime());
                    }
                    db.Relay_Dao().Insert(relay);
                }

            }
            i++;
            relays.add(relay);
        }

        Data[0]=i;
        Data[2]=relays.size();
        return Data;
    }
}
