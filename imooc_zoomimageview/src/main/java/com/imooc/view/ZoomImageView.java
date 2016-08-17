package com.imooc.view;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

/**
 * Created by yo on 2016/3/23.
 */
public class ZoomImageView extends ImageView implements
        ViewTreeObserver.OnGlobalLayoutListener,
        ScaleGestureDetector.OnScaleGestureListener,
        View.OnTouchListener{

    private boolean mOnce;
    /**
     * 初始化时缩放的值
     */
    private float mInitScale;

    /**
     * 双击放大到达的值
     */
    private float mMidScale;
    /**
     * 放大的最大值
     */
    private float mMaxScale;

    private Matrix mScaleMatrix;
    /**
     * 捕获用户多指触控时缩放比例
     */
    private ScaleGestureDetector mScaleGestureDetector;

    public ZoomImageView(Context context) {
        this(context, null);
    }

    public ZoomImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZoomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScaleMatrix = new Matrix();
        setScaleType(ScaleType.MATRIX);
        mScaleGestureDetector = new ScaleGestureDetector(context, this);
        setOnTouchListener(this);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getViewTreeObserver().removeGlobalOnLayoutListener(this);
    }

    /**
     * 获取ImageView加载完成的图片
     */
    @Override
    public void onGlobalLayout() {
        if (!mOnce) {
            // 得到控件的宽和高
            int width = getWidth();
            int height = getHeight();
            // 得到图片及宽和高
            Drawable d = getDrawable();
            if (d == null)
                return;
            int dw = d.getIntrinsicWidth();
            int dh = d.getIntrinsicHeight();
            float scale = 1.0f;
            /**
             * 如果图片的宽度大于控件的宽度，但是高度小于控件的高度，将其缩小
             */
            if (dw > width && dh < height){
                scale = width * 1.0f / dw;
            }
            /**
             * 如果图片的宽度小于控件的宽度，但是高度大于控件的高度，将其缩小
             */
            if (dh > height && dw < width){
                scale = height * 1.0f / dh;
            }
            /**
             * 如果图片的宽高都大于控件，或是都小于控件，取最小值缩放至适合图片显示
             */
            if ((dw> width && dh > height) || (dw < width && dh < height)){
                scale = Math.min(width * 1.0f / dw, height * 1.0f / dh);
            }
            /**
             * 得到初始化时缩放的比例
             */
            mInitScale = scale;
            mMaxScale = mInitScale * 4;
            mMidScale = mInitScale * 2;
            // 将图片移动至控件中心
            int dx = getWidth() / 2 - dw / 2;
            int dy = getHeight() / 2 - dh / 2;

            mScaleMatrix.postTranslate(dx, dy);
            mScaleMatrix.postScale(mInitScale, mInitScale, width / 2, height / 2);
            setImageMatrix(mScaleMatrix);
            mOnce = true;
        }
    }

    /**
     * 获取当前图片的缩放值
     * @return
     */
    public float getScale(){
        float[] values = new float[9];
        mScaleMatrix.getValues(values);
        return values[Matrix.MSCALE_X];
    }
    // 缩放区间initScale maxScale
    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        float scale = getScale();
        float scaleFactor = detector.getScaleFactor();
        if (getDrawable() == null)
            return true;
        // 缩放范围的控制
        if ((scale < mMaxScale && scaleFactor > 1.0f)
                || (scale > mInitScale && scaleFactor < 1.0f)){
            if (scale * scaleFactor < mInitScale){
                scaleFactor = mInitScale / scale;
            }
            if (scale * scaleFactor > mMaxScale){
                scaleFactor = mMaxScale / scale;
            }
            // 缩放
            mScaleMatrix.postScale(scaleFactor, scaleFactor,
                    detector.getFocusX(), detector.getFocusY());
            checkBorderAndCenterWhenScale();
            setImageMatrix(mScaleMatrix);
        }
        return true;
    }

    /**
     * 获得图片放大缩小以后的宽高以及个点坐标
     * @return
     */
    private RectF getMatrixRectF(){
        Matrix matrix = mScaleMatrix;
        RectF rectF = new RectF();
        Drawable d = getDrawable();
        if (d != null){
            rectF.set(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            matrix.mapRect(rectF);
        }
        return rectF;
    }
    /**
     * 在缩放的时候进行边界控制及位置的控制
     */
    private void checkBorderAndCenterWhenScale() {
        RectF rect = getMatrixRectF();
        float deltaX = 0;
        float deltaY = 0;
        int width = getWidth();
        int height = getHeight();
        //  缩放时进行边界检测防止白边的出现
        if (rect.width() >= width){
            if (rect.left > 0){
                deltaX = -rect.left;
            }
            if (rect.right < width){
                deltaX = width - rect.right;
            }
        }
        if (rect.height() >= height){
            if (rect.top > 0){
                deltaY = -rect.top;
            }
            if (rect.bottom < height){
                deltaY = height - rect.bottom;
            }
        }
        // 如果宽度或高度小于控件的宽高居中
        if (rect.width() < width){
            deltaX = width / 2f - rect.right + rect.width() / 2f;
        }
        if (rect.height() < height){
            deltaY = height / 2f - rect.bottom + rect.height() / 2f;
        }
        mScaleMatrix.postTranslate(deltaX, deltaY);
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        mScaleGestureDetector.onTouchEvent(event);
        return true;
    }
}
