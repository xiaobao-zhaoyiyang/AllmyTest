package yeeaoo.imooc_camera;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by yo on 2016/3/23.
 */
public class ResultActivty extends AppCompatActivity{
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        imageView = (ImageView) findViewById(R.id.result_iamgeview);
        String path = getIntent().getStringExtra("picPath");
        Log.i("path", "onCreate: "+path);
        try {
            FileInputStream fis = new FileInputStream(path);
            BitmapFactory.Options opts=new BitmapFactory.Options();
            opts.inTempStorage = new byte[100 * 1024];
            opts.inPreferredConfig = Bitmap.Config.RGB_565;
            opts.inPurgeable = true;
            opts.inSampleSize = 4;
            opts.inInputShareable = true;
            Bitmap bitmap = BitmapFactory.decodeStream(fis,null, opts);
            Matrix matrix = new Matrix();
            matrix.setRotate(90);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0,
                    bitmap.getWidth(), bitmap.getHeight(), matrix, false);
            imageView.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
//        Bitmap bitmap = BitmapFactory.decodeFile(path);
//        imageView.setImageBitmap(bitmap);
    }
}
