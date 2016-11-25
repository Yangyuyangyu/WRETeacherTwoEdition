package com.teacherhelp.base;

import android.app.Dialog;
import android.content.Context;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.teacherhelp.R;


/**
 *
 * 从底部往上弹出
 * Created by Administrator on 2016/5/24.
 */
public class BaseDialog extends Dialog{
    private Window window;

    public BaseDialog(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    public void showDialog(){
        window = getWindow(); // 得到对话框
        window.setWindowAnimations(R.style.bottom_anim_style); // 设置窗口弹出动画
        window.setBackgroundDrawableResource(R.color.translucent_background); // 设置对话框背景为透明
        WindowManager.LayoutParams wl = window.getAttributes();
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        window.setAttributes(wl);
        this.setCanceledOnTouchOutside(true);
        show();
    }

}
