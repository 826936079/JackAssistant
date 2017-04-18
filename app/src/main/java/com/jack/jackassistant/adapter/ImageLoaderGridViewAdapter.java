package com.jack.jackassistant.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jack.jackassistant.R;
import com.jack.jackassistant.util.Constants;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by xiaofeng on 2017/4/3.
 */

public class ImageLoaderGridViewAdapter extends BaseAdapter {

    private List<String> mGridViewListDate;
    private String mDirPath;
    private Context mContext;
    public static Set<String> sSelectedImages = new HashSet<String>();

    private OnImageItemClickListener mOnImageItemClickListener;

    public interface OnImageItemClickListener {
        void onImageItemClick(Set<String> selectedImages);
    }

    public ImageLoaderGridViewAdapter(List<String> gridViewListDate, String dirPath, Context context) {
        this.mGridViewListDate = gridViewListDate;
        this.mDirPath = dirPath;
        this.mContext = context;
        mOnImageItemClickListener = (OnImageItemClickListener) context;
    }

    @Override
    public int getCount() {
        return mGridViewListDate.size();
    }

    @Override
    public Object getItem(int position) {
        return mGridViewListDate.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.image_loader_gridview_item, parent, false);

            holder.imageLoaderItemImageView = (ImageView) convertView.findViewById(R.id.imageLoaderItemImageView);
            holder.imageLoaderItemSelectedImageView = (ImageView) convertView.findViewById(R.id.imageLoaderItemSelectedImageView);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //重置状态，避免显示错乱
        holder.imageLoaderItemImageView.setImageResource(R.drawable.pictures_no);
        holder.imageLoaderItemImageView.setColorFilter(null);
        holder.imageLoaderItemSelectedImageView.setImageResource(R.drawable.picture_unselected);


        final String path;
        if (mDirPath.equals(Constants.FILE_SEPARATOR)) {
            path = mGridViewListDate.get(position);
        } else {
            path = mDirPath + Constants.FILE_SEPARATOR + mGridViewListDate.get(position);
        }
        Glide.with(mContext)
                .load(path)
                .into(holder.imageLoaderItemImageView);

        //点击事件
        holder.imageLoaderItemImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sSelectedImages.contains(path)) {
                    sSelectedImages.remove(path);
                    holder.imageLoaderItemImageView.setColorFilter(null);
                    holder.imageLoaderItemSelectedImageView.setImageResource(R.drawable.picture_unselected);
                } else {
                    sSelectedImages.add(path);
                    holder.imageLoaderItemImageView.setColorFilter(R.color.image_loader_selected_bg);
                    holder.imageLoaderItemSelectedImageView.setImageResource(R.drawable.pictures_selected);
                }
                mOnImageItemClickListener.onImageItemClick(sSelectedImages);
            }
        });

        if (sSelectedImages.contains(path)) {
            holder.imageLoaderItemImageView.setColorFilter(R.color.image_loader_selected_bg);
            holder.imageLoaderItemSelectedImageView.setImageResource(R.drawable.pictures_selected);
        }


        return convertView;
    }

    private class ViewHolder {
        ImageView imageLoaderItemImageView;
        ImageView imageLoaderItemSelectedImageView;
    }

}
