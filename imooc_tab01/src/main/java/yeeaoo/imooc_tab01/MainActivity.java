package yeeaoo.imooc_tab01;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 使用ViewPager和View实现Tab效果，优点是可以进行左右滑动。
 * 最好是将View换成Fragment来实现，这样就解决了MainActivity代码冗长的缺点也便于日后的管理
 * FragmentPAgeAdapter适配器
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private ViewPager mViewPager;
    private LinearLayout mTabWechat;
    private LinearLayout mTabFrd;
    private LinearLayout mTabAddress;
    private LinearLayout mTabSettings;
    private ImageButton mWechatImg;
    private ImageButton mFrdImg;
    private ImageButton mAddressImg;
    private ImageButton mSettingImg;
    private PagerAdapter mAdapter;
    private List<View> mViews = new ArrayList<View>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        initView();
        initEvent();
    }

    private void initEvent() {
        mTabWechat.setOnClickListener(this);
        mTabFrd.setOnClickListener(this);
        mTabAddress.setOnClickListener(this);
        mTabSettings.setOnClickListener(this);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                resetImg();
                switch (position){
                    case 0:
                        mWechatImg.setImageResource(R.mipmap.tab_weixin_pressed);
                        break;
                    case 1:
                        mFrdImg.setImageResource(R.mipmap.tab_find_frd_pressed);
                        break;
                    case 2:
                        mAddressImg.setImageResource(R.mipmap.tab_address_pressed);
                        break;
                    case 3:
                        mSettingImg.setImageResource(R.mipmap.tab_settings_pressed);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.id_viewpager);
        mTabWechat = (LinearLayout) findViewById(R.id.id_wechat);
        mTabFrd = (LinearLayout) findViewById(R.id.id_frd);
        mTabAddress = (LinearLayout) findViewById(R.id.id_address);
        mTabSettings = (LinearLayout) findViewById(R.id.id_settings);
        mWechatImg = (ImageButton) findViewById(R.id.id_wechat_img);
        mFrdImg = (ImageButton) findViewById(R.id.id_frd_img);
        mAddressImg = (ImageButton) findViewById(R.id.id_address_img);
        mSettingImg = (ImageButton) findViewById(R.id.id_settings_img);

        LayoutInflater inflater = LayoutInflater.from(this);
        View tab01 = inflater.inflate(R.layout.tab01, null);
        View tab02 = inflater.inflate(R.layout.tab02, null);
        View tab03 = inflater.inflate(R.layout.tab03, null);
        View tab04 = inflater.inflate(R.layout.tab04, null);

        mViews.add(tab01);
        mViews.add(tab02);
        mViews.add(tab03);
        mViews.add(tab04);

        mAdapter = new PagerAdapter() {

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View view = mViews.get(position);
                container.addView(view);
                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(mViews.get(position));
            }

            @Override
            public int getCount() {
                return mViews.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }
        };

        mViewPager.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        resetImg();
        switch (v.getId()){
            case R.id.id_wechat:
                mViewPager.setCurrentItem(0);
                mWechatImg.setImageResource(R.mipmap.tab_weixin_pressed);
                break;
            case R.id.id_frd:
                mViewPager.setCurrentItem(1);
                mFrdImg.setImageResource(R.mipmap.tab_find_frd_pressed);
                break;
            case R.id.id_address:
                mViewPager.setCurrentItem(2);
                mAddressImg.setImageResource(R.mipmap.tab_address_pressed);
                break;
            case R.id.id_settings:
                mViewPager.setCurrentItem(3);
                mSettingImg.setImageResource(R.mipmap.tab_settings_pressed);
                break;
        }
    }

    private void resetImg() {
        mWechatImg.setImageResource(R.mipmap.tab_weixin_normal);
        mFrdImg.setImageResource(R.mipmap.tab_find_frd_normal);
        mAddressImg.setImageResource(R.mipmap.tab_address_normal);
        mSettingImg.setImageResource(R.mipmap.tab_settings_normal);
    }
}
