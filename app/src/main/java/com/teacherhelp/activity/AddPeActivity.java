package com.teacherhelp.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
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
import com.teacherhelp.dialog.BirthDayDialog;
import com.teacherhelp.utils.CommonUtils;
import com.teacherhelp.utils.LogHelper;
import com.teacherhelp.utils.ShareReferenceUtils;
import com.teacherhelp.utils.Utility;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;

/**
 * 名称:新增个人经历页面
 * 作用：通过选择开始时间，结束时间和个人经历介绍来增加个人经历
 * Created on 2016/7/26.
 * 创建人：WangHaoMiao
 */
public class AddPeActivity extends BaseActivity {
    @Bind(R.id.imgbtn_back)
    ImageButton imgbtnBack;
    @Bind(R.id.tv_titletwo)
    TextView tvTitletwo;
    @Bind(R.id.tv_titleright)
    TextView tvTitleright;
    @Bind(R.id.tv_starttime)
    TextView tvStarttime;
    @Bind(R.id.rl_starttime)
    RelativeLayout rlStarttime;
    @Bind(R.id.tv_endtime)
    TextView tvEndtime;
    @Bind(R.id.rl_endtime)
    RelativeLayout rlEndtime;
    @Bind(R.id.et_content)
    EditText etContent;
    @Bind(R.id.delete)
    Button delete;

