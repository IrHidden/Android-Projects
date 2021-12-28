package ir.myapp.controller3.activity;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.InputType;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.EditTextPreference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import ir.myapp.controller3.R;
import ir.myapp.controller3.comunication.Information;
import ir.myapp.controller3.comunication.SMSInformation;
import ir.myapp.controller3.settings.Settings;
import ir.myapp.controller3.tools.Utils;

public class SettingsActivity2 extends AppCompatActivity {
    Information information;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            PreferenceManager preferenceManager = getPreferenceManager();
            preferenceManager.setSharedPreferencesName("settings");
            setPreferencesFromResource(R.xml.preference, rootKey);

            androidx.preference.EditTextPreference editTextPreference = getPreferenceManager().findPreference("password");
            editTextPreference.setOnBindEditTextListener(new EditTextPreference.OnBindEditTextListener() {
                @Override
                public void onBindEditText(@NonNull EditText editText) {
                    editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                }
            });

            SharedPreferences.OnSharedPreferenceChangeListener listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
                public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                    if (key.equals("audible_phone_numbers")){
                        String phoneNumber = new Settings(getContext()).getRemotePhoneNumber();
                        PendingIntent pi = PendingIntent.getActivity(getContext(), 0,
                                new Intent(getContext(), SMSInformation.class), 0);
                        String message = "perm_numb:"+prefs.getString("audible_phone_numbers", "");
                        SmsManager.getDefault().sendTextMessage(phoneNumber, null, message, pi, null);
                        Utils.deleteThread(getContext(), phoneNumber);
                    }
                    else if (key.equals("phone_number")){
                        String phoneNumber = prefs.getString("phone_number", "");
                        if (phoneNumber.startsWith("0")){
                            phoneNumber = "+98" + phoneNumber.substring(1);
                        }else if (phoneNumber.startsWith("9")){
                            phoneNumber = "+98" + phoneNumber;
                        }
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("phone_number", phoneNumber);
                        System.out.println("Hello i am changing phone number");
                        editor.commit();
                    }
                }
            };

            preferenceManager.getSharedPreferences().registerOnSharedPreferenceChangeListener(listener);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}