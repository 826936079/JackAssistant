package com.jack.jackassistant.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.jack.jackassistant.R;
import com.jack.jackassistant.app.OnOperationListener;

import java.util.List;

/**
 * Created by xiaofeng on 2017/3/26.
 */

public class FaceAdapter extends BaseAdapter {

    private static final String TAG = "FaceAdapter";

    private List<String> mData;
    private Context mContext;

    private OnOperationListener mOnOperationListener;

    public FaceAdapter(Context context, List<String> data) {
        this.mData = data;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String emoji = mData.get(position);
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.face_gridview_item, parent, false);
            holder.faceImageView = (ImageView) convertView.findViewById(R.id.faceImageView);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        int resId = mContext.getResources().getIdentifier(emoji, "drawable", mContext.getPackageName());
        holder.faceImageView.setImageResource(resId);

        return convertView;
    }

    public OnOperationListener getOnOperationListener() {
        return mOnOperationListener;
    }

    public void setOnOperationListener(OnOperationListener onOperationListener) {
        this.mOnOperationListener = onOperationListener;
    }

    class ViewHolder {
        ImageView faceImageView;
    }
}
