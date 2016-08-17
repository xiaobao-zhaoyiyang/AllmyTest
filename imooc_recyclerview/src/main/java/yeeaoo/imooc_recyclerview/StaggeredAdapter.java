package yeeaoo.imooc_recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yo on 2016/5/4.
 */
public class StaggeredAdapter extends SimpleAdapter {
    private List<Integer> mHeight = new ArrayList<>();
    public StaggeredAdapter(Context context, List<String> datas){
        super(context, datas);
        mInflater = LayoutInflater.from(context);
        for (int i = 0; i < mDatas.size(); i++) {
            mHeight.add((int) (100 + Math.random() * 300));
        }
    }
    @Override // 绑定
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        layoutParams.height = mHeight.get(position);
        holder.itemView.setLayoutParams(layoutParams);
        holder.tv.setText(mDatas.get(position));
        setUpItemEvent(holder);
    }

}
