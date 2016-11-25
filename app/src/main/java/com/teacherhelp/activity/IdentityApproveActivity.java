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
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
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
import com.teacherhelp.view.ClearEditText;

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
 * 身份证认证页面
 * Created by Administrator on 2016/7/22.
 */
public class IdentityApproveActivity extends BaseActivity {
    @Bind(R.id.imgbtn_back)
    ImageButton imgbtnBack;
    @Bind(R.id.tv_titletwo)
    TextView tvTitletwo;
    @Bind(R.id.ia_rb_one)
    RadioButton RbOne;
    @Bind(R.id.ia_rb_two)
    RadioButton RbTwo;
    @Bind(R.id.ia_img_head)
    ImageView ImgHead;
    @Bind(R.id.ia_rl_head)
    RelativeLayout RlHead;
    @Bind(R.id.ia_et_name)
    ClearEditText EtName;
    @Bind(R.id.ia_et_idcard)
    ClearEditText EtIdcard;
    @Bind(R.id.tv_titleright)
    TextView tvTitleright;
    @Bind(R.id.ia_rl_positive)
    RelativeLayout rl_positive;
    @Bind(R.id.ia_img_positive)
    ImageView img_positive;
    @Bind(R.id.ia_rl_reverse)
    RelativeLayout rl_reverse;
    @Bind(R.id.ia_img_reverse)
    ImageView img_reverse;
    @Bind(R.id.ia_ll)
    LinearLayout ia_ll;
    @Bind(R.id.ia_text_hand)
    TextView textHand;

    //头像
    private Bitmap head;// 头像Bitmap

    private static String imgUrl = null;//上传成功返回的手持图片地址
    private static String imgUrlPositive = null;//上传成功返回的正面图片地址
    private static String imgUrlReverse = null;//上传成功返回的反面图片地址

    private int type = -1;//定义点击图片类型
    private int idType = 1;//选择身份证 1 或者护照 2

