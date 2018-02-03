package com.less.test.bean;

import java.io.Serializable;

/**
 * Created by deeper on 2018/2/3.
 */

public class Html implements Serializable {

    private String url;
    private String html;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }
}
