package yeeaoo.imooc_mooo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import bean.ChatMessage;
import utils.HttpUtils;

public class MainActivity extends AppCompatActivity {
    private ListView mMsgs;
    private ChatMessageAdapter mAdapter;
    private List<ChatMessage> mDatas;
    private EditText mInputMsg;
    private Button mSendMsg;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ChatMessage fromMessage = (ChatMessage) msg.obj;
            mDatas.add(fromMessage);
            mAdapter.notifyDataSetChanged();
            mMsgs.setSelection(mDatas.size() - 1);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        initView();
        initDatas();
        initListener();
    }

    private void initListener() {
        mSendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String toMsg = mInputMsg.getText().toString();
                if (TextUtils.isEmpty(toMsg)){
                    Toast.makeText(MainActivity.this, "消息不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                ChatMessage toMessage = new ChatMessage();
                toMessage.setDate(new Date());
                toMessage.setMsg(toMsg);
                toMessage.setType(ChatMessage.Type.OUTCOMING);
                mDatas.add(toMessage);
                mAdapter.notifyDataSetChanged();
                mMsgs.setSelection(mDatas.size() - 1);
                mInputMsg.setText("");
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        ChatMessage fromMessage = HttpUtils.sendMessage(toMsg);
                        Message m = Message.obtain();
                        m.obj = fromMessage;
                        mHandler.sendMessage(m);
                    }
                }.start();

            }
        });
    }

    private void initDatas() {
        mDatas = new ArrayList<>();
        mDatas.add(new ChatMessage("你好，小慕为您服务", ChatMessage.Type.INCOMING, new Date()));
        mAdapter = new ChatMessageAdapter(this, mDatas);
        mMsgs.setAdapter(mAdapter);
    }

    private void initView() {
        mMsgs = (ListView) findViewById(R.id.id_listView);
        mInputMsg = (EditText) findViewById(R.id.id_intput_msg);
        mSendMsg = (Button) findViewById(R.id.id_send_msg);
    }
}
