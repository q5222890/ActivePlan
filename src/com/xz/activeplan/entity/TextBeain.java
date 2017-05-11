package com.xz.activeplan.entity;

/**
 * Created by Administrator on 2016/6/1.
 */
public class TextBeain {
    private int i;
    private String content;
    private String title;

    public TextBeain(String content, String title, int i) {
        this.content = content;
        this.title = title;
        this.i = i;
    }

    public TextBeain() {
    }

    public TextBeain(int i, String content) {
        this.i = i;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return "TextBeain{" +
                "i=" + i +
                ", content='" + content + '\'' +
                ", title='" + title + '\'' +
                '}';
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
