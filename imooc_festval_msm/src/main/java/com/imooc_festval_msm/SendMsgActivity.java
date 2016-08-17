package com.imooc_festval_msm;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.imooc_festval_msm.bean.Festival;
import com.imooc_festval_msm.bean.FestivalLab;
import com.imooc_festval_msm.bean.Msg;
import com.imooc_festval_msm.bean.SendMsg;
import com.imooc_festval_msm.biz.SmsBiz;
import com.imooc_festval_msm.view.FlowLayout;

import java.util.HashSet;

import static com.imooc_festval_msm.R.id.id_layout_loading;

/**
 * Created by yo on 2016/7/13.
 */
public class SendMsgActivity extends AppCompatActivity {
    public static final String KEY_FESTIVAL_ID = "festivalId";
    public static final String KEY_MSG_ID = "msgId";
    private int mFestivalId;
    private int mMsgId;

    private Festival mFestival;
    private Msg mMsg;

    private EditText mEdMsg;
    private Button mBtnAdd;
    private FlowLayout mFlContacts;
    private FloatingActionButton mFabSend;
    private View mLayoutLoading;
    private static final int CODE_REQUEST = 1;

    private HashSet<String> mContactNames = new HashSet<>();
    private HashSet<String> mContactNum = new HashSet<>();

    private LayoutInflater mInflater;

    public static final String ACTION_SEND_MSG = "ACTION_SEND_MSG";
    public static final String ACTION_DELIVER_MSG = "ACTION_DELIVER_MSG";

    private PendingIntent mSendPi;
    private PendingIntent mDeliverPi;
    private BroadcastReceiver mSendBroadcastReceiver;
    private BroadcastReceiver mDeliverBroadcastReceiver;

    private SmsBiz mSmsBiz;

    private int mSendMsgCount;
    private int mTotalCount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_msg);
        mSmsBiz = new SmsBiz(this);
        initData();
        initView();
        initEvent();
        initReceiver();

    }

    private void initReceiver() {
        Intent sendIntent = new Intent(ACTION_SEND_MSG);
        mSendPi = PendingIntent.getBroadcast(this, 0, sendIntent, 0);
        Intent deliverIntent = new Intent(ACTION_DELIVER_MSG);
        mDeliverPi = PendingIntent.getBroadcast(this, 0, deliverIntent, 0);

        registerReceiver(mSendBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mSendMsgCount ++;
                if (getResultCode() == RESULT_OK){
                    Log.e("TAG", "短信发送成功 " + (mSendMsgCount + " / " + mTotalCount));
                }else{
                    Log.e("TAG", "短信发送失败");
                }
                Toast.makeText(SendMsgActivity.this, (mSendMsgCount + " / " + mTotalCount) + " 短信发送成功", Toast.LENGTH_SHORT).show();
                if (mSendMsgCount == mTotalCount){
                    finish();
                }
            }
        }, new IntentFilter(ACTION_SEND_MSG));

        registerReceiver(mDeliverBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                    Log.e("TAG", "联系人已经成功接收到短信");

            }
        }, new IntentFilter(ACTION_DELIVER_MSG));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mSendBroadcastReceiver);
        unregisterReceiver(mDeliverBroadcastReceiver);
    }

    private void initEvent() {
        mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, CODE_REQUEST);
            }
        });
        mFabSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContactNames.size() == 0) {
                    Toast.makeText(SendMsgActivity.this, "请先选择联系人", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mContactNum.size() == 0) {
                    Toast.makeText(SendMsgActivity.this, "联系人号码为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(mEdMsg.getText().toString())){
                    Toast.makeText(SendMsgActivity.this, "短信内容不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                mLayoutLoading.setVisibility(View.VISIBLE);
                mTotalCount = mSmsBiz.sendMsg(mContactNum, buildSendMsg(mEdMsg.getText().toString()), mSendPi, mDeliverPi);
                mSendMsgCount = 0;
            }
        });
    }

    private SendMsg buildSendMsg(String s) {
        SendMsg sendMsg = new SendMsg();
        sendMsg.setMsg(s);
        sendMsg.setFestivalName(mFestival.getName());
        String names = "";
        for (String name : mContactNames){
            names += name + ":";
        }
        sendMsg.setNames(names.substring(0, names.length() -1));
        String numbers = "";
        for (String number : mContactNum){
            numbers += number + ":";
        }
        sendMsg.setNumber(numbers.substring(0, numbers.length() -1));
        return sendMsg;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_REQUEST){
            if (resultCode == RESULT_OK){
                Uri contactUri = data.getData();
                Cursor cursor = getContentResolver().query(contactUri, null, null, null, null);
//                managedQuery(contactUri, null, null, null, null);
                cursor.moveToFirst();
                String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String number = getContactNumber(cursor);
                if (!TextUtils.isEmpty(number)){
                    mContactNames.add(contactName);
                    mContactNum.add(number);

                    addTag(contactName);
                }
            }
        }
    }

    private void addTag(String contactName) {
        TextView view = (TextView) mInflater.inflate(R.layout.tag, mFlContacts, false);
        view.setText(contactName);
        mFlContacts.addView(view);
    }

    private String getContactNumber(Cursor cursor) {
        int numberCount = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
        String number = null;
        if (numberCount > 0){
            int contactId = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            Cursor phoneCursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId, null, null);
            phoneCursor.moveToFirst();
            number = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            Log.i("TAG", "NUmber = " + number);
            phoneCursor.close();
            return number;
        }
        cursor.close();
        return number;
    }

    private void initView() {
        mInflater = LayoutInflater.from(this);
        mEdMsg = (EditText) findViewById(R.id.id_et_content);
        mBtnAdd = (Button) findViewById(R.id.id_but_add);
        mFlContacts = (FlowLayout) findViewById(R.id.id_fl_contact);
        mFabSend = (FloatingActionButton) findViewById(R.id.id_fab_send);
        mLayoutLoading = findViewById(id_layout_loading);
        mLayoutLoading.setVisibility(View.GONE);
        if (mMsgId != -1){
            mMsg = FestivalLab.getInstance().getMsgById(mMsgId);
            mEdMsg.setText(mMsg.getContent());
            mEdMsg.setSelection(mMsg.getContent().length());
        }
    }

    private void initData() {
        mFestivalId = getIntent().getIntExtra(KEY_FESTIVAL_ID, -1);
        mMsgId = getIntent().getIntExtra(KEY_MSG_ID, -1);

        mFestival = FestivalLab.getInstance().getFestivalById(mFestivalId);
        setTitle(mFestival.getName());
    }

    public static void toActivity(Context context, int festivalId, int msgId){
        Intent intent = new Intent(context, SendMsgActivity.class);
        intent.putExtra(KEY_FESTIVAL_ID, festivalId);
        intent.putExtra(KEY_MSG_ID, msgId);
        context.startActivity(intent);
    }
}
