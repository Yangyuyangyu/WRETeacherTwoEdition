package com.teacherhelp.bean.curriculum;

import java.util.List;

/**
 * Created by waycubeoxa on 16/11/2.教师课表
 */

public class CurriculumBean {
    private List<CollectiveBean> group;
    private List<IndividualBean> single;

    public List<CollectiveBean> getGroup() {
        return group;
    }

    public void setGroup(List<CollectiveBean> group) {
        this.group = group;
    }

    public List<IndividualBean> getSingle() {
        return single;
    }

    public void setSingle(List<IndividualBean> single) {
        this.single = single;
    }
}
