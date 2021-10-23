package ir.myapp.controller3.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity
public class Sensor_Struct {
    @PrimaryKey(autoGenerate = true)
    public int Data_Num;

    @ColumnInfo
    public String Mode; //Normal, Error, Disconnected

    @ColumnInfo
    public int Place; //1, 2, 3, 4, 5

    @ColumnInfo
    public int Value_T; //Temperature Value

    @ColumnInfo
    public int Value_H; //Humidity Value

    @ColumnInfo
    public String Data_Time; //Time That Data is Received


    public Sensor_Struct(int Place, String Mode, int Value_T, int Value_H, String Data_Time) {
        this.Place = Place;
        this.Mode = Mode;
        this.Value_T = Value_T;
        this.Value_H = Value_H;
        this.Data_Time = Data_Time;
    }

    //
    public int Get_Num() {
        return Data_Num;
    }

    //
    public int Get_Place() {
        return Place;
    }

    //
    public String Get_Mode() {
        return Mode;
    }

    //
    public int Get_ValueT() {
        return Value_T;
    }

    //
    public int Get_ValueH() {
        return Value_H;
    }

    //
    public String Get_DataTime() {
        return Data_Time;
    }
}