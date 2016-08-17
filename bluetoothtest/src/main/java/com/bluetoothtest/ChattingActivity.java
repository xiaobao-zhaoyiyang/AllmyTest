package com.bluetoothtest;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bluetoothtest.bean.ChatMessage;

/**
 * Created by yo on 2016/7/18.
 */
public class ChattingActivity extends AppCompatActivity {

    /**
     * 创建监听器
     * ConnectionManager定义了ConnectionListener接口，状态变化、数据的接收可以通过这个接口获得。创建一个ConnectionListener监听器，
     */
    private ConnectionManager.ConnectionListener mConnectionListener = new ConnectionManager.ConnectionListener() {

        @Override
        public void onConnectStateChange(int oldState, int State) {
            //连接状态的变化通知给UI线程，请UI线程处理
            Log.i("TAG", "连接状态 " + State);
            mHandler.obtainMessage(MSG_UPDATE_UI).sendToTarget();
        }

        @Override
        public void onListenStateChange(int oldState, int State) {
            //监听状态的变化通知给UI线程，请UI线程处理
            mHandler.obtainMessage(MSG_UPDATE_UI).sendToTarget();
        }

        @Override
        public void onSendData(boolean suc, byte[] data) {
            Log.i("TAG", "发送状态 " + suc);
            //将发送的数据交给UI线程，请UI线程处理
            mHandler.obtainMessage(MSG_SENT_DATA, suc?1:0, 0, data).sendToTarget();
        }

        @Override
        public void onReadData(byte[] data) {
            //将收到的数据交给UI线程，请UI线程处理
            mHandler.obtainMessage(MSG_RECEIVE_DATA,  data).sendToTarget();
        }

    };

    /**
     * 创建Handler
     * 因为监听器触发的函数不一定是在UI线程被调用的，例如onConnectStateChange()，所以不能在监听器当中对界面做修改，
     * 必须把界面更新的任务交给UI线程进行。
     */
    private final static int MSG_SENT_DATA = 0;
    private final static int MSG_RECEIVE_DATA = 1;
    private final static int MSG_UPDATE_UI = 2;

    //不使用参数创建Handler，说明这个Handler是给主线程服务的
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case MSG_SENT_DATA: {
                    //UI线程处理发送成功的数据，
                    //把文字内容展示到主界面上
                    //获取发送的文字内容
                    byte [] data = (byte []) msg.obj;
                    boolean suc = msg.arg1 == 1;
                    if(data != null && suc) {
                        //发送成功后创建消息
                        ChatMessage chatMsg = new ChatMessage();
                        chatMsg.messageSender = ChatMessage.MSG_SENDER_ME;
                        chatMsg.messageContent = new String(data);

                        //将消息展示到消息列表中
                        MessageAdapter adapter = (MessageAdapter) mListView.getAdapter();
                        adapter.add(chatMsg);
                        adapter.notifyDataSetChanged();

                        mText.setText("");
                    }
                }
                break;

                case MSG_RECEIVE_DATA: {
                    //UI线程处理接收到的对方发送的数据，
                    //把文字内容展示到主界面上
                    byte [] data = (byte []) msg.obj;
                    if(data != null) {

                        ChatMessage chatMsg = new ChatMessage();
                        chatMsg.messageSender = ChatMessage.MSG_SENDER_OTHERS;
                        chatMsg.messageContent = new String(data);

                        //将消息展示到消息列表中
                        MessageAdapter adapter = (MessageAdapter) mListView.getAdapter();
                        adapter.add(chatMsg);
                        adapter.notifyDataSetChanged();
                    }
                }
                break;

                case MSG_UPDATE_UI: {
                    //更新界面上的菜单等显示状态
                    //更新界面上的菜单等显示状态
//                    updateUI();
                }
                break;
            }
        }
    };

    private ConnectionManager mConnectionManager;
    private ListView mListView;
    private EditText mText;
    private Button mSend;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);
        String deviceAddr = getIntent().getStringExtra("DEVICE_ADDR");

        mListView = (ListView) findViewById(R.id.chat_listView);
        mText = (EditText) findViewById(R.id.chat_editText);
        mSend = (Button) findViewById(R.id.chat_send);

        mConnectionManager = new ConnectionManager(mConnectionListener);

        mConnectionManager.connect(deviceAddr);

        //开启监听  当聊天应用运行起来的时候，需要开启对其它蓝牙设备可能接入的监听，
        mConnectionManager.startListen();

        MessageAdapter adapter = new MessageAdapter(this, R.layout.item_to_msg, R.layout.item_from_msg);
        mListView.setAdapter(adapter);

        mSend.setOnClickListener(mSendClickListener);
    }

    /**
     * 发送按钮的监听
     */
    private View.OnClickListener mSendClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            //获取要发送的文字内容
            String content = mText.getText().toString();
            if(content != null) {
                content = content.trim();
                if(content.length() > 0) {
                    //利用ConnectionManager发送数据
                    if (mConnectionManager.getCurrentConnectState() != ConnectionManager.CONNECT_STATE_CONNECTED){
                        Toast.makeText(ChattingActivity.this, "正在连接...", Toast.LENGTH_SHORT).show();
                    }else {
                        boolean ret = mConnectionManager.sendData(content.getBytes());
                        if (!ret) {
                            Toast.makeText(ChattingActivity.this, R.string.send_fail, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        }
    };

    class MessageAdapter extends ArrayAdapter<ChatMessage> {

        private final LayoutInflater mInflater;
        private int mResourceMe;
        private int mResourceOthers;

        //要指定自己发送的消息显示用的布局－me_list_item，
        //以及对方发送消息显示用的布局－others_list_item
        public MessageAdapter(Context context, int resourceMe, int resourceOthers) {
            super(context, 0);
            mInflater = LayoutInflater.from(context);
            mResourceMe = resourceMe;
            mResourceOthers = resourceOthers;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ChatMessage message = getItem(position);
            //根据消息类型的不同，使用不同的布局作为消息项
            convertView = mInflater.inflate(message.messageSender == ChatMessage.MSG_SENDER_ME ? mResourceMe : mResourceOthers, parent, false);

            //显示消息的内容
            TextView content = (TextView) convertView.findViewById(R.id.id_msg_info);
            TextView date = (TextView) convertView.findViewById(R.id.id_msg_date);
            content.setText(message.messageContent);
            date.setText(message.getDate());

            return convertView;
        }
    }

    /**
     * 当应用退出的时候，要断开可能存在的连接并停止监听
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //移除Handler中可能存在的各种任务
        mHandler.removeMessages(MSG_UPDATE_UI);
        mHandler.removeMessages(MSG_SENT_DATA);
        mHandler.removeMessages(MSG_RECEIVE_DATA);

        //停止监听
        if(mConnectionManager != null) {
            mConnectionManager.disconnect();
            mConnectionManager.stopListen();
        }
    }
}
