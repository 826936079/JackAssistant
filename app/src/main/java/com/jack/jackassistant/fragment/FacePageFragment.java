package com.jack.jackassistant.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jack.jackassistant.R;
import com.jack.jackassistant.adapter.FaceAdapter;
import com.jack.jackassistant.adapter.FacePagerAdapter;
import com.jack.jackassistant.app.OnOperationListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FacePageFragment extends Fragment {

    private static final String TAG = "FacePageFragment";

    public static final int FACE_PAGE_COUNT = 12;
    private int mPosition;
    private List<String> mData;

    private ViewPager mFaceViewPager;
    private List<View> mGridViews;
    private List<View> mPageIndicatorViews;

    private FacePagerAdapter mFacePagerAdapter;
    private FaceAdapter mFaceAdapter;

    private LinearLayout mPageIndicator;

    private OnOperationListener mOnOperationListener;

    public FacePageFragment() {
    }

    public FacePageFragment(int position, List<String> data) {
        this.mPosition = position;
        this.mData = data;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.face_fragment, container, false);
        mFaceViewPager = (ViewPager) rootView.findViewById(R.id.faceViewPager);
        mPageIndicator = (LinearLayout) rootView.findViewById(R.id.pageIndicator);

        mGridViews = new ArrayList<View>();
        mPageIndicatorViews = new ArrayList<View>();


        //get all grid view of each face category
        int viewPageNum = mData.size() % FACE_PAGE_COUNT == 0 ? mData.size() / FACE_PAGE_COUNT : (mData.size() / FACE_PAGE_COUNT) + 1;
        for (int index = 0; index < viewPageNum; index++) {

            //get face grid view
            LinearLayout gridViewLayout = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.face_gridview, mFaceViewPager, false);
            GridView gridView = (GridView) gridViewLayout.findViewById(R.id.faceGridView);


            //use code to layout view
//            GridView gridView = new GridView(getContext());
//            gridView.setNumColumns(4);
//            gridView.setBackgroundColor(Color.TRANSPARENT);
//            gridView.setHorizontalSpacing(1);
//            gridView.setVerticalSpacing(1);
//            gridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
//            gridView.setCacheColorHint(0);
//            gridView.setPadding(5, 0, 5, 0);
//            gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
//            gridView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
//                    FrameLayout.LayoutParams.WRAP_CONTENT));
//            gridView.setGravity(Gravity.CENTER);

            //get each face data of each category in one page
            mFaceAdapter = new FaceAdapter(getContext(), mData.subList(index * FACE_PAGE_COUNT, (index + 1) * FACE_PAGE_COUNT > mData.size() ? mData.size() : (index + 1) * FACE_PAGE_COUNT));
            gridView.setAdapter(mFaceAdapter);
            final int currentIndex = index;
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (mOnOperationListener != null) {
                        mOnOperationListener.selectedFace(mData.get(currentIndex * FACE_PAGE_COUNT + position));
                    }
                }
            });


            mGridViews.add(gridViewLayout);


            //get indicator
            ImageView indicatorImageView = new ImageView(getContext());
            indicatorImageView.setBackgroundResource(R.drawable.point_normal);
            if (index == 0) {
                indicatorImageView.setBackgroundResource(R.drawable.point_selected);
            }
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.width = 10;
            layoutParams.height = 10;
            layoutParams.leftMargin = 10;
            layoutParams.rightMargin = 10;


            mPageIndicator.addView(indicatorImageView, layoutParams);
            mPageIndicatorViews.add(indicatorImageView);

        }


        mFacePagerAdapter = new FacePagerAdapter(mGridViews);
        mFaceViewPager.setAdapter(mFacePagerAdapter);
        mFaceViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int index = 0; index < mPageIndicatorViews.size(); index++) {
                    if (index == position) {
                        mPageIndicatorViews.get(index).setBackgroundResource(R.drawable.point_selected);
                    } else {
                        mPageIndicatorViews.get(index).setBackgroundResource(R.drawable.point_normal);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return rootView;
    }

    public OnOperationListener getOnOperationListener() {
        return mOnOperationListener;
    }

    public void setOnOperationListener(OnOperationListener onOperationListener) {
        this.mOnOperationListener = onOperationListener;
    }
}
