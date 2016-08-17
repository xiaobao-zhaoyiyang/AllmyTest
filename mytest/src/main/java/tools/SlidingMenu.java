package tools;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.nineoldandroids.view.ViewHelper;

import yeeaoo.mytest.R;

public class SlidingMenu extends HorizontalScrollView {
	/** 屏幕宽度 */
	private int mScreenWidth;
	/** 右侧预留距离dp */
	private int mMenuRightPadding;
	/** 菜单的宽度 */
	private int mMenuWidth;
	/** 菜单的展开隐藏边界 */
	private int mSwtichMenuWidth;
	/** 在被判定为滚动之前用户手指可以移动的最大值。 */
	private int touchSlop;
	/** 标识 */
	private boolean down = false;
	/** 记录按下手指下去时候的X轴滑动起点坐标 */
	private int startScrollX;
	/** 记录按下手指下去时候的X轴坐标 */
	private int downX;
	/** 从屏幕边缘滑动[打开状态为右侧，隐藏状态为左侧] */
	private boolean dragFromEdge = false;
	/** 菜单状态[true打开状态，false隐藏状态] */
	private boolean isOpen = false;
	/** 滑动状态[true正在滑动，false完成滑动] */
	private boolean isAnim = false;
	/** 滑动开关[true开启，false禁止] */
	private boolean isAbleToSliding = true;
	/** 滑动动画时间 此时段内屏蔽SlidingMenu点击事件 */
	private static final int ANIM_TIME = 200;
	private int mHalfMenuWidth;

	private boolean once;

	private ViewGroup mMenu;
	private ViewGroup mContent;

	Handler handler = new Handler();

	public OnSlidingFinishedListener listener;

	public interface OnSlidingFinishedListener {
		void onFinished(boolean isOpen);
	}

	public void setOnSlidingFinishedListener(OnSlidingFinishedListener listener) {
		this.listener = listener;
	}

	public SlidingMenu(Context context, AttributeSet attrs) {
		this(context, attrs, 0);

	}

