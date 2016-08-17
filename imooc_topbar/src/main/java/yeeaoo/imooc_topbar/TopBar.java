package yeeaoo.imooc_topbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 自定义的一个标题栏
 */
public class TopBar extends RelativeLayout {
    private Button leftButton, rightButton;
    private TextView tvTitle;

    private int leftTextColor;
    private Drawable leftBackGround;
    private String leftText;

    private int rightTextColor;
    private Drawable rightBackGround;
    private String rightText;

    private float titleTextSize;
    private int titleTextColor;
    private String title;

    private LayoutParams leftParams, rightParams, titleParams;

    private topBarClickListener mListener;

    public interface topBarClickListener{
        void leftClick();
        void rightClick();
    }

    public void setOnTopBarClickListener(topBarClickListener listener){
        this.mListener = listener;
    }

    public TopBar(Context context) {
        this(context, null);
    }

    public TopBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TopBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TopBar);
        leftTextColor = a.getColor(R.styleable.TopBar_leftTextColor, 0);
        leftBackGround = a.getDrawable(R.styleable.TopBar_leftBackGround);
        leftText = a.getString(R.styleable.TopBar_leftText);

        rightTextColor = a.getColor(R.styleable.TopBar_rightTextColor, 0);
        rightBackGround = a.getDrawable(R.styleable.TopBar_rightBackGround);
        rightText = a.getString(R.styleable.TopBar_rightText);

        titleTextSize = a.getDimension(R.styleable.TopBar_topBarTitleTextSize, 0);
        titleTextColor = a.getColor(R.styleable.TopBar_topBarTitleTextColor, 0);
        title = a.getString(R.styleable.TopBar_topBarTitle);

        a.recycle();

        leftButton = new Button(context);
        rightButton = new Button(context);
        tvTitle = new TextView(context);

        leftButton.setTextColor(leftTextColor);
        leftButton.setBackground(leftBackGround);
        leftButton.setText(leftText);

        rightButton.setTextColor(rightTextColor);
        rightButton.setBackground(rightBackGround);
        rightButton.setText(rightText);

        tvTitle.setText(title);
        tvTitle.setTextColor(titleTextColor);
        tvTitle.setTextSize(titleTextSize);

        setBackgroundColor(0xFFF59563);

        leftParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        leftParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, TRUE);
        addView(leftButton, leftParams);

        rightParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        rightParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, TRUE);
        addView(rightButton, rightParams);

        titleParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        titleParams.addRule(RelativeLayout.CENTER_IN_PARENT, TRUE);
        addView(tvTitle, titleParams);

        leftButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.leftClick();
            }
        });

        rightButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.rightClick();
            }
        });
    }

    public void setLeftIsVisible(boolean flag){
        if (flag){
            leftButton.setVisibility(View.VISIBLE);
        }else
            leftButton.setVisibility(View.GONE);
    }

    public void setRightIsVisible(boolean flag){
        if (flag){
            rightButton.setVisibility(View.VISIBLE);
        }else
            rightButton.setVisibility(View.GONE);
    }
}
