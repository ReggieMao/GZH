package com.gzh.gzh.activity;

import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import butterknife.Bind;
import butterknife.OnClick;
import com.gzh.gzh.R;
import com.gzh.library.util.Util;

/**
 * Created by MaoLJ on 2018/8/6.
 * 新闻页面
 */

public class NewsActivity extends Base1Activity {

    private static final String TAG = "NewsActivity";
    public static String URL = "url";
    @Bind(R.id.web)
    WebView mWebView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_news;
    }

    @Override
    public void initView() {
        Util.immersiveStatus(this, true);
        String url = getIntent().getStringExtra(URL);
        mWebView.loadUrl(url);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }

    @OnClick({R.id.img_back})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
        }
    }

}
