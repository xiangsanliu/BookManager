package com.bookmanager.eidian.bookmanager.Helpers;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by xiang on 2017/1/17.
 */

public class PEGetter {
    public static final String SOUTH_LAKE_MAIN_URL =  "http://211.69.129.116:8501/login.do";
    public static final String SOUTH_LAKE_URL = "http://211.69.129.116:8501/stu/StudentSportModify.do";
    public static final String SCORE_URL = "http://211.69.129.116:8501/stu/viewresult.do";
    public static String peCookie;

    public static void setPeCookie(String account, String password) throws IOException {
        OkHttpClient client = new OkHttpClient();
        FormBody body = new FormBody.Builder()
                .add("username", account)
                .add("password", password)
                .add("btnlogin.x", "40")
                .add("btnlogin.y", "22")
                .build();
        Request request = new Request.Builder()
                .url(SOUTH_LAKE_MAIN_URL)
                .post(body)
                .addHeader("Accept","text/html,application" +
                        "/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                .addHeader("Connection","keep-alive")
                .addHeader("Host", "211.69.129.116:8501")
                .addHeader("Referer", "http://211.69.129.116:8501/login.do")
                .build();
        Response response = client.newCall(request).execute();
        peCookie = response.header("Set-Cookie");
        peCookie = peCookie.substring(0, peCookie.indexOf(';'));
    }

    public static String getSouthLake(String account, String password) throws IOException {
        if (peCookie == null) {
            setPeCookie(account, password);
        }
        String result = "";
        Document document = Jsoup.connect(SOUTH_LAKE_URL)
                .header("Referer","http://211.69.129.116:8501" +
                        "/jsp/menuForwardContent.jsp?url=stu/StudentSportModify.do")
                .header("Host","211.69.129.116:8501")
                .header("Cookie",peCookie)
                .get();
        Elements elements = document.getElementsByClass("fd");
        Log.d("document", document.text());
        if (elements.size() != 0 ) {
            result = result + "学号： " + elements.get(1).text() + "\n";
            result = result + "姓名：" + elements.get(2).text() + "\n";
            result = result + "已完成圈数：" + elements.get(7).text() + "\n";
        } else {
            result = "fail";
        }
        return result;
    }

    public static String getPEScore(String account, String password) throws IOException {
        if (peCookie == null) {
            setPeCookie(account, password);
        }
        String result = "";
        Document document = Jsoup.connect(SCORE_URL)
                .header("Cookie", peCookie)
                .header("Host", "211.69.129.116:8501")
                .header("Referer", "http://211.69.129.116:8501/jsp/me" +
                        "nuForwardContent.jsp?url=stu/viewresult.do").get();
        Elements elements = document.getElementsByClass("fd");
        if (elements.size() != 0) {
            result = result + "姓名     :\t" + elements.get(0).text() + "\n"
                            + "学号     :\t" + elements.get(2).text() + "\n"
                            + "班级     :\t" + elements.get(6).text() + "\n"
                            + "教师     :\t" + elements.get(7).text() + "\n"
                            + "成绩     :\t" + elements.get(9).text() + "\n";
        } else  {
            result = "fail";
        }
        return  result;
    }
}
