package com.teacherhelp.base;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.toolbox.StringRequest;
import com.teacherhelp.R;
import com.teacherhelp.app.MyApplication;
import com.teacherhelp.utils.StatusBarUtil;

import butterknife.ButterKnife;


/**
 * Created by Mr-fang on 2016/1/13.
 */
public abstract class BaseActivity extends FragmentActivity implements View.OnClickListener {

    protected Dialog webDialog;
    private InputMethodManager imm;


    /**
     * 等待菊花提示框
     */
    public void showWaitLoad(Context context, String msg){
        try {
            LayoutInflater inflater = LayoutInflater.from(context);
            View v = inflater.inflate(R.layout.loading_dialog, null);// 得到加载view
            LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
            ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
            TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
            // 加载动画
            Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                    context, R.anim.loading_animation);
            // 使用ImageView显示动画
            spaceshipImage.startAnimation(hyperspaceJumpAnimation);
            tipTextView.setText(msg);// 设置加载信息
            if(webDialog==null)
                webDialog = new Dialog(context, R.style.dialog_popup);// 创建自定义样式dialog
            webDialog.setCanceledOnTouchOutside(false);//点击外部不可以取消关闭窗体
            webDialog.setCancelable(true);// 不可以用“返回键”取消
            Window window = webDialog.getWindow();
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.dimAmount = 0f;
            window.setAttributes(lp);
            webDialog.setContentView(layout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
            webDialog.show();
        } catch (Exception e) {
            // TODO: handle exception
            e.getStackTrace();
        }
    }

    public void DismissDialog() {
        if(webDialog!=null){
            webDialog.dismiss();
            webDialog=null;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //管理activity
        MyApplication.getInstance().setActivity(this);
        super.onCreate(savedInstanceState);

        //初始化布局
        initLayout();
        //添加ButterKife框架
        ButterKnife.bind(this);
        //初始化变量
        initVariables();
        //添加监听时间
        aadListenter();

        setStatusBar();

    }
    protected abstract void initLayout();
    protected abstract void aadListenter();
    protected abstract void initVariables();

    @Override
    public void onClick(View v) {

    }

    public void onBack(View view) {
        finish();
        saveEditTextAndCloseIMM();
    }


    /**
     *
     */
    protected void saveEditTextAndCloseIMM() {
        /** 隐藏软键盘 **/
        View view = getWindow().peekDecorView();
        if (view != null) {
            imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        return;
    }



    protected  void setTmie(StringRequest request){
        request.setRetryPolicy( new DefaultRetryPolicy( 20*1000,//默认超时时间，应设置一个稍微大点儿的，例如本处的500000
                0,//默认最大尝试次数
                1.0f ) );


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    protected void setStatusBar() {
        StatusBarUtil.setColor(this,getResources().getColor(R.color.rer_color));
    }


}

