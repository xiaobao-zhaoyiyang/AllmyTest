package yeeaoo.imooc_tab03;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by yo on 2016/4/19.
 */
public class TabAdapter extends FragmentPagerAdapter {
    public static String [] TITLES = {"课程...", "问答...", "求课...", "学习...", "计划...", "中心...",
            "课程...", "问答...", "求课...", "学习...", "计划...", "中心..."};
    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        TabFragment fragment = new TabFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return TITLES.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position];
    }
}
