package ir.myapp.controller3.settings;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

public class Settings {
    private Context context;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private static final String PREFER_NAME = "settings";
    private static final String REMOTE_PHONE_NUMBER = "phone_number";
    private static final String PASSWORD = "password";

    public Settings(Context context) {
        this.context = context;
        setPreferences();

    }

    private void setPreferences(){
        preferences = context.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }



    public String getRemotePhoneNumber(){
        String phoneNumber = preferences.getString(REMOTE_PHONE_NUMBER, "+989366272656");
        if (phoneNumber.equals("")){
            Toast.makeText(this.context, "Remote phone number is not set", Toast.LENGTH_SHORT).show();
        }
        return phoneNumber;
    }


    public void setRemotePhoneNumber(String phoneNumber){
        editor.putString(REMOTE_PHONE_NUMBER, phoneNumber);
        editor.commit();
    }

    public String getPassword(){
        return preferences.getString(PASSWORD, "1111");
    }

}
