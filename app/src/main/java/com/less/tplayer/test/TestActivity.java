package com.less.tplayer.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.less.tplayer.R;
import com.less.uis.flowlayout.FlowLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 */
public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        FlowLayout flowLayout = (FlowLayout) findViewById(R.id.flowLayout);
        List<String> list = new ArrayList<>();
        list.add("DouYu");
        list.add("腾讯视频");
        list.add("阿里巴巴");
        list.add("支付宝");
        list.add("一行代码上你家");
        list.add("美女的诞生");
        list.add("生活大爆炸");
        list.add("天使的声音");
        list.add("爱美丽");
        list.add("哈哈");
        list.add("太好听");
        list.add("网易云音乐");

        // 使用排序令标签更整齐
        flowLayout.setFlowListener(list,true,new FlowLayout.OnFlowItemClickListener() {
            @Override
            public void onItemClick(String message) {
                Toast.makeText(TestActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void handle(View view) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
