package com.jack.jackassistant.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jack.jackassistant.R;
import com.jack.jackassistant.bean.ChatMessage;

import java.text.SimpleDateFormat;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by xiaofeng on 2017/3/12.
 */

public class ChatMessageAdapter extends BaseAdapter {

    List<ChatMessage> data;
    LayoutInflater inflater;

    public ChatMessageAdapter(Context context, List<ChatMessage> data) {
        this.data = data;
        inflater = LayoutInflater.from(context);
        Log.d(TAG, "ChatMessageAdapter() called with: context = [" + context + "], data = [" + data + "]");
    }

    @Override
    public int getCount() {
        return data.size();
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
        ChatMessage chatMessage = data.get(position);
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            if (getItemViewType(position) == 0) {
                convertView = inflater.inflate(R.layout.item_from_msg, parent, false);
                holder.tvDate = (TextView) convertView.findViewById(R.id.tv_from_date);
                holder.tvMsg = (TextView) convertView.findViewById(R.id.tv_from_msg);
            } else {
                convertView = inflater.inflate(R.layout.item_to_msg, parent, false);
                holder.tvDate = (TextView) convertView.findViewById(R.id.tv_to_date);
                holder.tvMsg = (TextView) convertView.findViewById(R.id.tv_to_msg);
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        holder.tvDate.setText(format.format(chatMessage.getDate()));
        holder.tvMsg.setText(chatMessage.getMsg());

        return convertView;
    }

    @Override
    public int getViewTypeCount() {
        return ChatMessage.Type.values().length;
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage chatMessage = data.get(position);
        if (chatMessage.getType() == ChatMessage.Type.INCOMING) {
            return 0;
        }
        return 1;
    }

    public class ViewHolder {
        TextView tvMsg;
        TextView tvDate;
    }
}
