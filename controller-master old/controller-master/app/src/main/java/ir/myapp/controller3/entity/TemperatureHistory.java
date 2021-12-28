package ir.myapp.controller3.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class TemperatureHistory{
    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo
    private int sensorId;

    @ColumnInfo
    private int value;

    @ColumnInfo
    private long saveDate;

    public TemperatureHistory(int sensorId, int value, long saveDate) {
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

    public int getSensorId() {
        return sensorId;
    }

    public void setSensorId(int sensorId) {
        this.sensorId = sensorId;
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
}