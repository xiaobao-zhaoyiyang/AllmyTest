package yeeaoo.imooc_viewpager_anim;

import android.support.v4.view.ViewPager;
import android.view.View;

import com.nineoldandroids.view.ViewHelper;

/**
 * Created by yo on 2016/4/15.
 */
public class RotateDownPageTransformer implements ViewPager.PageTransformer {

    private static final float MAX_ROTATE = 20f;
    private float mRot;
    // A页角度变化0~-20， B页角度变化20~0
    @Override
    public void transformPage(View view, float position) {
        int pageWidth = view.getWidth();
        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            ViewHelper.setRotation(view, 0);
            /**
             * A页切换到B页的position 0.0 ~ -1，B页的position 1 ~ 0.0
             */
        } else if (position <= 0) { // [-1,0]  A页
            // Use the default slide transition when moving to the left page
            mRot = position * MAX_ROTATE;
            ViewHelper.setPivotX(view, pageWidth/2);
            ViewHelper.setPivotY(view, view.getMeasuredHeight());

            ViewHelper.setRotation(view, mRot);

        } else if (position <= 1) { // (0,1]
            // Fade the page out.
            mRot = position * MAX_ROTATE;
            ViewHelper.setPivotX(view, pageWidth/2);
            ViewHelper.setPivotY(view, view.getMeasuredHeight());

            ViewHelper.setRotation(view, mRot);

        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            ViewHelper.setRotation(view, 0);
        }

    }
}
