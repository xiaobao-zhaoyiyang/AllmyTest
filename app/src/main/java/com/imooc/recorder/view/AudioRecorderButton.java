package com.imooc.recorder.view;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import yeeaoo.imooc_recorder.R;

/**
 * Created by yo on 2016/3/16.
 */
public class AudioRecorderButton extends Button implements AudioManager.AudioStateListener {
    private static final int DISTANCE_Y_CANCEL = 50;
    private static final int STATE_NORMAL = 1;
    private static final int STATE_RECORDING = 2;
    private static final int STATE_WANT_TO_CANAEL = 3;

    private int mCurState = STATE_NORMAL;
    private boolean isRecording;
    private DialogManager mDialogManager;
    private AudioManager mAudioManager;

    private float mTime;
    //是否触发LongClick
    private boolean mReady;

    public AudioRecorderButton(Context context) {
        this(context, null);
    }

    public AudioRecorderButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDialogManager = new DialogManager(getContext());
        String dir = Environment.getExternalStorageDirectory()+
                "/imooc_recorder_audios";
        mAudioManager = AudioManager.getInstance(dir);
        mAudioManager.setOnAudioStateListener(this);
        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mReady = true;
               mAudioManager.prepareAudio();
                return false;
            }
        });
    }

    /**
     * 录音完成后的回调
     */
    public interface AudioFinishRecorderListener{
        void onFinish(float seconds, String filePath);
    }
    private AudioFinishRecorderListener mListener;
    public void setAudioFinishRecorderListener(AudioFinishRecorderListener listener){
        mListener = listener;
    }

    /**
     * 获取音量大小的Runnable
     */
    private Runnable mGetVoiceLevelRunnable = new Runnable() {
        @Override
        public void run() {
            while (isRecording){
                try {
                    Thread.sleep(100);
                    mTime += 0.1f;
                    mHandler.sendEmptyMessage(MSG_VOICE_CHANGED);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private static final int MSG_AUDIO_PREPARED = 0x110;
    private static final int MSG_VOICE_CHANGED = 0x111;
    private static final int MSG_DIALOG_DISMISS = 0x112;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MSG_AUDIO_PREPARED:
                    // TODO 真正显示应该在audio end prepare以后
                    isRecording = true;
                    mDialogManager.showRecordingDialog();
                    new Thread(mGetVoiceLevelRunnable).start();
                    break;
                case MSG_VOICE_CHANGED:
                    mDialogManager.updateVoiceLevel(mAudioManager.getVoiceLevel(7));
                    break;
                case MSG_DIALOG_DISMISS:
                    mDialogManager.dismissDialog();
                    break;

            }
        }
    };

    @Override
    public void wellPrepared() {
        mHandler.sendEmptyMessage(MSG_AUDIO_PREPARED);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (action){
            case MotionEvent.ACTION_DOWN:
                changeState(STATE_RECORDING);
                break;

            case MotionEvent.ACTION_MOVE:
                if (isRecording) {
                    // 开始录音后根据x，y的坐标，判断是否想要取消
                    if (wantToCancel(x, y)){
                        changeState(STATE_WANT_TO_CANAEL);
                    }else {
                        changeState(STATE_RECORDING);
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
                if (!mReady) {
                    reset();
                    return super.onTouchEvent(event);
                }
                if (!isRecording || mTime < 0.6f){
                    mDialogManager.tooShort();
                    mAudioManager.cancel();
                    mHandler.sendEmptyMessageDelayed(MSG_DIALOG_DISMISS, 1300);
                }else if (mCurState == STATE_RECORDING){
                    // release
                    mDialogManager.dismissDialog();
                    mAudioManager.release();
                    if (mListener != null){
                        mListener.onFinish(mTime, mAudioManager.getCurrentFilePath());
                    }
                    // callbackToAct

                }else if (mCurState == STATE_WANT_TO_CANAEL){
                    // cancel
                    mDialogManager.dismissDialog();
                    mAudioManager.cancel();
                }
                reset();
                break;
        }
        return super.onTouchEvent(event);
    }

    // 恢复状态及标志位
    private void reset() {
        isRecording = false;
        mReady = false;
        mTime = 0;
        changeState(STATE_NORMAL);
    }

    private boolean wantToCancel(int x, int y) {
        if (x < 0 || x > getWidth()){
            return true;
        }
        if (y < -DISTANCE_Y_CANCEL || y > getHeight() + DISTANCE_Y_CANCEL){
            return true;
        }
        return false;
    }

    private void changeState(int state) {
        if (mCurState != state){
            mCurState = state;
            switch (state){
                case STATE_NORMAL:
                    setBackgroundResource(R.drawable.btn_recorder_normal);
                    setText(R.string.str_recorder_normal);
                    break;
                case STATE_RECORDING:
                    setBackgroundResource(R.drawable.btn_recording);
                    setText(R.string.str_recorder_recording);
                    if (isRecording){
                        // Dialog.recording();
                        mDialogManager.recording();
                    }
                    break;
                case STATE_WANT_TO_CANAEL:
                    setBackgroundResource(R.drawable.btn_recording);
                    setText(R.string.str_recorder_want_cancel);
                    // Dialog.recording();
                    mDialogManager.wantToCancel();
                    break;
            }
        }
    }

}
