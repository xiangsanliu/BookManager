package com.xiang.text4.Activities;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.xiang.text4.R;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import at.markushi.ui.CircleButton;


public class SouthLakeActivity extends AppCompatActivity implements View.OnClickListener {
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
                    String response = (String) msg.obj;
                    if (response.equals("fail")){
                        Toast.makeText(SouthLakeActivity.this,
                                "查询失败,学号或密码错误",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(SouthLakeActivity.this);
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_south_lake);

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
//                    connection.setDoOutput(true);
//                    connection.setUseCaches(false);
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

//                    connection.setDoInput(true);

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
                    String cookieVal = null;
                    String key = null;
                    String cookie = null;
                    int n=0;
                    if (connection.getResponseCode() == connection.HTTP_OK ) {
                        for (int i=1;(key=connection.getHeaderFieldKey(i))!=null;i++){
                            if (key.equalsIgnoreCase("Set-Cookie")) {
                                cookieVal = connection.getHeaderField(i);
                                cookieVal = cookieVal.substring(0, cookieVal.indexOf(";"));
                                while (n == 0) {
                                    cookie = cookieVal;
                                    n++;
                                }
                            }
                        }

                        URL urlNext = new URL("http://211.69.129.116:8501/stu/StudentSportModify.do");
                        HttpURLConnection connectionNext = (HttpURLConnection) urlNext.openConnection();
                        connectionNext.setRequestMethod("GET");
                        connectionNext.setConnectTimeout(10000);
                        connectionNext.setRequestProperty("Accept","text/html,application/" +
                                "xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
                        connectionNext.setRequestProperty("Accept-Language","zh-CN,zh;q=0.8");
                        connectionNext.setRequestProperty("Referer","http://211.69.129.116:8501" +
                                "/jsp/menuForwardContent.jsp?url=stu/StudentSportModify.do");
                        connectionNext.setRequestProperty("Host","211.69.129.116:8501");
                        connectionNext.setRequestProperty("Cookie",cookie);
                        if (connectionNext.getResponseCode() == 200){
                            InputStream in = connectionNext.getInputStream();
                            //获取返回的数据
                            BufferedReader reader = new BufferedReader(new InputStreamReader(in,"UTF-8"));
                            StringBuilder response = new StringBuilder();

                            String line;
                            while ((line = reader.readLine()) != null) {
                                response.append(line);
                            }
                            String saxParse = response.toString();

                            //解析返回的数据
                            org.jsoup.nodes.Document document = Jsoup.parse(saxParse);

                            Elements element1 = null;
                            element1 = document.getElementsByClass("fd");
                            if (element1.size() != 0) {
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

                                String id = element1.get(1).text();
                                datas.append("学号 : " + id);
                                datas.append("\n");
                                String name = element1.get(2).text();
                                datas.append("姓名 : " + name.toString());
                                datas.append("\n");
                                //如果一圈也没完成
                                String score = null;
                                if (element1.size() == 10) {
                                    score = element1.get(7).text();
                                }else {
                                    score = "0";
                                }
                                datas.append("已完成圈数 : " + score);
                            }else {
                                datas.append("fail");
                            }

                        }else {
                            datas.append(" ");
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
