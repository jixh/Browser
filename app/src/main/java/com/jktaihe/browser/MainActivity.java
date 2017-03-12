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


public class MainActivity extends AppCompatActivity {

    private EditText searchET = null;
    private TextView title = null;
    private WebView wv = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(VERTICAL);

        title = new TextView(this);
        title.setText("a sample browser");
        LayoutParams titleParam = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        layout.addView(title, titleParam);

        initHeader(layout);
        initWV(layout);

        setContentView(layout,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
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

    public void getUrlFromText(String msg, TextView textview) {
        Pattern pattern = Pattern.compile("(http://|https://|www.){1}[[^\\u4e00-\\u9fa5]&&\\w\\.\\-/:\\?\\&\\%\\@\\_a-zA-Z0-9\\=\\,]+", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(msg);
        int startPoint = 0;

        SpannableString sps = new SpannableString(msg);
        String url;
        while (matcher.find(startPoint)) {
            int endPoint = matcher.end();
            url = matcher.group();
            ClickableSpan clickSpan = new ClickSpan(url);
            sps.setSpan(clickSpan, endPoint - url.length(), endPoint, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            startPoint = endPoint;
        }
        textview.setText(sps);
        url = null;

    }


    class ClickSpan extends ClickableSpan {
        String text;
        TextPaint paint = null;

        public ClickSpan(String text) {
            super();
            this.text = text;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.linkColor = Color.parseColor("#00648f");
        }

        public void onClick(View widget) {
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            final Bundle bundle = new Bundle();
            if (!text.startsWith("https+://")) {
                text = "https://" + text;
            }
            bundle.putString("url", text);
            intent.putExtras(bundle);
            startActivity(intent);
        }
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
