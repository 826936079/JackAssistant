package com.jack.jackassistant.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jack.jackassistant.R;
import com.jack.jackassistant.bean.Function;

import java.util.List;

/**
 * Created by xiaofeng on 2017/3/29.
 */

public class FunctionAdapter extends BaseAdapter {

    private Context context;
    private List<Function> functionListDatas;

    public FunctionAdapter(Context context, List<Function> functionListDatas) {
        this.context = context;
        this.functionListDatas = functionListDatas;
    }

    @Override
    public int getCount() {
        return functionListDatas == null ? 0 : functionListDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return functionListDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Function function = functionListDatas.get(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.function_gridview_item, parent, false);

            viewHolder.functionImageView = (ImageView) convertView.findViewById(R.id.functionImageView);
            viewHolder.functionTextView = (TextView) convertView.findViewById(R.id.functionTextView);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.functionTextView.setText(function.getFunctionName());

        int resId = context.getResources().getIdentifier(function.getFunctionImageString(), "drawable", context.getPackageName());

        viewHolder.functionImageView.setImageResource(resId);


        return convertView;
    }

    class ViewHolder {
        ImageView functionImageView;
        TextView functionTextView;

    }

}
