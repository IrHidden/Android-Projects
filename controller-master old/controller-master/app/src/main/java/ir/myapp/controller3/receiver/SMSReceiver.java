package ir.myapp.controller3.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;

import ir.myapp.controller3.comunication.Information;
import ir.myapp.controller3.comunication.SMSInformation;
import ir.myapp.controller3.settings.Logger;
import ir.myapp.controller3.settings.Settings;
import ir.myapp.controller3.tools.Utils;

public class SMSReceiver extends BroadcastReceiver {
    private Bundle bundle;
    private SmsMessage currentSMS;
    private String message;

    @Override
    public void onReceive(Context context, Intent intent) {
        String remotePhoneNumber = new Settings(context).getRemotePhoneNumber();

        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdu_Objects = (Object[]) bundle.get("pdus");
                if (pdu_Objects != null) {
                    Information newInfo = new SMSInformation(context, Information.PreferenceNames.New);
                    Information backupInfo = new SMSInformation(context, Information.PreferenceNames.Backup);
                    Logger logger = new Logger(context);
                    for (Object aObject : pdu_Objects) {

                        currentSMS = getIncomingMessage(aObject, bundle);

                        String senderNo = currentSMS.getDisplayOriginatingAddress();
                        message = currentSMS.getDisplayMessageBody();
//                        Toast.makeText(context, "senderNum: " + senderNo + " :\n message: " + message, Toast.LENGTH_LONG).show();
                        if (senderNo.equals(remotePhoneNumber)){
                            Utils.digestAndSaveSMSInfo(message, newInfo, logger);
                            Utils.copySharedPreferences(newInfo.getPreferences(), backupInfo.getPreferences());
                            Utils.addToDb(newInfo, context);
                            Intent stopRefresh = new Intent("ir.myapp.controller3.STOP_REFRESH");
                            context.sendBroadcast(stopRefresh);

                            Intent updateNotification = new Intent("ir.myapp.controller3.UPDATE_NOTIFICATION");
                            context.sendBroadcast(updateNotification);
                            this.abortBroadcast();
                        }
                    }
                    // End of loop
                }
            }
        }
    }
    private SmsMessage getIncomingMessage(Object aObject, Bundle bundle) {
        SmsMessage currentSMS;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String format = bundle.getString("format");
            currentSMS = SmsMessage.createFromPdu((byte[]) aObject, format);
        } else {
            currentSMS = SmsMessage.createFromPdu((byte[]) aObject);
        }
        return currentSMS;
    }
}
