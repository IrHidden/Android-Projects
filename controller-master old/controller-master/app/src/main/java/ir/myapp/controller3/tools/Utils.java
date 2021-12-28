package ir.myapp.controller3.tools;

import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import ir.myapp.controller3.db.AppDatabase;
import ir.myapp.controller3.entity.HumidityHistory;
import ir.myapp.controller3.comunication.Information;
import ir.myapp.controller3.entity.TemperatureHistory;
import ir.myapp.controller3.settings.Logger;

public class Utils {

    public static <E> String convertToCommaSeperated(List<E> values) {
        List<String> sValues = new ArrayList<>();
        for (E val : values) {
            sValues.add(String.valueOf(val));
        }
        return TextUtils.join(",", sValues);
    }

    public static String convertToCommaSeperatedArray(List<String[]> values) {
        List<String> sValues = new ArrayList<>();
        for (String[] val : values) {
            sValues.add(val[0] + "-" + val[1]);
        }
        return TextUtils.join(",", sValues);
    }

    public static List<Boolean> getBooleanListFromCommaSeperated(String s) {
        List<Boolean> values = new ArrayList<>();
        for (String ss : s.split(",")) {
            values.add(ss.equals("1"));
        }
        return values;
    }

    public static List<Integer> getIntegerListFromCommaSeperated(String s) {
        List<Integer> values = new ArrayList<>();
        for (String ss : s.split(",")) {
            values.add(Integer.valueOf(ss));
        }

        return values;


    }


    public static List<Float> getFloatListFromCommaSeperated(String s) {
        List<Float> values = new ArrayList<>();
        for (String ss : s.split(",")) {
            values.add(Float.valueOf(ss));
        }

        return values;
    }


    public static void copySharedPreferences(SharedPreferences from, SharedPreferences to) {
        //sp1 is the shared pref to copy to
        SharedPreferences.Editor ed = to.edit();
        ed.clear(); // This clears the one we are copying to, but you don't necessarily need to do that.
        //Cycle through all the entries in the sp
        for (Map.Entry<String, ?> entry : from.getAll().entrySet()) {
            Object v = entry.getValue();
            String key = entry.getKey();
            //Now we just figure out what type it is, so we can copy it.
            // Note that i am using Boolean and Integer instead of boolean and int.
            // That's because the Entry class can only hold objects and int and boolean are primatives.
            if (v instanceof Boolean)
                // Also note that i have to cast the object to a Boolean
                // and then use .booleanValue to get the boolean
                ed.putBoolean(key, (Boolean) v);
            else if (v instanceof Float)
                ed.putFloat(key, (Float) v);
            else if (v instanceof Integer)
                ed.putInt(key, (Integer) v);
            else if (v instanceof Long)
                ed.putLong(key, (Long) v);
            else if (v instanceof String)
                ed.putString(key, ((String) v));
        }
        ed.commit(); //save it.
    }

