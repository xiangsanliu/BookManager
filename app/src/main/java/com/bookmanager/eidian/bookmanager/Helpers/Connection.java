package com.bookmanager.eidian.bookmanager.Helpers;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by xiang on 2016/9/25/025.
 */

public class Connection {


    public static HttpURLConnection getConnectionToHZAUlib(String u) throws Exception {

        URL url = new URL(u);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,a" +
                "pplication/xml;q=0.9,image/webp,*/*;q=0.8");
        connection.setRequestProperty("Cache-Control", "max-age=0");
        connection.setRequestProperty("Connection", "keep-alive");
        connection.setRequestProperty("Host", "lib.hzau.edu.cn");
        connection.setRequestProperty("Upgrade-Insecure-Requests", "1");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0;" +
                " WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.116 Safari/537.36");

        return connection;
    }

}
