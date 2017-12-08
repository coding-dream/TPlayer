package com.less.tvplayer;

/**
 * Created by deeper on 2017/12/6.
 */

public class LiveInfo {
    private String room_id;
    private String room_name;
    private int rateSwitch;
    private String hls_url;
    private String live_url;

    public String getRoom_id() {
        return room_id;
    }

    public void setRoom_id(String room_id) {
        this.room_id = room_id;
    }

    @Override
    public String toString() {
        return "LiveInfo{" +
                "room_id='" + room_id + '\'' +
                ", room_name='" + room_name + '\'' +
                ", rateSwitch=" + rateSwitch +
                ", hls_url='" + hls_url + '\'' +
                ", live_url='" + live_url + '\'' +
                '}';
    }

    public String getRoom_name() {
        return room_name;
    }

    public void setRoom_name(String room_name) {
        this.room_name = room_name;
    }

    public int getRateSwitch() {
        return rateSwitch;
    }

    public void setRateSwitch(int rateSwitch) {
        this.rateSwitch = rateSwitch;
    }

    public String getHls_url() {
        return hls_url;
    }

    public void setHls_url(String hls_url) {
        this.hls_url = hls_url;
    }

    public String getLive_url() {
        return live_url;
    }

    public void setLive_url(String live_url) {
        this.live_url = live_url;
    }
}
