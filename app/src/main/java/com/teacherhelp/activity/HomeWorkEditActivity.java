package com.teacherhelp.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.actionsheet.ActionSheet;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.teacherhelp.R;
import com.teacherhelp.activity.personPic.ChoosePhotoListAdapter;
import com.teacherhelp.activity.personPic.listener.UILPauseOnScrollListener;
import com.teacherhelp.activity.personPic.loader.UILImageLoader;
import com.teacherhelp.app.MyApplication;
import com.teacherhelp.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.PauseOnScrollListener;
import cn.finalteam.galleryfinal.PhotoEditActivity;
import cn.finalteam.galleryfinal.PhotoPreviewActivity;
import cn.finalteam.galleryfinal.ThemeConfig;
import cn.finalteam.galleryfinal.model.PhotoInfo;

/**
 * 名称:编辑作业
 * 作用：用户编辑作业
 * Created on 2016/7/27.
 * 创建人：WangHaoMiao
 */
public class HomeWorkEditActivity extends BaseActivity {
    @Bind(R.id.imgbtn_back)
    ImageButton imgbtnBack;
    @Bind(R.id.tv_titletwo)
    TextView tvTitletwo;
    @Bind(R.id.tv_titleright)
    TextView tvTitleright;
    @Bind(R.id.eh_img_head)
    ImageView ImgHead;
    @Bind(R.id.eh_tv_name)
    TextView TvName;
    @Bind(R.id.eh_tv_sex)
    TextView TvSex;
    @Bind(R.id.eh_tv_class)
    TextView TvClass;
    @Bind(R.id.eh_tv_time)
    TextView TvTime;
    @Bind(R.id.eh_tv_timetwo)
    TextView TvTimetwo;
    @Bind(R.id.eh_et_content)
    EditText EtContent;
    @Bind(R.id.eh_ll_other)
    LinearLayout LlOther;//附件
    @Bind(R.id.eh_gridview)
    GridView mGridView;

    private final int REQUEST_CODE_CAMERA = 1000;
    private final int REQUEST_CODE_GALLERY = 1001;
    private final int REQUEST_CODE_EDIT = 1003;
    private ArrayList<PhotoInfo> mPhotoList;
    private ChoosePhotoListAdapter mChoosePhotoListAdapter;

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_edithomework);
    }

    @Override
    protected void aadListenter() {
        imgbtnBack.setOnClickListener(this);
        tvTitleright.setOnClickListener(this);
        LlOther.setOnClickListener(this);
    }

    @Override
    protected void initVariables() {
        MyApplication.getInstance().addActivity(this);
        tvTitletwo.setVisibility(View.VISIBLE);
        tvTitleright.setVisibility(View.VISIBLE);
        tvTitleright.setText("确定");
        tvTitletwo.setText("编辑作业");

        mPhotoList = new ArrayList<>();
        mChoosePhotoListAdapter = new ChoosePhotoListAdapter(this, mPhotoList);

        mGridView.setAdapter(mChoosePhotoListAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (mPhotoList != null) {
                    //图片预览
                    ArrayList<PhotoInfo> preview = new ArrayList<PhotoInfo>();
                    preview.add(mPhotoList.get(i));
                    Intent intent = new Intent(HomeWorkEditActivity.this, PhotoPreviewActivity.class);
                    intent.putExtra("photo_list", preview);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.imgbtn_back: //返回
                finish();
                saveEditTextAndCloseIMM();
                break;
            case R.id.tv_titleright: //确认
                break;
            case R.id.eh_ll_other: //附件
                dialog();
                break;
        }
    }

    private void dialog() {
        //建议在application中配置GalleryFinal
        ThemeConfig themeConfig = null;

        themeConfig = ThemeConfig.DEFAULT;

        ThemeConfig theme = new ThemeConfig.Builder()
                .setTitleBarBgColor(Color.rgb(0xFF, 0x57, 0x22))
                .setTitleBarTextColor(Color.BLACK)
                .setTitleBarIconColor(Color.BLACK)
                .setFabNornalColor(Color.RED)
                .setFabPressedColor(Color.BLUE)
                .setCheckNornalColor(Color.WHITE)
                .setCheckSelectedColor(Color.BLACK)
                .setIconBack(R.mipmap.ic_action_previous_item)
                .setIconRotate(R.mipmap.ic_action_repeat)
                .setIconCrop(R.mipmap.ic_action_crop)
                .setIconCamera(R.mipmap.ic_action_camera)
                .build();
        themeConfig = theme;


        FunctionConfig.Builder functionConfigBuilder = new FunctionConfig.Builder();
        cn.finalteam.galleryfinal.ImageLoader imageLoader;
        PauseOnScrollListener pauseOnScrollListener = null;
        imageLoader = new UILImageLoader();
        pauseOnScrollListener = new UILPauseOnScrollListener(false, true);

        functionConfigBuilder.setMutiSelectMaxSize(32)
                .setEnableEdit(true)
                .setEnableRotate(true)
                .setEnableCrop(true)
                .setCropSquare(true)
                .setForceCrop(false)
                .setEnableCamera(true)
                .setEnablePreview(true);
        functionConfigBuilder.setSelected(mPhotoList);//添加已选集合
        final FunctionConfig functionConfig = functionConfigBuilder.build();

        CoreConfig coreConfig = new CoreConfig.Builder(HomeWorkEditActivity.this, imageLoader, themeConfig)
                .setFunctionConfig(functionConfig)
                .setPauseOnScrollListener(pauseOnScrollListener)
                .setNoAnimcation(false)
                .build();
        GalleryFinal.init(coreConfig);

        ActionSheet.createBuilder(HomeWorkEditActivity.this, getSupportFragmentManager())
                .setCancelButtonTitle("取消")
                .setOtherButtonTitles("打开相册", "拍照", "编辑")
                .setCancelableOnTouchOutside(true)
                .setListener(new ActionSheet.ActionSheetListener() {
                    @Override
                    public void onDismiss(ActionSheet actionSheet, boolean isCancel) {

                    }

                    @Override
                    public void onOtherButtonClick(ActionSheet actionSheet, int index) {
                        switch (index) {
                            case 0:
                                GalleryFinal.openGalleryMuti(REQUEST_CODE_GALLERY, functionConfig, mOnHanlderResultCallback);

                                break;
                            case 1:
                                GalleryFinal.openCamera(REQUEST_CODE_CAMERA, functionConfig, mOnHanlderResultCallback);
                                break;
                            case 2:
//                                GalleryFinal.openEdit(REQUEST_CODE_EDIT, functionConfig, mPhotoList.get(0).getPhotoPath(), mOnHanlderResultCallback);
                                toPhotoEdit();
                                break;

                            default:
                                break;
                        }
                    }
                })
                .show();
        initImageLoader(this);
    }

    private void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }

    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
            if (resultList != null) {
                switch (reqeustCode) {
                    case REQUEST_CODE_EDIT:
                        mPhotoList.clear();
                        mPhotoList.addAll(resultList);
                        break;
                    case REQUEST_CODE_CAMERA:
                        mPhotoList.addAll(resultList);
                        break;
                    default:
                        mPhotoList.clear();
                        mPhotoList.addAll(resultList);
                        break;
                }
                mChoosePhotoListAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            Toast.makeText(HomeWorkEditActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * 执行编辑
     */
    protected void toPhotoEdit() {
        Intent intent = new Intent(this, PhotoEditActivity.class);
        intent.putExtra("select_map", mPhotoList);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GalleryFinal.cleanCacheFile();
    }
}
