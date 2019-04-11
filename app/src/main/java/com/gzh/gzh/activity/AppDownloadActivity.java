package com.gzh.gzh.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Message;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.gzh.gzh.R;
import com.gzh.library.util.Util;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by MaoLJ on 2018/9/7.
 * APP下载页面
 */

public class AppDownloadActivity extends Base1Activity {

    private static final String TAG = "AppDownloadActivity";
    public static String URL = "url";
    @Bind(R.id.web)
    WebView mWebView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_app_download;
    }

    @Override
    public void initView() {
        Util.immersiveStatus(this, true);
        String url = getIntent().getStringExtra(URL);
        mWebView.clearCache(true);
        mWebView.loadUrl(url);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        mWebView.getSettings().setDomStorageEnabled(false);
        mWebView.getSettings().setDatabaseEnabled(false);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setAppCacheEnabled(false);
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
                WebView newWebView = new WebView(view.getContext());
                newWebView.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        mWebView.clearCache(true);
                        mWebView.loadUrl(url);
                        mWebView.getSettings().setJavaScriptEnabled(true);
                        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
                        mWebView.getSettings().setAllowFileAccess(true);
                        mWebView.getSettings().setSupportZoom(true);
                        mWebView.getSettings().setBuiltInZoomControls(true);
                        mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
                        mWebView.getSettings().setDomStorageEnabled(false);
                        mWebView.getSettings().setDatabaseEnabled(false);
                        mWebView.getSettings().setLoadWithOverviewMode(true);
                        mWebView.getSettings().setUseWideViewPort(true);
                        mWebView.getSettings().setAppCacheEnabled(false);
                        return true;
                    }
                });
                WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
                transport.setWebView(newWebView);
                resultMsg.sendToTarget();
                return true;
            }
        });
        mWebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String s, String s1, String s2, String s3, long l) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(url));
                startActivity(intent);
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
