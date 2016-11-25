package com.teacherhelp.activity.teachcontent;


import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.teacherhelp.R;
import com.teacherhelp.adapter.teachcontent.Adapter;
import com.teacherhelp.app.MyApplication;
import com.teacherhelp.app.UrlConfig;
import com.teacherhelp.base.BaseActivity;
import com.teacherhelp.utils.CommonUtils;
import com.teacherhelp.utils.LogHelper;
import com.teacherhelp.utils.ShareReferenceUtils;
import com.teacherhelp.utils.teachcontent.TeachContentBitmapUtils;
import com.teacherhelp.utils.upload.MultiPartStack;
import com.teacherhelp.utils.upload.MultiPartStringRequest;
import com.teacherhelp.view.NoScrollGridView;

import org.json.JSONArray;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 教学内容详情或新增
 */
public class TeachContentDetailActivity extends BaseActivity {
    @Bind(R.id.imgbtn_back)
    ImageButton imgbtnBack;
    @Bind(R.id.tv_titletwo)
    TextView tvTitletwo;
    @Bind(R.id.tv_titleright)
    TextView tvTitleRight;
    @Bind(R.id.teachContentDetail_img)
    ImageView img;
    @Bind(R.id.teachContentDetail_name)
    TextView name;
    @Bind(R.id.teachContentDetail_number)
    TextView number;
    @Bind(R.id.teachContentDetail_type)
    TextView teachContentDetailType;
    @Bind(R.id.teachContentDetail_time)
    TextView time1;
    @Bind(R.id.teachContentDetail_time2)
    TextView time2;
    @Bind(R.id.teachContentDetail_text1)
    EditText text1;
    @Bind(R.id.teachContentDetail_text2)
    EditText text2;
    @Bind(R.id.teachContentDetail_text3)
    EditText text3;
    @Bind(R.id.teachContentDetail_add)
    TextView add;
    @Bind(R.id.teachContentDetail_delete)
    ImageView delete;
    @Bind(R.id.teachContentDetail_text4)
    EditText text4;
    @Bind(R.id.teachContentDetail_LL)
    LinearLayout deleteLL;
    @Bind(R.id.teachContentDetail_gridview1)
    NoScrollGridView mGridView;

    private String id;
    private String scheduleTimeId;
    private StringBuffer imgs;

