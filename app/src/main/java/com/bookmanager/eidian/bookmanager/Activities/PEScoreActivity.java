package com.bookmanager.eidian.bookmanager.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.bookmanager.eidian.bookmanager.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import at.markushi.ui.CircleButton;

public class PEScoreActivity extends AppCompatActivity implements View.OnClickListener{

    public static final int SHOW_RESPONSE = 0;

    private SharedPreferences pref;

    private SharedPreferences.Editor editor;

    private EditText accountEdit;

    private EditText passwordEdit;

    private CircleButton login;

    private CheckBox rememberPass;

    private Handler handler = new Handler(){

        public void handleMessage(Message msg){
            switch (msg.what){
                case SHOW_RESPONSE:
                    Toast.makeText(PEScoreActivity.this, "查询成功", Toast.LENGTH_SHORT).show();
                    String response = (String) msg.obj;
                    if (response.equals("fail")){
                        Toast.makeText(PEScoreActivity.this,
                                "查询失败,学号或密码错误",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(PEScoreActivity.this);
                        builder.setMessage(response)
                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // FIRE ZE MISSILES!
                                    }
                                });
                        builder.create().show();
                    }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pescore);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        accountEdit = (EditText) findViewById(R.id.input_account);
        passwordEdit = (EditText) findViewById(R.id.input_password);
        rememberPass = (CheckBox) findViewById(R.id.remember_pass);
        login = (CircleButton) findViewById(R.id.login_button);
        boolean isRemember = pref.getBoolean("remember_password_southlake",false);
        if (isRemember){
            //将账号和密码都设置到文本框中
            String account = pref.getString("account_southlake","");
            String password = pref.getString("password_southlake","");
            accountEdit.setText(account);
            passwordEdit.setText(password);
            rememberPass.setChecked(true);
        }
        login.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(passwordEdit.getWindowToken(), 0);
        Toast.makeText(PEScoreActivity.this, "查询中...", Toast.LENGTH_SHORT).show();

        new Thread(new Runnable() {
            String account = accountEdit.getText().toString();
            String password = passwordEdit.getText().toString();

            StringBuilder datas = new StringBuilder();
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    String path = "http://211.69.129.116:8501/login.do";
                    URL url = new URL(path);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setConnectTimeout(8000);
                    connection.setRequestProperty("Accept","text/html,application" +
                            "/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
                    connection.setRequestProperty("Connection","keep-alive");
                    connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
                    connection.setRequestProperty("Host", "211.69.129.116:8501");
                    connection.setRequestProperty("Referer", "http://211.69.129.116:8501/login.do");
                    connection.setRequestProperty("User-Agent",
                            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko)" +
                                    " Chrome/52.0.2743.116 Safari/537.36");
                    connection.setRequestMethod("POST");
                    StringBuilder sb = new StringBuilder();
                    sb.append("username="+ URLEncoder.encode(account, "UTF-8"))
                            .append("&password="+URLEncoder.encode(password, "UTF-8"))
                            .append("&btnlogin.x=41")
                            .append("&btnlogin.y=8");
                    String parame = sb.toString();
                    byte[] postParame = parame.getBytes();
                    //发送请求参数
                    DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
                    dos.write(postParame);
                    dos.flush();
                    dos.close();
                    if (connection.getResponseCode() == connection.HTTP_OK ) {
                        String cookie = connection.getHeaderField("Set-Cookie");
                        cookie = cookie.substring(0, cookie.indexOf(';'));
                        Document document = Jsoup.connect("http://211.69.129.116:8501/stu/viewresult.do")
                                .header("Cookie", cookie)
                                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                                .header("Connection", "keep-alive")
                                .header("Host", "211.69.129.116:8501")
                                .header("Referer", "http://211.69.129.116:8501/jsp/menuForwardContent.jsp?url=stu/viewresult.do")
                                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36")
                                .header("Accept-Encoding", "gzip, deflate, sdch")
                                .header("Cache-Control", "max-age=0")
                                .header("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6")
                                .header("Upgrade-Insecure-Requests", "1").get();
                        Elements elements = document.getElementsByClass("fd");
                        if (elements.size() != 0) {
                            //实现记住密码
                            editor = pref.edit();
                            if (rememberPass.isChecked()){
                                editor.putBoolean("remember_password_southlake",true);
                                editor.putString("account_southlake",account);
                                editor.putString("password_southlake",password);
                            }else {
                                editor.clear();
                            }
                            editor.commit();
                            //从节点中取出所需数据
                            if (elements.size() != 0) {
                                //从节点中取出所需数据
                                datas.append("姓名     :  " + elements.get(0).text() + "\n");
                                datas.append("学号     :  " + elements.get(2).text() + "\n");
                                datas.append("班级     :  " + elements.get(3).text() + "\n");
                                datas.append("体育班 :" + "\t" + elements.get(6).text() + "\n");
                                datas.append("教师     :  " + elements.get(7).text() + "\n");
                                datas.append("成绩     :  " + elements.get(9).text() + "\n");
                            }else {
                                datas.append("fail");
                            }
                        }else {
                            datas.append("fail");
                        }
                    }else {
                        datas.append("登录失败");
                    }
                    Message message = new Message();
                    message.what = SHOW_RESPONSE;
                    //将服务器返回的数据存放到Message中
                    message.obj = datas.toString();
                    handler.sendMessage(message);

                } catch(MalformedURLException e)  {
                    e.printStackTrace();
                } catch(IOException e)  {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}
