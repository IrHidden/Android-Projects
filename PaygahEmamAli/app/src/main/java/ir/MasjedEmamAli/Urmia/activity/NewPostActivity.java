package ir.MasjedEmamAli.Urmia.activity;

import static ir.MasjedEmamAli.Urmia.services.ConstantParams.Gordan;
import static ir.MasjedEmamAli.Urmia.services.ConstantParams.Hoze;
import static ir.MasjedEmamAli.Urmia.services.ConstantParams.Paygah;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.MasjedEmamAli.Urmia.R;
import ir.MasjedEmamAli.Urmia.services.PostSending_PostApi;

public class NewPostActivity extends AppCompatActivity {

    TextView Title,Content;
    ImageView Img;
    Button Btn_Send;
    String Type;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_creater);


        Img=findViewById(R.id.newpost_img);
        Img.setOnClickListener(ImgNewPost_OnClick);

        Title=findViewById(R.id.newpost_title);
        Content=findViewById(R.id.newpost_content);

        Btn_Send=findViewById(R.id.btn_newpost);
        Btn_Send.setOnClickListener(BtnNewPost_OnClick);


        String ListType=getIntent().getStringExtra("Type");
        if(ListType.equals(Paygah))
            Type="Paygah";
        else if (ListType.equals(Hoze))
            Type="Hoze";
        else if (ListType.equals(Gordan))
            Type="Gordan";
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            Uri SelecedImg = data.getData();
            Img.setImageURI(SelecedImg);
        } catch (Exception e) {

        }
    }

    View.OnClickListener ImgNewPost_OnClick= v -> {
        Intent GaleryIntent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(GaleryIntent,1);
    };
    View.OnClickListener BtnNewPost_OnClick= v -> {
        Bitmap bitmap=((BitmapDrawable) Img.getDrawable()).getBitmap();
        PostSending_PostApi NewPostApi=new PostSending_PostApi(Title.getText().toString(), bitmap, Content.getText().toString(), Type);
        NewPostApi.start();
        Toast.makeText(this, "انجام شد!", Toast.LENGTH_SHORT).show();
        finish();
    };


}
