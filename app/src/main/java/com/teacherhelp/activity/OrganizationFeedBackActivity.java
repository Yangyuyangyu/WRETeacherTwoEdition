package com.teacherhelp.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.teacherhelp.R;
import com.teacherhelp.app.MyApplication;
import com.teacherhelp.base.BaseActivity;

import butterknife.Bind;

/**
 * 机构意见反馈页面
 * Created by Administrator on 2016/7/22.
 */
public class OrganizationFeedBackActivity extends BaseActivity {
    @Bind(R.id.imgbtn_back)
    ImageButton imgbtnBack;
    @Bind(R.id.tv_titletwo)
    TextView tvTitletwo;
    @Bind(R.id.feedback_et)
    EditText feedbackEt;
    @Bind(R.id.ratingbar)
    RatingBar ratingbar;
    @Bind(R.id.tv_titleright)
    TextView tvTitleright;

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_organizationfeedback);
    }

    @Override
    protected void aadListenter() {
        imgbtnBack.setOnClickListener(this);
    }

    @Override
    protected void initVariables() {
        MyApplication.getInstance().addActivity(this);
        tvTitletwo.setVisibility(View.VISIBLE);
        tvTitleright.setVisibility(View.VISIBLE);
        tvTitleright.setText("提交");
        tvTitletwo.setText("意见反馈");
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.imgbtn_back:
                finish();
                break;
            case R.id.tv_titleright: //提交
                break;
        }
    }
}
