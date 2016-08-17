package tool;

import android.util.Log;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by yo on 2016/8/1.
 */
public class SimpleHttpServer {
    private final WebConfiguration configruation;
    private boolean isEnable;
    private InetSocketAddress socketAddress;
    private ServerSocket socket;
    private ExecutorService threadPool;

    public SimpleHttpServer(WebConfiguration configuration){
        this.configruation = configuration;
    }

    /**
     * 启动Server(异步)
     */
    public void startSync(){
        isEnable = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                doProSync();
            }
        }).start();
    }

    /**
     * 停止Server(异步)
     */
    public void stopSync(){
        if (!isEnable) return;
        isEnable = false;
        try {
            socket.close();
            socket = null;
        } catch (IOException e) {
            Log.d("zyy...stopSync", e.toString());
        }
    }

    private void doProSync() {
        try {
            Log.d("zyy...doProSync", "...1" );
            socketAddress = new InetSocketAddress(configruation.getPort());
            Log.d("zyy...doProSync", "...2" );
            socket = new ServerSocket();
            Log.d("zyy...doProSync", "...3" );
            socket.bind(socketAddress);
            Log.d("zyy...doProSync", "...4" );
            while (isEnable){
                final Socket remotePeer = socket.accept();
                threadPool = Executors.newCachedThreadPool();
                threadPool.submit(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("zyy", "a remote peer accepted..." + remotePeer.getRemoteSocketAddress().toString());
                        onAcceptRemotePeer(remotePeer);
                    }
                });
            }
        } catch (IOException e) {
            Log.d("zyy...doProSync", e.toString());
        }
    }

    private void onAcceptRemotePeer(Socket remotePeer) {
        try {
            remotePeer.getOutputStream().write("configurations connected successful".getBytes());
        } catch (IOException e) {
            Log.d("zyy...onAccept", e.toString());
        }
    }
}
