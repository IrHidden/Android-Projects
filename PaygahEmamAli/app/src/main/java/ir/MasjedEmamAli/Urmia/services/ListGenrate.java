package ir.MasjedEmamAli.Urmia.services;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Space;

import androidx.core.content.ContextCompat;

import com.MasjedEmamAli.Urmia.R;
import ir.MasjedEmamAli.Urmia.activity.ConentViewerActivity;

public class ListGenrate extends  Thread {


        Context cont;

        String[] Title;
        LinearLayout layout;
        String ListType;

        public ListGenrate(String[] Title, LinearLayout layout, Context cont, String ListType) {
                this.Title = Title;
                this.layout = layout;
                this.cont = cont;
                this.ListType = ListType;
        }

        @Override
        public void run() {
                try {
                        Refersher_UiThread();
                } catch (Exception e) {
                        System.out.println("Quran Thread: " + e.getMessage());
                }
        }

        private void Refersher_UiThread() {
                try {
                        new Handler(Looper.getMainLooper()).post(() -> {
                                try {
                                        ListGenrator(null);
                                } catch (Exception e) {
                                        System.out.println("Quran MainLoop Handler Error: " + e.getMessage());
                                }
                        });
                } catch (Exception e) {
                        System.out.println("Quran MainLoop Thread Error: " + e.getMessage());
                }
        }

        public void ListGenrator(String search) {
                layout.removeAllViews();
                LinearLayout.LayoutParams LayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                LayoutParams.weight = 1;

                for (int i = 0; i < Title.length; ) {
                        LinearLayout Layout = new LinearLayout(cont);
                        Layout.setLayoutParams(LayoutParams);
                        LinearLayout.LayoutParams BtnParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1);
                        BtnParams.bottomMargin = 20;

                        Button BtnFirst = new Button(cont);
                        if (search == null || search == "") {
                                BtnFirst.setText(Title[i]);

                        } else {
                                for (; i < Title.length; i++)
                                        if (Title[i].contains(search)) {
                                                BtnFirst.setText(Title[i]);
                                                break;
                                        }
                        }
                        if (i >= Title.length)
                                break;
                        BtnFirst.setId(i++);
                        BtnFirst.setLayoutParams(BtnParams);
                        BtnFirst.setBackground(ContextCompat.getDrawable(cont, R.drawable.theme));
                        BtnFirst.setTextSize(25);
                        BtnFirst.setTextColor(ContextCompat.getColor(cont, R.color.white));
                        BtnFirst.setOnClickListener(SoreView_CLick);
                        Layout.addView(BtnFirst);


                        Button BtnSecond = new Button(cont);
                        if (search == null || search == "") {
                                BtnSecond.setText(Title[i]);

                        } else {
                                for (; i < Title.length; i++)
                                        if (Title[i].contains(search)) {
                                                BtnSecond.setText(Title[i]);
                                                break;
                                        }
                        }
                        if (i >= Title.length)
                                break;
                        BtnSecond.setId(i++);
                        BtnSecond.setLayoutParams(BtnParams);
                        BtnSecond.setBackground(ContextCompat.getDrawable(cont, R.drawable.theme));
                        BtnSecond.setTextSize(25);
                        BtnSecond.setTextColor(ContextCompat.getColor(cont, R.color.white));
                        BtnSecond.setOnClickListener(SoreView_CLick);
                        Layout.addView(BtnSecond);


                        layout.addView(Layout);

                }
                Space sp = new Space(cont);
                LinearLayout.LayoutParams spParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 90);
                sp.setLayoutParams(spParams);
                layout.addView(sp);
        }

        private View.OnClickListener SoreView_CLick = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        Intent intent = new Intent(cont, ConentViewerActivity.class);
                        Animation anim = AnimationUtils.loadAnimation(cont, R.anim.presses_state);
                        v.startAnimation(anim);
                        Button btn = (Button) v;
                        int ListNum = (btn.getId()) + 1;
                        intent.putExtra(ConstantParams.List_Num, ListNum);
                        intent.putExtra("List", ListType);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        cont.startActivity(intent);
                }
        };
}
