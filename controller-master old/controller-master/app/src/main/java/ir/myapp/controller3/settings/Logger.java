package ir.myapp.controller3.settings;

import android.content.Context;
import android.content.SharedPreferences;

public class Logger {
    private Context context;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private static final String PREFER_NAME = "Logs";
    private static final String KEY_LOGS = "Logs";

    public Logger(Context context) {
        this.context = context;
        setPreferences();

    }

    private void setPreferences(){
        preferences = context.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void addLog(String log){
        editor.putString(KEY_LOGS, preferences.getString(KEY_LOGS, "")+"\n"+log);
        editor.commit();
    }

    public void clearLogs(){
        editor.clear();
        editor.commit();
    }

    public String getLogs(){
        return preferences.getString(KEY_LOGS, "");
    }
}
