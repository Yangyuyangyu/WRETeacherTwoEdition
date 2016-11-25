package com.teacherhelp.utils;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;

/**
 * 拍照工具类 用与APP中头像的修改
 * 
 * @author 小男孩
 * 
 */
@SuppressLint("SimpleDateFormat")
public class CameraUtils {
	// 使用照相机拍照获取图片
	public static final int CAMERA_UTILS_TACK_PHOTO = 20001;
	// 使用相册中的图片
	public static final int CAMERA_UTILS_PICK_PHOTO = 20002;
	// 截取图片
	public static final int CAMERA_UTILS_INTERCEPT = 20003;
	// 相机拍照保存路径
	public static final String Camera_Path = Environment
			.getExternalStorageDirectory() + "/WR_EducationApp/imageloader/";
	public static final String IMG_NAME = "head.jpg";
	public static final String IMG_NAME_1="head_1.jpg";
	private File image;
	
	
	
    public File getImage() {
		return image;
	}

	public void setImage(File image) {
		this.image = image;
	}

	/**
     * 得到调用系统截图的intent
     * @param uri
     * @param outputX 输出的图片宽度
     * @param outputY 输出的图片高度
     * @return
     */
	public static Intent cropImageUri(Uri uri, int outputX, int outputY,Uri new_uri) {

		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");

		intent.putExtra("crop", "true");

		intent.putExtra("aspectX", 1);

		intent.putExtra("aspectY", 1);

		intent.putExtra("outputX", outputX);

		intent.putExtra("outputY", outputY);
		 
		intent.putExtra("scale", true);
		
		intent.putExtra("scaleUpIfNeeded", true);

		intent.putExtra("return-data", false);
		
		intent.putExtra(MediaStore.EXTRA_OUTPUT, new_uri);

	    intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

	   intent.putExtra("noFaceDetection", true); // no face detection


		return intent;
	}

	/**
	 * 得到调用系统相机intent
	 * 
	 * @return
	 */
	public  Intent get_OpenCarmIntent(boolean isFront) {
		// TODO Auto-generated method stub
		String SDState = Environment.getExternalStorageState();
		if (SDState.equals(Environment.MEDIA_MOUNTED)) {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// "android.media.action.IMAGE_CAPTURE"
			File dir = new File(Camera_Path);
			if (!dir.exists())
				dir.mkdirs();
			image = new File(dir,IMG_NAME_1);
			if (image.exists()) {
				image.delete();
			}
			Uri photoUri = Uri.fromFile(image);
			if(isFront)
				intent.putExtra("camerasensortype", 2); // 调用前置摄像头 
			intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
			return intent;
		}
		return null;

	}
	
	/**
	 * 得到调用系统相机intent
	 * 
	 * @return
	 */
	public  Intent get_OpenCarmIntent() {
		// TODO Auto-generated method stub
		String SDState = Environment.getExternalStorageState();
		if (SDState.equals(Environment.MEDIA_MOUNTED)) {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// "android.media.action.IMAGE_CAPTURE"
			File dir = new File(Camera_Path);
			if (!dir.exists())
				dir.mkdirs();
			image = new File(dir,IMG_NAME_1);
			if (image.exists()) {
				image.delete();
			}
			Uri photoUri = Uri.fromFile(image);
			intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
			return intent;
		}
		return null;

	}

	/**
	 * 得到调用系统相册的Intent
	 */
	public Intent get_Pick_Photo() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		return intent;
	}

}
