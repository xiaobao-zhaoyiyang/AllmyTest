package fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.copyyeeaootf.R;

import tool.BlurBitmap;

/**
 * Created by yo on 2016/7/29.
 */
public class Fragment_In_1 extends Fragment {
    private View view;
    private ImageView mOriginImage;// 原始图片
    private ImageView mFinalImage; // 模糊后的图片
    private SeekBar mSeekBar;
    private float mAlpha;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null){
            view = inflater.inflate(R.layout.fragment_tab1, container, false);
            initView(view);
        }
//        ViewGroup parent = (ViewGroup) view.getParent();
//        if (parent != null){
//            parent.removeView(view);
//        }
        return view;
    }

    private void initView(View view) {
        Log.d("TAG", "..................initView");
        mOriginImage = (ImageView) view.findViewById(R.id.id_origin_image);
        mFinalImage = (ImageView) view.findViewById(R.id.id_final_image);
        Bitmap origin = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.ok);
        Bitmap blurBitmap = BlurBitmap.blur(getActivity(), origin);
        mOriginImage.setImageBitmap(origin);
        mFinalImage.setImageBitmap(blurBitmap);

        mSeekBar = (SeekBar) view.findViewById(R.id.id_seekBar);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mAlpha = (int) (progress * 2.55);
                mOriginImage.setAlpha((255 - mAlpha) / 255);
//                mOriginImage.setImageAlpha(255 - mAlpha);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
