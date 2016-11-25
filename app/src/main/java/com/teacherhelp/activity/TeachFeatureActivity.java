package com.teacherhelp.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.teacherhelp.R;
import com.teacherhelp.app.MyApplication;
import com.teacherhelp.base.BaseActivity;
import com.teacherhelp.view.ClearEditText;

import butterknife.Bind;

/**
 * 名称:教学特点页面
 * 作用：设置用户教学特点
 * Created on 2016/7/27.
 * 创建人：WangHaoMiao
 */
public class TeachFeatureActivity extends BaseActivity {
    @Bind(R.id.imgbtn_back)
    ImageButton imgbtnBack;
    @Bind(R.id.tv_titletwo)
    TextView tvTitletwo;
    @Bind(R.id.tv_titleright)
    TextView tvTitleright;
    @Bind(R.id.et_content)
    ClearEditText etContent;

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_teachfeature);
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
        tvTitleright.setText("确定");
        tvTitletwo.setText("教学特点");
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.imgbtn_back:
                finish();
                saveEditTextAndCloseIMM();
                break;
            case R.id.tv_titleright:
                Intent i = new Intent();
                i.putExtra("teachFeature", etContent.getText().toString());
                setResult(22, i);
                finish();
                break;
        }
    }
}
