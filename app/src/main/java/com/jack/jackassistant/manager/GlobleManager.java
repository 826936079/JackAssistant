package com.jack.jackassistant.manager;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

/**
 * Created by Administrator on 2017/3/16.
 */
public class GlobleManager {

    private static Context sContext;
    private static GlobleManager sGlobleManager;
    private static OkHttpClient sOkHttpClient;
    private static Map<String, List<Cookie>> sListMap;

    private GlobleManager() {
        initOkHttpClient();
    }

    private static void initOkHttpClient() {
        sListMap = new HashMap<String, List<Cookie>>();
        sOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .cookieJar(new CookieJar() {
                    @Override
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                        sListMap.put(url.host(), cookies);
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl url) {
                        List<Cookie> cookies = sListMap.get(url.host());
                        return cookies != null ? cookies : new ArrayList<Cookie>();
                    }
                })
                .build();
    }

    public static GlobleManager getInstance(Context context) {
        if (sGlobleManager == null) {
            sContext = context.getApplicationContext();
            sGlobleManager = new GlobleManager();
        }
        return sGlobleManager;
    }


    public static Context getContext() {
        return sContext;
    }

    public static OkHttpClient getsOkHttpClient() {
        if (sOkHttpClient == null) {
            initOkHttpClient();
        }
        return sOkHttpClient;
    }
}
