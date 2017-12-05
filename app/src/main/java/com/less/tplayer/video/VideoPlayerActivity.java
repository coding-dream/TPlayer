package com.less.tplayer.video;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.less.tplayer.R;

import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.VideoView;


/**
 * Created by deeper on 2017/12/5.
 */

public class VideoPlayerActivity extends AppCompatActivity {
    private VideoView mVideoView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Vitamio.isInitialized(this);
        setContentView(R.layout.activity_video);

        String url = "http://yf.m.l.cztv.com/channels/lantian/channel01/360p.m3u8?k=d713e997b5ac1b24cf3714e8966a5553&t=1485264314";
        mVideoView = (VideoView) findViewById(R.id.videoView);
        mVideoView.setVideoURI(Uri.parse(url));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        //屏幕切换时，设置全屏
        if (mVideoView != null){
            mVideoView.setVideoLayout(VideoView.VIDEO_LAYOUT_SCALE, 0);
        }
        super.onConfigurationChanged(newConfig);
    }
}
