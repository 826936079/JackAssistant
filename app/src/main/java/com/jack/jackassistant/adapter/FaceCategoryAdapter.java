package com.jack.jackassistant.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.astuetz.PagerSlidingTabStrip;
import com.jack.jackassistant.app.OnOperationListener;
import com.jack.jackassistant.fragment.FacePageFragment;

import java.util.List;
import java.util.Map;

/**
 * Created by xiaofeng on 2017/3/26.
 */

public class FaceCategoryAdapter extends FragmentPagerAdapter implements PagerSlidingTabStrip.IconTabProvider {

    private Map<Integer, List<String>> mData;

    private OnOperationListener mOnOperationListener;

    public FaceCategoryAdapter(FragmentManager fm, Map<Integer, List<String>> data) {
        super(fm);
        this.mData = data;
    }

    @Override
    public Fragment getItem(int position) {
        //get values of the key which is on this position
        FacePageFragment facePageFragment = new FacePageFragment(position, mData.get(mData.keySet().toArray()[position]));
        facePageFragment.setOnOperationListener(mOnOperationListener);

        return facePageFragment;
    }

    @Override
    public int getCount() {
        return mData == null ? 0: mData.size();
    }

    @Override
    public int getPageIconResId(int position) {
        return (int) mData.keySet().toArray()[position];
    }

    public OnOperationListener getOnOperationListener() {
        return mOnOperationListener;
    }

    public void setOnOperationListener(OnOperationListener onOperationListener) {
        this.mOnOperationListener = onOperationListener;
    }
}
