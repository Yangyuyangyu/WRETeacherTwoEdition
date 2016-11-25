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
 * 视频展示
 * Created by Administrator on 2016/7/22.
 */
public class OrganizationVidioActivity extends BaseActivity {
    @Bind(R.id.imgbtn_back)
    ImageButton imgbtnBack;
    @Bind(R.id.tv_titletwo)
    TextView tvTitletwo;
    @Bind(R.id.ov_gridview)
    GridView Gridview;

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_organizationvidio);
    }

    @Override
    protected void aadListenter() {
        imgbtnBack.setOnClickListener(this);

    }

    @Override
    protected void initVariables() {
        MyApplication.getInstance().addActivity(this);
        tvTitletwo.setVisibility(View.VISIBLE);
        tvTitletwo.setText("视频相册");
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
