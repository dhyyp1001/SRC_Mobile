package com.auto.src_mobile.network_side;

import android.util.Log;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkOnvifController {
    private static final String TAG = "NetworkOnvifController : ";

    public NetworkOnvifController(String mediaUri, String x, String y, String home) {
        try {
            URL url = new URL(new ServerUrl().serverUrl+"onvifCont");//new UrlList().networkOnvifControllerUrl());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);

            DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
            dataOutputStream.write(mediaUri.getBytes());
            dataOutputStream.write(" ".getBytes());
            dataOutputStream.write(x.getBytes());
            dataOutputStream.write(" ".getBytes());
            dataOutputStream.write(y.getBytes());
            dataOutputStream.write(" ".getBytes());
            dataOutputStream.write(home.getBytes());
            dataOutputStream.flush();

            dataOutputStream.close();


            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                Log.i(TAG, "connection OK");
            } else {
                Log.i(TAG, "connection FAIL");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
