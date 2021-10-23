package ir.MasjedEmamAli.Urmia.activity;

import static ir.MasjedEmamAli.Urmia.services.ConstantParams.Gordan;
import static ir.MasjedEmamAli.Urmia.services.ConstantParams.Hoze;
import static ir.MasjedEmamAli.Urmia.services.ConstantParams.Paygah;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.MasjedEmamAli.Urmia.R;
import ir.MasjedEmamAli.Urmia.services.GetPosts;

public class PostActivity extends AppCompatActivity {
    LinearLayout layout;
    Button btn;
    String ListType;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_viewer);

        layout=findViewById(R.id.linear_posts);
        btn=findViewById(R.id.btn_createpost);
        btn.setOnClickListener(btncreate_OnClick);

        ListType=getIntent().getStringExtra("Post");
        String Type = null;
        if(ListType.equals(Paygah))
            Type="Paygah";
        else if (ListType.equals(Hoze))
            Type="Hoze";
        else if (ListType.equals(Gordan))
            Type="Gordan";
        GetPosts Prosses=new GetPosts(layout,getLayoutInflater(), btn, Type);
        Prosses.start();
        Prosses.Post();
    }
    View.OnClickListener btncreate_OnClick=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), NewPostActivity.class);
            intent.putExtra("Type",ListType);
            startActivity(intent);
        }
    };

}
