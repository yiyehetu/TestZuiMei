package com.yph.beauty.bean;

/**
 * Created by yph
 * Time is 2016/12/8 18:09
 * Good Good Study, Day Day Up
 *
 * 设计类别
 */

public class DesignType {
    private String type;
    private int id;

    public DesignType() {
    }

    public DesignType(String type, int id) {
        this.type = type;
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "DesignType{" +
                "type='" + type + '\'' +
                ", id=" + id +
                '}';
    }
}
