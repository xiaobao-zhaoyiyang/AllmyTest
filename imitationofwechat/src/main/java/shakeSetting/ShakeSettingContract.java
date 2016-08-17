package shakeSetting;

import android.content.Intent;

import com.imitationofwechat.main.BasePresenter;
import com.imitationofwechat.main.BaseView;

/**
 * Created by lbf on 2016/7/28.
 */
public interface ShakeSettingContract {
    interface Presenter extends BasePresenter {
        void saveBackgroundImage(String imagePath);
        void pause();
        void result(int requestCode, int resultCode, Intent result);
    }

    interface View extends BaseView<Presenter> {
        void setUseSoundSelected(boolean isSelected);
        boolean isUseSoundSelected();

    }
}
