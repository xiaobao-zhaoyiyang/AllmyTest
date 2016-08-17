package com.imooc.recorder.view;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import yeeaoo.imooc_recorder.MainActivity;
import yeeaoo.imooc_recorder.R;

/**
 * Created by yo on 2016/3/17.
 */
public class RecorderAdapter extends ArrayAdapter<MainActivity.Recorder> {
    private List<MainActivity.Recorder> mDatas;
    private Context mContext;

    private int mMinItemWidth;
    private int mMaxItemWidth;
    public RecorderAdapter(Context context, List<MainActivity.Recorder> datas) {
        super(context, -1, datas);
        this.mContext = context;
        this.mDatas = datas;

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        mMaxItemWidth = (int) (outMetrics.widthPixels * 0.7f);
        mMinItemWidth = (int) (outMetrics.widthPixels * 0.15f);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            convertView = View.inflate(mContext, R.layout.item_recorder, null);
            holder = new ViewHolder();
            holder.seconds = (TextView) convertView.findViewById(R.id.id_recorder_time);
            holder.length = convertView.findViewById(R.id.id_recorder_length);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.seconds.setText(Math.round(getItem(position).getTime()) + "\"");
        ViewGroup.LayoutParams lp = holder.length.getLayoutParams();
        lp.width = (int)(mMinItemWidth + (mMaxItemWidth/60f * getItem(position).getTime()));
        return convertView;
    }
    private class ViewHolder{
        TextView seconds;
        View length;
    }
}
