package ir.myapp.controller3.activity;

import static ir.myapp.controller3.tools.CommenFuntions.RelaysNum;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import org.jetbrains.annotations.NotNull;

import ir.myapp.controller3.R;
import ir.myapp.controller3.db.AppDatabase;
import ir.myapp.controller3.entity.Relay_Struct;
import ir.myapp.controller3.entity.System_Struct;
import ir.myapp.controller3.service.Relay_Activity_Service;
import ir.myapp.controller3.tools.CommenFuntions;

public class Relay_Activity_Java extends AppCompatActivity {

    CommenFuntions comnFunc;

    LinearLayout Relay_LinearLayout;
    Button Relay_Edit_btn;
    Thread RelayActivity_Service_Thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.relay_activity_xml);

        comnFunc =new CommenFuntions();

        Relay_LinearLayout = findViewById(R.id.linear_relay);

        Relay_Edit_btn=findViewById(R.id.btn_relayedit);
        Relay_Edit_btn.setOnClickListener(Btn_Relay_Edit_Click);

        RelayActivity_Service_Thread = new Relay_Activity_Service(this, getLayoutInflater(), Relay_LinearLayout);

        try {
            RelayActivity_Service_Thread.start();
        } catch (Exception e) {
        }

    }

    private final View.OnClickListener  Btn_Relay_Edit_Click=new View.OnClickListener() {
        @Override
        public void onClick(@NotNull View v) {
            Intent intent = new Intent(getApplicationContext(), RelayEdit_Activity_Java.class);
            Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.presses_state);
            v.startAnimation(anim);
            startActivity(intent);
        }
    };
}