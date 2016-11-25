package com.teacherhelp.app;

import com.teacherhelp.bean.personPic.PersonPicBean;

import java.util.List;

/**
 * Created by waycubeoxa on 16/10/25.老师信息单例
 */

public class UserInfo {
    private String id;
    private String name;
    private String headImg;
    private String sex;
    private String birthday;
    private String edu;
    private String graduatedSchool;
    private String qq;
    private String wxName;
    private String idCardCode;
    private String cellPhone;
    private String addr;
    private String summary;
    private String merits;
    private List<?> history;
    private List<?> result;
    private List<PersonPicBean> style;

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

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getEdu() {
        return edu;
    }

    public void setEdu(String edu) {
        this.edu = edu;
    }

    public String getGraduatedSchool() {
        return graduatedSchool;
    }

    public void setGraduatedSchool(String graduatedSchool) {
        this.graduatedSchool = graduatedSchool;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getWxName() {
        return wxName;
    }

    public void setWxName(String wxName) {
        this.wxName = wxName;
    }

    public String getIdCardCode() {
        return idCardCode;
    }

    public void setIdCardCode(String idCardCode) {
        this.idCardCode = idCardCode;
    }

    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getMerits() {
        return merits;
    }

    public void setMerits(String merits) {
        this.merits = merits;
    }

    public List<?> getHistory() {
        return history;
    }

    public void setHistory(List<?> history) {
        this.history = history;
    }

    public List<?> getResult() {
        return result;
    }

    public void setResult(List<?> result) {
        this.result = result;
    }

    public List<PersonPicBean> getStyle() {
        return style;
    }

    public void setStyle(List<PersonPicBean> style) {
        this.style = style;
    }

}
