package com.imooc_festval_msm;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.imooc_festval_msm.bean.FestivalLab;
import com.imooc_festval_msm.bean.Msg;
import com.imooc_festval_msm.fragment.FestivalCategoryFragment;

/**
 * Created by yo on 2016/7/12.
 */
public class ChooseMsgActivity extends AppCompatActivity {
    private ListView mListView;
    private FloatingActionButton mFabToSend;
    private int mFestivalId;

    private ArrayAdapter<Msg> mAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_msg);
        mFestivalId = getIntent().getIntExtra(FestivalCategoryFragment.ID_FESTIVAL, -1);
        setTitle(FestivalLab.getInstance().getFestivalById(mFestivalId).getName());

        initView();
        initEvent();
    }

    private void initEvent() {
        mFabToSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendMsgActivity.toActivity(ChooseMsgActivity.this, mFestivalId, -1);
            }
        });
    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.id_lv_msgs);
        mFabToSend = (FloatingActionButton) findViewById(R.id.id_fab_toSend);

        mListView.setAdapter(mAdapter = new ArrayAdapter<Msg>(this, -1, FestivalLab.getInstance().getMsgByFestivalId(mFestivalId)){
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                if (convertView == null){
                    convertView = View.inflate(ChooseMsgActivity.this, R.layout.item_msg, null);
                }
                TextView content = (TextView) convertView.findViewById(R.id.id_tv_content);
                Button toSend = (Button) convertView.findViewById(R.id.id_btn_toSend);
                content.setText("   " + getItem(position).getContent());
                toSend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SendMsgActivity.toActivity(ChooseMsgActivity.this, mFestivalId, getItem(position).getId());
                    }
                });
                return convertView;
            }
        });
    }
}
