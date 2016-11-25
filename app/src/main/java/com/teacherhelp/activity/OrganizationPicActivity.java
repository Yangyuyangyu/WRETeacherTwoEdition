package com.teacherhelp.activity;

import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.teacherhelp.R;
import com.teacherhelp.app.MyApplication;
import com.teacherhelp.base.BaseActivity;

import butterknife.Bind;

/**
 * 机构相册
 * Created by Administrator on 2016/7/22.
 */
public class OrganizationPicActivity extends BaseActivity {
    @Bind(R.id.imgbtn_back)
    ImageButton imgbtnBack;
    @Bind(R.id.tv_titletwo)
    TextView tvTitletwo;
    @Bind(R.id.op_gridview)
    GridView gridView;

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_organizationphoto);
    }

    @Override
    protected void aadListenter() {
        imgbtnBack.setOnClickListener(this);
        tvTitletwo.setVisibility(View.VISIBLE);
        tvTitletwo.setText("机构相册");
    }

    @Override
    protected void initVariables() {
        MyApplication.getInstance().addActivity(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.imgbtn_back:
                finish();
                break;
        }
    }
}
