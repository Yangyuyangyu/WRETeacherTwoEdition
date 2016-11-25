package com.teacherhelp.activity.personPic.photoview;


import android.view.View;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.teacherhelp.R;
import com.teacherhelp.base.BaseActivity;

import butterknife.Bind;
import cn.finalteam.galleryfinal.widget.zoonview.PhotoView;
import cn.finalteam.galleryfinal.widget.zoonview.PhotoViewAttacher;

/**
 * 点击风采图片进行预览
 */
public class PersonPicPreviewActivity extends BaseActivity {

    @Bind(R.id.preview_photoview)
    PhotoView previewPhotoview;

    private String path;

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_person_pic_preview);
    }

    @Override
    protected void initVariables() {
        path = getIntent().getStringExtra("photo_list");

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnFail(R.drawable.ic_gf_default_photo)
                .showImageForEmptyUri(R.drawable.ic_gf_default_photo)
                .showImageOnLoading(R.drawable.ic_gf_default_photo).build();
        if (path != null) {
            if (path.contains("http")) {//网络
                ImageLoader.getInstance().displayImage(path, previewPhotoview, options);
            } else {//本地
                ImageLoader.getInstance().displayImage("file:/" + path, previewPhotoview, options);
            }
        }
        previewPhotoview.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {

            @Override
            public void onPhotoTap(View arg0, float arg1, float arg2) {
                finish();
            }
        });
    }

    @Override
    protected void aadListenter() {

    }


}
