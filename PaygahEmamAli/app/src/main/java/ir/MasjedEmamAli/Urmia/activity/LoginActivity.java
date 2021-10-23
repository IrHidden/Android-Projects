package ir.MasjedEmamAli.Urmia.activity;

import static ir.MasjedEmamAli.Urmia.services.ConstantParams.SiteAddress;

import static java.lang.Thread.sleep;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.MasjedEmamAli.Urmia.R;
import ir.MasjedEmamAli.Urmia.db.AppDatabase;
import ir.MasjedEmamAli.Urmia.entity.UserStruct;
import ir.MasjedEmamAli.Urmia.services.Tools;

public class LoginActivity extends AppCompatActivity {

    TextView usename, password;
    Button btn, btn_q;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login);

        usename = findViewById(R.id.login_username);
        password = findViewById(R.id.login_password);
        btn = findViewById(R.id.btn_login);
        btn.setOnClickListener(btn_onClick);


        btn_q = findViewById(R.id.btn_q);
        btn_q.setOnClickListener(btnQ_OnClick);
    }

    View.OnClickListener btn_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new Thread(() -> {
                String url = SiteAddress + "/getuser.php?Username=" + usename.getText().toString() + "&Password=" + password.getText().toString();
                String Data = new Tools().GetApi(url);
                try {
                    if (Data.toCharArray()[0] == '1' || Data.toCharArray()[1] == '1' || Data.toCharArray()[2] == '1') {
                        AppDatabase appDatabase = AppDatabase.getInstance(getApplicationContext());
                        UserStruct user = new UserStruct(usename.getText().toString(), password.getText().toString());
                        appDatabase.UserDao().Delete_All();
                        appDatabase.UserDao().Insert(user);
                        Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
                        startActivity(intent);
                        finish();
                    } else if (Data.toCharArray()[0] == '4' && Data.toCharArray()[1] == '0' && Data.toCharArray()[2] == '4') {
                        btn.setText("اطلاعات صحیح نیست");
                        sleep(2000);
                    }

                } catch (Exception e) {

                }
            }).start();
        }
    };
    View.OnClickListener btnQ_OnClick=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
            startActivity(intent);
            AppDatabase appDatabase = AppDatabase.getInstance(getApplicationContext());
            UserStruct user = new UserStruct("1", "1");
            appDatabase.UserDao().Delete_All();
            appDatabase.UserDao().Insert(user);
            finish();
        }
    };
}
