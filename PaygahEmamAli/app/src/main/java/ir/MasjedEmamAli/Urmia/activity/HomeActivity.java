package ir.MasjedEmamAli.Urmia.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.MasjedEmamAli.Urmia.R;
import ir.MasjedEmamAli.Urmia.db.AppDatabase;
import ir.MasjedEmamAli.Urmia.services.TimeApiGet;
import ir.MasjedEmamAli.Urmia.services.ConstantParams;

public class HomeActivity extends AppCompatActivity {

    TextView txt_AzanSob, txt_Tolu, txt_AzanZohr, txt_Gorub, txt_AzanMaghreb, txt_NimeShab, txt_daynow;
    View layout1, layout10, layout100;
    View btn_quranlist, btn_mafatih, btn_paygah, btn_hoze, btn_gordan, btn_GroupPaygah, btn_exit;
    String Acc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_main);
        txt_AzanSob = findViewById(R.id.txt_AzanSob);
        txt_Tolu = findViewById(R.id.txt_Tolu);
        txt_AzanZohr = findViewById(R.id.txt_AzanZohr);
        txt_Gorub = findViewById(R.id.txt_Gorub);
        txt_AzanMaghreb = findViewById(R.id.txt_AzanMaghreb);
        txt_NimeShab = findViewById(R.id.txt_NimeShab);
        txt_daynow = findViewById(R.id.txt_daynow);
        btn_quranlist = findViewById(R.id.btn_quranlist);
        btn_mafatih = findViewById(R.id.btn_mafatih);
        btn_paygah = findViewById(R.id.btn_post_paygah);
        btn_hoze = findViewById(R.id.btn_post_hoze);
        btn_gordan = findViewById(R.id.btn_post_gordan);
        btn_GroupPaygah = findViewById(R.id.btn_group_paygah);
        btn_exit = findViewById(R.id.btn_exitlogin);
        layout1 = findViewById(R.id.layout1);
        layout10 = findViewById(R.id.layout10);
        layout100 = findViewById(R.id.layout100);

        btn_quranlist.setOnClickListener(QuransList_OnClick);
        btn_mafatih.setOnClickListener(PraysList_OnClick);
        btn_paygah.setOnClickListener(PostPaygah_OnClick);
        btn_hoze.setOnClickListener(PostGordan_OnClick);
        btn_gordan.setOnClickListener(PostHoze_OnClick);
        btn_GroupPaygah.setOnClickListener(GroupPaygah_OnClick);
        btn_exit.setOnClickListener(ExitLogin_OnClick);

        TimeApiGet time = new TimeApiGet(txt_AzanSob, txt_Tolu, txt_AzanZohr, txt_Gorub, txt_AzanMaghreb, txt_NimeShab, txt_daynow);
        time.start();

        Acc = getIntent().getStringExtra("Acc");

        try {
            if (Acc.toCharArray()[0] == '1')
                layout1.setVisibility(View.VISIBLE);
            if (Acc.toCharArray()[1] == '1')
                layout10.setVisibility(View.VISIBLE);
            if (Acc.toCharArray()[2] == '1')
                layout100.setVisibility(View.VISIBLE);
        } catch (Exception e) {

        }

    }

    private final View.OnClickListener QuransList_OnClick = v -> {
        Intent intent = new Intent(getApplicationContext(), ListActivity.class);
        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.presses_state);
        v.startAnimation(anim);
        intent.putExtra("List", ConstantParams.Quran);
        startActivity(intent);
    };
    private final View.OnClickListener PraysList_OnClick = v -> {
        Intent intent = new Intent(getApplicationContext(), ListActivity.class);
        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.presses_state);
        v.startAnimation(anim);
        intent.putExtra("List", ConstantParams.Mafatih);
        startActivity(intent);
    };
    private final View.OnClickListener PostPaygah_OnClick = v -> {
        Intent intent = new Intent(getApplicationContext(), PostActivity.class);
        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.presses_state);
        v.startAnimation(anim);
        intent.putExtra("Post", ConstantParams.Paygah);
        if (Acc.toCharArray()[0] != '1')
            Toast.makeText(this, "برای دیدن اعلانات مربوط به پایگاه باید عضو شوید", Toast.LENGTH_SHORT).show();
        else
            startActivity(intent);
    };
    private final View.OnClickListener PostHoze_OnClick = v -> {
        Intent intent = new Intent(getApplicationContext(), PostActivity.class);
        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.presses_state);
        v.startAnimation(anim);
        intent.putExtra("Post", ConstantParams.Hoze);
        if (Acc.toCharArray()[1] != '1')
            Toast.makeText(this, "برای دیدن اعلانات مربوط به حوزه باید عضو شوید", Toast.LENGTH_SHORT).show();
        else
            startActivity(intent);
    };
    private final View.OnClickListener PostGordan_OnClick = v -> {
        Intent intent = new Intent(getApplicationContext(), PostActivity.class);
        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.presses_state);
        v.startAnimation(anim);
        intent.putExtra("Post", ConstantParams.Gordan);
        if (Acc.toCharArray()[2] != '1')
            Toast.makeText(this, "برای دیدن اعلانات مربوط به گردان باید عضو شوید", Toast.LENGTH_SHORT).show();
        else
            startActivity(intent);
    };
    private final View.OnClickListener GroupPaygah_OnClick = v -> {
        try {
            Intent intentWhatsapp = new Intent(Intent.ACTION_VIEW);
            String url = "https://chat.whatsapp.com/GBNd8EuTXjN99nJunV5IAt";
            intentWhatsapp.setData(Uri.parse(url));
            intentWhatsapp.setPackage("com.whatsapp");
            startActivity(intentWhatsapp);
        } catch (Exception e) {
        }
    };
    private final View.OnClickListener ExitLogin_OnClick = v -> {
        Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.presses_state);
        v.startAnimation(anim);
        AppDatabase appDatabase = AppDatabase.getInstance(getApplicationContext());
        appDatabase.UserDao().Delete_All();
        startActivity(intent);
    };


}
