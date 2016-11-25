package com.teacherhelp.bean;

/**
 * Created by Administrator on 2016/7/21.
 */
public class ClassBean {
    private String name;
    private int type;
    private boolean ischeck=false;

    public boolean ischeck() {
        return ischeck;
    }

    public void setIscheck(boolean ischeck) {
        this.ischeck = ischeck;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
