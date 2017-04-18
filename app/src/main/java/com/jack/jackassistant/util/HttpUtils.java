package com.jack.jackassistant.util;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.jack.jackassistant.bean.ChatMessage;
import com.jack.jackassistant.bean.RobotCookBookResult;
import com.jack.jackassistant.bean.RobotNewsResult;
import com.jack.jackassistant.bean.RobotTextResult;
import com.jack.jackassistant.bean.RobotUrlResult;
import com.jack.jackassistant.manager.GlobleManager;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by xiaofeng on 2017/3/12.
 */

public class HttpUtils {

    private static final String TAG = "HttpUtils";

    public static final String API_URL = "http://www.tuling123.com/openapi/api";
    public static final String KEY = "1e91d04c5b2d4dc2aa8b31eace653456";
    public static final String USER_ID = "0728";

    private static final String CODE_TEXT = "100000";  //文本类
    private static final String CODE_URL = "200000";  //链接类
    private static final String CODE_NEWS = "302000";  //新闻类
    private static final String CODE_COOKBOOK = "308000";  //菜谱类

    private static final int SHOW_MAX_NUM = 3;

    public static ChatMessage getChatMessage (String msg) {
        ChatMessage chatMessage = new ChatMessage();
        //get
        //String jsonStr = doGet(msg);
        //post
        String jsonStr = doPost(msg);
        MyLog.e(TAG, "jsonStr:" + jsonStr);

        String code = null;
        try {
            code = new JSONObject(jsonStr).optString("code");
            MyLog.e(TAG, "code:" + code);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        try {

            if (code.equals(CODE_TEXT)) {
                MyLog.e(TAG, "CODE_TEXT");
                RobotTextResult result = gson.fromJson(jsonStr, RobotTextResult.class);
                chatMessage.setContent(result.getText());
            } else if (code.equals(CODE_URL)) {
                MyLog.e(TAG, "CODE_URL");
                RobotUrlResult result = gson.fromJson(jsonStr, RobotUrlResult.class);

                StringBuffer sb = new StringBuffer();
                sb.append(result.getText()).append("\n").append(result.getUrl());

                chatMessage.setContent(sb.toString());
            } else if (code.equals(CODE_NEWS)) {
                MyLog.e(TAG, "CODE_NEWS");
                RobotNewsResult result = gson.fromJson(jsonStr, RobotNewsResult.class);

                StringBuffer sb = new StringBuffer();
                int showNum = result.getList().size() > SHOW_MAX_NUM ? SHOW_MAX_NUM : result.getList().size();
                for (int index = 0; index < showNum; index++) {
                    RobotNewsResult.ListBean listBean = result.getList().get(index);
                    sb.append(listBean.getArticle()).append("\n").append(listBean.getDetailurl()).append("\n");
                    if (showNum > 1 && index != showNum - 1) {
                        sb.append("\n");
                    }
                }

                chatMessage.setContent(sb.toString());
            } else if (code.equals(CODE_COOKBOOK)) {
                MyLog.e(TAG, "CODE_COOKBOOK");
                RobotCookBookResult result = gson.fromJson(jsonStr, RobotCookBookResult.class);

                StringBuffer sb = new StringBuffer();
                int showNum = result.getList().size() > SHOW_MAX_NUM ? SHOW_MAX_NUM : result.getList().size();
                for (int index = 0; index < showNum; index++) {
                    RobotCookBookResult.ListBean listBean = result.getList().get(index);
                    if (index == 0) {
                        sb.append(listBean.getName()).append("\n");
                    }
                    sb.append(listBean.getInfo()).append("\n").append(listBean.getDetailurl()).append("\n");
                    if (showNum > 1 && index != showNum - 1) {
                        sb.append("\n");
                    }
                }

                chatMessage.setContent(sb.toString());
            } else {
                MyLog.e(TAG, "暂未开通该类型");
            }


        } catch (JsonSyntaxException e) {
            chatMessage.setContent("服务器繁忙，请稍后再试");
            e.printStackTrace();
        }
        chatMessage.setSendType(ChatMessage.SendType.INCOMING);
        chatMessage.setDate(new Date());
        chatMessage.setContentType(ChatMessage.ContentType.TEXT);

        return chatMessage;
    }

    public static String doPost (String msg) {
        String result = null;

        FormBody.Builder builder = new FormBody.Builder();
        RequestBody requestBody = builder
                .add("key", KEY)
                .add("info", msg)
                .add("userid", USER_ID)
                .build();

        Request request = new Request.Builder()
                .post(requestBody)
                .url(API_URL)
                .build();

        Call call = GlobleManager.getsOkHttpClient().newCall(request);
        try {
            Response response = call.execute();
            ResponseBody body = response.body();
            result = body.string();

        } catch (IOException e) {
            e.printStackTrace();
        }


        return result;
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
