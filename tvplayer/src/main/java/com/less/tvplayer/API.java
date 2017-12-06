package com.less.tvplayer;

/**
 * Created by deeper on 2017/12/6.
 */

public class API {

    public interface LoadCallback {
        void onDataLoaded(LiveInfo liveInfo);
        void onDataNotAvailable();
    }

    public static void getDataById(String id,LoadCallback loadCallback){
        // okhttp -> get id
        try {
            // 模拟数据
            LiveInfo liveInfo = new LiveInfo();
            liveInfo.setLive_url("http://yf.m.l.cztv.com/channels/lantian/channel01/360p.m3u8?k=d713e997b5ac1b24cf3714e8966a5553&t=1485264314");
            liveInfo.setRoom_id(id);
            liveInfo.setRoom_name("呆萌的女孩");
            loadCallback.onDataLoaded(liveInfo);
        } catch (Exception e) {
            loadCallback.onDataNotAvailable();
            e.printStackTrace();
        }
    }
}
