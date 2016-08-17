package tools;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import yeeaoo.mytest.MainActivity;
import yeeaoo.mytest.R;

/**
 * Created by yo on 2016/6/14.
 */
public class MyService extends Service {
    private DownloadBinder mBinder = new DownloadBinder();
    public class DownloadBinder extends Binder{
        public void startDownload(){
            Log.d("MyService", "startDownload executed");
        }
        public int getProgress(){
            Log.d("MyService", "getProgress executed");
            return 0;
        }
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("Notification comes");
        builder.setContentText("Time:" + System.currentTimeMillis());
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        builder.setContentIntent(pendingIntent);
        startForeground(1, builder.build());
        Log.d("MyService", "onCreate executed");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("MyService", "onStartCommand executed");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("MyService", "onDestroy executed");
    }
}
