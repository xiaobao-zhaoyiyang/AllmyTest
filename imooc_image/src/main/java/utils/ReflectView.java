package utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import yeeaoo.imooc_image.R;

/**
 * Created by yo on 2016/4/28.
 */
public class ReflectView extends View {
    private Bitmap bitmap;
    private Bitmap reflectBitmap;
    private Paint paint;
    public ReflectView(Context context) {
        super(context);
        initView();
    }

    public ReflectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ReflectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView(){
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.test3);
        Matrix matrix = new Matrix();
        matrix.setScale(1, -1);
        reflectBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        paint = new Paint();
        paint.setShader(new LinearGradient(0, bitmap.getHeight(),
                0, bitmap.getHeight() * 1.4f,
                0xDD000000, 0x10000000,
                Shader.TileMode.CLAMP));
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.BLACK);
        canvas.drawBitmap(bitmap, 0, 0, null);
        canvas.drawBitmap(reflectBitmap, 0, bitmap.getHeight(), null);
        canvas.drawRect(0, reflectBitmap.getHeight(),
                reflectBitmap.getWidth(), reflectBitmap.getWidth()*2,
                paint);
    }
}
