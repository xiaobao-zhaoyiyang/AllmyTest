package yeeaoo.imooc_progressbar;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import views.HorizontalProgressbarWithProgress;
import views.RoundProgressBarWithProgress;

public class MainActivity extends AppCompatActivity {

    private static final int MSG_UPDATE = 0X110;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int progress = mHProgress.getProgress();
            mHProgress.setProgress(++progress);
            mRProgress.setProgress(++progress);
            if (progress >= 100){
                mHandler.removeMessages(MSG_UPDATE);
            }
            mHandler.sendEmptyMessageDelayed(MSG_UPDATE, 100);
        }
    };

    private HorizontalProgressbarWithProgress mHProgress;
    private RoundProgressBarWithProgress mRProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHProgress = (HorizontalProgressbarWithProgress) findViewById(R.id.id_progress1);
        mRProgress = (RoundProgressBarWithProgress) findViewById(R.id.id_progress2);
        mHandler.sendEmptyMessage(MSG_UPDATE);
    }
}
