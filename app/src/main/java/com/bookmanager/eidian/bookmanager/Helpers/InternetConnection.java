package com.bookmanager.eidian.bookmanager.Helpers;


import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by clmiberf on 2016/9/20.
 */
public class InternetConnection {

    private URL url;

    private String response;


    public String getResponse() {
        return response;
    }

    public InternetConnection(String path, String strf) {
        try {
            url = new URL(path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Accept","text/html," +
                    "application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            connection.setRequestProperty("Host","211.69.140.4:8991");
            connection.setRequestProperty("Upgrade-Insecure-Requests","1");
//            connection.setRequestProperty("Referer",referer);
            connection.setRequestProperty("User-Agent","Mozilla/" +
                    "5.0 (Windows NT 10.0; WOW64; rv:48.0) Gecko/20100101 Firefox/48.0");
            connection.setRequestProperty("Referer",strf);
            connection.setRequestMethod("GET");
            if (connection.getResponseCode() == 200){
                InputStream inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder str = new StringBuilder();
                String line;
                while((line = reader.readLine()) != null){
                    str.append(line);
                }
                response = str.toString();

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
