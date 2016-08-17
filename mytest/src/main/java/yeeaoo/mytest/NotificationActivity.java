package yeeaoo.mytest;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TextView;

/**
 * 适配通知样式
 * 注意：使用此方法时，Activity不能继承自AppCompatActivity（实测4.4可以，5.1不行），
 * 因为ImageView已经被替换成AppCompatImageView，而AppCompatImageView的setBackgroundResource(int)
 * 未被标记为RemotableViewMethod，导致apply时抛异常。
 * 额，暂未找到好的解决办法。端午节后试试根据鸿神的方法 探究 LayoutInflater setFactory 应该可以解决。
 */
public class NotificationActivity extends Activity implements View.OnClickListener{
    private Button bt_default, bt_custom;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        initView();
        setClick();


    }
    private void initView() {
        bt_default = (Button) findViewById(R.id.bt_default);
        bt_custom = (Button) findViewById(R.id.bt_custom);
    }
    private void setClick() {
        bt_default.setOnClickListener(this);
        bt_custom.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_default:
                sendDefaultNotification();
                break;
            case R.id.bt_custom:
                sendCustomNotification();
                break;
        }
    }

    /**
     * 发送默认通知
     */
    private void sendDefaultNotification(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("这是标题 ");
        builder.setContentText("这是内容");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setShowWhen(false); // 不显示时间
        builder.setOngoing(true); // 常驻通知
        builder.setAutoCancel(true); // 点击后自动清除
        // 如果不设置LargeIcon，小图标会显示在大图标的位置上
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        // 点击处理
        Intent intent = new Intent(this, DBTestActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        builder.setContentIntent(pendingIntent);

        builder.setDefaults(android.support.v4.app.NotificationCompat.DEFAULT_ALL);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1, builder.build());
    }

    /**
     * 发送自定义通知
     */
    private void sendCustomNotification(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher); // 必须要设置

        // 点击处理, 2.3及以下系统必须要设置
//        Intent intent = new Intent(this, DBTestActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
//        builder.setContentIntent(pendingIntent);

        RemoteViews remoteViews;
        if (isDarkNotificationBar()){
            remoteViews = new RemoteViews(getPackageName(), R.layout.custom_notify);
        }else{
            remoteViews = new RemoteViews(getPackageName(), R.layout.custom_notify);
        }
        builder.setContent(remoteViews);

        Intent intent = new Intent(this, DBTestActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        builder.setContentIntent(pendingIntent);

        remoteViews.setOnClickPendingIntent(R.id.id_custom_notifyButton, pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1, builder.build());
    }

    private boolean isDarkNotificationBar(){
        return !isColorSimilar(Color.parseColor("#000000"), getNotificationColor());
    }



    /**
     * 现在切入主题，谈谈如何来更好的适配自定义通知。有过锁屏开发经验的人应该知道，
     * 如果你的应用有读取系统通知栏的权限，那么每当应用程序发出一个通知，
     * 你的应用都会收到对应的notification对象，这个时候，我们一般会执行以下操作：
     *  // 获取到app发来的notification对象
     *   Notification notification = getReceivedNotification();
     *
     * 将notification的布局设置到notificationItemLayout，简单的说就是将app发来的默认通知
     * 或是自定义通知的布局文件的跟View作为子元素添加到notificationItemLayout
     *
     * View notificationItemLayout = notification.contentView.apply(this, new LinearLayout(this));
     * mNoticationContainer.addView(notificationItemLayout);
     */

    /**
     * notificationItemLayout本身就是带有样式的，即便是默认通知。那么方案来了！我们先构造一个默认通知
     */
    /**
     * 获取通知栏title的颜色
     */
    private int getNotificationColor(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        Notification notification = builder.build();
        ViewGroup viewGroup = (ViewGroup) notification.contentView.apply(this, new LinearLayout(this));
        TextView title = (TextView) viewGroup.findViewById(android.R.id.title);
        return title.getCurrentTextColor();
    }
    /**
     * 通知并不发送出去，只是用来获取通知栏title的颜色，如果你还想获取content的颜色，抱歉，
     * 不能通过查找android.R.id.text来获取，这个字段是访问不到的。可通过反射获取，
     * 也可预先设置一个content，然后遍历viewGroup根据content内容找到对应的TextView再获取颜色。
     * 拿到颜色后，可根据算法判断这个颜色是近似白色还是近似黑色，我们使用黑色作为基准色，
     * 使用方差来计算这个颜色是否近似黑色：
     */
    /**
     * baseColor传入Color.parseColor("#000000")，
     * color传入刚刚获取到的title的颜色，
     * 根据我实测，阈值为180.0较为合理。
     * 上述方法返回true，即表示title的颜色近似黑色，也就是说通知栏背景近似白色。
     */
    private static final double COLOR_THRESHOLD = 180.0;
    public static boolean isColorSimilar(int baseColor, int color){
        int simpleBaseColor = baseColor | 0xff000000;
        int simpleColor = color | 0xff000000;
        int baseRed = Color.red(simpleBaseColor) - Color.red(simpleColor);
        int baseGreen = Color.green(simpleBaseColor) - Color.green(simpleColor);
        int baseBlue = Color.blue(simpleBaseColor) - Color.blue(simpleColor);
        double value = Math.sqrt(baseRed * baseRed + baseGreen * baseGreen + baseBlue * baseBlue);
        if (value < COLOR_THRESHOLD){
            return true;
        }
        return false;
    }
}
