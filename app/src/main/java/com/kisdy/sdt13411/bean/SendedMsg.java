package com.kisdy.sdt13411.bean;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by sdt13411 on 2015/12/8.
 */
public class SendedMsg {

    private int id;
    private String smsContent; //短信内容
    private String number;  //发送的号码拼接成的String
    private String names;   //发送的名字拼接成的String
    private String festivalName;
    private Date date;
    private String dateStr;
    private DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSmsContent() {
        return smsContent;
    }

    public void setSmsContent(String name) {
        this.smsContent = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getFestivalName() {
        return festivalName;
    }

    public void setFestivalName(String festivalName) {
        this.festivalName = festivalName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
        dateStr=dateFormat.format(date);
    }

    public String getDateDesc() {
        dateStr=dateFormat.format(date);
        return dateStr;
    }

    public void setDateDesc(String dateDesc) {
        this.dateStr = dateDesc;
    }

    public DateFormat getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(DateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }



}
