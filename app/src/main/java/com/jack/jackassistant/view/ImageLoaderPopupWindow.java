package com.jack.jackassistant.view;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.jack.jackassistant.R;
import com.jack.jackassistant.adapter.ImageLoaderPopupWindowListViewAdapter;
import com.jack.jackassistant.bean.ImageFolder;

import java.util.List;

/**
 * Created by xiaofeng on 2017/4/9.
 */

public class ImageLoaderPopupWindow extends BasePopupWindowForListView<ImageFolder> {

    private ListView mListView;
    private ImageLoaderPopupWindowListViewAdapter mImageLoaderPopupWindowListViewAdapter;
    private OnImageLoaderPopupWindowSelectedListener mOnImageLoaderPopupWindowSelectedListener;

    public interface OnImageLoaderPopupWindowSelectedListener{
        void onImageLoaderPopupWindowSelected(ImageFolder imageFolder);
    }

    public void setOnImageLoaderPopupWindowSelectedListener (OnImageLoaderPopupWindowSelectedListener listener) {
        mOnImageLoaderPopupWindowSelectedListener = listener;
    }

    public ImageLoaderPopupWindow(View contentView, int width, int height, List<ImageFolder> datas) {
        super(contentView, width, height, true, datas);
    }

    @Override
    protected void initView() {

        mListView = (ListView) findViewById(R.id.imageLoaderPopupWindowListView);
        mImageLoaderPopupWindowListViewAdapter = new ImageLoaderPopupWindowListViewAdapter(mContext, mDatas);

        mListView.setAdapter(mImageLoaderPopupWindowListViewAdapter);

    }

    @Override
    protected void initEvent() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mOnImageLoaderPopupWindowSelectedListener != null) {
                    mOnImageLoaderPopupWindowSelectedListener.onImageLoaderPopupWindowSelected(mDatas.get(position));
                }
            }
        });
    }

    @Override
    protected void init() {

    }

    @Override
    protected void beforeInitWeNeedSomeParams(Object... params) {

    }

}
