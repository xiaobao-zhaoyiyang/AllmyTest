package com.imooc_arcmenu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import view.ArcMenu;

public class MainActivity extends AppCompatActivity {

    private ArcMenu mArcMenu;
    private ListView mListView;
    private List<String> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mArcMenu = (ArcMenu) findViewById(R.id.id_arcMenu);
        mArcMenu.setOnMenuItemClickListener(new ArcMenu.OnMenuItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                String s = (String) view.getTag();
                Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        });
        initView();
        initData();
        mListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mData));
        initEvent();
    }

    private void initEvent() {
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (mArcMenu.isOpen()){
                    mArcMenu.toggleMenu(600);
                }
            }
        });
    }

    private void initData() {
        mData = new ArrayList<>();
        for (int i = 'A'; i < 'Z'; i++) {
            mData.add((char)i + "");
        }
    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.id_listView);
    }
}
