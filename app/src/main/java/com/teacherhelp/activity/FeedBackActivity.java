package com.teacherhelp.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.teacherhelp.R;
import com.teacherhelp.app.MyApplication;
import com.teacherhelp.base.BaseActivity;

import butterknife.Bind;

/**
 * 意见反馈页面
 * Created by Administrator on 2016/7/24.
 */
public class FeedBackActivity extends BaseActivity {
    @Bind(R.id.imgbtn_back)
    ImageButton imgbtnBack;
    @Bind(R.id.tv_titletwo)
    TextView tvTitletwo;
    @Bind(R.id.tv_titleright)
    TextView tvTitleright;
    @Bind(R.id.feedback_et_text)
    EditText EtText;

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_feedback);
    }

    @Override
    protected void aadListenter() {
        imgbtnBack.setOnClickListener(this);
        tvTitleright.setOnClickListener(this);
    }

    @Override
    protected void initVariables() {
        MyApplication.getInstance().addActivity(this);
        tvTitletwo.setVisibility(View.VISIBLE);
        tvTitleright.setVisibility(View.VISIBLE);
        tvTitletwo.setText("意见反馈");
        tvTitleright.setText("提交");
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.imgbtn_back:
                finish();
                saveEditTextAndCloseIMM();
                break;
            case R.id.tv_titleright:
                break;
        }
    }
}
