package fragment;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.copyyeeaootf.MainActivity;
import com.copyyeeaootf.R;

/**
 * Created by yo on 2016/7/29.
 */
public class Fragment_In_2 extends Fragment implements View.OnClickListener {
    private View view;
    private TextView mTv;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private LinearLayout layout_control;
    private Button but_start, but_pause, but_stop;
    private MediaPlayer mediaPlayer;
    private String path;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null){
            view = inflater.inflate(R.layout.fragment_tab2, container, false);
            initView(view);
        }
        return view;
    }

    private void initView(View view) {
        mTv = (TextView) view.findViewById(R.id.id_2_textView);
        // 使用占位符实现字符串的拼接
        String str = getActivity().getString(R.string.textTest);
        mTv.setText(String.format(str, 2000, 12.0, "string"));

        surfaceView = (SurfaceView) view.findViewById(R.id.id_surfaceView);
        surfaceHolder = surfaceView.getHolder();

        layout_control = (LinearLayout) view.findViewById(R.id.id_control);
        but_start = (Button) view.findViewById(R.id.id_start);
        but_pause = (Button) view.findViewById(R.id.id_pause);
        but_stop = (Button) view.findViewById(R.id.id_stop);
        but_start.setOnClickListener(this);
        but_pause.setOnClickListener(this);
        but_stop.setOnClickListener(this);

        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                // 当SurfaceView中的Surface被创建的时候被调用
                //在这里我们指定MediaPlayer在当前的Surface中进行播放

                //在指定了MediaPlayer播放的容器后，我们就可以使用prepare或者prepareAsync来准备播放了

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                // 当Surface尺寸等参数改变时触发
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });

        // 将myTouchListener注册到分发列表
//        ((MainActivity)this.getActivity()).registerMyTouchListener(myTouchListener);

        path = Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera/143013.mp4";
    }

    MainActivity.MyTouchListener myTouchListener = new MainActivity.MyTouchListener() {
        @Override
        public void onTouchEvent(MotionEvent event) {
            if (layout_control.getVisibility() == View.GONE){
                layout_control.setVisibility(View.VISIBLE);
            }else{
                layout_control.setVisibility(View.GONE);
            }
        }
    };

    private void play(){
        try{
            if (mediaPlayer == null) {
                if (TextUtils.isEmpty(path)) {
                    Toast.makeText(getActivity(), "地址有误", Toast.LENGTH_SHORT).show();
                    return;
                }
                mediaPlayer = new MediaPlayer();
                // 设计音频流类型
                mediaPlayer
                        .setAudioStreamType(AudioManager.STREAM_MUSIC);

                // 指定播放的文件
                mediaPlayer.setDataSource(path);
                // 指定用于播放视频的SurfaceView的控件
                mediaPlayer.setDisplay(surfaceHolder);
                mediaPlayer.prepare();
//                mediaPlayer.prepareAsync();

                //记录上次播放的位置
//                mediaPlayer.seekTo(backPosition);
                mediaPlayer.start();
            }else{
                mediaPlayer.start();
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    private void pause(){
        if (mediaPlayer != null){
            if (mediaPlayer.isPlaying()){
                mediaPlayer.pause();
            }
        }
    }

    private void stop(){
        if (mediaPlayer != null){
            if (mediaPlayer.isPlaying()){
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_start:
                play();
                break;
            case R.id.id_pause:
                pause();
                break;
            case R.id.id_stop:
                stop();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null){
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
