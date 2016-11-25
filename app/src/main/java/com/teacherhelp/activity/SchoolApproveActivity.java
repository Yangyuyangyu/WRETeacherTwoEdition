package com.teacherhelp.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.baoyz.actionsheet.ActionSheet;
import com.teacherhelp.R;
import com.teacherhelp.app.MyApplication;
import com.teacherhelp.app.UrlConfig;
import com.teacherhelp.base.BaseActivity;
import com.teacherhelp.utils.CommonUtils;
import com.teacherhelp.utils.LogHelper;
import com.teacherhelp.utils.ShareReferenceUtils;
import com.teacherhelp.utils.UploadUtil;
import com.teacherhelp.utils.upload.MultiPartStack;
import com.teacherhelp.utils.upload.MultiPartStringRequest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;

/**
 * 毕业证认证
 * Created by Administrator on 2016/7/22.
 */
public class SchoolApproveActivity extends BaseActivity {
    @Bind(R.id.imgbtn_back)
    ImageButton imgbtnBack;
    @Bind(R.id.tv_titletwo)
    TextView tvTitletwo;
    @Bind(R.id.tv_titleright)
    TextView tvTitleright;
    @Bind(R.id.sa_rl_head)
    RelativeLayout RlHead;
    @Bind(R.id.sa_rl_headImg)
    ImageView headImg;

    //头像
    private Bitmap head;// 头像Bitmap

    private String imgUrl = null;//上传成功返回的图片地址

