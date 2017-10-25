package com.less.tplayer.ui;

import android.os.Handler;
import android.os.Message;

import com.less.smart_progressbar.SmartProgressbar;
import com.less.tplayer.R;
import com.less.tplayer.base.BaseActivity;

/**
 * @author Administrator
 */
public class MainActivity extends BaseActivity {
    private SmartProgressbar smartProgressbar;
    private int SEND_LOADING  = 0x123;
    private int progress = 0;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            progress++;
            smartProgressbar.setProgress(progress);
            if (progress < 100) {
                handler.sendEmptyMessageDelayed(SEND_LOADING, 100);
            } else {
                handler.removeMessages(SEND_LOADING);
            }
        }
    };

    @Override
    public int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        super.initData();
        smartProgressbar = (SmartProgressbar) findViewById(R.id.sp_progressbar);
        handler.sendEmptyMessageDelayed(SEND_LOADING, 3000);
    }
}
