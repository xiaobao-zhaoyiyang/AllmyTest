package shake;

import android.graphics.Bitmap;
import android.view.animation.Animation;

import com.imitationofwechat.main.BasePresenter;
import com.imitationofwechat.main.BaseView;

/**
 * Created by lbf on 2016/7/28.
 */
public interface ShakeContract {
    interface Presenter extends BasePresenter {
        void setupAnimation();
        void create();
        void resume();
        void pause();
    }

    interface View extends BaseView<Presenter> {
        void changeSelection(int position);
        void setHiddenBackground(Bitmap background);
        void setHiddenBackground(int background);
        void startAnimation(Animation animationUp, Animation animationDown);
    }
}
