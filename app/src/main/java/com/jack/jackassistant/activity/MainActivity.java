package com.jack.jackassistant.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.jack.jackassistant.R;
import com.jack.jackassistant.adapter.ChatMessageAdapter;
import com.jack.jackassistant.bean.ChatMessage;
import com.jack.jackassistant.util.HttpUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    ListView listMsg;
    EditText etInput;
    Button btnSend;

    ChatMessageAdapter chatMessageAdapter;
    List<ChatMessage> data = new ArrayList<ChatMessage>();

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            ChatMessage result = (ChatMessage) msg.obj;
            data.add(result);
            Log.e("jack", "handler Thread.getId():" + Thread.currentThread().getId());
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
        listMsg = (ListView) findViewById(R.id.list_msg);
        etInput = (EditText) findViewById(R.id.et_input);
        btnSend = (Button) findViewById(R.id.btn_send);

    }

    private void initData() {

//        httpTest();to
        data = getInitData();
        chatMessageAdapter = new ChatMessageAdapter(this, data);
        listMsg.setAdapter(chatMessageAdapter);

        btnSend.setOnClickListener(this);
    }

    private List<ChatMessage> getInitData() {
        List<ChatMessage> initData = new ArrayList<ChatMessage>();
        ChatMessage fromChatMessage = new ChatMessage();
        fromChatMessage.setType(ChatMessage.Type.INCOMING);
        fromChatMessage.setDate(new Date());
        fromChatMessage.setMsg(getString(R.string.default_incoming_msg));
        initData.add(fromChatMessage);

//        ChatMessage toChatMessage = new ChatMessage();
//        toChatMessage.setType(ChatMessage.Type.OUTCOMING);
//        toChatMessage.setDate(new Date());
//        toChatMessage.setMsg(getString(R.string.hello));
//        initData.add(toChatMessage);

        return initData;
    }

    private void httpTest() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.e(TAG, "run: ");

                String result = HttpUtils.doGet("hi");
                Log.e("jack", "hi->result=" + result);

                result = HttpUtils.doGet("说个笑话");
                Log.e("jack", "说个笑话->result=" + result);

                result = HttpUtils.doGet("天气");
                Log.e("jack", "天气->result=" + result);
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        final String msg = etInput.getText().toString();
        if (msg.trim().isEmpty()) {
            Toast.makeText(this, R.string.empty_msg, Toast.LENGTH_SHORT).show();
            return;
        }

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setDate(new Date());
        chatMessage.setType(ChatMessage.Type.OUTCOMING);
        chatMessage.setMsg(msg);
        data.add(chatMessage);
        chatMessageAdapter.notifyDataSetChanged();
        Log.e("jack", "onClick Thread.getId():" + Thread.currentThread().getId());

        etInput.setText("");

        new Thread() {
            @Override
            public void run() {
                Log.e("jack", "new Thread.getId():" + Thread.currentThread().getId());
                ChatMessage result = HttpUtils.getChatMessage(msg);
                Message message = Message.obtain();
                message.obj = result;
                handler.sendMessage(message);
            }
        }.start();
    }
}
