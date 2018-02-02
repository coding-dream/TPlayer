package com.less.test;

import android.util.Log;

import com.less.aspider.ASpider;
import com.less.aspider.bean.Page;
import com.less.aspider.processor.PageProcessor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimpleSpider {
    public static String TAG = SimpleSpider.class.getSimpleName();
    public static final String REGEX_URL = "(http|https)+://[^\\s|\\?|&|'|\"]+(com|cn|org|net)+?";
    public static final Pattern pattern = Pattern.compile(REGEX_URL);

    public static void start(String startUrl) {
        ASpider.create()
                .pageProcessor(new PageProcessor() {
                    @Override
                    public void process(Page page) {
                        String url = page.getUrl();
                        Log.e(TAG, "page: " + page.getRawText());
                        Matcher matcher = pattern.matcher(page.getRawText());
                        while(matcher.find()){
                            String result = matcher.group();
                            page.addTargetRequestsNoReferer(result);
                        }
                    }
                })
                .thread(3)
                .sleepTime(0)
                .retrySleepTime(0)
                .urls(startUrl)
                .runAsync();
    }
}
