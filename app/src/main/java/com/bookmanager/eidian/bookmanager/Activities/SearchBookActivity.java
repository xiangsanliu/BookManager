package com.bookmanager.eidian.bookmanager.Activities;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.bookmanager.eidian.bookmanager.Adapters.BookAdapter;
import com.bookmanager.eidian.bookmanager.Entities.Book;
import com.bookmanager.eidian.bookmanager.Helpers.BaseActivity;
import com.bookmanager.eidian.bookmanager.Helpers.InternetConnection;
import com.bookmanager.eidian.bookmanager.R;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class SearchBookActivity extends BaseActivity {

    private List<Book> bookList ;

    static final int SHOW_CHINESE_RESPONSE = 1;

    static final int SHOW_WESTERN_RESPONSE = 0;

    private ListView listView;

    private String path;

    BookAdapter adapter1;

    BookAdapter adapter;


    MaterialRefreshLayout materialRefreshLayout ;

    //记录第几页
    int page = 1;

    private Handler handler = new Handler(){
        public void handleMessage(Message message){
            switch (message.what){
                case SHOW_CHINESE_RESPONSE:
                    Object responseChinese =  message.obj;
                    adapter = new BookAdapter(SearchBookActivity.this, R.layout.item_book_view, (List<Book>) responseChinese);
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Book book = bookList.get(i);
                            Intent intent = new Intent(SearchBookActivity.this, ShowBookChineseInfo.class);
                            intent.putExtra("path",path);
                            intent.putExtra("titleUrl",book.getTitleUrl());
                            startActivity(intent);
                        }
                    });
                    break;
                case SHOW_WESTERN_RESPONSE:
                    Object responseWestern = message.obj;
                    adapter1 = new BookAdapter(SearchBookActivity.this,R.layout.item_book_view, (List<Book>) responseWestern);
                    listView.setAdapter(adapter1);
                    adapter1.notifyDataSetChanged();
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Book book = bookList.get(i);
                            String titleWesternUrl = book.getTitleUrl();
                            Intent intent = new Intent(SearchBookActivity.this,ShowBookWesternInfo.class);
                            intent.putExtra("titleWesternUrl",titleWesternUrl);
                            intent.putExtra("path",path);
                            startActivity(intent);
                        }
                    });
                    break;
                case 3:
                    if (adapter!= null){
                        adapter.notifyDataSetChanged();
                    }
                    if (adapter1!= null){
                        adapter1.notifyDataSetChanged();
                    }
                    materialRefreshLayout.finishRefreshLoadMore();

                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_book);

        listView = (ListView) findViewById(R.id.show_result);
        materialRefreshLayout = (MaterialRefreshLayout) findViewById(R.id.refresh);
        //接受传来的数据
        Intent intent = getIntent();
        final String builder = intent.getStringExtra("search_extra");
        Boolean isChinese = intent.getBooleanExtra("isChinese", true);
        String book = intent.getStringExtra("search_content");

        final int[] i = {builder.length()};
        final String str = builder.substring(0, i[0] -2);
        final String ptr = str.substring(0,i[0]-8);

        if (isChinese) {
            searchChinese(str, builder, book);
        } else {
            searchEnglish(str, builder, book);
        }

        materialRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                materialRefreshLayout.finishRefresh();

            }

            @Override
            public void onRefreshLoadMore(final MaterialRefreshLayout materialRefreshLayout) {
                super.onRefreshLoadMore(materialRefreshLayout);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        page+=10;
                        String path3 = ptr+"short-jump&jump="+page+"&pag=now";
                        InternetConnection inter = new InternetConnection(path3,builder);
                        Document document = Jsoup.parse(inter.getResponse());

                        Elements elements = document.getElementsByClass("items");
                        //将搜索结果归类
                        for (int i =0;i<elements.size();i++){
                            Book book1 = new Book();
                            Element element = elements.get(i);
                            String titleUrl = element.select("div.itemtitle a").attr("href");
                            String title = element.select("div.itemtitle a").text();
                            String author = element.select("td.content").first().text();
                            String callNumber = element.select("td.content").get(1).text();
                            String publisher = element.select("td.content").get(2).text();
                            String publishTime = element.select("td.content").get(3).text();
                            book1.setBookName(title);
                            book1.setAuthor("作者: "+author);
                            book1.setCallNumber("索书号: "+callNumber);
                            book1.setPublisher("出版社: "+publisher);
                            book1.setPublishTime("出版年份: "+publishTime);
                            book1.setTitleUrl(titleUrl);
                            bookList.add(book1);
                        }


                        Message message = new Message();
                        message.what = 3;
                        message.obj = bookList;
                        handler.sendMessage(message);
                    }
                }).start();
            }
        });


    }

    public void searchChinese(String str, final String builder, String book) {
        page = 1;
        path = str+"&find_code=WRD&request="+book+"&local_base=HZA01&filter_code_1=WLN&filter_request_1=&filter_code_2=WYR&filter_request_2=&filter_code_3=" +
                "WYR&filter_request_3=&filter_code_4=WFM&filter_request_4=&filter_code_5=WSL&filter_request_5=";
        new Thread(new Runnable() {
            @Override
            public void run() {
                InternetConnection inter = new InternetConnection(path,builder);
                String response = inter.getResponse();

                Document document = Jsoup.parse(response);

                Elements elements = document.getElementsByClass("items");
                //将搜索结果归类
                bookList = new ArrayList<Book>();
                for (int i =0;i<elements.size();i++){
                    Book book = new Book();
                    Element element = elements.get(i);
                    String bookImag = element.select("td.cover").attr("src");
                    String titleUrl = element.select("div.itemtitle a").attr("href");
                    String title = element.select("div.itemtitle a").text();
                    String author = element.select("td.content").first().text();
                    String callNumber = element.select("td.content").get(1).text();
                    String publisher = element.select("td.content").get(2).text();
                    String publishTime = element.select("td.content").get(3).text();
                    book.setBookName(title);
                    book.setAuthor("作者: "+author);
                    book.setCallNumber("索书号: "+callNumber);
                    book.setPublisher("出版社: "+publisher);
                    book.setPublishTime("出版年份: "+publishTime);
                    book.setTitleUrl(titleUrl);
                    bookList.add(book);
                }

                Message message = new Message();
                message.what = SHOW_CHINESE_RESPONSE;
                message.obj = bookList;
                handler.sendMessage(message);
            }
        }).start();
    }

    public void searchEnglish(String str, final String builder, String book) {
        page = 1;
        final String path2 = str+"&find_code=WRD&request="+book+"&local_base=HZA09&filter_code_1=WLN&filter_request_1=&filter_code_2=WYR&filter_request_2=&filter_code_3=" +
                "WYR&filter_request_3=&filter_code_4=WFM&filter_request_4=&filter_code_5=WSL&filter_request_5=";
        new Thread(new Runnable() {
            @Override
            public void run() {
                InternetConnection inter = new InternetConnection(path2,builder);
                String response = inter.getResponse();

                Document document = Jsoup.parse(response);

                Elements elements = document.getElementsByClass("items");
                //将搜索结果归类
                bookList = new ArrayList<Book>();
                for (int i =0;i<elements.size();i++){
                    Book book1 = new Book();
                    Element element = elements.get(i);
                    String titleUrl = element.select("div.itemtitle a").attr("href");
                    String title = element.select("div.itemtitle a").text();
                    String author = element.select("td.content").first().text();
                    String callNumber = element.select("td.content").get(1).text();
                    String publisher = element.select("td.content").get(2).text();
                    String publishTime = element.select("td.content").get(3).text();
                    book1.setBookName(title);
                    book1.setAuthor("作者: "+author);
                    book1.setCallNumber("索书号: "+callNumber);
                    book1.setPublisher("出版社: "+publisher);
                    book1.setPublishTime("出版年份: "+publishTime);
                    book1.setTitleUrl(titleUrl);
                    bookList.add(book1);
                }

                Message message = new Message();
                message.what = SHOW_WESTERN_RESPONSE;
                message.obj = bookList;
                handler.sendMessage(message);
            }
        }).start();
    }
}
