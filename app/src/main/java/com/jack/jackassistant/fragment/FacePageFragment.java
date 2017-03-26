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
import com.jack.jackassistant.util.MyLog;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FacePageFragment extends Fragment {

    private static final String TAG = "FacePageFragment";

    public static final int FACE_PAGE_COUNT = 12;
    private int position;
    private List<String> data;

    private ViewPager faceViewPager;
    private List<View> gridViews;
    private List<View> pageIndicatorViews;

    private FacePagerAdapter facePagerAdapter;
    private FaceAdapter faceAdapter;

    private LinearLayout pageIndicator;

    private OnOperationListener onOperationListener;

    public FacePageFragment() {
    }

    public FacePageFragment(int position, List<String> data) {
        this.position = position;
        this.data = data;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.face_fragment, container, false);
        faceViewPager = (ViewPager) rootView.findViewById(R.id.faceViewPager);
        pageIndicator = (LinearLayout) rootView.findViewById(R.id.pageIndicator);

        gridViews = new ArrayList<View>();
        pageIndicatorViews = new ArrayList<View>();


        //get all grid view of each face category
        for (int index = 0; index < (data.size() % FACE_PAGE_COUNT == 0 ? data.size() / FACE_PAGE_COUNT : (data.size() / FACE_PAGE_COUNT) + 1); index++) {

            //get face grid view
            LinearLayout gridViewLayout = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.face_gridview, faceViewPager, false);
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
            faceAdapter = new FaceAdapter(getContext(), data.subList(index * FACE_PAGE_COUNT, (index + 1) * FACE_PAGE_COUNT > data.size() ? data.size() : (index + 1) * FACE_PAGE_COUNT));
            MyLog.e(TAG, "index:" + index + "  faceAdapter:" + faceAdapter);
            gridView.setAdapter(faceAdapter);
            final int currentIndex = index;
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (onOperationListener != null) {
                        onOperationListener.selectedFace(data.get(currentIndex * FACE_PAGE_COUNT + position));
                    }
                }
            });


            gridViews.add(gridViewLayout);


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


            pageIndicator.addView(indicatorImageView, layoutParams);
            pageIndicatorViews.add(indicatorImageView);

        }


        facePagerAdapter = new FacePagerAdapter(gridViews);
        faceViewPager.setAdapter(facePagerAdapter);
        faceViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                for (int index = 0; index < pageIndicatorViews.size(); index++) {
                    if (index == position) {
                        pageIndicatorViews.get(index).setBackgroundResource(R.drawable.point_selected);
                    } else {
                        pageIndicatorViews.get(index).setBackgroundResource(R.drawable.point_normal);
                    }
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return rootView;
    }

    public OnOperationListener getOnOperationListener() {
        return onOperationListener;
    }

    public void setOnOperationListener(OnOperationListener onOperationListener) {
        this.onOperationListener = onOperationListener;
    }
}
