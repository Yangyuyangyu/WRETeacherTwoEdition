package com.teacherhelp.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.WriterException;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.teacherhelp.R;
import com.teacherhelp.app.MyApplication;
import com.teacherhelp.base.BaseActivity;
import com.teacherhelp.view.RoundImageView;
import com.teacherhelp.zxing.encode.CodeCreator;

import java.io.File;

import butterknife.Bind;

/**
 * 我的名片页面
 * Created by Administrator on 2016/7/20.
 */
public class MyCardActivity extends BaseActivity {
    @Bind(R.id.imgbtn_back)
    ImageButton imgbtnBack;
    @Bind(R.id.tv_titletwo)
    TextView tvTitletwo;
    @Bind(R.id.card_img_head)
    RoundImageView ImgHead;
    @Bind(R.id.card_tv_name)
    TextView TvName;
    @Bind(R.id.card_image)
    ImageView Image;

    //头像
    private Bitmap head;// 头像Bitmap
    private static String path = "/sdcard/myHead/";// sd路径

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_mycard);
    }

    @Override
    protected void aadListenter() {
        imgbtnBack.setOnClickListener(this);
    }

    @Override
    protected void initVariables() {
        MyApplication.getInstance().addActivity(this);
        tvTitletwo.setVisibility(View.VISIBLE);
        tvTitletwo.setText("我的名片");

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
             */if (MyApplication.userInfo.getHeadImg() != null) {
                DisplayImageOptions options = new DisplayImageOptions.Builder()
                        .showImageOnFail(R.mipmap.my_icon_head)
                        .showImageForEmptyUri(R.mipmap.my_icon_head)
                        .showImageOnLoading(R.mipmap.my_icon_head).build();
                ImageLoader.getInstance().displayImage(MyApplication.userInfo.getHeadImg(), ImgHead, options);

            }
        }

        String url = "https://www.baidu.com/";
        try {
            Bitmap bitmap = CodeCreator.createQRCode(url);
            Image.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.imgbtn_back:
                finish();
                break;
        }
    }
}
