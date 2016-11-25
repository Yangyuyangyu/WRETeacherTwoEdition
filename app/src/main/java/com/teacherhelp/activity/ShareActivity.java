package com.teacherhelp.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.WriterException;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.utils.Utility;
import com.teacherhelp.R;
import com.teacherhelp.adapter.ShareGridViewAdapter;
import com.teacherhelp.app.MyApplication;
import com.teacherhelp.app.UrlConfig;
import com.teacherhelp.base.BaseActivity;
import com.teacherhelp.base.ShareBean;
import com.teacherhelp.utils.CommonUtils;
import com.teacherhelp.view.NoScrollGridView;
import com.teacherhelp.zxing.encode.CodeCreator;
import com.tencent.connect.common.Constants;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXImageObject;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXTextObject;
import com.tencent.mm.sdk.platformtools.Util;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * 分享页面
 * Created by Administrator on 2016/7/20.
 */
public class ShareActivity extends BaseActivity implements IUiListener {

    @Bind(R.id.imgbtn_back)
    ImageButton imgbtnBack;
    @Bind(R.id.tv_titletwo)
    TextView tvTitletwo;
    @Bind(R.id.Share_image)
    ImageView Image;
    @Bind(R.id.Share_gridview)
    NoScrollGridView Gridview;

    private String[] Name = {"朋友圈", "微信", "QQ空间", "QQ好友", "微博"};
    private int[] pic = {R.mipmap.share_we_chat_circle_icon, R.mipmap.share_we_chat_icon, R.mipmap.share_qq_zone_icon, R.mipmap.share_qq_icon, R.mipmap.share_sina_logo};
    private ArrayList<ShareBean> list;
    private ShareGridViewAdapter adapter;
    private Bitmap bitmapImg;

