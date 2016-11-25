package com.teacherhelp.bean.homework;

import java.util.List;

/**
 * Created by waycubeoxa on 16/11/11.学生布置作业列表
 */

public class HomeWorkBean {
//    id:作业id
//    name:展示名称
//    img:展示图片
//    courseName:课程名称
//    teacheMode:课程对应教学方式
//    summary:简介(多少人或多少岁)
//    date:考勤日期
//    startTime:上课时间
//    endTime:下课时间
//    state:状态(0待布置作业,1已布置作业)

    //    下面参数当已布置作业时有效
//    content:作业内容
//    file:作业附件 json 数组
//    id:附件id
//    type:附件类型(1图片,2音乐,3视频)
//    url:附件在服务器的地址
//    subNum:已提交的作业总数
//    resultNum:已批改的作业数量
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

    //    下面参数当已布置作业时有效
    private String content;
    private List<HomeWorkFile> file;
    private String subNum;
    private String resultNum;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<HomeWorkFile> getFile() {
        return file;
    }

    public void setFile(List<HomeWorkFile> file) {
        this.file = file;
    }

    public String getSubNum() {
        return subNum;
    }

    public void setSubNum(String subNum) {
        this.subNum = subNum;
    }

    public String getResultNum() {
        return resultNum;
    }

    public void setResultNum(String resultNum) {
        this.resultNum = resultNum;
    }
}
