package com.teacherhelp.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.teacherhelp.R;
import com.teacherhelp.adapter.GridViewHomeWorkAdapter;
import com.teacherhelp.app.MyApplication;
import com.teacherhelp.base.BaseActivity;
import com.teacherhelp.bean.TeachBean;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * 名称:批改作业页面
 * 作用：用户批改作业
 * Created on 2016/7/27.
 * 创建人：WangHaoMiao
 */
public class HwcdActivity extends BaseActivity {
    @Bind(R.id.imgbtn_back)
    ImageButton imgbtnBack;
    @Bind(R.id.tv_titletwo)
    TextView tvTitletwo;
    @Bind(R.id.tv_titleright)
    TextView tvTitleright;
    @Bind(R.id.hwcd_img_head)
    ImageView ImgHead;
    @Bind(R.id.hwcd_tv_name)
    TextView TvName;
    @Bind(R.id.hwcd_tv_sex)
    TextView TvSex;
    @Bind(R.id.hwcd_tv_class)
    TextView TvClass;
    @Bind(R.id.hwcd_tv_time)
    TextView TvTime;
    @Bind(R.id.hwcd_tv_timetwo)
    TextView TvTimetwo;
    @Bind(R.id.hwcd_tv_content)
    TextView TvContent;
    @Bind(R.id.hwcd_gridview)
    GridView Gridview;
    @Bind(R.id.hwcd_ll_select)
    LinearLayout LlSelect;
    @Bind(R.id.ratingbar_one)
    RatingBar ratingbarOne;
    @Bind(R.id.hwcd_et_content)
    EditText EtContent;

    private GridViewHomeWorkAdapter adapter;
    private ArrayList<TeachBean> imglist=new ArrayList<>();

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_hwcd);
    }

    @Override
    protected void aadListenter() {
        imgbtnBack.setOnClickListener(this);
        tvTitleright.setOnClickListener(this);
        LlSelect.setOnClickListener(this);
    }

    @Override
    protected void initVariables() {
        MyApplication.getInstance().addActivity(this);
        tvTitletwo.setVisibility(View.VISIBLE);
        tvTitleright.setVisibility(View.VISIBLE);
        tvTitleright.setText("确定");
        tvTitletwo.setText("批改作业");

        for (int i = 0; i <3; i++) {
            TeachBean teachbean=new TeachBean();
            teachbean.setImg(R.mipmap.ic_launcher);
            imglist.add(teachbean);
        }
        adapter=new GridViewHomeWorkAdapter(HwcdActivity.this,imglist);
        Gridview.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.imgbtn_back: //返回
                finish();
                saveEditTextAndCloseIMM();
                break;
            case R.id.tv_titleright: //确认
                break;
            case R.id.hwcd_ll_select: //查看原题
                break;
        }
    }
}
