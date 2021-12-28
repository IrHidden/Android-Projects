package ir.myapp.controller3.comunication;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;

import java.util.Iterator;

import ir.myapp.controller3.settings.Settings;
import ir.myapp.controller3.tools.Utils;

public class SMSInformation extends Information {
    SmsManager manager;
    String phoneNumber;


    public SMSInformation(Context context, PreferenceNames preferenceName) {
        super(context, preferenceName);
        manager = SmsManager.getDefault();
        phoneNumber = new Settings(context).getRemotePhoneNumber();
    }


    @Override
    public void fetchAllFromRemote() {
        PendingIntent pi = PendingIntent.getActivity(context, 0,
                new Intent(context, SMSInformation.class), 0);
        String message = "getAll";
        manager.sendTextMessage(phoneNumber, null, message, pi, null);
        Utils.deleteThread(super.context, phoneNumber);
    }

    @Override
    public void sendStatus(String type, boolean status) {
        PendingIntent pi = PendingIntent.getActivity(context, 0,
                new Intent(context, SMSInformation.class), 0);
        String message = status ? type + ":on." : type + ":off.";
        manager.sendTextMessage(phoneNumber, null, message, pi, null);
    }

    @Override
    public void sendValue(String type, String value) {
        PendingIntent pi = PendingIntent.getActivity(context, 0,
                new Intent(context, SMSInformation.class), 0);
        String message = type + ":" + value;
        manager.sendTextMessage(phoneNumber, null, message, pi, null);
    }

    @Override
    public void sendAll() {
        PendingIntent pi = PendingIntent.getActivity(context, 0,
                new Intent(context, SMSInformation.class), 0);
        String message = "He:";
        Iterator<Boolean> heaterStatusIterator = getHeatersOnOrOff().iterator();
        Iterator<Integer> expectedHeaterTempIterator = getExpectedHeatersTemps().iterator();
        while (heaterStatusIterator.hasNext() && expectedHeaterTempIterator.hasNext()){
            message += heaterStatusIterator.next() ? "1-":"0-";
            String next = String.valueOf(expectedHeaterTempIterator.next());
            if (next.length() == 1){
                next = "0"+next;
            }
            message += next + ",";
        }
        message += ";Va:";
        Iterator<Boolean> vapourStatusIterator = getVapoursOnOrOff().iterator();
        Iterator<Integer> expectedVapourIterator = getExpectedVapoursHumidities().iterator();
        while (vapourStatusIterator.hasNext() && expectedVapourIterator.hasNext()){
            message += vapourStatusIterator.next() ? "1-":"0-";
            String next = String.valueOf(expectedVapourIterator.next());
            if (next.length() == 1){
                next = "0"+next;
            }
            message += next + ",";
        }
        message += ";AC:";
        message += getACOnOrOff() ? "1":"0";
        message += ";Ti:";
        for (String [] time: getACOnOffTimes()){
            message += time[0] + "-" + time[1] + ",";
        }
//        message += ";Tg:";
//        for (int i: getExpectedHeatersTemps()){
//            message += i+",";
//        }
//        message += ";Hg:";
//
//        for (int i: getExpectedVapoursHumidities()){
//            message += i+",";
//        }

        message += ";.";
        message = message.replaceAll(",;", ";");
        manager.sendTextMessage(phoneNumber, null, message, pi, null);
        Intent updateNotification = new Intent("ir.myapp.controller3.UPDATE_NOTIFICATION");
        context.sendBroadcast(updateNotification);
//        Utils.deleteThread(super.context, phoneNumber);
    }
}
