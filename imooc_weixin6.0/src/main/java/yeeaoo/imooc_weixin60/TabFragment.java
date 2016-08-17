package yeeaoo.imooc_weixin60;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by yo on 2016/5/6.
 */
public class TabFragment extends Fragment {
    private String mTitle = "Default";
    public static String TITLE = "title";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getArguments() != null){
            mTitle = getArguments().getString(TITLE);
        }
        TextView tv = new TextView(getActivity());
        tv.setTextSize(20);
        tv.setGravity(Gravity.CENTER);
        tv.setBackgroundColor(Color.parseColor("#ffffffff"));
        tv.setText(mTitle);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TestActivity.class);
                startActivity(intent);
            }
        });

        return tv;
    }
}