    private List<Bitmap> data = new ArrayList<Bitmap>();
    private List<String> pathList = new ArrayList<String>();
    private String photoPath;
    private Adapter adapter;

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_teach_content_detail);
    }

    @Override
    protected void initVariables() {
        MyApplication.getInstance().addActivity(this);
        tvTitletwo.setVisibility(View.VISIBLE);
        tvTitleRight.setVisibility(View.VISIBLE);
        tvTitletwo.setText("教学内容");
        tvTitleRight.setText("提交");

        imgs = new StringBuffer();
        id = getIntent().getStringExtra("teachContentId");
        scheduleTimeId = getIntent().getStringExtra("scheduleTimeId");

        mSingleQueue = Volley.newRequestQueue(this, new MultiPartStack());//上传图片队列

        Bitmap bp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_addpic);
        data.add(bp);
        adapter = new Adapter(this, data);
        mGridView.setAdapter(adapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (data.size() == 10) {
                    CommonUtils.showToast("已达到最大图片数限度");
                } else {
                    if (position == data.size() - 1) {
                        Intent intent = new Intent(Intent.ACTION_PICK, null);
                        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                        startActivityForResult(intent, 0x1);
                    } else {

                    }
                }
            }
        });
        mGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                dialog(position);
                return true;
            }
        });
    }

    @Override
    protected void aadListenter() {
        imgbtnBack.setOnClickListener(this);
        tvTitleRight.setOnClickListener(this);
        add.setOnClickListener(this);
        delete.setOnClickListener(this);
    }


    @OnClick({R.id.teachContentDetail_add, R.id.teachContentDetail_delete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgbtn_back:
                finish();
                break;
            case R.id.tv_titleright:
                //提交教学内容
                if (id == null) {
                    return;
                }
                showWaitLoad(this, "正在上传...");
                int size = pathList.size();
                for (int i = 0; i < size; i++) {
                    upLoad(pathList.get(i));
                }
                addTeachContent(id, scheduleTimeId, "11111", "1");
                break;

            case R.id.teachContentDetail_add:
                deleteLL.setVisibility(View.VISIBLE);
                text4.setVisibility(View.VISIBLE);
                mGridView.setVisibility(View.VISIBLE);
                break;
            case R.id.teachContentDetail_delete:
                deleteLL.setVisibility(View.GONE);
                text4.setVisibility(View.GONE);
                mGridView.setVisibility(View.GONE);

                data.clear();
                Bitmap bp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_addpic);
                data.add(bp);
                photoPath = null;
                adapter.notifyDataSetChanged();
                break;
        }
    }


    /**
     * 提交教学内容
     */
    private void addTeachContent(final String id, final String scheduleTimeId, final String OrgId, final String schoolId) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:MM");
        Date curDate = new Date(System.currentTimeMillis());
        final String time = formatter.format(curDate);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConfig.POST_AddTeachContent, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                LogHelper.d("请求返回的结果：" + result);

                //请求成功
                JSONObject jsonObject = JSONObject.parseObject(result);
                int responseCode = jsonObject.getIntValue("code");
                switch (responseCode) {
                    case 1:
                        CommonUtils.showToast("提交成功");
                        DismissDialog();
                        finish();
                        break;
                    default:
                        CommonUtils.showToast("提交失败" + responseCode + jsonObject.get("msg"));
                        DismissDialog();
                        imgs.delete(0, imgs.length());//清空
                        break;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                CommonUtils.showToast("请求失败，请重试！");
                LogHelper.d(volleyError.getMessage());
                DismissDialog();

                imgs.delete(0, imgs.length());//清空
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Gson gson = new Gson();
                Map<String, String> map = new HashMap<String, String>();
                map.put("id", id);
                map.put("scheduleTimeId", scheduleTimeId);
                map.put("orgId", OrgId);
                map.put("schoolId", schoolId);
                map.put("material", text1.getText().toString());
                map.put("objective", text2.getText().toString());
                map.put("process", text3.getText().toString());
                map.put("explan", text4.getText().toString());
                map.put("ctime", time);


                if (pathList.isEmpty()) {
                    map.put("imgs", "");
                } else {

                    if (imgs.toString().length() > 1) {
                        String imgStr = imgs.toString();
                        String imgS = imgStr.substring(0, imgStr.length() - 1);
                        map.put("imgs", imgS);
                        Log.e("上传的图片地址", imgS);
                    }

                }

                String json = gson.toJson(map);
                Map<String, String> mapNew = new HashMap<String, String>();
                mapNew.put("jsonBean", json);
                return mapNew;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("token", ShareReferenceUtils.getValue("myToken"));
                return headers;
            }
        };
        setTmie(stringRequest);
        stringRequest.setTag("volleyAddTeachContent");
        MyApplication.getHttpQueue().add(stringRequest);
    }


    protected void dialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(TeachContentDetailActivity.this);
        builder.setMessage("是否删除该张图片");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                data.remove(position);
                pathList.remove(position);
                adapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0x1 && resultCode == RESULT_OK) {
            if (data != null) {
//                ContentResolver resolver = getContentResolver();
                try {
                    Uri uri = data.getData();
                    String[] proj = {MediaStore.Images.Media.DATA};
                    Cursor cursor = managedQuery(uri, proj, null, null, null);
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    photoPath = cursor.getString(column_index);
                    pathList.add(photoPath);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }

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
                    imgs.append(jsonObject.getString("data"));
                    imgs.append(",");
                    Log.e("上传教学内容图片成功,图片路径", imgs.toString());
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

        mSingleQueue.add(multiPartRequest);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(photoPath)) {
            Bitmap newBp = TeachContentBitmapUtils.decodeSampledBitmapFromFd(photoPath, 300, 300);
            data.remove(data.size() - 1);
            Bitmap bp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_addpic);
            data.add(newBp);
            data.add(bp);
            photoPath = null;
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
