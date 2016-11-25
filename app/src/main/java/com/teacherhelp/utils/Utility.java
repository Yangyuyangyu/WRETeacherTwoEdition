package com.teacherhelp.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.teacherhelp.R;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 方法辅助类
 *
 * @author Tangji
 */
public class Utility {
    public static Toast toast = null;
    public static TextView textView = null;
    public static Snackbar snackbar = null;
    public static MaterialDialog loadingdialog=null;

    private Utility() {
    }

    public static String encodeUrl(Map<String, String> param) {
        if (param == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        Set<String> keys = param.keySet();
        boolean first = true;

        for (String key : keys) {
            String value = param.get(key);
            // pain...EditMyProfileDao params' values can be empty
            if (!TextUtils.isEmpty(value)) {
                if (first)
                    first = false;
                else
                    sb.append("&");
                try {
                    sb.append(URLEncoder.encode(key, "UTF-8")).append("=")
                            .append(URLEncoder.encode(param.get(key), "UTF-8"));
                } catch (UnsupportedEncodingException e) {

                }
            }
        }
        return sb.toString();
    }

    public static Bundle decodeUrl(String s) {
        Bundle params = new Bundle();
        if (s != null) {
            String array[] = s.split("&");
            for (String parameter : array) {
                String v[] = parameter.split("=");
                try {
                    params.putString(URLDecoder.decode(v[0], "UTF-8"),
                            URLDecoder.decode(v[1], "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();

                }
            }
        }
        return params;
    }

    public static void closeSilently(Closeable closeable) {
        if (closeable != null)
            try {
                closeable.close();
            } catch (IOException ignored) {

            }
    }

    /**
     * Parse a URL query and fragment parameters into a key-value bundle.
     */
    public static Bundle parseUrl(String url) {
        url = url.replace("weiboconnect", "http");
        try {
            URL u = new URL(url);
            Bundle b = decodeUrl(u.getQuery());
            b.putAll(decodeUrl(u.getRef()));
            return b;
        } catch (MalformedURLException e) {
            return new Bundle();
        }
    }

    public static int length(String paramString) {
        int i = 0;
        for (int j = 0; j < paramString.length(); j++) {
            if (paramString.substring(j, j + 1).matches("[Α-￥]"))
                i += 2;
            else
                i++;
        }

        if (i % 2 > 0) {
            i = 1 + i / 2;
        } else {
            i = i / 2;
        }

        return i;
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }

    public static boolean isWifi(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                return true;
            }
        }
        return false;
    }

    public static int getNetType(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return networkInfo.getType();
        }
        return -1;
    }

