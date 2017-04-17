package com.jack.jackassistant.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jack.jackassistant.R;
import com.jack.jackassistant.bean.ImageFolder;

import java.util.List;

/**
 * Created by xiaofeng on 2017/4/10.
 */

public class ImageLoaderPopupWindowListViewAdapter extends ArrayAdapter<ImageFolder> {

    private List<ImageFolder> mImageFloderDatas;
    private LayoutInflater mInflater;
    private Context mContext;


    public ImageLoaderPopupWindowListViewAdapter(Context context, List<ImageFolder> objects) {
        super(context, 0, objects);
        mInflater = LayoutInflater.from(context);
        mContext = context;
        mImageFloderDatas = objects;

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        ImageFolder imageFolder = mImageFloderDatas.get(position);

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.image_loader_popup_window_item, parent, false);
            viewHolder.imageLoaderPopupWindowFirstImageView = (ImageView) convertView.findViewById(R.id.imageLoaderPopupWindowFirstImageView);
            viewHolder.imageLoaderPopupWindowDirName = (TextView) convertView.findViewById(R.id.imageLoaderPopupWindowDirName);
            viewHolder.imageLoaderPopupWindowDirCount = (TextView) convertView.findViewById(R.id.imageLoaderPopupWindowDirCount);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //重置状态
        viewHolder.imageLoaderPopupWindowFirstImageView.setImageResource(R.drawable.pictures_no);

        Glide.with(mContext).load(imageFolder.getFirstImagePath()).into(viewHolder.imageLoaderPopupWindowFirstImageView);
        viewHolder.imageLoaderPopupWindowDirName.setText(imageFolder.getDirName());
        viewHolder.imageLoaderPopupWindowDirCount.setText(String.format(mContext.getString(R.string.image_total_sheet), imageFolder.getDirCount()));



        return convertView;
    }

    private class ViewHolder {
        ImageView imageLoaderPopupWindowFirstImageView;
        TextView imageLoaderPopupWindowDirName;
        TextView imageLoaderPopupWindowDirCount;
    }


}
