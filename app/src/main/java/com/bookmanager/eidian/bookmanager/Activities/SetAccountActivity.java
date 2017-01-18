package com.bookmanager.eidian.bookmanager.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bookmanager.eidian.bookmanager.R;

import at.markushi.ui.CircleButton;

public class SetAccountActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    EditText account, passwordLib, passwordJw, passwordPe;
    CircleButton save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_account);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        account = (EditText) findViewById(R.id.account);
        passwordLib = (EditText) findViewById(R.id.password_lib);
        passwordJw = (EditText) findViewById(R.id.password_jw);
        passwordPe = (EditText) findViewById(R.id.password_pe);
        save = (CircleButton) findViewById(R.id.save);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if (account.getText().toString().equals(""))
                    Toast.makeText(SetAccountActivity.this, "请输入学号", Toast.LENGTH_SHORT).show();
                else {
                    editor.putString("account", account.getText().toString());
                    editor.putString("password_jw", passwordJw.getText().toString());
                    editor.putString("password_lib", passwordLib.getText().toString());
                    editor.putString("password_pe", passwordPe.getText().toString());
                    editor.commit();
                    startActivity(new Intent(SetAccountActivity.this, MainActivity.class));
                    finish();
                }
            }
        });
        account.setText(sharedPreferences.getString("account", ""));
        passwordJw.setText(sharedPreferences.getString("password_jw", ""));
        passwordPe.setText(sharedPreferences.getString("password_pe", ""));
        passwordLib.setText(sharedPreferences.getString("password_lib", ""));
    }

}
