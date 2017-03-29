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

    private List<String> data;
    private Context context;

    private OnOperationListener onOperationListener;

    public FaceAdapter(Context context, List<String> data) {
        this.data = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String emoji = data.get(position);
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.face_gridview_item, parent, false);
            holder.faceImageView = (ImageView) convertView.findViewById(R.id.faceImageView);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        int resId = context.getResources().getIdentifier(emoji, "drawable", context.getPackageName());
        holder.faceImageView.setImageResource(resId);

        return convertView;
    }

    public OnOperationListener getOnOperationListener() {
        return onOperationListener;
    }

    public void setOnOperationListener(OnOperationListener onOperationListener) {
        this.onOperationListener = onOperationListener;
    }

    class ViewHolder {
        ImageView faceImageView;
    }
}
