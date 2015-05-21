package com.haoge.luanru.music.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory.Options;

public class BitmapUtil {
	
	/**
	 * 保存图片
	 * 
	 * @param targetFile
	 * @param bitmap
	 * @throws IOException
	 */
	public static void save(File targetFile, Bitmap bitmap) throws IOException {
		if (!targetFile.getParentFile().exists()) {
			targetFile.getParentFile().mkdirs();
		}
		if (!targetFile.exists()) {
			FileOutputStream fos = new FileOutputStream(targetFile);
			bitmap.compress(CompressFormat.JPEG, 100, fos);
			fos.flush();
			fos.close();
		}
	}

	public static Bitmap loadBitmap(String path, int width, int height) {
		Options opt = new Options();
		// 仅仅加载边界属性
		opt.inJustDecodeBounds = true;
		// 解析数据源
		BitmapFactory.decodeFile(path, opt);
		// 获取图片原本的width height
		int w = opt.outWidth / width;
		int h = opt.outHeight / height;
		int scale = w > h ? w : h;
		opt.inSampleSize = scale;
		// 按照opt的参数设置,解析Bitmap
		opt.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(path, opt);
	}

	/***
	 * 从byte数组中读取一张图片
	 * 
	 * @param bytes
	 *            数据源
	 * @param width
	 *            目标图片的宽度
	 * @param height
	 *            目标图片的高度
	 * @return
	 */
	public static Bitmap loadBitmap(byte[] bytes, int width, int height) {
		Options opt = new Options();
		// 仅仅加载边界属性
		opt.inJustDecodeBounds = true;
		// 解析数据源
		BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opt);
		// 获取图片原本的width height
		int w = opt.outWidth / width;
		int h = opt.outHeight / height;
		int scale = w > h ? w : h;
		opt.inSampleSize = scale;
		// 按照opt的参数设置,解析Bitmap
		opt.inJustDecodeBounds = false;
		Bitmap bit= BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opt);
		return bit;
	}

	/***
	 * 从byte数组中读取一张图片
	 * 
	 * @param bytes
	 *            数据源
	 * @param width
	 *            目标图片的宽度
	 * @param height
	 *            目标图片的高度
	 * @return
	 */
	public static Bitmap loadBitmap(byte[] bytes) {
		return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
	}
}
