package com.less.tplayer.ui;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.just.agentweb.AgentWeb;
import com.just.agentweb.ChromeClientCallbackManager;
import com.less.tplayer.R;
import com.less.tplayer.base.activity.BaseActivity;
import com.less.tplayer.interfaces.AndroidInterface;
import com.less.tplayer.mvp.movie.data.Movie;
import com.less.tplayer.util.HttpConnUtils;
import com.less.tplayer.util.LogUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 *
 * @author deeper
 * @date 2017/12/28
 */

public class DetailMovieActivity extends BaseActivity {
    private Movie movie;
    private LinearLayout mLinearLayout;
    private AgentWeb mAgentWeb;

    @Override
    protected void initToolBar() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_movie_detail;
    }

    @Override
    protected void initBundle(Bundle extras) {
        super.initBundle(extras);
        movie = (Movie) extras.getSerializable("movie");
    }

    @Override
    protected void initView() {
        super.initView();
        mLinearLayout = (LinearLayout) findViewById(R.id.ll_layout);
    }

    @Override
    protected void initData() {
        super.initData();
        WebViewClient webChromeClient = new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                String jsCode = "window.ainterface.callAndroid('hello java')";
                view.loadUrl("javascript:" + jsCode);
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                LogUtils.d("===========> intercept : " + url);
                return getResponse(url);
            }
        };

        mAgentWeb = AgentWeb.with(this)//传入Activity or Fragment
                .setAgentWebParent(mLinearLayout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
                .useDefaultIndicator()// 使用默认进度条
                .defaultProgressBarColor() // 使用默认进度条颜色
                .setReceivedTitleCallback(new ChromeClientCallbackManager.ReceivedTitleCallback() {
                    @Override
                    public void onReceivedTitle(WebView view, String title) {
                        LogUtils.d("title " + title);
                    }
                })
                .setWebViewClient(webChromeClient)
                .createAgentWeb()
                .ready()
                .go(movie.getDetailUrl());
        // mAgentWeb.getJsEntraceAccess().quickCallJs("callByAndroid");
        mAgentWeb.getJsInterfaceHolder().addJavaObject("ainterface",new AndroidInterface(this));
    }

    private WebResourceResponse getResponse(String url) {
        WebResourceResponse response = null;
        try {
            // url = "http://www.java1234.com";
            if (url.startsWith("http://v.361keji.com/play.php?play=")) {
                byte[] datas = HttpConnUtils.getDefault().sendRequest(url);
                String html = new String(datas);
                Document document = Jsoup.parse(html);
                document.select("header[class=header]").remove();
                document.select("div[class=asst asst-post_header]").remove();
                document.select("div[class=sidebar]").remove();
                document.select("div[class=article-actions clearfix]").remove();
                document.select("div[class=widget widget-textasst]").remove();
                document.select("footer[class=footer]").remove();

                InputStream in = new ByteArrayInputStream(document.outerHtml().getBytes());
                response = new WebResourceResponse("text/html","utf-8",in);
            } else if (url.startsWith("http://xxad.js")) {
                String data = new String("hello");
                InputStream inputStream = new ByteArrayInputStream(data.getBytes());
                response = new WebResourceResponse("text/html", "utf-8", inputStream);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!mAgentWeb.back()) {
                finish();
            }
            return true;
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAgentWeb.getWebLifeCycle().onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAgentWeb.getWebLifeCycle().onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAgentWeb.getWebLifeCycle().onDestroy();
    }
}