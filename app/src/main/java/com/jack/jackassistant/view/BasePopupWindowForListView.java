package com.jack.jackassistant.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupWindow;

import java.util.List;

/**
 * Created by xiaofeng on 2017/4/9.
 */

public abstract class BasePopupWindowForListView<T> extends PopupWindow {

    protected View mContentView;
    protected Context mContext;
    protected List<T> mDatas;

    public BasePopupWindowForListView(View contentView, int width, int height, boolean focusable) {
        this(contentView, width, height, focusable, null);
    }

    public BasePopupWindowForListView(View contentView, int width, int height, boolean focusable,
                                      List<T> datas) {
        this(contentView, width, height, focusable, datas, new Object[0]);
    }

    public BasePopupWindowForListView(View contentView, int width, int height,
                                      boolean focusable, List<T> datas, Object... params) {
        super(contentView, width, height, focusable);
        mContentView = contentView;
        mContext = contentView.getContext();
        if (datas != null) {
            mDatas = datas;
        }
        if (params != null && params.length > 0) {
            beforeInitWeNeedSomeParams(params);
        }

        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setTouchable(true);
        setOutsideTouchable(true);
        setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    dismiss();
                    return true;
                }
                return false;
            }
        });

        initView();
        initEvent();
        init();

    }

    protected abstract void initView();

    protected abstract void initEvent();

    protected abstract void init();

    protected abstract void beforeInitWeNeedSomeParams(Object... params);

    public View findViewById(int id){
        return mContentView.findViewById(id);
    }

    public static int dpToPx(Context context, int dp) {
        return (int) (context.getResources().getDisplayMetrics().density * dp + 0.5f);
    }

}
