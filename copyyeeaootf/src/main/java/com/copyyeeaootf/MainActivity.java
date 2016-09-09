package com.copyyeeaootf;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;

import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.ArrayList;

import fragment.Fragment1;
import tool.SimpleHttpServer;

public class MainActivity extends AppCompatActivity {

    private Fragment1 mContentFragment  ;
    private SimpleHttpServer simpleHttpServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        Fresco.initialize(this);
        FragmentManager fm = getSupportFragmentManager();
        mContentFragment = (Fragment1) fm.findFragmentById(R.id.id_fragment_container);

        if(mContentFragment == null )
        {
            mContentFragment = Fragment1.newInstance(Fragment1.ARGUMENT);
            fm.beginTransaction().add(R.id.id_fragment_container,mContentFragment).commit();
        }

//        WebConfiguration configuration = new WebConfiguration();
//        Log.i("TAG", configuration.toString());
//        configuration.setPort(8088);
//        simpleHttpServer = new SimpleHttpServer(configuration);
//        simpleHttpServer.startSync();

    }

    @Override
    protected void onDestroy() {
//        simpleHttpServer.stopSync();
        super.onDestroy();
    }

    public interface MyTouchListener {
        void onTouchEvent(MotionEvent event);
    }

    // 保存MyTouchListener接口的列表
    private ArrayList<MyTouchListener> myTouchListeners =
            new ArrayList<MainActivity.MyTouchListener>();

    /**
     * 提供给Fragment通过getActivity()方法来注册自己的触摸事件的方法
     * @param listener
     */
    public void registerMyTouchListener(MyTouchListener listener) {
        myTouchListeners.add(listener);
    }

    /**
     * 提供给Fragment通过getActivity()方法来取消注册自己的触摸事件的方法
     * @param listener
     */
    public void unRegisterMyTouchListener(MyTouchListener listener) {
        myTouchListeners.remove(listener);
    }

    /**
     * 分发触摸事件给所有注册了MyTouchListener的接口
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        for (MyTouchListener listener : myTouchListeners) {
            listener.onTouchEvent(ev);
        }
        return super.dispatchTouchEvent(ev);
    }
}
