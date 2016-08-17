package yeeaoo.imooc_wechat;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;
    private List<Fragment> mDatas;
    private TextView mChatTextView;
    private TextView mFriendTextView;
    private TextView mContactTextView;
    private BadgeView mBadgeView;
    private LinearLayout mChatLayout;
    private int mScreen_3;
    private ImageView mTabLine;
    private int mCurrentPageIndex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        initTabLine();
        initView();
    }

    private void initTabLine() {
        mScreen_3 = getResources().getDisplayMetrics().widthPixels / 3;
        mTabLine = (ImageView) findViewById(R.id.iv_tabline);
        ViewGroup.LayoutParams params = mTabLine.getLayoutParams();
        params.width = mScreen_3;
        mTabLine.setLayoutParams(params);
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mChatTextView = (TextView) findViewById(R.id.tv_chat);
        mFriendTextView = (TextView) findViewById(R.id.tv_friend);
        mContactTextView = (TextView) findViewById(R.id.tv_contact);
        mChatLayout = (LinearLayout) findViewById(R.id.chat_layout);
        mDatas = new ArrayList<>();
        ChatMainTabFragment tab01 = new ChatMainTabFragment();
        FriendMainTabFragment tab02 = new FriendMainTabFragment();
        ContactMainTabFragment tab03 = new ContactMainTabFragment();
        mDatas.add(tab01);
        mDatas.add(tab02);
        mDatas.add(tab03);
        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mDatas.get(position);
            }

            @Override
            public int getCount() {
                return mDatas.size();
            }
        };
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTabLine.getLayoutParams();
//                if (mCurrentPageIndex == 0 && position == 0){// 0-1
//                    lp.leftMargin = (int) (positionOffset * mScreen_3 + mCurrentPageIndex * mScreen_3);
//                }else if (mCurrentPageIndex == 1 && position == 0){ // 1 - 0
//                    lp.leftMargin = (int) (mCurrentPageIndex * mScreen_3 + (positionOffset - 1) * mScreen_3);
//                }else if (mCurrentPageIndex == 1 && position == 1){ // 1 - 2
//                    lp.leftMargin = (int) (mCurrentPageIndex * mScreen_3 + positionOffset * mScreen_3);
//                }else if (mCurrentPageIndex == 2 && position == 1){ // 2 - 1
//                    lp.leftMargin = (int) (mCurrentPageIndex * mScreen_3 + (positionOffset - 1) * mScreen_3);
//                }
//                mTabLine.setLayoutParams(lp);
                mTabLine.setTranslationX((positionOffset + position) * mScreen_3);
            }

            @Override
            public void onPageSelected(int position) {
                resetTextView();
                switch (position){
                    case 0:
                        if (mBadgeView != null){
                            mChatLayout.removeView(mBadgeView);
                        }
                        mBadgeView = new BadgeView(MainActivity.this);
                        mBadgeView.setBadgeCount(7);
                        mChatLayout.addView(mBadgeView);
                        mChatTextView.setTextColor(Color.parseColor("#008000"));
                        break;
                    case 1:
                        mFriendTextView.setTextColor(Color.parseColor("#008000"));
                        break;
                    case 2:
                        mContactTextView.setTextColor(Color.parseColor("#008000"));
                        break;
                }
                mCurrentPageIndex = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void resetTextView() {
        mChatTextView.setTextColor(Color.BLACK);
        mFriendTextView.setTextColor(Color.BLACK);
        mContactTextView.setTextColor(Color.BLACK);
    }
}
