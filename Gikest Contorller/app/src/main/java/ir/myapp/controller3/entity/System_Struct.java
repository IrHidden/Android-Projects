package ir.myapp.controller3.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


@Entity
public class System_Struct {

    @PrimaryKey(autoGenerate = true)
    public int Data_Num;

    @ColumnInfo
    public  String Username; //Ali, Hamed, ...

    @ColumnInfo
    public String System_Power; //on, off

    @ColumnInfo
    public String System_Login_State; //in, out

    @ColumnInfo
    public float Data_RefreshRate; //Data Transfer Rate(Minute)

    @ColumnInfo
    public String PhoneNumber1; //PhoneNumber for Power off in System;

    @ColumnInfo
    public String PhoneNumber2; //PhoneNumber for Power off in System;

    @ColumnInfo
    public String PhoneNumber3; //PhoneNumber for Power off in System;

    @ColumnInfo
    public int Sensor_num; //1, 2, 3, 4, 5

    @ColumnInfo
    public int Relay_num; //Mostly a Number With Mood 5

    @ColumnInfo
    public String Relays; //String of a Json File That Shows Relays

    @ColumnInfo
    public int Temp_val;  // Goal Temperature of System

    @ColumnInfo
    public int Hum_val; // Goal Humidity of System

    @ColumnInfo
    public int Temp_Tolerance; // Temperature Tolerance

    @ColumnInfo
    public int Hum_Tolerance; // Humidity Tolerance

    public System_Struct(String System_Power,
                         String System_Login_State,
                         float Data_RefreshRate,
                         String PhoneNumber1,
                         String PhoneNumber2,
                         String PhoneNumber3,
                         int Sensor_num,
                         int Relay_num,
                         String Relays,
                         int Temp_val,
                         int Hum_val,
                         int Temp_Tolerance,
                         int Hum_Tolerance) {

        this.System_Power = System_Power;
        this.System_Login_State=System_Login_State;
        this.Data_RefreshRate = Data_RefreshRate;
        this.PhoneNumber1 = PhoneNumber1;
        this.PhoneNumber2 = PhoneNumber2;
        this.PhoneNumber3 = PhoneNumber3;
        this.Sensor_num = Sensor_num;
        this.Relay_num = Relay_num;
        this.Relays = Relays;
        this.Hum_val = Hum_val;
        this.Temp_val = Temp_val;
        this.Temp_Tolerance = Temp_Tolerance;
        this.Hum_Tolerance = Hum_Tolerance;
    }

    public int Get_Num() {
        return Data_Num;
    }

    public String Get_SystemPower() {
        return System_Power;
    }

    public String Get_SystemLogState(){ return System_Login_State; }

    public float Get_DataRefreshRate(){ return Data_RefreshRate; }

    public String Get_PhoneNumber1(){ return PhoneNumber1; }

    public String Get_PhoneNumber2(){ return PhoneNumber2; }

    public String Get_PhoneNumber3(){ return PhoneNumber3; }

    public int Get_SensorNum() {
        return Sensor_num;
    }

    public int Get_RelayNum() {
        return Relay_num;
    }

    public JSONObject Get_Relays(){
        JSONObject json = null;
        try {
            json = new JSONObject(Relays);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    public int Get_TempValue() {
        return Temp_val;
    }

    public int Get_HumValue() {
        return Hum_val;
    }

    public int Get_TempTolerance() {
        return Temp_Tolerance;
    }

    public int Get_HumTolerance() {
        return Hum_Tolerance;
    }

}