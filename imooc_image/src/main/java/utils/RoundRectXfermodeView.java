package utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import yeeaoo.imooc_image.R;

/**
 * Created by yo on 2016/4/28.
 */
public class RoundRectXfermodeView extends View {
    private Bitmap bitmap, outBitmap;// 原始的图片和需要输出的图片
    private Paint paint;
    public RoundRectXfermodeView(Context context) {
        super(context);
        initView();
    }

    public RoundRectXfermodeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public RoundRectXfermodeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initView(){
        setLayerType(LAYER_TYPE_SOFTWARE, null);// 禁用硬件加速
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.test3);
        outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(outBitmap);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // Dst
//        canvas.drawRoundRect(0, 0, bitmap.getWidth(), bitmap.getHeight(), 50, 50, paint); // API21
//        canvas.drawRoundRect(new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight()), 50, 50, paint);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
//        canvas.drawRoundRect(new RectF(0, 0, width, height), width/2, width/2, paint);
        canvas.drawCircle(width/2, height/2, Math.min(width/2, height/2), paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        // Src
        canvas.drawBitmap(bitmap, 0, 0, paint);
        paint.setXfermode(null);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(outBitmap, 0, 0, null);
    }
}
