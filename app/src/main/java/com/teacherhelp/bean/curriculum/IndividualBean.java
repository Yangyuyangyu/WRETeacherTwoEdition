package com.teacherhelp.bean.curriculum;

/**
 * Created by waycubeoxa on 16/11/11.老师课表  个别课
 */

public class IndividualBean {
    private String date;
    private String courseName;
    private String startTime;
    private String endTime;
    private String schedulingId;
    private String schedulingTimeId;
    private String state;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
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

    public String getSchedulingId() {
        return schedulingId;
    }

    public void setSchedulingId(String schedulingId) {
        this.schedulingId = schedulingId;
    }

    public String getSchedulingTimeId() {
        return schedulingTimeId;
    }

    public void setSchedulingTimeId(String schedulingTimeId) {
        this.schedulingTimeId = schedulingTimeId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
