package com.imooc.recorder.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import yeeaoo.imooc_recorder.R;

/**
 * Created by yo on 2016/3/16.
 */
public class DialogManager {
    private Dialog mDialog;
    private ImageView mIcon;
    private ImageView mVoice;
    private TextView mLabel;
    private Context mContext;
    public DialogManager(Context context){
        mContext = context;
    }
    public void showRecordingDialog(){
        mDialog = new Dialog(mContext, R.style.Theme_AudioDialog);
        View view = View.inflate(mContext, R.layout.dialog_record, null);
        mDialog.setContentView(view);
        mIcon = (ImageView) mDialog.findViewById(R.id.id_recorder_dialog_icon);
        mVoice = (ImageView) mDialog.findViewById(R.id.id_recorder_dialog_voice);
        mLabel = (TextView) mDialog.findViewById(R.id.id_recorder_dialog_label);
        mDialog.show();
    }
    public void recording(){
        if (mDialog != null && mDialog.isShowing()){
            mIcon.setVisibility(View.VISIBLE);
            mVoice.setVisibility(View.VISIBLE);
            mLabel.setVisibility(View.VISIBLE);
            mIcon.setImageResource(R.mipmap.recorder);
            mLabel.setText("手指上滑，取消发送");
        }
    }
    public void wantToCancel(){
        if (mDialog != null && mDialog.isShowing()){
            mIcon.setVisibility(View.VISIBLE);
            mVoice.setVisibility(View.GONE);
            mLabel.setVisibility(View.VISIBLE);
            mIcon.setImageResource(R.mipmap.cancel);
            mLabel.setText("松开手指，取消发送");
        }
    }
    public void tooShort(){
        if (mDialog != null && mDialog.isShowing()){
            mIcon.setVisibility(View.VISIBLE);
            mVoice.setVisibility(View.GONE);
            mLabel.setVisibility(View.VISIBLE);
            mIcon.setImageResource(R.mipmap.voice_to_short);
            mLabel.setText("录音时间过短");
        }
    }
    public void dismissDialog(){
        if (mDialog != null && mDialog.isShowing()){
           mDialog.dismiss();
            mDialog = null;
        }
    }

    /**
     * 通过level去更新voice上的图片
     * @param level 等级1~7
     */
    public void updateVoiceLevel(int level){
        if (mDialog != null && mDialog.isShowing()){
//            mIcon.setVisibility(View.VISIBLE);
//            mVoice.setVisibility(View.VISIBLE);
//            mLabel.setVisibility(View.VISIBLE);
            int resId = mContext.getResources().getIdentifier("v"+level,
                    "mipmap", mContext.getPackageName());
            mVoice.setImageResource(resId);
//            mLabel.setText("手指上滑，取消发送");
        }
    }

}
