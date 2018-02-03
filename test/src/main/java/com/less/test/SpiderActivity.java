package com.less.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import static android.R.id.list;

public class SpiderActivity extends AppCompatActivity implements View.OnClickListener {

    private String startUrl = "http://www.java1234.com";
    private SimpleSpider simpleSpider;
    private ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spider);
        listview = (ListView) findViewById(R.id.listview);
        simpleSpider = new SimpleSpider(App.getContext(), startUrl);

        findViewById(R.id.btn_start).setOnClickListener(this);
        findViewById(R.id.btn_stop).setOnClickListener(this);
        findViewById(R.id.btn_count).setOnClickListener(this);
        findViewById(R.id.btn_list).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
                Toast.makeText(SpiderActivity.this, "start", Toast.LENGTH_SHORT).show();
                simpleSpider.start();
                break;
            case R.id.btn_stop:
                Toast.makeText(SpiderActivity.this, "stop", Toast.LENGTH_SHORT).show();
                simpleSpider.stop();
                break;
            case R.id.btn_count:
                int count = simpleSpider.count();
                Toast.makeText(SpiderActivity.this, "count: " + count, Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_list:
                simpleSpider.list();
                break;
            default:
                break;
        }
    }
}