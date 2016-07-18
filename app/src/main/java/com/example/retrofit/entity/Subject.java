package com.example.retrofit.entity;

/**
 * 测试显示数据
 * Created by WZG on 2016/7/16.
 */
public class Subject {
    private int id;
    private String name;
    private String title;

    @Override
    public String toString() {
        return "名：" + name + "\n标题:" + title;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
