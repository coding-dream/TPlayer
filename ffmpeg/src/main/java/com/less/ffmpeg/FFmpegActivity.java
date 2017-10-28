package com.less.ffmpeg;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class FFmpegActivity extends Activity implements View.OnClickListener {
    Button btn_protocol;
    Button btn_format;
    Button btn_codec;
    Button btn_filter;

    TextView tv_info;
    FFmpegUtil fFmpegUtil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ffmpeg);
        fFmpegUtil = new FFmpegUtil();

        TextView tv = (TextView) findViewById(R.id.sample_text);
        tv_info = (TextView) findViewById(R.id.tv_info);
        tv.setText(fFmpegUtil.stringFromJNI());

        btn_protocol= (Button) findViewById(R.id.btn_protocol);
        btn_format= (Button) findViewById(R.id.btn_format);
        btn_codec= (Button) findViewById(R.id.btn_codec);
        btn_filter= (Button) findViewById(R.id.btn_filter);

        btn_protocol.setOnClickListener(this);
        btn_format.setOnClickListener(this);
        btn_codec.setOnClickListener(this);
        btn_filter.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_protocol) {
            tv_info.setText(fFmpegUtil.urlProtocolInfo());

        } else if (i == R.id.btn_format) {
            tv_info.setText(fFmpegUtil.avFormatInfo());

        } else if (i == R.id.btn_codec) {
            tv_info.setText(fFmpegUtil.avCodecInfo());

        } else if (i == R.id.btn_filter) {
            tv_info.setText(fFmpegUtil.avFilterInfo());
        }
    }
}
