package com.jktaihe.browser;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import com.jktaihe.browser.util.StringUtils;
import static android.widget.LinearLayout.HORIZONTAL;
import static android.widget.LinearLayout.VERTICAL;

/**
 * Created by jktaihe on 12/3/17.
 * blag: blag.jktaihe.com
 */

public class MainActivity extends AppCompatActivity {

    private EditText searchET = null;
    private WebView wv = null;
    private LinearLayout layout = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layout = new LinearLayout(this);
        layout.setOrientation(VERTICAL);
        initHeader(layout);
        initWV(layout);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        setContentView(layout, params);
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
                if ((event.getKeyCode()==KeyEvent.KEYCODE_SEARCH)){

                }
                return true;
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
                search(searchET.getText().toString().trim());
            }
        });
    }

    private void search(String s) {

        if (TextUtils.isEmpty(s)){
            Toast.makeText(this,"输入不可以为空",Toast.LENGTH_SHORT).show();
            return;
        }

        String netUrl;

        if (StringUtils.isUrl(s)){
            netUrl = s;
        }else {
            netUrl = "https://www.google.com/#newwindow=1&q="+s;
        }

        wv.loadUrl(netUrl);
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
        layout.removeAllViews();
        layout = null;
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
