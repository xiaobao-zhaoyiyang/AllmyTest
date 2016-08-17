package com.imooc_update.update;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;

/**
 * 处理文件的下载和线程中的通信
 */
public class UpdateDownloadRequest implements Runnable {

    private String downloadUrl; // 下载路径
    private String localFilePath; // 文件下载位置
    private UpdateDownloadListener downloadListener; // 下载监听
    private long currentLength;
    private boolean isDownloading = false;

    private DownloadResponseHandler downloadResponseHandler;

    public UpdateDownloadRequest(String downloadUrl, String localFilePath, UpdateDownloadListener downloadListener) {
        this.downloadUrl = downloadUrl;
        this.localFilePath = localFilePath;
        this.downloadListener = downloadListener;
        this.isDownloading = true;
    }

    @Override
    public void run() {
        try {
            makeRequest();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // 真正的去建立连接的方法
    private void makeRequest() throws IOException, InterruptedException {
        Log.i("Download", "..........");
        if (!Thread.currentThread().isInterrupted()) {
            try {
                Log.i("Download", "..........建立连接开始");
                URL url = new URL(downloadUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.connect(); // 阻塞当前的线程
                currentLength = connection.getContentLength();
                if (!Thread.currentThread().isInterrupted()) {
                    Log.i("Download", "开始下载");
                    // 真正完成文件的下载
                    downloadResponseHandler = new DownloadResponseHandler();
                    downloadResponseHandler.sendResponseMessage(connection.getInputStream());
                }
            } catch (IOException e) {
                Log.i("Download", e.toString());
                throw e;
            }
        }
    }

    /**
     * 格式化数字
     *
     * @param value
     * @return
     */
    private String getTwoPointFloatStr(float value) {
        DecimalFormat fnum = new DecimalFormat("0.00");
        return fnum.format(value);
    }

    /**
     * 包含了下载过程中所有可能出现的异常情况
     */
    public enum FailureCode {
        UnknownHost, Socket, SocketTimeout, ConnectTimeout,
        IO, HttpResponse, JSON, Interrupted
    }

    /**
     * 用来真正的去下载文件，并发送消息和回调的接口
     */
    public class DownloadResponseHandler {
        protected static final int SUCCESS_MESSAGE = 0;
        protected static final int FAILURE_MESSAGE = 1;
        protected static final int START_MESSAGE = 2;
        protected static final int FINISH_MESSAGE = 3;
        protected static final int NETWORK_OFF = 4;
        protected static final int PROGRESS_CHANGED = 5;

        private int mCompleteSize = 0;
        private int progress = 0;

        private Handler handler; // 真正的完成线程间的通信

        public DownloadResponseHandler() {
            handler = new Handler(Looper.getMainLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    handlerSelfMessage(msg);
                }
            };
        }

        /**
         * 用来发送不同的消息对象
         */
        protected void sendFinishMessage() {
            sendMessage(obtainMessage(FINISH_MESSAGE, null));
        }

        protected void sendProgressChangedMessage(int progress) {
            sendMessage(obtainMessage(PROGRESS_CHANGED, new Object[]{progress}));
        }

        protected void sendFailureMessage(FailureCode failureCode) {
            sendMessage(obtainMessage(FAILURE_MESSAGE, new Object[]{failureCode}));
        }

        protected void sendMessage(Message msg) {
            if (handler != null) {
                handler.sendMessage(msg);
            } else {
                handlerSelfMessage(msg);
            }
        }

        /**
         * 获取一个消息对象
         *
         * @param responseMessage
         * @param objects
         * @return
         */
        private Message obtainMessage(int responseMessage, Object[] objects) {
            Message msg = null;
            if (handler != null) {
                msg = handler.obtainMessage(responseMessage, objects);
            } else {
                msg = Message.obtain();
                msg.what = responseMessage;
                msg.obj = objects;
            }
            return msg;
        }

        protected void handlerSelfMessage(Message msg) {
            Object[] response;
            switch (msg.what) {
                case FAILURE_MESSAGE:
                    response = (Object[]) msg.obj;
                    handleFailureMessage((FailureCode) response[0]);
                    break;
                case PROGRESS_CHANGED:
                    response = (Object[]) msg.obj;
                    int progress = (int) response[0];
                    Log.i("Request", "progress:" + progress);
                    handleProgressChangedMessage(progress);
                    break;
                case FINISH_MESSAGE:
                    onFinish();
                    break;
            }
        }

        /**
         * 各种消息的处理逻辑
         */
        protected void handleProgressChangedMessage(int progress) {
            downloadListener.onProgressChanged(progress, downloadUrl);
        }

        protected void handleFailureMessage(FailureCode failureCode) {
            onFailure(failureCode);
        }

        // 外部接口的回调
        public void onFinish() {
            downloadListener.onFinished(mCompleteSize, "");
        }

        public void onFailure(FailureCode failureCode) {
            downloadListener.onFailure();
        }

        // 文件 下载方法,会发生各种类型的事件
        void sendResponseMessage(InputStream is) {
            Log.i("SendResponsemessage", "loading");
            RandomAccessFile randomAccessFile = null;
            mCompleteSize = 0;

            try {
                byte[] buffer = new byte[1024];
                int length = -1;
                int limit = 0;
                randomAccessFile = new RandomAccessFile(localFilePath, "rwd");
                while ((length = is.read(buffer)) != -1) {
                    if (isDownloading) {
                        randomAccessFile.write(buffer, 0, length);
                        mCompleteSize += length;
                        if (mCompleteSize < currentLength) {
                            progress = (int) (Float.parseFloat(
                                    getTwoPointFloatStr(mCompleteSize)) / currentLength * 100);
                            if (limit % 30 == 0 && progress <= 100) {
                                // 为了限制一下notification的更新频率
//                                Log.i("SendResponsemessage", "loading....progress" + progress);
                                sendProgressChangedMessage(progress);
                            }
                            limit++;
                        }
                    }
                }
                sendFinishMessage();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                sendFailureMessage(FailureCode.IO);
            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                    if (randomAccessFile != null) {
                        randomAccessFile.close();
                    }
                } catch (IOException e) {
                    sendFailureMessage(FailureCode.IO);
                }
            }


        }
    }
}
