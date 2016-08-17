package search;

import com.imitationofwechat.main.BasePresenter;
import com.imitationofwechat.main.BaseView;

/**
 * Created by lbf on 2016/7/28.
 */
public interface SearchContract {
    interface Presenter extends BasePresenter {
    }

    interface View extends BaseView<Presenter> {
    }
}
