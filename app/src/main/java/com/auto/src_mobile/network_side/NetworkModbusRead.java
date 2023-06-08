package com.auto.src_mobile.network_side;

import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkModbusRead {
    private static final String TAG = "NetworkModbusRead : ";
    public static String[] responseArr;
    public String response = "";

    public NetworkModbusRead(String modIP) {
        try {
            URL url = new URL(new ServerUrl().serverUrl + "modbusRead");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);

            DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
            dataOutputStream.write(modIP.getBytes());
            dataOutputStream.flush();

            DataInputStream dataInputStream = new DataInputStream(connection.getInputStream());
            byte[] buffer = new byte[1024];
            int length;
            while ((length = dataInputStream.read(buffer)) > 0) {
                response += new String(buffer, 0, length);
            }
            dataOutputStream.close();
            dataInputStream.close();

            responseArr = response.split("/");
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                Log.i(TAG, "Login OK");
            } else {
                Log.i(TAG, "Login FAIL");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
