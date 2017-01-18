package com.bookmanager.eidian.bookmanager.Helpers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by xiang on 2017/1/17.
 */

public class JWGetter {
    public static String cookie;
    public static OkHttpClient okHttpClient;
    public static final String MAIN_URL = "http://jw.hzau.edu.cn/";
    public static final String POST_URL = "http://jw.hzau.edu.cn/default2.aspx";
    public static final String GET_CODE_URL = "http://jw.hzau.edu.cn/CheckCode.aspx";
    public static String examPlanUrl, courseTableUrl ;

    public static String getViewState() throws IOException {
        Document document = Jsoup.connect(MAIN_URL).get();
        String viewState = document.getElementsByTag("input").attr("value");
        return viewState;
    }

    public static Bitmap getCodeBitmap() throws IOException {
        okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(GET_CODE_URL).build();
        Call call = okHttpClient.newCall(request);
        Response response = call.execute();
        cookie =  response.header("Set-Cookie") ;
        cookie = cookie.substring(0, cookie.indexOf(';'));
        byte[] bytes = response.body().bytes();
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public static void setUrl(String account, String password, String code) throws IOException {
        FormBody body = new FormBody.Builder()
                .add("__VIEWSTATE", getViewState())
                .add("txtUserName", account)
                .add("TextBox2", password)
                .add("txtSecretCode", code)
                .add("RadioButtonList1", "学生")
                .add("Button1", "")
                .add("lbLanguage", "")
                .add("hidPdrs", "")
                .add("hidsc", "")
                .build();
        Request request = new Request.Builder()
                .url(POST_URL)
                .post(body)
                .addHeader("Cookie", cookie)
                .build();
        Response urlResponse = okHttpClient.newCall(request).execute();
        Document urlDocument = Jsoup.parse(urlResponse.body().string());
        Elements urlElements = urlDocument.getElementsByAttributeValue("target", "zhuti");
        examPlanUrl = MAIN_URL + urlElements.get(11).attr("href");
        courseTableUrl = MAIN_URL + urlElements.get(11).attr("href");
    }

    public static Document getCourseDocument(String account, String password, String code) throws IOException {
        if (courseTableUrl == null) {
            setUrl(account, password, code);
        }
        Request courseRequest = new Request.Builder()
                .url(courseTableUrl)
                .addHeader("Cookie", cookie)
                .addHeader("Referer", "http://jw.hzau.edu.cn/xs_main.aspx?xh="+account)
                .build();
        Response courseResponse = okHttpClient.newCall(courseRequest).execute();
        return Jsoup.parse(courseResponse.body().string());
    }

    public static Elements getCourse(String account, String password, String code) throws IOException {
        Document document = getCourseDocument(account, password, code);
        Elements elements = document.select("table#Table1.blacktab tr");
        Log.d("text", elements.size()+"");
        //移除无效的数据
        elements.remove(13);
        elements.remove(11);
        elements.remove(9);
        elements.remove(7);
        elements.remove(5);
        elements.remove(3);
        elements.remove(1);
        elements.remove(0);
        String[][] course = new String[6][7];
//        for (int i = 0; i < elements.size(); i++) {
//            Elements elements1 = elements.get(i).select("td");
//            if (i == 0 || i == 2 || i == 4) {
//                elements1.remove(0);
//                elements1.remove(0);
//            } else {
//                elements1.remove(0);
//            }
//            Log.d("size", elements1.size()+"");
//            for (int j = 0; j < elements1.size(); j++) {
//                String text = elements1.get(j).text();
//                course[i][j] = text;
//                Log.d("text"+i+j, text);
//            }
//        }
        return elements;
    }

}
