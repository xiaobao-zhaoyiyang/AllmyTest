package me;

import util.CommonUtil;

/**
 * Created by lbf on 2016/7/29.
 */
public class MePresenter implements MeContract.Presenter {

    private MeContract.View mView;

    public MePresenter(MeContract.View view) {
        mView = CommonUtil.checkNotNull(view,"view cannot be null!");
        mView.setPresenter(this);
    }

    @Override
    public void start() {

    }
}
