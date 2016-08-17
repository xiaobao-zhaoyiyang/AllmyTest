package yeeaoo.mytest;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by yo on 2016/6/15.
 */
public class LightSensorTestActivity extends AppCompatActivity {
    private SensorManager sensorManager;
    private TextView lightLevel;
    private ImageView compassImg;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lightsensortest);
        lightLevel = (TextView) findViewById(R.id.light_level);
        compassImg = (ImageView) findViewById(R.id.arrow_img);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        // 光照传感器
        Sensor sensor_light = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        sensorManager.registerListener(listener_light, sensor_light, SensorManager.SENSOR_DELAY_NORMAL);
        // 加速传感器
        Sensor sensor_acc = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(listener_acc, sensor_acc, SensorManager.SENSOR_DELAY_NORMAL);
        // 地磁传感器
        Sensor sensor_mag = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sensorManager.registerListener(listener, sensor_acc, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(listener, sensor_mag, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (sensorManager != null){
            sensorManager.unregisterListener(listener_light);
            sensorManager.unregisterListener(listener_acc);
            sensorManager.unregisterListener(listener);
        }
    }

    private SensorEventListener listener_light = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            // values数组中第一个下标的值就是当前的光照强度
            float value = event.values[0];
            lightLevel.setText("current light level is " + value + "lx");
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    private SensorEventListener listener_acc = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            // 加速度可能会是负值，所以要取他们的绝对值
            float xValue = Math.abs(event.values[0]);
            float yValue = Math.abs(event.values[1]);
            float zValue = Math.abs(event.values[2]);
            if (xValue > 15 || yValue > 15 || zValue > 15){
                // 认为用户摇动了手机，触发摇一摇逻辑
                Toast.makeText(LightSensorTestActivity.this, "摇一摇", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    private float[] accValues = new float[3];
    private float[] magValues = new float[3];
    private float lastRotateDegree;
    private SensorEventListener listener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            // 判断当前是加速度传感器还是地磁传感器
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
                // 注意赋值时要调用clone()方法
                accValues = event.values.clone();
            }else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
                magValues = event.values.clone();
            }
            float[] R = new float[9];
            float[] values = new float[3];
            SensorManager.getRotationMatrix(R, null, accValues, magValues);
            SensorManager.getOrientation(R, values);
            // 将计算出来的旋转角度取反，用于旋转指南针背景图
            float rotateDegree = -(float) Math.toDegrees(values[0]);
            if (Math.abs(rotateDegree - lastRotateDegree) > 5){
                RotateAnimation animation = new RotateAnimation(lastRotateDegree,
                        rotateDegree, Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);
                animation.setFillAfter(true);
                compassImg.startAnimation(animation);
                lastRotateDegree = rotateDegree;
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };
}
