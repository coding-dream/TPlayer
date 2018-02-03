package com.less.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.less.test.bean.Html;

import java.util.List;

public class SpiderActivity extends AppCompatActivity implements View.OnClickListener {

    private String startUrl = "http://www.java1234.com";
    private SimpleSpider simpleSpider;
    private ListView listview;
    private EditText et_input;
    private HtmlAdapter htmlAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spider);
        listview = (ListView) findViewById(R.id.listview);
        et_input = (EditText) findViewById(R.id.et_input);
        simpleSpider = new SimpleSpider(App.getContext(), startUrl);
        findViewById(R.id.btn_start).setOnClickListener(this);
        findViewById(R.id.btn_stop).setOnClickListener(this);
        findViewById(R.id.btn_count).setOnClickListener(this);
        findViewById(R.id.btn_list).setOnClickListener(this);
        htmlAdapter = new HtmlAdapter(this);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Html html = (Html) htmlAdapter.getItem(position);
                Toast.makeText(SpiderActivity.this, html.getUrl(), Toast.LENGTH_SHORT).show();
            }
        });
        listview.setAdapter(htmlAdapter);
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
                String input = et_input.getText().toString();
                List<Html> list = simpleSpider.search(input);
                htmlAdapter.clear();
                htmlAdapter.addAll(list);
                break;
            default:
                break;
        }
    }
}