package com.imooc_shark;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by yo on 2016/6/24.
 */
public class ShowActivity extends AppCompatActivity {
    private ImageView imageView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        imageView = (ImageView) findViewById(R.id.img_show);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShakeSensor.STOP = false;
                overridePendingTransition(0, R.anim.set_out);
                finish();
            }
        });
    }
}
