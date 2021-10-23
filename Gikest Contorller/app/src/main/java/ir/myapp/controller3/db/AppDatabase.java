package ir.myapp.controller3.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import ir.myapp.controller3.R;
import ir.myapp.controller3.dao.Relay_Dao;
import ir.myapp.controller3.dao.Sensor_Dao;
import ir.myapp.controller3.dao.System_Dao;
import ir.myapp.controller3.entity.Relay_Struct;
import ir.myapp.controller3.entity.Sensor_Struct;
import ir.myapp.controller3.entity.System_Struct;

@Database(entities = {System_Struct.class, Relay_Struct.class, Sensor_Struct.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase appDatabase;
    public static AppDatabase getInstance(Context context){
        if (appDatabase == null){
            appDatabase = Room.databaseBuilder(context, AppDatabase.class, context.getString(R.string.app_name)).allowMainThreadQueries().build();
        }
        return appDatabase;
    }
    public abstract ir.myapp.controller3.dao.Sensor_Dao Sensor_Dao();
    public abstract ir.myapp.controller3.dao.Relay_Dao Relay_Dao();
    public abstract ir.myapp.controller3.dao.System_Dao System_Dao();
}
