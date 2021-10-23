package ir.myapp.controller3.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import ir.myapp.controller3.R;

public class Register_Activity_Java extends AppCompatActivity {
    Button Login_btn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity_xml);
        Login_btn=findViewById(R.id.login_button);
        Login_btn.setOnClickListener(Login_btn_Click);
    }

    final View.OnClickListener Login_btn_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.presses_state);
            v.startAnimation(anim);
            startActivity(intent);
        }
    };
}

