package com.teacherhelp.activity;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.teacherhelp.R;
import com.teacherhelp.app.MyApplication;
import com.teacherhelp.base.BaseActivity;

import butterknife.Bind;

/**
 * 审批详情
 * Created by Administrator on 2016/7/25.
 */
public class LeaveDetailsActivity extends BaseActivity {
    @Bind(R.id.imgbtn_back)
    ImageButton imgbtnBack;
    @Bind(R.id.tv_titletwo)
    TextView tvTitletwo;
    @Bind(R.id.leavedetails_tv_name)
    TextView TvName;
    @Bind(R.id.leavedetails_tv_class)
    TextView TvClass;
    @Bind(R.id.leavedetails_tv_starttime)
    TextView TvStarttime;
    @Bind(R.id.leavedetails_tv_endtime)
    TextView TvEndtime;
    @Bind(R.id.leavedetails_tv_cause)
    TextView TvCause;
    @Bind(R.id.leavedetails_tv_type)
    TextView TvType;
    @Bind(R.id.leavedetails_tv_reson)
    TextView TvReson;

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_leavedetails);
    }

    @Override
    protected void aadListenter() {
        imgbtnBack.setOnClickListener(this);
    }

    @Override
    protected void initVariables() {
        MyApplication.getInstance().addActivity(this);
        tvTitletwo.setVisibility(View.VISIBLE);
        tvTitletwo.setText("审批详情");
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
