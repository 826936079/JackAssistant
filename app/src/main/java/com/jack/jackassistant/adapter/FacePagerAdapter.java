package com.jack.jackassistant.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by xiaofeng on 2017/3/26.
 */

public class FacePagerAdapter extends PagerAdapter {

    private List<View> mGridViews;

    public FacePagerAdapter(List<View> gridViews) {
        this.mGridViews = gridViews;
    }

    @Override
    public int getCount() {
        return mGridViews == null ? 0 : mGridViews.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mGridViews.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mGridViews.get(position));
        return mGridViews.get(position);
    }
}
