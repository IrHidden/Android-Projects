package ir.myapp.controller3.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.Date;
import java.util.List;

import ir.myapp.controller3.entity.HumidityHistory;

@Dao
public interface HumidityHistoryDao {

    @Query("SELECT * FROM humidityhistory")
    List<HumidityHistory> getAll();


    @Query("SELECT * FROM HUMIDITYHISTORY where saveDate >= :date")
    List<HumidityHistory> getAfterDate(long date);

    @Insert
    void insertAll(HumidityHistory... histories);

    @Insert
    void insertAll(List<HumidityHistory> histories);

    @Delete
    void delete(HumidityHistory history);
}
