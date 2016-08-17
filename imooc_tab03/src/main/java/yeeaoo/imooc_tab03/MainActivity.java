package yeeaoo.imooc_tab03;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.viewpagerindicator.TabPageIndicator;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private TabPageIndicator tabPageIndicator;
    private List<Fragment> mFragment;
    private TabAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        initView();
    }

    private void initView() {
        viewPager= (ViewPager) findViewById(R.id.id_viewpager);
        tabPageIndicator = (TabPageIndicator) findViewById(R.id.id_indicator);
        adapter = new TabAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
//        tabPageIndicator.setVisibility(View.VISIBLE);
        tabPageIndicator.setViewPager(viewPager);
    }
}
