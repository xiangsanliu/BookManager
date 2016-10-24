package com.bookmanager.eidian.bookmanager.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bookmanager.eidian.bookmanager.Entities.ReaderBook;
import com.bookmanager.eidian.bookmanager.R;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;


public class BmobReaderAddBook extends AppCompatActivity {

    private EditText bName;

    private EditText bAuthor;

    private EditText bOwner;

    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmob_reader_add_book);
        Bmob.initialize(this,"d882083bc95b77c5c072cbcc4264078d");
        submit = (Button) findViewById(R.id.submit_book_each);
        bName = (EditText) findViewById(R.id.input_book_name_each);
        bAuthor = (EditText) findViewById(R.id.input_author_name_each);
        bOwner = (EditText) findViewById(R.id.book_owner_each);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bookName = bName.getText().toString();
                String bookAuthor = bAuthor.getText().toString();
                String bookOwner = bOwner.getText().toString();

                if (bookName.equals("") || bookAuthor.equals("") || bookOwner.equals("")) {
                    return;
                }
                ReaderBook readerBook = new ReaderBook();
                readerBook.setbName(bookName);
                readerBook.setbAuthor(bookAuthor);
                readerBook.setbOwner(bookOwner);
                readerBook.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null){
                            Log.d("Borrow","111111111");
                            AlertDialog.Builder builder = new AlertDialog.Builder(BmobReaderAddBook.this);
                            builder.setTitle("图书互借");
                            builder.setMessage("提交成功");
                            builder.setCancelable(false);
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            builder.show();
//                            Toast.makeText(getActivity(),"添加成功",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(BmobReaderAddBook.this,"添加失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }
}
