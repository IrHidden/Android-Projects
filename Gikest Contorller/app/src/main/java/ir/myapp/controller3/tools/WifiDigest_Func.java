package ir.myapp.controller3.tools;

import android.content.Context;

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


    private void DigestMsg(char[] Msg_Parts, AppDatabase db) {
        //Format Msg Hum,Temp,Hum,Temp,Hum,Temp,RelayState.
        //for DisConnect 120
        //for Error 110
        Sensor_Struct sensor;
        if (Msg_Parts[0] == 120 || Msg_Parts[1] == 120)
            sensor = new Sensor_Struct(1, "Disconnected", Msg_Parts[0], Msg_Parts[1], ComFuc.getCurrentTime());
        else if (Msg_Parts[0] == 110 || Msg_Parts[1] == 110)
            sensor = new Sensor_Struct(1, "Error", Msg_Parts[0], Msg_Parts[1], ComFuc.getCurrentTime());
        else
            sensor = new Sensor_Struct(1, "Normal", Msg_Parts[0], Msg_Parts[1], ComFuc.getCurrentTime());
        db.Sensor_Dao().Insert(sensor);


        if (Msg_Parts[3] == 120 || Msg_Parts[4] == 120)
            sensor = new Sensor_Struct(2, "Disconnected", Msg_Parts[3], Msg_Parts[4], ComFuc.getCurrentTime());
        else if (Msg_Parts[3] == 110 || Msg_Parts[4] == 110)
            sensor = new Sensor_Struct(2, "Error", Msg_Parts[3], Msg_Parts[4], ComFuc.getCurrentTime());
        else
            sensor = new Sensor_Struct(2, "Normal", Msg_Parts[3], Msg_Parts[4], ComFuc.getCurrentTime());
        db.Sensor_Dao().Insert(sensor);

        if (Msg_Parts[6] == 120 || Msg_Parts[7] == 120)
            sensor = new Sensor_Struct(3, "Disconnected", Msg_Parts[6], Msg_Parts[7], ComFuc.getCurrentTime());
        else if (Msg_Parts[6] == 110 || Msg_Parts[7] == 110)
            sensor = new Sensor_Struct(3, "Error", Msg_Parts[6], Msg_Parts[7], ComFuc.getCurrentTime());
        else
            sensor = new Sensor_Struct(3, "Normal", Msg_Parts[6], Msg_Parts[7], ComFuc.getCurrentTime());
        db.Sensor_Dao().Insert(sensor);
    }

    private void DigestMsg_old(char[] Msg_Parts, AppDatabase db)
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
