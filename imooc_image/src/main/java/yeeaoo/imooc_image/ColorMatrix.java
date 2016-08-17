package yeeaoo.imooc_image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;

/**
 * Created by yo on 2016/4/27.
 */
public class ColorMatrix extends AppCompatActivity {
    private ImageView imageView;
    private GridLayout mGroup;
    private Bitmap bitmap;
    private int mEtWidth, mEtHeight;
    private EditText[] mEts = new EditText[20];
    private float [] mColorMatrix = new float[20];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.color_matrix);
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.test1);
        imageView = (ImageView) findViewById(R.id.imageView);
        mGroup = (GridLayout) findViewById(R.id.group);
        imageView.setImageBitmap(bitmap);

        mGroup.post(new Runnable() {
            @Override
            public void run() { // 绘制后执行
                mEtWidth = mGroup.getWidth() / 5;
                mEtHeight = mGroup.getHeight() / 4;
                addEts();
                initMatrix();
            }
        });
    }

    /**
     * 获取用户输入的矩阵值
     */
    private void getMatrix(){
        for (int i = 0; i < 20; i++) {
            mColorMatrix[i] = Float.valueOf(mEts[i].getText().toString());
        }
    }

    /**
     * 为图片设置Matrix值
     */
    private void setImageMatrix(){
        Bitmap bmp = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        android.graphics.ColorMatrix colorMatrix = new android.graphics.ColorMatrix();
        colorMatrix.set(mColorMatrix);
        Canvas canvas = new Canvas(bmp);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);// 抗锯齿
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(bitmap, 0, 0, paint);
        imageView.setImageBitmap(bmp);
    }

    public void btnChange(View view){
        getMatrix();
        setImageMatrix();
    }

    public void btnReset(View view){
        initMatrix();
        getMatrix();
        setImageMatrix();
    }

    private void addEts(){
        for (int i = 0; i < 20; i++) {
            EditText et = new EditText(ColorMatrix.this);
            mEts[i] = et;
            et.setGravity(Gravity.CENTER);
            mGroup.addView(et, mEtWidth, mEtHeight);
        }
    }

    private void initMatrix(){
        for (int i = 0; i < 20; i++) {
            if (i % 6 == 0){
                mEts[i].setText(String.valueOf(1));
            }else{
                mEts[i].setText(String.valueOf(0));
            }
        }
    }
}
