package com.imooc_festval_msm.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.imooc_festval_msm.ChooseMsgActivity;
import com.imooc_festval_msm.R;
import com.imooc_festval_msm.bean.Festival;
import com.imooc_festval_msm.bean.FestivalLab;

/**
 * Created by yo on 2016/7/12.
 */
public class FestivalCategoryFragment extends Fragment {

    public static final String ID_FESTIVAL = "festivalId";
    private GridView mGridView;
    private ArrayAdapter<Festival> mAdapter;
    private LayoutInflater mInflater;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_festival_category, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mInflater = LayoutInflater.from(getActivity());
        mGridView = (GridView) view.findViewById(R.id.id_gv_festivalCategory);
        mGridView.setAdapter(mAdapter = new ArrayAdapter<Festival>(getActivity(), -1, FestivalLab.getInstance().getFestivals()){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null){
                    convertView = mInflater.inflate(R.layout.item_festival, parent, false);
                }
                TextView tv = (TextView) convertView.findViewById(R.id.id_tv_festival_name);
                tv.setText(getItem(position).getName());
                return convertView;
            }
        });
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ChooseMsgActivity.class);
                intent.putExtra(ID_FESTIVAL, mAdapter.getItem(position).getId());
                startActivity(intent);
            }
        });
    }
}
