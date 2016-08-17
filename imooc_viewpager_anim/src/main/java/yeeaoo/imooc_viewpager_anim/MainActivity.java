package yeeaoo.imooc_viewpager_anim;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
//    private ViewPagerCompat mViewPager;
    private ViewPagerWithTransformerAnim mViewPager;
    private int[] mImgIds = {R.mipmap.guide_image1, R.mipmap.guide_image2, R.mipmap.guide_image3};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main2);
        mViewPager = (ViewPagerWithTransformerAnim) findViewById(R.id.id_viewpager);
        // 为ViewPager添加动画效果 3.0以上有效
//        mViewPager.setPageTransformer(true, new DepthPageTransformer());
        mViewPager.setAdapter(new PagerAdapter() {
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ImageView imageView = new ImageView(MainActivity.this);
                imageView.setImageResource(mImgIds[position]);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                container.addView(imageView);
                mViewPager.setViewForPosition(imageView, position);
                return imageView;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
                mViewPager.removeViewFromPosition(position);
            }

            @Override
            public int getCount() {
                return mImgIds.length;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }
        });
    }
}
