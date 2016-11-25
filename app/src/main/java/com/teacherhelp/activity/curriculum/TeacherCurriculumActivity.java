package com.teacherhelp.activity.curriculum;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.teacherhelp.R;
import com.teacherhelp.activity.curriculum.calendar.HighlightWeekendsDecorator;
import com.teacherhelp.activity.curriculum.calendar.OneDayDecorator;
import com.teacherhelp.adapter.curriculum.CurriculumAdapter;
import com.teacherhelp.adapter.curriculum.CurriculumSingleAdapter;
import com.teacherhelp.app.MyApplication;
import com.teacherhelp.app.UrlConfig;
import com.teacherhelp.base.BaseActivity;
import com.teacherhelp.bean.curriculum.CollectiveBean;
import com.teacherhelp.bean.curriculum.CurriculumBean;
import com.teacherhelp.bean.curriculum.IndividualBean;
import com.teacherhelp.utils.CommonUtils;
import com.teacherhelp.utils.LogHelper;
import com.teacherhelp.utils.ShareReferenceUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;


/**
 * 首页老师课程表
 */
public class TeacherCurriculumActivity extends BaseActivity implements OnDateSelectedListener {
    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();
    @Bind(R.id.curriculumRecyclerview1)
    RecyclerView curriculumRecyclerview1;
    @Bind(R.id.curriculumRecyclerview2)
    RecyclerView curriculumRecyclerview2;
    @Bind(R.id.imgbtn_back)
    ImageButton imgbtnBack;
    @Bind(R.id.tv_titletwo)
    TextView tvTitletwo;
    private MaterialCalendarView widget;

