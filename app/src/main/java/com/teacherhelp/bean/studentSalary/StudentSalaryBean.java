package com.teacherhelp.bean.studentSalary;

/**
 * Created by waycubeoxa on 16/11/11.学生考勤数据
 */

public class StudentSalaryBean {
    //    id:考勤id
//    name:展示名称
//    img:展示图片
//    courseName:课程名称
//    teacheMode:课程对应教学方式
//    summary:简介(多少人或多少岁)
//    date:考勤日期
//    startTime:上课时间
//    endTime:下课时间
//    state:状态(0待考勤,1已考勤)
    private String id;
    private String name;
    private String img;
    private String courseName;
    private String teacheMode;
    private String summary;
    private String date;
    private String startTime;
    private String endTime;
    private String state;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getTeacheMode() {
        return teacheMode;
    }

    public void setTeacheMode(String teacheMode) {
        this.teacheMode = teacheMode;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
