package com.teacherhelp.bean;

import java.util.List;

/**
 * Created by waycubeoxa on 16/11/23.教学内容
 */

public class TeachDataBean {
    private int dataCount;
    private int totalPage;
    private int rowNum;
    /**
     * id : 1
     * scheduleTimeId : 1
     * teacheMode : 2
     * date : 2016-11-14
     * subjectName : 广场舞
     * courseChildName : dasasd
     * rollCallStuNum : 1
     * startTime : 13:10
     * endTime : 14:10
     * state : 2
     * headImg : null
     * studentName : null
     * birthday : null
     * age : null
     */

    private List<TeachContentBean> data;

    public int getDataCount() {
        return dataCount;
    }

    public void setDataCount(int dataCount) {
        this.dataCount = dataCount;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getRowNum() {
        return rowNum;
    }

    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }

    public List<TeachContentBean> getData() {
        return data;
    }

    public void setData(List<TeachContentBean> data) {
        this.data = data;
    }


}
