package com.kisdy.sdt13411.bean;

import java.util.Date;

/**
 * Created by sdt13411 on 2015/12/3.
 */
public class FestivalBean {
    private int id;
    private String name;
    private String decs;
    private Date date;

    public FestivalBean() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FestivalBean(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
