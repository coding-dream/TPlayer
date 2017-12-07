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
            liveInfo.setLive_url("http://220.194.236.202/12/h/p/j/p/hpjpepdtiwxnrgfqmdhlfmdkycgbnd/hc.yinyuetai.com/DF9E01601C061B7C9B0669F3CBE27426.mp4?sc=34c122c3de10498b&br=783&vid=3105087&aid=43565&area=US&vst=0");
            liveInfo.setRoom_id(id);
            liveInfo.setRoom_name("呆萌的女孩");
            loadCallback.onDataLoaded(liveInfo);
        } catch (Exception e) {
            loadCallback.onDataNotAvailable();
            e.printStackTrace();
        }
    }
}
