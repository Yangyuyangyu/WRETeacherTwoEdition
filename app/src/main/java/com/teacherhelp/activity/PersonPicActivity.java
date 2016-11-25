package com.teacherhelp.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.baoyz.actionsheet.ActionSheet;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.teacherhelp.R;
import com.teacherhelp.activity.personPic.ChoosePhotoListAdapter;
import com.teacherhelp.activity.personPic.listener.UILPauseOnScrollListener;
import com.teacherhelp.activity.personPic.loader.UILImageLoader;
import com.teacherhelp.activity.personPic.photoview.PersonPicPreviewActivity;
import com.teacherhelp.app.MyApplication;
import com.teacherhelp.app.UrlConfig;
import com.teacherhelp.base.BaseActivity;
import com.teacherhelp.bean.personPic.PersonPicBean;
import com.teacherhelp.utils.CommonUtils;
import com.teacherhelp.utils.LogHelper;
import com.teacherhelp.utils.ShareReferenceUtils;
import com.teacherhelp.utils.upload.MultiPartStack;
import com.teacherhelp.utils.upload.MultiPartStringRequest;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
 * 名称:个人风采
 * 作用：展示个人照片
 * Created on 2016/7/26.
 * 创建人：WangHaoMiao
 */
public class PersonPicActivity extends BaseActivity {
    @Bind(R.id.imgbtn_back)
    ImageButton imgbtnBack;
    @Bind(R.id.tv_titletwo)
    TextView tvTitletwo;
    @Bind(R.id.tv_titleright)
    TextView tvTitleright;
    @Bind(R.id.xGridView)
    GridView mGridView;

    private final int REQUEST_CODE_CAMERA = 1000;
    private final int REQUEST_CODE_GALLERY = 1001;
    private final int REQUEST_CODE_EDIT = 1003;
    private ArrayList<PhotoInfo> mPhotoList;
    private ChoosePhotoListAdapter mChoosePhotoListAdapter;

    private List<PersonPicBean> listPicNet = new ArrayList<PersonPicBean>();//用来保存获取接口的数据

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_personpic);
    }

    @Override
    protected void aadListenter() {
        imgbtnBack.setOnClickListener(this);
        tvTitleright.setOnClickListener(this);
    }

    @Override
    protected void initVariables() {
        MyApplication.getInstance().addActivity(this);
        tvTitletwo.setVisibility(View.VISIBLE);
        tvTitleright.setVisibility(View.VISIBLE);
        tvTitletwo.setText("个人风采");
        tvTitleright.setText("管理");

        mPhotoList = new ArrayList<>();
        mChoosePhotoListAdapter = new ChoosePhotoListAdapter(this, mPhotoList);
        mChoosePhotoListAdapter.setMyClick(new ChoosePhotoListAdapter.MyClick() {
            @Override
            public void deletePhoto(int id, int position) {
                //删除图片
                delPersonalStyle(listPicNet.get(position).getId(), position);
            }
        });
        mGridView.setAdapter(mChoosePhotoListAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (mPhotoList != null) {
                    //图片预览
//                    ArrayList<PhotoInfo> preview = new ArrayList<PhotoInfo>();
//                    preview.add(mPhotoList.get(i));
//                    Intent intent = new Intent(PersonPicActivity.this, PhotoPreviewActivity.class);
//                    intent.putExtra("photo_list", preview);
//                    startActivity(intent);
                    Intent intent = new Intent(PersonPicActivity.this, PersonPicPreviewActivity.class);
                    intent.putExtra("photo_list", mPhotoList.get(i).getPhotoPath());
                    startActivity(intent);
                }
            }
        });

        mSingleQueue = Volley.newRequestQueue(this, new MultiPartStack());//上传图片队列

        getDate();//获取风采图片

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.imgbtn_back: //返回
                finish();
                break;
            case R.id.tv_titleright: //管理
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
//        functionConfigBuilder.setFilter(mPhotoList);//添加图片过滤
        final FunctionConfig functionConfig = functionConfigBuilder.build();

        CoreConfig coreConfig = new CoreConfig.Builder(PersonPicActivity.this, imageLoader, themeConfig)
                .setFunctionConfig(functionConfig)
                .setPauseOnScrollListener(pauseOnScrollListener)
                .setNoAnimcation(false)
                .build();
        GalleryFinal.init(coreConfig);
        String isEditStr = null;
        if (mChoosePhotoListAdapter.isEdit) {
            isEditStr = "取消编辑";
        } else {
            isEditStr = "编辑";
        }
        ActionSheet.createBuilder(PersonPicActivity.this, getSupportFragmentManager())
                .setCancelButtonTitle("取消")
                .setOtherButtonTitles("打开相册", "拍照", isEditStr)
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
//                                toPhotoEdit();
                                mChoosePhotoListAdapter.notifyDataSetChanged();
                                mChoosePhotoListAdapter.isEdit = !mChoosePhotoListAdapter.isEdit;
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
//                        mPhotoList.clear();
                        mPhotoList.addAll(resultList);
                        break;
                    case REQUEST_CODE_CAMERA:
                        mPhotoList.addAll(resultList);
                        break;
                    default:
