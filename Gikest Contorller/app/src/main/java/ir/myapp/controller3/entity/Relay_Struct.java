package ir.myapp.controller3.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity
public class Relay_Struct {

    @PrimaryKey(autoGenerate = true)
    public int Data_Num;

    @ColumnInfo
    public String Name;  //Types: None, Heater, Vaporizer, Circulator

    @ColumnInfo
    public String Mode;  //Types: Auto, Timer, None

    @ColumnInfo
    public int Place; //1, 2, 3, 4, 5, ...

    @ColumnInfo
    public boolean State; //True, False

    @ColumnInfo
    public float ValueG; //Goal Temperature or Humidity Value

    @ColumnInfo
    public int OffTime; //Goal Temperature or Humidity Value

    @ColumnInfo
    public int OnTime; //Temperature or Humidity Tolerance

    @ColumnInfo
    public String Sensors; //Sensors Number That Relay Will Works with: 1-2-3,4-31,14-7,1,2,None

    @ColumnInfo
    public String Data_Time; //Time That Data is Received


    public Relay_Struct(int Place, String Name, String Mode, boolean State,float ValueG, int OnTime, int OffTime, String Sensors, String Data_Time) {
        this.Place = Place;
        this.Mode = Mode;
        this.Name = Name;
        this.State = State;
        this.ValueG = ValueG;
        this.OffTime = OffTime;
        this.OnTime = OnTime;
        this.Sensors = Sensors;
        this.Data_Time = Data_Time;
    }

    //
    public int Get_Num() {
        return Data_Num;
    }

    //
    public String Get_Name() {
        return Name;
    }

    //
    public String Get_Mode() {
        return Mode;
    }

    //
    public int Get_Place() {
        return Place;
    }

    //
    public boolean Get_State() {
        return State;
    }

    //
    public float Get_ValueG() {
        return ValueG;
    }

    //
    public int Get_OffTime() {
        return OffTime;
    }

    //
    public int Get_OnTime() {
        return OnTime;
    }

    //
    public String Get_Sensors() {
        return Sensors;
    }

    //
    public String Get_DataTime() {
        return Data_Time;
    }
}