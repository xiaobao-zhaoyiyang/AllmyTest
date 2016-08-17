package yeeaoo.gellerydemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

/**
 * Created by yo on 2016/4/5.
 */
public class MyTextView extends View {
    private Paint paint;
    private int x = 0;
    private int y = 0;
    private int sWidth;
    private String content;
    private int textLength;

    public MyTextView(Context context) {
        this(context, null);
    }

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setColor(Color.parseColor("#f9aa00"));
//        paint.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f);
        paint.setTextSize(20f);
        sWidth = context.getResources().getDisplayMetrics().widthPixels;
//        x = sWidth;
        Log.i("width", sWidth + "");
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyTextView);
        content = typedArray.getString(R.styleable.MyTextView_Text);
        Log.i("content", content + "");
        typedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Rect rect = new Rect();
        textLength = (int) paint.measureText(content);
        paint.getTextBounds(content, 0, content.length(), rect);
//        canvas.drawText(content, 0, 100, paint);
        translationText();
        canvas.drawText(content, x, 100, paint);

    }

    private void translationText() {
//        if ( x == textLength){
//            x = 0;
//        }else {
            x -= 1;
//        }
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        postInvalidate();
    }
}