    public static void deleteThread(Context context, String phoneNumber) {
        try {
            Cursor c = context.getContentResolver().query(
                    Uri.parse("content://sms/"), new String[]{
                            "_id", "thread_id", "address", "person", "date", "body"}, null, null, null);

            try {
                while (c.moveToNext()) {
                    int id = c.getInt(0);
                    String address = c.getString(2);
//                    System.out.println("address" + address);
                    if (address.equals(phoneNumber)) {
//                        System.out.println("deleting");
                        context.getContentResolver().delete(Uri.parse("content://sms/" + id), null, null);
                    }

                }
            } catch (Exception e) {

            }

        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public static void addToStatistics(){

    }


    public static void digestAndSaveSMSInfo(String message, Information information, Logger logger) {
        message = message.trim();
        if (message.startsWith("C")) {
            logger.addLog(message.replace("C", ""));
        } else {
            String[] parts = message.split(";");
            for (String p : parts) {
                if (p.startsWith("Te:")) {
                    String sValues = p.replace("T:", "");
                    List<Integer> values = Utils.getIntegerListFromCommaSeperated(sValues);
                    information.setSensorsTemperature(values);
                } else if (p.startsWith("H:")) {
                    String sValues = p.replace("H:", "");
                    List<Integer> values = Utils.getIntegerListFromCommaSeperated(sValues);
                    information.setSensorsHumidity(values);

                } else if (p.startsWith("T:")) {
                    String sValues = p.replace("T:", "");
                    List<Integer> values = Utils.getIntegerListFromCommaSeperated(sValues);
                    information.setSensorsTemperature(values);
                } else if (p.startsWith("He:")) {
                    String sValues = p.replace("He:", "");
                    String sValuesStatus = sValues.replaceAll("-\\d+", "");
                    String sValuesExpected = sValues.replaceAll("\\d-", "");
                    List<Boolean> values = Utils.getBooleanListFromCommaSeperated(sValuesStatus);
                    List<Integer> expected = Utils.getIntegerListFromCommaSeperated(sValuesExpected);
                    information.setHeatersOnOff(values);
                    information.setExpectedHeatersTemps(expected);

                } else if (p.startsWith("Va:")) {
                    String sValues = p.replace("Va:", "");
                    String sValuesStatus = sValues.replaceAll("-\\d+", "");
                    String sValuesExpected = sValues.replaceAll("\\d-", "");
                    List<Boolean> values = Utils.getBooleanListFromCommaSeperated(sValuesStatus);
                    List<Integer> expected = Utils.getIntegerListFromCommaSeperated(sValuesExpected);
                    information.setVapoursOnOff(values);
                    information.setExpectedVapoursHumidities(expected);

                } else if (p.startsWith("AC:")) {
                    String sValues = p.replace("AC:", "");
                    information.setACOnOrOff(sValues.equals("1"));
                } else if (p.startsWith("Ti:")) {
                    String[] Values = p.replace("Ti:", "").split(",");
                    List<String[]> saveValues = new ArrayList<>();
                    for (String s : Values) {
                        String val1 = s.split("-")[0];
                        String val2 = s.split("-")[1];
                        saveValues.add(new String[]{val1, val2});
                    }
                    information.setACOnOffTimes(saveValues);
                } else if (p.startsWith("L:")) {
                    String sValues = p.replace("L:", "");
                    information.setLightOnOrOff(sValues.equals("1"));
                } else if (p.startsWith("P:")) {
                    String sValues = p.replace("P:", "");
                    information.setPowerOnOff(sValues.equals("1"));
                }
            }
        }
    }

    public static boolean isMyServiceRunning(Class<?> serviceClass, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("Service status", "Running");
                return true;
            }
        }
        Log.i ("Service status", "Not running");
        return false;
    }

    public static void addToDb(Information information, Context context){
        Date date = new Date();
        AppDatabase db = AppDatabase.getInstance(context);
        TemperatureHistory last = db.temperatureHistoryDao().getLast();
        if (last != null) {
            if (date.getTime() - last.getSaveDate() < 5 * 1000) {
                // sometimes sms is received twice
                Log.d("Saving history", "ignoring sms");
                return;
            }
        }
        List<Integer> humidities = information.getSensorsHumidity();
        List<HumidityHistory> humidityHistories = new ArrayList<>();
        for(int i = 0; i < humidities.size(); i++){
            HumidityHistory humidityHistory = new HumidityHistory(i, humidities.get(i), date.getTime());
            humidityHistories.add(humidityHistory);
        }
        Log.d("Humidities size to add", ""+humidityHistories.size());
        db.humidityHistoryDao().insertAll(humidityHistories);

        List<Integer> temperatures = information.getSensorsHumidity();
        List<TemperatureHistory> temperatureHistories = new ArrayList<>();
        for(int i = 0; i < temperatures.size(); i++){
            TemperatureHistory temperatureHistory = new TemperatureHistory(i, temperatures.get(i), date.getTime());
            temperatureHistories.add(temperatureHistory);
        }
        db.temperatureHistoryDao().insertAll(temperatureHistories);
    }
}
