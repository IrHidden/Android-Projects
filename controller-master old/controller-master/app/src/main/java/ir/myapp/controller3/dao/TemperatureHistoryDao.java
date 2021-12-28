package ir.myapp.controller3.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import ir.myapp.controller3.entity.TemperatureHistory;

@Dao
public interface TemperatureHistoryDao {

    @Query("SELECT * FROM temperaturehistory WHERE saveDate >= :date")
    List<TemperatureHistory> getAfterDate(double date);

    @Insert
    void insert(TemperatureHistory temperatureHistory);

    @Insert
    void insertAll(List<TemperatureHistory> temperatureHistories);

    @Query("SELECT * FROM temperaturehistory ORDER BY saveDate desc limit 1")
    TemperatureHistory getLast();
}
