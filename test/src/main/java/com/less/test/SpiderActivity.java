package com.less.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class SpiderActivity extends AppCompatActivity {

    private String startUrl = "http://www.java1234.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spider);
        findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SpiderActivity.this, "start", Toast.LENGTH_SHORT).show();
                SimpleSpider.start(startUrl);
            }
        });
    }
}
