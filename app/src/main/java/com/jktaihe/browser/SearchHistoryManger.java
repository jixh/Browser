package com.jktaihe.browser;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.widget.ArrayAdapter;

import static android.content.Context.CONTEXT_IGNORE_SECURITY;
import static android.content.Context.MODE_WORLD_WRITEABLE;
import static com.jktaihe.browser.Constant.HISTORY;

/**
 * Created by jktaihe on 12/3/17.
 * blag: blag.jktaihe.com
 */

public class SearchHistoryManger {

    private SharedPreferences sp = null;
    private ArrayAdapter<String> histroyAdapter = null;
    private Context context;

    private SearchHistoryManger(){

    }

    private static class Holder{
        private static final SearchHistoryManger SINGLETON= new SearchHistoryManger();
    }


    public static SearchHistoryManger getInstance() {
        return Holder.SINGLETON;
    }


    public void init(Context context) {
        this.context = context;
        sp = context.getSharedPreferences(HISTORY,MODE_WORLD_WRITEABLE);//初始化context后调用
    }



    private String[] readHistroy() {
        String histroy = sp.getString(HISTORY,"");
        if (histroy.equals(""))
            return null;
        histroy = histroy.replaceAll("\\[|\\]","");
        histroy = histroy.substring(0, histroy.length()-2);
        return histroy.split(",");
    }

    private void addHistroy(String str) {
        String histroy = sp.getString(HISTORY,"");
        histroy = histroy.replaceAll("\\["+str+"\\]\\,","");
        histroy = "["+str+"],"+histroy;
        sp.edit().putString(HISTORY,histroy).commit();
    }

    /**
     *
     * @param outUri //com.jc.apps
     */
    private void readOuterAppHistory(String outUri){
        Context serverContext = null;
        try {
            serverContext = context.createPackageContext(outUri,CONTEXT_IGNORE_SECURITY);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        SharedPreferences sp = serverContext.getSharedPreferences("",Context.MODE_WORLD_READABLE);
    }





}
