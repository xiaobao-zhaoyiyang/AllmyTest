package yeeaoo.imooc_image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import utils.ImageHelper;

/**
 * Created by yo on 2016/4/27.
 */
public class PixelEffect extends AppCompatActivity {
    private ImageView imageView1, imageView2, imageView3, imageView4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pixeleffect);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.test2);
        imageView1 = (ImageView) findViewById(R.id.imageview1);
        imageView2 = (ImageView) findViewById(R.id.imageview2);
        imageView3 = (ImageView) findViewById(R.id.imageview3);
        imageView4 = (ImageView) findViewById(R.id.imageview4);

        imageView1.setImageBitmap(bitmap);
        imageView2.setImageBitmap(ImageHelper.handleImageNegative(bitmap));

        imageView3.setImageBitmap(ImageHelper.handleImageRelief(bitmap));
        imageView4.setImageBitmap(ImageHelper.handleImageOldPhoto(bitmap));
    }
}
