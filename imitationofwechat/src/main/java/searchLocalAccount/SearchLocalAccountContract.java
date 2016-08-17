package searchLocalAccount;


import com.imitationofwechat.main.BasePresenter;
import com.imitationofwechat.main.BaseView;

import java.util.List;

import beans.ContactBean;

/**
 * Created by lbf on 2016/7/28.
 */
public interface SearchLocalAccountContract {
    interface Presenter extends BasePresenter {
        void searchAccount(String name);
    }

    interface View extends BaseView<Presenter> {
        void setResultList(List<ContactBean> resultList);
        List<ContactBean> getAccountList();
    }
}