    private String state1;//获取getIntent的身份证id
    private String state2;//获取getIntent的护照id

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_identityapprove);
    }

    @Override
    protected void aadListenter() {
        imgbtnBack.setOnClickListener(this);
        RlHead.setOnClickListener(this);
        rl_positive.setOnClickListener(this);
        rl_reverse.setOnClickListener(this);
        RbOne.setOnClickListener(this);
        RbTwo.setOnClickListener(this);
        tvTitleright.setOnClickListener(this);
    }

    @Override
    protected void initVariables() {
        MyApplication.getInstance().addActivity(this);
        tvTitleright.setVisibility(View.VISIBLE);
        tvTitletwo.setVisibility(View.VISIBLE);
        tvTitletwo.setText("身份证认证");
        tvTitleright.setText("提交");

        File file = new File(Environment.getExternalStorageDirectory(), "/DCIM/");
        if (!file.exists()) {
            file.mkdirs();// 创建文件夹
        }
        getCache(file, ImgHead, "IDHead.jpg");
        getCache(file, img_positive, "IDHeadPositive.jpg");
        getCache(file, img_reverse, "IDHeadReverse.jpg");

        mSingleQueue = Volley.newRequestQueue(this, new MultiPartStack());//上传图片队列


        //判断审核状态并更新
        state1 = getIntent().getStringExtra("approveID");//身份证id
        state2 = getIntent().getStringExtra("approvePassport");//护照id
    }


    private void getCache(File file, ImageView iv, String path) {
        Bitmap bt = BitmapFactory.decodeFile(file.getPath() + path);// 从SD卡中找头像，转换成Bitmap
        if (bt != null) {
            @SuppressWarnings("deprecation")
            Drawable drawable = new BitmapDrawable(bt);// 转换成drawable
            iv.setImageDrawable(drawable);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.imgbtn_back:
                finish();
                saveEditTextAndCloseIMM();
                break;
            case R.id.tv_titleright:
                if (TextUtils.isEmpty(EtName.getText().toString()) || TextUtils.isEmpty(EtIdcard.getText().toString())) {
                    CommonUtils.showToast("请完善个人信息");
                    return;
                }
                if (idType == 1) {
                    if (imgUrl == null && imgUrlPositive == null & imgUrlReverse == null) {
                        CommonUtils.showToast("请重新上传个人身份证照片信息");
                        return;
                    }
                } else if (idType == 2) {
                    if (imgUrl == null) {
                        CommonUtils.showToast("请重新上传个人护照照片信息");
                        return;
                    }
                }


                if (state1 == null) {//如果没有身份证id
                    getAddPapers();
                } else {//有身份证id则更新
                    getUpdatePapers();
                }
                if (state2 == null) {//如果没有身份证id
                    getAddPapers();
                } else {//有身份证id则更新
                    getUpdatePapers();
                }

                break;

            case R.id.ia_rl_head: //手持头照片
                type = 0;
                showTypeDialog();
                break;
            case R.id.ia_rl_positive://正面照片
                type = 1;
                showTypeDialog();
                break;
            case R.id.ia_rl_reverse://反面照片
                type = 2;
                showTypeDialog();
                break;
            case R.id.ia_rb_one:
                idType = 1;
                ia_ll.setVisibility(View.VISIBLE);
                textHand.setText("手持照片");
                break;
            case R.id.ia_rb_two:
                idType = 2;
                ia_ll.setVisibility(View.GONE);
                textHand.setText("正面照片");
                break;

        }
    }

    /**
     * 图片库 intent 1
     * 拍照  intent 0
     */
    private void showTypeDialog() {
        ActionSheet.createBuilder(IdentityApproveActivity.this, getSupportFragmentManager())
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
                                File file = new File(Environment.getExternalStorageDirectory(), "/DCIM/");
                                if (!file.exists()) {
                                    file.mkdirs();
                                }
                                String path = null;
                                if (type == 0) {
                                    path = file.getPath() + "IDHead.jpg";
                                } else if (type == 1) {
                                    path = file.getPath() + "IDHeadPositive.jpg";
                                } else if (type == 2) {
                                    path = file.getPath() + "IDHeadReverse.jpg";
                                }
                                if (path == null) {
                                    return;
                                }

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
                    String fileName = null;
                    if (type == 0) {
                        fileName = file.getPath() + "IDHead.jpg";
                    } else if (type == 1) {
                        fileName = file.getPath() + "IDHeadPositive.jpg";
                    } else if (type == 2) {
                        fileName = file.getPath() + "IDHeadReverse.jpg";
                    }

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
                        if (type == 0) {
                            ImgHead.setImageBitmap(head);// 用ImageView显示出来
                        } else if (type == 1) {
                            img_positive.setImageBitmap(head);// 用ImageView显示出来
                        } else if (type == 2) {
                            img_reverse.setImageBitmap(head);// 用ImageView显示出来
                        }


                        /**
                         * 上传图片到服务器
                         */
                        showWaitLoad(this, "正在上传...");
                        Map<String, File> files = new HashMap<String, File>();

                        File file = new File(Environment.getExternalStorageDirectory(), "/DCIM/");
                        if (!file.exists()) {
                            file.mkdirs();
                        }
                        String path = null;
                        if (type == 0) {
                            path = file.getPath() + "IDHead.jpg";
                        } else if (type == 1) {
                            path = file.getPath() + "IDHeadPositive.jpg";
                        } else if (type == 2) {
                            path = file.getPath() + "IDHeadReverse.jpg";
                        }
                        File path1 = new File(path);
                        files.put("imgFile", path1);

                        Map<String, String> params = new HashMap<String, String>();
                        params.put("token", ShareReferenceUtils.getValue("myToken"));

                        String uri = UrlConfig.POST_UpImage;
                        addPutUploadFileRequest(
                                uri,
                                files, params, mResonseListenerString, mErrorListener, "personID");

                    }
                }
                break;
        }
        /**
         * 上传身份图片认证
         */

        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 调用系统的裁剪功能 intent 3
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
        File file = new File(Environment.getExternalStorageDirectory(), "/DCIM/");
        if (!file.exists()) {
            file.mkdirs();// 创建文件夹
        }
        String fileName = null;
        if (type == 0) {
            fileName = file.getPath() + "IDHead.jpg";
        } else if (type == 1) {
            fileName = file.getPath() + "IDHeadPositive.jpg";
        } else if (type == 2) {
            fileName = file.getPath() + "IDHeadReverse.jpg";
        }
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
                    Log.e("身份证图片", jsonObject.get("data") + "");
                    CommonUtils.showToast("上传成功");
                    if (type == 0) {
                        imgUrl = jsonObject.getString("data");
                    } else if (type == 1) {
                        imgUrlPositive = jsonObject.getString("data");
                    } else if (type == 2) {
                        imgUrlReverse = jsonObject.getString("data");
                    }

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
     * 上传身份认证图片
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
                if (idType == 1) {//身份证
                    map.put("peparName", EtName.getText().toString());
                    map.put("peparNo", EtIdcard.getText().toString());
                    map.put("peparType", String.valueOf(idType));
                    map.put("peparHand", imgUrl);//手持
                    map.put("peparFront", imgUrlPositive);//正面
                    map.put("peparCon", imgUrlReverse);//反面
                } else if (idType == 2) {
                    map.put("peparName", EtName.getText().toString());
                    map.put("peparNo", EtIdcard.getText().toString());
                    map.put("peparType", String.valueOf(idType));
                    map.put("peparOther", imgUrl);//其他证件
                }
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
     * 更新认证
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
                if (idType == 1) {//身份证
                    map.put("id", state1);
                    map.put("peparName", EtName.getText().toString());
                    map.put("peparNo", EtIdcard.getText().toString());
                    map.put("peparType", String.valueOf(idType));
                    map.put("peparHand", imgUrl);//手持
                    map.put("peparFront", imgUrlPositive);//正面
                    map.put("peparCon", imgUrlReverse);//反面
                } else if (idType == 2) {
                    map.put("id", state2);
                    map.put("peparName", EtName.getText().toString());
                    map.put("peparNo", EtIdcard.getText().toString());
                    map.put("peparType", String.valueOf(idType));
                    map.put("peparOther", imgUrl);//其他证件
                }
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
