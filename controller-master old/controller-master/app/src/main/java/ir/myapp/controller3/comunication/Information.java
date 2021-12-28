package ir.myapp.controller3.comunication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

import ir.myapp.controller3.tools.Utils;

public abstract class Information {
    Context context;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    public enum PreferenceNames {Backup, New}

    final String PREFER_NAME;

    private static final String KEY_LIGHT_ON_OFF = "LightOnOff";
    private static final String KEY_SENSORS_TEMPERATURE_VALUES = "SensorsTemperatureValues";
    private static final String KEY_SENSORS_HUMIDITY_VALUES = "SensorsHumidityValues";
    private static final String KEY_HEATERS_EXPECTED_VALUES = "HeatersExpectedValues";
    private static final String KEY_HEATERS_ON_OR_OFF = "HeatersOnOrOff";
    private static final String KEY_VAPOURS_ON_OR_OFF = "VapoursOnOrOff";
    private static final String KEY_VAPOURS_EXPECTED_VALUES = "VapoursExpectedValues";
    private static final String KEY_AC_ON_OFF_TIMES = "ACOnOffTimes";
    private static final String KEY_AC_ON_OFF = "ACOnOff";
    private static final String KEY_POWER_ON_OFF = "powerOnOff";

    private static final String KEY_AUTO_MODE = "AutoMode";


    public static final String REFRESHING = "REFRESHING";

    public Information(Context context, PreferenceNames preferenceName) {
        this.PREFER_NAME = preferenceName.name();
        this.context = context;
        setPreferences();
    }

