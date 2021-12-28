package ir.myapp.controller3.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import ir.myapp.controller3.R;
import ir.myapp.controller3.dao.HumidityHistoryDao;
import ir.myapp.controller3.dao.TemperatureHistoryDao;
import ir.myapp.controller3.entity.HumidityHistory;
import ir.myapp.controller3.entity.TemperatureHistory;

@Database(entities = {HumidityHistory.class, TemperatureHistory.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase appDatabase;
    public static AppDatabase getInstance(Context context){
        if (appDatabase == null){
            appDatabase = Room.databaseBuilder(context, AppDatabase.class, context.getString(R.string.app_name)).allowMainThreadQueries().build();
        }
        return appDatabase;
    }

    public abstract HumidityHistoryDao humidityHistoryDao();
    public abstract TemperatureHistoryDao temperatureHistoryDao();

}
