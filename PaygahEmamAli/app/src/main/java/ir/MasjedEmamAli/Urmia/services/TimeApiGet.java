package ir.MasjedEmamAli.Urmia.services;

import static ir.MasjedEmamAli.Urmia.services.ConstantParams.TimeAddress;

import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class TimeApiGet extends Thread{

    String t = "";
    TextView txt_AzanSob,txt_Tolu, txt_AzanZohr, txt_Gorub, txt_AzanMaghreb, txt_NimeShab, txt_daynow;
    public TimeApiGet(TextView txt_AzanSob,
                      TextView txt_Tolu,
                      TextView txt_AzanZohr,
                      TextView txt_Gorub,
                      TextView txt_AzanMaghreb,
                      TextView txt_NimeShab,
                      TextView txt_daynow) {
        this.txt_AzanSob=txt_AzanSob;
        this.txt_Tolu=txt_Tolu;
        this.txt_AzanZohr=txt_AzanZohr;
        this.txt_Gorub=txt_Gorub;
        this.txt_AzanMaghreb=txt_AzanMaghreb;
        this.txt_NimeShab=txt_NimeShab;
        this.txt_daynow=txt_daynow;

    }

    @Override
    public void run() {

        String ParyTimes = new Tools().GetApi(TimeAddress);

        try {
            JSONObject Json=new JSONObject(ParyTimes);

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    try {
                        txt_AzanSob.setText(Json.getString("Imsaak"));
                        txt_Tolu.setText(Json.getString("Sunrise"));
                        txt_AzanZohr.setText(Json.getString("Noon"));
                        txt_Gorub.setText(Json.getString("Sunset"));
                        txt_AzanMaghreb.setText(Json.getString("Maghreb"));
                        txt_NimeShab.setText(Json.getString("Midnight"));
                        txt_daynow.setText(Json.getString("TodayQamari"));
                    }
                    catch (Exception e)
                    {

                    }
                }
            });
        } catch (JSONException e) {
            System.out.println("Json Error:"+e.getMessage());
        }


    }

}
