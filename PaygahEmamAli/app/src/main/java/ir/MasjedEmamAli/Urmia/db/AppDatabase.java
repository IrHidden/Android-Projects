package ir.MasjedEmamAli.Urmia.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.MasjedEmamAli.Urmia.R;

import ir.MasjedEmamAli.Urmia.dao.UserDao;
import ir.MasjedEmamAli.Urmia.entity.UserStruct;


@Database(entities = {UserStruct.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase appDatabase;
    public static AppDatabase getInstance(Context context){
        if (appDatabase == null){
            appDatabase = Room.databaseBuilder(context, AppDatabase.class, context.getString(R.string.app_name)).allowMainThreadQueries().build();
        }
        return appDatabase;
    }
    public abstract UserDao UserDao();
}
