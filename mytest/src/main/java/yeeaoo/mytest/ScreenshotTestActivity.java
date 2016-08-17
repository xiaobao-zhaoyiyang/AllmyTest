package yeeaoo.mytest;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import tools.ScreenUtils;

/**
 * Created by yo on 2016/7/28.
 */
public class ScreenshotTestActivity extends AppCompatActivity implements View.OnClickListener {
    private Button button_withBar;
    private Button button_noBar;
    private ImageView imageView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screenshot);
        initView();
    }

    private void initView() {
        button_withBar = (Button) findViewById(R.id.btn_withBar);
        button_noBar = (Button) findViewById(R.id.btn_noBar);
        button_withBar.setOnClickListener(this);
        button_noBar.setOnClickListener(this);
        imageView = (ImageView) findViewById(R.id.imageView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_withBar:
                Bitmap bitmap = ScreenUtils.snapShotWithStatusBar(this);
                imageView.setImageBitmap(bitmap);
                break;
            case R.id.btn_noBar:
                Bitmap bitmap1 = ScreenUtils.snapShotWithoutStatusBar(this);
                imageView.setImageBitmap(bitmap1);
                break;
        }
    }
}
