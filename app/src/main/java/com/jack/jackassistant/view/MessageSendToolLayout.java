package com.jack.jackassistant.view;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.jack.jackassistant.R;
import com.jack.jackassistant.adapter.FaceCategoryAdapter;
import com.jack.jackassistant.app.OnOperationListener;
import com.jack.jackassistant.util.KeyboardUtils;
import com.jack.jackassistant.util.MyLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xiaofeng on 2017/3/23.
 */

public class MessageSendToolLayout extends RelativeLayout implements View.OnClickListener {

    private static final String TAG = "MessageSendToolLayout";

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

    //adapter
    private FaceCategoryAdapter faceCategroyAdapter;
    private Map<Integer, List<String>> faceDatas;

    private OnOperationListener onOperationListener;

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
        initDate();
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
        funcButton.setOnClickListener(this);
        sendButton.setOnClickListener(this);
        inputEditText.setOnClickListener(this);

        inputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s == null || s.toString().trim().isEmpty()) {
                    funcButton.setVisibility(VISIBLE);
                    sendButton.setVisibility(GONE);
                } else {
                    sendButton.setEnabled(true);
                    sendButton.setVisibility(VISIBLE);
                    funcButton.setVisibility(GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void initDate() {

        faceDatas = faceTestData();

        faceCategroyAdapter = new FaceCategoryAdapter(((FragmentActivity)context).getSupportFragmentManager(), faceDatas);
        faceCategoryViewPager.setAdapter(faceCategroyAdapter);

        faceCategoryTabStrip.setViewPager(faceCategoryViewPager);
        if (faceDatas.size() < 2) {
            faceCategoryTabStrip.setVisibility(GONE);
        }

    }

    private Map<Integer, List<String>> faceTestData() {
        Map<Integer, List<String>> faceTestDatas = new HashMap<Integer, List<String>>();
        List<String> resStringListBig = new ArrayList<String>();
        List<String> resStringListCig = new ArrayList<String>();
        List<String> resStringListDig = new ArrayList<String>();

        for (int index = 1; index <= 10; index++) {
            resStringListBig.add("big" + index);
        }

        for (int index = 1; index <= 7; index++) {
            resStringListCig.add("cig" + index);
        }

        for (int index = 1; index <= 24; index++) {
            resStringListDig.add("dig" + index);
        }

        faceTestDatas.put(R.drawable.em_cate_duck, resStringListBig);
        faceTestDatas.put(R.drawable.em_cate_rib, resStringListCig);
        faceTestDatas.put(R.drawable.em_cate_magic, resStringListDig);

        MyLog.e(TAG, faceTestDatas.toString());
        return faceTestDatas;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.faceButton:
                if (faceProviderLayout.getVisibility() == VISIBLE) {
                    hideFaceProviderLayout();
                    KeyboardUtils.showKeyBoard((Activity) context);
                } else {
                    showFaceProviderLayout();
                    KeyboardUtils.hideKeyBoard((Activity) context);
                }
                break;
            case R.id.funcButton:
                break;
            case R.id.sendButton:
                String msg = inputEditText.getText().toString();
                if (msg == null || msg.trim().isEmpty()) {
                    Toast.makeText(context, R.string.empty_msg, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (onOperationListener != null) {
                    onOperationListener.sendMessages(msg);
                }
                inputEditText.setText("");

                break;
            case R.id.inputEditText:
                hideFaceProviderLayout();

                break;

            default:
                break;

        }

    }

    public void hideMessageProviderLayout() {
        hideFaceProviderLayout();
        KeyboardUtils.hideKeyBoard((Activity) context);
    }

    private void hideFaceProviderLayout() {
        messageProviderLayout.setVisibility(GONE);
        faceProviderLayout.setVisibility(GONE);

    }

    private void showFaceProviderLayout() {
        KeyboardUtils.hideKeyBoard((Activity) context);
        postDelayed(new Runnable() {
            @Override
            public void run() {
                messageProviderLayout.setVisibility(VISIBLE);
                faceProviderLayout.setVisibility(VISIBLE);
            }
        }, 50);

    }


    public OnOperationListener getOnOperationListener() {
        return onOperationListener;
    }

    public void setOnOperationListener(OnOperationListener onOperationListener) {
        this.onOperationListener = onOperationListener;
        faceCategroyAdapter.setOnOperationListener(onOperationListener);
    }
}
