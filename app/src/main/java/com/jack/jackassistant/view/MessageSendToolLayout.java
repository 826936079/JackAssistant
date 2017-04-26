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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.astuetz.PagerSlidingTabStrip;
import com.jack.jackassistant.R;
import com.jack.jackassistant.adapter.FaceCategoryAdapter;
import com.jack.jackassistant.adapter.FunctionAdapter;
import com.jack.jackassistant.adapter.FunctionPagerAdapter;
import com.jack.jackassistant.app.OnOperationListener;
import com.jack.jackassistant.bean.Function;
import com.jack.jackassistant.util.KeyboardUtils;
import com.jack.jackassistant.util.MyLog;
import com.jack.jackassistant.util.ToastUtil;

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
    private EditText mInputEditText;
    private Button mFaceButton;
    private Button mFuncButton;
    private Button mSendButton;
    private Button mVoiceButton;
    private AudioRecorderButton mAudioRecorderButton;

    //message provider layout relative
    private RelativeLayout mMessageProviderLayout;
    private LinearLayout mFaceProviderLayout;
    private ViewPager mFaceCategoryViewPager;
    private PagerSlidingTabStrip mFaceCategoryTabStrip;

    //function provider layout relative
    private LinearLayout mFunctionProviderLayout;
    private ViewPager mFunctionViewPager;
    private LinearLayout mFunctionIndicator;

    //adapter
    private FaceCategoryAdapter mFaceCategroyAdapter;
    private Map<Integer, List<String>> mFaceDatas;

    private FunctionPagerAdapter mFunctionPagerAdapter;
    private List<View> mFunctionListViews;
    private List<Function> mfunctionListDatas;
    private List<ImageView> mFunctionIndicatorListViews;

    private int mCurrentStatus = STATUS_INPUT;

    private static final int STATUS_INPUT = 0;
    private static final int STATUS_RECORDER = 1;

    public static final int FUNCTION_PAGE_COUNT = 8;

    private OnOperationListener mOnOperationListener;

    private Context mContext;

    public MessageSendToolLayout(Context context) {
        super(context);
        this.mContext = context;
        LayoutInflater.from(context).inflate(R.layout.message_send_tool_layout, this);
    }

    public MessageSendToolLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        LayoutInflater.from(context).inflate(R.layout.message_send_tool_layout, this);
    }

    public MessageSendToolLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        LayoutInflater.from(context).inflate(R.layout.message_send_tool_layout, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initView();
        initDate();
    }

    private void initView() {

        mInputEditText = (EditText) findViewById(R.id.inputEditText);
        mFaceButton = (Button) findViewById(R.id.faceButton);
        mFuncButton = (Button) findViewById(R.id.funcButton);
        mSendButton = (Button) findViewById(R.id.sendButton);
        mVoiceButton = (Button) findViewById(R.id.voiceButton);
        mAudioRecorderButton = (AudioRecorderButton) findViewById(R.id.btn_audio_recorder);

        mMessageProviderLayout = (RelativeLayout) findViewById(R.id.messageProviderLayout);

        mFaceProviderLayout = (LinearLayout) findViewById(R.id.faceProviderLayout);
        mFaceCategoryViewPager = (ViewPager) findViewById(R.id.faceCategoryViewPager);
        mFaceCategoryTabStrip = (PagerSlidingTabStrip) findViewById(R.id.faceCategoryTabStrip);

        mFunctionProviderLayout = (LinearLayout) findViewById(R.id.functionProviderLayout);
        mFunctionViewPager = (ViewPager) findViewById(R.id.functionViewPager);
        mFunctionIndicator = (LinearLayout) findViewById(R.id.functionIndicator);

        mFaceButton.setOnClickListener(this);
        mFuncButton.setOnClickListener(this);
        mSendButton.setOnClickListener(this);
        mVoiceButton.setOnClickListener(this);
        mInputEditText.setOnClickListener(this);

        mInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s == null || s.toString().trim().isEmpty()) {
                    mFuncButton.setVisibility(VISIBLE);
                    mSendButton.setVisibility(GONE);
                } else {
                    mSendButton.setEnabled(true);
                    mSendButton.setVisibility(VISIBLE);
                    mFuncButton.setVisibility(GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void initDate() {

        mFaceDatas = faceTestData();

        mFaceCategroyAdapter = new FaceCategoryAdapter(((FragmentActivity) mContext).getSupportFragmentManager(), mFaceDatas);
        mFaceCategoryViewPager.setAdapter(mFaceCategroyAdapter);

        mFaceCategoryTabStrip.setViewPager(mFaceCategoryViewPager);
        if (mFaceDatas.size() < 2) {
            mFaceCategoryTabStrip.setVisibility(GONE);
        }

        mFunctionIndicatorListViews = new ArrayList<ImageView>();

        mFunctionListViews = getFunctionViews();
        mFunctionPagerAdapter = new FunctionPagerAdapter(mFunctionListViews);
        mFunctionViewPager.setAdapter(mFunctionPagerAdapter);

        mFunctionViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int index = 0; index < mFunctionIndicatorListViews.size(); index++) {
                    if (position == index) {
                        mFunctionIndicatorListViews.get(index).setImageResource(R.drawable.point_selected);
                    } else {
                        mFunctionIndicatorListViews.get(index).setImageResource(R.drawable.point_normal);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    private List<View> getFunctionViews() {
        mfunctionListDatas = getFunctionTestDatas();

        List<View> funcListViews = new ArrayList<View>();

        int funcSize = mfunctionListDatas.size();
        int viewPageNum = funcSize % FUNCTION_PAGE_COUNT == 0 ? funcSize / FUNCTION_PAGE_COUNT : ( funcSize / FUNCTION_PAGE_COUNT + 1);
        for (int index = 0; index < viewPageNum; index ++) {
            LinearLayout gridViewLayout = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.function_gridview, null, false);
            GridView gridView = (GridView) gridViewLayout.findViewById(R.id.functionGridView);

            List<Function> listData = mfunctionListDatas.subList(index * FUNCTION_PAGE_COUNT, (index + 1) * FUNCTION_PAGE_COUNT > funcSize ? funcSize : (index + 1) * FUNCTION_PAGE_COUNT);
            FunctionAdapter functionAdapter = new FunctionAdapter(mContext, listData);
            gridView.setAdapter(functionAdapter);

            final int currentIndex = index;
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (mOnOperationListener != null) {
                        mOnOperationListener.selectedFunction(mfunctionListDatas.get(currentIndex * FUNCTION_PAGE_COUNT + position));
                    }
                }
            });

            funcListViews.add(gridViewLayout);

            ImageView indicatorImageView = new ImageView(mContext);
            indicatorImageView.setImageResource(R.drawable.point_normal);

            if (index == 0) {
                indicatorImageView.setImageResource(R.drawable.point_selected);
            }

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.leftMargin = 10;
            layoutParams.topMargin = 10;
            layoutParams.rightMargin = 10;
            layoutParams.bottomMargin = 10;

            indicatorImageView.setLayoutParams(layoutParams);

            mFunctionIndicator.addView(indicatorImageView);
            mFunctionIndicatorListViews.add(indicatorImageView);

        }

        return funcListViews;
    }

    private List<Function> getFunctionTestDatas() {
        List<Function> functions = new ArrayList<Function>();
//        for (int i = 0; i < 5; i++) {
            Function functionGallery = new Function(getContext().getString(R.string.gallery), "gallery");
            Function functionTakePhoto = new Function(getContext().getString(R.string.take_photo), "take_photo");
            functions.add(functionGallery);
            functions.add(functionTakePhoto);
//        }

        return functions;
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
                showInputStatus(STATUS_INPUT);
                if (mFaceProviderLayout.getVisibility() == VISIBLE) {
                    hideFaceFunctionProviderLayout();
                    KeyboardUtils.showKeyBoard((Activity) mContext);
                } else {
                    showFaceProviderLayout();
                    KeyboardUtils.hideKeyBoard((Activity) mContext);
                }
                break;
            case R.id.funcButton:
                showInputStatus(STATUS_INPUT);
                if (mFunctionProviderLayout.getVisibility() == VISIBLE) {
                    hideFaceFunctionProviderLayout();
                    KeyboardUtils.showKeyBoard((Activity) mContext);
                } else {
                    showFunctionProviderLayout();
                    KeyboardUtils.hideKeyBoard((Activity) mContext);
                }
                break;
            case R.id.sendButton:
                String msg = mInputEditText.getText().toString();
                if (msg == null || msg.trim().isEmpty()) {
                    ToastUtil.showShortToast(mContext, R.string.empty_msg);
                    return;
                }
                if (mOnOperationListener != null) {
                    mOnOperationListener.sendMessages(msg);
                }
                mInputEditText.setText("");

                break;
            case R.id.voiceButton:
                mCurrentStatus = mCurrentStatus == STATUS_INPUT ? STATUS_RECORDER : STATUS_INPUT;
                showInputStatus(mCurrentStatus);
                hideFaceFunctionProviderLayout();
                KeyboardUtils.hideKeyBoard((Activity) mContext);
                break;

            case R.id.inputEditText:
                hideFaceFunctionProviderLayout();

                break;

            default:
                break;

        }

    }

    private void showInputStatus(int status) {
        if (status == STATUS_INPUT) {
            mInputEditText.setVisibility(VISIBLE);
            mAudioRecorderButton.setVisibility(GONE);
            mVoiceButton.setBackgroundResource(R.drawable.voice_pressed);
        }else if (status == STATUS_RECORDER) {
            mInputEditText.setVisibility(GONE);
            mAudioRecorderButton.setVisibility(VISIBLE);
            mVoiceButton.setBackgroundResource(R.drawable.keyboard_pressed);
        }
    }

    public void hideMessageProviderLayout() {
        hideFaceFunctionProviderLayout();
        KeyboardUtils.hideKeyBoard((Activity) mContext);
    }

    private void hideFaceFunctionProviderLayout() {
        mMessageProviderLayout.setVisibility(GONE);
        mFaceProviderLayout.setVisibility(GONE);
        mFunctionProviderLayout.setVisibility(GONE);

    }

    private void showFaceProviderLayout() {
        KeyboardUtils.hideKeyBoard((Activity) mContext);
        postDelayed(new Runnable() {
            @Override
            public void run() {
                mMessageProviderLayout.setVisibility(VISIBLE);
                mFaceProviderLayout.setVisibility(VISIBLE);
                mFunctionProviderLayout.setVisibility(GONE);
            }
        }, 50);

    }

    private void showFunctionProviderLayout() {
        KeyboardUtils.hideKeyBoard((Activity) mContext);
        postDelayed(new Runnable() {
            @Override
            public void run() {
                mMessageProviderLayout.setVisibility(VISIBLE);
                mFunctionProviderLayout.setVisibility(VISIBLE);
                mFaceProviderLayout.setVisibility(GONE);
            }
        }, 50);

    }


    public OnOperationListener getOnOperationListener() {
        return mOnOperationListener;
    }

    public void setOnOperationListener(OnOperationListener onOperationListener) {
        this.mOnOperationListener = onOperationListener;
        mFaceCategroyAdapter.setOnOperationListener(onOperationListener);
    }
}
