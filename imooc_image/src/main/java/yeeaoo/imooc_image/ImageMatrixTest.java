package yeeaoo.imooc_image;

import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.GridLayout;

import utils.ImageMatrixView;

/**
 * Created by yo on 2016/4/28.
 */
public class ImageMatrixTest extends AppCompatActivity{
    private GridLayout gridLayout;
    private ImageMatrixView imageMatrixView;
    private int etWidth, etHeight;
    private float[] imageMatrix = new float[9];
    private EditText[] ets = new EditText[9];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.matrix);

        gridLayout = (GridLayout) findViewById(R.id.id_gridLayout);
        imageMatrixView = (ImageMatrixView) findViewById(R.id.id_matrixView);

        gridLayout.post(new Runnable() {
            @Override
            public void run() {
                etWidth = gridLayout.getWidth() / 3;
                etHeight = gridLayout.getHeight() / 3;
                addEts();
                initImageMatrix();
            }
        });
    }

    private void addEts() {
        for (int i = 0; i < 9; i++) {
            EditText et = new EditText(ImageMatrixTest.this);
            et.setGravity(Gravity.CENTER);
            gridLayout.addView(et, etWidth, etHeight);
            ets[i] = et;
        }
    }
    private void initImageMatrix(){
        for (int i = 0; i < 9; i++) {
            if (i % 4 == 0){
                ets[i].setText(String.valueOf(1));
            }else{
                ets[i].setText(String.valueOf(0));
            }
        }
    }
    public void change(View view){
        getImageMatrix();
        Matrix matrix = new Matrix();
        matrix.setValues(imageMatrix);
        imageMatrixView.setImageMatrix(matrix);
        imageMatrixView.invalidate();
    }
    private void getImageMatrix(){
        for (int i = 0; i < 9; i++) {
            imageMatrix[i] = Float.parseFloat(ets[i].getText().toString());
        }
    }
    public void reset(View view){
        initImageMatrix();
        getImageMatrix();
        Matrix matrix = new Matrix();
//        matrix.setValues(imageMatrix);
        // Rotate 旋转  Translate 平移 Scale 缩放 Skew 错切  post 矩阵组合
        matrix.setTranslate(150, 150);// 平移
        matrix.setScale(2, 2); // 缩放  matrix.postScale(2, 2)顺序执行
        imageMatrixView.setImageMatrix(matrix);
        imageMatrixView.invalidate();
    }
}
