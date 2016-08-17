package com.bluetoothtest;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

/**
 * Created by yo on 2016/7/18.
 */
public class ConnectionManager {

    public static final String BT_NAME = "AnddleChat";
    public static UUID BT_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    //监听的两种状态
    public static final int LISTEN_STATE_IDLE = 3;
    public static final int LISTEN_STATE_LISTENING = 4;
    //记录当前监听的状态
    private int mListenState = LISTEN_STATE_IDLE;

    //连接的三种状态
    public static final int CONNECT_STATE_IDLE = 0;
    public static final int CONNECT_STATE_CONNECTING = 1;
    public static final int CONNECT_STATE_CONNECTED = 2;

    //记录当前连接的状态
    private int mConnectState = CONNECT_STATE_IDLE;

    private ConnectionListener mConnectionListener;

    private AcceptThread mAcceptThread;
    private ConnectedThread mConnectedThread;
    private final BluetoothAdapter mBluetoothAdapter;

    private BluetoothDevice mDevice;

    //定义监听器
    public interface ConnectionListener {

        /*当ConnectionManager的连接状态发生变化,通过onConnectStateChange()将变化通知到ChatActivity*/
        public void onConnectStateChange(int oldState, int State);

        //当ConnectionManager的监听状态发生变化,通过onListenStateChange()将变化通知到ChatActivity
        public void onListenStateChange(int oldState, int State);

        //当ConnectionManager的数据发送完成后，通过onSendData()将发送的内容通知到ChatActivity
        public void onSendData(boolean suc, byte[] data);

        //当ConnectionManager的接收到对方连接设备传来的数据，通过onReadData()将传来的数据通知到ChatActivity
        public void onReadData(byte [] data);
    }

