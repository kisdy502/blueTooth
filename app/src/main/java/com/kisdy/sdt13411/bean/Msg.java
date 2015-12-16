package com.kisdy.sdt13411.bean;

/**
 * 短信
 * Created by sdt13411 on 2015/12/3.
 */
public class Msg {
    private int msgId;
    private int festivalId;

    public Msg(int msgId, int festivalId, String content) {
        this.msgId = msgId;
        this.festivalId = festivalId;
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    public int getFestivalId() {
        return festivalId;
    }

    public void setFestivalId(int festivalId) {
        this.festivalId = festivalId;
    }

    private String content;
}
