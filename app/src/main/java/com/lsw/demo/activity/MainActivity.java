package com.lsw.demo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.lsw.demo.R;

public class MainActivity extends AppCompatActivity {


    private Button toDownload;
    private Button toUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        toDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, DownloadActivity.class);
                startActivity(intent);
            }
        });

        toUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, WeatherActivity.class);
                startActivity(intent);
            }
        });
    }


    private void initView() {
        toDownload = (Button) findViewById(R.id.to_download);
        toUser = (Button) findViewById(R.id.to_user);
    }
}