    /*选择年月日的Dialog*/
    private BirthDayDialog birthDayDialog;

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_addpe);
    }

    @Override
    protected void aadListenter() {
        imgbtnBack.setOnClickListener(this);
        rlStarttime.setOnClickListener(this);
        rlEndtime.setOnClickListener(this);
        tvTitleright.setOnClickListener(this);
        delete.setOnClickListener(this);
    }

    @Override
    protected void initVariables() {
        MyApplication.getInstance().addActivity(this);
        tvTitletwo.setVisibility(View.VISIBLE);
        tvTitleright.setVisibility(View.VISIBLE);
        tvTitleright.setText("确定");
        tvTitletwo.setText("新增个人经历");

        if (getIntent().getStringExtra("type") != null) {
            switch (getIntent().getStringExtra("type")) {
                case "item":
                    tvTitletwo.setText("编辑个人经历");

                    if (getIntent().getStringArrayExtra("info") != null) {
                        String[] info = getIntent().getStringArrayExtra("info");
                        delete.setVisibility(View.VISIBLE);
                        tvStarttime.setText(info[1].substring(0, 10));
                        tvEndtime.setText(info[2].substring(0, 10));
                        etContent.setText(info[3]);
                    }
                    break;
                case "add":
                    Calendar c = Calendar.getInstance();
                    delete.setVisibility(View.GONE);
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    Date curDate = new Date(System.currentTimeMillis());
                    String str = formatter.format(curDate);
                    tvStarttime.setText(str);
                    tvEndtime.setText(str);
                    etContent.setText("");
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.imgbtn_back: //返回
                finish();
                saveEditTextAndCloseIMM();
                break;
            case R.id.tv_titleright: //确定
                if (TextUtils.isEmpty(etContent.getText())) {
                    CommonUtils.showToast("内容不能为空");
                    return;
                }
                if (getIntent().getStringExtra("type") != null) {
                    switch (getIntent().getStringExtra("type")) {
                        case "item"://修改
                            if (getIntent().getStringArrayExtra("info") != null) {
                                updatePe(getIntent().getStringArrayExtra("info")[0]);
                            } else {
                                CommonUtils.showToast("修改失败，请稍后再试");
                            }
                            break;
                        case "add"://添加
                            addPe();
                            break;
                    }
                }

                break;
            case R.id.delete: //删除
                if (getIntent().getStringArrayExtra("info") != null) {
                    delPe(getIntent().getStringArrayExtra("info")[0]);
                } else {
                    CommonUtils.showToast("删除失败，请稍后再试");
                }
                break;
            case R.id.rl_starttime: //设置开始时间
                if (Utility.StringIsNull(tvStarttime.getText().toString())) {
                    String[] array = {"1950", "01", "01"};
                    birthDayDialog = new BirthDayDialog(AddPeActivity.this, array, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!Utility.StringIsNull(birthDayDialog.GetText())) {
                                tvStarttime.setText(birthDayDialog.GetText());
                            } else {
                                tvStarttime.setText("1950-01-01");
                            }
                            birthDayDialog.dismiss();
                        }
                    });
                    birthDayDialog.showDialog();
                } else {
                    birthDayDialog = new BirthDayDialog(AddPeActivity.this, convertStrToArray(tvStarttime.getText().toString()), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!Utility.StringIsNull(birthDayDialog.GetText())) {
                                tvStarttime.setText(birthDayDialog.GetText());
                            }
                            birthDayDialog.dismiss();
                        }
                    });
                    birthDayDialog.showDialog();
                }
                break;
            case R.id.rl_endtime: //设置结束时间
                if (Utility.StringIsNull(tvEndtime.getText().toString())) {
                    String[] array = {"1950", "01", "01"};
                    birthDayDialog = new BirthDayDialog(AddPeActivity.this, array, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!Utility.StringIsNull(birthDayDialog.GetText())) {
                                tvEndtime.setText(birthDayDialog.GetText());
                            } else {
                                tvEndtime.setText("1950-01-01");
                            }
                            birthDayDialog.dismiss();
                        }
                    });
                    birthDayDialog.showDialog();
                } else {
                    birthDayDialog = new BirthDayDialog(AddPeActivity.this, convertStrToArray(tvEndtime.getText().toString()), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!Utility.StringIsNull(birthDayDialog.GetText())) {
                                tvEndtime.setText(birthDayDialog.GetText());
                            }
                            birthDayDialog.dismiss();
                        }
                    });
                    birthDayDialog.showDialog();
                }
                break;
        }
    }

    //使用String的split 方法
    public static String[] convertStrToArray(String str) {
        String[] strArray = null;
        strArray = str.split("-"); //拆分字符为"," ,然后把结果交给数组strArray
        return strArray;
    }

    /**
     * 新增
     */
    private void addPe() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConfig.PersonalExperience.POST_Add, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                LogHelper.d("请求返回的结果：" + result);
                //请求成功
                JSONObject jsonObject = JSONObject.parseObject(result);
                int responseCode = jsonObject.getIntValue("code");
                switch (responseCode) {
                    case 0:
                        CommonUtils.showToast(jsonObject.get("msg") + "");
                        finish();
                        break;
                    default:
                        CommonUtils.showToast(jsonObject.get("msg") + "");
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
                map.put("startTime", tvStarttime.getText().toString());
                map.put("endTime", tvEndtime.getText().toString());
                map.put("experience", etContent.getText().toString());
                LogHelper.d("请求头:" + map.toString());
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
        stringRequest.setTag("volleyPersonalExperienceAdd");
        MyApplication.getHttpQueue().add(stringRequest);
    }

    /**
     * 编辑
     */
    private void updatePe(String id) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConfig.POST_EditPersonalExperience(id), new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                LogHelper.d("请求返回的结果：" + result);
                //请求成功
                JSONObject jsonObject = JSONObject.parseObject(result);
                int responseCode = jsonObject.getIntValue("code");
                switch (responseCode) {
                    case 0:
                        CommonUtils.showToast(jsonObject.get("msg") + "");
                        finish();
                        break;
                    default:
                        CommonUtils.showToast(jsonObject.get("msg") + "");
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
                map.put("startTime", tvStarttime.getText().toString());
                map.put("endTime", tvEndtime.getText().toString());
                map.put("experience", etContent.getText().toString());
                LogHelper.d("请求头:" + map.toString());
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
        stringRequest.setTag("volleyPersonalExperienceUpdate");
        MyApplication.getHttpQueue().add(stringRequest);
    }

    /**
     * 删除
     */
    private void delPe(String id) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, UrlConfig.GET_DelPersonalExperience(id), new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                LogHelper.d("请求返回的结果：" + result);
                //请求成功
                JSONObject jsonObject = JSONObject.parseObject(result);
                int responseCode = jsonObject.getIntValue("code");
                switch (responseCode) {
                    case 0:
                        CommonUtils.showToast("删除成功" + jsonObject.get("msg") + "");
                        finish();
                        break;
                    default:
                        CommonUtils.showToast("删除成功" + jsonObject.get("msg") + "");
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
        stringRequest.setTag("volleyPersonalExperienceDel");
        MyApplication.getHttpQueue().add(stringRequest);
    }
}
