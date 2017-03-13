package com.jktaihe.browser;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static android.widget.LinearLayout.HORIZONTAL;
import static android.widget.LinearLayout.VERTICAL;

/**
 * Created by jktaihe on 12/3/17.
 * blag: blag.jktaihe.com
 */

public class MainActivity extends AppCompatActivity {

    private EditText searchET = null;
    private TextView title = null;
    private WebView wv = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(VERTICAL);
        initTitle(layout);
        initHeader(layout);
        initWV(layout);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        setContentView(layout, params);
    }

    private void initTitle(LinearLayout layout) {
        title = new TextView(this);
        title.setText("A sample browser");
        title.setTextSize(17f);
        title.setGravity(Gravity.CENTER);
        LayoutParams titleParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        layout.addView(title,titleParams);
    }

    private void initWV(LinearLayout v) {
        wv = new WebView(this);
        LayoutParams wvParams = new LayoutParams(LayoutParams.MATCH_PARENT, 0);
        wvParams.weight = 1;
        v.addView(wv, wvParams);
        setWV();
    }

    private void setWV() {
        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        wv.getSettings().setDefaultTextEncodingName("utf-8");
        wv.getSettings().setAllowFileAccess(true);
//        wv.getSettings().setAllowFileAccessFromFileURLs(true);

        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                title.setText("" + view.getTitle());
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });
    }

    private void initHeader(LinearLayout v) {
        LinearLayout headerLayout = new LinearLayout(this);
        headerLayout.setOrientation(HORIZONTAL);
        LayoutParams headerLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        v.addView(headerLayout, headerLayoutParams);

        searchET = new EditText(this);
        LayoutParams headerETParams = new LayoutParams(0, LayoutParams.WRAP_CONTENT);
        headerETParams.weight = 1;
        searchET.setHint("pl,input something");
        searchET.setMaxLines(1);
        searchET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return (event.getKeyCode()==KeyEvent.KEYCODE_ENTER);
            }
        });

        headerLayout.addView(searchET, 0, headerETParams);

        LayoutParams headerBtnParams = new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        Button searchBtn = new Button(this);
        searchBtn.setText("search");
        headerLayout.addView(searchBtn, 1, headerBtnParams);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String urlString = searchET.getText().toString().trim();
                wv.loadUrl(urlString);
            }
        });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && wv.canGoBack()) {
            wv.goBack();
            return true;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyWV();
    }

    private void destroyWV() {
        wv.removeAllViews();
        wv.destroy();
        wv = null;
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }
}
