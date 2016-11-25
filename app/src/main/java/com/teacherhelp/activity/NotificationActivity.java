package com.teacherhelp.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.os.Vibrator;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import com.teacherhelp.R;
import com.teacherhelp.app.MyApplication;
import com.teacherhelp.base.BaseActivity;
import com.teacherhelp.utils.CommonUtils;
import com.teacherhelp.utils.ShareReferenceUtils;
import com.teacherhelp.view.UISwitchButton;

import butterknife.Bind;

/**
 * 新消息通知页面
 * Created by Administrator on 2016/7/25.
 */
public class NotificationActivity extends BaseActivity {
    @Bind(R.id.imgbtn_back)
    ImageButton imgbtnBack;
    @Bind(R.id.tv_titletwo)
    TextView tvTitletwo;
    @Bind(R.id.voice_switch)
    UISwitchButton voiceSwitch;
    @Bind(R.id.shake_switch)
    UISwitchButton shakeSwitch;
    @Bind(R.id.Nodisturbing_switch)
    UISwitchButton NodisturbingSwitch;

    private Vibrator vibrator = null;//振动
    private NotificationManager mNotificationManager;//获取通知管理类

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_notification);
    }

    @Override
    protected void aadListenter() {
        imgbtnBack.setOnClickListener(this);
        voiceSwitch.setOnClickListener(this);
        shakeSwitch.setOnClickListener(this);
        NodisturbingSwitch.setOnClickListener(this);

    }

    @Override
    protected void initVariables() {
        MyApplication.getInstance().addActivity(this);
        tvTitletwo.setVisibility(View.VISIBLE);
        tvTitletwo.setText("新消息通知");
        vibrator = (Vibrator) getApplication().getSystemService(Service.VIBRATOR_SERVICE);
        mNotificationManager = (NotificationManager) getSystemService(getApplicationContext().NOTIFICATION_SERVICE);


        if (ShareReferenceUtils.getValue("voiceSwitch") != null && ShareReferenceUtils.getValue("voiceSwitch").equals("true")) {
            voiceSwitch.setChecked(true);
        } else {
            voiceSwitch.setChecked(false);
        }
        if (ShareReferenceUtils.getValue("shakeSwitch") != null && ShareReferenceUtils.getValue("shakeSwitch").equals("true")) {
            shakeSwitch.setChecked(true);
        } else {
            shakeSwitch.setChecked(false);
        }
        if (ShareReferenceUtils.getValue("NodisturbingSwitch") != null && ShareReferenceUtils.getValue("NodisturbingSwitch").equals("true")) {
            NodisturbingSwitch.setChecked(true);
        } else {
            NodisturbingSwitch.setChecked(false);
        }

        voiceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    ShareReferenceUtils.putValue("voiceSwitch", "true");
                } else {
                    ShareReferenceUtils.putValue("voiceSwitch", "false");
                }
            }
        });
        shakeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    ShareReferenceUtils.putValue("shakeSwitch", "true");
                    vibrator.vibrate(500);//震动0.5秒
                } else {
                    ShareReferenceUtils.putValue("shakeSwitch", "false");
                    vibrator.cancel();
                }
            }
        });
        NodisturbingSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    ShareReferenceUtils.putValue("NodisturbingSwitch", "true");
                } else {
                    ShareReferenceUtils.putValue("NodisturbingSwitch", "false");
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.imgbtn_back:
                finish();
                break;
            case R.id.voice_switch: //声音选项

                break;
            case R.id.shake_switch: //震动选项

                break;
            case R.id.Nodisturbing_switch: //免打扰选项

                break;
        }
    }
}
