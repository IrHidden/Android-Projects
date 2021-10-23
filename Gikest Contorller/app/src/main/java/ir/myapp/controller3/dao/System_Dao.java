package ir.myapp.controller3.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import ir.myapp.controller3.entity.Sensor_Struct;
import ir.myapp.controller3.entity.System_Struct;

@Dao
public interface System_Dao {

    @Query("SELECT * FROM System_Struct Order By Data_Num Desc Limit 1")
    System_Struct GetLast();

    @Query("Delete FROM System_Struct")
    void Delete_All(); //be careful...

    @Insert
    void Insert(System_Struct... system);

    @Insert
    void InsertList(List<Sensor_Struct> system);


}
