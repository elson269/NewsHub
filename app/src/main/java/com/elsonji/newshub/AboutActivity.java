package com.elsonji.newshub;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        TextView aboutTextView = findViewById(R.id.about_text_view);
        aboutTextView.setText(getString(R.string.about_content));

        TextView poweredByTextView = findViewById(R.id.powered_by);
        poweredByTextView.setText(getString(R.string.powered_by));

    }
}
