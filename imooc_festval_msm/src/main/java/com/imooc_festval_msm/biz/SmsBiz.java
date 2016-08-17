package com.imooc_festval_msm.biz;

import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.telephony.SmsManager;

import com.imooc_festval_msm.bean.SendMsg;
import com.imooc_festval_msm.db.SmsProvider;

import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

/**
 * Created by yo on 2016/7/13.
 */
public class SmsBiz {
    private Context context;
    public SmsBiz(Context context){
        this.context = context;
    }
    public int sendMsg (String number, String msg, PendingIntent sendPi, PendingIntent deliverPi){
        SmsManager smsManager = SmsManager.getDefault();
        ArrayList<String> contents = smsManager.divideMessage(msg);
        for(String content : contents){
            smsManager.sendTextMessage(number, null, content, sendPi, deliverPi);
        }
        return contents.size();
    }
    public int sendMsg (Set<String> numbers, SendMsg msg, PendingIntent sendPi, PendingIntent deliverPi){
        save(msg);
        int result = 0;
        for(String number : numbers){
            int count = sendMsg(number, msg.getMsg(), sendPi, deliverPi);
            result += count;
        }
        return result;
    }
    private void save(SendMsg sendMsg){
        sendMsg.setDate(new Date());
        ContentValues values = new ContentValues();
        values.put(SendMsg.COLUMN_DATE, sendMsg.getDate().getTime());
        values.put(SendMsg.COLUMN_FESTIVAL_NAME, sendMsg.getFestivalName());
        values.put(SendMsg.COLUMN_MSG, sendMsg.getMsg());
        values.put(SendMsg.COLUMN_NAMES, sendMsg.getNames());
        values.put(SendMsg.COLUMN_NUMBER, sendMsg.getNumber());

        context.getContentResolver().insert(SmsProvider.URI_SMS_ALL, values);
    }
}