    public static boolean isGprs(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            if (networkInfo.getType() != ConnectivityManager.TYPE_WIFI) {
                return true;
            }
        }
        return false;
    }

    public static boolean isSystemRinger(Context context) {
        AudioManager manager = (AudioManager) context
                .getSystemService(Context.AUDIO_SERVICE);
        return manager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL;
    }

    public static String getPicPathFromUri(Uri uri, Activity activity) {
        String value = uri.getPath();

        if (value.startsWith("/external")) {
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = activity.managedQuery(uri, proj, null, null, null);
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else {
            return value;
        }
    }

    public static boolean isAllNotNull(Object... obs) {
        for (int i = 0; i < obs.length; i++) {
            if (obs[i] == null) {
                return false;
            }
        }
        return true;
    }

    public static boolean isIntentSafe(Activity activity, Uri uri) {
        Intent mapCall = new Intent(Intent.ACTION_VIEW, uri);
        PackageManager packageManager = activity.getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(
                mapCall, 0);
        return activities.size() > 0;
    }

    public static boolean isIntentSafe(Activity activity, Intent intent) {
        PackageManager packageManager = activity.getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(
                intent, 0);
        return activities.size() > 0;
    }

    public static boolean isGooglePlaySafe(Activity activity) {
        Uri uri = Uri
                .parse("http://play.google.com/store/apps/details?id=com.google.android.gms");
        Intent mapCall = new Intent(Intent.ACTION_VIEW, uri);
        mapCall.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        mapCall.setPackage("com.android.vending");
        PackageManager packageManager = activity.getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(
                mapCall, 0);
        return activities.size() > 0;
    }

    public static Rect locateView(View v) {
        int[] location = new int[2];
        if (v == null)
            return null;
        try {
            v.getLocationOnScreen(location);
        } catch (NullPointerException npe) {
            return null;
        }
        Rect locationRect = new Rect();
        locationRect.left = location[0];
        locationRect.top = location[1];
        locationRect.right = locationRect.left + v.getWidth();
        locationRect.bottom = locationRect.top + v.getHeight();
        return locationRect;
    }

    public static int countWord(String content, String word, int preCount) {
        int count = preCount;
        int index = content.indexOf(word);
        if (index == -1) {
            return count;
        } else {
            count++;
            return countWord(content.substring(index + word.length()), word,
                    count);
        }
    }

    public static String getIdFromWeiboAccountLink(String url) {
        String id = url.substring(19);
        id = id.replace("/", "");
        return id;
    }

    public static String getDomainFromWeiboAccountLink(String url) {
        String domain = url.substring(17);
        domain = domain.replace("/", "");
        return domain;
    }

    public static boolean isWeiboAccountIdLink(String url) {
        return !TextUtils.isEmpty(url) && url.startsWith("http://weibo.com/u/");
    }

    public static boolean isWeiboAccountDomainLink(String url) {
        if (TextUtils.isEmpty(url)) {
            return false;
        } else {
            boolean a = url.startsWith("http://weibo.com/");
            boolean b = !url.contains("?");
            return a && b;
        }
    }

    public static void playClickSound(View view) {
        view.playSoundEffect(SoundEffectConstants.CLICK);
    }

    public static View getListViewItemViewFromPosition(ListView listView,
                                                       int position) {
        return listView.getChildAt(position
                - listView.getFirstVisiblePosition());
    }

    public static String getMotionEventStringName(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                return "MotionEvent.ACTION_DOWN";
            case MotionEvent.ACTION_UP:
                return "MotionEvent.ACTION_UP";
            case MotionEvent.ACTION_CANCEL:
                return "MotionEvent.ACTION_CANCEL";
            case MotionEvent.ACTION_MOVE:
                return "MotionEvent.ACTION_MOVE";
            default:
                return "Other";
        }
    }

    /**
     * 检测服务是否启动
     *
     * @param @param  serviceName
     * @param @return
     * @return boolean
     * @Title: isWorked
     * @Description: TODO
     */
    public synchronized static boolean isWorked(String serviceName,
                                                Context context) {
        ActivityManager myManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<RunningServiceInfo> runningService = (ArrayList<RunningServiceInfo>) myManager
                .getRunningServices(30);
        for (int i = 0; i < runningService.size(); i++) {
            if (runningService.get(i).service.getClassName().toString()
                    .equals(serviceName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Toast提示
     *
     * @param
     * @param context
     */
    @SuppressLint("ResourceAsColor")
    public static void showToastNoNetWork(Context context) {
        // Toast.makeText(context.getApplicationContext(), com,
        // Toast.LENGTH_LONG).show();
        if (toast == null) {
            textView = new TextView(context);
            toast = new Toast(context.getApplicationContext());
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(textView);
            textView.setText("没有网络请检查连接！");
            textView.setTextSize(18);
            textView.setPadding(20, 10, 20, 10);
            textView.setTextColor(0xffffffff);
            textView.setBackgroundColor(0xbb000000); // 0xff6B9CD8
        } else {
            textView.setText("没有网络请检查连接！");
        }
        toast.show();
    }

    /**
     * Toast提示
     *
     * @param com
     * @param context
     */
    @SuppressLint("ResourceAsColor")
    public static void showToast(String com, Context context) {
        // Toast.makeText(context.getApplicationContext(), com,
        // Toast.LENGTH_LONG).show();
        if (toast == null) {
            textView = new TextView(context);
            toast = new Toast(context.getApplicationContext());
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(textView);
            textView.setText(com);
            textView.setTextSize(18);
            textView.setPadding(20, 10, 20, 10);
            textView.setTextColor(0xffffffff);
            textView.setBackgroundColor(0xbb000000); // 0xff6B9CD8
        } else {
            textView.setText(com);
        }
        toast.show();
    }

    /**
     * Snackbar提示
     *
     * @param context
     */
    public static void showSnackbarNoNetWork(View view, Context context) {
        snackbar = Snackbar.make(view, "没有网络请检查连接！", Snackbar.LENGTH_LONG);
        Snackbar.SnackbarLayout ve = (Snackbar.SnackbarLayout) snackbar.getView();
        ((TextView) ve.findViewById(R.id.snackbar_text)).setTextColor(Color.parseColor("#FFFFFF"));
        snackbar.show();
    }

    /**
     * Snackbar提示
     *
     * @param com
     * @param context
     */
    public static void showSnackbar(View view, String com, Context context) {
        snackbar = Snackbar.make(view, com, Snackbar.LENGTH_LONG);
        Snackbar.SnackbarLayout ve = (Snackbar.SnackbarLayout) snackbar.getView();
        ((TextView) ve.findViewById(R.id.snackbar_text)).setTextColor(Color.parseColor("#FFFFFF"));
        snackbar.show();
    }

    public static MaterialDialog ShowWaitDialog(Context context, String content){
        loadingdialog=new MaterialDialog.Builder(context)
                .content(content)
                .progress(true, 0)
                .build();
        return loadingdialog;
    }

    public static void DisMissWaitDialog(){
//        if(loadingdialog!=null){
            loadingdialog.dismiss();
//            loadingdialog=null;
//        }
    }


    /**
     * Dialog提示
     *
     * @param com
     * @param context
     */
    public static void showTextDialog(final View view, String com, final Context context,String rightbtn,String leftbtn,MaterialDialog.SingleButtonCallback buttonCallback) {
        new MaterialDialog.Builder(context)
                .content(com)
                .positiveText(rightbtn)
                .onPositive(buttonCallback)
                .negativeText(leftbtn)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {

                    }
                })
                .build()
                .show();

    }




    /**
     * 网络是否可用，包括3G和WIFI中的任意一种
     */
    public static boolean NetworkAvailable(Context context) {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info == null || !info.isConnected()) {
                return false;
            }
            if (info.isRoaming()) {
                return true;
            }
        } catch (NullPointerException e) {
        } catch (RuntimeException e) {
        }
        return true;
    }

    public static int NetworkType(Context context) {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info == null)
                return -1;
            return info.getType();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * GPRS/3G 是否可用
     */
    public static boolean GprsNetworkAvailable(Context context) {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity == null) {
                return false;
            } else {
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info == null) {
                    return false;
                } else {
                    if (info.isAvailable()) {
                        return true;
                    }
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        } catch (RuntimeException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

//    /**
//     * wifi 是否可用
//     */
//    public static boolean WifiNetworkAvailable(Context context) {
//        try {
//            WifiManager wifiManager = (WifiManager) context
//                    .getSystemService(Context.WIFI_SERVICE);
//            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
//            int ipAddress = wifiInfo == null ? 0 : wifiInfo.getIpAddress();//
//
//            if (wifiManager.isWifiEnabled() && ipAddress != 0) {
//                return true;
//            } else {
//                return false;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }

    /**
     * 获取SD卡剩余空间，单位 MB
     */
    public static long getSDAvailaleSpace() {

        File path = Environment.getExternalStorageDirectory(); // 取得sdcard文件路径
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return (availableBlocks * blockSize) / 1024 / 1024;
        // (availableBlocks * blockSize)/1024 KIB 单位
        // (availableBlocks * blockSize)/1024 /1024 MIB单位

    }

    // dip转像素
    public static int DipToPixels(Context context, int dip) {
        final float SCALE = context.getResources().getDisplayMetrics().density;

        float valueDips = dip;
        int valuePixels = (int) (valueDips * SCALE + 0.5f);

        return valuePixels;

    }

    // 像素转dip
    public static float PixelsToDip(Context context, int Pixels) {
        final float SCALE = context.getResources().getDisplayMetrics().density;

        float dips = Pixels / SCALE;

        return dips;

    }

    /**
     * @根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * @得到手机的dpi
     */
    public static float getdpi(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    /**
     * @根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @param （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public static String getVersion(Context ctx) {
        if (ctx == null)
            return "";
        String versionName = "";
        PackageInfo packageInfo;
        try {
            packageInfo = ctx.getPackageManager().getPackageInfo(
                    ctx.getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    public static int getVersionCode(Context ctx) {
        if (ctx == null)
            return 0;
        try {
            return ctx.getPackageManager().getPackageInfo("com.committee.app",
                    0).versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getDeviceId(Context ctx) {
        if (ctx == null)
            return "";
        return ((TelephonyManager) ctx
                .getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
    }

    /*
     * 加密过后的设备号
     */
    public static String getPhoneSerialNumber(Context mContext) {
        final TelephonyManager tm = (TelephonyManager) mContext
                .getSystemService(Context.TELEPHONY_SERVICE);
        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = ""
                + android.provider.Settings.Secure.getString(
                mContext.getContentResolver(),
                android.provider.Settings.Secure.ANDROID_ID);
        UUID deviceUuid = new UUID(androidId.hashCode(),
                ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        return deviceUuid.toString();

    }

    public static void recycleViewGroupAndChildViews(ViewGroup viewGroup,
                                                     boolean recycleBitmap) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {

            View child = viewGroup.getChildAt(i);

            if (child instanceof WebView) {
                WebView webView = (WebView) child;
                webView.loadUrl("about:blank");
                webView.stopLoading();
                continue;
            }

            if (child instanceof ViewGroup) {
                recycleViewGroupAndChildViews((ViewGroup) child, true);
                continue;
            }

            if (child instanceof ImageView) {
                ImageView iv = (ImageView) child;
                Drawable drawable = iv.getDrawable();
                if (drawable instanceof BitmapDrawable) {
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                    Bitmap bitmap = bitmapDrawable.getBitmap();
                    if (recycleBitmap && bitmap != null) {
                        bitmap.recycle();
                    }
                }
                iv.setImageBitmap(null);
                iv.setBackgroundDrawable(null);
                continue;
            }

            child.setBackgroundDrawable(null);

        }

        viewGroup.setBackgroundDrawable(null);
    }

    // ///////////////////////////////////file Utility/////////////////////

    public static long MB = 1024 * 1024;
    private final static int FILESIZE = 4 * 1024;

    /**
     * 从输入流中获取数据
     *
     * @param inStream 输入流
     * @return
     * @throws Exception
     */
    public static byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();
    }

    /**
     * 写入文件
     */
    public static void writeToFile(File file, Bitmap data) throws Exception {

        FileOutputStream fOut = null;
        try {
            if (!file.exists())
                file.createNewFile();
            fOut = new FileOutputStream(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        data.compress(Bitmap.CompressFormat.JPEG, 100, fOut);// 把Bitmap对象解析成流
        try {
            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return;
    }

    /**
     * 根据URL得到输入流
     *
     * @param urlStr
     * @return
     */
    public static InputStream getInputFileStreamFromURL(String urlStr) {
        HttpURLConnection urlConn = null;
        InputStream inputStream = null;
        try {
            URL url = new URL(urlStr);
            urlConn = (HttpURLConnection) url.openConnection();
            inputStream = urlConn.getInputStream();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return inputStream;
    }

    public static String writeToFile(Bitmap data) throws Exception {
        File file = File.createTempFile(
                String.valueOf(System.currentTimeMillis()), ".png");
        FileOutputStream fOut = null;
        try {
            if (!file.exists())
                file.createNewFile();
            fOut = new FileOutputStream(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        data.compress(Bitmap.CompressFormat.JPEG, 100, fOut);// 把Bitmap对象解析成流
        try {
            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }

    /**
     * 将一个InputStream里面的数据写入到SD卡中
     *
     * @param path
     * @param fileName
     * @param input
     * @return
     */
    public static File write2SDFromInput(String path, String fileName,
                                         InputStream input) {
        File file = null;
        OutputStream output = null;
        try {
            mkDirs(path);
            file = createSDFile(path, fileName);
            output = new FileOutputStream(file);
            byte[] buffer = new byte[FILESIZE];
            while ((input.read(buffer)) != -1) {
                output.write(buffer);
            }
            output.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /**
     * 将一个InputStream里面的数据写入到SD卡中
     *
     * @param path
     * @param fileName
     * @param input
     * @return
     */
    public static File writeGuideImg2SDFromInput(String path, String fileName,
                                                 InputStream input) {
        File file = null;
        OutputStream output = null;
        try {
            mkDirs(path);
            file = createSDFile(path, fileName);
            output = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = input.read(buffer)) != -1) {
                output.write(buffer, 0, len);
            }
            input.close();
            output.close();
            output.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /**
     * 在SD卡上创建文件
     *
     * @param fileName
     * @return
     * @throws IOException
     */
    public static File createSDFile(String path, String fileName)
            throws IOException {
        File file = new File(path, fileName);
        file.createNewFile();
        return file;
    }

    public static File createFile(String path, String fileName)
            throws IOException {
        File file = new File(path, fileName);
        file.createNewFile();
        return file;
    }

    public static boolean mkDir(String dirPath) {
        File mPath = new File(dirPath);
        if (mPath.exists()) {
            return false;
        }
        return mPath.mkdir();

    }

    public static boolean mkDirs(String dirPath) {

        File mPath = new File(dirPath);
        if (mPath.exists()) {
            return false;
        }
        return mPath.mkdirs();

    }

    /*
     * 判断SD卡是否存在
     */
    public static boolean isSDExist() {
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {

            return true;
        }

        return false;
    }

    public static boolean isExist(String path) {
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            File f = new File(path);
            if (f.exists()) {
                return true;
            }
        }

        return false;
    }

    public static long getFileSize(String path) {
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            return new File(path).length();
        }
        return 0;
    }

    public static void deleteFile(String path) {
        if (TextUtils.isEmpty(path))
            return;
        try {

            if (Environment.MEDIA_MOUNTED.equals(Environment
                    .getExternalStorageState())) {
                new File(path).delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }

    /**
     * 获取文件大小
     *
     * @param length
     * @return
     */
    public static String formatFileSize(long length) {
        String result = null;
        int sub_string = 0;
        if (length >= 1073741824) {
            sub_string = String.valueOf((float) length / 1073741824).indexOf(
                    ".");
            result = ((float) length / 1073741824 + "000").substring(0,
                    sub_string + 3) + "GB";
        } else if (length >= 1048576) {
            sub_string = String.valueOf((float) length / 1048576).indexOf(".");
            result = ((float) length / 1048576 + "000").substring(0,
                    sub_string + 3) + "MB";
        } else if (length >= 1024) {
            sub_string = String.valueOf((float) length / 1024).indexOf(".");
            result = ((float) length / 1024 + "000").substring(0,
                    sub_string + 3) + "KB";
        } else if (length < 1024)
            result = Long.toString(length) + "B";
        return result;
    }

    public static String GetFileName(String URL, String type) {
        try {

            int end = URL.indexOf(type);
            if (end != -1) {
                String temp = URL.substring(0, end + 3);
                int start = temp.lastIndexOf("/");

                return (temp.substring(start + 1));
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return null;

    }

    public static boolean downGuideImgLoadFile(String url, String path,
                                               String filename) {
        try {
            InputStream inputStream = null;
            try {

                if (!Utility.isExist(path))
                    Utility.mkDirs(path);
                if (Utility.isExist(TextUtils.isEmpty(filename) ? path
                        + File.separator + url + ".jpg" : path + File.separator
                        + filename)) {
                    return false;
                } else {
                    inputStream = Utility.getInputFileStreamFromURL(url);
                    if (inputStream != null)
                        Utility.writeGuideImg2SDFromInput(path, TextUtils
                                        .isEmpty(filename) ? url + ".jpg" : filename,
                                inputStream);
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

    /**
     * 递归删除文件和文件夹，注意，别乱用，防止誤殺
     */
    public static void DeleteFile(File file) {
        if (file.isFile()) {
            file.deleteOnExit();
            return;
        }
        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                file.delete();
                return;
            }
            for (File f : childFile) {
                DeleteFile(f);
            }
            file.delete();
        }
    }

    /**
     * md5加密方法
     *
     * @param string
     * @return
     */
    public static String md5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(
                    string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    /* 时间戳转换成自定义格式字符串 */
    @SuppressLint("SimpleDateFormat")
    public static String getDateToString(String time, String formatString) {
        SimpleDateFormat sdf = new SimpleDateFormat(formatString);
        String date = sdf.format(new Date(Long.parseLong(time + "000")));
        return date;
    }

    // 禁止快速点击
    private static long lastClickTime = 0;

    public synchronized static boolean isFastClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < 1000) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /**
     * 浏览器属性设置
     *
     * @param mWebView
     */
    public static void webViewBuildSettings(WebView mWebView) {
        // mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        // //优先使用缓存
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setSupportMultipleWindows(true);// 多窗口
        mWebView.getSettings().setPluginState(PluginState.ON);// 视频播放只支持2.2以上的版本
        // mWebView.getSettings().setPluginsEnabled(true);// 视频播放支持所有版本
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.getSettings().setDefaultTextEncodingName("UTF-8");
        // mWebView.getSettings().setDefaultTextEncodingName("GBK");
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        // mWebView.getSettings().setUserAgent(0);
        mWebView.getSettings()
                .setUserAgentString(
                        "Mozilla/5.0 (Linux; U; Android 4.0.1; zh-cn; MB525 Build/3.4.2-117) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1");

        return;
    }

    /**
     * 检测是否开启gps
     */
    public static final boolean isGPSOpen(Context context) {
        LocationManager locationManager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        // boolean network =
        // locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        // if (gps || network) {
        // return true;
        // }
        if (gps) {
            return true;
        }
        return false;
    }

    /**
     * 转义字符，编码转字符
     *
     * @param UNCoding
     * @return
     */
    public static String UNCoding2Character(String UNCoding) {
        // UNCoding = ToDBC(UNCoding);
        // // UNCoding = UNCoding.replaceAll("\t", "&#160;");
        // UNCoding = UNCoding.replaceAll("&#160;", " ");
        // UNCoding = UNCoding.replaceAll("&#60;", "<");
        // UNCoding = UNCoding.replaceAll("&#62;", ">");
        // UNCoding = UNCoding.replaceAll("&#38;", "&");
        // UNCoding = UNCoding.replaceAll("&#34;", "\"");
        // UNCoding = UNCoding.replaceAll("&#39;", "'");
        // UNCoding = UNCoding.replaceAll("&#162;", "￠");
        // UNCoding = UNCoding.replaceAll("&#163;", "£");
        // UNCoding = UNCoding.replaceAll("&#165;", "¥");
        // UNCoding = UNCoding.replaceAll("&#8364;", "€");
        // UNCoding = UNCoding.replaceAll("&#167;", "§");
        // UNCoding = UNCoding.replaceAll("&#169;", "©");
        // UNCoding = UNCoding.replaceAll("&#174;", "®");
        // UNCoding = UNCoding.replaceAll("&#8482;", "™");
        // UNCoding = UNCoding.replaceAll("&#215;", "×");
        // UNCoding = UNCoding.replaceAll("&#247;", "÷");
        // UNCoding = UNCoding.replaceAll("&quot;", "\"");
        // UNCoding = UNCoding.replaceAll("&nbsp;", " ");
        // UNCoding = UNCoding.replaceAll("&apos;", "'");
        // UNCoding = UNCoding.replaceAll("&lt;", "<");
        // UNCoding = UNCoding.replaceAll("&gt;", ">");
        // UNCoding = UNCoding.replaceAll("&copy", "©");
        // UNCoding = UNCoding.replaceAll("&reg;", "®");
        // UNCoding = UNCoding.replaceAll("&iexcl;", "¡");
        // UNCoding = UNCoding.replaceAll("&iquest;", "¿");
        // UNCoding = UNCoding.replaceAll("&amp;", "&");
        // UNCoding = UNCoding.replaceAll("&ldquo;", "“");
        // UNCoding = UNCoding.replaceAll("&rdquo;", "”");
        // UNCoding = UNCoding.replaceAll("&mdash;", "——");
        // UNCoding = UNCoding.replaceAll(" ", "");
        UNCoding = UNCoding.replaceAll("\\*\\{.*\\}", "");
        return UNCoding;
    }

    /**
     * 全角转半角
     *
     * @param input String.
     * @return 半角字符串
     */
    public static String ToDBC(String input) {

        if ("".equals(input) || input == null) {
            return "";
        }
        char c[] = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == '\u3000') {
                c[i] = ' ';
            } else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
                c[i] = (char) (c[i] - 65248);
            }
        }
        String returnString = new String(c);
        return returnString;
    }

    /**
     * 判断是否是手机号
     *
     * @param phone 手机号码
     * @return
     */
    public static boolean isPhoneNum(String phone) {
        //^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$以前的是：^1[3|4|5|7|8][0-9]\\d{4,8}$
        if (phone.length() < 11) {
            return false;
        }
        Pattern p = Pattern.compile("^1[3|4|5|7|8][0-9]\\d{4,8}$");
        Matcher m = p.matcher(phone);
        if (m.matches()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断是否是电子邮箱
     *
     * @param email 电子邮箱
     * @return
     */
    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    private static final String REGEX_CHINESE = "^[\u4e00-\u9fa5],{0,}$";

    /**
     * 判断是否是中文
     *
     * @param chinese
     * @return
     */
    public static boolean isChinese(String chinese) {
        return Pattern.matches(REGEX_CHINESE, chinese);
    }


    /*
     * 判断String是否为null、""或者null字符串
     */
    public static boolean StringIsNull(String v) {
        boolean flag = true;
        if (v != null) {
            v = v.trim();
            if (!"".equals(v) && !"null".equals(v)) {
                flag = false;
            } else {
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 判断当前应用是否在前台运行
     *
     * @param activity
     * @return
     */
    public static boolean isRunningForeground(Activity activity) {
        String packageName = getPackageName(activity);
        String topActivityClassName = getTopActivityName(activity);
        System.out.println("packageName=" + packageName
                + ",topActivityClassName=" + topActivityClassName);
        if (packageName != null && topActivityClassName != null
                && topActivityClassName.startsWith(packageName)) {
            // System.out.println("---> 前台运行");
            return true;
        } else {
            // System.out.println("---> 后台运行");
            return false;
        }
    }

    public static String getTopActivityName(Context context) {
        String topActivityClassName = null;
        ActivityManager activityManager = (ActivityManager) (context
                .getSystemService(Context.ACTIVITY_SERVICE));
        List<RunningTaskInfo> runningTaskInfos = activityManager
                .getRunningTasks(1);
        if (runningTaskInfos != null) {
            ComponentName f = runningTaskInfos.get(0).topActivity;
            topActivityClassName = f.getClassName();
        }
        return topActivityClassName;
    }

    public static String getPackageName(Context context) {
        String packageName = context.getPackageName();
        return packageName;
    }

    /**
     * 时间戳转换成字符窜
     *
     * @param time
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String getDateToString(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(new Date(Long.parseLong(time + "000")));
        return date;
    }

    /**
     * 将字符串转为时间戳
     *
     * @param time
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static long getStringToDate(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        try {
            date = sdf.parse(time);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        LogHelper.i("时间转换成时间戳：" + date.getTime());
        return date.getTime();
    }

    /**
     * 获取当前系统时间
     *
     * @return yyyy-MM-dd HH:mm:ss
     */
    @SuppressLint("SimpleDateFormat")
    public static String getCurrentTimeDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        String str = formatter.format(curDate);
        LogHelper.i("当前系统时间：" + str);
        return str;
    }

//	/**
//	 *
//	 * <code>openFile</code>
//	 *
//	 * @description: TODO(打开附件)
//	 * @param context
//	 * @param file
//	 * @since 2014-8-6
//	 */
//	public static void openFile(Context context, File file) {
//		try {
//			Intent intent = new Intent();
//			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			// 设置intent的Action属性
//			intent.setAction(Intent.ACTION_VIEW);
//			// 获取文件file的MIME类型
//			String type = getMIMEType(file);
//			// 设置intent的data和Type属性。
//			intent.setDataAndType(/* uri */Uri.fromFile(file), type);
//			// 跳转
//			context.startActivity(intent);
//		} catch (ActivityNotFoundException e) {
//			// TODO: handle exception
//			Toast.makeText(context, "sorry附件不能打开，请下载相关软件！", 500).show();
//		}
//	}

    private static String getMIMEType(File file) {

        String type = "*/*";
        String fName = file.getName();
        // 获取后缀名前的分隔符"."在fName中的位置。
        int dotIndex = fName.lastIndexOf(".");
        if (dotIndex < 0) {
            return type;
        }
        /* 获取文件的后缀名 */
        String end = fName.substring(dotIndex, fName.length()).toLowerCase();
        if (end == "")
            return type;
        // 在MIME和文件类型的匹配表中找到对应的MIME类型。
        for (int i = 0; i < MIME_MapTable.length; i++) {

            if (end.equals(MIME_MapTable[i][0]))
                type = MIME_MapTable[i][1];
        }
        return type;
    }

    // 可以自己随意添加
    private static String[][] MIME_MapTable = {
            // {后缀名，MIME类型}
            {".3gp", "video/3gpp"},
            {".apk", "application/vnd.Android.package-archive"},
            {".asf", "video/x-ms-asf"},
            {".avi", "video/x-msvideo"},
            {".bin", "application/octet-stream"},
            {".bmp", "image/bmp"},
            {".c", "text/plain"},
            {".class", "application/octet-stream"},
            {".conf", "text/plain"},
            {".cpp", "text/plain"},
            {".doc", "application/msword"},
            {".docx",
                    "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
            {".xls", "application/vnd.ms-excel"},
            {".xlsx",
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
            {".exe", "application/octet-stream"},
            {".gif", "image/gif"},
            {".gtar", "application/x-gtar"},
            {".gz", "application/x-gzip"},
            {".h", "text/plain"},
            {".htm", "text/html"},
            {".html", "text/html"},
            {".jar", "application/java-archive"},
            {".java", "text/plain"},
            {".jpeg", "image/jpeg"},
            {".jpg", "image/jpeg"},
            {".js", "application/x-javascript"},
            {".log", "text/plain"},
            {".m3u", "audio/x-mpegurl"},
            {".m4a", "audio/mp4a-latm"},
            {".m4b", "audio/mp4a-latm"},
            {".m4p", "audio/mp4a-latm"},
            {".m4u", "video/vnd.mpegurl"},
            {".m4v", "video/x-m4v"},
            {".mov", "video/quicktime"},
            {".mp2", "audio/x-mpeg"},
            {".mp3", "audio/x-mpeg"},
            {".mp4", "video/mp4"},
            {".mpc", "application/vnd.mpohun.certificate"},
            {".mpe", "video/mpeg"},
            {".mpeg", "video/mpeg"},
            {".mpg", "video/mpeg"},
            {".mpg4", "video/mp4"},
            {".mpga", "audio/mpeg"},
            {".msg", "application/vnd.ms-outlook"},
            {".ogg", "audio/ogg"},
            {".pdf", "application/pdf"},
            {".png", "image/png"},
            {".pps", "application/vnd.ms-powerpoint"},
            {".ppt", "application/vnd.ms-powerpoint"},
            {".pptx",
                    "application/vnd.openxmlformats-officedocument.presentationml.presentation"},
            {".prop", "text/plain"}, {".rc", "text/plain"},
            {".rmvb", "audio/x-pn-realaudio"}, {".rtf", "application/rtf"},
            {".sh", "text/plain"}, {".tar", "application/x-tar"},
            {".tgz", "application/x-compressed"}, {".txt", "text/plain"},
            {".wav", "audio/x-wav"}, {".wma", "audio/x-ms-wma"},
            {".wmv", "audio/x-ms-wmv"},
            {".wps", "application/vnd.ms-works"}, {".xml", "text/plain"},
            {".z", "application/x-compress"},
            {".zip", "application/x-zip-compressed"}, {"", "*/*"}};

    /**
     * @Description:把数组转换为一个用逗号分隔的字符串 ，以便于用in+String 查询
     */
    public static String converToString(String[] ig) {
        String str = "";
        if (ig != null && ig.length > 0) {
            for (int i = 0; i < ig.length; i++) {
                str += ig[i] + ",";
            }
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }


    /**
     * 获得状态栏的高度
     *
     * @param context
     * @return
     */
    public static int getStatusHeight(Context context) {

        int statusHeight = -1;
        try {
            Class clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }


}
