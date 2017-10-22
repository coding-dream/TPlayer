package com.less.tplayer.util;

public class ReadState {
    private ReadStateHelper helper;

    public ReadState(ReadStateHelper helper) {
        this.helper = helper;
    }

    /**
     * 添加已读状态
     *
     * @param key 一般为资讯等Id
     */
    public void put(String key) {
        helper.put(key);
    }

    /**
     * 获取是否为已读
     *
     * @param key 一般为咨询等Id
     * @return true 已读
     */
    public boolean already(String key) {
        return helper.already(key);
    }
}
