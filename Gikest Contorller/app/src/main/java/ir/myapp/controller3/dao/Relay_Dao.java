package ir.myapp.controller3.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import ir.myapp.controller3.entity.Relay_Struct;

@Dao
public interface Relay_Dao {

    @Query("SELECT * FROM Relay_Struct")
    List<Relay_Struct> Get_All();

    @Query("SELECT * FROM Relay_Struct WHERE Place= :Place  ORDER BY Data_Num DESC LIMIT 1")
    Relay_Struct GetLast_WithPlace(int Place);

    @Query("Delete FROM Relay_Struct")
    void Delete_All(); //be careful...

    @Query("SELECT Count(*) FROM Relay_Struct")
    int Data_Count();

    @Insert
    void Insert(Relay_Struct... relay);

    @Insert
    void InsertList(List<Relay_Struct> relay);
}
