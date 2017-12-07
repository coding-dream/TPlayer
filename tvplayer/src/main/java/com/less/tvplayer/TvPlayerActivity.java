package com.less.tvplayer;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.util.Pair;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.utils.ScreenResolution;
import io.vov.vitamio.widget.VideoView;

/**
 *
 * @author deeper
 * @date 2017/12/6
 */

public class TvPlayerActivity extends Activity {
    private static final String TAG = TvPlayerActivity.class.getSimpleName();
    private VideoView mVideoView;
    private int mScreenWidth = 0;
    private int mScreenHeight = 0;

    /** 左右滑动敏感度 */
    private int sensitiveOffset = 2;
    /** 当前显示的声音 */
    private int mShowVolume;
    /** 当前显示的亮度 */
    private int mShowLightness;

    private static final int MAX_SHOW_VOLUME = 100;

    private static final int MAX_SHOW_LIGHTNESS = 255;

    /**
     * mMaxVolume 最大声音(音量是按个数计算的)
     * 如: 获取系统最大音量 mMaxVolume = 6,设置具体音量也是1 ~ 6
     *
     * 注意: 亮度和声音均可以有两种设置增加的方式
     * 1. 根据滑动的 距离/屏幕高度 比例设置增长值
     * 2. 根据手势onScroll触发次数(其实每次触发都大概是1px),每次递增或递减 设置增长值,但需要设置某个增长的最大值(不固定)
     * (这里设置音量增长的最大值 MAX_SHOW_VOLUME = 100)
     */
    private int mMaxVolume;
    /**
     * 有两种亮度 1. Window的亮度(0.00 ~ 1.00) 2. 系统屏幕的亮度(0 ~ 255)
     *
     * 注意: 同上
     * (这里设置亮度增长的最大值 MAX_SHOW_LIGHTNESS = 255)
     */

    private AudioManager mAudioManager;

    private GestureDetector mGestureDetector;
    private GestureDetector.SimpleOnGestureListener mSimpleOnGestureListener;

    public static final int HIDE_TOP_BOTTOM_BAR = 0x02;// 隐藏Top和Bottom控制条
    public static final int HIDE_TOP_BOTTOM_TIME = 5000;// 隐藏控制条时间

