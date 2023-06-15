package com.auto.src_mobile.network_side;

import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;

public class NetworkModbusWrite {
    private static final String TAG = "NetworkModbusRead : ";
    public static String[] responseArr;

    public NetworkModbusWrite(String modIP, String stopStartBit) {
        try {
            URL url = new URL(new ServerUrl().serverUrl + "modbusWrite");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setConnectTimeout(5000);
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);

            DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
            dataOutputStream.write((modIP + "/" + stopStartBit).getBytes());
            dataOutputStream.flush();

            dataOutputStream.close();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                Log.i(TAG, "modbus OK");
            } else {
                Log.i(TAG, "modbus FAIL");
            }
        } catch (ProtocolException e) {
            e.printStackTrace();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (SocketTimeoutException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
