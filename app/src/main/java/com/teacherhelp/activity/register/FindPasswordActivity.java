package com.teacherhelp.activity.register;


import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.teacherhelp.R;
import com.teacherhelp.app.MyApplication;
import com.teacherhelp.base.BaseActivity;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 找回密码
 */
public class FindPasswordActivity extends BaseActivity {
    @Bind(R.id.imgbtn_back)
    ImageButton imgbtnBack;
    @Bind(R.id.tv_titletwo)
    TextView tvTitletwo;
    @Bind(R.id.tv_titleright)
    TextView tvTitleright;
    @Bind(R.id.findPwd_phone)
    EditText phone;

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_find_password);
    }

    @Override
    protected void initVariables() {
        MyApplication.getInstance().addForgetActivity(this);
        tvTitletwo.setVisibility(View.VISIBLE);
        tvTitleright.setVisibility(View.VISIBLE);
        tvTitletwo.setText("找回密码1/3");
        tvTitleright.setText("下一步");

    }

    @Override
    protected void aadListenter() {

    }

    @OnClick({R.id.imgbtn_back, R.id.tv_titleright})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgbtn_back:
                finish();
                break;
            case R.id.tv_titleright:
                if (TextUtils.isEmpty(phone.getText().toString())) {
                    return;
                }
                if (phone.getText().length() < 11) {
                    return;
                }
                Intent intent = new Intent(FindPasswordActivity.this, FindPasswordTwoActivity.class);
                startActivity(intent);
                break;
        }
    }
}
