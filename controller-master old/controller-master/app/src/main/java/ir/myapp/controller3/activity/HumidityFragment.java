package ir.myapp.controller3.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import ir.myapp.controller3.R;
import ir.myapp.controller3.comunication.Information;
import ir.myapp.controller3.comunication.SMSInformation;

public class HumidityFragment extends Fragment {
    Information information;
    int expectedTempVal = 0;
    int position = 0;
    FloatingActionButton fab;
    SwitchCompat onOffSwitch;
    TextView onOrOffText;
    TextView expectedTemp;
    TextView number;
    boolean init = true;

    public static Fragment instance(int position){
        Fragment fragment = new HumidityFragment();
        Bundle b = new Bundle();
        b.putInt("position", position);
        fragment.setArguments(b);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.humidity_fragment, container, false);
        fab = getActivity().findViewById(R.id.fab_humidity);
        fab.hide();
        position = getArguments().getInt("position");
        information = new SMSInformation(getContext(), Information.PreferenceNames.New);
        onOffSwitch = view.findViewById(R.id.vapour_on_or_off_switch);
        onOrOffText = view.findViewById(R.id.vapour_on_or_off_txt);
        expectedTemp = view.findViewById(R.id.expected_humidity);
        number = view.findViewById(R.id.vapour_number);
        number.setText(String.valueOf(position+1));
        TextView plus = view.findViewById(R.id.humidity_plus);
        TextView minus = view.findViewById(R.id.humidity_mines);
        expectedTempVal = information.getExpectedVapoursHumidities().get(position);
        onOffSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    onOrOffText.setText(R.string.vapour_is_on);
                }else{
                    onOrOffText.setText(R.string.vapour_is_off);
                }
                if(!init){
                    fab.show();
                }

            }
        });
        expectedTemp.setText(expectedTempVal + "%");
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expectedTempVal += 1;
                expectedTemp.setText(expectedTempVal+"%");
                if(!init){
                    fab.show();
                }
            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expectedTempVal -= 1;
                expectedTemp.setText(expectedTempVal+"%");
                if(!init){
                    fab.show();
                }
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Integer> current = information.getExpectedVapoursHumidities();
                current.set(position, expectedTempVal);
                information.setExpectedVapoursHumidities(current);

                List<Boolean> currentOnOffs = information.getVapoursOnOrOff();
                currentOnOffs.set(position, onOffSwitch.isChecked());
                information.setVapoursOnOff(currentOnOffs);
                fab.hide();
            }
        });
        fab.hide();
        onOffSwitch.setChecked(information.getVapoursOnOrOff().get(position));
        init = false;
        return view;
    }

}
