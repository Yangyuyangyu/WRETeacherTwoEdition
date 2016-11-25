package com.teacherhelp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.teacherhelp.R;
import com.teacherhelp.app.MyApplication;
import com.teacherhelp.base.BaseActivity;
import com.teacherhelp.view.wheelView.OnWheelScrollListener;
import com.teacherhelp.view.wheelView.WheelView;
import com.teacherhelp.view.wheelView.adapter.NumericWheelAdapter;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 名称:出生年月页面
 * 作用:设置用户出生年月
 * Created on 2016/7/26.
 * 创建人:WangHaoMiao
 */
public class DateOfBirthActivity extends BaseActivity {
    @Bind(R.id.imgbtn_back)
    ImageButton imgbtnBack;
    @Bind(R.id.tv_titletwo)
    TextView tvTitletwo;
    @Bind(R.id.tv_titleright)
    TextView tvTitleright;
    @Bind(R.id.year)
    WheelView year;
    @Bind(R.id.month)
    WheelView month;
    @Bind(R.id.day)
    WheelView day;

    /*初始年*/
    private int mYear=1996;
    /*初始月*/
    private int mMonth=0;
    /*初始日*/
    private int mDay=1;
    /*用于存放最后选择的年月日*/
    private String birthday;
    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_dateofbirth);
    }

    @Override
    protected void aadListenter() {
        imgbtnBack.setOnClickListener(this);
        tvTitleright.setOnClickListener(this);
    }

    @Override
    protected void initVariables() {
        MyApplication.getInstance().addActivity(this);
        tvTitletwo.setVisibility(View.VISIBLE);
        tvTitleright.setVisibility(View.VISIBLE);
        tvTitleright.setText("确定");
        tvTitletwo.setText("出生年月");

        initwheelview();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.imgbtn_back:
                finish();
                saveEditTextAndCloseIMM();
                break;
            case R.id.tv_titleright:
                Intent i = new Intent();
                i.putExtra("birth", birthday);
                setResult(14, i);
                finish();
                break;
        }
    }

    private void initwheelview(){
        Calendar c = Calendar.getInstance();
        int norYear = c.get(Calendar.YEAR);

        int curYear = mYear;
        int curMonth =mMonth+1;
        int curDate = mDay;

        /*初始化年*/
        NumericWheelAdapter numericWheelAdapter1=new NumericWheelAdapter(DateOfBirthActivity.this,1950, norYear);
        numericWheelAdapter1.setLabel("年");
        year.setViewAdapter(numericWheelAdapter1);
        year.setCyclic(true);//是否可循环滑动
        year.addScrollingListener(scrollListener);

        /*初始化月*/
        NumericWheelAdapter numericWheelAdapter2=new NumericWheelAdapter(DateOfBirthActivity.this,1, 12, "%02d");
        numericWheelAdapter2.setLabel("月");
        month.setViewAdapter(numericWheelAdapter2);
        month.setCyclic(true);
        month.addScrollingListener(scrollListener);

        initDay(curYear,curMonth);
        day.addScrollingListener(scrollListener);
        day.setCyclic(true);

        year.setVisibleItems(7);//设置显示行数
        month.setVisibleItems(7);
        day.setVisibleItems(7);

        year.setCurrentItem(curYear - 1950);
        month.setCurrentItem(curMonth - 1);
        day.setCurrentItem(curDate - 1);
    }

    OnWheelScrollListener scrollListener = new OnWheelScrollListener() {
        @Override
        public void onScrollingStarted(WheelView wheel) {

        }

        @Override
        public void onScrollingFinished(WheelView wheel) {
            int n_year = year.getCurrentItem() + 1950;//年
            int n_month = month.getCurrentItem() + 1;//月

            initDay(n_year,n_month);

            birthday=new StringBuilder().append((year.getCurrentItem()+1950)).append("-").append((month.getCurrentItem() + 1)< 10 ? "0" + (month.getCurrentItem() + 1) : (month.getCurrentItem() + 1)).append("-").append(((day.getCurrentItem()+1) < 10) ? "0" + (day.getCurrentItem()+1) : (day.getCurrentItem()+1)).toString();
        }
    };

    /**
     */
    private void initDay(int arg1, int arg2) {
        NumericWheelAdapter numericWheelAdapter=new NumericWheelAdapter(DateOfBirthActivity.this,1, getDay(arg1, arg2), "%02d");
        numericWheelAdapter.setLabel("日");
        day.setViewAdapter(numericWheelAdapter);
    }

    /**
     *
     * @param year
     * @param month
     * @return
     */
    private int getDay(int year, int month) {
        int day = 30;
        boolean flag = false;
        switch (year % 4) {
            case 0:
                flag = true;
                break;
            default:
                flag = false;
                break;
        }
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                day = 31;
                break;
            case 2:
                day = flag ? 29 : 28;
                break;
            default:
                day = 30;
                break;
        }
        return day;
    }
}
