package ir.MasjedEmamAli.Urmia.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.MasjedEmamAli.Urmia.R;
import ir.MasjedEmamAli.Urmia.db.AppDatabase;
import ir.MasjedEmamAli.Urmia.entity.UserStruct;
import ir.MasjedEmamAli.Urmia.services.Tools;
import ir.MasjedEmamAli.Urmia.services.ConstantParams;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_loading);
        getActionBar().hide();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        new Handler().postDelayed(RunMain, 3000);

    }

    Runnable RunMain = () -> {
        try {
            new Thread(() -> {
                try {
                    AppDatabase appDatabase = AppDatabase.getInstance(getBaseContext());
                    UserStruct user = appDatabase.UserDao().Get();
                    String Data = null;
                    String url = ConstantParams.SiteAddress + "/getuser.php?Username=" + user.Username() + "&Password=" + user.Password();
                    Data= new Tools().GetApi(url);
                    if (Data.toCharArray()[0] !=4 && Data.toCharArray()[2] !='4') {
                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        intent.putExtra("Acc",Data);
                        startActivity(intent);
                        finish();
                    } else if (Data.toCharArray()[0] =='4' && Data.toCharArray()[1] =='0' && Data.toCharArray()[2] =='4') {
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } catch (Exception e) {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }).start();
        }
        catch (Exception e)
        {
            System.out.println("Splash Error: "+e.getMessage());
        }
    };
}