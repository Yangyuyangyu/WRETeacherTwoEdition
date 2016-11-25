package com.teacherhelp.activity.register;


import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;


import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
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


import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;


/**
 * A login screen that offers login via email/password.
 */
public class RegisterAvtivity extends BaseActivity {
    @Bind(R.id.imgbtn_back)
    ImageButton imgbtnBack;
    @Bind(R.id.tv_titletwo)
    TextView tvTitletwo;


    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private EditText mAuthCodeView;
    private Button mCodeBtn;

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_register_avtivity);
    }

    @Override
    protected void aadListenter() {

    }

    @Override
    protected void initVariables() {
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mAuthCodeView = (EditText) findViewById(R.id.authCode);
        mCodeBtn = (Button) findViewById(R.id.authCodeBtn);
        mCodeBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                if (!isFastClick()) {
                    SendCode();
                }

            }
        });
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_ACTION_DONE) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });


        tvTitletwo.setText("注册");
        tvTitletwo.setVisibility(View.VISIBLE);
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {


        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String authCode = mAuthCodeView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_password_required));
            focusView = mPasswordView;
            cancel = true;
        }
        if (TextUtils.isEmpty(authCode)) {
            mAuthCodeView.setError("验证码不能为空");
            focusView = mAuthCodeView;
            cancel = true;
        }


        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            Register();
        }


    }


    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() >= 6;
    }


    @OnClick(R.id.imgbtn_back)
    public void onClick() {

        finish();
    }

    /**
     * 注册
     */
    private void Register() {
        String url = UrlConfig.Login.POST_Register + "?phone=" + mEmailView.getText().toString() + "&pwd=" + mPasswordView.getText().toString() + "&authCode=" + mAuthCodeView.getText().toString();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                LogHelper.d("请求返回的结果：" + result);
                //请求成功
                JSONObject jsonObject = JSONObject.parseObject(result);
                int responseCode = jsonObject.getIntValue("code");
                switch (responseCode) {
                    case 0:
                        ShareReferenceUtils.putValue("regUserPhone", mEmailView.getText().toString());
                        finish();
                        CommonUtils.showToast(jsonObject.getString("msg"));
                        break;
                    default:
                        CommonUtils.showToast(jsonObject.getString("msg"));
                        break;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                CommonUtils.showToast("请求失败，请重试!");
                LogHelper.d(volleyError.getMessage());
            }
        });
        setTmie(stringRequest);
        stringRequest.setTag("volleyregister");
        MyApplication.getHttpQueue().add(stringRequest);

    }

    /**
     * 发送验证码
     */

    private void SendCode() {

        if (TextUtils.isEmpty(mEmailView.getText().toString())) {
            CommonUtils.showToast("请输入用户手机号");
            return;
        }
        String url = UrlConfig.GET_SendCode(mEmailView.getText().toString());
        LogHelper.d("注册请求url：" + url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                LogHelper.d("请求返回的结果：" + result);
                //请求成功
                JSONObject jsonObject = JSONObject.parseObject(result);
                int responseCode = jsonObject.getIntValue("code");
                switch (responseCode) {
                    case 0:
                        mCodeBtn.setEnabled(false);
                        timer.start();
                        break;
                    default:
                        CommonUtils.showToast(jsonObject.getString("msg"));
                        break;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                CommonUtils.showToast("网络请求失败，请重试！");
                LogHelper.d(volleyError.getMessage());
            }
        });
        setTmie(stringRequest);
        stringRequest.setTag("volleysendcode");
        MyApplication.getHttpQueue().add(stringRequest);

    }


    CountDownTimer timer = new CountDownTimer(60000, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            mCodeBtn.setText(millisUntilFinished / 1000 + "秒");
        }

        @Override
        public void onFinish() {
            mCodeBtn.setEnabled(true);
            mCodeBtn.setText("发送验证码");
        }
    };

    // 禁止快速点击
    private static long lastClickTime = 0;

    public synchronized static boolean isFastClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < 1000) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

}

