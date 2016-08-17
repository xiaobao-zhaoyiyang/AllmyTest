package com.imooc_update.update;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.imooc_update.R;

import java.io.File;

/**
 * app更新下载后台服务
 */
public class UpdateService extends Service {

    private String apkURL;
    private String filePath;
    private NotificationManager notificationManager;
    private Notification notification;
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("UpdateService", "onCreate");
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        filePath = Environment.getExternalStorageDirectory() + "/imooc/QjFund.apk";
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("UpdateService", "onStartCommand" + (intent == null ? true : false));
        if (intent == null){
            notifyUser(getString(R.string.update_download_failed),
                    getString(R.string.update_download_failed_msg), 0);
            stopSelf();
        }
        apkURL = intent.getStringExtra("apkUrl");
        notifyUser(getString(R.string.update_download_start),
                getString(R.string.update_download_start), 0);
        startDownload();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void startDownload() {
        UpdateManager.getInstance().startDownload(apkURL, filePath, new UpdateDownloadListener() {
            @Override
            public void onStarted() {
                Log.i("UpdateService", "startDownload_onStarted");

            }

            @Override
            public void onProgressChanged(int progress, String downloadUrl) {
                Log.i("UpdateService", "startDownload_onProgressChanged" + progress);
                notifyUser(getString(R.string.update_download_processing),
                        getString(R.string.update_download_processing), progress);
            }

            @Override
            public void onFinished(int mCompleteSize, String s) {
                Log.i("UpdateService", "startDownload_onFinished");
                notifyUser(getString(R.string.update_download_finish),
                        getString(R.string.update_download_finish), 100);
                stopSelf();
            }

            @Override
            public void onFailure() {
                Log.i("UpdateService", "startDownload_onFailure");
                notifyUser(getString(R.string.update_download_failed),
                        getString(R.string.update_download_failed_msg), 0);
                stopSelf();
            }
        });
    }

    // 更新我们的notification来告知用户当前下载的进度
    private void notifyUser(String result, String reason, int progress){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                        R.mipmap.ic_launcher))
                .setContentTitle(getString(R.string.app_name));
        if (progress > 0 && progress < 100){
            builder.setProgress(100, progress, false);
            builder.setContentText(progress + "%");
        }else if (progress >= 100){
            builder.setProgress(0, 0, false);
            builder.setContentText("下载完成");
            builder.setSubText("......");
        }
        builder.setAutoCancel(true);
        builder.setWhen(System.currentTimeMillis());
        builder.setTicker(result);
        builder.setContentIntent(progress >= 100 ?
                getContentIntent()
                : PendingIntent.getActivity(this, 0,
                new Intent(),
                PendingIntent.FLAG_CANCEL_CURRENT));
        notification = builder.build();
        notificationManager.notify(0, notification);
        Log.i("UpdateService", "notifyUser");
    }

    private PendingIntent getContentIntent(){
        File apkFile = new File(filePath);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.parse("file://" + apkFile.getAbsolutePath()),
                "application/vnd.android.package-archive");
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }

}
