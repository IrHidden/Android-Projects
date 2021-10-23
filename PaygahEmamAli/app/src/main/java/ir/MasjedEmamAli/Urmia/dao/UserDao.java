package ir.MasjedEmamAli.Urmia.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import ir.MasjedEmamAli.Urmia.entity.UserStruct;

@Dao
public interface UserDao {

    @Query("SELECT * FROM UserStruct")
    UserStruct Get();

    @Insert
    void Insert(UserStruct... user);

    @Query("Delete FROM UserStruct")
    void Delete_All();
}
