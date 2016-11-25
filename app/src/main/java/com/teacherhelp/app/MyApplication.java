package com.teacherhelp.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.baidu.mapapi.SDKInitializer;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.teacherhelp.R;
import com.teacherhelp.activity.LoginActivity;
import com.teacherhelp.utils.ShareReferenceUtils;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;

/**
 * Created by Administrator on 2016/5/23.自定义Appcation
 */
public class MyApplication extends Application {

    /**
     * 管理activity
     */
    private Activity activity = null;
    private List<Activity> mList = new LinkedList<>();
    private List<Activity> mListForget = new LinkedList<>();

    private static MyApplication instance;
    private static boolean ismessage = true; //用于判断是会话还是联系人
    //保存老师信息
    public static UserInfo userInfo;

    /**
     * volley
     */
    public static RequestQueue queue;

    /**
     * 解决分包限制
     * @param base
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();


        //初始化图片缓存
        initImageLoader();

        //管理activity
        instance = this;

        //初始化ShareReferenceUtils
        ShareReferenceUtils.init(getApplicationContext());

        //初始化常用工具类
        com.teacherhelp.utils.CommonUtils.init(getApplicationContext());

        //初始化volley
        queue = Volley.newRequestQueue(getApplicationContext());//使用全局上下文

        //百度地图初始化
        SDKInitializer.initialize(getApplicationContext());


        /**
         * OnCreate 会被多个进程重入，这段保护代码，确保只有您需要使用 RongIM 的进程和 Push 进程执行了 init。
         * io.rong.push 为融云 push 进程名称，不可修改。
         */
        if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext())) ||
                "io.rong.push".equals(getCurProcessName(getApplicationContext()))) {

            /**
             * IMKit SDK调用第一步 初始化
             */
            RongIM.init(this);

            /**
             * 当连接成功融云聊天消息点击事件，返回false走默认处理
             * 融云连接状态判断
             */
            if (RongIM.getInstance() != null && RongIM.getInstance().getCurrentConnectionStatus() == RongIMClient.ConnectionStatusListener.ConnectionStatus.KICKED_OFFLINE_BY_OTHER_CLIENT)
                //在其他设备登录,弹窗返回到登录界面
                new MaterialDialog.Builder(this)
                        .content("您的账号已在其他设备上登录，请重新登录或修改密码")
                        .positiveText("确定")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                //执行退出账号操作
                                MyApplication.getInstance().exit();
                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                            }
                        })
                        .build()
                        .show();
        } else if (RongIM.getInstance() != null && RongIM.getInstance().getCurrentConnectionStatus() == RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTED) {
            RongIM.getInstance().setConversationBehaviorListener(new RongIM.ConversationBehaviorListener() {
                @Override
                public boolean onUserPortraitClick(Context context, Conversation.ConversationType conversationType, io.rong.imlib.model.UserInfo userInfo) {
                    return false;
                }

                @Override
                public boolean onUserPortraitLongClick(Context context, Conversation.ConversationType conversationType, io.rong.imlib.model.UserInfo userInfo) {
                    return false;
                }

                @Override
                public boolean onMessageClick(Context context, View view, Message message) {
                    return false;
                }

                @Override
                public boolean onMessageLinkClick(Context context, String s) {
                    return false;
                }

                @Override
                public boolean onMessageLongClick(Context context, View view, Message message) {
                    return false;
                }
            });
        }


    }

    /**
     * 获得当前进程的名字
     *
     * @param context
     * @return
     */
    public static String getCurProcessName(Context context) {

        int pid = android.os.Process.myPid();

        ActivityManager activityManager = (ActivityManager) context.getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {

            if (appProcess.pid == pid) {

                return appProcess.processName;
            }
        }
        return null;
    }


    private void initImageLoader() {
        File cacheDir = StorageUtils.getOwnCacheDirectory(getApplicationContext(), "WrEducation/cache");
        if (!cacheDir.exists())
            cacheDir.mkdirs();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .memoryCacheExtraOptions(480, 800) // max width, max height，即保存的每个缓存文件的最大长宽
                .diskCacheExtraOptions(480, 800, null)//设置缓存的详细信息，最好不要设置这个
                .threadPoolSize(3) //线程池内加载的数量
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))//你可以通过自己的内存缓存实现
                .memoryCacheSize(2 * 1024 * 1024)
                .diskCache(new UnlimitedDiskCache(cacheDir))//自定义缓存路径
                .diskCacheSize(100 * 1024 * 1024)
                .diskCacheFileCount(300) //缓存的文件数量
                .defaultDisplayImageOptions(getDisplayImageOptions())
                .build();//开始构建
        ImageLoader.getInstance().init(config);//全局初始化此配置
    }

    private DisplayImageOptions getDisplayImageOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnFail(R.drawable.ic_gf_default_photo)//设置图片在下载期间显示的图片
                .showImageOnLoading(R.drawable.ic_gf_default_photo)//设置图片Uri为空或是错误的时候显示的图片
                .showImageForEmptyUri(R.drawable.ic_gf_default_photo)//设置图片加载/解码过程中错误时候显示的图片
//                .delayBeforeLoading(1000)//下载前的延迟时间
                .cacheInMemory(true) //设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)//设置下载的图片是否缓存在SD卡中
                .considerExifParams(false)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) //设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.ARGB_8888) //设置图片的解码类型
                .displayer(new FadeInBitmapDisplayer(500)) //是否图片加载好后渐入的动画时间
                .build();//构建完成
        return options;
    }

    public static RequestQueue getHttpQueue() {
        return queue;
    }


    public static MyApplication getInstance() {
        return instance;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    // add Activity
    public void addActivity(Activity activity) {
        mList.add(activity);
    }

    public void addForgetActivity(Activity activity) {
        mListForget.add(activity);
    }

    public void finishForgetActivity() {
        try {
            for (Activity activity : mListForget) {
                if (activity != null)
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean ismessage() {
        return ismessage;
    }

    public static void setIsmessage(boolean ismessage) {
        MyApplication.ismessage = ismessage;
    }

    public void exit() {
        try {
            for (Activity activity : mList) {
                if (activity != null)
                    activity.finish();
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //杀掉后台进程
//            System.exit(0);
        }
    }

    public void exitAll() {
        try {
            for (Activity activity : mList) {
                if (activity != null)
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(10);   //返回值为0代表正常退出
        }
    }

}
