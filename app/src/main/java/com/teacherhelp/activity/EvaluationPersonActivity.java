package com.teacherhelp.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.teacherhelp.R;
import com.teacherhelp.app.MyApplication;
import com.teacherhelp.base.BaseActivity;

import butterknife.Bind;

/**
 * 名称:个人评价
 * 作用：单个评价某个同学的评分
 * Created on 2016/7/26.
 * 创建人：WangHaoMiao
 */
public class EvaluationPersonActivity extends BaseActivity {
    @Bind(R.id.imgbtn_back)
    ImageButton imgbtnBack;
    @Bind(R.id.tv_titletwo)
    TextView tvTitletwo;
    @Bind(R.id.tv_titleright)
    TextView tvTitleright;
    @Bind(R.id.ep_img_head)
    ImageView ImgHead;
    @Bind(R.id.ep_tv_name)
    TextView TvName;
    @Bind(R.id.ep_tv_sex)
    TextView TvSex;
    @Bind(R.id.ep_tv_class)
    TextView TvClass;
    @Bind(R.id.ep_tv_time)
    TextView TvTime;
    @Bind(R.id.ep_tv_timetwo)
    TextView TvTimetwo;
    @Bind(R.id.ratingbar_one)
    RatingBar ratingbarOne;
    @Bind(R.id.ratingbar_two)
    RatingBar ratingbarTwo;
    @Bind(R.id.ep_et_content)
    EditText EtContent;

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_evaluationperson);
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
        tvTitletwo.setText("个人评价");
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
                break;
        }
    }
}