    //QQ分享
    private Tencent mTencent;// 新建Tencent实例用于调用分享方法
    private IUiListener mIUiListener;
    private Bundle params;
    //微信分享
    private IWXAPI api;
    //新浪分享
    private IWeiboShareAPI mWeiboShareAPI;


    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_share);
    }

    @Override
    protected void aadListenter() {
        imgbtnBack.setOnClickListener(this);
    }

    @Override
    protected void initVariables() {
        MyApplication.getInstance().addActivity(this);
        tvTitletwo.setVisibility(View.VISIBLE);
        tvTitletwo.setText("推荐分享");
        String url = "https://www.baidu.com/";
        try {
            bitmapImg = CodeCreator.createQRCode(url);
            Image.setImageBitmap(bitmapImg);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        //初始化RecyclerView
        initRecyclerView();

        mTencent = Tencent.createInstance(UrlConfig.Share_QQ, getApplicationContext());
        regToWx();

        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(this, UrlConfig.Share_Sina);
        mWeiboShareAPI.registerApp();


    }


    private void regToWx() {
        //通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(this, UrlConfig.Share_Wx, true);
        api.registerApp(UrlConfig.Share_Wx);

    }

    private void initRecyclerView() {
        list = new ArrayList<>();

        upData();
        adapter = new ShareGridViewAdapter(ShareActivity.this, list);
        adapter.setMyClick(new ShareGridViewAdapter.MyClick() {
            @Override
            public void wxFriend() {
                shareImgToWx(bitmapImg, 1);
            }

            @Override
            public void wxShare() {
                shareImgToWx(bitmapImg, 0);
            }

            @Override
            public void qqZone() {
                shareToQZone();
            }

            @Override
            public void qqFriend() {
                shareToQQ();
            }

            @Override
            public void wbSina() {
                Share_Sina(bitmapImg);
            }
        });
        Gridview.setAdapter(adapter);

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

    private void upData() {
        for (int i = 0; i < 5; i++) {
            ShareBean bean = new ShareBean();
            bean.setImg(pic[i]);
            bean.setName(Name[i]);
            list.add(bean);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Tencent.onActivityResultData(requestCode, resultCode, data, mIUiListener);
        if (requestCode == Constants.REQUEST_API) {
            if (resultCode == Constants.REQUEST_QQ_SHARE || resultCode == Constants.REQUEST_QZONE_SHARE || resultCode == Constants.REQUEST_OLD_SHARE) {
                Tencent.handleResultData(data, mIUiListener);
            }
        }
    }

    @Override
    public void onComplete(Object o) {

    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onError(UiError uiError) {

    }


    /**
     * 分享到QQ好友
     */
    private void shareToQQ() {
        params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, "标题");// 标题
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, "要分享的摘要");// 摘要
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, "http://www.qq.com/news/1.html");// 内容地址
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, "http://imgcache.qq.com/qzone/space_item/pre/0/66768.gif");// 网络图片地址　　params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "应用名称");// 应用名称
        params.putString(QQShare.SHARE_TO_QQ_EXT_INT, "其它附加功能");
        // 分享操作要在主线程中完成
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                mTencent.shareToQQ(ShareActivity.this, params, mIUiListener);
            }
        });
    }

    /**
     * 分享到QQ空间
     */
    private void shareToQZone() {
        params = new Bundle();
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, "标题");// 标题
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, "要分享的摘要");// 摘要
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, "http://www.qq.com/news/1.html");// 内容地址
        ArrayList<String> imgUrlList = new ArrayList<>();
        imgUrlList.add("http://f.hiphotos.baidu.com/image/h%3D200/sign=6f05c5f929738bd4db21b531918a876c/6a600c338744ebf8affdde1bdef9d72a6059a702.jpg");
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imgUrlList);// 图片地址
        // 分享操作要在主线程中完成
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                mTencent.shareToQzone(ShareActivity.this, params, mIUiListener);
            }
        });
    }

    /**
     * 微信文本分享
     */
    private void shareTextToWx(String text, int type) {
        if (!api.isWXAppInstalled()) {
            CommonUtils.showToast("本机尚未安装微信客户端");
            return;
        }
        WXTextObject textObject = new WXTextObject();
        textObject.text = text;
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObject;
        msg.description = text;
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());//唯一字段标识请求
        req.message = msg;
        if (type == 0) {
            req.scene = SendMessageToWX.Req.WXSceneSession;//好友会话
        } else if (type == 1) {
            req.scene = SendMessageToWX.Req.WXSceneTimeline;//朋友圈
        }
        api.sendReq(req);

    }

    /**
     * 微信图片分享
     */
    private void shareImgToWx(Bitmap bmp, int type) {
        if (!api.isWXAppInstalled()) {
            CommonUtils.showToast("本机尚未安装微信客户端");
            return;
        }

        WXImageObject imgObj = new WXImageObject(bmp);
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;
        //设置缩略图
        Matrix matrix = new Matrix();
        matrix.postScale(100, 100);
        Bitmap thumbBmp = Bitmap.createBitmap(bmp, 0, 0, 100, 100, matrix, true);
        thumbBmp.recycle();
        msg.thumbData = Util.bmpToByteArray(thumbBmp, true);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());//唯一字段标识请求
        req.message = msg;
        if (type == 0) {
            req.scene = SendMessageToWX.Req.WXSceneSession;//好友会话
        } else if (type == 1) {
            req.scene = SendMessageToWX.Req.WXSceneTimeline;//朋友圈
        }
        api.sendReq(req);

    }

    /**
     * 新浪微博分享
     */
    private void Share_Sina(Bitmap bmp) {
        //这是分享网页的配置
        WebpageObject mediaObject = new WebpageObject();
        mediaObject.identify = Utility.generateGUID();
        mediaObject.title = "title";
        mediaObject.description = "描述";
        mediaObject.setThumbImage(bmp);
        mediaObject.actionUrl = "http://www.baidu.com";

        /**
         * 无论分享什么 都要添加这些代码 通过
         * request.multiMessage = weiboMessage;
         * 实现具体内容的分享（网页 图片 文字 .....）
         */
        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();//初始化微博的分享消息
        weiboMessage.mediaObject = mediaObject;
        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.multiMessage = weiboMessage;

        /**
         * 唤醒微博分享页面第二种方式
         * 如果安装了客户端 就调起客户端 如果没有安装 就调起网页
         */

        AuthInfo authInfo = new AuthInfo(this, UrlConfig.Share_Sina, UrlConfig.Sina_REDIRECT_URL, UrlConfig.Sina_SCOPE);
        mWeiboShareAPI.sendRequest(this, request, authInfo, "", new WeiboAuthListener() {

            @Override
            public void onWeiboException(WeiboException arg0) {
                CommonUtils.showToast("分享失败:" + arg0.getMessage());
            }

            @Override
            public void onComplete(Bundle bundle) {
                CommonUtils.showToast("分享成功");
            }

            @Override
            public void onCancel() {
                CommonUtils.showToast("您取消了分享");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        api.unregisterApp();//取消wx注册
    }
}
