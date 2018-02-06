package com.less.test;

import android.content.Context;
import android.util.Log;

import com.less.aspider.ASpider;
import com.less.aspider.bean.Page;
import com.less.aspider.pipeline.Pipeline;
import com.less.aspider.processor.PageProcessor;
import com.less.test.bean.Html;
import com.less.test.db.HtmlDao;
import com.less.test.db.HtmlDaoImpl;
import com.less.test.util.HTMLFilter;
import com.less.test.util.HtmlChooser;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimpleSpider {
    public static String TAG = SimpleSpider.class.getSimpleName();
    public static final String REGEX_URL = "(http|https)+://[^\\s|\\?|&|'|\"]+(com|cn|org|net)+?";
    public static Pattern pattern = Pattern.compile(REGEX_URL);
    private ASpider aSpider;
    private HtmlChooser htmlChooser = new HtmlChooser();
    private HtmlDao htmlDao;

    public SimpleSpider(Context context) {
        htmlDao = new HtmlDaoImpl(context);

        aSpider = ASpider.create()
                .pageProcessor(new PageProcessor() {
                    @Override
                    public void process(Page page) {
                        Log.e(TAG, "page: " + page.getRawText());
                        boolean flag = htmlChooser.contains(page.getRawText());
                        if (flag) {
                            Html html = new Html();
                            html.setUrl(page.getUrl());
                            String filterText = HTMLFilter.filter(page.getRawText());
                            html.setHtml(filterText);
                            page.putField("html", html);

                            Matcher matcher = pattern.matcher(page.getRawText());
                            while (matcher.find()) {
                                String result = matcher.group();
                                page.addTargetRequestsNoReferer(result);
                            }
                        }
                    }
                })
                .thread(50)
                .addPipeline(new Pipeline() {
                    @Override
                    public void process(Map<String, Object> fields) {
                        for (Map.Entry<String, Object> entry : fields.entrySet()) {
                            Html html = (Html) entry.getValue();
                            Log.d(TAG, html.getHtml());
                            htmlDao.save(html);
                        }
                    }
                })
                .sleepTime(0)
                .retrySleepTime(0);
    }

    public void start(String url) {
        aSpider.urls(url);
        aSpider.runAsync();
    }

    public void stop() {
       aSpider.stop();
    }

    public int count(){
        return htmlDao.count();
    }

    public List<Html> search(String text) {
        List<Html> list =  htmlDao.search(text);
        return list;
    }

    public void urls(String...urls){
        aSpider.urls(urls);
    }
}