	public SlidingMenu(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mScreenWidth = context.getResources().getDisplayMetrics().widthPixels;
		touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.SlidingMenu, defStyle, 0);
		int n = a.getIndexCount();
		for (int i = 0; i < n; i++) {
			int attr = a.getIndex(i);
			switch (attr) {
				case R.styleable.SlidingMenu_rightPadding:
					// 默认50
					mMenuRightPadding = a.getDimensionPixelSize(attr,
							(int) TypedValue.applyDimension(
									TypedValue.COMPLEX_UNIT_DIP, 50f,
									getResources().getDisplayMetrics()));// 默认为10DP
					break;
			}
		}
		a.recycle();
	}

	public SlidingMenu(Context context) {
		this(context, null, 0);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		/**
		 * 显示的设置一个宽度
		 */
		if (!once) {
			LinearLayout wrapper = (LinearLayout) getChildAt(0);
			mMenu = (ViewGroup) wrapper.getChildAt(0);
			mContent = (ViewGroup) wrapper.getChildAt(1);

			mMenuWidth = mScreenWidth - mMenuRightPadding;
			mHalfMenuWidth = mMenuWidth / 2;
			mMenu.getLayoutParams().width = mMenuWidth;
			mContent.getLayoutParams().width = mScreenWidth;

		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if (changed) {
			// 将菜单隐藏
			this.scrollTo(mMenuWidth, 0);
			once = true;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (!isAnim && isAbleToSliding) {
			int action = ev.getAction();
			switch (action) {

				case MotionEvent.ACTION_DOWN:
					/**
					 * 记录手指按下时候的X轴坐标 , X轴滑动起点坐标
					 */
					downX = (int) ev.getRawX();
					startScrollX = getScrollX();
					down = true;

					if (isOpen && downX > mMenuWidth) {
						// LogUtils.d("打开状态从右侧边缘[向左滑]");
						dragFromEdge = true;
					} else if (!isOpen && downX < mScreenWidth / 5) {
						// LogUtils.d("隐藏状态从左侧边缘[向右滑]");
						dragFromEdge = true;
					} else {
						// LogUtils.d("从中间不触发滑动");
						dragFromEdge = false;
					}
					break;

				case MotionEvent.ACTION_MOVE:
					/**
					 * fix : 从屏幕左侧中间滑动不会触发 onTouchEvent ACTION_DOWN
					 * [原因被leftMenu事件截断]
					 */
					if (!down) {
						downX = (int) ev.getRawX();
						startScrollX = getScrollX();
						down = true;

						if (isOpen && downX > mMenuWidth) {
							// LogUtils.d("打开状态从右侧边缘[向左滑]");
							dragFromEdge = true;
						} else if (!isOpen && downX < mScreenWidth / 5) {
							// LogUtils.d("隐藏状态从左侧边缘[向右滑]");
							dragFromEdge = true;
						} else {
							// LogUtils.d("从中间不触发滑动");
							dragFromEdge = false;
						}
					}

					if (!dragFromEdge) {
						// LogUtils.d("从中间拖动不触发滑动");
						return true;
					}
					break;
				// Up时，进行判断，如果显示区域大于菜单宽度一半则完全显示，否则隐藏
				case MotionEvent.ACTION_UP:
					// int scrollX = getScrollX();
					// if (scrollX > mHalfMenuWidth)
					// {
					// this.smoothScrollTo(mMenuWidth, 0);
					// isOpen = false;
					// } else
					// {
					// this.smoothScrollTo(0, 0);
					// isOpen = true;
					// }
					// return true;
					/**
					 * fix : 轻触不会触发 onTouchEvent ACTION_MOVE <br>
					 * [原因被轻触屏幕不会触发 ACTION_MOVE]
					 */
					if (!down) {
						downX = (int) ev.getRawX();
						startScrollX = getScrollX();
						down = true;
					}

					int moveScrollX = startScrollX - getScrollX();

					int upX = (int) ev.getRawX();
					if (isOpen && downX < mMenuWidth
							&& Math.abs(downX - upX) < touchSlop) {
//						LogUtils.d("直接点击左侧内容页");
					} else if (isOpen && downX >= mMenuWidth
							&& Math.abs(downX - upX) < touchSlop) {
//						LogUtils.d("直接点击右侧内容页"); // 需要放在[想要隐藏 但是移动距离不够]之前判断
						closeMenu();
					}
					else if (!isOpen && moveScrollX >= mSwtichMenuWidth) {
//						LogUtils.d("打开 ");
						openMenu();
					} else if (!isOpen && moveScrollX < mSwtichMenuWidth) {
//						LogUtils.d("想要打开 但是移动距离不够 ");
						closeMenu();
					} else if (!isOpen && moveScrollX < 0) {
//						LogUtils.d("隐藏状态，手指向左滑");
						closeMenu();
					} else if (isOpen && -moveScrollX >= mSwtichMenuWidth) {
//						LogUtils.d("隐藏 ");
						closeMenu();
					} else if (isOpen && -moveScrollX < mSwtichMenuWidth) {
//						LogUtils.d("想要隐藏 但是移动距离不够");
						openMenu();
					} else if (isOpen && moveScrollX > 0) {
//						LogUtils.d("打开状态 手指向右滑");
						openMenu();
					}
					else {
//						LogUtils.d("其他情况 还原"); // 一般不会到这里
						if (isOpen) {
							openMenu();
						} else {
							closeMenu();
						}
					}

					/**
					 * fix : 重置按下事件标识
					 */
					down = false;

					return true;
			}
			return super.onTouchEvent(ev);
		} else {
			// 动画执行中
			return true;
		}
	}

	/**
	 * 打开菜单
	 */
	public void openMenu() {
		this.smoothScrollTo(0, 0);
		isOpen = true;
		if (listener != null) {
			listener.onFinished(isOpen);
		}
		letAnimPlay();
	}

	/**
	 * 关闭菜单
	 */
	public void closeMenu() {
//		if (isOpen) {
//			this.smoothScrollTo(mMenuWidth, 0);
//			isOpen = false;
//		}
		this.smoothScrollTo(mMenuWidth, 0);
		isOpen = false;
		if (listener != null) {
			listener.onFinished(isOpen);
		}
		letAnimPlay();
	}

	/**
	 * 切换菜单状态
	 */
	public void toggle() {
		if (isOpen) {
			closeMenu();
		} else {
			openMenu();
		}
	}

	public boolean isOpenMenu(){
		return isOpen;
	}
	/**
	 * 设置是否开启滑动功能
	 */
	public void setAbleToSliding(boolean able) {
		isAbleToSliding = able;
	}

	private void letAnimPlay() {
		isAnim = true;
		handler.postDelayed(overAnimPlay, ANIM_TIME);
	}

	private Runnable overAnimPlay = new Runnable() {
		@Override
		public void run() {
			isAnim = false;
		}
	};

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		float scale = l * 1.0f / mMenuWidth;
		float leftScale = 1 - 0.3f * scale;
		float rightScale = 0.8f + scale * 0.2f;

		ViewHelper.setScaleX(mMenu, leftScale);
		ViewHelper.setScaleY(mMenu, leftScale);
		ViewHelper.setAlpha(mMenu, 0.6f + 0.4f * (1 - scale));
		ViewHelper.setTranslationX(mMenu, mMenuWidth * scale * 0.7f);

		ViewHelper.setPivotX(mContent, 0);
		ViewHelper.setPivotY(mContent, mContent.getHeight() / 2);
		ViewHelper.setScaleX(mContent, rightScale);
		ViewHelper.setScaleY(mContent, rightScale);

	}

}

