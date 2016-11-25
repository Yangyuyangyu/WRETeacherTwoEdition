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
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.baoyz.actionsheet.ActionSheet;
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
import com.teacherhelp.utils.Utility;
import com.teacherhelp.utils.upload.MultiPartStack;
import com.teacherhelp.utils.upload.MultiPartStringRequest;
import com.teacherhelp.view.RoundImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;

/**
 * 个人资料设置
 * Created by Administrator on 2016/7/18.
 */
public class PersonDataActivty extends BaseActivity {
    @Bind(R.id.imgbtn_back)
    ImageButton imgbtnBack;
    @Bind(R.id.tv_titletwo)
    TextView tvTitletwo;
    @Bind(R.id.data_rl_head)
    RelativeLayout RlHead;
    @Bind(R.id.data_rl_sign)
    RelativeLayout RlSign;
    @Bind(R.id.data_rl_name)
    RelativeLayout RlName;
    @Bind(R.id.data_rl_sex)
    RelativeLayout RlSex;
    @Bind(R.id.data_rl_birth)
    RelativeLayout RlBirth;
    @Bind(R.id.data_rl_teachold)
    RelativeLayout RlTeachold;
    @Bind(R.id.data_rl_education)
    RelativeLayout RlEducation;
    @Bind(R.id.data_rl_major)
    RelativeLayout RlMajor;
    @Bind(R.id.data_rl_school)
    RelativeLayout RlSchool;
    @Bind(R.id.data_rl_QQ)
    RelativeLayout RlQQ;
    @Bind(R.id.data_rl_weixin)
    RelativeLayout RlWeixin;
    @Bind(R.id.data_rl_addree)
    RelativeLayout RlAddree;
    @Bind(R.id.data_rl_educationfeature)
    RelativeLayout RlEducationfeature;
    @Bind(R.id.data_rl_personalexperience)
    RelativeLayout RlPersonalexperience;
    @Bind(R.id.data_rl_share)
    RelativeLayout RlShare;
    @Bind(R.id.data_personpic)
    RelativeLayout Personpic;
    @Bind(R.id.data_rc_vidio)
    RecyclerView RcVidio;
    @Bind(R.id.data_rl_vidio)
    RelativeLayout RlVidio;
    @Bind(R.id.data_img_head)
    RoundImageView ImgHead;
    @Bind(R.id.data_tv_sign)
    TextView TvSign;
    @Bind(R.id.data_tv_name)
    TextView TvName;
    @Bind(R.id.data_tv_sex)
    TextView TvSex;
    @Bind(R.id.data_tv_birth)
    TextView TvBirth;
    @Bind(R.id.data_tv_teachold)
    TextView TvTeachold;
    @Bind(R.id.data_tv_education)
    TextView TvEducation;
    @Bind(R.id.data_tv_major)
    TextView TvMajor;
    @Bind(R.id.data_tv_school)
    TextView TvSchool;
    @Bind(R.id.data_tv_phone)
    TextView TvPhone;
    @Bind(R.id.data_tv_QQ)
    TextView TvQQ;
    @Bind(R.id.data_tv_weixin)
    TextView TvWeixin;
    @Bind(R.id.data_tv_addree)
    TextView TvAddree;
    @Bind(R.id.data_tv_educationfeature)
    TextView TvEducationfeature;
    @Bind(R.id.data_tv_personalexperience)
    TextView TvPersonalexperience;
    @Bind(R.id.data_tv_share)
    TextView TvShare;
    @Bind(R.id.style_ll)
    LinearLayout style_LL;//个人风采预览LinearLayout
    @Bind(R.id.style_img1)
    ImageView style_img1;
    @Bind(R.id.style_img2)
    ImageView style_img2;
    @Bind(R.id.style_img3)
    ImageView style_img3;


    //头像
    private Bitmap head;// 头像Bitmap

