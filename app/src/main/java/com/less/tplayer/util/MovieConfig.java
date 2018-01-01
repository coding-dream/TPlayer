package com.less.tplayer.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by deeper on 2018/1/1.
 */

public class MovieConfig {

    private Map<String,String> catMap = new HashMap<>();

    public static MovieConfig create() {
        return new MovieConfig();
    }

    public MovieConfig() {
        init();
    }

    private void init() {
        catMap.put("all", "all");
        catMap.put("103", "喜剧");
        catMap.put("100", "爱情");
        catMap.put("106", "动作");
        catMap.put("102", "恐怖");
        catMap.put("104", "科幻");
        catMap.put("112", "剧情");
        catMap.put("105", "犯罪");
        catMap.put("113", "奇幻");
        catMap.put("108", "战争");
        catMap.put("115", "悬疑");
        catMap.put("107", "动画");
        catMap.put("117", "文艺");
        catMap.put("101", "伦理");
        catMap.put("118", "记录");
        catMap.put("119", "传记");
        catMap.put("120", "歌舞");
        catMap.put("121", "古装");
        catMap.put("122", "历史");
        catMap.put("123", "惊悚");
        catMap.put("123", "惊悚");
        catMap.put("other", "other");
    }

    public String get(String key) {
        return catMap.get(key);
    }

    public Map getCatMap() {
        return catMap;
    }
}
