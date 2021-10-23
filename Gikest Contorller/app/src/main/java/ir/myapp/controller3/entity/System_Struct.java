package ir.myapp.controller3.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity
public class System_Struct {
    @PrimaryKey(autoGenerate = true)
    public int Data_Num;
    @ColumnInfo
    public  String Username; //Ali, Hamed, ...
    @ColumnInfo
    public String System_Power; //on, off

    @ColumnInfo
    public String System_Log_State; //in, out

    @ColumnInfo
    public int Sensor_num; //1, 2, 3, 4, 5

    @ColumnInfo
    public int Relay_num; //Mostly a Number With Mood 5

    @ColumnInfo
    public int Temp_val;

    @ColumnInfo
    public int Hum_val;

    public System_Struct(String System_Power,String System_Log_State, int Sensor_num, int Relay_num, int Temp_val, int Hum_val) {
        this.System_Power = System_Power;
        this.System_Log_State=System_Log_State;
        this.Sensor_num = Sensor_num;
        this.Relay_num = Relay_num;
        this.Hum_val = Hum_val;
        this.Temp_val = Temp_val;
    }

    //
    public int Get_Num() {
        return Data_Num;
    }

    //
    public String Get_SystemPower() {
        return System_Power;
    }

    //
    public String Get_SystemLogState(){ return System_Log_State; }

    //
    public int Get_SensorNum() {
        return Sensor_num;
    }

    //
    public int Get_RelayNum() {
        return Relay_num;
    }

    //

    //
    public int Get_Humidity() {
        return Hum_val;
    }
}