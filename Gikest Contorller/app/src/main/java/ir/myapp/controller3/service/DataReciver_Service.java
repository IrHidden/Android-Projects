package ir.myapp.controller3.service;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import ir.myapp.controller3.tools.CommenFuntions;
import ir.myapp.controller3.tools.WifiDigest_Func;

import static java.lang.Thread.sleep;

public class DataReciver_Service {

    public static class Wifi_Clint_Receive extends AsyncTask<String, Void, Void> {
        private Socket soc;

        @Override
        protected Void doInBackground(String... str) {
            try {
                while (true) {
                    soc = new Socket(str[0], Integer.getInteger(str[1]));
                    DataInputStream dos = new DataInputStream(soc.getInputStream());
                    System.out.println(dos.readUTF());
                }
            } catch (Exception e) {
                System.out.println(e.toString());
            } finally {
                try {
                    soc.close();
                } catch (Exception e) {
                }
            }
            return null;
        }
    }

    public static class Wifi_Clint_Send extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... str) {
            Socket soc = null;
            try {
                soc = new Socket(str[0], Integer.getInteger(str[1]));
                DataOutputStream dos = new DataOutputStream(soc.getOutputStream());
                dos.writeUTF(str[2]);
            } catch (Exception ignored) {
            } finally {
                try {
                    soc.close();
                } catch (Exception ignored) {
                }
            }
            return null;
        }
    }

    public static class WifiSocketServer extends Thread {

        Context cont;

        ServerSocket Server;
        Socket Client;
        CommenFuntions ComFuc;

        public WifiSocketServer(Context cont)
        {
            this.cont=cont;
        }
        @Override
        public void run() {
            try {
                sleep(500);
                StartSocketServer();
                while (true) {
                    AcceptClient();
                    ListenToClient();
                    sleep(3000);
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        public void StartSocketServer() {
            try {
                Server = new ServerSocket(5712);
                //Toast.makeText(cont, "Listening On Port 5712.", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        public void AcceptClient() {
            try {
                if (!Server.isClosed()) {
                    Client = Server.accept();
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        public void ListenToClient() {
            try {
                BufferedInputStream bis = new BufferedInputStream(Client.getInputStream());
                ByteArrayOutputStream buf = new ByteArrayOutputStream();
                for (int result = bis.read(); result != -1 ; result = bis.read()) {
                    if((char) result!='.')
                    buf.write((byte) result);
                    else
                        break;
                }
                        Client.close();
                new WifiDigest_Func(String.valueOf(buf),cont);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }

        }

        public void SendToClient()
        {

        }

    }

}
