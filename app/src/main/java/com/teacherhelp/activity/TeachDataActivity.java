package com.teacherhelp.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.teacherhelp.R;
import com.teacherhelp.app.MyApplication;
import com.teacherhelp.base.BaseActivity;
import com.teacherhelp.utils.LogHelper;
import com.teacherhelp.utils.Utility;
import com.teacherhelp.view.RoundImageView;

import butterknife.Bind;
import io.rong.imkit.RongIM;

/**
 * 名称:教师资料页面
 * 作用：展示教师资料
 * Created on 2016/7/26.
 * 创建人：WangHaoMiao
 */
public class TeachDataActivity extends BaseActivity {
    @Bind(R.id.imgbtn_back)
    ImageButton imgbtnBack;
    @Bind(R.id.tv_titletwo)
    TextView tvTitletwo;
    @Bind(R.id.td_img_head)
    RoundImageView ImgHead;
    @Bind(R.id.td_tv_name)
    TextView TvName;
    @Bind(R.id.td_img_sex)
    ImageView ImgSex;
    @Bind(R.id.td_ratingbar)
    RatingBar Ratingbar;
    @Bind(R.id.td_tv_grade)
    TextView TvGrade;
    @Bind(R.id.teachdata_tv_educationyear)
    TextView TvEducationyear;
    @Bind(R.id.teachdata_tv_education)
    TextView TvEducation;
    @Bind(R.id.teachdata_tv_major)
    TextView TvMajor;
    @Bind(R.id.teachdata_tv_school)
    TextView TvSchool;
    @Bind(R.id.teachdata_ll_pe)
    LinearLayout LlPe;
    @Bind(R.id.teachdata_ll_share)
    LinearLayout LlShare;
    @Bind(R.id.teachdata_ll_photo)
    LinearLayout LlPhoto;
    @Bind(R.id.teachdata_ll_vidio)
    LinearLayout LlVidio;
    @Bind(R.id.ll_message)
    LinearLayout llMessage;
    @Bind(R.id.ll_call)
    LinearLayout llCall;
    @Bind(R.id.ll_care)
    LinearLayout llCare;

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_teachdata);
    }

    @Override
    protected void aadListenter() {
        imgbtnBack.setOnClickListener(this);
        LlPe.setOnClickListener(this);
        LlShare.setOnClickListener(this);
        LlPhoto.setOnClickListener(this);
        LlVidio.setOnClickListener(this);
        llMessage.setOnClickListener(this);
        llCall.setOnClickListener(this);
        llCare.setOnClickListener(this);
    }

    @Override
    protected void initVariables() {
        MyApplication.getInstance().addActivity(this);
        tvTitletwo.setVisibility(View.VISIBLE);
        tvTitletwo.setText("教师资料");
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.imgbtn_back: //返回
                finish();
                break;

            case R.id.teachdata_ll_pe: //个人经历
                startActivity(new Intent(TeachDataActivity.this,PersonalExperienceActivity.class));
                break;

            case R.id.teachdata_ll_share: //成果分享
                startActivity(new Intent(TeachDataActivity.this,ShareTeachActivity.class));
                break;

            case R.id.teachdata_ll_photo: //个人风采
                startActivity(new Intent(TeachDataActivity.this,PersonPicActivity.class));
                break;

            case R.id.teachdata_ll_vidio: //视频相册
                startActivity(new Intent(TeachDataActivity.this,OrganizationVidioActivity.class));
                break;
            case R.id.ll_call: //打电话
                Utility.showTextDialog(llCall, "是否拨打电话：15756242976", TeachDataActivity.this, "确定", "取消", new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:15756242976"));
                        try {
                            startActivity(intent);
                        } catch (Exception e) {
                            LogHelper.i(e.getMessage());
                            Utility.showToast("拨打电话的权限未开启，请检查您的权限",TeachDataActivity.this);
                        }
                    }
                });
                break;
            case R.id.ll_message: //发消息
                /**
                 * 启动单聊界面。
                 *
                 * @param context      应用上下文。
                 * @param targetUserId 要与之聊天的用户 Id。
                 * @param title        聊天的标题，如果传入空值，则默认显示与之聊天的用户名称。
                 */
                RongIM.getInstance().startPrivateChat(TeachDataActivity.this, "15756242976", "");
                break;
            case R.id.ll_care: //关注
                break;
        }
    }

}
