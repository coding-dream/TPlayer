package com.less.tplayer.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by deeper on 2018/1/1.
 */

public class UrlFilters {

    private List<String> urls = new ArrayList<>();

    public static UrlFilters create() {
        return new UrlFilters();
    }
    public UrlFilters() {
        init();
    }
    private void init() {
        urls.add("lovelala.cn");
        urls.add("maopaotuan.com");
        urls.add("xkhejx.cn");
        urls.add("xkhejx.cn");
        urls.add("gszbba.cn");
        urls.add("symaa.cn");
        urls.add("cnzz.com");
        urls.add("kfkfl.cn");
        urls.add("ylbdtg.com");
        urls.add("jscsd.cn");
        urls.add("wgewj.cn");
        urls.add("maopaotuan.com");
        urls.add("ynjczy.net");
        urls.add("mmstat.com");
        urls.add("88gc.net");
        urls.add("j8ly.com");
        urls.add("mmstat.com");
        urls.add("88gc.net");
        urls.add("baidu.com");
        urls.add("ktzte.cn");
        urls.add("mmstat.com");
        urls.add("admin60.com");
    }

    public void addUrl(String url) {
        urls.add(url);
    }

    public boolean contains(String originUrl) {
        for (String url : urls) {
            if (originUrl.contains(url)) {
                return true;
            }
        }
        return false;
    }
}
