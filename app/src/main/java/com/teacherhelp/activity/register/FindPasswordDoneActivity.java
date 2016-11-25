package com.teacherhelp.activity.register;


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
 * 找回密码修改完成
 */
public class FindPasswordDoneActivity extends BaseActivity {
    @Bind(R.id.imgbtn_back)
    ImageButton imgbtnBack;
    @Bind(R.id.tv_titletwo)
    TextView tvTitletwo;
    @Bind(R.id.tv_titleright)
    TextView tvTitleright;
    @Bind(R.id.findPwd_passwordNew)
    EditText findPwdPasswordNew;
    @Bind(R.id.findPwd_passwordOK)
    EditText findPwdPasswordOK;

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_find_password_done);
    }

    @Override
    protected void initVariables() {
        MyApplication.getInstance().addForgetActivity(this);
        tvTitletwo.setVisibility(View.VISIBLE);
        tvTitleright.setVisibility(View.VISIBLE);
        tvTitletwo.setText("找回密码3/3");
        tvTitleright.setText("完成");

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
                MyApplication.getInstance().finishForgetActivity();
                break;
        }
    }
}
