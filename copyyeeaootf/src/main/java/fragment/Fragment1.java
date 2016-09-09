package fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.copyyeeaootf.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 给Fragment添加newInstance方法，将需要的参数传入，设置到bundle中，然后setArguments(bundle)，
 * 最后在onCreate中进行获取；
 * 这样就完成了Fragment和Activity间的解耦。当然了这里需要注意：
 * setArguments方法必须在fragment创建以后，添加给Activity前完成。
 * 千万不要，首先调用了add，然后设置arguments。
 */
public class Fragment1 extends Fragment implements View.OnClickListener {

    private String mArgument;
    public static final String ARGUMENT = "argument";

    private ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;
    private List<Fragment> mDatas;
    private TextView mChatTextView;
    private TextView mFriendTextView;
    private TextView mContactTextView;
    private LinearLayout mChatLayout;
    private int mScreen_3;
    private ImageView mTabLine, mLine;
    private int mCurrentPageIndex;

    private String[] title = new String[]{"猜你爱", "新分享", "赞点评", "精华帖", "优微课", "瞎咋呼"};
    private ArrayList<TextView> mTitleText = new ArrayList<>();
    private LinearLayout mTabContainer;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null)
            mArgument = bundle.getString(ARGUMENT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_1, container, false);
        initView(view);
        initTabLine(view);
        return view;
    }

    private void initView(View view) {
        mViewPager = (ViewPager) view.findViewById(R.id.id_viewpager);
        mChatTextView = (TextView) view.findViewById(R.id.tv_chat);
        mFriendTextView = (TextView) view.findViewById(R.id.tv_friend);
        mContactTextView = (TextView) view.findViewById(R.id.tv_contact);
        mChatLayout = (LinearLayout) view.findViewById(R.id.chat_layout);

        mChatTextView.setOnClickListener(this);
        mFriendTextView.setOnClickListener(this);
        mContactTextView.setOnClickListener(this);

        mTabContainer = (LinearLayout) view.findViewById(R.id.id_tab_container);

        mDatas = new ArrayList<>();
        Fragment_In_1 tab01 = new Fragment_In_1();
        Fragment_In_2 tab02 = new Fragment_In_2();
        Fragment_In_3 tab03 = new Fragment_In_3();
        Fragment_In_4 tab04 = new Fragment_In_4();
        Fragment_In_5 tab05 = new Fragment_In_5();
        Fragment_In_6 tab06 = new Fragment_In_6();

        mDatas.add(tab01);
        mDatas.add(tab02);
        mDatas.add(tab03);
        mDatas.add(tab04);
        mDatas.add(tab05);
        mDatas.add(tab06);
        mAdapter = new FragmentPagerAdapter(getActivity().getSupportFragmentManager()) {
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
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                mTabLine.setTranslationX((positionOffset + position) * mScreen_3);
                mLine.setTranslationX((positionOffset + position) * mScreen_3);
            }

            @Override
            public void onPageSelected(int position) {
                resetTextView();
//                switch (position) {
//                    case 0:
//                        mChatTextView.setTextColor(Color.parseColor("#008000"));
//                        break;
//                    case 1:
//                        mFriendTextView.setTextColor(Color.parseColor("#008000"));
//                        break;
//                    case 2:
//                        mContactTextView.setTextColor(Color.parseColor("#008000"));
//                        break;
//                }
                mTitleText.get(position).setTextColor(Color.parseColor("#008000"));
                mCurrentPageIndex = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 重置文字的颜色
     */
    private void resetTextView() {
//        mChatTextView.setTextColor(Color.BLACK);
//        mFriendTextView.setTextColor(Color.BLACK);
//        mContactTextView.setTextColor(Color.BLACK);

        for (int i = 0; i < mTitleText.size(); i++) {
            mTitleText.get(i).setTextColor(Color.BLACK);
        }
    }

    private void initTabLine(View view) {

        for (int i = 0; i < title.length; i++) {
            TextView tv = new TextView(getActivity());
            tv.setText(title[i]);
            tv.setGravity(Gravity.CENTER);
            if (i == 0) {
                tv.setTextColor(Color.parseColor("#008000"));
            }else
                tv.setTextColor(Color.BLACK);
            tv.setTag(i);
            mTitleText.add(tv);
            LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
            ll.weight = 1;
            mTabContainer.addView(tv, ll);
            tv.setOnClickListener(customClickListener);
        }
        mLine = (ImageView) view.findViewById(R.id.iv_tab_line);

        mScreen_3 = getActivity().getResources().getDisplayMetrics().widthPixels / mTitleText.size();
//        mTabLine = (ImageView) view.findViewById(R.id.iv_tabline);
//        ViewGroup.LayoutParams params = mTabLine.getLayoutParams();
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mLine.getLayoutParams();
        params.width = mScreen_3 - (int) (getActivity().getResources().getDisplayMetrics().density * 20 + 0.5f);

//        mTabLine.setLayoutParams(params);

        mLine.setLayoutParams(params);
    }

    /**
     * 传入需要的参数，设置给arguments
     *
     * @param argument
     * @return
     */
    public static Fragment1 newInstance(String argument) {
        Bundle bundle = new Bundle();
        bundle.putString(ARGUMENT, argument);
        Fragment1 contentFragment = new Fragment1();
        contentFragment.setArguments(bundle);
        return contentFragment;
    }

    @Override
    public void onClick(View v) {
        resetTextView();
        switch (v.getId()) {
            case R.id.tv_chat:
                mChatTextView.setTextColor(Color.parseColor("#008000"));
                mViewPager.setCurrentItem(0);
                mCurrentPageIndex = 0;
                break;
            case R.id.tv_friend:
                mFriendTextView.setTextColor(Color.parseColor("#008000"));
                mViewPager.setCurrentItem(1);
                mCurrentPageIndex = 1;
                break;
            case R.id.tv_contact:
                mContactTextView.setTextColor(Color.parseColor("#008000"));
                mViewPager.setCurrentItem(2);
                mCurrentPageIndex = 2;
                break;
        }
    }

    private View.OnClickListener customClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int tag = (int) v.getTag();

            mTitleText.get(tag).setTextColor(Color.parseColor("#008000"));
            mViewPager.setCurrentItem(tag);

        }
    };
}
