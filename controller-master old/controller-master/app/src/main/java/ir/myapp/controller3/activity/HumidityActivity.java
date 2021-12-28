package ir.myapp.controller3.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ir.myapp.controller3.R;
import ir.myapp.controller3.comunication.Information;
import ir.myapp.controller3.comunication.SMSInformation;
import ir.myapp.controller3.db.AppDatabase;
import ir.myapp.controller3.entity.HumidityHistory;
import ir.myapp.controller3.settings.Constants;

public class HumidityActivity extends AppCompatActivity {
    private Information information;
    Calendar rightNow = Calendar.getInstance();
    private int currentHour = rightNow.get(Calendar.HOUR_OF_DAY);
    private int currentDay = rightNow.get(Calendar.DAY_OF_MONTH);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.humidity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.humidity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        information = new SMSInformation(this, Information.PreferenceNames.New);


        ViewPager2 viewPagerVapours = findViewById(R.id.humidity_view_pager_vapours);
        viewPagerVapours.setAdapter( new VapourAdapter(getSupportFragmentManager(), getLifecycle()));

        TabLayout tabLayoutVapour = findViewById(R.id.humidity_vapour_tab_layout);
        new TabLayoutMediator(tabLayoutVapour, viewPagerVapours, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {

            }
        }
        ).attach();


//        ViewPager2 viewPagerSensors = findViewById(R.id.humidity_view_pager_sensors);
//        viewPagerSensors.setAdapter( new SensorsAdapter(getSupportFragmentManager(), getLifecycle()));
//
//        TabLayout tabLayoutSensors = findViewById(R.id.humidity_sensor_tab_layout);
//        new TabLayoutMediator(tabLayoutSensors, viewPagerSensors, new TabLayoutMediator.TabConfigurationStrategy() {
//            @Override
//            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
//
//            }
//        }
//        ).attach();


        LineChart chart = findViewById(R.id.humidity_chart);

        AppDatabase database = AppDatabase.getInstance(getApplicationContext());
        long last24Hours = new Date().getTime()-24*60*60*1000;
        List<HumidityHistory> humidityHistories = database.humidityHistoryDao().getAfterDate(last24Hours);
        Log.d("history size", ""+humidityHistories.size());
        SensorsData sensorsData = new SensorsData(information.getSensorsCount());
        sensorsData.addData(humidityHistories);
        LineData lineData = new LineData();

        for (int j = 0; j < sensorsData.sensorData.length; j++){
            SensorData s = sensorsData.sensorData[j];
            List<Entry> entries = new ArrayList<Entry>();
            for (int i = 0; i < 24; i++){
                Float value = s.hourData[i].value;
                if (value != null){
                    entries.add(new Entry(i,value));
                }
            }
            LineDataSet dataSet = new LineDataSet(entries, "Sensor "+ (j+1));
            dataSet.setColor(Constants.colors[j]);
            lineData.addDataSet(dataSet);
        }

        chart.setData(lineData);
        chart.invalidate();
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                int index = (int)value;
                System.out.println("index in chart " + index);
                int currentHour = rightNow.get(Calendar.HOUR_OF_DAY);
                int label = currentHour-(24-index);
                if (label <= 0){
                    label += 24;
                }
                return label + ":00";
            }
        });
        
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    class VapourAdapter extends FragmentStateAdapter {


        public VapourAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return HumidityFragment.instance(position);
        }

        @Override
        public int getItemCount() {
            return information.getVapoursOnOrOff().size();
        }
    }

//    class SensorsAdapter extends FragmentStateAdapter {
//
//
//        public SensorsAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
//            super(fragmentManager, lifecycle);
//        }
//
//        @NonNull
//        @Override
//        public Fragment createFragment(int position) {
//            return SensorsHumidityFragment.instance(position);
//        }
//
//        @Override
//        public int getItemCount() {
//            return information.getSensorsHumidity().size();
//        }
//    }

    private class HourData{
        List<Integer> values = new ArrayList<>();
        Float value = null;
        public void addValue(int val){
            this.values.add(val);
        }

        public void postProcess(){
            if (values.size()>0) {
                int sum = 0;
                for (int v : values) {
                    Log.d("chart values", v+"");
                    sum += v;
                }
                this.value = Float.valueOf(sum / values.size());

            }
        }
    }

    private class SensorData{
        HourData  hourData [];
        public SensorData(){
            hourData = new HourData[24];
            for (int i = 0; i < 24; i++){
                hourData[i] = new HourData();
            }
        }

        public void postProcess(){
            for (HourData h: hourData){
                h.postProcess();
            }
        }


        public void addData(HumidityHistory humidityHistory){
            Date date = new Date(humidityHistory.getSaveDate());
            int hour = date.getHours();
            int day = date.getDay();
            int index = 0;
            index = hour-currentHour;
            if (index < 0){
                index += 24;
            }
            if (index == 0 && currentDay == day){
                index = 23;
            }
            hourData[index].addValue(humidityHistory.getValue());

        }

    }

    private class SensorsData{
        SensorData sensorData[];
        public SensorsData(int sensorsCount){
            this.sensorData = new SensorData[sensorsCount];
            for (int i = 0; i < sensorsCount; i++){
                this.sensorData[i] = new SensorData();
            }
        }

        public void addData(List<HumidityHistory> humidityHistories){
            for (HumidityHistory h: humidityHistories){
                this.sensorData[h.getSensorId()].addData(h);
            }

            for (SensorData sensorDatum : sensorData) {
                sensorDatum.postProcess();
            }
        }
    }
}
