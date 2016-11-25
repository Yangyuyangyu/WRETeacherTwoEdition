package com.teacherhelp.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.teacherhelp.R;
import com.teacherhelp.activity.register.FindPasswordActivity;
import com.teacherhelp.activity.register.RegisterAvtivity;
import com.teacherhelp.app.MyApplication;
import com.teacherhelp.app.UrlConfig;
import com.teacherhelp.base.BaseActivity;
import com.teacherhelp.utils.CommonUtils;
import com.teacherhelp.utils.LogHelper;
import com.teacherhelp.utils.ShareReferenceUtils;
import com.teacherhelp.utils.Utility;
import com.teacherhelp.view.ClearEditText;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * 登录页面
 * Created by Administrator on 2016/7/18.
 */
public class LoginActivity extends BaseActivity {
    @Bind(R.id.login_et_phonenumber)
    ClearEditText EtPhonenumber;
    @Bind(R.id.login_et_passworld)
    ClearEditText EtPassworld;
    @Bind(R.id.login_btn_login)
    Button BtnLogin;
    @Bind(R.id.login_cb)
    CheckBox Cb;
    @Bind(R.id.login_tv_rule)
    TextView TvRule;
    @Bind(R.id.login_tv_findpassworld)
    TextView Tv_FindPassWorld;
    @Bind(R.id.login_btn_register)
    Button BtnRegister;

    private String timestamp;

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void aadListenter() {
        BtnLogin.setOnClickListener(this);
        Cb.setOnClickListener(this);
        TvRule.setOnClickListener(this);
        Tv_FindPassWorld.setOnClickListener(this);
        BtnRegister.setOnClickListener(this);
    }

    @Override
    protected void initVariables() {
        MyApplication.getInstance().addActivity(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.login_btn_login: //登录
                if (!Cb.isChecked()) {
                    CommonUtils.showToast("您未选择服务协议");
                    return;
                }
                if (Utility.StringIsNull(EtPhonenumber.getText().toString()) || Utility.StringIsNull(EtPassworld.getText().toString())) {
                    CommonUtils.showToast("账号不能为空");
                    return;
                }
                if (Utility.NetworkAvailable(LoginActivity.this)) {
                    showWaitLoad(this, "正在登录，请稍后...");
                    login(EtPhonenumber.getText().toString(), EtPassworld.getText().toString());

                } else {
                    CommonUtils.showToast("没有网络连接");
                }
                break;
            case R.id.login_cb: //选择
                break;
            case R.id.login_tv_rule: //服务协议
                break;
            case R.id.login_tv_findpassworld: //找回密码
                startActivity(new Intent(LoginActivity.this, FindPasswordActivity.class));
                break;
            case R.id.login_btn_register: //注册
                startActivity(new Intent(LoginActivity.this, RegisterAvtivity.class));
                break;
        }
    }

    /**
     * 获得token
     */
    private void getToken(final String phone) {
        timestamp = "" + Utility.getStringToDate(Utility.getCurrentTimeDate());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConfig.Token_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                LogHelper.e("请求返回的结果：" + result);
                //请求成功
                JSONObject jsonObject = JSONObject.parseObject(result);
                int responseCode = jsonObject.getIntValue("code");
                switch (responseCode) {
                    case 200:
                        ShareReferenceUtils.putValue("Token", "" + jsonObject.get("token"));
                        connect("" + jsonObject.get("token"));
                        break;
                    default:
                        startActivity(new Intent(LoginActivity.this, TabHomeOldActivity.class));
                        DismissDialog();
                        finish();
                        break;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                startActivity(new Intent(LoginActivity.this, TabHomeOldActivity.class));
                DismissDialog();
                finish();
                LogHelper.e(volleyError.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("userId", phone);
                map.put("name", phone);
                map.put("portraitUri", "");
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("App-Key", UrlConfig.RongCloudAppKey);
                headers.put("Nonce", UrlConfig.RongCloudNonce);
                headers.put("Timestamp", timestamp);
                headers.put("Signature", UrlConfig.RongCloudSignature(timestamp));
                return headers;
            }
        };
        setTmie(stringRequest);
        stringRequest.setTag("volleygetRCloudToken");
        MyApplication.getHttpQueue().add(stringRequest);

    }

    /**
     * 建立与融云服务器的连接
     *
     * @param token
     */
    private void connect(String token) {

        if (getApplicationInfo().packageName.equals(MyApplication.getCurProcessName(getApplicationContext()))) {

            /**
             * IMKit SDK调用第二步,建立与服务器的连接
             */
            RongIM.connect(token, new RongIMClient.ConnectCallback() {

                /**
                 * Token 错误，在线上环境下主要是因为 Token 已经过期，您需要向 App Server 重新请求一个新的 Token
                 */
                @Override
                public void onTokenIncorrect() {

                    Log.e("LoginActivity", "--onTokenIncorrect");
                }

                /**
                 * 连接融云成功
                 * @param userid 当前 token
                 */
                @Override
                public void onSuccess(String userid) {
                    startActivity(new Intent(LoginActivity.this, TabHomeOldActivity.class));
                    DismissDialog();
                    finish();
                    Log.e("LoginActivity", "--onSuccess" + userid);

                }

                /**
                 * 连接融云失败
                 * @param errorCode 错误码，可到官网 查看错误码对应的注释
                 *                  http://www.rongcloud.cn/docs/android.html#常见错误码
                 */
                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    startActivity(new Intent(LoginActivity.this, TabHomeOldActivity.class));
                    DismissDialog();
                    finish();
                    Log.e("LoginActivity", "--onError" + errorCode);
                }
            });
        }
    }

    private void login(final String phone, final String pwd) {
        String url = UrlConfig.Login.POST_Login + "?phone=" + phone + "&pwd=" + pwd;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                LogHelper.d("请求返回的结果：" + result);
                //请求成功
                JSONObject jsonObject = JSONObject.parseObject(result);
                int responseCode = jsonObject.getIntValue("code");
                switch (responseCode) {
                    case 0:
                        ShareReferenceUtils.putValue("myToken", jsonObject.get("data") + "");
                        if (ShareReferenceUtils.getValue("Token") == null) {
                            getToken(phone);
                        } else {
                            connect(ShareReferenceUtils.getValue("Token"));
                        }

                        break;
                    default:
                        CommonUtils.showToast("请求失败," + jsonObject.get("msg"));
                        DismissDialog();
                        break;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                CommonUtils.showToast("请求失败，请重试！" + volleyError.getMessage());
                LogHelper.d(volleyError.getMessage());
                DismissDialog();
            }
        });
        setTmie(stringRequest);
        stringRequest.setTag("volleylogin");
        MyApplication.getHttpQueue().add(stringRequest);
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        String userPhone = ShareReferenceUtils.getValue("regUserPhone");
        if (userPhone != null) {
            EtPhonenumber.setText(userPhone);
            ShareReferenceUtils.putValue("regUserPhone", null);
        }
    }
}
