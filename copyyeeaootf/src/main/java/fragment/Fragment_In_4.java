package fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.copyyeeaootf.R;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by yo on 2016/7/29.
 */
public class Fragment_In_4 extends Fragment {
    private View view;
    private CircleImageView circleImageView;
    private SimpleDraweeView simpleDraweeView;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null){
            view = inflater.inflate(R.layout.fragment_tab4, container, false);
            initView();
        }
        return view;
    }

    private void initView() {
        circleImageView = (CircleImageView) view.findViewById(R.id.id_circleImageView);
        circleImageView.setImageResource(R.drawable.ic_test_2);
        circleImageView.setBorderWidth(20);
        circleImageView.setBorderColor(Color.BLACK);
//        circleImageView.setBorderOverlay(false);

        simpleDraweeView = (SimpleDraweeView) view.findViewById(R.id.id_frescoImageView);
        String url = "http://img06.tooopen.com/images/20160723/tooopen_sy_171427629275.jpg";


        GenericDraweeHierarchy genericDraweeHierarchy = new GenericDraweeHierarchyBuilder(getResources())
                .build();
        simpleDraweeView.setHierarchy(genericDraweeHierarchy);

        simpleDraweeView.setImageURI(url);

        //创建DraweeController
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                //加载的图片URI地址
                .setUri(url)
                //设置点击重试是否开启
                .setTapToRetryEnabled(true)
                //设置旧的Controller
                .setOldController(simpleDraweeView.getController())
                //构建
                .build();

        //设置DraweeController
        simpleDraweeView.setController(controller);
    }
}
