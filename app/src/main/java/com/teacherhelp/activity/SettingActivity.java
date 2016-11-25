package com.teacherhelp.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.teacherhelp.R;
import com.teacherhelp.app.MyApplication;
import com.teacherhelp.base.BaseActivity;
import com.teacherhelp.utils.LogHelper;
import com.teacherhelp.utils.Utility;

import butterknife.Bind;

/**
 * 设置页面
 * Created by Administrator on 2016/7/22.
 */
public class SettingActivity extends BaseActivity {
    @Bind(R.id.imgbtn_back)
    ImageButton imgbtnBack;
    @Bind(R.id.tv_titletwo)
    TextView tvTitletwo;
    @Bind(R.id.setting_rl_change)
    RelativeLayout RlChange;
    @Bind(R.id.setting_rl_nocation)
    RelativeLayout RlNocation;
    @Bind(R.id.setting_rl_feedback)
    RelativeLayout RlFeedback;
    @Bind(R.id.setting_tv_phone)
    TextView TvPhone;
    @Bind(R.id.setting_rl_phone)
    RelativeLayout RlPhone;
    @Bind(R.id.setting_rl_aboutus)
    RelativeLayout RlAboutus;
    @Bind(R.id.setting_btn_exit)
    Button BtnExit;
    @Bind(R.id.setting_tv_versions)
    TextView TvVersions;
    @Bind(R.id.setting_rl_help)
    RelativeLayout RlHelp;

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_setting);
    }

    @Override
    protected void aadListenter() {
        imgbtnBack.setOnClickListener(this);
        RlChange.setOnClickListener(this);
        RlNocation.setOnClickListener(this);
        RlFeedback.setOnClickListener(this);
        RlPhone.setOnClickListener(this);
        RlAboutus.setOnClickListener(this);
        BtnExit.setOnClickListener(this);
        RlHelp.setOnClickListener(this);
    }

    @Override
    protected void initVariables() {
        MyApplication.getInstance().addActivity(this);
        tvTitletwo.setVisibility(View.VISIBLE);
        tvTitletwo.setText("设置");
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.imgbtn_back:
                finish();
                break;
            case R.id.setting_rl_change: //修改密码
                startActivity(new Intent(SettingActivity.this, SendPassWorldActivity.class));
                break;
            case R.id.setting_rl_nocation: //新消息通知
                startActivity(new Intent(SettingActivity.this, NotificationActivity.class));
                break;
            case R.id.setting_rl_feedback: //意见反馈
                startActivity(new Intent(SettingActivity.this, FeedBackActivity.class));
                break;
            case R.id.setting_rl_phone: //客服电话
                Utility.showTextDialog(RlPhone, "是否拨打电话：15982407336", SettingActivity.this, "确定", "取消", new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:15982407336"));
                        try {
                            startActivity(intent);
                        } catch (Exception e) {
                            LogHelper.i(e.getMessage());
                            Utility.showToast("拨打电话的权限未开启，请检查您的权限", SettingActivity.this);
                        }
                    }
                });
                break;
            case R.id.setting_rl_help: //帮助指南
                startActivity(new Intent(SettingActivity.this, HelpActivity.class));
                break;
            case R.id.setting_rl_aboutus: //关于我们
                startActivity(new Intent(SettingActivity.this, AboutUsActivity.class));
                break;
            case R.id.setting_btn_exit: //退出登录
                Utility.showTextDialog(BtnExit, "退出登录后将无法收到通知和消息，是否继续?", SettingActivity.this, "确定", "取消", new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        MyApplication.getInstance().exit();
                        startActivity(new Intent(SettingActivity.this, LoginActivity.class));
                    }
                });
                break;
        }
    }

}
