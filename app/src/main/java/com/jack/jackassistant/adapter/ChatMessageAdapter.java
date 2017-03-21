package com.jack.jackassistant.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jack.jackassistant.R;
import com.jack.jackassistant.bean.ChatMessage;
import com.jack.jackassistant.util.MyLog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by xiaofeng on 2017/3/12.
 */

public class ChatMessageAdapter extends BaseAdapter {

    List<ChatMessage> data;
    LayoutInflater inflater;
    Context context;

    public ChatMessageAdapter(Context context, List<ChatMessage> data) {
        this.data = data;
        this.context = context;
        inflater = LayoutInflater.from(context);
        MyLog.d(TAG, "ChatMessageAdapter() called with: context = [" + context + "], data = [" + data + "]");
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

            } else {
                convertView = inflater.inflate(R.layout.item_to_msg, parent, false);
            }

            holder.sendDateTextView = (TextView) convertView.findViewById(R.id.sendDateTextView);

            holder.userAvatarImageView = (ImageView) convertView.findViewById(R.id.userAvatarImageView);
            holder.userNameTextView = (TextView) convertView.findViewById(R.id.userNameTextView);

            holder.sendContentTextView = (TextView) convertView.findViewById(R.id.sendContentTextView);
            holder.sendPhotoImageView = (ImageView) convertView.findViewById(R.id.sendPhotoImageView);
            holder.sendFaceImageView = (ImageView) convertView.findViewById(R.id.sendFaceImageView);

            holder.sendFailImageView = (ImageView) convertView.findViewById(R.id.sendFailImageView);
            holder.isSendingProgressBar = (ProgressBar) convertView.findViewById(R.id.isSendingProgressBar);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateString = format.format(chatMessage.getDate());
            String[] date = dateString.split(" ");

            if (position == 0) {
                holder.sendDateTextView.setVisibility(View.VISIBLE);
                holder.sendDateTextView.setText(date[0]);
            } else {
                if (isSameDay(chatMessage.getDate(), data.get(position - 1).getDate())) {
                    holder.sendDateTextView.setVisibility(View.GONE);

                } else {
                    holder.sendDateTextView.setVisibility(View.VISIBLE);
                    if (isSameDay(chatMessage.getDate(), new Date())) {
                        holder.sendDateTextView.setText(date[1]);
                    } else {
                        holder.sendDateTextView.setText(date[0]);
                    }
                }
            }



        } catch (Exception e) {
            e.printStackTrace();
        }

        if (getItemViewType(position) == 0) {
            String fromName = ( chatMessage.getFromName() == null || chatMessage.getFromName().isEmpty() ) ? context.getResources().getString(R.string.text_title) : chatMessage.getFromName();
            holder.userNameTextView.setText(fromName);
        } else {
            String toName = ( chatMessage.getFromName() == null || chatMessage.getToName().isEmpty() ) ? context.getResources().getString(R.string.text_me) : chatMessage.getToName();
            holder.userNameTextView.setText(toName);
        }

        if (chatMessage.getContentType() == null || chatMessage.getContentType() == ChatMessage.ContentType.TEXT) {
            holder.sendContentTextView.setVisibility(View.VISIBLE);
            holder.sendPhotoImageView.setVisibility(View.GONE);
            holder.sendFaceImageView.setVisibility(View.GONE);

            holder.sendContentTextView.setText(chatMessage.getContent());

        } else if (chatMessage.getContentType() == ChatMessage.ContentType.PHOTO) {
            holder.sendContentTextView.setVisibility(View.GONE);
            holder.sendPhotoImageView.setVisibility(View.VISIBLE);
            holder.sendFaceImageView.setVisibility(View.GONE);

            int id = context.getResources().getIdentifier(chatMessage.getContent(), "drawable", context.getPackageName());
            holder.sendPhotoImageView.setImageResource(id);

        } else if (chatMessage.getContentType() == ChatMessage.ContentType.FACE) {
            holder.sendContentTextView.setVisibility(View.GONE);
            holder.sendPhotoImageView.setVisibility(View.GONE);
            holder.sendFaceImageView.setVisibility(View.VISIBLE);

            int id = context.getResources().getIdentifier(chatMessage.getContent(), "drawable", context.getPackageName());
            holder.sendFaceImageView.setImageResource(id);
        }

        if (chatMessage.getSendStatus() == null || chatMessage.getSendStatus() == ChatMessage.SendStatus.SUCCESS) {
            holder.sendFailImageView.setVisibility(View.GONE);
            holder.isSendingProgressBar.setVisibility(View.GONE);

        } else if (chatMessage.getSendStatus() == ChatMessage.SendStatus.FAIL) {
            holder.sendFailImageView.setVisibility(View.VISIBLE);
            holder.isSendingProgressBar.setVisibility(View.GONE);

        } else if (chatMessage.getSendStatus() == ChatMessage.SendStatus.ONGOING) {
            holder.sendFailImageView.setVisibility(View.GONE);
            holder.isSendingProgressBar.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    @Override
    public int getViewTypeCount() {
        return ChatMessage.SendType.values().length;
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage chatMessage = data.get(position);
        if (chatMessage.getSendType() == ChatMessage.SendType.INCOMING) {
            return 0;
        }
        return 1;
    }

    public class ViewHolder {
        TextView sendDateTextView;

        ImageView userAvatarImageView;
        TextView userNameTextView;

        TextView sendContentTextView;
        ImageView sendPhotoImageView;
        ImageView sendFaceImageView;

        ImageView sendFailImageView;
        ProgressBar isSendingProgressBar;

    }

    public boolean isSameDay(Date preDate, Date date) {
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(preDate);
        int preYear = calendar.get(Calendar.YEAR);
        int preDay = calendar.get(Calendar.DAY_OF_YEAR);

        calendar.setTime(date);
        int currentYear = calendar.get(Calendar.YEAR);
        int currentDay = calendar.get(Calendar.DAY_OF_YEAR);

        if (preYear == currentYear && preDay == currentDay) {
            return true;
        }

        return false;
    }

}
