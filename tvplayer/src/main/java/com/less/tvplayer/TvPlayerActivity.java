package com.less.tvplayer;

import android.app.Activity;
import android.os.Bundle;

import io.vov.vitamio.widget.VideoView;

/**
 *
 * @author deeper
 * @date 2017/12/6
 */

public class TvPlayerActivity extends Activity {
    private VideoView mVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tvplayer);
        mVideoView = findViewById(R.id.videoView);
        
    }
}
