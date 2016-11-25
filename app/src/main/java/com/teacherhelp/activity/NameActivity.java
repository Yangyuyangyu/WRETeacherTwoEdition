package com.teacherhelp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.teacherhelp.R;
import com.teacherhelp.app.MyApplication;
import com.teacherhelp.base.BaseActivity;
import com.teacherhelp.view.ClearEditText;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 名称:姓名页面
 * 作用:设置用户姓名
 * Created on 2016/7/26.
 * 创建人:WangHaoMiao
 */
public class NameActivity extends BaseActivity {
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
        setContentView(R.layout.activity_name);
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
        tvTitletwo.setText("姓名");
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
                i.putExtra("name", etContent.getText().toString());
                setResult(12, i);
                finish();
                break;
        }
    }
}
