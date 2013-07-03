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

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

import com.example.starbaby_03.BuildConfig;


/**
 * ������װ���������һЩ���ⳤʱ�����еĹ���������һ��λͼ��ImageView��
 * �����Դ�������ʹ���ڴ�ʹ��̻���,���й�����һ����̨�̺߳�����һ��ռλ��ͼ��
 */
public abstract class ImageWorker {
	private static final String TAG = "ImageWorker";
	private static final int FADE_IN_TIME = 200;

	private ImageCache mImageCache;
	private Bitmap mLoadingBitmap;
	private boolean mFadeInBitmap = true;
	private boolean mExitTasksEarly = false;

	protected Context mContext;
	protected ImageWorkerAdapter mImageWorkerAdapter;

	protected ImageWorker(Context context) {
		mContext = context;
	}

	/**
	 * Load an image specified by the data parameter into an ImageView (override
	 * {@link ImageWorker#processBitmap(Object)} to define the processing
	 * logic). A memory and disk cache will be used if an {@link ImageCache} has
	 * been set using {@link ImageWorker#setImageCache(ImageCache)}. If the
	 * image is found in the memory cache, it is set immediately, otherwise an
	 * {@link AsyncTask} will be created to asynchronously load the bitmap.
	 * 
	 * @param data
	 *           ͼ���URL���ء�
	 * @param imageView
	 *            ���󶨵�ImageView����ͼ��
	 */
	public void loadImage(Object data, ImageView imageView) {
		Bitmap bitmap = null;

		if (mImageCache != null) {
			bitmap = mImageCache.getBitmapFromMemCache(String.valueOf(data));
		}

		if (bitmap != null) {
			//λͼ�ڴ滺�����ҵ�
			imageView.setImageBitmap(bitmap);
		} else if (cancelPotentialWork(data, imageView)) {
			final BitmapWorkerTask task = new BitmapWorkerTask(imageView);
			final AsyncDrawable asyncDrawable = new AsyncDrawable(mContext.getResources(), mLoadingBitmap, task);
			imageView.setImageDrawable(asyncDrawable);
			task.execute(data);
		}
	}

	/**
	 * Load an image specified from a set adapter into an ImageView (override
	 * {@link ImageWorker#processBitmap(Object)} to define the processing
	 * logic). A memory and disk cache will be used if an {@link ImageCache} has
	 * been set using {@link ImageWorker#setImageCache(ImageCache)}. If the
	 * image is found in the memory cache, it is set immediately, otherwise an
	 * {@link AsyncTask} will be created to asynchronously load the bitmap.
	 * {@link ImageWorker#setAdapter(ImageWorkerAdapter)} must be called before
	 * using this method.
	 * 
	 * @param data
	 *            ͼ���URL���ء�
	 * @param imageView
	 *            ���󶨵�ImageView����ͼ��
	 */
	public void loadImage(int num, ImageView imageView) {
		if (mImageWorkerAdapter != null) {
			loadImage(mImageWorkerAdapter.getItem(num), imageView);
		} else {
			throw new NullPointerException("Data not set, must call setAdapter() first.");
		}
	}

	/**
	 * ����ռλ��λͼ��ʾ����̨�߳����С�
	 * 
	 * @param bitmap
	 */
	public void setLoadingImage(Bitmap bitmap) {
		mLoadingBitmap = bitmap;
	}

	/**
	 * ����ռλ��λͼ��ʾ����̨�߳����С�
	 * 
	 * @param resId
	 */
	public void setLoadingImage(int resId) {
		mLoadingBitmap = BitmapFactory.decodeResource(mContext.getResources(), resId);
	}

	/**
	 * Set the {@link ImageCache} object to use with this ImageWorker.
	 * 
	 * @param cacheCallback
	 */
	public void setImageCache(ImageCache cacheCallback) {
		mImageCache = cacheCallback;
	}

	public ImageCache getImageCache() {
		return mImageCache;
	}

	/**
	 * ���������Ϊ��,ͼ��ᵭ����غ�ĺ�̨�̡߳�
	 * 
	 * @param fadeIn
	 */
	public void setImageFadeIn(boolean fadeIn) {
		mFadeInBitmap = fadeIn;
	}

	public void setExitTasksEarly(boolean exitTasksEarly) {
		mExitTasksEarly = exitTasksEarly;
	}

	/**
	 * Subclasses should override this to define any processing or work that
	 * must happen to produce the final bitmap. This will be executed in a
	 * background thread and be long running. For example, you could resize a
	 * large bitmap here, or pull down an image from the network.
	 * 
	 * @param data
	 *            The data to identify which image to process, as provided by
	 *            {@link ImageWorker#loadImage(Object, ImageView)}
	 * @return �������λͼ
	 */
	protected abstract Bitmap processBitmap(Object data);

	public static void cancelWork(ImageView imageView) {
		final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
		if (bitmapWorkerTask != null) {
			bitmapWorkerTask.cancel(true);
			if (BuildConfig.DEBUG) {
				final Object bitmapData = bitmapWorkerTask.data;
				Log.d(TAG, "cancelWork - cancelled work for " + bitmapData);
			}
		}
	}

	/**
	 * Returns true if the current work has been canceled or if there was no
	 * work in progress on this image view. Returns false if the work in
	 * progress deals with the same data. The work is not stopped in that case.
	 */
	public static boolean cancelPotentialWork(Object data, ImageView imageView) {
		final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

		if (bitmapWorkerTask != null) {
			final Object bitmapData = bitmapWorkerTask.data;
			if (bitmapData == null || !bitmapData.equals(data)) {
				bitmapWorkerTask.cancel(true);
				if (BuildConfig.DEBUG) {
					Log.d(TAG, "cancelPotentialWork - cancelled work for " + data);
				}
			} else {
				// ͬ���Ĺ����Ѿ��ڽ�����
				return false;
			}
		}
		return true;
	}

