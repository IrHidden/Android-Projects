package ir.myapp.controller3.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import ir.myapp.controller3.entity.Sensor_Struct;

@Dao
public interface Sensor_Dao {

    @Query("SELECT * FROM Sensor_Struct ")
    List<Sensor_Struct> Get_All();

    @Query("SELECT * FROM Sensor_Struct WHERE Place= :Place  ORDER BY Data_Num DESC LIMIT 1")
    Sensor_Struct getLast_WithPlace(int Place);

    @Query("Delete FROM Sensor_Struct")
    void Delete_All(); //be careful...

    @Query("SELECT Count(*) FROM Sensor_Struct")
    int Data_Count();

    @Query("SELECT * FROM Sensor_Struct ORDER BY Data_Num DESC LIMIT :Sensors_Num")
    List<Sensor_Struct> Temperature_Avg(int Sensors_Num);

    @Query("SELECT * FROM Sensor_Struct ORDER BY Data_Num DESC LIMIT :Sensors_Num")
    List<Sensor_Struct> Humidity_Avg(int Sensors_Num);

    @Insert
    void Insert(Sensor_Struct... sensor);

    @Insert
    void InsertList(List<Sensor_Struct> sensor);


}
