package com.teacherhelp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.teacherhelp.R;
import com.teacherhelp.app.MyApplication;
import com.teacherhelp.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 名称:性别页面
 * 作用:设置用户性别
 * Created on 2016/7/26.
 * 创建人:WangHaoMiao
 */
public class SexActivity extends BaseActivity {
    @Bind(R.id.imgbtn_back)
    ImageButton imgbtnBack;
    @Bind(R.id.tv_titletwo)
    TextView tvTitletwo;
    @Bind(R.id.tv_titleright)
    TextView tvTitleright;
    @Bind(R.id.rb_man)
    RadioButton rbMan;
    @Bind(R.id.rd_woman)
    RadioButton rdWoman;
    @Bind(R.id.radioGroup)
    RadioGroup radioGroup;

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_sex);
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
        tvTitletwo.setText("性别");
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
                String sex = null;
                if (rbMan.isChecked()) {
                    sex = "1";
                } else if (rdWoman.isChecked()) {
                    sex = "2";
                }
                if (sex == null) {
                    return;
                }
                Intent i = new Intent();
                i.putExtra("sex", sex);
                setResult(13, i);
                finish();
                break;
        }
    }
}
