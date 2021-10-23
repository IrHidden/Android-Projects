package ir.MasjedEmamAli.Urmia.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity
public class UserStruct {


    @PrimaryKey
    @NonNull
    String Username;
    @ColumnInfo
    String Password;


    public UserStruct(String Username, String Password) {
        this.Username = Username;
        this.Password = Password;

    }

    //
    public String Username() {
        return Username;
    }

    //
    public String Password() {
        return Password;
    }
}