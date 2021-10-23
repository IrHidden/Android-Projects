package ir.MasjedEmamAli.Urmia.services;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Tools {
    public String StringValueOf(InputStream Input)
    {
        try {
            BufferedInputStream bis = new BufferedInputStream(Input);

            ByteArrayOutputStream buf = new ByteArrayOutputStream();

            for (int result = bis.read(); result!= -1 ; result = bis.read())
                buf.write((byte) result);
            String s=String.valueOf(buf);

            bis.close();
            buf.close();

            return s;
        } catch (Exception e) {
            System.out.println("ErrorDebufing: " + e.getMessage());
            return null;
        }
    }
    public String GetApi(String url_str)
    {
        try {
            URL url = new URL(url_str);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            String s=StringValueOf(con.getInputStream());
            con.disconnect();
            return s;
        } catch (Exception e) {
            System.out.println("ErrorGet: " + e.getMessage());
        }


        return null;
    }
}
