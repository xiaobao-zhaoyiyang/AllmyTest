package yeeaoo.imooc_tab03;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by yo on 2016/4/19.
 */
public class TabFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag, container, false);
        TextView tv = (TextView) view.findViewById(R.id.iv_tv);
        int pos = getArguments().getInt("position");
        tv.setText(TabAdapter.TITLES[pos]);
        return view;
    }
}
