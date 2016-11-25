package com.teacherhelp.activity.register;


import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.teacherhelp.R;
import com.teacherhelp.app.MyApplication;
import com.teacherhelp.base.BaseActivity;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 找回密码第二步
 */
public class FindPasswordTwoActivity extends BaseActivity {


    @Bind(R.id.imgbtn_back)
    ImageButton imgbtnBack;
    @Bind(R.id.tv_titletwo)
    TextView tvTitletwo;
    @Bind(R.id.tv_titleright)
    TextView tvTitleright;
    @Bind(R.id.findPwd_authCode)
    EditText findPwdAuthCode;
    @Bind(R.id.findPwd_authCodeBtn)
    Button findPwdAuthCodeBtn;

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_find_password_two);
    }

    @Override
    protected void initVariables() {
        MyApplication.getInstance().addForgetActivity(this);
        tvTitletwo.setVisibility(View.VISIBLE);
        tvTitleright.setVisibility(View.VISIBLE);
        tvTitletwo.setText("找回密码2/3");
        tvTitleright.setText("下一步");

    }

    @Override
    protected void aadListenter() {

    }

    @OnClick({R.id.imgbtn_back, R.id.tv_titleright, R.id.findPwd_authCodeBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgbtn_back:
                finish();
                break;
            case R.id.tv_titleright:
                if (TextUtils.isEmpty(findPwdAuthCode.getText().toString())) {
                    return;
                }
                if (findPwdAuthCode.getText().length() < 4) {
                    return;
                }
                Intent intent = new Intent(FindPasswordTwoActivity.this, FindPasswordDoneActivity.class);
                startActivity(intent);
                break;
            case R.id.findPwd_authCodeBtn:
                findPwdAuthCodeBtn.setEnabled(false);
                timer.start();
                break;
        }
    }

    CountDownTimer timer = new CountDownTimer(60000, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            findPwdAuthCodeBtn.setText(millisUntilFinished / 1000 + "秒");
        }

        @Override
        public void onFinish() {
            findPwdAuthCodeBtn.setEnabled(true);
            findPwdAuthCodeBtn.setText("发送验证码");
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
