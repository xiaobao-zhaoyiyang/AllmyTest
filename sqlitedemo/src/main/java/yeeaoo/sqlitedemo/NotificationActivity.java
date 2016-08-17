package yeeaoo.sqlitedemo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.app.NotificationCompat.Builder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by yo on 2016/4/14.
 */
public class NotificationActivity extends AppCompatActivity implements View.OnClickListener {
    private NotificationManager manager;
    private int notification_ID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_layout);
        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        findViewById(R.id.bt_send).setOnClickListener(this);
        findViewById(R.id.bt_cancle).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_send:
                sendNotification();
                break;
            case R.id.bt_cancle:
                manager.cancel(notification_ID);
                break;
        }
    }

    /**
     * 构造notification并发送
     */
    private void sendNotification(){
        Intent intent = new Intent(this, NotificationActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
        Builder builder = new Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher); // 图标
        builder.setTicker("hello"); // 手机状态栏提示
        builder.setWhen(System.currentTimeMillis()); // 时间
        builder.setContentTitle("通知栏通知"); // 标题
        builder.setContentText("我来自NotificationDemo"); // 内容
        builder.setContentIntent(pIntent);// 点击后的意图
//        builder.setDefaults(Notification.DEFAULT_SOUND);// 提示声音
//        builder.setDefaults(Notification.DEFAULT_LIGHTS);// 提示指示灯
//        builder.setDefaults(Notification.DEFAULT_VIBRATE);// 震动效果
        builder.setDefaults(Notification.DEFAULT_ALL);// 三种效果
        Notification notification = builder.build(); // 4.1以上
        notification.flags = Notification.FLAG_AUTO_CANCEL; //设置点击通知后自动取消
        manager.notify(notification_ID, notification);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings:
                Toast.makeText(NotificationActivity.this, "setting", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onContextItemSelected(item);
    }
}