    public static final int HIDE_CENTER_BAR = 0x03;// 隐藏声音和亮度控制条
    public static final int HIDE_CENTER_TIME = 1000;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                /**
                 *  隐藏top ,bottom
                 */
                case HIDE_TOP_BOTTOM_BAR:
//                    hideControlBar();
                    break;
                /**
                 *  隐藏center控件
                 */
                case HIDE_CENTER_BAR:
//                    if (controlCenter != null) {
//                        controlCenter.setVisibility(View.GONE);
//                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVitamio();
        setContentView(R.layout.activity_tvplayer);
        initView();
    }

    private void initVitamio() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Vitamio.isInitialized(this);
    }

    private void initView() {
        mVideoView = (VideoView) findViewById(R.id.videoView);
        mVideoView.setKeepScreenOn(true);
        // String id = getIntent().getExtras().getString("Room_id");
        String id = "1";
        // okhttp get -> id { callback TvBean } 此处和下面的代码是异步执行的.
        API.getDataById(id, new API.LoadCallback() {
            @Override
            public void onDataLoaded(LiveInfo liveInfo) {
                String url = liveInfo.getLive_url();
                Uri uri = Uri.parse(url);
//                tvLiveNickname.setText(liveInfo.getRoom_name());
                mVideoView.setVideoURI(uri);
                mVideoView.setBufferSize(1024 * 1024 * 2);
                /*
                 * 设置视频质量。参数quality参见MediaPlayer的常量：
                 * VIDEOQUALITY_LOW（流畅）、VIDEOQUALITY_MEDIUM（普通）、VIDEOQUALITY_HIGH（高质）。
                 */
                mVideoView.setVideoQuality(MediaPlayer.VIDEOQUALITY_HIGH);
                mVideoView.requestFocus();
                mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        // optional need Vitamio 4.0
//                        mediaPlayer.setPlaybackSpeed(1.0f);
//                        flLoading.setVisibility(View.GONE);
//                        ivLivePlay.setImageResource(R.drawable.img_live_videopause);
//                        mHandler.sendEmptyMessageDelayed(HIDE_CONTROL_BAR, HIDE_TIME);
                    }
                });

            }

            @Override
            public void onDataNotAvailable() {
                // svProgressHUD.showErrorWithStatus("主播还在赶来的路上~~");
            }
        });

        // 获取屏幕宽度
        Pair<Integer, Integer> screenPair = ScreenResolution.getResolution(this);
        mScreenWidth = screenPair.first;
        mScreenHeight = screenPair.second;

        initVolumeWithLight();
        // initDanMu(Room_id);
        mVideoView.setVideoLayout(VideoView.VIDEO_LAYOUT_STRETCH, 0);
        initTouchListener();
    }

    private void initTouchListener() {
        mSimpleOnGestureListener = new GestureDetector.SimpleOnGestureListener() {
            /**
             * onScroll 由1个MotionEvent ACTION_DOWN, 多个MotionEvent ACTION_MOVE 触发
             */
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2,float distanceX, float distanceY) {
                // e1为第一次按下的事件，和onDown事件里面的一样，e2为当前的事件，distanceX为本次onScroll移动的X轴距离，distanceY为移动的Y轴距离，移动的距离是相对于上一次onScroll事件的移动距离
                float mOldX = e1.getX();

                float absDistanceX = Math.abs(distanceX);// distanceX < 0 从左到右
                float absDistanceY = Math.abs(distanceY);// distanceY < 0 从上到下

                // Y方向的距离比X方向的大，即 上下 滑动
                if (absDistanceX < absDistanceY) {
                    // 向上滑动
                    if (distanceY > 0) {
                        if (mOldX > mScreenWidth * 0.8) {
                            // 右边调节声音(大)
                            changeVolume(1);
                        } else if(mOldX < mScreenWidth * 0.2){
                            // 左边调节亮度(大)
                            changeLightness(1);
                        }
                    } else { // 向下滑动
                        if (mOldX > mScreenWidth * 0.8) {
                            // 右边调节声音(小)
                            changeVolume(-1);
                        } else if(mOldX < mScreenWidth * 0.2){
                            // 左边调节亮度(小)
                            changeLightness(-1);
                        }
                    }
                } else if(absDistanceX - absDistanceY > sensitiveOffset){
                    // X方向的距离比Y方向的大，即 左右 滑动

                }
                return false;
            }

            // 双击事件，有的视频播放器支持双击播放暂停，可从这实现
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                return super.onDoubleTap(e);
            }

            // 单击事件
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
//                if (controlBottom.getVisibility() == View.VISIBLE) {
//                    mHandler.removeMessages(HIDE_CONTROL_BAR);
//                    hideControlBar();
//                } else {
//                    showControlBar();
//                    mHandler.sendEmptyMessageDelayed(HIDE_CONTROL_BAR, HIDE_TIME);
//                }
                return true;
            }
        };
        mGestureDetector = new GestureDetector(this, mSimpleOnGestureListener);
    }

    private void changeVolume(int value) {
        mShowVolume += value;
        if (mShowVolume > 100) {
            mShowVolume = 100;
        } else if (mShowVolume < 0) {
            mShowVolume = 0;
        }
//        tvControlName.setText("音量");
//        ivControlImg.setImageResource(R.drawable.img_volume);
//        tvControl.setText(mShowVolume + "%");
//        int tagVolume = mShowVolume * mMaxVolume / 100;
//        //tagVolume:音量绝对值
//        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, tagVolume, 0);
//        mHandler.removeMessages(HIDE_CENTER_CONTROL);
//        controlCenter.setVisibility(View.VISIBLE);
//        mHandler.sendEmptyMessageDelayed(HIDE_CENTER_CONTROL, HIDE_CONTROL_TIME);
    }

    private void changeLightness(int value) {
        mShowLightness += value;
        if (mShowLightness > 255) {
            mShowLightness = 255;
        } else if (mShowLightness <= 0) {
            mShowLightness = 0;
        }
//        tvControlName.setText("亮度");
//        ivControlImg.setImageResource(R.drawable.img_light);
//        tvControl.setText(mShowLightness * 100 / 255 + "%");
//        WindowManager.LayoutParams lp = getWindow().getAttributes();
//        lp.screenBrightness = mShowLightness / 255f;
//        getWindow().setAttributes(lp);
//
//        mHandler.removeMessages(HIDE_CENTER_CONTROL);
//        controlCenter.setVisibility(View.VISIBLE);
//        mHandler.sendEmptyMessageDelayed(HIDE_CENTER_CONTROL, HIDE_CONTROL_TIME);
    }

    private void initVolumeWithLight() {
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        mShowVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC) * 100 / mMaxVolume;
        mShowLightness = getScreenBrightness();
    }

    /**
     * 获得当前屏幕亮度值 0--255
     */
    private int getScreenBrightness() {
        int screenBrightness = 255;
        try {
            screenBrightness = Settings.System.getInt(getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return screenBrightness;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mGestureDetector != null) {
            mGestureDetector.onTouchEvent(event);
        }
        return super.onTouchEvent(event);
    }

    /**
     * 该方法只有在Manifest设置后才会被系统回调,且Manifest没有设置情况下Activity会自动销毁.
     * 当然也可以手动调用:
     *      (1) setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
     *      (2) setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
     * @param newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.d(TAG, "横屏旋转");
        }
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Log.d(TAG, "竖屏旋转");
        }
    }
}
