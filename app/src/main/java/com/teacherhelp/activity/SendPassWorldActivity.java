package com.teacherhelp.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
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
import com.teacherhelp.view.ClearEditText;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;

/**
 * 修改登录密码
 * Created by Administrator on 2016/7/24.
 */
public class SendPassWorldActivity extends BaseActivity {
    @Bind(R.id.imgbtn_back)
    ImageButton imgbtnBack;
    @Bind(R.id.tv_titletwo)
    TextView tvTitletwo;
    @Bind(R.id.tv_titleright)
    TextView tvTitleright;
    @Bind(R.id.sendpassworld_tv_username)
    TextView TvUsername;
    @Bind(R.id.sendpassworld_et_old)
    ClearEditText EtOld;
    @Bind(R.id.sendpassworld_et_new)
    ClearEditText EtNew;
    @Bind(R.id.sendpassworld_et_newtwo)
    ClearEditText Newtwo;

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_sendpassworld);
    }

    @Override
    protected void aadListenter() {
        imgbtnBack.setOnClickListener(this);
        tvTitleright.setOnClickListener(this);
        if(MyApplication.userInfo!=null){
            TvUsername.setText(MyApplication.userInfo.getName());
        }

    }

    @Override
    protected void initVariables() {
        MyApplication.getInstance().addActivity(this);
        tvTitletwo.setVisibility(View.VISIBLE);
        tvTitleright.setVisibility(View.VISIBLE);
        tvTitletwo.setText("修改登录密码");
        tvTitleright.setText("确定");
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
                if (TextUtils.isEmpty(EtOld.getText().toString()) || TextUtils.isEmpty(EtNew.getText().toString()) || TextUtils.isEmpty(Newtwo.getText().toString())) {
                    CommonUtils.showToast("密码不能为空");
                    return;
                } else if (!EtNew.getText().toString().equals(Newtwo.getText().toString())) {
                    CommonUtils.showToast("新密码不一致");
                    return;
                }
                changePwd();
                break;
        }
    }

    /**
     * 修改密码
     */
    private void changePwd() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConfig.POST_ChangPwd(), new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                LogHelper.d("请求返回的结果：" + result);
                //请求成功
                JSONObject jsonObject = JSONObject.parseObject(result);
                int responseCode = jsonObject.getIntValue("code");
                switch (responseCode) {
                    case 0:
                        CommonUtils.showToast(jsonObject.get("msg") + "");
                        startActivity(new Intent(SendPassWorldActivity.this, LoginActivity.class));
                        MyApplication.getInstance().exitAll();
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
                map.put("phone", MyApplication.userInfo.getCellPhone());
                map.put("oldPwd", EtOld.getText().toString());
                map.put("newPwd", Newtwo.getText().toString());
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
        stringRequest.setTag("volleyChangePwd");
        MyApplication.getHttpQueue().add(stringRequest);
    }
}
