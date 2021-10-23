package ir.MasjedEmamAli.Urmia.services;

import static ir.MasjedEmamAli.Urmia.services.ConstantParams.SiteAddress;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.MasjedEmamAli.Urmia.R;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GetPosts extends Thread {

    LinearLayout layout;
    LayoutInflater inflater;
    TextView[] title, content;
    ImageView[] img;
    Button btn;
    String Type;

    View posts[];
    int PostLen=0;

    public GetPosts(LinearLayout layout, LayoutInflater inflater, Button btn, String Type) {
        this.layout = layout;
        this.inflater = inflater;
        this.btn = btn;
        this.Type = Type;
        title=new TextView[10];
        content=new TextView[10];
        img=new ImageView[10];
        posts=new ViewGroup[10];
        GetLayoutItems();
    }

    private void GetLayoutItems() {
        for (int i = 0; i < 10; i++) {
            posts[i] = inflater.inflate(R.layout.postviewer_fragment, layout, false);
            posts[i].setVisibility(View.GONE);
            layout.addView(posts[i]);

            ViewGroup post = (ViewGroup) layout.getChildAt(i);
            int count = 0;
            for (int j = 0; j < post.getChildCount(); j++) {
                if (post.getChildAt(j) instanceof ImageView) {
                    img[i] = (ImageView) post.getChildAt(j);
                }
                if (count == 0 && post.getChildAt(j) instanceof TextView) {
                    title[i] = (TextView) post.getChildAt(j);
                    count++;
                }

                if (count == 1 && post.getChildAt(j) instanceof TextView)
                    content[i] = (TextView) post.getChildAt(j);
            }
        }
    }

    @Override
    public void run() {
        String str_prb = new Tools().GetApi(SiteAddress + "/getcommon.php?Type=" + Type);
        List<JSONObject> jsonObjects = JsonArray_notLearned(str_prb);
        PostLen=jsonObjects.size();
        for (int i = 0; i < jsonObjects.size(); i++) {
            try {
                JSONObject Json = jsonObjects.get(i);
                InputStream is = (InputStream) new URL(Json.getString("Photo")).getContent();
                Drawable d = Drawable.createFromStream(is, "src name");

                int finalI = i;
                new Handler(Looper.getMainLooper()).post(() -> {
                    try {
                        String s = Json.getString("Title");
                        title[finalI].setText(s);
                        img[finalI].setImageDrawable(d);
                        content[finalI].setText(Json.getString("Content"));

                    } catch (Exception e) {
                        System.out.println("Handler Error:" + e.getMessage());
                    }
                });
            } catch (Exception e) {
                System.out.println("PostThread Error:" + e.getMessage());
            }
        }

        btn.setText("افزودن پست جدید");
    }
    public void Post(){
        try {
            sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for(int i=0;i<PostLen;i++){
            posts[i].setVisibility(View.VISIBLE);
        }
    }

    private List<JSONObject> JsonArray_notLearned(String str) {
        List<JSONObject> jsonObjects = new ArrayList<>();
        for (int i = 1; i < str.length() - 1; ) {
            StringBuilder str_temp = new StringBuilder();
            int count = 0;
            while (str.charAt(i - 1) != '}')
                str_temp.insert(count++, str.charAt(i++));
            i++;
            JSONObject json_temp = null;
            try {
                json_temp = new JSONObject(str_temp.toString());
            } catch (Exception e) {
                System.out.println("JsonListing Error:" + e.getMessage());

            }
            jsonObjects.add(json_temp);
        }
        return jsonObjects;
    }

}
