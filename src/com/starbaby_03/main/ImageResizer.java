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
 * һ���򵥵�����{ @link ImageWorker },����ͼ����Դ����һ��Ŀ��Ŀ�Ⱥ͸߶ȡ����ڵ�����ͼ�����̫��,ֻ���ص��ڴ�ֱ�ӡ�
 */
public class ImageResizer extends ImageWorker {
	private static final String TAG = "ImageWorker";
	protected int mImageWidth;
	protected int mImageHeight;

	/**
	 *��ʼ���ṩһ����һ��Ŀ��ͼ���С(���ڿ�Ⱥ͸�);
	 */
	public ImageResizer(Context context, int imageWidth, int imageHeight) {
		super(context);
		setImageSize(imageWidth, imageHeight);
	}

	/**
	��ʼ���ṩһ����һ��Ŀ��ͼ���С(���ڿ�Ⱥ͸�);
	 */
	public ImageResizer(Context context, int imageSize) {
		super(context);
		setImageSize(imageSize);
	}

	/**
	 * ����Ŀ��ͼ��Ŀ�Ⱥ͸߶ȡ�
	 */
	public void setImageSize(int width, int height) {
		mImageWidth = width;
		mImageHeight = height;
	}

	/**
	 ����Ŀ��ͼ���С(��Ⱥ͸߶�����ͬ��)��e
	 */
	public void setImageSize(int size) {
		setImageSize(size, size);
	}

	/**
	 *����Ҫ�Ĵ��������ⷢ����һ����̨�����������������ֻ�ǳ�����λͼ����������һ����Դ��
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
	 *�����������һλͼ����Դ����Ŀ�Ⱥ͸߶ȡ�
	 * 
	 * @param res
	 *            ��Դ����,���а�����ͼ������
	 * @param resId
	 *            ��Դid��ͼ������
	 * @param reqWidth
	 *            ����Ŀ�Ȳ�����λͼ
	 * @param reqHeight
	 *            ����ĸ߶Ȳ�����λͼ
	 * @return һ��λͼ����������ԭʼ����ͬ�ķ���Ⱥ��ܶȴ��ڻ����Ҫ��Ŀ�Ⱥ͸߶�
	 */
	public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {

		// ���Ƚ�����inJustDecodeBounds = true�����ߴ�
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);

		// ����inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		// λͼ��inSampleSize��������
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, resId, options);
	}

	/**
	 * �����������һ��λͼ�ļ�������ӿ�Ⱥ͸߶ȡ�
	 * 
	 * @param filename
	 *            ����·���ļ�����
	 * @param reqWidth
	 *            ����Ŀ�Ȳ�����λͼ
	 * @param reqHeight
	 *            ����ĸ߶Ȳ�����λͼ
	 * @return һ��λͼ����������ԭʼ����ͬ�ķ���Ⱥ��ܶȴ��ڻ����Ҫ��Ŀ�Ⱥ͸߶�
	 */
	public static synchronized Bitmap decodeSampledBitmapFromFile(String filename, int reqWidth, int reqHeight) {

		// ���Ƚ�����inJustDecodeBounds = true�����ߴ�
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filename, options);

		// ����inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		// λͼ��inSampleSize��������
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(filename, options);
	}

	/**
	 * �������һ��inSampleSize����{ @link BitmapFactory.Options } ���󵱽���λͼʹ�ý��뷽��{ @link BitmapFactory }��
	 * ���ʵ�ּ�����ӽ���inSampleSize���������ս����п�ȵ�λͼ�͸߶ȵ��ڻ��������Ŀ�Ⱥ͸߶ȡ�
	 * ��ʵ�ֲ���ȷ��2������inSampleSize���ؿɿ�Щ,�������һ������Ľ���λͼ, ������Ϊ���õĻ����Ŀ�ġ�
	 * 
	 * @param options
	 *          һ��ѡ�����params�����Ѿ����(����ͨ�����뷽����inJustDecodeBounds = = true
	 * @param reqWidth
	 *            ����Ŀ�Ȳ�����λͼ
	 * @param reqHeight
	 *            ����ĸ߶Ȳ�����λͼ
	 * @return ���ֵ������inSampleSize
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// ԭʼͼ��ĸ߶ȺͿ��
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			if (width > height) {
				inSampleSize = Math.round((float) height / (float) reqHeight);
			} else {
				inSampleSize = Math.round((float) width / (float) reqWidth);
			}

//			���ṩ��һЩ������߼����ͼ����һ����ֵĿ�߱ȡ�
//			����,һ��ȫ�����ܸ����ȱȸ߶ȡ�
//			����Щ����������ؿ�����Ȼ����̫��,�ʺ����ڴ���,��������Ӧ�ø�����������������ͼ��(=����inSampleSize)��

			final float totalPixels = width * height;

			// �κγ���2 x������������ǽ���Ʒ������һ����
			final float totalReqPixelsCap = reqWidth * reqHeight * 2;

			while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
				inSampleSize++;
			}
		}
		return inSampleSize;
	}
}