    private static String imgUrl = null;//上传成功返回的图片地址

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_persondata);
    }

    @Override
    protected void aadListenter() {
        imgbtnBack.setOnClickListener(this);
        RlHead.setOnClickListener(this);
        RlSign.setOnClickListener(this);
        RlName.setOnClickListener(this);
        RlSex.setOnClickListener(this);
        RlBirth.setOnClickListener(this);
        RlTeachold.setOnClickListener(this);
        RlEducation.setOnClickListener(this);
        RlMajor.setOnClickListener(this);
        RlSchool.setOnClickListener(this);
        RlQQ.setOnClickListener(this);
        RlWeixin.setOnClickListener(this);
        RlAddree.setOnClickListener(this);
        RlEducationfeature.setOnClickListener(this);
        RlPersonalexperience.setOnClickListener(this);
        RlShare.setOnClickListener(this);
        Personpic.setOnClickListener(this);
        RlVidio.setOnClickListener(this);
    }

    @Override
    protected void initVariables() {
        MyApplication.getInstance().addActivity(this);
        tvTitletwo.setVisibility(View.VISIBLE);
        tvTitletwo.setText("个人资料");

        setInfo();
        mSingleQueue = Volley.newRequestQueue(this, new MultiPartStack());//上传图片队列
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        Intent intent = null;
        int requestCode = -1;
        switch (v.getId()) {
            case R.id.imgbtn_back:
                Intent i = new Intent();
                setResult(222, i);
                finish();
                break;
            case R.id.data_rl_head: //头像点击
                showTypeDialog();
                break;
            case R.id.data_rl_sign: //个性签名
                intent = new Intent(PersonDataActivty.this, SignActivity.class);
                requestCode = 11;
                break;
            case R.id.data_rl_name: //姓名
                intent = new Intent(PersonDataActivty.this, NameActivity.class);
                requestCode = 12;
                break;
            case R.id.data_rl_sex: //性别
                intent = new Intent(PersonDataActivty.this, SexActivity.class);
                requestCode = 13;
                break;
            case R.id.data_rl_birth: //出生年月
                intent = new Intent(PersonDataActivty.this, DateOfBirthActivity.class);
                requestCode = 14;
                break;
            case R.id.data_rl_teachold: //教龄
                intent = new Intent(PersonDataActivty.this, TeachYearActivity.class);
                requestCode = 15;
                break;
            case R.id.data_rl_education: //学历
                intent = new Intent(PersonDataActivty.this, DegreeActivity.class);
                requestCode = 16;
                break;
            case R.id.data_rl_major: //专业
                intent = new Intent(PersonDataActivty.this, MajorActivity.class);
                requestCode = 17;
                break;
            case R.id.data_rl_school: //毕业院校
                intent = new Intent(PersonDataActivty.this, GraduateSchoolActivity.class);
                requestCode = 18;
                break;
            case R.id.data_rl_QQ: //QQ号码
                intent = new Intent(PersonDataActivty.this, QqActivity.class);
                requestCode = 19;
                break;
            case R.id.data_rl_weixin: //微信号
                intent = new Intent(PersonDataActivty.this, WxActivity.class);
                requestCode = 20;
                break;
            case R.id.data_rl_addree: //家庭地址
                intent = new Intent(PersonDataActivty.this, AddressNewActivity.class);
                requestCode = 21;
                break;
            case R.id.data_rl_educationfeature: //教学特点
                intent = new Intent(PersonDataActivty.this, TeachFeatureActivity.class);
                requestCode = 22;
                break;
            case R.id.data_rl_personalexperience: //个人经历
                intent = new Intent(PersonDataActivty.this, PersonalExperienceActivity.class);
                requestCode = 23;
                break;
            case R.id.data_rl_share: //成果分享
                intent = new Intent(PersonDataActivty.this, ShareTeachActivity.class);
                requestCode = 24;
                break;
            case R.id.data_personpic://个人风采
                intent = new Intent(PersonDataActivty.this, PersonPicActivity.class);
                requestCode = 25;
                break;
            case R.id.data_rl_vidio: //视频相册
                intent = new Intent(PersonDataActivty.this, OrganizationVidioActivity.class);
                requestCode = 26;
                break;
        }
        if (intent != null) {
            startActivityForResult(intent, requestCode);
        }
    }

    private void showTypeDialog() {
        ActionSheet.createBuilder(PersonDataActivty.this, getSupportFragmentManager())
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
//                                File file = new File(Environment.getExternalStorageDirectory(), "/DCIM/" + "GalleryFinal/");
                                File file = new File(Environment.getExternalStorageDirectory(), "/DCIM/");
                                if (!file.exists()) {
                                    file.mkdirs();
                                }
                                String path = file.getPath() + "head.jpg";

                                Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                intent2.putExtra(MediaStore.EXTRA_OUTPUT,
                                        Uri.fromFile(new File(file.getPath(), "head.jpg")));
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
        String key = null;
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    cropPhoto(data.getData());// 裁剪图片
                }

                break;
            case 2:
                if (resultCode == RESULT_OK) {
//                    File file = new File(Environment.getExternalStorageDirectory(), "/DCIM/" + "GalleryFinal/");
                    File file = new File(Environment.getExternalStorageDirectory(), "/DCIM/");
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    String path = file.getPath() + "head.jpg";
                    File temp = new File(file.getPath(), "head.jpg");
                    cropPhoto(Uri.fromFile(temp));// 裁剪图片
                }

                break;
            case 3:
                if (data != null) {
                    Bundle extras = data.getExtras();
                    head = extras.getParcelable("data");
                    if (head != null) {
                        setPicToView(head);// 保存在SD卡中
                        ImgHead.setImageBitmap(head);// 用ImageView显示出来

                        /**
                         * 上传图片到服务器
                         */
                        showWaitLoad(this, "正在上传...");
                        Map<String, File> files = new HashMap<String, File>();
//                        File file = new File(Environment.getExternalStorageDirectory(), "/DCIM/" + "GalleryFinal/");
                        File file = new File(Environment.getExternalStorageDirectory(), "/DCIM/");
                        if (!file.exists()) {
                            file.mkdirs();
                        }
//                        "file:/" +
                        String path = file.getPath() + "head.jpg";
                        File path1 = new File(file.getPath(), "head.jpg");
                        if (path1.exists()) {
                            Log.e("头像上传本地地址存在111", path1.getPath());
                        } else {
                            Log.e("头像上传本地地址不存在000", path1.getPath());
                        }
                        files.put("imgFile", path1);

                        Map<String, String> params = new HashMap<String, String>();
                        params.put("token", ShareReferenceUtils.getValue("myToken"));

                        String uri = UrlConfig.POST_UpImage;
                        addPutUploadFileRequest(
                                uri,
                                files, params, mResonseListenerString, mErrorListener, "personHead");
                    }
                    map.put("headImg", imgUrl);
                    key = "headImg";
                }
                break;
            //
            case 11://个性签名
                if (resultCode == 11) {
                    String sign = data.getStringExtra("sign");
                    TvSign.setText(sign);
                    map.put("summary", sign);
                    key = "summary";
                }
                break;
            case 12://姓名
                if (resultCode == 12) {
                    String sign = data.getStringExtra("name");
                    TvName.setText(sign);
                    map.put("name", sign);
                    key = "name";
                }
                break;
            case 13://性别
                if (resultCode == 13) {
                    String sign = data.getStringExtra("sex");
                    if (sign.equals("1")) {
                        TvSex.setText("男");
                    } else if (sign.equals("2")) {
                        TvSex.setText("女");
                    }
                    map.put("sex", sign);
                    key = "sex";
                }
                break;
            case 14://出生年月
                if (resultCode == 14) {
                    String sign = data.getStringExtra("birth");
                    TvBirth.setText(sign);
                    map.put("birthday", sign);
                    key = "birthday";
                }
                break;
            case 15://教龄
                if (resultCode == 15) {
                    String sign = data.getStringExtra("teacherOld");
                    TvTeachold.setText(sign);
//                    map.put("sex",sign);
//                    key="birthday";
                }
                break;
            case 16://学历
                if (resultCode == 16) {
                    String sign = data.getStringExtra("degree");
                    int degreeIndex = 0;
                    if (sign.equals("大专")) {
                        degreeIndex = 1;
                    } else if (sign.equals("本科")) {
                        degreeIndex = 2;
                    } else if (sign.equals("硕士")) {
                        degreeIndex = 3;
                    } else if (sign.equals("博士")) {
                        degreeIndex = 4;
                    }
                    TvEducation.setText(sign);
                    map.put("edu", String.valueOf(degreeIndex));
                    key = "edu";
                }
                break;
            case 17://专业
                if (resultCode == 17) {
                    String sign = data.getStringExtra("major");
                    TvMajor.setText(sign);
//                    map.put("sex",sign);
//                    key="edu";
                }
                break;
            case 18://毕业院校
                if (resultCode == 18) {
                    String sign = data.getStringExtra("school");
                    TvSchool.setText(sign);
                    map.put("graduatedSchool", sign);
                }
                break;
            case 19://QQ号
                if (resultCode == 19) {
                    String sign = data.getStringExtra("qq");
                    TvQQ.setText(sign);
                    map.put("qq", sign);
                    key = "qq";
                }
                break;
            case 20://微信号
                if (resultCode == 20) {
                    String sign = data.getStringExtra("wx");
                    TvWeixin.setText(sign);
                    map.put("wxName", sign);
                    key = "wxName";
                }
                break;
            case 21://家庭地址
                if (resultCode == 21) {
                    String sign = data.getStringExtra("address");
                    TvAddree.setText(sign);
                    map.put("addr", sign);
                    key = "addr";
                }
                break;
            case 22://教学特点
                if (resultCode == 22) {
                    String sign = data.getStringExtra("teachFeature");
                    TvEducationfeature.setText(sign);
                    map.put("merits", sign);
                    key = "merits";
                }
                break;
            case 23://个人经历

                break;
            case 24://成果分享
                break;
            case 25://个人风采
                break;
            case 26://视频相册
                break;
            default:
                break;

        }
        //更新用户信息
        updateUerInfo(map, resultCode, key);

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
//        File file = new File(Environment.getExternalStorageDirectory(), "/DCIM/" + "GalleryFinal/");
        File file = new File(Environment.getExternalStorageDirectory(), "/DCIM/");
        if (!file.exists()) {
            file.mkdirs();// 创建文件夹
        }
        File path = new File(file.getPath(), "head.jpg");
        String fileName = path.getPath();// 图片名字
        Log.e("头像保存本地地址", fileName);
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
     * 修改个人信息
     */
    private void updateUerInfo(final Map<String, String> map, final int requestCode, final String key) {
        if (map.values().isEmpty()) {
            return;
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConfig.UserInfo.POST_Update, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                LogHelper.e("修改个人信息请求返回的结果：" + result);
                //请求成功
                JSONObject jsonObject = JSONObject.parseObject(result);
                int responseCode = jsonObject.getIntValue("code");
                switch (responseCode) {
                    case 0:
                        switch (requestCode) {
                            case 3:
                                MyApplication.userInfo.setHeadImg(imgUrl);
                                break;
                            case 11://个性签名
                                MyApplication.userInfo.setSummary(map.get(key));
                                break;
                            case 12://姓名
                                MyApplication.userInfo.setName(map.get(key));
                                break;
                            case 13://性别
                                MyApplication.userInfo.setSex(map.get(key));
                                break;
                            case 14://出生年月
                                MyApplication.userInfo.setBirthday(map.get(key));
                                break;
                            case 15://教龄

                                break;
                            case 16://学历
                                MyApplication.userInfo.setEdu(map.get(key));
                                break;
                            case 17://专业

                                break;
                            case 18://毕业院校
                                MyApplication.userInfo.setGraduatedSchool(map.get(key));
                                break;
                            case 19://QQ号
                                MyApplication.userInfo.setQq(map.get(key));
                                break;
                            case 20://微信号
                                MyApplication.userInfo.setWxName(map.get(key));
                                break;
                            case 21://家庭地址
                                MyApplication.userInfo.setAddr(map.get(key));
                                break;
                            case 22://教学特点
                                MyApplication.userInfo.setMerits(map.get(key));
                                break;
                            case 23://个人经历

                                break;
                            case 24://成果分享
                                break;
                        }
                        CommonUtils.showToast("修改成功");
                        break;
                    default:
                        CommonUtils.showToast("修改失败" + jsonObject.get("msg"));
                        break;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                CommonUtils.showToast("请求失败，请重试！");
                LogHelper.e(volleyError.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
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
        stringRequest.setTag("volleyUpdateUserInfo");
        MyApplication.getHttpQueue().add(stringRequest);
    }

    /**
     * 设置用户信息
     */
    private void setInfo() {
        if (MyApplication.userInfo == null) {
            return;
        }
        TvName.setText(MyApplication.userInfo.getName());
        if (MyApplication.userInfo.getSex() == null) {
            TvSex.setText("");
        } else if (MyApplication.userInfo.getSex().equals("1")) {
            TvSex.setText("男");
        } else if (MyApplication.userInfo.getSex().equals("2")) {
            TvSex.setText("女");
        }
        TvBirth.setText(MyApplication.userInfo.getBirthday().toString().substring(0, 10));
        if (MyApplication.userInfo.getEdu() == null) {
            TvEducation.setText("");
        } else {
            if (MyApplication.userInfo.getEdu().equals("1")) {
                TvEducation.setText("大专");
            } else if (MyApplication.userInfo.getEdu().equals("2")) {
                TvEducation.setText("本科");
            } else if (MyApplication.userInfo.getEdu().equals("3")) {
                TvEducation.setText("硕士");
            } else if (MyApplication.userInfo.getEdu().equals("4")) {
                TvEducation.setText("博士");
            }

        }
        TvSchool.setText(MyApplication.userInfo.getGraduatedSchool());
        TvQQ.setText(MyApplication.userInfo.getQq());
        TvWeixin.setText(MyApplication.userInfo.getWxName());
        TvPhone.setText(MyApplication.userInfo.getCellPhone());
        TvAddree.setText(MyApplication.userInfo.getAddr());
        TvEducationfeature.setText(MyApplication.userInfo.getMerits());
        TvSign.setText(MyApplication.userInfo.getSummary());
//        File file = new File(Environment.getExternalStorageDirectory(), "/DCIM/" + "GalleryFinal/");
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

        setStylePic();

    }


    /**
     * 上传图片
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
                    Log.e("头像图片", jsonObject.get("data") + "");
                    imgUrl = jsonObject.getString("data");
                    MyApplication.userInfo.setHeadImg(imgUrl);
                    DismissDialog();
                    //同更新融云用户的头像
                    if (imgUrl != null) {
                        refreshRCloud(imgUrl);
                    }
                    break;
                default:
                    DismissDialog();
                    CommonUtils.showToast("上传失败" + jsonObject.getIntValue("code") + "---" + jsonObject.getString("data"));
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
            DismissDialog();
            Log.e("上传图片失败", error.getMessage());
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
        Log.e("test", " volley put : uploadFile " + url);
        Log.e("头像上传接口使用的地址", files.values().toString());

        mSingleQueue.add(multiPartRequest);
    }


    /**
     * 更新融云用户头像
     */
    private void refreshRCloud(final String url) {
        final String timestamp = "" + Utility.getStringToDate(Utility.getCurrentTimeDate());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConfig.RC_Refresh, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                LogHelper.e("请求返回的结果：" + result);
                //请求成功
                JSONObject jsonObject = JSONObject.parseObject(result);
                int responseCode = jsonObject.getIntValue("code");
                switch (responseCode) {
                    case 200:
                        LogHelper.e("更新融云用户信息头像成功");
                        break;
                    default:
                        LogHelper.e("更新融云用户信息头像失败" + responseCode);
                        break;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LogHelper.e(volleyError.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("userId", MyApplication.userInfo.getCellPhone());
                map.put("name", MyApplication.userInfo.getCellPhone());
                map.put("portraitUri", url);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("App-Key", UrlConfig.RongCloudAppKey);
                headers.put("Nonce", UrlConfig.RongCloudNonce);
                headers.put("Timestamp", timestamp);
                headers.put("Signature", UrlConfig.RongCloudSignature(timestamp));
                return headers;
            }
        };
        setTmie(stringRequest);
        stringRequest.setTag("volleygetRCloudRefresh");
        MyApplication.getHttpQueue().add(stringRequest);

    }

    /**
     * 设置个人风采预览图片
     */
    private void setStylePic() {
        if (MyApplication.userInfo.getStyle() == null) {
            return;
        }
        if (MyApplication.userInfo.getStyle().size() != 0) {
            style_LL.setVisibility(View.VISIBLE);
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .showImageOnFail(R.drawable.ic_gf_default_photo)
                    .showImageForEmptyUri(R.drawable.ic_gf_default_photo)
                    .showImageOnLoading(R.drawable.ic_gf_default_photo).build();
            if (MyApplication.userInfo.getStyle().size() == 1) {
                style_img1.setVisibility(View.INVISIBLE);
                style_img2.setVisibility(View.INVISIBLE);
                ImageLoader.getInstance().displayImage(MyApplication.userInfo.getStyle().get(0).getImgUrl(), style_img3, options);
            } else if (MyApplication.userInfo.getStyle().size() == 2) {
                style_img1.setVisibility(View.INVISIBLE);
                ImageLoader.getInstance().displayImage(MyApplication.userInfo.getStyle().get(0).getImgUrl(), style_img2, options);
                ImageLoader.getInstance().displayImage(MyApplication.userInfo.getStyle().get(1).getImgUrl(), style_img3, options);
            } else if (MyApplication.userInfo.getStyle().size() == 3) {
                ImageLoader.getInstance().displayImage(MyApplication.userInfo.getStyle().get(0).getImgUrl(), style_img1, options);
                ImageLoader.getInstance().displayImage(MyApplication.userInfo.getStyle().get(1).getImgUrl(), style_img2, options);
                ImageLoader.getInstance().displayImage(MyApplication.userInfo.getStyle().get(2).getImgUrl(), style_img3, options);
            } else {
                ImageLoader.getInstance().displayImage(MyApplication.userInfo.getStyle().get(0).getImgUrl(), style_img1, options);
                ImageLoader.getInstance().displayImage(MyApplication.userInfo.getStyle().get(1).getImgUrl(), style_img2, options);
                ImageLoader.getInstance().displayImage(MyApplication.userInfo.getStyle().get(2).getImgUrl(), style_img3, options);
            }
        }
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        //更新个人风三张图片信息
        setStylePic();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        imgUrl = null;
    }


}