    //构造函数中设置监听器
    public ConnectionManager(ConnectionListener cl) {
        mConnectionListener = cl;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public void startListen() {
        //创建监听线程
        if(mAcceptThread != null) {
            mAcceptThread.cancel();
        }

        mAcceptThread = new AcceptThread();
        mAcceptThread.start();
    }

    public void stopListen() {
        //停止监听线程
        if(mAcceptThread != null) {
            mAcceptThread.cancel();
        }
    }

    public void connect(String deviceAddr) {
        Log.i("TAG", "发起连接");
        //发起连接
        if(mConnectedThread != null) {
            mConnectedThread.cancel();
        }

        mDevice = mBluetoothAdapter.getRemoteDevice(deviceAddr);

        try {
            Log.i("TAG", "创建发起主动连接使用的Socket");
            //创建发起主动连接使用的Socket
            BluetoothSocket socket = mDevice.createRfcommSocketToServiceRecord(BT_UUID);
            Log.i("TAG", "启动连接线程");
            //启动连接线程
            mConnectedThread = new ConnectedThread(socket, true);
            mConnectedThread.start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void disconnect() {
        //停止连接线程
        if(mConnectedThread != null) {
            mConnectedThread.cancel();
        }
    }

    public int getCurrentListenState() {
        //查询当前监听线程的状态
        return mListenState;
    }

    public int getCurrentConnectState() {
        //查询当前连接线程的状态
        return mConnectState;
    }

    public boolean sendData(byte[] data) {
        Log.i("TAG", "发送 " + "ConnectionManager : sendData");
        //发送数据
        if(mConnectedThread != null
                && mConnectState == CONNECT_STATE_CONNECTED) {
            mConnectedThread.sendData(data);

            return true;
        }
        return false;
    }




    class AcceptThread extends Thread{

        private BluetoothServerSocket mServerSocket;
        private boolean mUserCancel;

        public AcceptThread() {
            BluetoothServerSocket tmp = null;
            mUserCancel = false;

            //创建监听用的ServerSocket
            try {
                tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(BT_NAME, BT_UUID);
            } catch (IOException e) {

            }
            mServerSocket = tmp;
        }
        //监听线程开始运行
        @Override
        public void run() {

            setName("AcceptThread");

            //将ConnectionManger监听的状态设置成“正在监听”
            setListenState(LISTEN_STATE_LISTENING);

            BluetoothSocket socket = null;

            while(!mUserCancel) {
                try {
                    //阻塞在这里，等待别的设备连接
                    socket = mServerSocket.accept();

                } catch (IOException e) {
                    //阻塞过程中，如果其它地方调用了mServerSocket.close()，
                    //将会进入到这个异常当中
                    mServerSocket = null;
                    break;
                }

                if(mConnectState == CONNECT_STATE_CONNECTED
                        || mConnectState == CONNECT_STATE_CONNECTING) {
                    //如果当前正在连接别的设备，
                    //或者已经和别的设备连接上了，就放弃这个连接，
                    //因为每次只能和一个设备连接
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else if(mConnectState == CONNECT_STATE_IDLE) {
                    //如果当前没有和别的设备连接上，
                    //启动连接线程
                    mConnectedThread = new ConnectedThread(socket, false);
                    mConnectedThread.start();
                }
            }

            if(mServerSocket != null) {
                try {
                    mServerSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mServerSocket = null;
            }
            setListenState(LISTEN_STATE_IDLE);
            mAcceptThread = null;
        }

        //让监听线程退出运行
        public void cancel() {
            try {
                mUserCancel = true;
                //ServerSocket此时阻塞在accept()方法中，
                //关闭之后，会让accept()方法抛出异常，实现监听线程的退出
                if (mServerSocket != null) {
                    mServerSocket.close();
                }
            } catch (IOException e) {
            }
        }


        //修改当前监听的状态
        private void setListenState(int state) {

            //状态没有发生变化，不用通知
            if (mListenState == state) {
                return;
            }

            int oldState = mListenState;
            mListenState = state;

            //状态发生变化，发起通知
            if (mConnectionListener != null) {
                mConnectionListener.onListenStateChange(oldState, mListenState);
            }
        }
    }

    class ConnectedThread extends Thread{
        private final int MAX_BUFFER_SIZE = 1024;

        private BluetoothSocket mSocket;
        private InputStream mInStream;
        private OutputStream mOutStream;
        private boolean mUserCancel;
        private boolean mNeedConnect;
        public ConnectedThread(BluetoothSocket socket, boolean needConnect){
//        public ConnectedThread(String addr, boolean needConnect){
            Log.i("TAG", "连接线程中");

//            try {
                //保存下工作线程中要用到的参数
                setName("ConnectedThread");
                mNeedConnect = needConnect;
                mSocket = socket;
//                mDevice = mBluetoothAdapter.getRemoteDevice(addr);
//                mSocket = mDevice.createRfcommSocketToServiceRecord(BT_UUID);
                mUserCancel = false;
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }

        @Override
        public void run() {

            //将ConnectionManager的连接状态修改成CONNECT_STATE_CONNECTING
            setConnectState(CONNECT_STATE_CONNECTING);

            //如果这是一个主动连接，说明Socket还没有和对方相连接，就需要发起主动连接
            if(mNeedConnect && !mUserCancel) {
                try {
                    Log.i("TAG", "连接线程中....mSocket.connect();");
                    mSocket.connect();
                } catch (IOException e) {
                    Log.i("TAG", "连接线程中....发生异常 " + e.toString());
                    try {
                        mSocket =(BluetoothSocket) mDevice.getClass().getMethod("createRfcommSocket", new Class[] {int.class}).invoke(mDevice,1);
                        mSocket.connect();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    //主动连接发生异常，回到未连接的状态
//                    setConnectState(CONNECT_STATE_IDLE);
//                    mSocket = null;
//                    mConnectedThread = null;

//                    return;
                }
            }

            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            //从连接的Socket中获取读数据和写数据的流接口
            try {
                Log.i("TAG", "连接线程中....从连接的Socket中获取读数据和写数据的流接口");
                tmpIn = mSocket.getInputStream();
                tmpOut = mSocket.getOutputStream();
            } catch (IOException e) {
                setConnectState(CONNECT_STATE_IDLE);
                mSocket = null;
                mConnectedThread = null;

                return;
            }

            mInStream = tmpIn;
            mOutStream = tmpOut;

            //将ConnectionManager的连接状态修改成CONNECT_STATE_CONNECTED
            setConnectState(CONNECT_STATE_CONNECTED);

            byte[] buffer = new byte[MAX_BUFFER_SIZE];
            int bytes;

            while (!mUserCancel) {
                try {
                    //阻塞在这里，用流接口等待读取数据
                    bytes = mInStream.read(buffer);
                    //将读取到的数据传递给关注它的组件
                    if(mConnectionListener != null && bytes > 0) {

                        byte [] data = new byte[bytes];
                        System.arraycopy(buffer, 0, data, 0, bytes);
                        mConnectionListener.onReadData(data);
                    }
                } catch (IOException e) {
                    //阻塞过程中，如果其它地方调用了mSocket.close()，
                    //或者对方的连接关闭
                    //将会进入到这个异常当中
                    break;
                }
            }

            setConnectState(CONNECT_STATE_IDLE);
            mSocket = null;
            mConnectedThread = null;
        }

        //让连接线程退出运行
        public void cancel() {
            try {
                mUserCancel = true;
                //Socket此时阻塞在InputStream的read()方法中，
                //关闭之后，会让read()方法抛出异常
                if(mSocket != null) {
                    mSocket.close();
                }

            } catch (IOException e) {
            }
        }

        //向对方发送数据
        public void sendData(byte[] data) {
            try {
                Log.i("TAG", "连接线程中....sendData .... ");
                //用流接口发送数据
                mOutStream.write(data);
                //向关心的组件通知发送成功
                if(mConnectionListener != null) {
                    mConnectionListener.onSendData(true, data);
                }
            } catch (IOException e) {
                Log.i("TAG", "连接线程中....sendData .... " + e.toString());
                //向关心的组件通知发送失败
                if(mConnectionListener != null) {
                    mConnectionListener.onSendData(false, data);
                }
            }
        }
    }

        //修改当前连接的状态
        private void setConnectState(int state) {

            ///状态没有发生变化，不用通知
            if(mConnectState == state) {
                return;
            }

            int oldState = mConnectState;
            mConnectState = state;

            //状态发生变化，发起通知
            if(mConnectionListener != null) {
                mConnectionListener.onConnectStateChange(oldState, mConnectState);
            }
        }

}
