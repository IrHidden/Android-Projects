package ir.myapp.controller3.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ir.myapp.controller3.R;
import ir.myapp.controller3.comunication.Information;
import ir.myapp.controller3.comunication.SMSInformation;

public class SensorsHumidityFragment extends Fragment{
    Information information;
    int expectedTempVal = 0;
    int position = 0;
    TextView number;
    TextView sensorsHumidity;
    boolean init = true;

    public static Fragment instance(int position){
        Fragment fragment = new SensorsHumidityFragment();
        Bundle b = new Bundle();
        b.putInt("position", position);
        fragment.setArguments(b);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sensors_humidity_fragment, container, false);
        position = getArguments().getInt("position");
        information = new SMSInformation(getContext(), Information.PreferenceNames.New);
        number = view.findViewById(R.id.sensor_number_humidity);
        sensorsHumidity = view.findViewById(R.id.sensor_humidity);
        number.setText(String.valueOf(position+1));
        sensorsHumidity.setText(information.getSensorsHumidity().get(position) + " %");

        init = false;
        return view;
    }
}
