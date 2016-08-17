package com.imooc_update.update;

/**
 * Created by yo on 2016/8/8.
 */
public interface UpdateDownloadListener {
    void onStarted();
    void onProgressChanged(int progress, String downloadUrl);
    void onFinished(int mCompleteSize, String downloadUrl);
    void onFailure();
}
