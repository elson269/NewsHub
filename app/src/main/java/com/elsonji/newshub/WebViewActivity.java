package com.elsonji.newshub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import static com.elsonji.newshub.ListRemoteViewFactory.EXTRA_NEWS_URL;
import static com.elsonji.newshub.PageFragment.NEWS_URL;

public class WebViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        Intent intentFromPageFrag = getIntent();
        String newsUrl = intentFromPageFrag.getStringExtra(NEWS_URL);

        WebView webView = findViewById(R.id.web_view);

        Intent fillInIntent = getIntent();
        Bundle extras = fillInIntent.getExtras();
        if (extras != null) {
            webView.loadUrl(extras.getString(EXTRA_NEWS_URL));
        }
        webView.loadUrl(newsUrl);
    }
}
