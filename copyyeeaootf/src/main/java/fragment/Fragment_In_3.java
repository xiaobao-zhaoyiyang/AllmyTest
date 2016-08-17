package fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ToxicBakery.viewpager.transforms.CubeOutTransformer;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.copyyeeaootf.R;

import java.util.ArrayList;

import tool.LocalImageHolderView;

/**
 * Created by yo on 2016/7/29.
 */
public class Fragment_In_3 extends Fragment {
    private View view;
    private ConvenientBanner convenientBanner;
    private ArrayList<Integer> list = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null){
            view = inflater.inflate(R.layout.fragment_tab3, container, false);
            initView();
        }
        return view;
    }

    private void initView() {
        convenientBanner = (ConvenientBanner) view.findViewById(R.id.id_convenientBanner);
        list.add(R.drawable.ic_test_0);
        list.add(R.drawable.ic_test_1);
        list.add(R.drawable.ic_test_2);
        list.add(R.drawable.ic_test_3);
        list.add(R.drawable.ok);
        convenientBanner.setPages(new CBViewHolderCreator<LocalImageHolderView>() {
            @Override
            public LocalImageHolderView createHolder() {
                return new LocalImageHolderView();
            }
        }, list);
        try {
            convenientBanner.setPageTransformer(CubeOutTransformer.class.newInstance());
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
//        try {
//            String transformerName = "FlipVerticalTransformer";
//            Class cls = Class.forName("com.ToxicBakery.viewpager.transforms." + transformerName);
//            ABaseTransformer transformer= (ABaseTransformer) cls.newInstance();
//            convenientBanner.getViewPager().setPageTransformer(true,transformer);
//
//            //部分3D特效需要调整滑动速度
//            if(transformerName.equals("StackTransformer")){
//                convenientBanner.setScrollDuration(1200);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        convenientBanner.setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused});
        convenientBanner.startTurning(5000);
        convenientBanner.setScrollDuration(1500);

        convenientBanner.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Log.i("TAG", position + "");
            }
        });
    }
}
