package com.jack.jackassistant.util;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.jack.jackassistant.bean.ChatMessage;
import com.jack.jackassistant.bean.Result;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;

/**
 * Created by xiaofeng on 2017/3/12.
 */

public class HttpUtils {

    public static final String API_URL = "http://www.tuling123.com/openapi/api";
    public static final String KEY = "1e91d04c5b2d4dc2aa8b31eace653456";
    public static final String USER_ID = "0728";

    public static ChatMessage getChatMessage (String msg) {
        ChatMessage chatMessage = new ChatMessage();
        String jsonStr = doGet(msg);
        Log.e("jack", "jsonStr:" + jsonStr);
        Gson gson = new Gson();
        try {
            Result result = gson.fromJson(jsonStr, Result.class);
            chatMessage.setMsg(result.getText());
        } catch (JsonSyntaxException e) {
            chatMessage.setMsg("服务器繁忙，请稍后再试");
            e.printStackTrace();
        }
        chatMessage.setDate(new Date());
        chatMessage.setType(ChatMessage.Type.INCOMING);

        return chatMessage;
    }


    public static String doGet (String msg) {
        String result = null;
        String url = setParams(msg);
        HttpURLConnection conn = null;
        InputStream is = null;
        ByteArrayOutputStream baos = null;

        try {
            URL urlNet = new URL(url);
            conn = (HttpURLConnection) urlNet.openConnection();
            conn.setReadTimeout(5 * 1000);
            conn.setConnectTimeout(5 * 1000);
            conn.setRequestMethod("GET");

            is = conn.getInputStream();
            baos = new ByteArrayOutputStream();
            int len = -1;
            byte[] buffer = new byte[128];
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            baos.flush();
            result = new String(baos.toByteArray());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (baos != null) {
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public static String setParams (String msg) {
        String url = null;
        try {
            url = API_URL + "?key=" + KEY + "&info=" + URLEncoder.encode(msg, "UTF-8") + "&userid=" + USER_ID;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return url;
    }


}
