package yeeaoo.viewflipper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.ViewFlipper;

public class MainActivity extends AppCompatActivity {
    private ViewFlipper viewFlipper;
    private int[] resId = {R.mipmap.pic1, R.mipmap.pic2, R.mipmap.pic3, R.mipmap.pic4};
    private float startX;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewFlipper = (ViewFlipper) findViewById(R.id.viewfilpper);
        for (int i = 0; i < resId.length; i++) {
            viewFlipper.addView(getImage(resId[i]));
        }
        viewFlipper.setInAnimation(this, R.anim.left_in);
        viewFlipper.setOutAnimation(this, R.anim.left_out);
        viewFlipper.setFlipInterval(3000);
        viewFlipper.startFlipping();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            // 手指落下
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                viewFlipper.stopFlipping();
                break;
            // 手指滑动
            case MotionEvent.ACTION_MOVE:
                break;
            // 手指离开
            case MotionEvent.ACTION_UP:
                // 向右滑
                if (event.getX()-startX >100){
                    viewFlipper.setInAnimation(this, R.anim.left_in);
                    viewFlipper.setOutAnimation(this, R.anim.left_out);
                    viewFlipper.showPrevious(); // 前一页
                }
                if (startX-event.getX() >100){// 向左滑
                    viewFlipper.setInAnimation(this, R.anim.right_in);
                    viewFlipper.setOutAnimation(this, R.anim.right_out);
                    viewFlipper.showNext(); // 后一页
                }
                viewFlipper.setInAnimation(this, R.anim.left_in);
                viewFlipper.setOutAnimation(this, R.anim.left_out);
                viewFlipper.startFlipping();
                break;
        }
        return super.onTouchEvent(event);
    }

    private ImageView getImage(int resId){
        ImageView image = new ImageView(this);
        image.setBackgroundResource(resId);
        return  image;
    }
}
