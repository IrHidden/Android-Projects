package ir.MasjedEmamAli.Urmia.services;

import static ir.MasjedEmamAli.Urmia.services.ConstantParams.SiteAddress;

import android.graphics.Bitmap;
import android.util.Base64;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class PostSending_PostApi extends Thread {

    public static String PostSendAddress = SiteAddress+"/uploadpost.php";

    String Title, Content, Type;
    Bitmap img_bitmap;

    public PostSending_PostApi(String Title, Bitmap img_bitmap, String Content, String Type) {
        this.Title = Title;
        this.Content = Content;
        this.img_bitmap = img_bitmap;
        this.Type = Type;
    }

    @Override
    public void run() {
        try {
            ByteArrayOutputStream ByteOut = new ByteArrayOutputStream();
            img_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ByteOut);
            String EncodedImg = Base64.encodeToString(ByteOut.toByteArray(), Base64.DEFAULT);

            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("title", Title));
            params.add(new BasicNameValuePair("image", EncodedImg));
            params.add(new BasicNameValuePair("content", Content));
            params.add(new BasicNameValuePair("type", Type));
            System.out.println("Doing Hard Part...");

            HttpParams httpParams = getHttpParams();
            HttpClient client = new DefaultHttpClient(httpParams);
            HttpPost post = new HttpPost(PostSendAddress);

            post.setEntity(new UrlEncodedFormEntity(params,"UTF-8"));
            client.execute(post);
            System.out.println("Done");

        } catch (Exception e) {
            System.out.println("Error Sending Post: " + e.getMessage());
        }
    }

    public HttpParams getHttpParams() {
        HttpParams httpParam = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParam, 30000);
        HttpConnectionParams.setSoTimeout(httpParam, 30000);
        return httpParam;
    }
}
