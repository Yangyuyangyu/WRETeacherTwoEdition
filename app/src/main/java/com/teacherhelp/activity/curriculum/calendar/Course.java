package com.teacherhelp.activity.curriculum.calendar;


import java.io.Serializable;

public class Course implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -9121734039844677432L;
    private int jieci;//第几节

    private int day;//周几
    private String des;
    private int spanNum = 2;//

    private String ClassRoomName;
    private String ClassTypeName;

    public Course(int jieci, int day, String des) {
        this.jieci = jieci;
        this.day = day;
        this.des = des;
    }

    public Course() {
    }

    public int getJieci() {
        return jieci;
    }

    public void setJieci(int jieci) {
        this.jieci = jieci;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public int getSpanNum() {
        return spanNum;
    }

    public void setSpanNum(int spanNum) {
        this.spanNum = spanNum;
    }

    @Override
    public String toString() {
        return "Course [jieci=" + jieci + ", day=" + day + ", des=" + des
                + ", spanNun=" + spanNum + "]";
    }

    public String getClassRoomName() {
        return ClassRoomName;
    }

    public void setClassRoomName(String classRoomName) {
        ClassRoomName = classRoomName;
    }

    public String getClassTypeName() {
        return ClassTypeName;
    }

    public void setClassTypeName(String classTypeName) {
        ClassTypeName = classTypeName;
    }


}
