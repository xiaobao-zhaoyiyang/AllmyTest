package com.copyyeeaootf;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.facebook.drawee.backends.pipeline.Fresco;

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
}
