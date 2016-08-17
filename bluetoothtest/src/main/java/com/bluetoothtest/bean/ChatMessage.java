package com.bluetoothtest.bean;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yo on 2016/7/19.
 */
public class ChatMessage {
    //主动发出的消息
    static public final int MSG_SENDER_ME = 0;
    //接收到的消息
    static public final int MSG_SENDER_OTHERS = 1;

    public int messageSender;
    public String messageContent;
    public String date;

    public int getMessageSender() {
        return messageSender;
    }

    public void setMessageSender(int messageSender) {
        this.messageSender = messageSender;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(System.currentTimeMillis()));
    }
}
