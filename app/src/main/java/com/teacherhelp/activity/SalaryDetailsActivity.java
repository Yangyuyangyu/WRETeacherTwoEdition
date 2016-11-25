package com.teacherhelp.activity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.teacherhelp.R;
import com.teacherhelp.app.MyApplication;
import com.teacherhelp.app.UrlConfig;
import com.teacherhelp.base.BaseActivity;
import com.teacherhelp.utils.CommonUtils;
import com.teacherhelp.utils.LogHelper;
import com.teacherhelp.utils.ShareReferenceUtils;
import com.teacherhelp.view.RoundImageView;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;

/**
 * 个人考勤详情
 * Created by Administrator on 2016/7/25.
 */
public class SalaryDetailsActivity extends BaseActivity {
    @Bind(R.id.imgbtn_back)
    ImageButton imgbtnBack;
    @Bind(R.id.tv_titletwo)
    TextView tvTitletwo;
    @Bind(R.id.salarydetails_img_head)
    RoundImageView ImgHead;
    @Bind(R.id.salarydetails_tv_name)
    TextView TvName;
    @Bind(R.id.salarydetails_btn_wait)
    Button BtnWait;
    @Bind(R.id.salarydetails_btn_ontime)
    Button BtnOntime;
    @Bind(R.id.salarydetails_btn_late)
    Button BtnLate;
    @Bind(R.id.salarydetails_btn_truant)
    Button BtnTruant;
    @Bind(R.id.salarydetails_btn_leave)
    Button BtnLeave;
    @Bind(R.id.salarydetails_tv_ontime)
    TextView TvOntime;
    @Bind(R.id.salarydetails_tv_late)
    TextView TvLate;
    @Bind(R.id.salarydetails_tv_truant)
    TextView TvTruant;
    @Bind(R.id.salarydetails_tv_leave)
    TextView TvLeave;
    @Bind(R.id.tv_titleright)
    TextView tvTitleright;

    /*存放4个button*/
    private Button[] btns = new Button[5];
    /*判断选择的是哪一个签到状态  0-准时  1-迟到  2-旷课  3-请假*/

    /**
     * 考勤情况(0待考勤,1准时,2迟到,3请假,4旷课)
     */

    private int type = 0;

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_salarydetails);
    }

    @Override
    protected void aadListenter() {
        imgbtnBack.setOnClickListener(this);
        tvTitletwo.setOnClickListener(this);
    }

    @Override
    protected void initVariables() {
        MyApplication.getInstance().addActivity(this);
        tvTitletwo.setVisibility(View.VISIBLE);
        tvTitleright.setVisibility(View.VISIBLE);
        tvTitletwo.setText("个人考勤");
        tvTitleright.setText("确定");
        btns[0]=BtnWait;
        btns[1] = BtnOntime;
        btns[2] = BtnLate;
        btns[3] = BtnTruant;
        btns[4] = BtnLeave;

        BtnWait.setOnClickListener(onClickListener);
        BtnOntime.setOnClickListener(onClickListener);
        BtnLate.setOnClickListener(onClickListener);
        BtnTruant.setOnClickListener(onClickListener);
        BtnLeave.setOnClickListener(onClickListener);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.imgbtn_back:
                finish();
                break;
            case R.id.tv_titleright: //确定

                break;
        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            for (int i = 0; i < btns.length; i++) {
                btns[i].setTextColor(getResources().getColor(R.color.gray_color));
                btns[i].setBackgroundResource(R.drawable.salary_btn_bg);
            }
            switch (v.getId()) {
                case R.id.salarydetails_btn_wait://待考勤
                    type = 0;
                    BtnWait.setBackgroundResource(R.color.rer_color);
                    BtnWait.setTextColor(getResources().getColor(R.color.white));
                    break;
                case R.id.salarydetails_btn_ontime: //准时
                    type = 1;
                    BtnOntime.setBackgroundResource(R.color.rer_color);
                    BtnOntime.setTextColor(getResources().getColor(R.color.white));
                    break;
                case R.id.salarydetails_btn_late: //迟到
                    type = 2;
                    BtnLate.setBackgroundResource(R.color.rer_color);
                    BtnLate.setTextColor(getResources().getColor(R.color.white));
                    break;
                case R.id.salarydetails_btn_truant: //请假
                    type = 3;
                    BtnTruant.setBackgroundResource(R.color.rer_color);
                    BtnTruant.setTextColor(getResources().getColor(R.color.white));
                    break;
                case R.id.salarydetails_btn_leave: //旷课
                    type = 4;
                    BtnLeave.setBackgroundResource(R.color.rer_color);
                    BtnLeave.setTextColor(getResources().getColor(R.color.white));
                    break;
            }
        }
    };

    /**
     * 考勤详情
     */
    private void getDetail(String id) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, UrlConfig.GET_DetailAttendance(id), new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                LogHelper.d("请求返回的结果：" + result);
                //请求成功
                JSONObject jsonObject = JSONObject.parseObject(result);
                int responseCode = jsonObject.getIntValue("code");
                switch (responseCode) {
                    case 0:

                        break;
                    default:
                        break;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                CommonUtils.showToast("请求失败，请重试！");
                LogHelper.d(volleyError.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
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
        stringRequest.setTag("volleyAttendanceDetail");
        MyApplication.getHttpQueue().add(stringRequest);
    }

    /**
     * 考勤确定
     */
    private void attendance(String id) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConfig.POST_Attendance(id), new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                LogHelper.d("请求返回的结果：" + result);
                //请求成功
                JSONObject jsonObject = JSONObject.parseObject(result);
                int responseCode = jsonObject.getIntValue("code");
                switch (responseCode) {
                    case 0:

                        break;
                    default:
                        break;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                CommonUtils.showToast("请求失败，请重试！");
                LogHelper.d(volleyError.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("rollCallStudent", "json数组");
                return map;
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
        stringRequest.setTag("volleyAttendance");
        MyApplication.getHttpQueue().add(stringRequest);
    }
}
