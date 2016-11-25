package com.teacherhelp.bean.curriculum;

/**
 * Created by waycubeoxa on 16/11/3.课程详情
 */

public class CurriculumDetailBean {
    private String leftText;
    private String rightText;
    private Boolean isRightRow;

    public String getLeftText() {
        return leftText;
    }

    public void setLeftText(String leftText) {
        this.leftText = leftText;
    }

    public String getRightText() {
        return rightText;
    }

    public void setRightText(String rightText) {
        this.rightText = rightText;
    }

    public Boolean getRightRow() {
        return isRightRow;
    }

    public void setRightRow(Boolean rightRow) {
        isRightRow = rightRow;
    }
}
