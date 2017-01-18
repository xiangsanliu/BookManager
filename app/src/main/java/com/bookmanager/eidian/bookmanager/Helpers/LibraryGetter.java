package com.bookmanager.eidian.bookmanager.Helpers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by xiang on 2017/1/18.
 */

public class LibraryGetter {

    private static LibraryList libraryUrl = new LibraryList();

    private static final String MAIN_URL = "http://211.69.140.4:8991/F";
    private static final String HOST = "211.69.140.4:8991";
    private static final String REFERER = "http://211.69.140.4:8991/F/AANVD5RM5P3YS5519" +
            "8TBQ113HP3DQ4TVYPX57AP3GPIMUPQB1P-03630?func=file&file_name=login-session";
    private static final String ORIGIN = "http://211.69.140.4:8991";
    private static List<String> urlList = null;

    private static String loginUrl;
    private static OkHttpClient client;
    private static String name;


    private static void setLoginUrl() throws IOException {
        Document document = Jsoup.connect(MAIN_URL).get();
        Elements elements = document.getElementsByTag("a");
        libraryUrl.setMyLogin(elements.first().attr("href"));
        loginUrl = elements.first().attr("href");
    }

    public static List getLoginUrl(String account, String password) throws IOException {
        setLoginUrl();
        client = new OkHttpClient();
        FormBody body = new FormBody.Builder()
                .add("func", "login-session")
                .add("login_source", "bor-info")
                .add("bor_id", account)
                .add("bor_verification", password)
                .add("bor_library", "HZA50")
                .build();
        Request request = new Request.Builder()
                .url(loginUrl)
                .addHeader("Host", HOST)
                .addHeader("Referer", loginUrl)
                .addHeader("Origin", ORIGIN)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        Document document = Jsoup.parse(response.body().string());
        Element element = document.getElementById("header");
        Elements elements = document.select("td.td2");
        if (elements.size()>1) {
            name = elements.get(1).text();
        }
        String isLogined = element.getElementsByTag("a").first().text();
        if (isLogined.equals("退出")) {
            urlList = new ArrayList<>();
            urlList.add(element.childNode(5).attr("href"));     //我的图书馆
            urlList.add(element.childNode(16).attr("href"));    //热门推荐
            urlList.add(element.childNode(11).attr("href"));    //读者推荐
            urlList.add(element.childNode(20).attr("href"));    //搜索结果
            urlList.add(element.childNode(3).attr("href"));     //搜索
            urlList.add(loginUrl);                               //loginUrl
            urlList.add(name);                                   //存储名字
        }
        return urlList;
    }

}