    private String getId;

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_schoolapprove);
    }

    @Override
    protected void aadListenter() {
        imgbtnBack.setOnClickListener(this);
        tvTitleright.setOnClickListener(this);
        RlHead.setOnClickListener(this);
    }

    @Override
    protected void initVariables() {
        MyApplication.getInstance().addActivity(this);
        tvTitletwo.setVisibility(View.VISIBLE);
        tvTitleright.setVisibility(View.VISIBLE);
        tvTitleright.setText("提交");
        tvTitletwo.setText("毕业证认证");
        File file = new File(Environment.getExternalStorageDirectory(), "/DCIM/" );
        if (!file.exists()) {
            file.mkdirs();// 创建文件夹
        }
        Bitmap bt = BitmapFactory.decodeFile(file.getPath() + "SchoolHead.jpg");// 从SD卡中找头像，转换成Bitmap
        if (bt != null) {
            @SuppressWarnings("deprecation")
            Drawable drawable = new BitmapDrawable(bt);// 转换成drawable
            headImg.setImageDrawable(drawable);
        }

        mSingleQueue = Volley.newRequestQueue(this, new MultiPartStack());//上传图片队列

        getId = getIntent().getStringExtra("approveSchool");
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.imgbtn_back:
                finish();
                break;
            case R.id.tv_titleright:
                if(imgUrl==null){
                    CommonUtils.showToast("请重新上传照片信息");
                    return;
                }
                if (getId == null) {
                    getAddPapers();
                } else {
                    getUpdatePapers();
                }
                break;
            case R.id.sa_rl_head:
                showTypeDialog();
                break;
        }
    }

    private void showTypeDialog() {
        ActionSheet.createBuilder(SchoolApproveActivity.this, getSupportFragmentManager())
                .setCancelButtonTitle("取消")
                .setOtherButtonTitles("从图库中选取", "拍摄照片")
                .setCancelableOnTouchOutside(true)
                .setListener(new ActionSheet.ActionSheetListener() {
                    @Override
                    public void onDismiss(ActionSheet actionSheet, boolean isCancel) {
                    }

                    @Override
                    public void onOtherButtonClick(ActionSheet actionSheet, int index) {
                        switch (index) {
                            case 0:

                                Intent intent1 = new Intent(Intent.ACTION_PICK, null);
                                intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                                startActivityForResult(intent1, 1);
                                break;
                            case 1:
                                File file = new File(Environment.getExternalStorageDirectory(), "/DCIM/" );
                                if (!file.exists()) {
                                    file.mkdirs();
                                }
                                String path = file.getPath() + "SchoolHead.jpg";
                                Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                intent2.putExtra(MediaStore.EXTRA_OUTPUT,
                                        Uri.fromFile(new File(path)));
                                startActivityForResult(intent2, 2);// 采用ForResult打开
                                break;
                            default:
                                break;
                        }
                    }
                })
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Map<String, String> map = new HashMap<String, String>();
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    cropPhoto(data.getData());// 裁剪图片
                }

                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    File file = new File(Environment.getExternalStorageDirectory(), "/DCIM/");
                    if (!file.exists()) {
                        file.mkdirs();// 创建文件夹
                    }
                    String fileName = file.getPath() + "SchoolHead.jpg";// 图片名字

                    File temp = new File(fileName);
                    cropPhoto(Uri.fromFile(temp));// 裁剪图片
                }

                break;
            case 3:
                if (data != null) {
                    Bundle extras = data.getExtras();
                    head = extras.getParcelable("data");
                    if (head != null) {
                        setPicToView(head);// 保存在SD卡中
                        headImg.setImageBitmap(head);// 用ImageView显示出来

                        /**
                         * 上传图片到服务器
                         */
                        showWaitLoad(this, "正在上传...");
                        Map<String, File> files = new HashMap<String, File>();
                        File file = new File(Environment.getExternalStorageDirectory(), "/DCIM/" );
                        if (!file.exists()) {
                            file.mkdirs();
                        }
                        String path = file.getPath() + "SchoolHead.jpg";
                        File path1 = new File(path);
                        files.put("imgFile", path1);

                        Map<String, String> params = new HashMap<String, String>();
                        params.put("token", ShareReferenceUtils.getValue("myToken"));

                        String uri = UrlConfig.POST_UpImage;
                        addPutUploadFileRequest(
                                uri,
                                files, params, mResonseListenerString, mErrorListener, "personSchool");
                        if (imgUrl != null) {
                        }
                    }
                }
                break;
        }
        /**
         * 上传毕业图片认证
         */

        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 调用系统的裁剪功能
     *
     * @param uri
     */
    public void cropPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);
    }

    private void setPicToView(Bitmap mBitmap) {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            return;
        }
        FileOutputStream b = null;
        File file = new File(Environment.getExternalStorageDirectory(), "/DCIM/" );
        if (!file.exists()) {
            file.mkdirs();// 创建文件夹
        }
        String fileName = file.getPath() + "SchoolHead.jpg";// 图片名字
        try {
            b = new FileOutputStream(fileName);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                // 关闭流
                b.flush();
                b.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 上传图片
     */

    private static RequestQueue mSingleQueue;

    Response.Listener<JSONObject> mResonseListener = new Response.Listener<JSONObject>() {

        @Override
        public void onResponse(JSONObject response) {
            Log.i("test", " on response json" + response.toString());
        }
    };

    Response.Listener<String> mResonseListenerString = new Response.Listener<String>() {

        @Override
        public void onResponse(String response) {
            JSONObject jsonObject = JSONObject.parseObject(response);
            int responsecode = jsonObject.getIntValue("code");
            switch (responsecode) {
                case 0:
                    Log.e("毕业证图片", jsonObject.get("data") + "");
                    CommonUtils.showToast("上传成功");
                    imgUrl = jsonObject.getString("data");
                    DismissDialog();
                    break;
                default:
                    DismissDialog();
                    CommonUtils.showToast("上传失败");
                    break;
            }
        }
    };

    Response.ErrorListener mErrorListener = new Response.ErrorListener() {

        @Override
        public void onErrorResponse(VolleyError error) {
            if (error != null) {
                if (error.networkResponse != null)
                    Log.i("test", " error " + new String(error.networkResponse.data));
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
                Request.Method.PUT, url, responseListener, errorListener) {

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

        Log.i("test", " volley put : uploadFile " + url);

        mSingleQueue.add(multiPartRequest);
    }

    /**
     * 上传认证图片
     */
    private void getAddPapers() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConfig.POST_AddPapers, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                LogHelper.d("请求返回的结果：" + result);
                //请求成功
                JSONObject jsonObject = JSONObject.parseObject(result);
                int responseCode = jsonObject.getIntValue("code");
                switch (responseCode) {
                    case 1:
                        CommonUtils.showToast("上传信息成功");
                        finish();
                        break;
                    default:
                        CommonUtils.showToast("上传信息失败" + responseCode + jsonObject.get("msg"));
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
                Map<String, String> map = new HashMap<String, String>();

//                map.put("peparName", EtName.getText().toString());
//                map.put("peparNo", EtIdcard.getText().toString());
                map.put("peparType", 4+"");
                map.put("peparOther", imgUrl);//其他证件

                //提交时间
                Calendar c = Calendar.getInstance();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:MM");
                Date curDate = new Date(System.currentTimeMillis());
                String str = formatter.format(curDate);
                map.put("peparCtime", str);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("token", ShareReferenceUtils.getValue("myToken"));
                LogHelper.d("请求头:" + headers.toString());
                return headers;
            }
        };
        setTmie(stringRequest);
        stringRequest.setTag("volleypostAddPapers");
        MyApplication.getHttpQueue().add(stringRequest);
    }

    /**
     * 更新
     */
    private void getUpdatePapers() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConfig.POST_PaperMod, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                LogHelper.d("请求返回的结果：" + result);
                //请求成功
                JSONObject jsonObject = JSONObject.parseObject(result);
                int responseCode = jsonObject.getIntValue("code");
                switch (responseCode) {
                    case 1:
                        CommonUtils.showToast("更新信息成功");
                        finish();
                        break;
                    default:
                        CommonUtils.showToast("更新信息失败" + responseCode + jsonObject.get("msg"));
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
                Map<String, String> map = new HashMap<String, String>();
                map.put("id", getId);
//                map.put("peparName", EtName.getText().toString());
//                map.put("peparNo", EtIdcard.getText().toString());
                map.put("peparType", 4 + "");
                map.put("peparOther", imgUrl);//其他证件
                //提交时间
                Calendar c = Calendar.getInstance();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:MM");
                Date curDate = new Date(System.currentTimeMillis());
                String str = formatter.format(curDate);
                map.put("peparCtime", str);

                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("token", ShareReferenceUtils.getValue("myToken"));
                LogHelper.d("请求头:" + headers.toString());
                return headers;
            }
        };
        setTmie(stringRequest);
        stringRequest.setTag("volleypostUpdatePapers");
        MyApplication.getHttpQueue().add(stringRequest);
    }
}
