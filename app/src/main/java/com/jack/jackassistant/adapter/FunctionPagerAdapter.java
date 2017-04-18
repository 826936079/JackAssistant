package com.jack.jackassistant.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by xiaofeng on 2017/3/28.
 */

public class FunctionPagerAdapter extends PagerAdapter {

    private List<View> mFunctionListViews;

    public FunctionPagerAdapter(List<View> listViews) {
        this.mFunctionListViews = listViews;
    }

    @Override
    public int getCount() {
        return mFunctionListViews == null ? 0 : mFunctionListViews.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mFunctionListViews.get(position));
        return mFunctionListViews.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mFunctionListViews.get(position));
    }
}
