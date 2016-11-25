package com.teacherhelp.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.teacherhelp.R;
import com.teacherhelp.activity.ApproveSettingActivity;
import com.teacherhelp.activity.ClassInComeActivity;
import com.teacherhelp.activity.MyCardActivity;
import com.teacherhelp.activity.OrganizationActivity;
import com.teacherhelp.activity.PersonDataActivty;
import com.teacherhelp.activity.SettingActivity;
import com.teacherhelp.app.MyApplication;
import com.teacherhelp.base.BaseFragment;
import com.teacherhelp.view.RoundImageView;

import java.io.File;

import butterknife.Bind;

/**
 * 我的Fragment
 * Created by Administrator on 2016/7/14.
 */
public class MineFragment extends BaseFragment {
    @Bind(R.id.imgbtn_back)
    ImageButton imgbtnBack;
    @Bind(R.id.tv_titletwo)
    TextView Title;
    @Bind(R.id.mine_img_head)
    RoundImageView ImgHead;
    @Bind(R.id.mine_tv_name)
    TextView TvName;
    @Bind(R.id.mine_tv_sign)
    TextView TvSign;
    @Bind(R.id.mine_rl_businesscard)
    RelativeLayout RlBusinesscard;
    @Bind(R.id.mine_rl_organization)
    RelativeLayout RlOrganization;
    @Bind(R.id.mine_rl_income)
    RelativeLayout RlIncome;
    @Bind(R.id.mine_ll_approve)
    LinearLayout LlApprove;
    @Bind(R.id.mine_ll_setting)
    LinearLayout LlSetting;
    @Bind(R.id.mine_rl_title)
    RelativeLayout RlTitle;

    @Override
    protected View initLayout(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mine, null);
    }

    @Override
    protected void initVariables() {
        imgbtnBack.setVisibility(View.GONE);
        Title.setVisibility(View.VISIBLE);
        Title.setText("我");
        if (MyApplication.userInfo != null && MyApplication.userInfo.getSummary() != null) {
            TvSign.setText(MyApplication.userInfo.getSummary());
        }
        if (MyApplication.userInfo != null && MyApplication.userInfo.getName() != null) {
            TvName.setText(MyApplication.userInfo.getName());
        }
//        File file = new File(Environment.getExternalStorageDirectory(), "/DCIM/" + "GalleryFinal/");
        File file = new File(Environment.getExternalStorageDirectory(), "/DCIM/");
        if (!file.exists()) {
            file.mkdirs();
        }
        File path = new File(file.getPath(), "head.jpg");
        Bitmap bt = BitmapFactory.decodeFile(path.getPath());// 从SD卡中找头像，转换成Bitmap
        if (bt != null) {
            @SuppressWarnings("deprecation")
            Drawable drawable = new BitmapDrawable(bt);// 转换成drawable
            ImgHead.setImageDrawable(drawable);
        } else {
            /**
             * 如果SD里面没有则需要从服务器取头像，取回来的头像再保存在SD中
             */
            if (MyApplication.userInfo != null && MyApplication.userInfo.getHeadImg() != null) {
                DisplayImageOptions options = new DisplayImageOptions.Builder()
                        .showImageOnFail(R.mipmap.my_icon_head)
                        .showImageForEmptyUri(R.mipmap.my_icon_head)
                        .showImageOnLoading(R.mipmap.my_icon_head).build();
                ImageLoader.getInstance().displayImage(MyApplication.userInfo.getHeadImg(), ImgHead, options);
            }
        }
    }

    @Override
    protected void addListener() {
        RlBusinesscard.setOnClickListener(this);
        RlOrganization.setOnClickListener(this);
        RlIncome.setOnClickListener(this);
        LlApprove.setOnClickListener(this);
        LlSetting.setOnClickListener(this);
        RlTitle.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.mine_rl_businesscard: //我的名片
                startActivity(new Intent(getActivity(), MyCardActivity.class));
                break;
            case R.id.mine_rl_organization: //我的机构
                startActivity(new Intent(getActivity(), OrganizationActivity.class));
                break;
            case R.id.mine_rl_income: //课程统计
                startActivity(new Intent(getActivity(), ClassInComeActivity.class));
                break;
            case R.id.mine_ll_approve: //认证设置
                startActivity(new Intent(getActivity(), ApproveSettingActivity.class));
                break;
            case R.id.mine_ll_setting: //设置
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
            case R.id.mine_rl_title: //个人设置
                Intent intent = new Intent(getActivity(), PersonDataActivty.class);
                startActivityForResult(intent, 222);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 222:
                if (resultCode == 222) { //刷新头像
//                    File file = new File(Environment.getExternalStorageDirectory(), "/DCIM/" + "GalleryFinal/");
                    File file = new File(Environment.getExternalStorageDirectory(), "/DCIM/");
                    if (!file.exists()) {
                        file.mkdirs();// 创建文件夹
                    }
                    File path = new File(file.getPath(), "head.jpg");
                    Bitmap bt = BitmapFactory.decodeFile(path.getPath());// 从SD卡中找头像，转换成Bitmap
                    if (bt != null) {
                        @SuppressWarnings("deprecation")
                        Drawable drawable = new BitmapDrawable(bt);// 转换成drawable
                        ImgHead.setImageDrawable(drawable);
                    } else {
                        /**
                         * 如果SD里面没有则需要从服务器取头像，取回来的头像再保存在SD中
                         */
                        if (MyApplication.userInfo != null && MyApplication.userInfo.getHeadImg() != null) {
                            DisplayImageOptions options = new DisplayImageOptions.Builder()
                                    .showImageOnFail(R.mipmap.my_icon_head)
                                    .showImageForEmptyUri(R.mipmap.my_icon_head)
                                    .showImageOnLoading(R.mipmap.my_icon_head).build();
                            ImageLoader.getInstance().displayImage(MyApplication.userInfo.getHeadImg(), ImgHead, options);
                        }
                    }
                }
                break;
            default:
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
