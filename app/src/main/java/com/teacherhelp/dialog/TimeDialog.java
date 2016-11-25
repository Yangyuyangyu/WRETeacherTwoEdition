package com.teacherhelp.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.teacherhelp.R;
import com.teacherhelp.base.BaseDialog;
import com.teacherhelp.view.wheelView.OnWheelScrollListener;
import com.teacherhelp.view.wheelView.WheelView;
import com.teacherhelp.view.wheelView.adapter.NumericWheelAdapter;

import java.util.Calendar;

/**
 * 名称:年月日时分秒的Dialog
 * 作用:选择年月日时分秒
 * Created on 2016/7/29.
 * 创建人:WangHaoMiao
 */
public class TimeDialog extends BaseDialog {

    private RelativeLayout enter;
    private LinearLayout ll;
    /*初始年*/
    private int mYear=1996;
    /*初始月*/
    private int mMonth=0;
    /*初始日*/
    private int mDay=1;
    /*初始分*/
    private int mMinute=0;
    /*初始秒*/
    private int mSecond=0;
    View view=null;
    private LayoutInflater inflater = null;

    private WheelView year;
    private WheelView month;
    private WheelView day;
    private WheelView minute;
    private WheelView second;

    /*最后需要返回的年月日*/
    private String birthday;

    public TimeDialog(Context context, String[] str, View.OnClickListener onClickListener) {
        super(context);
        setContentView(R.layout.birthdaydialog_layout);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mYear=Integer.valueOf(str[0]);
        mMonth=(Integer.valueOf(str[1]))-1;
        mDay=Integer.valueOf(str[2]);
        mMinute=Integer.valueOf(str[3]);
        mSecond=Integer.valueOf(str[4]);
        enter=(RelativeLayout)findViewById(R.id.birthdaydialog_rl);
        enter.setOnClickListener(onClickListener);
        ll=(LinearLayout) findViewById(R.id.ll);
        ll.addView(getDataPick());
    }

    private View getDataPick() {
        Calendar c = Calendar.getInstance();
        int norYear = c.get(Calendar.YEAR);
//		int curMonth = c.get(Calendar.MONTH) + 1;//通过Calendar算出的月数要+1
//		int curDate = c.get(Calendar.DATE);

        int curYear = mYear;
        int curMonth =mMonth+1;
        int curDate = mDay;
        int curMinute = mMinute;
        int curSecond = mSecond;

        view = inflater.inflate(R.layout.wheel_time_picker, null);

        year = (WheelView) view.findViewById(R.id.year);
        NumericWheelAdapter numericWheelAdapter1=new NumericWheelAdapter(getContext(),1950, norYear);
        numericWheelAdapter1.setLabel("年");
        year.setViewAdapter(numericWheelAdapter1);
        year.setCyclic(true);//是否可循环滑动
        year.addScrollingListener(scrollListener);

        month = (WheelView) view.findViewById(R.id.month);
        NumericWheelAdapter numericWheelAdapter2=new NumericWheelAdapter(getContext(),1, 12, "%02d");
        numericWheelAdapter2.setLabel("月");
        month.setViewAdapter(numericWheelAdapter2);
        month.setCyclic(true);
        month.addScrollingListener(scrollListener);

        day = (WheelView) view.findViewById(R.id.day);
        initDay(curYear,curMonth);
        day.addScrollingListener(scrollListener);
        day.setCyclic(true);

        minute = (WheelView) view.findViewById(R.id.minute);
        NumericWheelAdapter numericWheelAdapter3=new NumericWheelAdapter(getContext(),1, 60, "%02d");
        numericWheelAdapter3.setLabel("分");
        minute.setViewAdapter(numericWheelAdapter3);
        minute.setCyclic(true);
        minute.addScrollingListener(scrollListener);

        second = (WheelView) view.findViewById(R.id.second);
        NumericWheelAdapter numericWheelAdapter4=new NumericWheelAdapter(getContext(),1, 60, "%02d");
        numericWheelAdapter4.setLabel("秒");
        second.setViewAdapter(numericWheelAdapter4);
        second.setCyclic(true);
        second.addScrollingListener(scrollListener);

        year.setVisibleItems(7);//设置显示行数
        month.setVisibleItems(7);
        day.setVisibleItems(7);
        minute.setVisibleItems(7);
        second.setVisibleItems(7);

        year.setCurrentItem(curYear - 1950);
        month.setCurrentItem(curMonth - 1);
        day.setCurrentItem(curDate - 1);
        minute.setCurrentItem(curMinute - 1);
        second.setCurrentItem(curSecond - 1);

        return view;
    }

    OnWheelScrollListener scrollListener = new OnWheelScrollListener() {
        @Override
        public void onScrollingStarted(WheelView wheel) {

        }

        @Override
        public void onScrollingFinished(WheelView wheel) {
            int n_year = year.getCurrentItem() + 1950;//年
            int n_month = month.getCurrentItem() + 1;//月
            int n_minute = minute.getCurrentItem() + 1;//分
            int n_second = second.getCurrentItem() + 1;//秒

            initDay(n_year,n_month);

            birthday=new StringBuilder().append((year.getCurrentItem()+1950)).append("-").append((month.getCurrentItem() + 1)< 10 ? "0" + (month.getCurrentItem() + 1) : (month.getCurrentItem() + 1)).append("-").append(((day.getCurrentItem()+1) < 10) ? "0" + (day.getCurrentItem()+1) : (day.getCurrentItem()+1)).toString();
        }
    };

    /**
     */
    private void initDay(int arg1, int arg2) {
        NumericWheelAdapter numericWheelAdapter=new NumericWheelAdapter(getContext(),1, getDay(arg1, arg2), "%02d");
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


    /**
     * 获取最后的年月日
     * @return 返回的年月日 如:1950-01-01
     */
    public String GetText(){
        return birthday;
    }

    public void DissMiss(){
        dismiss();
    }
}
