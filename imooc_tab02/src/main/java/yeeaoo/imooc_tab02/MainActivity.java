package yeeaoo.imooc_tab02;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

/**
 * 单纯使用Fragment实现Tab效果，缺点是不能左右滑动。但可以在该页添加滑动删除效果。
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private LinearLayout mTabWechat;
    private LinearLayout mTabFrd;
    private LinearLayout mTabAddress;
    private LinearLayout mTabSettings;

    private ImageButton mWechatImg;
    private ImageButton mFrdImg;
    private ImageButton mAddressImg;
    private ImageButton mSettingImg;

    private Fragment Tab01;
    private Fragment Tab02;
    private Fragment Tab03;
    private Fragment Tab04;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        initView();
        initEvent();
        setSelect(0);
        int a = 8 & 6;
        Log.i("Tag", a + "");
    }

    private void initEvent() {
        mTabWechat.setOnClickListener(this);
        mTabFrd.setOnClickListener(this);
        mTabAddress.setOnClickListener(this);
        mTabSettings.setOnClickListener(this);
    }

    private void initView() {
        mTabWechat = (LinearLayout) findViewById(R.id.id_wechat);
        mTabFrd = (LinearLayout) findViewById(R.id.id_frd);
        mTabAddress = (LinearLayout) findViewById(R.id.id_address);
        mTabSettings = (LinearLayout) findViewById(R.id.id_settings);
        mWechatImg = (ImageButton) findViewById(R.id.id_wechat_img);
        mFrdImg = (ImageButton) findViewById(R.id.id_frd_img);
        mAddressImg = (ImageButton) findViewById(R.id.id_address_img);
        mSettingImg = (ImageButton) findViewById(R.id.id_settings_img);
    }

    @Override
    public void onClick(View v) {
        resetImg();
        switch (v.getId()){
            case R.id.id_wechat:
                setSelect(0);
                break;
            case R.id.id_frd:
                setSelect(1);
                break;
            case R.id.id_address:
                setSelect(2);
                break;
            case R.id.id_settings:
                setSelect(3);
                break;
        }
    }

    private void resetImg() {
        mWechatImg.setImageResource(R.mipmap.tab_weixin_normal);
        mFrdImg.setImageResource(R.mipmap.tab_find_frd_normal);
        mAddressImg.setImageResource(R.mipmap.tab_address_normal);
        mSettingImg.setImageResource(R.mipmap.tab_settings_normal);
    }

    /**
     * 图片点亮，设置内容区域
     * @param i
     */
    private void setSelect(int i){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        hideFragment(transaction);
        switch (i) {
            case 0:
                if (Tab01 == null){
                    Tab01 = new WechatFragment();
                    transaction.add(R.id.content, Tab01);
                }else {
                    transaction.show(Tab01);
                }
                mWechatImg.setImageResource(R.mipmap.tab_weixin_pressed);
                break;
            case 1:
                if (Tab02 == null){
                    Tab02 = new FrdFragment();
                    transaction.add(R.id.content, Tab02);
                }else {
                    transaction.show(Tab02);
                }
                mFrdImg.setImageResource(R.mipmap.tab_find_frd_pressed);
                break;
            case 2:
                if (Tab03 == null){
                    Tab03 = new AddressFragment();
                    transaction.add(R.id.content, Tab03);
                }else {
                    transaction.show(Tab03);
                }
                mAddressImg.setImageResource(R.mipmap.tab_address_pressed);
                break;
            case 3:
                if (Tab04 == null){
                    Tab04 = new SettingsFragment();
                    transaction.add(R.id.content, Tab04);
                }else {
                    transaction.show(Tab04);
                }
                mSettingImg.setImageResource(R.mipmap.tab_settings_pressed);
                break;
        }
        transaction.commit();
    }

    private void hideFragment(FragmentTransaction transaction) {
        if (Tab01 != null){
            transaction.hide(Tab01);
        }
        if (Tab02 != null){
            transaction.hide(Tab02);
        }
        if (Tab03 != null){
            transaction.hide(Tab03);
        }
        if (Tab04 != null){
            transaction.hide(Tab04);
        }
    }
}
