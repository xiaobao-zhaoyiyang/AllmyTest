package com.imooc_shark;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private ShakeSensor mShakeSensor;
    private ImageView mHand;
    private TextView mCount;
    private static int count = 3;
    private boolean isStart;

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0){
                Toast.makeText(MainActivity.this, "没有次数啦", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        mShakeSensor = new ShakeSensor(this);
        mHand = (ImageView) findViewById(R.id.img_handle);
        mCount = (TextView) findViewById(R.id.txt_show_count);
        mShakeSensor.setonShakeListener(new ShakeSensor.onShakeListener() {
            @Override
            public void onShake() {
                if (count == 0){
                    mHandler.sendEmptyMessage(0);
                    mShakeSensor.unRegisterListener();
                    return;
                }
                Toast t = Toast.makeText(MainActivity.this, "摇一摇成功", Toast.LENGTH_SHORT);
                t.setGravity(Gravity.CENTER, 0, 0);
                t.show();

                ShakeSensor.STOP = true;
                Intent intent = new Intent(MainActivity.this, ShowActivity.class);
                overridePendingTransition(R.anim.set_in, 0);
                startActivity(intent);

            }
        });
        mShakeSensor.init();

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_handle);
        animation.setRepeatMode(Animation.REVERSE);
        mHand.startAnimation(animation);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (count == 0){
            count = 0;
            mHand.clearAnimation();
        }else {
            if (isStart)
                count--;
        }
        isStart = true;
        mCount.setText("今天还剩" + count + "次");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mShakeSensor.unRegisterListener();
    }
}
