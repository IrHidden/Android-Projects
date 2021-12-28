package ir.myapp.controller3.service;

import android.content.Context;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import ir.myapp.controller3.tools.CommenFuntions;
import ir.myapp.controller3.tools.WifiDigest_Func;

import static java.lang.Thread.sleep;
import static ir.myapp.controller3.tools.CommenFuntions.ControllerIPAdress;
import static ir.myapp.controller3.tools.CommenFuntions.ControllerPort;

public class WifiDataTransfer_Service {

    public static class WifiSocketClint extends Thread {
        private Socket soc;
        Context cont;

        public WifiSocketClint(Context cont) {
            this.cont = cont;
        }

        @Override
        public void run() {
            try {
                Get_ControllerData();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void Get_ControllerData() throws IOException {
            try {
                soc = new Socket(ControllerIPAdress, ControllerPort);
                while (true) {
                    Send_Data(soc, "SendData."); //Sending Request to Controller

                    String Msg = Get_Data(soc);
                    new WifiDigest_Func(Msg,cont); // Digest then InsertData to Database;
                    System.out.println(Msg); // Checking Data

                    Send_Data(soc, new CommenFuntions().getSystemInfo(cont));

                    Send_Data(soc, new CommenFuntions().getRelayInfo(cont));

                    sleep(10000);
                }
            } catch (Exception e) {
                System.out.println(e.toString());
            } finally {
                soc.close();
            }
        }
        public void Send_Data(Socket soc, String Msg)  {
            try {
                System.out.println("Sending(Wifi): " + Msg);
                sleep(500);
                DataOutputStream dos = new DataOutputStream(soc.getOutputStream());
                dos.write(Msg.getBytes(StandardCharsets.UTF_8));
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }

        private String Get_Data(Socket soc)  {
            try {
                sleep(300);
                DataInputStream dis = new DataInputStream(soc.getInputStream());
                CommenFuntions ComFunc = new CommenFuntions();
                String Msg = ComFunc.StringValueOfInputStream(dis); // Getting Data From Wifi
                return Msg;
            }catch (Exception e){
                System.out.println(e.getMessage());
                return  null;
            }
        }
    }


    public static class WifiSocketServer extends Thread {

        Context cont;

        ServerSocket Server;
        Socket Client;

        public WifiSocketServer(Context cont) {
            this.cont = cont;
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
                for (int result = bis.read(); result != -1; result = bis.read()) {
                    if ((char) result != '.')
                        buf.write((byte) result);
                    else
                        break;
                }
                Client.close();
                new WifiDigest_Func(String.valueOf(buf), cont);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }

        }

        public void SendToClient() {
            // TODO
        }

    }
}
