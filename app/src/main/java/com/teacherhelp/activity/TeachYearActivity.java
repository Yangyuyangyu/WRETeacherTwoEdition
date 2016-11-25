package com.teacherhelp.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.teacherhelp.R;
import com.teacherhelp.app.MyApplication;
import com.teacherhelp.base.BaseActivity;

import butterknife.Bind;

/**
 * 名称:教龄页面
 * 作用：设置用户教龄
 * Created on 2016/7/27.
 * 创建人：WangHaoMiao
 */
public class TeachYearActivity extends BaseActivity {
    @Bind(R.id.imgbtn_back)
    ImageButton imgbtnBack;
    @Bind(R.id.tv_titletwo)
    TextView tvTitletwo;
    @Bind(R.id.tv_titleright)
    TextView tvTitleright;
    @Bind(R.id.img_sub)
    ImageView imgSub;
    @Bind(R.id.et_content)
    TextView etContent;
    @Bind(R.id.img_add)
    ImageView imgAdd;

    /*用于设置教龄*/
    private int number=0;

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_teachyear);
    }

    @Override
    protected void aadListenter() {
        imgbtnBack.setOnClickListener(this);
        tvTitleright.setOnClickListener(this);
        imgAdd.setOnClickListener(this);
        imgSub.setOnClickListener(this);
    }

    @Override
    protected void initVariables() {
        MyApplication.getInstance().addActivity(this);
        tvTitletwo.setVisibility(View.VISIBLE);
        tvTitleright.setVisibility(View.VISIBLE);
        tvTitleright.setText("确定");
        tvTitletwo.setText("教龄");

        switch (etContent.getText().toString()){
            case "0":
                imgSub.setFocusable(false);
                imgSub.setBackgroundResource(R.mipmap.modify_teach_age_icon_sub_sel);
                imgAdd.setFocusable(true);
                imgAdd.setBackgroundResource(R.mipmap.modify_teach_age_icon_add_nor);
                break;
            case "20":
                imgSub.setFocusable(true);
                imgSub.setBackgroundResource(R.mipmap.modify_teach_age_icon_sub_nor);
                imgAdd.setFocusable(false);
                imgAdd.setBackgroundResource(R.mipmap.modify_teach_age_icon_add_sel);
                break;
        }
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
                if(number==0){
                    return;
                }
                Intent i = new Intent();
                i.putExtra("teacherOld", number);
                setResult(15, i);
                finish();
                break;
            case R.id.img_sub:
                if(etContent.getText().toString().equals("0")){
                    imgSub.setFocusable(false);
                    imgSub.setBackgroundResource(R.mipmap.modify_teach_age_icon_sub_sel);
                    number=0;
                    etContent.setText(""+number);
                }else{
                    imgAdd.setFocusable(true);
                    imgAdd.setBackgroundResource(R.mipmap.modify_teach_age_icon_add_nor);
                    number--;
                    etContent.setText(""+number);
                    if(etContent.getText().toString().equals("0")){
                        imgSub.setFocusable(false);
                        imgSub.setBackgroundResource(R.mipmap.modify_teach_age_icon_sub_sel);
                    }
                }
                break;
            case R.id.img_add:
                if(etContent.getText().toString().equals("20")){
                    imgAdd.setFocusable(false);
                    imgAdd.setBackgroundResource(R.mipmap.modify_teach_age_icon_add_sel);
                    number=20;
                    etContent.setText(""+number);
                }else{
                    imgSub.setFocusable(true);
                    imgSub.setBackgroundResource(R.mipmap.modify_teach_age_icon_sub_nor);
                    number++;
                    etContent.setText(""+number);
                    if(etContent.getText().toString().equals("20")){
                        imgAdd.setFocusable(false);
                        imgAdd.setBackgroundResource(R.mipmap.modify_teach_age_icon_add_sel);
                    }
                }
                break;
        }
    }
}
