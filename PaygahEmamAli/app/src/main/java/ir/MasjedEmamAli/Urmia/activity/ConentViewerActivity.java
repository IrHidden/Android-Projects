package ir.MasjedEmamAli.Urmia.activity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.MasjedEmamAli.Urmia.R;

import ir.MasjedEmamAli.Urmia.services.ConstantParams;

public class ConentViewerActivity extends AppCompatActivity {

    TextView title;
    TextView content;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_viewer);

        title = findViewById(R.id.quranviewer_title);
        content = findViewById(R.id.quranviewer_content);

        String ListType=getIntent().getStringExtra("List");

        if(ListType.equals(ConstantParams.Quran))
            QuranViewer();
        else if (ListType.equals(ConstantParams.Mafatih))
            MafatihViewer();

        StringBuilder str = new StringBuilder();
        int len = content.getText().toString().length();
        int count = 0;
        for (int i = 0; i < len; i++) {
            if (content.getText().charAt(i) != '(' && content.getText().charAt(i) != ')') {
                if (content.getText().charAt(i) == '.') {
                    str.insert(count++, "\n");
                    str.insert(count++, "\n");
                } else
                    str.insert(count++, content.getText().charAt(i));
            } else {

                while (content.getText().charAt(i++) != ')') ;
                str.insert(count++, "\n");
            }
        }
        content.setText(str);

    }

    private void QuranViewer() {
        int ListNumber = getIntent().getIntExtra(ConstantParams.List_Num, 0) - 1;


        String[] List_Title = this.getResources().getStringArray(R.array.QuranList_Title);
        String[] List_Content = this.getResources().getStringArray(R.array.QuranList_Content);

        title.setText(List_Title[ListNumber]);

        if (List_Content[ListNumber].equals("None")) {
            String[] sore;
            switch (ListNumber) {
                case 1:
                    sore = this.getResources().getStringArray(R.array.SoreBaghare);
                    break;
                case 2:
                    sore = this.getResources().getStringArray(R.array.SoreAlEmran);
                    break;
                case 3:
                    sore = this.getResources().getStringArray(R.array.SoreAnesa);
                    break;
                case 4:
                    sore = this.getResources().getStringArray(R.array.SoreMaede);
                    break;
                case 5:
                    sore = this.getResources().getStringArray(R.array.SoreAlanaam);
                    break;
                case 6:
                    sore = this.getResources().getStringArray(R.array.SoreAleraf);
                    break;
                case 7:
                    sore = this.getResources().getStringArray(R.array.SoreAlenfal);
                    break;
                case 8:
                    sore = this.getResources().getStringArray(R.array.SoreAltobe);
                    break;
                case 9:
                    sore = this.getResources().getStringArray(R.array.SoreYones);
                    break;
                case 10:
                    sore = this.getResources().getStringArray(R.array.SoreHood);
                    break;
                case 11:
                    sore = this.getResources().getStringArray(R.array.SoreYosef);
                    break;
                case 14:
                    sore = this.getResources().getStringArray(R.array.SoreAlhjr);
                    break;
                case 15:
                    sore = this.getResources().getStringArray(R.array.SoreAlNahl);
                    break;
                case 16:
                    sore = this.getResources().getStringArray(R.array.SoreAlasra);
                    break;
                case 17:
                    sore = this.getResources().getStringArray(R.array.SoreAlkahaf);
                    break;
                case 18:
                    sore = this.getResources().getStringArray(R.array.SoreMaryam);
                    break;
                case 19:
                    sore = this.getResources().getStringArray(R.array.SoreTaha);
                    break;
                case 20:
                    sore = this.getResources().getStringArray(R.array.SoreAlnaei);
                    break;
                case 21:
                    sore = this.getResources().getStringArray(R.array.SorAlhaj);
                    break;
                case 22:
                    sore = this.getResources().getStringArray(R.array.SoreAlmomenin);
                    break;
                case 23:
                    sore = this.getResources().getStringArray(R.array.SoreAlnor);
                    break;
                case 25:
                    sore = this.getResources().getStringArray(R.array.SoreAlshoara);
                    break;
                case 26:
                    sore = this.getResources().getStringArray(R.array.SoreAlnml);
                    break;
                case 27:
                    sore = this.getResources().getStringArray(R.array.SoreAlgesas);
                    break;
                case 32:
                    sore = this.getResources().getStringArray(R.array.SoreAlahzab);
                    break;
                case 36:
                    sore = this.getResources().getStringArray(R.array.SoreAlsafat);
                    break;
                case 38:
                    sore = this.getResources().getStringArray(R.array.SoreAlzamr);
                    break;
                case 42:
                    sore = this.getResources().getStringArray(R.array.SoreAlghafer);
                    break;
                default:
                    sore = null;
            }

            content.setText(" ");
            for (int i = 0; i < sore.length; i++)
                content.append(sore[i]);
        } else
            content.setText(List_Content[ListNumber]);
    }

    private void MafatihViewer() {
        int ListNumber = getIntent().getIntExtra(ConstantParams.List_Num, 0) - 1;


        String[] List_Title = this.getResources().getStringArray(R.array.MafatihList_Title);
        String[] List_Content = this.getResources().getStringArray(R.array.Pray_Content);

        title.setText(List_Title[ListNumber]);

        if (List_Content[ListNumber].equals("None")) {
            String[] sore;
            switch (ListNumber) {
                case 5:
                    sore = this.getResources().getStringArray(R.array.DoaieJameKabire);
                    break;
                case 7:
                    sore = this.getResources().getStringArray(R.array.DoaieKomeil);
                    break;
                case 9:
                    sore = this.getResources().getStringArray(R.array.DoaieNodbe);
                    break;
                default:
                    sore = null;
            }

            content.setText(" ");
            for (int i = 0; i < sore.length; i++)
                content.append(sore[i]);

        } else
            content.setText(List_Content[ListNumber]);


    }

}
