package com.teacherhelp.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.teacherhelp.R;
import com.teacherhelp.app.MyApplication;
import com.teacherhelp.base.BaseActivity;
import com.teacherhelp.dialog.BirthDayDialog;
import com.teacherhelp.dialog.TimeDialog;

import butterknife.Bind;

/**
 * 名称:老师请假详情
 * 作用:修改和新建老师请假详情
 * Created on 2016/7/28.
 * 创建人:WangHaoMiao
 */
public class TeachAmendLeaveActivity extends BaseActivity {
    @Bind(R.id.imgbtn_back)
    ImageButton imgbtnBack;
    @Bind(R.id.tv_titletwo)
    TextView tvTitletwo;
    @Bind(R.id.tv_titleright)
    TextView tvTitleright;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.rl_origanation)
    RelativeLayout rlOriganation;
    @Bind(R.id.tv_school)
    TextView tvSchool;
    @Bind(R.id.rl_school)
    RelativeLayout rlSchool;
    @Bind(R.id.tv_starttime)
    TextView tvStarttime;
    @Bind(R.id.rl_starttime)
    RelativeLayout rlStarttime;
    @Bind(R.id.tv_endtime)
    TextView tvEndtime;
    @Bind(R.id.rl_endtime)
    RelativeLayout rlEndtime;
    @Bind(R.id.et_content)
    EditText etContent;
    @Bind(R.id.btn_delete)
    Button btnDelete;

    /*选择年月日的Dialog*/
    private BirthDayDialog birthDayDialog;
    private TimeDialog timeDialog;

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_teachamendleave);
    }

    @Override
    protected void aadListenter() {
        imgbtnBack.setOnClickListener(this);
        tvTitleright.setOnClickListener(this);
        rlOriganation.setOnClickListener(this);
        rlSchool.setOnClickListener(this);
        rlStarttime.setOnClickListener(this);
        rlEndtime.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
    }

    @Override
    protected void initVariables() {
        MyApplication.getInstance().addActivity(this);
        if(getIntent().getStringExtra("type")!=null){
            tvTitletwo.setVisibility(View.VISIBLE);
            tvTitleright.setVisibility(View.VISIBLE);
            tvTitleright.setText("保存");
            switch (getIntent().getStringExtra("type")){
                case "add": //新建
                    tvTitletwo.setText("新建请假");
                    btnDelete.setVisibility(View.GONE);
                    break;
                case "update": //修改
                    tvTitletwo.setText("修改请假");
                    btnDelete.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.imgbtn_back: //返回
                finish();
                saveEditTextAndCloseIMM();
                break;
            case R.id.tv_titleright: //保存
                break;
            case R.id.rl_origanation: //选择机构
                startActivity(new Intent(TeachAmendLeaveActivity.this,OrganizationChooseActivity.class));
                break;
            case R.id.rl_school: //选择分校
                startActivity(new Intent(TeachAmendLeaveActivity.this,SchoolChooseActivity.class));
                break;
            case R.id.rl_starttime: //选择开始时间
                String[] array = {"1950", "01", "01","01","01"};
                timeDialog=new TimeDialog(TeachAmendLeaveActivity.this, array, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        timeDialog.dismiss();
                    }
                });
                timeDialog.showDialog();
//                if (Utility.StringIsNull(tvStarttime.getText().toString())) {
//                    String[] array = {"1950", "01", "01"};
//                    birthDayDialog = new BirthDayDialog(TeachAmendLeaveActivity.this, array, new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            if (!Utility.StringIsNull(birthDayDialog.GetText())) {
//                                tvStarttime.setText(birthDayDialog.GetText());
//                            } else {
//                                tvStarttime.setText("1950-01-01");
//                            }
//                            birthDayDialog.dismiss();
//                        }
//                    });
//                    birthDayDialog.showDialog();
//                } else {
//                    birthDayDialog = new BirthDayDialog(TeachAmendLeaveActivity.this, convertStrToArray(tvStarttime.getText().toString()), new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            if (!Utility.StringIsNull(birthDayDialog.GetText())) {
//                                tvStarttime.setText(birthDayDialog.GetText());
//                            }
//                            birthDayDialog.dismiss();
//                        }
//                    });
//                    birthDayDialog.showDialog();
//                }
                break;
            case R.id.rl_endtime: //选择结束时间
//                if (Utility.StringIsNull(tvEndtime.getText().toString())) {
//                    String[] array = {"1950", "01", "01"};
//                    birthDayDialog = new BirthDayDialog(TeachAmendLeaveActivity.this, array, new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            if (!Utility.StringIsNull(birthDayDialog.GetText())) {
//                                tvEndtime.setText(birthDayDialog.GetText());
//                            } else {
//                                tvEndtime.setText("1950-01-01");
//                            }
//                            birthDayDialog.dismiss();
//                        }
//                    });
//                    birthDayDialog.showDialog();
//                } else {
//                    birthDayDialog = new BirthDayDialog(TeachAmendLeaveActivity.this, convertStrToArray(tvEndtime.getText().toString()), new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            if (!Utility.StringIsNull(birthDayDialog.GetText())) {
//                                tvEndtime.setText(birthDayDialog.GetText());
//                            }
//                            birthDayDialog.dismiss();
//                        }
//                    });
//                    birthDayDialog.showDialog();
//                }
                break;
            case R.id.btn_delete: //删除
                break;
        }
    }

    //使用String的split 方法
    public static String[] convertStrToArray(String str) {
        String[] strArray = null;
        strArray = str.split("-"); //拆分字符为"," ,然后把结果交给数组strArray
        return strArray;
    }
}
