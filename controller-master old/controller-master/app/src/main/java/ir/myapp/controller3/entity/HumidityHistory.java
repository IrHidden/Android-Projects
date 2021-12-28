package ir.myapp.controller3.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;



@Entity
public class HumidityHistory{
    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo
    public int sensorId;

    @ColumnInfo
    public int value;

    @ColumnInfo
    public long saveDate;

    public HumidityHistory(int sensorId, int value, long saveDate) {
        this.sensorId = sensorId;
        this.value = value;
        this.saveDate = saveDate;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public long getSaveDate() {
        return saveDate;
    }

    public void setSaveDate(long saveDate) {
        this.saveDate = saveDate;
    }

    public int getSensorId() {
        return sensorId;
    }

    public void setSensorId(int sensorId) {
        this.sensorId = sensorId;
    }
}