package yeeaoo.imooc_viewpager_anim;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.nineoldandroids.view.ViewHelper;

import java.util.HashMap;
import java.util.Map;

import javax.security.auth.login.LoginException;

/**
 * Created by yo on 2016/4/15.
 */
public class ViewPagerWithTransformerAnim extends ViewPager {
    private View mLeft;
    private View mRight;

    private float mTrans;
    private float mScale;

    private static final float MIN_SCALE = 0.6f;

    private Map<Integer, View> mChildren = new HashMap<Integer, View>();

    public void setViewForPosition(View view, int position){
        mChildren.put(position, view);
    }

    public void removeViewFromPosition(Integer position){
        mChildren.remove(position);
    }

    public ViewPagerWithTransformerAnim(Context context) {
        super(context);
    }

    public ViewPagerWithTransformerAnim(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onPageScrolled(int position, float offset, int offsetPixels) {
//        Log.i("TAG", "position:" + position + ",offest:" + offset);
        mLeft = mChildren.get(position);
        mRight = mChildren.get(position + 1);
        animStack(mLeft, mRight, offset, offsetPixels);
        super.onPageScrolled(position, offset, offsetPixels);

    }

    private void animStack(View left, View right, float offset, int offsetPixels) {
        if (right != null){
            // 从0页到1页，offset：0~1
            mScale = (1 - MIN_SCALE) * offset +MIN_SCALE;
            // 从0到宽度
            mTrans = -getWidth() - getPageMargin() + offsetPixels;
            ViewHelper.setScaleX(right, mScale);
            ViewHelper.setScaleY(right, mScale);
            ViewHelper.setTranslationX(right, mTrans);
            if (left != null){
                left.bringToFront();// 是left始终在上面显示
            }
        }
    }
}
