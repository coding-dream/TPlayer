package com.less.tplayer.bean;

import android.os.Bundle;

/**
 * Created by deeper on 2017/11/23.
 */

public class PagerInfo {
    private String title;
    private Class<?> clx;
    private Bundle args;

    public PagerInfo(String title, Class<?> clx, Bundle args) {
        this.title = title;
        this.clx = clx;
        this.args = args;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Class<?> getClx() {
        return clx;
    }

    public void setClx(Class<?> clx) {
        this.clx = clx;
    }

    public Bundle getArgs() {
        return args;
    }

    public void setArgs(Bundle args) {
        this.args = args;
    }
}
