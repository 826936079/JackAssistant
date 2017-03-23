package com.jack.jackassistant.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.astuetz.PagerSlidingTabStrip;
import com.jack.jackassistant.R;

/**
 * Created by xiaofeng on 2017/3/23.
 */

public class MessageSendToolLayout extends RelativeLayout implements View.OnClickListener {

    //message input layout relative
    private EditText inputEditText;
    private Button faceButton;
    private Button funcButton;
    private Button sendButton;

    //message provider layout relative
    private RelativeLayout messageProviderLayout;
    private LinearLayout faceProviderLayout;
    private ViewPager faceCategoryViewPager;
    private PagerSlidingTabStrip faceCategoryTabStrip;


    private Context context;

    public MessageSendToolLayout(Context context) {
        super(context);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.message_send_tool_layout, this);
    }

    public MessageSendToolLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.message_send_tool_layout, this);
    }

    public MessageSendToolLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.message_send_tool_layout, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initView();
    }

    private void initView() {

        inputEditText = (EditText) findViewById(R.id.inputEditText);
        faceButton = (Button) findViewById(R.id.faceButton);
        funcButton = (Button) findViewById(R.id.funcButton);
        sendButton = (Button) findViewById(R.id.sendButton);

        messageProviderLayout = (RelativeLayout) findViewById(R.id.messageProviderLayout);
        faceProviderLayout = (LinearLayout) findViewById(R.id.faceProviderLayout);
        faceCategoryViewPager = (ViewPager) findViewById(R.id.faceCategoryViewPager);
        faceCategoryTabStrip = (PagerSlidingTabStrip) findViewById(R.id.faceCategoryTabStrip);

        faceButton.setOnClickListener(this);

    }

    private void initDate() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.faceButton:
                messageProviderLayout.setVisibility(VISIBLE);
                faceProviderLayout.setVisibility(VISIBLE);
                break;

            default:
                break;

        }

    }
}
