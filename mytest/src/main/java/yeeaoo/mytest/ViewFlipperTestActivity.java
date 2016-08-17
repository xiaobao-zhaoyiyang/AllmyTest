package yeeaoo.mytest;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.ViewFlipper;

/**
 * Created by yo on 2016/6/29.
 */
public class ViewFlipperTestActivity extends AppCompatActivity implements GestureDetector.OnGestureListener{
    private ViewFlipper viewFlipper;
    private GestureDetector detector;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewflipper);
        viewFlipper = (ViewFlipper) findViewById(R.id.ViewFlipper1);
        detector = new GestureDetector(this, this);
        viewFlipper.addView(addView2ViewFlipper(1, "#eeeeee"));
        viewFlipper.addView(addView2ViewFlipper(2, "#f9aa00"));
        viewFlipper.addView(addView2ViewFlipper(3, "#ffeeef"));

        setAnimation2ViewFlipper();
        viewFlipper.setFlipInterval(2000);
        viewFlipper.startFlipping();
    }
    private void setAnimation2ViewFlipper(){
        this.viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this,
                R.anim.push_left_in));
        this.viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this,
                R.anim.push_left_out));
    }

    private View addView2ViewFlipper(int num, String color) {
        TextView tv = new TextView(this);
        tv.setText("这是第" + num + "页");
        tv.setTextColor(Color.parseColor(color));
        tv.setTextSize(35);
        tv.setGravity(Gravity.CENTER);
        return tv;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return this.detector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        viewFlipper.stopFlipping();
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (e1.getX() - e2.getX() > 100) {
            this.viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this,
                    R.anim.push_left_in));
            this.viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this,
                    R.anim.push_left_out));
            this.viewFlipper.showNext();
            setAnimation2ViewFlipper();
            viewFlipper.startFlipping();
            return true;
        } else if (e1.getX() - e2.getX() < -100) {
            this.viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this,
                    R.anim.push_right_in));
            this.viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this,
                    R.anim.push_right_out));
            this.viewFlipper.showPrevious();
            setAnimation2ViewFlipper();
            viewFlipper.startFlipping();
            return true;
        }
        return false;
    }
}
