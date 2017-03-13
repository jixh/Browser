package com.jktaihe.browser;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jktaihe on 13/3/17.
 * blag: blag.jktaihe.com
 */

public class BrowserUtils {

    public static void setTextViewUrl(String msg, TextView textview) {

        Pattern pattern = Pattern.compile("(http://|https://|www.){1}[[^\\u4e00-\\u9fa5]&&\\w\\.\\-/:\\?\\&\\%\\@\\_a-zA-Z0-9\\=\\,]+", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(msg);

        int startPoint = 0;

        SpannableString sps = new SpannableString(msg);
        String url;
        while (matcher.find(startPoint)) {
            int endPoint = matcher.end();
            url = matcher.group();
            ClickableSpan clickSpan = new ClickSpan(url,textview.getContext());
            sps.setSpan(clickSpan, endPoint - url.length(), endPoint, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            startPoint = endPoint;
        }

        textview.setText(sps);
    }


    private static class ClickSpan extends ClickableSpan {

        private String text;
        private Context mContext;

        public ClickSpan(String text, Context context) {
            super();
            this.text = text;
            mContext = context;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.linkColor = Color.parseColor("#00648f");
        }

        public void onClick(View widget) {
            Intent intent = new Intent(mContext, MainActivity.class);
            final Bundle bundle = new Bundle();
            if (!text.startsWith("https+://")) {
                text = "https://" + text;
            }
            bundle.putString("url", text);
            intent.putExtras(bundle);
            mContext.startActivity(intent);
        }
    }


}