//                        mPhotoList.clear();
                        mPhotoList.addAll(resultList);
                        break;
                }
                compare(resultList, reqeustCode);//添加
            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            Toast.makeText(PersonPicActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
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

    /**
     * 获取个人风采列表图片
     */
    private void getDate() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, UrlConfig.PersonalStyle.GET_List, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                LogHelper.d("请求返回的结果：" + result);
                //请求成功
                JSONObject jsonObject = JSONObject.parseObject(result);
                int responseCode = jsonObject.getIntValue("code");
                switch (responseCode) {
                    case 0:
                        Gson gson = new Gson();
                        listPicNet = gson.fromJson(
                                jsonObject.getString("data"),
                                new TypeToken<List<PersonPicBean>>() {
                                }.getType());
                        int size = listPicNet.size();
                        if (!listPicNet.isEmpty()) {
                            for (int i = 0; i < size; i++) {
                                PhotoInfo p = new PhotoInfo();
                                p.setPhotoPath(listPicNet.get(i).getImgUrl());
                                mPhotoList.add(p);
                            }
                            mChoosePhotoListAdapter.notifyDataSetChanged();
                        }

                        break;
                    default:
                        break;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                CommonUtils.showToast("请求失败，请重试！");
                LogHelper.d(volleyError.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                return null;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("token", ShareReferenceUtils.getValue("myToken"));
                return headers;
            }
        };
        setTmie(stringRequest);
        stringRequest.setTag("volleyPersonalStyleList");
        MyApplication.getHttpQueue().add(stringRequest);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!listPicNet.isEmpty()) {
            MyApplication.userInfo.setStyle(listPicNet);
        }
        listPicNet = null;
        //关闭asyncTask
        if (!asyncTask.isCancelled()) {
            boolean isCancel = asyncTask.cancel(true);
        }
    }


    /**
     * 图片上传第一步
     * 对比原数组mPhotoList和结果数组resultList
     * 异步执行添加还是删除
     */
    private void compare(List<PhotoInfo> resultNew, int reqeustCode) {
        int size = resultNew.size();
        String[] urls = new String[size];
        for (int i = 0; i < size; i++) {
            urls[i] = resultNew.get(i).getPhotoPath();
        }
        switch (reqeustCode) {
            case REQUEST_CODE_CAMERA:
                for (String url : urls) {
                    //执行图片上传
                    upLoad(url);
                }
                break;
            default:
                asyncTask.execute(urls);
                break;
        }


    }

    /**
     * 图片上传第三步
     * 上传图片到服务器
     */
    private void upLoad(String path) {
        Map<String, File> files = new HashMap<String, File>();
        files.put("imgFile", new File(
                path));
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", ShareReferenceUtils.getValue("myToken"));

        String uri = UrlConfig.POST_UpImage;
        addPutUploadFileRequest(uri, files, params, mResonseListenerString, mErrorListener, null);
    }

    /**
     * 上传图片初始化
     */

    private static RequestQueue mSingleQueue;

    Response.Listener<JSONObject> mResonseListener = new Response.Listener<JSONObject>() {

        @Override
        public void onResponse(JSONObject response) {
            Log.e("test", " on response json" + response.toString());
        }
    };

    Response.Listener<String> mResonseListenerString = new Response.Listener<String>() {

        @Override
        public void onResponse(String response) {
            JSONObject jsonObject = JSONObject.parseObject(response);
            int responsecode = jsonObject.getIntValue("code");
            switch (responsecode) {
                case 0:
                    addPersonalStyle(jsonObject.getString("data"));
                    break;
                default:
                    CommonUtils.showToast("上传图片失败" + responsecode + jsonObject.get("msg"));
                    break;
            }
        }
    };

    Response.ErrorListener mErrorListener = new Response.ErrorListener() {

        @Override
        public void onErrorResponse(VolleyError error) {
            if (error != null) {
                if (error.networkResponse != null)
                    Log.e("test", " error " + new String(error.networkResponse.data));
            }
        }
    };

    public static void addPutUploadFileRequest(final String url,
                                               final Map<String, File> files, final Map<String, String> params,
                                               final Response.Listener<String> responseListener, final Response.ErrorListener errorListener,
                                               final Object tag) {
        if (null == url || null == responseListener) {
            return;
        }

        MultiPartStringRequest multiPartRequest = new MultiPartStringRequest(
                Request.Method.POST, url, responseListener, errorListener) {

            @Override
            public Map<String, File> getFileUploads() {
                return files;
            }

            @Override
            public Map<String, String> getStringUploads() {
                return params;
            }

            @Override
            public String getBodyContentType() {
                return ShareReferenceUtils.getValue("myToken");
            }

        };

//        Log.e("test", " volley put : uploadFile " + url);

        mSingleQueue.add(multiPartRequest);
    }


    /**
     * 图片上传第二步
     * 异步执行图片数组的添加或删除方法
     */
    AsyncTask<String, Integer, String> asyncTask = new AsyncTask<String, Integer, String>() {

        @Override
        protected void onPreExecute() {//此函数是在任务没被线程池执行之前调用 运行在UI线程中 比如现在一个等待下载进度Progress
            super.onPreExecute();
            Log.e("test", "AsyncTask onPreExecute");
        }

        @Override
        protected String doInBackground(String[] params) {//此函数是在任务被线程池执行时调用 运行在子线程中，在此处理比较耗时的操作 比如执行下载
            for (String url : params) {
                //执行图片上传
                upLoad(url);
                //如果AsyncTask被调用了cancel()方法，那么任务取消，跳出for循环
                if (isCancelled()) {
                    break;
                }
            }
            String result = "upLoad end";
            return result;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {//此函数是任务在线程池中执行处于Running状态，回调给UI主线程的进度 比如上传或者下载进度
            super.onProgressUpdate(values);
            int progress = values[0];
            Log.e("test", "AsyncTask onProgressUpdate progress---->" + progress);
        }

        @Override
        protected void onPostExecute(String s) {//此函数任务在线程池中执行结束了，回调给UI主线程的结果 比如下载结果
            super.onPostExecute(s);
            Log.e("test", "AsyncTask onPostExecute result---->" + s);
        }

        @Override
        protected void onCancelled() {//此函数表示任务关闭
            super.onCancelled();
            Log.e("test", "AsyncTask onCancelled");
        }

        @Override
        protected void onCancelled(String s) {//此函数表示任务关闭 返回执行结果 有可能为null
            super.onCancelled(s);
            Log.e("test", "AsyncTask onCancelled---->" + s);
        }
    };


    /**
     * 添加个人风采图片
     */
    private void addPersonalStyle(final String url) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConfig.PersonalStyle.POST_Add, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                LogHelper.d("请求返回的结果：" + result);
                //请求成功
                JSONObject jsonObject = JSONObject.parseObject(result);
                int responseCode = jsonObject.getIntValue("code");
                switch (responseCode) {
                    case 0:
                        if (!listPicNet.isEmpty()) {
                            listPicNet.clear();
                        }
                        if (!mPhotoList.isEmpty()) {
                            mPhotoList.clear();
                        }

                        getDate();
                        break;
                    default:
                        CommonUtils.showToast("添加图片失败" + jsonObject.get("msg"));
                        break;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LogHelper.d(volleyError.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("imgUrl", url);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("token", ShareReferenceUtils.getValue("myToken"));
                return headers;
            }
        };
        setTmie(stringRequest);
        stringRequest.setTag("volleyaddPersonalStyleAdd");
        MyApplication.getHttpQueue().add(stringRequest);
    }

    /**
     * 删除个人风采图片
     */
    private void delPersonalStyle(final String id, final int position) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, UrlConfig.GET_DelPersonalStyle(id), new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                LogHelper.d("请求返回的结果：" + result);
                //请求成功
                JSONObject jsonObject = JSONObject.parseObject(result);
                int responseCode = jsonObject.getIntValue("code");
                switch (responseCode) {
                    case 0:
                        mPhotoList.remove(position);//删除要显示的位置
                        listPicNet.remove(position);//删除数据源
                        mChoosePhotoListAdapter.notifyDataSetChanged();
                        break;
                    default:
                        CommonUtils.showToast("删除图片失败" + jsonObject.getIntValue("code") + jsonObject.get("msg"));
                        break;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LogHelper.d(volleyError.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("token", ShareReferenceUtils.getValue("myToken"));
                return headers;
            }
        };
        setTmie(stringRequest);
        stringRequest.setTag("volleyaddPersonalStyleDel");
        MyApplication.getHttpQueue().add(stringRequest);
    }
}
