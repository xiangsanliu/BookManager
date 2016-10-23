package com.bookmanager.eidian.bookmanager.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.bookmanager.eidian.bookmanager.Adapters.BmobQueryBookAdapter;
import com.bookmanager.eidian.bookmanager.Entities.ReaderBook;
import com.bookmanager.eidian.bookmanager.R;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * 查询读者添加的书籍
 * Created by clmiberf on 2016/10/23.
 */

public class BmobReaderQueryBook extends AppCompatActivity {

    private Button addButton;

    private Button queryBookButton;

    private EditText bNameEdit;

    private List<ReaderBook> queryBookList;

    private ListView queryBookListView;

    private BmobQueryBookAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.bmob_reader_query_book);
        Bmob.initialize(this,"d882083bc95b77c5c072cbcc4264078d");
        addButton = (Button) findViewById(R.id.bmob_query_add_button);
        queryBookButton = (Button) findViewById(R.id.bmob_query_book_button);
        bNameEdit = (EditText) findViewById(R.id.bmob_query_book_edit);
        queryBookListView = (ListView) findViewById(R.id.bmob_query_book_listview);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bName = bNameEdit.getText().toString();
                BmobQuery<ReaderBook> bmobQuery = new BmobQuery<ReaderBook>();
                //查询"bName"为bName的数据
                bmobQuery.addWhereEqualTo("bName",bName);
                //执行查询方法
                bmobQuery.findObjects(new FindListener<ReaderBook>() {
                    @Override
                    public void done(List<ReaderBook> list, BmobException e) {
                        //如果查询成功
                        if (e == null){
                            ReaderBook readerBook;
                            queryBookList = new ArrayList<ReaderBook>();
                            for (ReaderBook readerBook1 : list){
                                readerBook = new ReaderBook();
                                readerBook.setbName(readerBook1.getbName());
                                readerBook.setbAuthor(readerBook1.getbAuthor());
                                readerBook.setbOwner(readerBook1.getbOwner());
                                queryBookList.add(readerBook);
                            }
                           adapter = new BmobQueryBookAdapter(BmobReaderQueryBook.this,
                                   R.layout.item_bmob_query_book_view,queryBookList);
                            queryBookListView.setAdapter(adapter);
                        }
                    }
                });

            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BmobReaderQueryBook.this,BmobReaderAddBook.class));
            }
        });
    }
}