	/**
	 * @param imageView
	 *            Any imageView
	 * @return ��������ǰ���������(����еĻ�)��ϵ��һ����imageView�������û������������
	 */
	private static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
		if (imageView != null) {
			final Drawable drawable = imageView.getDrawable();
			if (drawable instanceof AsyncDrawable) {
				final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
				return asyncDrawable.getBitmapWorkerTask();
			}
		}
		return null;
	}

	/**
	 * ʵ�ʵ�AsyncTask,�첽����ͼ��
	 */
	private class BitmapWorkerTask extends AsyncTask<Object, Void, Bitmap> {
		private Object data;
		private final WeakReference<ImageView> imageViewReference;

		public BitmapWorkerTask(ImageView imageView) {
			imageViewReference = new WeakReference<ImageView>(imageView);
		}

		/**
		 * �����������̨����

		 */
		@Override
		protected Bitmap doInBackground(Object... params) {
			data = params[0];
			final String dataString = String.valueOf(data);
			Bitmap bitmap = null;

			//���ͼ�񻺴��ǿ��õĺ��������û��ȡ������һ���̺߳�ImageView,����ǰ󶨵����������Ȼ�ǰ���һ���������,���ǵġ��˳����ڡ�����û������Ȼ�����Ż�ȡλͼ����
			if (mImageCache != null && !isCancelled() && getAttachedImageView() != null && !mExitTasksEarly) {
				bitmap = mImageCache.getBitmapFromDiskCache(dataString);
			}

			//�����λͼ������û���ҵ�,�������û�б�ȡ������һ���̺߳�ImageView,����Ǳ�Ȼ���������Ȼ�ǰ󶨻��������,���ǵġ��˳����ڡ�������û������, Ȼ����������̷���(ʵ�ֵ�һ������)
			if (bitmap == null && !isCancelled() && getAttachedImageView() != null && !mExitTasksEarly) {
				bitmap = processBitmap(params[0]);
			}

			// ���λͼ�����ͼ�񻺴����,Ȼ����Ӽӹ�λͼ�������Ա�����ʹ�á���ע�����ǲ��������ȡ����������,�����,�߳���������,���ǲ�����Ӽӹ�λͼ�����ǵĻ���,��Ϊ�����ܻ��ڽ����ٴ�ʹ��
			if (bitmap != null && mImageCache != null) {
				mImageCache.addBitmapToCache(dataString, bitmap);
			}

			return bitmap;
		}

		/**
		 * һ��ͼ��Ĵ���,Ա����imageView
		 */
		@Override
		protected void onPostExecute(Bitmap bitmap) {
			// ���ȡ���Ǻ������������˳����ڡ���־������Ȼ��������
			if (isCancelled() || mExitTasksEarly) {
				bitmap = null;
			}

			final ImageView imageView = getAttachedImageView();
			if (bitmap != null && imageView != null) {
				setImageBitmap(imageView, bitmap);
			}
		}

		/**
		 * ���������ص�����ImageViewֻҪImageView��������Ȼָ���������һ��������null����
		 */
		private ImageView getAttachedImageView() {
			final ImageView imageView = imageViewReference.get();
			final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

			if (this == bitmapWorkerTask) {
				return imageView;
			}

			return null;
		}
	}

	/**
	 * ��һ���Զ���Ļ�ͼ,��������imageView�������ڽ���������һ���ο�ʵ�ʹ����ߵ�����,���Կ���ֹͣ���һ���µİ��Ǳ����,��ȷ��ֻ�����ʼ�������̿��԰�����,������ɶ�����
	 */
	private static class AsyncDrawable extends BitmapDrawable {
		private final WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;

		public AsyncDrawable(Resources res, Bitmap bitmap, BitmapWorkerTask bitmapWorkerTask) {
			super(res, bitmap);

			bitmapWorkerTaskReference = new WeakReference<BitmapWorkerTask>(bitmapWorkerTask);
		}

		public BitmapWorkerTask getBitmapWorkerTask() {
			return bitmapWorkerTaskReference.get();
		}
	}

	/**
	 * ʱ���ô�����ɺ����յ�λͼӦ��������ImageView��
	 * 
	 * @param imageView
	 * @param bitmap
	 */
	private void setImageBitmap(ImageView imageView, Bitmap bitmap) {
		if (mFadeInBitmap) {
			// ������һ��͸����drwabale�����Ĺ���,���λͼ
			final TransitionDrawable td = new TransitionDrawable(new Drawable[] { new ColorDrawable(android.R.color.transparent),
					new BitmapDrawable(mContext.getResources(), bitmap) });
			// ���ñ���λͼ����
			imageView.setBackgroundDrawable(new BitmapDrawable(mContext.getResources(), mLoadingBitmap));

			imageView.setImageDrawable(td);
			td.startTransition(FADE_IN_TIME);
		} else {
			imageView.setImageBitmap(bitmap);
		}
	}

	/**
	 * ���ü򵥵�������,������Щ�������ݡ�
	 * 
	 * @param adapter
	 */
	public void setAdapter(ImageWorkerAdapter adapter) {
		mImageWorkerAdapter = adapter;
	}

	/**
	 * ��ȡ��ǰ����������
	 * 
	 * @return
	 */
	public ImageWorkerAdapter getAdapter() {
		return mImageWorkerAdapter;
	}

	/**
	 * һ���ǳ��򵥵�������ʹ��ImageWorker������ࡣ
	 */
	public static abstract class ImageWorkerAdapter {
		public abstract Object getItem(int num);

		public abstract int getSize();
	}
}
