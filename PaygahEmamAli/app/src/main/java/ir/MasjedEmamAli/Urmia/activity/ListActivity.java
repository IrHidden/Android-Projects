package ir.MasjedEmamAli.Urmia.activity;

import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.MasjedEmamAli.Urmia.R;
import ir.MasjedEmamAli.Urmia.services.ListGenrate;
import ir.MasjedEmamAli.Urmia.services.ConstantParams;

public class ListActivity extends AppCompatActivity {

    ListGenrate listgenrate;
    LinearLayout ListLayout;
    SearchView srcView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_viewer);

        ListLayout =findViewById(R.id.listlayout);
        srcView=findViewById(R.id.listsearch);
        srcView.setOnQueryTextListener(Src_OnSearch);

        String ListType=getIntent().getStringExtra("List");

        String[] List_Title=null;
        if(ListType.equals(ConstantParams.Quran))
            List_Title = this.getResources().getStringArray(R.array.QuranList_Title);
        else if (ListType.equals(ConstantParams.Mafatih))
            List_Title = this.getResources().getStringArray(R.array.MafatihList_Title);


        listgenrate=new ListGenrate(List_Title, ListLayout, getApplicationContext(), ListType);
        listgenrate.run();



    }
    private SearchView.OnQueryTextListener Src_OnSearch =new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            listgenrate.ListGenrator(newText);
            return false;
        }
    };


}
