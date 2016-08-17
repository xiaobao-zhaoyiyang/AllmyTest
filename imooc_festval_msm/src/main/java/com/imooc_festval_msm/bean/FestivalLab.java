package com.imooc_festval_msm.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yo on 2016/7/12.
 */
public class FestivalLab {
    public static FestivalLab mInstance;

    private List<Festival> mFestivals = new ArrayList<>();
    private List<Msg> mMsgs = new ArrayList<>();

    private FestivalLab(){
        mFestivals.add(new Festival(1, "国庆节"));
        mFestivals.add(new Festival(2, "中秋节"));
        mFestivals.add(new Festival(3, "元旦"));
        mFestivals.add(new Festival(4, "春节"));
        mFestivals.add(new Festival(5, "端午节"));
        mFestivals.add(new Festival(6, "七夕节"));
        mFestivals.add(new Festival(7, "圣诞节"));
        mFestivals.add(new Festival(8, "儿童节"));

        mMsgs.add(new Msg(1, 1,
                "月光静悄悄，秋阳黄橙橙;灯笼红彤彤，佳节喜盈盈;心情美滋滋，祝福乐淘淘。国庆佳节，问候送到，愿你幸福，一切都好!"));
        mMsgs.add(new Msg(2, 1,
                "白云是属于蓝天的，繁星是属于夜空的，叶子是属于泥土的，而我的祝福是属于你的，国庆节就要到了，短信送上问候，提前祝你长假愉快!"));
        mMsgs.add(new Msg(3, 1,
                "不管天多高，海多深，钢多硬，风多大，尺多长，河多宽，酒多烈，冰多冷，火多热。亲~我只想告诉你，这些都不关你的事!十一快乐!"));
        mMsgs.add(new Msg(4, 1,
                "国富安康和谐家，中华盛世平天下，年年度度国庆日，时时刻刻喜庆时，分分秒秒送祝愿，全家欢乐无边，全天幸福欢颜，全民国庆快乐!"));
        mMsgs.add(new Msg(5, 1,
                "国庆到，送你一张信用卡，温暖随便刷，幸福自由取，快乐任你抓，好运挑着拿，美女任你挎，钞票随你花，取之不尽，用之不竭，祝你国庆节快乐!"));
        mMsgs.add(new Msg(6, 1,
                "国庆到来举国欢，红灯高挂张灯彩。锣鼓喧天鞭炮鸣，庆祝祖国生日来。齐心协力建国家，不畏辛苦不畏难。祝愿祖国永昌盛，民富国强立世界。国庆快乐!"));
        mMsgs.add(new Msg(7, 1,
                "国庆的祝福最“十”在：“十”指相扣心中所爱，朋友亲人“十”意相待，前程似海“十”破天开，永远没有“十”面伏埋。国庆快乐!"));
        mMsgs.add(new Msg(8, 1,
                "国庆佳节到，祝福来报到，盛放的灿烂礼花是我对你的祝福，祝你青春绽放生活倍儿棒;飘荡的缤纷彩带是我对你的祝愿，愿你梦想飞翔快乐无疆!"));
        mMsgs.add(new Msg(9, 1,
                "国庆佳节又来到，向你问个好;忙碌疲倦全丢掉，满脸都是笑;快乐悠闲来报到，赶走烦和恼;思念随风越天涯，祝福我送到;健康永远把你抱，幸福将你绕!"));
    }

    public List<Msg> getMsgByFestivalId(int fesId){
        List<Msg> msgs = new ArrayList<>();
        for (Msg msg : mMsgs) {
            if (msg.getFestivalId() == fesId){
                msgs.add(msg);
            }
        }
        return msgs;
    }

    public Msg getMsgById(int id){
        for (Msg msg : mMsgs) {
            if (msg.getId() == id){
                return msg;
            }
        }
        return null;
    }
    public List<Festival> getFestivals(){
        return new ArrayList<>(mFestivals);
    }

    public Festival getFestivalById(int fesId){
        for (Festival festival : mFestivals) {
            if (festival.getId() == fesId){
                return festival;
            }
        }
        return null;
    }

    public static FestivalLab getInstance(){
        if (mInstance == null){
            synchronized (FestivalLab.class){
                if (mInstance == null){
                    mInstance = new FestivalLab();
                }
            }
        }
        return mInstance;
    }

}
