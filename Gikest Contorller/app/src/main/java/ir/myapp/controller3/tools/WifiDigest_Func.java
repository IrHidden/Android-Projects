package ir.myapp.controller3.tools;

import android.content.Context;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import ir.myapp.controller3.db.AppDatabase;
import ir.myapp.controller3.entity.Relay_Struct;
import ir.myapp.controller3.entity.Sensor_Struct;

public class  WifiDigest_Func {


    CommenFuntions ComFuc;

    public WifiDigest_Func(String Msg, Context cont) {
        ComFuc=new CommenFuntions();
        Msg = Msg.trim();
        char[] Msg_Parts = Msg.toCharArray();
        AppDatabase db = AppDatabase.getInstance(cont);

        DigestMsg(Msg_Parts, db);
    }


    private void DigestMsg(char[] Msg_Parts,AppDatabase db)
    {
        //Format Msg: Hum ( 2 Digit ),Temp ( 2 Digit );state,state,state,state,state.
        //Format Msg: Hum (2 Num),Temp(2 Num);

        int i=0;
        int Humidity,Temprature;

        StringBuilder Temp=new StringBuilder();
        while (Msg_Parts[i] != ',') //the first part is to digest Hum
            Temp.append(Msg_Parts[i++]);
        Humidity=Integer.parseInt(String.valueOf(Temp));

        i++; //cross Over ','

        Temp=new StringBuilder();
        while (Msg_Parts[i] != ';') //the first part is to digest Temp
            Temp.append(Msg_Parts[i++]);
        Temprature=Integer.parseInt(String.valueOf(Temp));

        i++;
        Sensor_Struct snc;
        snc =new Sensor_Struct( 1, "Normal", Temprature, Humidity, ComFuc.getCurrentTime());
        db.Sensor_Dao().Insert(snc);

        int place = 1;
        while(Msg_Parts[i] != '.')
        {
            Relay_Struct relay;

            Boolean State;
            if(Msg_Parts[i]=='1')
                State=true;
            else
                State=false;

            if(place%2!=0) //Temprature
                relay = new Relay_Struct(place++, "Heater", "Auto", State, Temprature, 21, 3, "All", ComFuc.getCurrentTime());
            else //Humidity
                relay = new Relay_Struct(place++, "Vaporizer", "Auto", State, Humidity, 60, 10, "All", ComFuc.getCurrentTime());

            db.Relay_Dao().Insert(relay);
            i+=2;
        }


    }
}
