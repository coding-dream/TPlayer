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
import com.less.tplayer.util.UrlFilters;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * @author deeper
 * @date 2017/12/28
 */

public class DetailMovieActivity extends BaseActivity {
    private Movie movie;
    private LinearLayout mLinearLayout;
    private AgentWeb mAgentWeb;
    private UrlFilters urlFilters = UrlFilters.create();

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
        WebViewClient webViewClient = new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // String jsCode = "window.ainterface.callAndroid('hello java')";
                // String adCode = "function hideDiv(){document.querySelector('div[div7926=div625]').style.display='none';} hideDiv();";
                // view.loadUrl("javascript:" + adCode);
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
                .setWebViewClient(webViewClient)
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
                document.prepend("<p style='margin:16px;'>温馨提示: 请手动选择播放列表！</p>");
                document.select("div[class=asst asst-post_header]").remove();
                document.select("div[class=sidebar]").remove();
                document.select("div[class=article-actions clearfix]").remove();
                document.select("div[class=widget widget-textasst]").remove();
                document.select("h3[class=single-strong]").remove();
                document.select("footer[class=footer]").remove();

                InputStream in = new ByteArrayInputStream(document.outerHtml().getBytes());
                response = new WebResourceResponse("text/html","utf-8",in);
            }else if(url.equals("http://v.361keji.com/images/jiazai.png") || url.equals("http://v.361keji.com/images/1280jiazai.png")){
                InputStream inputStream = getAssets().open("loading.gif");
                response = new WebResourceResponse("text/html", "utf-8", inputStream);
            } else if(urlFilters.contains(url)){
                String data = new String("");
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