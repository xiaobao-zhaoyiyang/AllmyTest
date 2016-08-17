package com.imooc_festval_msm.fragment;


import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.imooc_festval_msm.R;
import com.imooc_festval_msm.bean.SendMsg;
import com.imooc_festval_msm.db.SmsProvider;
import com.imooc_festval_msm.view.FlowLayout;

import java.text.SimpleDateFormat;

/**
 * Created by yo on 2016/7/13.
 */
public class SmsHistoryFragment extends ListFragment {
    private static final int LOADER_ID = 1;
    private LayoutInflater mInflater;
    private CursorAdapter mAdapter;
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mInflater = LayoutInflater.from(getActivity());
        initLoader();
        setUpListAdapter();
    }

    private void initLoader() {
        getLoaderManager().initLoader(LOADER_ID, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                CursorLoader loader = new CursorLoader(getActivity(), SmsProvider.URI_SMS_ALL, null, null, null, null);
                return loader;
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                if (loader.getId() == LOADER_ID){
                    mAdapter.swapCursor(data);
                }
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
                mAdapter.swapCursor(null);
            }
        });
    }

    private void setUpListAdapter() {
        mAdapter = new CursorAdapter(getActivity(), null, false) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                View view = mInflater.inflate(R.layout.item_sended_msg, parent, false);
                return view;
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                TextView msg = (TextView) view.findViewById(R.id.id_tv_msg);
                FlowLayout fl = (FlowLayout) view.findViewById(R.id.id_fl_contacts);
                TextView fes = (TextView) view.findViewById(R.id.id_tv_festival);
                TextView date = (TextView) view.findViewById(R.id.id_tv_date);

                msg.setText(cursor.getString(cursor.getColumnIndex(SendMsg.COLUMN_MSG)));
                fes.setText(cursor.getString(cursor.getColumnIndex(SendMsg.COLUMN_FESTIVAL_NAME)));
                long dateVal = cursor.getLong(cursor.getColumnIndex(SendMsg.COLUMN_DATE));
                date.setText(parseDate(dateVal));

                String names = cursor.getString(cursor.getColumnIndex(SendMsg.COLUMN_NAMES));
                if (TextUtils.isEmpty(names))
                    return;
                fl.removeAllViews();
                for (String name : names.split(":")){
                    addTag(name, fl);
                }
                msg.setText(cursor.getString(cursor.getColumnIndex(SendMsg.COLUMN_MSG)));
            }
        };
        setListAdapter(mAdapter);
    }

    private String parseDate(long dateVal) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return df.format(dateVal);
    }

    private void addTag(String name, FlowLayout fl) {
        TextView tv = (TextView) mInflater.inflate(R.layout.tag, fl, false);
        tv.setText(name);
        fl.addView(tv);
    }
}
