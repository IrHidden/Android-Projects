package ir.myapp.controller3.tools;

import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class DataTransferMethods {

    CommenFuntions ComFuc;

    public DataTransferMethods() {
        ComFuc = new CommenFuntions();
    }

    public String Send(String Arguments) {
        try {
            String data = "Apikey=5712&Username=TestUser1&Password=123456&TempValue=534";
            URL url = new URL("http://gikest.ir/value-inserter/");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.getOutputStream().write(data.getBytes(StandardCharsets.UTF_8));
            return ComFuc.StringValueOfInputStream(con.getInputStream());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }

}
