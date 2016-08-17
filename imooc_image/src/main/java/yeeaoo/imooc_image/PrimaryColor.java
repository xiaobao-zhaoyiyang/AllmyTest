package yeeaoo.imooc_image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.SeekBar;

import utils.ImageHelper;

/**
 * Created by yo on 2016/4/27.
 */
public class PrimaryColor extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener{
    private ImageView mImageView;
    private SeekBar seekBarHue, seekBarSaturation, seekBarLum;
    private static int MAX_VALUE = 255; // 最大值
    private static int MID_VALUE = 127; // 中间值
    private float mHue, mSaturation, mLum;
    private Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.primarycolor);
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.test3);
        mImageView = (ImageView) findViewById(R.id.imageview);
        seekBarHue = (SeekBar) findViewById(R.id.seekbarHue);
        seekBarSaturation = (SeekBar) findViewById(R.id.seekbarSaturation);
        seekBarLum = (SeekBar) findViewById(R.id.seekbarLum);
        seekBarHue.setOnSeekBarChangeListener(this);
        seekBarSaturation.setOnSeekBarChangeListener(this);
        seekBarLum.setOnSeekBarChangeListener(this);
        seekBarHue.setMax(MAX_VALUE);
        seekBarSaturation.setMax(MAX_VALUE);
        seekBarLum.setMax(MAX_VALUE);
        seekBarHue.setProgress(MID_VALUE);
        seekBarSaturation.setProgress(MID_VALUE);
        seekBarLum.setProgress(MID_VALUE);
        mImageView.setImageBitmap(bitmap);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()){
            case R.id.seekbarHue:
                mHue = (progress - MID_VALUE) * 1.0f / MID_VALUE * 180;
                break;
            case R.id.seekbarSaturation:
                mSaturation = progress * 1.0f / MID_VALUE;
                break;
            case R.id.seekbarLum:
                mLum = progress * 1.0f / MID_VALUE;
                break;
        }
        Bitmap bt = ImageHelper.handleImageEffect(bitmap, mHue, mSaturation, mLum);
        mImageView.setImageBitmap(bt);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
