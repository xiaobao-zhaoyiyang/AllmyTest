package com.imooc_shark;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

/**
 * Created by yo on 2016/6/24.
 */
public class ShakeSensor implements SensorEventListener {
    public ShakeSensor(){

    }
    private static final String TAG = ShakeSensor.class.getSimpleName();
    public static boolean STOP = false;
    private static final int SPEED_HOLD = 4800;
    private Context mContext;
    private SensorManager mSensorManager;
    private Sensor mSensor;

    private long last_time; // 记录最后一次的时间
    private float last_x; // 记录上一次摇动X的值
    private float last_y; // 记录上一次摇动Y的值
    private float last_z; // 记录上一次摇动Z的值

    public ShakeSensor(Context context){
        this.mContext = context;
    }

    /**
     * 初始化传感器
     */
    public void init(){
        mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        // 注册传感器
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    /**
     * 取消注册
     */
    public void unRegisterListener(){
        mSensorManager.unregisterListener(this, mSensor);
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - last_time > 10){
            // 两次摇动的时间间隔
            long timeDistance = currentTime - last_time;
            last_time = currentTime;

            // 当前的值
            float x = event.values[0]; // x轴变化的值
            float y = event.values[1]; // y轴变化的值
            float z = event.values[2]; // z轴变化的值
            Log.e(TAG, x + "..." + y + "..." + z);
            double speed; // 速度的阀值
            double absValue = Math.abs(x+y+z-last_x-last_y-last_z);
            speed = absValue / timeDistance * 10000;
            if (speed > SPEED_HOLD){
                // 当三个值的变化达到一定时进行后续操作
                if (!STOP) {
                    if (mListener != null) {
                        mListener.onShake();
                    }
                }
            }
            last_x = x;
            last_y = y;
            last_z = z;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private onShakeListener mListener;
    public void setonShakeListener(onShakeListener listener){
        this.mListener = listener;
    }
    public interface onShakeListener{
        void onShake();
    }
}
