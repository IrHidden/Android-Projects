package ir.myapp.controller3.activity;

import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import ir.myapp.controller3.R;
import ir.myapp.controller3.comunication.Information;
import ir.myapp.controller3.comunication.SMSInformation;

public class AirConditionActivity extends AppCompatActivity {

    List<String[]> actimes;
    Information information;
    SwitchCompat acOnOff;
    TextView acOnOffText;
    ImageView add;
    FloatingActionButton fab;
    boolean init= true;
    boolean ACActive = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.air_condition);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.air_conditioning);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        information = new SMSInformation(this, Information.PreferenceNames.New);
        actimes = information.getACOnOffTimes();
        acOnOff = findViewById(R.id.ac_on_switch);
        acOnOffText = findViewById(R.id.ac_on_off_txt);
        add = findViewById(R.id.timer_add);

        RecyclerView recyclerView = findViewById(R.id.ac_recycler);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                manager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        final MyRecyclerViewAdapter adapter = new MyRecyclerViewAdapter(this);
        recyclerView.setAdapter(adapter);

        ACActive = information.getACOnOrOff();

        acOnOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    acOnOffText.setText(getResources().getString(R.string.ac_is_on));
                }else{
                    acOnOffText.setText(getResources().getString(R.string.ac_is_on));
                }
                if(!init){
                    ACActive = isChecked;
                    fab.show();
                }
            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actimes.add(new String[]{"12:00", "13:00"});
                fab.show();
                adapter.notifyDataSetChanged();
            }
        });

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                //Remove swiped item from list and notify the RecyclerView
                int position = viewHolder.getAdapterPosition();
                removeTime(position);
                adapter.notifyDataSetChanged();

            }
        };

        fab = findViewById(R.id.fab_ac);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                information.setACOnOffTimes(actimes);
                information.setACOnOrOff(ACActive);
                fab.hide();
            }
        });
        fab.hide();


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        init=false;
    }

    private void removeTime(int position){
        actimes.remove(position);
        fab.show();
    }

    private void setNewTime(int position, int index, String value){
        actimes.get(position)[index] = value;
        fab.show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder>{

        private LayoutInflater inflater;
        private Context context;


        public MyRecyclerViewAdapter(Context context) {
            this.inflater = LayoutInflater.from(context);
            this.context = context;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.ac_row, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.startTime.setText(actimes.get(position)[0]);
            holder.endTime.setText(actimes.get(position)[1]);
            holder.position = position;
        }

        @Override
        public int getItemCount() {
            return actimes.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView startTime;
            TextView endTime;
            int position;

                ViewHolder(@NonNull View itemView) {
                super(itemView);
                startTime = itemView.findViewById(R.id.start_time_value);
                endTime = itemView.findViewById(R.id.end_time_value);

                startTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TimePickerDialog timePickerDialog = new TimePickerDialog(AirConditionActivity.this,
                                new TimePickerDialog.OnTimeSetListener() {

                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                        String value = hourOfDay + ":" + minute;
                                        startTime.setText(value);
                                        setNewTime(position, 0, value);
                                    }
                                }, 12, 0, true);
                        timePickerDialog.show();
                    }
                });

                endTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        TimePickerDialog timePickerDialog = new TimePickerDialog(AirConditionActivity.this,
                                new TimePickerDialog.OnTimeSetListener() {

                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                        String value = hourOfDay + ":" + minute;
                                        endTime.setText(value);
                                        setNewTime(position, 1, value);
                                    }
                                }, 12, 0, true);
                        timePickerDialog.show();

                    }
                });
            }
        }
    }

}
