package com.less.ffmpeg;

/**
 * Created by deeper on 2017/10/28.
 */

public class FFmpegUtil {

    private static FFmpegUtil instance = null;

    public static FFmpegUtil getInstance(){
        if(instance == null){
            instance = new FFmpegUtil();
        }
        return instance;
    }
    static{
        System.loadLibrary("less");
    }

    public native String stringFromJNI();
    public native String urlProtocolInfo();
    public native String avFormatInfo();
    public native String avCodecInfo();
    public native String avFilterInfo();
}
