package com.example.myapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;

public class VirtualRobot extends AppCompatActivity {

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_virtual_robot);

        WebView webView = findViewById(R.id.webView);
        webView.getSettings().setMediaPlaybackRequiresUserGesture(true);
        webView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);



        if (webView == null) {
            Log.e("WebView", "WebView 找不到！");
            return;
        }

        // 先確認 WebView 是否可用
        try {
            // 測試基本的 WebView 設定，避免直接崩潰
            WebSettings webSettings = webView.getSettings();

            // **逐步啟用 WebView 設定，避免某個選項崩潰**
            webSettings.setJavaScriptEnabled(true); // 啟用 JavaScript
            webSettings.setDomStorageEnabled(true); // 允許 localStorage / IndexedDB
            webSettings.setAllowFileAccess(true); // 允許存取本地檔案
            webSettings.setAllowContentAccess(true); // 允許存取內容
            webView.getSettings().setDomStorageEnabled(true);
            webView.getSettings().setJavaScriptEnabled(true);


            try {
                webSettings.setUseWideViewPort(true); // 讓網頁自適應螢幕
                webSettings.setLoadWithOverviewMode(true); // 預設縮放模式
                webSettings.setJavaScriptCanOpenWindowsAutomatically(true); // 允許開啟新視窗
                webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW); // 允許 HTTP/HTTPS 混合內容
            } catch (Exception e) {
                Log.e("WebView", "某些 WebSettings 設定失敗：" + e.getMessage());
            }

            // 設置 WebViewClient 來防止開啟外部瀏覽器
            webView.setWebViewClient(new WebViewClient());

            // 設置 WebChromeClient 來支援 alert、confirm 等
            webView.setWebChromeClient(new WebChromeClient());

            // 載入網址
            webView.loadUrl("http://18.181.195.109/");
            Log.d("WebView", "正在載入：http://18.181.195.109/");

        } catch (Exception e) {
            Log.e("WebView", "WebView 初始化錯誤：" + e.getMessage());
        }
    }
}
