package com.elsonji.newshub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import static com.elsonji.newshub.PageFragment.NEWS_URL;

public class SavedNewsWebViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        Intent intentFromPageFrag = getIntent();
        String newsUrl = intentFromPageFrag.getStringExtra(NEWS_URL);

        WebView webView = findViewById(R.id.web_view);
        webView.loadUrl(newsUrl);
    }
}
