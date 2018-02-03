package com.less.test;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
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

    private SimpleSpider simpleSpider;
    private ListView listview;
    private EditText et_input;
    private EditText et_start_url;
    private HtmlAdapter htmlAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spider);
        listview = (ListView) findViewById(R.id.listview);
        et_start_url = (EditText) findViewById(R.id.et_start_url);
        et_input = (EditText) findViewById(R.id.et_input);
        simpleSpider = new SimpleSpider(App.getContext());
        findViewById(R.id.btn_start).setOnClickListener(this);
        findViewById(R.id.btn_stop).setOnClickListener(this);
        findViewById(R.id.btn_count).setOnClickListener(this);
        findViewById(R.id.btn_list).setOnClickListener(this);
        findViewById(R.id.btn_add_urls).setOnClickListener(this);
        htmlAdapter = new HtmlAdapter(this);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Html html = (Html) htmlAdapter.getItem(position);
                Intent intent = new Intent();
                intent.setClass(SpiderActivity.this, HtmlActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("html", html);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Html html = (Html) htmlAdapter.getItem(position);
                ClipboardManager cbm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("myLabel", html.getUrl());
                cbm.setPrimaryClip(clipData);
                Toast.makeText(SpiderActivity.this, "复制成功", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        listview.setAdapter(htmlAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
                Toast.makeText(SpiderActivity.this, "start", Toast.LENGTH_SHORT).show();
                String startUrl = et_start_url.getText().toString();
                if (startUrl.trim().equals("")) {
                    Toast.makeText(this, "input can't null", Toast.LENGTH_SHORT).show();
                    return;
                }
                simpleSpider.start(startUrl);
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
            case R.id.btn_add_urls:
                String url = et_start_url.getText().toString();
                simpleSpider.urls(url);
                break;
            default:
                break;
        }
    }
}