package com.less.test;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.less.test.util.ThreadUtil;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class SpiderSettingActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvMessage;
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spider_setting);

        findViewById(R.id.btn_add).setOnClickListener(this);
        findViewById(R.id.btn_clear).setOnClickListener(this);
        findViewById(R.id.btn_show).setOnClickListener(this);
        tvMessage = (TextView) findViewById(R.id.tv_message);
        path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "choose.txt";
    }

    private EditText getEtInput(){
        return (EditText) findViewById(R.id.et_input);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add:
                final String input = getEtInput().getText().toString();
                if (input.trim().equals("")) {
                    Toast.makeText(this, "input can't empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                ThreadUtil.newThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            FileUtils.writeStringToFile(new File(path), input + "\r\n", "utf-8", true);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                break;
            case R.id.btn_clear:
                File file = new File(path);
                if (file.exists()) {
                    file.delete();
                }
                Toast.makeText(this, "delete success", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_show:
                try {
                    String content = FileUtils.readFileToString(new File(path), "utf-8");
                    tvMessage.setText(content);
                } catch (IOException e) {
                    tvMessage.setText("Error: " + e.getMessage());
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }
}