    @SuppressLint("CommitPrefEdits")
    private void setPreferences(){
        preferences = context.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public SharedPreferences getPreferences(){
        return preferences;
    }


    public void cleanPreferences(){
        editor.clear();
        editor.commit();
    }

    public void setAutoMode(boolean autoMode){
        editor.putBoolean(KEY_AUTO_MODE, autoMode);
        editor.commit();
    }

    public boolean setAutoMode(){
        return preferences.getBoolean(KEY_AUTO_MODE, false);
    }

    public Boolean getPowerOnOff(){
        return preferences.getBoolean(KEY_POWER_ON_OFF, true);
    }

    public void setPowerOnOff(Boolean onOff){
        editor.putBoolean(KEY_POWER_ON_OFF, onOff);
        editor.commit();
    }

    public int getSensorsCount(){
        return getSensorsTemperatures().size();
    }

    public void setSensorsTemperature(List<Integer> temperatures){
        String STemperatures = "";
        for (int temp : temperatures){
            STemperatures += temp + ";";
        }
        editor.putString(KEY_SENSORS_TEMPERATURE_VALUES, STemperatures);
        editor.commit();
    }

    public List<Integer> getSensorsTemperatures() {
        String STemperatures = preferences.getString(KEY_SENSORS_TEMPERATURE_VALUES, "0");
        List<Integer> ans = new ArrayList<>();
        for (String s : STemperatures.split(";")) {
            if (!s.equals("")) {
                ans.add(Integer.parseInt(s));
            }
        }
        return ans;
    }

    public String getAVGSensorsTemperatures() {
        List<Integer> values = getSensorsTemperatures();
        int sum = 0;
        for (Integer val:values){
            sum += val;
        }
        if (sum != 0) {
            return String.valueOf((int) (sum / values.size()));
        }else{
            return  "0";
        }    }





    public void setSensorsHumidity(List<Integer> humidities){
        String SHumidities = "";
        for (int temp : humidities){
            SHumidities += temp + ";";
        }
        editor.putString(KEY_SENSORS_HUMIDITY_VALUES, SHumidities);
        editor.commit();
    }

    public List<Integer> getSensorsHumidity() {
        String SHumidities = preferences.getString(KEY_SENSORS_HUMIDITY_VALUES, "");
        List<Integer> ans = new ArrayList<>();
        for (String s : SHumidities.split(";")) {
            if (!s.equals("")) {
                ans.add(Integer.valueOf(s));
            }
        }
        return ans;
    }

    public String getAVGSensorsHumidity() {
        List<Integer> values = getSensorsHumidity();
        int sum = 0;
        for (Integer val:values){
            sum += val;
        }
        if (sum != 0) {
            return String.valueOf((int) (sum / values.size()));
        }else{
            return  "0";
        }
    }


    public void setExpectedHeatersTemps(List<Integer> temps){
        editor.putString(KEY_HEATERS_EXPECTED_VALUES, Utils.convertToCommaSeperated(temps));
        editor.commit();
    }



    public List<Integer> getExpectedHeatersTemps(){
        return Utils.getIntegerListFromCommaSeperated(preferences.getString(KEY_HEATERS_EXPECTED_VALUES, "0"));
    }

    public String getAVGHeatersExpectedTemp(){
        List<Integer> values = getExpectedHeatersTemps();
        float sum = 0;
        for (Integer val:values){
            sum += val;
        }
        if (sum != 0) {
            return String.valueOf((int) (sum / values.size()));
        }else{
            return  "0";
        }    }

    public List<Boolean> getHeatersOnOrOff(){
        String SOnOff = preferences.getString(KEY_HEATERS_ON_OR_OFF, "0");
        List<Boolean> ans = new ArrayList<>();
        for (String s : SOnOff.split(";")) {
            if (!s.equals("")) {
                ans.add(s.equals("1"));
            }
        }
        return ans;
    }

    public void setHeatersOnOff(List<Boolean> values){
        String ans = "";
        for (boolean b:values){
            ans += b ? "1;": "0;";
        }
        editor.putString(KEY_HEATERS_ON_OR_OFF, ans);
        editor.commit();
    }

    public List<Boolean> getVapoursOnOrOff(){
        String SOnOff = preferences.getString(KEY_VAPOURS_ON_OR_OFF, "0");
        List<Boolean> ans = new ArrayList<>();
        for (String s : SOnOff.split(";")) {
            if (!s.equals("")) {
                ans.add(s.equals("1"));
            }
        }
        return ans;
    }

    public void setVapoursOnOff(List<Boolean> values){
        String ans = "";
        for (boolean b:values){
            ans += b ? "1;": "0;";
        }
        editor.putString(KEY_VAPOURS_ON_OR_OFF, ans);
        editor.commit();
    }



    public void setExpectedVapoursHumidities(List<Integer> humidities){
        editor.putString(KEY_VAPOURS_EXPECTED_VALUES, Utils.convertToCommaSeperated(humidities));
        editor.commit();
    }




    public List<Integer> getExpectedVapoursHumidities(){
        return Utils.getIntegerListFromCommaSeperated(preferences.getString(KEY_VAPOURS_EXPECTED_VALUES, "0"));
    }


    public String getAVGVapoursExpectedHumidity(){
        List<Integer> values = getExpectedVapoursHumidities();
        float sum = 0;
        for (Integer val:values){
            sum += val;
        }
        return String.valueOf((int)(sum/values.size()));
    }


    public void setACOnOffTimes(List<String[]> onOffTimes){
        String value = "";
        for (String[] s: onOffTimes){
            value+= s[0];
            value+= "-";
            value+= s[1];
            value+= ",";
        }
        editor.putString(KEY_AC_ON_OFF_TIMES, value);
        editor.commit();
    }

    public List<String[]> getACOnOffTimes(){
        List<String[]> onOffTimes = new ArrayList<>();
        String value = preferences.getString(KEY_AC_ON_OFF_TIMES, "00:00-12:00");
        for(String s: value.split(",")){
            onOffTimes.add(s.split("-"));
        }
        return  onOffTimes;
    }

    public boolean getACOnOrOff(){
        return  preferences.getBoolean(KEY_AC_ON_OFF, false);
    }

    public void setACOnOrOff(boolean onOrOff){
        editor.putBoolean(KEY_AC_ON_OFF, onOrOff);
        editor.commit();
    }


    public void setLightOnOrOff(boolean status){
        editor.putBoolean(KEY_LIGHT_ON_OFF, status);
        editor.commit();
    }

    public boolean getLightOnOrOff(){
        return  preferences.getBoolean(KEY_LIGHT_ON_OFF, false);
    }

    public boolean getRefreshing(){
        return preferences.getBoolean(REFRESHING, false);
    }

    public void setRefreshing(boolean val){
        editor.putBoolean(REFRESHING, val);
        editor.commit();
    }



    public abstract void fetchAllFromRemote();
    public abstract void sendStatus(String type, boolean status);
    public abstract void sendValue(String type, String value);
    public abstract void sendAll();


    interface SettingListener{
        void onRefreshed();
    }
    private SettingListener listener = null;
    public void setListener(SettingListener listener){
        this.listener = listener;
    }

    public void refresh(){
        fetchAllFromRemote();
        if (this.listener != null)
            this.listener.onRefreshed();
    }

}
