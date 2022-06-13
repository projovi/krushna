package com.krushna.veginew.activites;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.krushna.veginew.R;
import com.krushna.veginew.databinding.ActivityWebBinding;

public class WebActivity extends AppCompatActivity {
    ActivityWebBinding binding;

    public static void open(Context mainActivity, String title, String url) {
        mainActivity.startActivity(new Intent(mainActivity, WebActivity.class).putExtra("URL", url).putExtra("TITLE", title));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_web);


        Intent intent = getIntent();
        if (intent != null) {
            String url = intent.getStringExtra("URL");
            String title = intent.getStringExtra("TITLE");
            if (title != null) {
                binding.tvtitle.setText(title);
            }
            if (url != null) {
                binding.webView.loadUrl(url);
                binding.webView.setWebViewClient(new WebViewClient());

            }
        }
    }

    public void onClickBack(View view) {
        onBackPressed();
    }
}