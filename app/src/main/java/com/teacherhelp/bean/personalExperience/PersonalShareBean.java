package com.teacherhelp.bean.personalExperience;

/**
 * Created by waycubeoxa on 16/11/11.个人成果
 */

public class PersonalShareBean {
//    id:成果id
//    title:标题
//    date:时间
//    content:经历说明
    private String id;
    private String title;
    private String date;
    private String content;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
