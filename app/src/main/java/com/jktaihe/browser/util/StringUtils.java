package com.jktaihe.browser.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jktaihe on 15/7/17.
 * blog: blog.jktaihe.com
 */

public class StringUtils {

    public static final String URL = "(https?|ftp|file)://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]";

    public static boolean isUrl(String url) {
        Pattern p = Pattern.compile(URL);
        Matcher m = p.matcher(url);
        return m.matches();
    }

}
