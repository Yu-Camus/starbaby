/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.starbaby_03.main;


import com.example.starbaby_03.BuildConfig;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

/**
 * 一个简单的子类{ @link ImageWorker },调整图像资源给定一个目标的宽度和高度。用于当输入图像可能太大,只加载到内存直接。
 */
public class ImageResizer extends ImageWorker {
	private static final String TAG = "ImageWorker";
	protected int mImageWidth;
	protected int mImageHeight;

	/**
	 *初始化提供一个单一的目标图像大小(用于宽度和高);
	 */
	public ImageResizer(Context context, int imageWidth, int imageHeight) {
		super(context);
		setImageSize(imageWidth, imageHeight);
	}

	/**
	初始化提供一个单一的目标图像大小(用于宽度和高);
	 */
	public ImageResizer(Context context, int imageSize) {
		super(context);
		setImageSize(imageSize);
	}

	/**
	 * 设置目标图像的宽度和高度。
	 */
	public void setImageSize(int width, int height) {
		mImageWidth = width;
		mImageHeight = height;
	}

	/**
	 设置目标图像大小(宽度和高度是相同的)。e
	 */
	public void setImageSize(int size) {
		setImageSize(size, size);
	}

	/**
	 *　主要的处理方法。这发生在一个后台任务。在这个案例我们只是抽样下位图并返回它从一个资源。
	 */
	private Bitmap processBitmap(int resId) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "processBitmap - " + resId);
		}
		return decodeSampledBitmapFromResource(mContext.getResources(), resId, mImageWidth, mImageHeight);
	}

	@Override
	protected Bitmap processBitmap(Object data) {
		return processBitmap(Integer.parseInt(String.valueOf(data)));
	}

	/**
	 *解码和样本下一位图从资源请求的宽度和高度。
	 * 
	 * @param res
	 *            资源对象,其中包含了图像数据
	 * @param resId
	 *            资源id的图像数据
	 * @param reqWidth
	 *            请求的宽度产生的位图
	 * @param reqHeight
	 *            请求的高度产生的位图
	 * @return 一个位图采样下来从原始与相同的方面比和密度大于或等于要求的宽度和高度
	 */
	public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {

		// 首先解码与inJustDecodeBounds = true来检查尺寸
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);

		// 计算inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		// 位图与inSampleSize解码设置
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, resId, options);
	}

	/**
	 * 解码和样本下一个位图文件的请求从宽度和高度。
	 * 
	 * @param filename
	 *            完整路径文件解码
	 * @param reqWidth
	 *            请求的宽度产生的位图
	 * @param reqHeight
	 *            请求的高度产生的位图
	 * @return 一个位图采样下来从原始与相同的方面比和密度大于或等于要求的宽度和高度
	 */
	public static synchronized Bitmap decodeSampledBitmapFromFile(String filename, int reqWidth, int reqHeight) {

		// 首先解码与inJustDecodeBounds = true来检查尺寸
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filename, options);

		// 计算inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		// 位图与inSampleSize解码设置
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(filename, options);
	}

	/**
	 * 　计算出一个inSampleSize用于{ @link BitmapFactory.Options } 对象当解码位图使用解码方法{ @link BitmapFactory }。
	 * 这个实现计算最接近的inSampleSize将导致最终解码有宽度的位图和高度等于或大于请求的宽度和高度。
	 * 这实现并不确保2的幂是inSampleSize返回可快些,但结果在一个更大的解码位图, 不是作为有用的缓存的目的。
	 * 
	 * @param options
	 *          一个选项与出params对象已经填充(运行通过解码方法与inJustDecodeBounds = = true
	 * @param reqWidth
	 *            请求的宽度产生的位图
	 * @param reqHeight
	 *            请求的高度产生的位图
	 * @return 这个值被用于inSampleSize
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// 原始图像的高度和宽度
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			if (width > height) {
				inSampleSize = Math.round((float) height / (float) reqHeight);
			} else {
				inSampleSize = Math.round((float) width / (float) reqWidth);
			}

//			这提供了一些额外的逻辑如果图像有一个奇怪的宽高比。
//			例如,一个全景可能更大宽度比高度。
//			在这些情况下总像素可能仍然结束太大,适合在内存中,所以我们应该更有侵略性与样本下图像(=更大inSampleSize)。

			final float totalPixels = width * height;

			// 任何超过2 x请求的像素我们将样品下来进一步。
			final float totalReqPixelsCap = reqWidth * reqHeight * 2;

			while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
				inSampleSize++;
			}
		}
		return inSampleSize;
	}
}
