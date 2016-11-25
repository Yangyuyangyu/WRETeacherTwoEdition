package com.teacherhelp.activity;

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
 * 名称:新增成果分享页面
 * 作用：通过设置标题，时间与内容来设置成果分享
 * Created on 2016/7/26.
 * 创建人：WangHaoMiao
 */
public class AddStActivity extends BaseActivity {
    @Bind(R.id.imgbtn_back)
    ImageButton imgbtnBack;
    @Bind(R.id.tv_titletwo)
    TextView tvTitletwo;
    @Bind(R.id.tv_titleright)
    TextView tvTitleright;
    @Bind(R.id.title)
    EditText title;
    @Bind(R.id.tv_time)
    TextView tvTime;
    @Bind(R.id.rl_time)
    RelativeLayout rltime;
    @Bind(R.id.et_content)
    EditText etContent;
    @Bind(R.id.delete)
    Button delete;

    /*选择年月日的Dialog*/
    private BirthDayDialog birthDayDialog;

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_addst);
    }

    @Override
    protected void aadListenter() {
        imgbtnBack.setOnClickListener(this);
        tvTitleright.setOnClickListener(this);
        delete.setOnClickListener(this);
        rltime.setOnClickListener(this);
    }

    @Override
    protected void initVariables() {
        MyApplication.getInstance().addActivity(this);
        tvTitletwo.setVisibility(View.VISIBLE);
        tvTitleright.setVisibility(View.VISIBLE);
        tvTitleright.setText("确定");
        tvTitletwo.setText("新增成果分享");

        if (getIntent().getStringExtra("type") != null) {
            switch (getIntent().getStringExtra("type")) {
                case "item":
                    tvTitletwo.setText("编辑成果分享");

                    if (getIntent().getStringArrayExtra("info") != null) {
                        String[] info = getIntent().getStringArrayExtra("info");
                        delete.setVisibility(View.VISIBLE);
                        title.setText(info[1]);
                        tvTime.setText(info[2].substring(0, 10));
                        etContent.setText(info[3]);
                    }
                    break;
                case "add":
                    Calendar c = Calendar.getInstance();
                    delete.setVisibility(View.GONE);
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    Date curDate = new Date(System.currentTimeMillis());
                    String str = formatter.format(curDate);
                    delete.setVisibility(View.GONE);
                    tvTime.setText(str);
                    title.setText("");
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
                if (getIntent().getStringExtra("type") != null) {
                    switch (getIntent().getStringExtra("type")) {
                        case "item":
                            if (getIntent().getStringArrayExtra("info") != null) {
                                updateSt(getIntent().getStringArrayExtra("info")[0]);
                            } else {
                                CommonUtils.showToast("修改失败，请稍后再试");
                            }
                            break;
                        case "add":
                            addSt();
                            break;
                    }
                }
                break;
            case R.id.delete: //删除
                if (getIntent().getStringArrayExtra("info") != null) {
                    delSt(getIntent().getStringArrayExtra("info")[0]);
                } else {
                    CommonUtils.showToast("删除失败，请稍后再试");
                }
                break;
            case R.id.rl_time: //时间选择
                if (Utility.StringIsNull(tvTime.getText().toString())) {
                    String[] array = {"1950", "01", "01"};
                    birthDayDialog = new BirthDayDialog(AddStActivity.this, array, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!Utility.StringIsNull(birthDayDialog.GetText())) {
                                tvTime.setText(birthDayDialog.GetText());
                            } else {
                                tvTime.setText("1950-01-01");
                            }
                            birthDayDialog.dismiss();
                        }
                    });
                    birthDayDialog.showDialog();
                } else {
                    birthDayDialog = new BirthDayDialog(AddStActivity.this, convertStrToArray(tvTime.getText().toString()), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!Utility.StringIsNull(birthDayDialog.GetText())) {
                                tvTime.setText(birthDayDialog.GetText());
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
    private void addSt() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConfig.PersonalResults.POST_Add, new Response.Listener<String>() {
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
                CommonUtils.showToast("请求失败，请重试！" + volleyError.getMessage());
                LogHelper.d(volleyError.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("title", title.getText().toString());
                map.put("date", tvTime.getText().toString());
                map.put("content", etContent.getText().toString());
                LogHelper.d("请求参数:" + map.values());
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("token", ShareReferenceUtils.getValue("myToken"));
                return headers;
            }
        };
        setTmie(stringRequest);
        stringRequest.setTag("volleyPersonalResultsAdd");
        MyApplication.getHttpQueue().add(stringRequest);
    }

    /**
     * 编辑
     */
    private void updateSt(String id) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConfig.POST_EditPersonalResults(id), new Response.Listener<String>() {
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
                map.put("title", title.getText().toString());
                map.put("date", tvTime.getText().toString());
                map.put("content", etContent.getText().toString());
                LogHelper.d("请求头:" + map.toString());
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("token", ShareReferenceUtils.getValue("myToken"));
                return headers;
            }
        };
        setTmie(stringRequest);
        stringRequest.setTag("volleyPersonalResultsUpdate");
        MyApplication.getHttpQueue().add(stringRequest);
    }

    /**
     * 删除
     */
    private void delSt(String id) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, UrlConfig.GET_DelPersonalResults(id), new Response.Listener<String>() {
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
        stringRequest.setTag("volleyPersonalResultsDel");
        MyApplication.getHttpQueue().add(stringRequest);
    }
}
