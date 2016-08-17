package yeeaoo.mytest;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.CommonPagerTitleView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yo on 2016/7/5.
 */
public class IndicatorTestActivity extends AppCompatActivity {
    private MagicIndicator magicIndicator;
    private CommonNavigator commonNavigator;
    private ViewPager viewPager;
    private List<String> mTitle = new ArrayList<>();
    private PagerAdapter mAdapter = new PagerAdapter() {

        @Override
        public int getCount() {
            return mTitle.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TextView textView = new TextView(container.getContext());
            textView.setText(mTitle.get(position));
            textView.setGravity(Gravity.CENTER);
            container.addView(textView);
            return textView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indicatortest);
        initView();

        viewPager.setAdapter(mAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                magicIndicator.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                magicIndicator.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                magicIndicator.onPageScrollStateChanged(state);
            }
        });
    }

    private void initView() {
        magicIndicator = (MagicIndicator) findViewById(R.id.magic_indicator);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        commonNavigator = new CommonNavigator(this);
        mTitle.add("科技");
        mTitle.add("汽车");
        mTitle.add("互联网");
        mTitle.add("搞笑");
        mTitle.add("奇闻异事");
        mTitle.add("教育");
        mTitle.add("育儿");
        mTitle.add("时尚");
        mTitle.add("星座");
        mTitle.add("购物");

        commonNavigator.setFollowTouch(false);
 /*       // 今日头条式
        commonNavigator.setRightPadding(UIUtil.dip2px(this, 50));
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mTitle == null ? 0 : mTitle.size();
            }

            @Override
            public IPagerTitleView getItemView(Context context, final int index) {
                ClipPagerTitleView clipPagerTitleView = new ClipPagerTitleView(context);

                clipPagerTitleView.setText(mTitle.get(index));
                clipPagerTitleView.setTextColor(Color.parseColor("#f2c4c4"));
                clipPagerTitleView.setClipColor(Color.WHITE);

                clipPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager.setCurrentItem(index);
                    }
                });

                return clipPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                return null;
            }
        });*/


        magicIndicator.setBackgroundColor(Color.WHITE);
/*
        // 当前页不定位到中间
        commonNavigator.setFollowTouch(false);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mTitle == null ? 0 : mTitle.size();
            }

            @Override
            public IPagerTitleView getItemView(Context context, final int index) {
                ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);
                colorTransitionPagerTitleView.setText(mTitle.get(index));
                colorTransitionPagerTitleView.setNormalColor(Color.GRAY);
                colorTransitionPagerTitleView.setSelectedColor(Color.BLACK);
                colorTransitionPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager.setCurrentItem(index);
                    }
                });
                return colorTransitionPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setWrapContentMode(true);
                List<String> colorList = new ArrayList<String>();
                colorList.add("#ff4a42");
                colorList.add("#fcde64");
                colorList.add("#73e8f4");
                colorList.add("#76b0ff");
                colorList.add("#c683fe");
                indicator.setColorList(colorList);
                return indicator;
            }
        });
*/

/*        // 当前页始终定位到中间
        commonNavigator.setFollowTouch(false);
        commonNavigator.setEnablePivotScroll(true);
        commonNavigator.setScrollPivotX(0.15f);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mTitle == null ? 0 : mTitle.size();
            }

            @Override
            public IPagerTitleView getItemView(Context context, final int index) {
                ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);
                colorTransitionPagerTitleView.setText(mTitle.get(index));
                colorTransitionPagerTitleView.setNormalColor(Color.GRAY);
                colorTransitionPagerTitleView.setSelectedColor(Color.BLACK);
                colorTransitionPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager.setCurrentItem(index);
                    }
                });
                return colorTransitionPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                List<String> colorList = new ArrayList<String>();
                colorList.add("#ff4a42");
                indicator.setColorList(colorList);
                return indicator;
            }
        });*/

       /* // 自适应模式
        commonNavigator.setAdjustMode(true);  // 自适应模式
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mTitle == null ? 0 : 2;
            }

            @Override
            public IPagerTitleView getItemView(Context context, final int index) {
                ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);
                colorTransitionPagerTitleView.setText(mTitle.get(index));
                colorTransitionPagerTitleView.setNormalColor(Color.GRAY);
                colorTransitionPagerTitleView.setSelectedColor(Color.BLACK);
                colorTransitionPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager.setCurrentItem(index);
                    }
                });
                return colorTransitionPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setLineHeight(UIUtil.dip2px(context, 6));
                indicator.setRoundRadius(UIUtil.dip2px(context, 3));
                List<String> colorList = new ArrayList<String>();
                colorList.add("#fcde64");
                indicator.setColorList(colorList);
                return indicator;
            }
        });*/

/*        // 自适应模式，带插值器
        commonNavigator.setAdjustMode(true);  // 自适应模式
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mTitle == null ? 0 : 2;
            }

            @Override
            public IPagerTitleView getItemView(Context context, final int index) {
                ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);
                colorTransitionPagerTitleView.setText(mTitle.get(index));
                colorTransitionPagerTitleView.setNormalColor(Color.GRAY);
                colorTransitionPagerTitleView.setSelectedColor(Color.BLACK);
                colorTransitionPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager.setCurrentItem(index);
                    }
                });
                return colorTransitionPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setStartInterpolator(new AccelerateInterpolator());
                indicator.setEndInterpolator(new DecelerateInterpolator(1.6f));
                indicator.setLineHeight(UIUtil.dip2px(context, 1));
                List<String> colorList = new ArrayList<String>();
                colorList.add("#76b0ff");
                indicator.setColorList(colorList);
                return indicator;
            }
        });*/

/*
        // 缩放 + 颜色渐变
        commonNavigator.setEnablePivotScroll(true);
        commonNavigator.setScrollPivotX(0.8f);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mTitle == null ? 0 : mTitle.size();
            }

            @Override
            public IPagerTitleView getItemView(Context context, final int index) {
                ScaleTransitionPagerTitleView colorTransitionPagerTitleView = new ScaleTransitionPagerTitleView(context);
                colorTransitionPagerTitleView.setText(mTitle.get(index));
                colorTransitionPagerTitleView.setTextSize(18);
                colorTransitionPagerTitleView.setNormalColor(Color.GRAY);
                colorTransitionPagerTitleView.setSelectedColor(Color.BLACK);
                colorTransitionPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager.setCurrentItem(index);
                    }
                });
                return colorTransitionPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setStartInterpolator(new AccelerateInterpolator());
                indicator.setEndInterpolator(new DecelerateInterpolator(1.6f));
                indicator.setLineHeight(UIUtil.dip2px(context, 1));
                List<String> colorList = new ArrayList<String>();
                colorList.add("#c683fe");
                indicator.setColorList(colorList);
                return indicator;
            }
        });
*/

/*        // 只有指示器，没有title
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mTitle == null ? 0 : 5;
            }

            @Override
            public IPagerTitleView getItemView(Context context, final int index) {
                return new DummyPagerTitleView(context);
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setLineHeight(UIUtil.dip2px(context, 5));
                List<String> colorList = new ArrayList<String>();
                colorList.add("#76b0ff");
                indicator.setColorList(colorList);
                return indicator;
            }
        });*/

/*        // 带吸附效果
        commonNavigator.setEnablePivotScroll(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mTitle == null ? 0 : mTitle.size();
            }

            @Override
            public IPagerTitleView getItemView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new SimplePagerTitleView(context);
                simplePagerTitleView.setText(mTitle.get(index));
                simplePagerTitleView.setNormalColor(Color.GRAY);
                simplePagerTitleView.setSelectedColor(Color.BLACK);
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                AbsorbPagerIndicator indicator = new AbsorbPagerIndicator(context);
                indicator.setLineHeight(UIUtil.dip2px(context, 6));
                List<String> colorList = new ArrayList<String>();
                colorList.add("#ff4a42");
                colorList.add("#fcde64");
                colorList.add("#73e8f4");
                colorList.add("#76b0ff");
                colorList.add("#c683fe");
                indicator.setColorList(colorList);
                return indicator;
            }
        });*/

/*        // 贝塞尔曲线
        commonNavigator.setEnablePivotScroll(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mTitle == null ? 0 : mTitle.size();
            }

            @Override
            public IPagerTitleView getItemView(Context context, final int index) {
                ScaleTransitionPagerTitleView colorTransitionPagerTitleView = new ScaleTransitionPagerTitleView(context);
                colorTransitionPagerTitleView.setText(mTitle.get(index));
                colorTransitionPagerTitleView.setTextSize(18);
                colorTransitionPagerTitleView.setNormalColor(Color.GRAY);
                colorTransitionPagerTitleView.setSelectedColor(Color.BLACK);
                colorTransitionPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager.setCurrentItem(index);
                    }
                });
                return colorTransitionPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                BezierPagerIndicator indicator = new BezierPagerIndicator(context);
                List<String> colorList = new ArrayList<String>();
                colorList.add("#ff4a42");
                colorList.add("#fcde64");
                colorList.add("#73e8f4");
                colorList.add("#76b0ff");
                colorList.add("#c683fe");
                indicator.setColorList(colorList);
                return indicator;
            }
        });*/

       /* // 天天快报式
        commonNavigator.setEnablePivotScroll(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mTitle == null ? 0 : mTitle.size();
            }

            @Override
            public IPagerTitleView getItemView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new SimplePagerTitleView(context);
                simplePagerTitleView.setText(mTitle.get(index));
                simplePagerTitleView.setNormalColor(Color.parseColor("#333333"));
                simplePagerTitleView.setSelectedColor(Color.parseColor("#e94220"));
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                WrapPagerIndicator indicator = new WrapPagerIndicator(context);
                indicator.setFillColor(Color.parseColor("#ebe4e3"));
                return indicator;
            }
        });
*/

/*        // 小尖角式
        commonNavigator.setScrollPivotX(0.15f);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mTitle == null ? 0 : mTitle.size();
            }

            @Override
            public IPagerTitleView getItemView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new SimplePagerTitleView(context);
                simplePagerTitleView.setText(mTitle.get(index));
                simplePagerTitleView.setNormalColor(Color.parseColor("#333333"));
                simplePagerTitleView.setSelectedColor(Color.parseColor("#e94220"));
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                TriangularPagerIndicator indicator = new TriangularPagerIndicator(context);
                indicator.setLineColor(Color.parseColor("#e94220"));
                return indicator;
            }
        });*/

//        magicIndicator.setNavigator(commonNavigator);

      /*  // 圆圈式
        final CircleNavigator circleNavigator = new CircleNavigator(this);
        circleNavigator.setCircleCount(mTitle.size());
        circleNavigator.setCircleColor(Color.RED);
        circleNavigator.setCircleClickListener(new CircleNavigator.OnCircleClickListener() {
            @Override
            public void onClick(int index) {
                viewPager.setCurrentItem(index);
            }
        });
        magicIndicator.setNavigator(circleNavigator);*/

        // 通用式
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return mTitle == null ? 0 : mTitle.size();
            }

            @Override
            public IPagerTitleView getItemView(Context context, final int index) {
                CommonPagerTitleView commonPagerTitleView = new CommonPagerTitleView(IndicatorTestActivity.this);
                commonPagerTitleView.setContentView(R.layout.simple_pager_title_layout);

                // 初始化
                final ImageView titleImg = (ImageView) commonPagerTitleView.findViewById(R.id.title_img);
                titleImg.setImageResource(R.mipmap.ic_launcher);
                final TextView titleText = (TextView) commonPagerTitleView.findViewById(R.id.title_text);
                titleText.setText(mTitle.get(index));

                commonPagerTitleView.setOnPagerTitleChangeListener(new CommonPagerTitleView.OnPagerTitleChangeListener() {

                    @Override
                    public void onSelected(int index) {
                        titleText.setTextColor(Color.RED);
                    }

                    @Override
                    public void onDeselected(int index) {
                        titleText.setTextColor(Color.BLACK);
                    }

                    @Override
                    public void onLeave(int index, float leavePercent, boolean leftToRight) {
                        titleImg.setScaleX(1.3f + (0.8f - 1.3f) * leavePercent);
                        titleImg.setScaleY(1.3f + (0.8f - 1.3f) * leavePercent);
                    }

                    @Override
                    public void onEnter(int index, float enterPercent, boolean leftToRight) {
                        titleImg.setScaleX(0.8f + (1.3f - 0.8f) * enterPercent);
                        titleImg.setScaleY(0.8f + (1.3f - 0.8f) * enterPercent);
                    }
                });

                commonPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager.setCurrentItem(index);
                    }
                });

                return commonPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                return null;
            }
        });
        magicIndicator.setNavigator(commonNavigator);

    }
}
