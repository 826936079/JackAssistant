package com.jack.jackassistant.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.jack.jackassistant.R;
import com.jack.jackassistant.adapter.ChatMessageAdapter;
import com.jack.jackassistant.bean.ChatMessage;
import com.jack.jackassistant.util.HttpUtils;
import com.jack.jackassistant.util.MyLog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    ListView messageListView;
    EditText inputEditText;
    Button sendButton;

    ChatMessageAdapter chatMessageAdapter;
    List<ChatMessage> data = new ArrayList<ChatMessage>();

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            ChatMessage result = (ChatMessage) msg.obj;
            data.add(result);
            MyLog.e("jack", "handler Thread.getId():" + Thread.currentThread().getId());
            chatMessageAdapter.notifyDataSetChanged();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();

    }

    private void initView() {
        messageListView = (ListView) findViewById(R.id.messageListView);
        inputEditText = (EditText) findViewById(R.id.inputEditText);
        sendButton = (Button) findViewById(R.id.sendButton);

    }

    private void initData() {

//        httpTest();
//        data = testData();
        data = getInitData();
        chatMessageAdapter = new ChatMessageAdapter(this, data);
        messageListView.setAdapter(chatMessageAdapter);

        sendButton.setOnClickListener(this);
    }

    private List<ChatMessage> getInitData() {

        List<ChatMessage> initData = new ArrayList<ChatMessage>();
        ChatMessage fromChatMessage =
                new ChatMessage(getResources().getString(R.string.text_title),
                        "",
                        getResources().getString(R.string.text_me),
                        "",
                        ChatMessage.SendType.INCOMING,
                        ChatMessage.SendStatus.SUCCESS,
                        new Date(),
                        getString(R.string.default_incoming_msg),
                        ChatMessage.ContentType.TEXT);

        initData.add(fromChatMessage);

        return initData;
    }

    @Override
    public void onClick(View v) {
        final String msg = inputEditText.getText().toString();
        if (msg.trim().isEmpty()) {
            Toast.makeText(this, R.string.empty_msg, Toast.LENGTH_SHORT).show();
            return;
        }

        ChatMessage toChatMessage =
                new ChatMessage(getResources().getString(R.string.text_title),
                        "",
                        getResources().getString(R.string.text_me),
                        "",
                        ChatMessage.SendType.OUTCOMING,
                        ChatMessage.SendStatus.SUCCESS,
                        new Date(),
                        msg,
                        ChatMessage.ContentType.TEXT);

        data.add(toChatMessage);
        chatMessageAdapter.notifyDataSetChanged();
        MyLog.e("jack", "onClick Thread.getId():" + Thread.currentThread().getId());

        inputEditText.setText("");

        new Thread() {
            @Override
            public void run() {
                MyLog.e("jack", "new Thread.getId():" + Thread.currentThread().getId());
                ChatMessage result = HttpUtils.getChatMessage(msg);
                Message message = Message.obtain();
                message.obj = result;
                handler.sendMessage(message);
            }
        }.start();
    }



    /**
     * just for test connection
     */
    private void httpTest() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                MyLog.e(TAG, "run: ");

                String result = HttpUtils.doGet("hi");
                MyLog.e("jack", "hi->result=" + result);

                result = HttpUtils.doGet("说个笑话");
                MyLog.e("jack", "说个笑话->result=" + result);

                result = HttpUtils.doGet("天气");
                MyLog.e("jack", "天气->result=" + result);
            }
        }).start();
    }

    /**
     * just for test data
     */
    private List<ChatMessage> testData() {
        List<ChatMessage> testData = new ArrayList<ChatMessage>();

        ChatMessage chatMessage1 =
                new ChatMessage("from robot",
                        "",
                        "me",
                        "",
                        ChatMessage.SendType.INCOMING,
                        ChatMessage.SendStatus.SUCCESS,
                        new Date(System.currentTimeMillis() - (1000 * 60 * 60 * 24) * 8),
                        "hello",
                        ChatMessage.ContentType.TEXT);


        ChatMessage chatMessage2 =
                new ChatMessage("robot",
                        "",
                        "me reply",
                        "",
                        ChatMessage.SendType.OUTCOMING,
                        ChatMessage.SendStatus.SUCCESS,
                        new Date(System.currentTimeMillis() - (1000 * 60 * 60 * 20) * 8),
                        "hello world",
                        ChatMessage.ContentType.TEXT);


        ChatMessage chatMessage3 =
                new ChatMessage("",
                        "",
                        "",
                        "",
                        ChatMessage.SendType.INCOMING,
                        ChatMessage.SendStatus.SUCCESS,
                        new Date(System.currentTimeMillis() - (1000 * 60 * 60 * 24) * 7),
                        "device_2014_08_21_215311",
                        ChatMessage.ContentType.PHOTO);


        ChatMessage chatMessage4 =
                new ChatMessage("",
                        "",
                        "",
                        "",
                        ChatMessage.SendType.OUTCOMING,
                        ChatMessage.SendStatus.SUCCESS,
                        new Date(System.currentTimeMillis() - (1000 * 60 * 60 * 20) * 7),
                        "device_2014_08_21_215311",
                        ChatMessage.ContentType.PHOTO);


        ChatMessage chatMessage5 =
                new ChatMessage("",
                        "",
                        "",
                        "",
                        ChatMessage.SendType.OUTCOMING,
                        ChatMessage.SendStatus.FAIL,
                        new Date(System.currentTimeMillis()),
                        "test send fail",
                        ChatMessage.ContentType.TEXT);

        ChatMessage chatMessage6 =
                new ChatMessage("",
                        "",
                        "",
                        "",
                        ChatMessage.SendType.OUTCOMING,
                        ChatMessage.SendStatus.ONGOING,
                        new Date(System.currentTimeMillis()),
                        "test sending",
                        ChatMessage.ContentType.TEXT);

        testData.add(chatMessage1);
        testData.add(chatMessage2);
        testData.add(chatMessage3);
        testData.add(chatMessage4);
        testData.add(chatMessage5);
        testData.add(chatMessage6);

        return testData;
    }
}
