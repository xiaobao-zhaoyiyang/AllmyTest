package yeeaoo.gellerydemo;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

/**
 * Created by yo on 2016/3/31.
 */
public class ImageAdapter extends BaseAdapter {
    private Context context;
    private int[] res ;

    public ImageAdapter(Context context, int[] res) {
        this.context = context;
        this.res = res;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public Object getItem(int position) {
        return res[position];
    }

    @Override
    public long getItemId(int position) {
        return position%res.length;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = new ImageView(context);
        imageView.setBackgroundResource(res[position%res.length]);
        imageView.setLayoutParams(new Gallery.LayoutParams(400, 300));
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return imageView;
    }
}
