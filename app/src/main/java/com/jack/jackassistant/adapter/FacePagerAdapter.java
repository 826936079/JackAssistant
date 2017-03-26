package com.jack.jackassistant.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by xiaofeng on 2017/3/26.
 */

public class FacePagerAdapter extends PagerAdapter {

    private List<View> gridViews;

    public FacePagerAdapter(List<View> gridViews) {
        this.gridViews = gridViews;
    }

    @Override
    public int getCount() {
        return gridViews == null ? 0 : gridViews.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(gridViews.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(gridViews.get(position));
        return gridViews.get(position);
    }
}
