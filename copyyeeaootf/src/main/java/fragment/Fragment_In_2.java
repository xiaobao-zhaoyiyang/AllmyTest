package fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.copyyeeaootf.R;

/**
 * Created by yo on 2016/7/29.
 */
public class Fragment_In_2 extends Fragment {
    private View view;
    private TextView mTv;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null){
            view = inflater.inflate(R.layout.fragment_tab2, container, false);
            initView(view);
        }
        return view;
    }

    private void initView(View view) {
        mTv = (TextView) view.findViewById(R.id.id_2_textView);
        // 使用占位符实现字符串的拼接
        String str = getActivity().getString(R.string.textTest);
        mTv.setText(String.format(str, 2000, 12.0, "string"));
    }
}
