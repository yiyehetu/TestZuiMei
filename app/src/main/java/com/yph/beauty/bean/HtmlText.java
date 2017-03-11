package com.yph.beauty.bean;

/**
 * Created by yph
 * 2017/3/8 15:33
 * Learn and live
 */

public class HtmlText {
    public enum HtmlType{
        TEXT,
        IMAGE;
    }

    private String html;
    private HtmlType type;
    private int imgPositon;

    public HtmlText() {
    }

    public HtmlText(String html, HtmlType type) {
        this.html = html;
        this.type = type;
    }

    public HtmlText(String html, HtmlType type, int imgPositon) {
        this.html = html;
        this.type = type;
        this.imgPositon = imgPositon;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public HtmlType getType() {
        return type;
    }

    public void setType(HtmlType type) {
        this.type = type;
    }

    public int getImgPositon() {
        return imgPositon;
    }

    public void setImgPositon(int imgPositon) {
        this.imgPositon = imgPositon;
    }

    @Override
    public String toString() {
        return "HtmlText{" +
                "html='" + html + '\'' +
                ", type=" + type +
                ", imgPositon=" + imgPositon +
                '}';
    }
}
