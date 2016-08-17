package com.imooc_imageloader;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import util.ImageLoader;

public class ImageAdapter extends BaseAdapter {

    private static Set<String> mSelectedImg = new HashSet<>();
    private String mDirPath;
    private List<String> mImgPaths;
    private LayoutInflater mInflater;

    public ImageAdapter(Context context, List<String> mDatas, String path) {
        this.mDirPath = path;
        this.mImgPaths = mDatas;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mImgPaths.size();
    }

    @Override
    public Object getItem(int position) {
        return mImgPaths.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_gridview, parent, false);
            holder = new ViewHolder();
            holder.mImg = (ImageView) convertView.findViewById(R.id.id_item_image);
            holder.mSelect = (ImageButton) convertView.findViewById(R.id.id_item_select);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        // 重置状态
        holder.mImg.setImageResource(R.mipmap.picture_no);
        holder.mSelect.setImageResource(R.mipmap.picture_unselected);
        holder.mImg.setColorFilter(null);

        ImageLoader.getInstance(3, ImageLoader.Type.LIFO)
                .loadImage(mDirPath + "/"
                                + mImgPaths.get(position),
                        holder.mImg);

        final String filePath = mDirPath + "/" + mImgPaths.get(position);
        holder.mImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Adapter", ".....");
                // 已经被选择
                if (mSelectedImg.contains(filePath)) {
                    mSelectedImg.remove(filePath);
                    holder.mImg.setColorFilter(null);
                    holder.mSelect.setImageResource(R.mipmap.picture_unselected);
                } else { // 未被选择
                    mSelectedImg.add(filePath);
                    holder.mImg.setColorFilter(Color.parseColor("#77000000"));
                    holder.mSelect.setImageResource(R.mipmap.picture_selected);
                }
//                notifyDataSetChanged();
            }
        });
        if (mSelectedImg.contains(filePath)){
            holder.mImg.setColorFilter(Color.parseColor("#77000000"));
            holder.mSelect.setImageResource(R.mipmap.picture_selected);
        }
        return convertView;
    }

    private class ViewHolder {
        ImageView mImg;
        ImageButton mSelect;
    }
}