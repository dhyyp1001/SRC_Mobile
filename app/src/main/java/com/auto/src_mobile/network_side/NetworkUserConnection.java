package com.auto.src_mobile.network_side;

import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkUserConnection {
    private static final String TAG = "NetworkUserConnection : ";
    public String response = "";
    public String okSign = "";
    public static String listValues = "";

    public NetworkUserConnection(String id, String pw) {
        try {
            String[]responseArr;
            String userName = id;
            String password = pw;
            URL url = new URL(new UrlList().networkUserConnectionUrl());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);

            DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
            dataOutputStream.write(userName.getBytes());
            dataOutputStream.write(" ".getBytes());
            dataOutputStream.write(password.getBytes());
            dataOutputStream.flush();

            DataInputStream dataInputStream = new DataInputStream(connection.getInputStream());
            byte[] buffer = new byte[1024];
            int length;
            while ((length = dataInputStream.read(buffer)) > 0) {
                response += new String(buffer, 0, length);
            }
            dataOutputStream.close();
            dataInputStream.close();

            responseArr = response.split("//////");
            okSign = responseArr[0];
            listValues = responseArr[1];//리스트값

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                Log.i(TAG, "Login OK");
                System.out.println(listValues);
            } else {
                Log.i(TAG, "Login FAIL");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
