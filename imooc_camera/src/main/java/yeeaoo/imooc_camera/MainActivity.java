package yeeaoo.imooc_camera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private static int RQD = 1;
    private String mFilePath;
    private ImageView mImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String path = Environment.getExternalStorageDirectory().getPath();
        mFilePath = path + "/" + "temp.png";
        mImageView = (ImageView) findViewById(R.id.id_imageview);
    }
    public void startCamera(View view){
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        Uri photoUri = Uri.fromFile(new File(mFilePath));
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
//        startActivityForResult(intent, RQD);
        Intent intent = new Intent(this, CustomCamera.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == RQD){
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(mFilePath);
                    BitmapFactory.Options opts=new BitmapFactory.Options();
                    opts.inTempStorage = new byte[100 * 1024];
                    opts.inPreferredConfig = Bitmap.Config.RGB_565;
                    opts.inPurgeable = true;
                    opts.inSampleSize = 4;
                    opts.inInputShareable = true;
                    Bitmap bitmap = BitmapFactory.decodeStream(fis, null, opts);
                    mImageView.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }finally {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
