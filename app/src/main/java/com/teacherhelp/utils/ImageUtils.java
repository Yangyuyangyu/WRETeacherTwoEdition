/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.teacherhelp.utils;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageUtils {
	public static String getThumbnailImagePath(String imagePath) {
		String path = imagePath.substring(0, imagePath.lastIndexOf("/") + 1);
		path += "th"
				+ imagePath.substring(imagePath.lastIndexOf("/") + 1,
						imagePath.length());
//		EMLog.d("msg", "original image path:" + imagePath);
//		EMLog.d("msg", "thum image path:" + path);
		return path;
	}

	public static String bitmaptoString(Bitmap bitmap) {
		String string = null;
		// 将Bitmap转换成字符串
		try {

			ByteArrayOutputStream bStream = new ByteArrayOutputStream();

			bitmap.compress(CompressFormat.JPEG, 100, bStream);
			bStream.flush();// 将bos流缓存在内存中的数据全部输出，清空缓存
			bStream.close();
			byte[] bytes = bStream.toByteArray();

			string = Base64.encodeToString(bytes, Base64.DEFAULT);
			bitmap.recycle();
			bitmap = null;
			bytes = null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
 		return string;

	}

	public static String getImageAbsolutePath(Context context, Uri imageUri) {
		if (context == null || imageUri == null)
			return null;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
				&& DocumentsContract.isDocumentUri(context, imageUri)) {
			if (isExternalStorageDocument(imageUri)) {
				String docId = DocumentsContract.getDocumentId(imageUri);
				String[] split = docId.split(":");
				String type = split[0];
				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/"
							+ split[1];
				}
			} else if (isDownloadsDocument(imageUri)) {
				String id = DocumentsContract.getDocumentId(imageUri);
				Uri contentUri = ContentUris.withAppendedId(
						Uri.parse("content://downloads/public_downloads"),
						Long.valueOf(id));
				return getDataColumn(context, contentUri, null, null);
			} else if (isMediaDocument(imageUri)) {
				String docId = DocumentsContract.getDocumentId(imageUri);
				String[] split = docId.split(":");
				String type = split[0];
				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}
				String selection = MediaStore.Images.Media._ID + "=?";
				String[] selectionArgs = new String[] { split[1] };
				return getDataColumn(context, contentUri, selection,
						selectionArgs);
			}
		} // MediaStore (and general)
		else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
			// Return the remote address
			if (isGooglePhotosUri(imageUri))
				return imageUri.getLastPathSegment();
			return getDataColumn(context, imageUri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
			return imageUri.getPath();
		}
		return null;
	}

	public static String getDataColumn(Context context, Uri uri,
			String selection, String[] selectionArgs) {
		Cursor cursor = null;
		String column = MediaStore.Images.Media.DATA;
		String[] projection = { column };
		try {
			cursor = context.getContentResolver().query(uri, projection,
					selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				int index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

	/**
	 * 注册第三步照片处理
	 * @param imagePath 图片路径
	 * @param width 您想要的图片的宽度
	 * @param height 您想要的图片的高度
	 * @return
	 */
	public static Bitmap GetMyBitmap(String imagePath, int width, int height) {
		Bitmap mybitmap = MyImageSLPic(imagePath, width,
				height);
		mybitmap =MycompressImage(mybitmap, getPhotopathcompress());
		int degree = getBitmapDegree(imagePath);
		mybitmap = rotateBitmapByDegree(mybitmap, degree);
		return mybitmap;
	}

	/**
	 * 获取缩略图图片
	 * 
	 * @param imagePath
	 *            图片的路径
	 * @param width
	 *            图片的宽度
	 * @param height
	 *            图片的高度
	 * @return 缩略图图片
	 */
	public static Bitmap MyImageSLPic(String imagePath, int width,
			int height) {
		Bitmap bitmap = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		options.inPreferredConfig=Config.RGB_565;
		options.inPurgeable=true;
		// 获取这个图片的宽和高，注意此处的bitmap为null
		bitmap = BitmapFactory.decodeFile(imagePath, options);
		options.inJustDecodeBounds = false; // 设为 false
		// 计算缩放比
		int h = options.outHeight;
		int w = options.outWidth;
		int beWidth=0;
		int beHeight=0;
		if(w<h){
			Log.e("whm", "获取到的图片的宽是："+w+";    高是："+h);
			beWidth = w / 800;
			beHeight = h / 1000;
			Log.e("whm", "计算出来的beWidth是:"+beWidth+";    beHeight是："+beHeight);
		}else{
			Log.e("whm", "获取到的图片的宽是："+w+";    高是："+h);
			beWidth = w / width;
			beHeight = h / height;
			Log.e("whm", "计算出来的beWidth是:"+beWidth+";    beHeight是："+beHeight);
		}
		
		int be = 1;
		if (beWidth < beHeight) {
			be = beWidth;
		} else {
			be = beHeight;
		}
		if (be <= 0) {
			be = 1;
		}
		Log.e("whm", "最后得到的缩放比是："+be);
		options.inSampleSize = be;
		// 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
		bitmap = BitmapFactory.decodeFile(imagePath, options);
		// 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
		if(w<h){
			bitmap = ThumbnailUtils.extractThumbnail(bitmap, 800, 1000,
					ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		}else{
			bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
					ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		}
		try {
			File file = new File(getPhotopathSLPic());
			FileOutputStream fos = new FileOutputStream(file);
			bitmap.compress(CompressFormat.JPEG, 100, fos);//将文件保存到新地址中
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bitmap;
	}
	
	
	/**
	 * 质量压缩
	 * @param image
	 * @return
	 */
	public static Bitmap MycompressImage(Bitmap image,String newPath) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while ( baos.toByteArray().length / 1024>200) {	//循环判断如果压缩后图片是否大于100kb,大于继续压缩		
		     if(options<0)
		    	 options=100;
			baos.reset();//重置baos即清空baos
			image.compress(CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;//每次都减少10
		}
		try {
			File file = new File(newPath);
			FileOutputStream fos = new FileOutputStream(file);
			image.compress(CompressFormat.JPEG, 100, fos);//将文件保存到新地址中
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
		 BitmapFactory.Options option = new BitmapFactory.Options();
		 option.inPreferredConfig=Config.RGB_565;
	     option.inPurgeable=true;
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, option);//把ByteArrayInputStream数据生成图片
		return bitmap;
	}
	
	
	private static String getPhotopathSLPic() {
		// 照片全路径
		String fileName = "";
		// 文件夹路径
		String pathUrl = Environment.getExternalStorageDirectory() + "/credit/";
		String imageName = "imageThumbnail.jpg";
		File file = new File(pathUrl);
		file.mkdirs();// 创建文件夹
		fileName = pathUrl + imageName;
		return fileName;
	}
	
	private static String getPhotopathcompress() {
		// 照片全路径
		String fileName = "";
		// 文件夹路径
		String pathUrl = Environment.getExternalStorageDirectory() + "/credit/";
		String imageName = "imagecompress.jpg";
		File file = new File(pathUrl);
		file.mkdirs();// 创建文件夹
		fileName = pathUrl + imageName;
		return fileName;
	}
	
	/**
	 * 读取图片的旋转的角度
	 * 
	 * @param path
	 *            图片绝对路径
	 * @return 图片的旋转角度
	 */
	private static int getBitmapDegree(String path) {
		int degree = 0;
		try {
			// 从指定路径下读取图片，并获取其EXIF信息
			ExifInterface exifInterface = new ExifInterface(path);
			// 获取图片的旋转信息
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	/**
	 * 将图片按照某个角度进行旋转
	 * 
	 * @param bm
	 *            需要旋转的图片
	 * @param degree
	 *            旋转角度
	 * @return 旋转后的图片
	 */
	public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
		Bitmap returnBm = null;

		// 根据旋转角度，生成旋转矩阵
		Matrix matrix = new Matrix();
		matrix.postRotate(degree);
		System.out.println("degree=" + degree);
		try {
			// 将原始图片按照旋转矩阵进行旋转，并得到新的图片
			returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
					bm.getHeight(), matrix, true);
		} catch (OutOfMemoryError e) {
		}
		if (returnBm == null) {
			returnBm = bm;
		}
		if (bm != returnBm) {
			bm.recycle();
		}
		return returnBm;
	}
	
	
	
	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is Google Photos.
	 */
	public static boolean isGooglePhotosUri(Uri uri) {
		return "com.google.android.apps.photos.content".equals(uri
				.getAuthority());
	}
	
//
//	/**
//	 * 显示略缩图
//	 * @param CRUID
//	 * @return
//	 */
//	public static void setUserHeadImageSmall(String CRUID,ImageView view){
//		String url=HttpPath.ImageUrl+CRUID+"_small.png";
//		MyApplication.getInstance().displayHeadImage(url, view, R.drawable.morentouxiang, new SimpleImageLoadingListener(){
//			@Override
//			public void onLoadingStarted(String imageUri, View view) {
//				// TODO Auto-generated method stub
//				DiskCacheUtils.removeFromCache(imageUri,  MyApplication.getInstance().imageLoader.getDiskCache());
//		    	MemoryCacheUtils.removeFromCache(imageUri, MyApplication.getInstance().imageLoader.getMemoryCache());
//			}
//		});
//
//	}

	
	
//	/**
//	 * 显示高清头像
//	 * @param CRUID
//	 * @return
//	 */
//	public static void setUserHeadImageHD(String CRUID,ImageView view){
//		String url=HttpPath.ImageUrl+CRUID+".png";
//		MyApplication.getInstance().displayHeadImage(url, view, R.drawable.morentouxiang, new SimpleImageLoadingListener(){
//			@Override
//			public void onLoadingStarted(String imageUri, View view) {
//				// TODO Auto-generated method stub
//				DiskCacheUtils.removeFromCache(imageUri,  MyApplication.getInstance().imageLoader.getDiskCache());
//		    	MemoryCacheUtils.removeFromCache(imageUri, MyApplication.getInstance().imageLoader.getMemoryCache());
//			}
//		});
//
//	}
//	/**
//	 * 得到高清头像地址
//	 * @param CRUID
//	 * @return
//	 */
//	public static String getUserHeadImageHDlUrl(String CRUID){
//		return HttpPath.ImageUrl+CRUID+".png";
//	}
//	/**
//	 * 得到略缩图形地址
//	 * @param CRUID
//	 * @return
//	 */
//	public static String getUserHeadImageSmallUrl(String CRUID){
//		return HttpPath.ImageUrl+CRUID+"_small.png";
//	}
	/**
	 * 根据路径得到Bitmap
	 */
	public static Bitmap getBitmapFile(String path){
		try {
			InputStream in=new FileInputStream(path);
		    BitmapFactory.Options option = new BitmapFactory.Options();
			option.inPreferredConfig=Config.RGB_565;
		    option.inPurgeable=true;
		    option.inJustDecodeBounds = true;
		   //根据
			Bitmap defaultIcon = BitmapFactory.decodeStream(in,null,option);
		    option.inSampleSize = caculateInSampleSize(option, 480, 800);
		    option.inJustDecodeBounds = false;
		    
		    InputStream in2=new FileInputStream(path);
			return BitmapFactory.decodeStream(in2,null,option);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
//	  /**
//     * 压缩指定路径的图片，并得到图片对象
//     * @param context
//     * @param path bitmap source path
//     * @return Bitmap 
//     */
//    public final static Bitmap compressBitmap(String path, int rqsW, int rqsH) {
//        final BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//        BitmapFactory.decodeFile(path, options);
//        options.inSampleSize = caculateInSampleSize(options, rqsW, rqsH);
//        options.inJustDecodeBounds = false;
//        return BitmapFactory.decodeFile(path, options);
//    }
    /**
     * 得到缩放比
     * @param
     * @return
     */
    public final static int caculateInSampleSize(BitmapFactory.Options options, int rqsW, int rqsH) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (rqsW == 0 || rqsH == 0) return 1;
        if (height > rqsH || width > rqsW) {
            final int heightRatio = Math.round((float) height/ (float) rqsH);
            final int widthRatio = Math.round((float) width / (float) rqsW);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }
    
    //上传图片转换方法
    public static String getImag(String path){
    	String base64=null;
    	Bitmap bitmap1 =ImageUtils.getBitmapFile(path);//根据图片路径得到本地图片
    	if(bitmap1!=null){
    		//如果得到Bitmap对象 进行质量压缩
    		Bitmap bm = null;
    		bm = ImageUtility.compressImage(bitmap1,"");
    		bitmap1.recycle();
    		bitmap1=null;
    		if(bm!=null){
    			//如果压缩成功 进行Base64转码
    			base64=ImageUtils.bitmaptoString(bm);
    			bm.recycle();
    			bm=null;
    		}
    	}
    	return base64;
    }
}