    private CurriculumAdapter adapter1;//集体课
    private CurriculumSingleAdapter adapter2;//个别课
    private List<CollectiveBean> list1;
    private List<IndividualBean> list2;

//    private CourseTableView courseTableView;

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_teacher_curriculum);
    }

    @Override
    protected void aadListenter() {

    }

    @Override
    protected void initVariables() {
        tvTitletwo.setVisibility(View.VISIBLE);
        tvTitletwo.setText("课程表");
//        courseTableView = (CourseTableView) findViewById(R.id.ctv);
        widget = (MaterialCalendarView) findViewById(R.id.calendarView);

//        initData();
//        courseTableView.setOnCourseItemClickListener(new CourseTableView.OnCourseItemClickListener() {
//            @Override
//            public void onCourseItemClick(TextView tv, int jieci, int day, String des) {
//                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(TeacherCurriculumActivity.this);
//                builder.setMessage(des);
//                builder.show();
//            }
//        });


        widget.setOnDateChangedListener(this);
        widget.setShowOtherDates(MaterialCalendarView.SHOW_ALL);

        Calendar instance = Calendar.getInstance();
        widget.setSelectedDate(instance.getTime());


        Calendar instance1 = Calendar.getInstance();
        instance1.set(instance1.get(Calendar.YEAR), instance1.get(Calendar.MONTH), instance1.get(Calendar.DAY_OF_MONTH) - 21 - poorDay(0));

        Calendar instance2 = Calendar.getInstance();
        instance2.set(instance2.get(Calendar.YEAR), instance2.get(Calendar.MONTH), instance2.get(Calendar.DAY_OF_MONTH) + 21 + poorDay(1));

        widget.state().edit()
                .setFirstDayOfWeek(Calendar.MONDAY)
                .setMinimumDate(instance1.getTime())
                .setMaximumDate(instance2.getTime())
                .setCalendarDisplayMode(CalendarMode.WEEKS)
                .commit();


        widget.addDecorators(
                new HighlightWeekendsDecorator(),
                oneDayDecorator
        );

        list1 = new ArrayList<CollectiveBean>();
        list2 = new ArrayList<IndividualBean>();
        adapter1 = new CurriculumAdapter(TeacherCurriculumActivity.this, list1);
        adapter2 = new CurriculumSingleAdapter(TeacherCurriculumActivity.this, list2);
        adapter1.setMyListenter(new CurriculumAdapter.MyListenter() {
            @Override
            public void myonclick(String id) {
                Intent intent = new Intent(TeacherCurriculumActivity.this, CurriculumDetailActivity.class);
//                intent.putExtra("集体课程id","id");
                startActivity(intent);
            }
        });
        adapter2.setMyListenter(new CurriculumSingleAdapter.MyListenter() {
            @Override
            public void myonclick(String id) {
                Intent intent = new Intent(TeacherCurriculumActivity.this, CurriculumDetailActivity.class);
//                intent.putExtra("个别课程id","id");
                startActivity(intent);
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        curriculumRecyclerview1.setLayoutManager(linearLayoutManager);
        curriculumRecyclerview2.setLayoutManager(linearLayoutManager2);
        curriculumRecyclerview1.setHasFixedSize(true);
        curriculumRecyclerview2.setHasFixedSize(true);
        curriculumRecyclerview1.setAdapter(adapter1);
        curriculumRecyclerview2.setAdapter(adapter2);
//        testDate();
        getDate("11111", "1");
    }


    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        oneDayDecorator.setDate(date.getDate());
        widget.invalidateDecorators();
        showWaitLoad(this, "正在更新...");
        getDate("11111", "1");
    }


    private void testDate() {
        for (int i = 0; i < 4; i++) {
            CollectiveBean c = new CollectiveBean();
            list1.add(c);
        }
        for (int i = 0; i < 6; i++) {
            IndividualBean c = new IndividualBean();
            list2.add(c);
        }
        adapter1.notifyDataSetChanged();
        adapter2.notifyDataSetChanged();
    }
//    private void initData() {
//        List<Course> list = new ArrayList<>();
//        Course c1 = new Course();
//        c1.setDay(1);
//        c1.setDes("周一课程");
//        c1.setJieci(1);
//        c1.setSpanNum(1);
//        list.add(c1);
//
//        Course c2 = new Course();
//        c2.setDay(2);
//        c2.setDes("周二课程");
//        c2.setJieci(2);
//        c2.setSpanNum(2);
//        list.add(c2);
//
//        Course c4 = new Course();
//        c4.setDay(3);
//        c4.setDes("周三课程周三课程周三课程周三课程周三课程周三课程周三周三课程周三课程周三课程周三课程周三课程周三课程周三课程周三课程周三课程周三课程周三课程周三课程课程周三课程周三课程周三课程周三课程周三课程周三课程周三课程周三课程周三课程周三课程周三课程周三课程周三课程周三课程");
//        c4.setJieci(3);
//        c4.setSpanNum(3);
//        list.add(c4);
//
//        Course c3 = new Course();
//        c3.setDay(4);
//        c3.setDes("周四课程");
//        c3.setJieci(4);
//        c3.setSpanNum(4);
//        list.add(c3);
//
//        Course c5 = new Course();
//        c5.setDay(5);
//        c5.setDes("周五课程");
//        c5.setJieci(5);
//        c5.setSpanNum(5);
//        list.add(c5);
//
//        Course c6 = new Course();
//        c6.setDay(6);
//        c6.setDes("周六课程");
//        c6.setJieci(6);
//        c6.setSpanNum(6);
//        list.add(c6);
//
//        Course c7 = new Course();
//        c7.setDay(7);
//        c7.setDes("周日课程");
//        c7.setJieci(7);
//        c7.setSpanNum(7);
//        list.add(c7);
//
//        courseTableView.updateCourseViews(list);

//    }

    /**
     * 获取课程表数据
     */
    private void getDate(String orgId, String schoolId) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, UrlConfig.GET_ListSchedule(orgId, schoolId), new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                LogHelper.d("请求返回的结果：" + result);
                //请求成功
                JSONObject jsonObject = JSONObject.parseObject(result);
                int responseCode = jsonObject.getIntValue("code");
                switch (responseCode) {
                    case 0:
                        Gson gson = new Gson();
                        CurriculumBean curriculum = gson.fromJson(
                                jsonObject.getString("data"),
                                new TypeToken<CurriculumBean>() {
                                }.getType());
                        Log.e("老师课表--------------", curriculum + "");
                        if (!curriculum.getGroup().isEmpty()) {
                            list1.clear();
                            list1.addAll(curriculum.getGroup());
                        } else if (!curriculum.getSingle().isEmpty()) {
                            list2.clear();
                            list2.addAll(curriculum.getSingle());
                        }
                        adapter1.notifyDataSetChanged();
                        adapter2.notifyDataSetChanged();
                        DismissDialog();
                        break;
                    default:
                        CommonUtils.showToast("" + jsonObject.get("msg"));
                        DismissDialog();
                        break;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                CommonUtils.showToast("请求失败，请重试！");
                LogHelper.d(volleyError.getMessage());
                DismissDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("dateStart", "查询开始日期");
                map.put("dateEnd", "结束日期");
                return null;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("token", ShareReferenceUtils.getValue("myToken"));
                LogHelper.d("请求头:" + headers.toString());
                return headers;
            }
        };
        setTmie(stringRequest);
        stringRequest.setTag("volleyCurriculum");
        MyApplication.getHttpQueue().add(stringRequest);
    }

    @OnClick(R.id.imgbtn_back)
    public void onClick() {
        finish();
    }


    /**
     * 计算出本周相邻周一与周末的差并补充剩余天数
     *
     * @param i
     * @return
     */
    private int poorDay(int i) {
        Calendar instance = Calendar.getInstance();
        int now = instance.get(Calendar.DAY_OF_WEEK) - 1;
        int beforeday = 0;
        int afterday = 0;

        switch (now) {
            case 1:
                beforeday = 0;
                afterday = 6;
                break;
            case 2:
                beforeday = 1;
                afterday = 5;
                break;
            case 3:
                beforeday = 2;
                afterday = 4;
                break;
            case 4:
                beforeday = 3;
                afterday = 3;
                break;
            case 5:
                beforeday = 4;
                afterday = 2;
                break;
            case 6:
                beforeday = 5;
                afterday = 1;
                break;
            case 7:
                beforeday = 6;
                afterday = 0;
                break;
            default:
                break;
        }
        return i == 0 ? beforeday : afterday;
    }

}
