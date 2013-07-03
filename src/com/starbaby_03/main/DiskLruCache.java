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
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.example.starbaby_03.BuildConfig;


/**
 * ����
 */
public class DiskLruCache {
	private static final String TAG = "DiskLruCache";
	private static final String CACHE_FILENAME_PREFIX = "cache_";
	private static final int MAX_REMOVALS = 4;
	private static final int INITIAL_CAPACITY = 32;
	private static final float LOAD_FACTOR = 0.75f;

	private final File mCacheDir;
	private int cacheSize = 0;
	private int cacheByteSize = 0;
	private final int maxCacheItemSize = 64; // Ĭ����󻺴���Ϊ64
	private long maxCacheByteSize = 1024 * 1024 * 5; // 5MB default ��󻺴� ��ռ�ڴ�Ϊ5M
	private CompressFormat mCompressFormat = CompressFormat.JPEG;
	private int mCompressQuality = 70;

	private final Map<String, String> mLinkedHashMap = Collections.synchronizedMap(new LinkedHashMap<String, String>(INITIAL_CAPACITY,
			LOAD_FACTOR, true));

	/**
	 * A filename filter to use to identify the cache filenames which have
	 * CACHE_FILENAME_PREFIX prepended.
	 * һ���ļ���������������ʶ�����ļ�������ЩӴ�����ļ����ġ�
	 */
	private static final FilenameFilter cacheFileFilter = new FilenameFilter() {
		@Override
		public boolean accept(File dir, String filename) {
			return filename.startsWith(CACHE_FILENAME_PREFIX);
		}
	};

	/**
	 * ���ڻ�ȡDiskLruCache��һ��ʵ����
	 */
	public static DiskLruCache openCache(Context context, File cacheDir, long maxByteSize) {
		if (!cacheDir.exists()) {
			cacheDir.mkdir();
		}

		if (cacheDir.isDirectory() && cacheDir.canWrite() && Utils.getUsableSpace(cacheDir) > maxByteSize) {
			return new DiskLruCache(cacheDir, maxByteSize);
		}

		return null;
	}

	/**
	 * ���캯��,��Ӧ��ֱ�ӵ���,Ӧ��
	 * {@link DiskLruCache#openCache(Context, File, long)}
	 * ����һЩ����ļ��DiskLruCache֮ǰ����һ��ʵ����
	 */
	private DiskLruCache(File cacheDir, long maxByteSize) {
		mCacheDir = cacheDir;
		maxCacheByteSize = maxByteSize;
	}

	/**
	 * ���һ��λͼ�����̻��档
	 * һ�����صı�ʶ��Ϊλͼ��
	 * λͼ�洢��
	 */
	public void put(String key, Bitmap data) {
		synchronized (mLinkedHashMap) {
			if (mLinkedHashMap.get(key) == null) {
				try {
					final String file = createFilePath(mCacheDir, key);
					if (writeBitmapToFile(data, file)) {
						put(key, file);
						flushCache();
					}
				} catch (final FileNotFoundException e) {
					Log.e(TAG, "Error in put: " + e.getMessage());
				} catch (final IOException e) {
					Log.e(TAG, "Error in put: " + e.getMessage());
				}
			}
		}
	}

	private void put(String key, String file) {
		mLinkedHashMap.put(key, file);
		cacheSize = mLinkedHashMap.size();
		cacheByteSize += new File(file).length();
	}

	/**
	 * ˢ�»��棬�Ƴ���Щ�����С�����涨����󻺴�ֵ
	 * ���ͼƬ��key���Ǹı䡣�Ͳ���remove������ʹ�ã�
	 */
	private void flushCache() {
		Entry<String, String> eldestEntry;
		File eldestFile;
		long eldestFileSize;
		int count = 0;

		while (count < MAX_REMOVALS && (cacheSize > maxCacheItemSize || cacheByteSize > maxCacheByteSize)) {
			eldestEntry = mLinkedHashMap.entrySet().iterator().next();
			eldestFile = new File(eldestEntry.getValue());
			eldestFileSize = eldestFile.length();
			mLinkedHashMap.remove(eldestEntry.getKey());
			eldestFile.delete();
			cacheSize = mLinkedHashMap.size();
			cacheByteSize -= eldestFileSize;
			count++;
			if (BuildConfig.DEBUG) {
				Log.d(TAG, "flushCache - Removed cache file, " + eldestFile + ", " + eldestFileSize);
			}
		}
	}

	/**
	 * �õ�һ��ͼ��Ӵ��̻���
	 * 
	 * @param key
	 *            λͼ��Ψһ��ʶ��
	 * @return λͼ��null���û���ҵ�
	 */
	public Bitmap get(String key) {
		synchronized (mLinkedHashMap) {
			final String file = mLinkedHashMap.get(key);
			if (file != null) {
				if (BuildConfig.DEBUG) {
					Log.d(TAG, "Disk cache hit");
				}
				return BitmapFactory.decodeFile(file);
			} else {
				final String existingFile = createFilePath(mCacheDir, key);
				if (new File(existingFile).exists()) {
					put(key, existingFile);
					if (BuildConfig.DEBUG) {
						Log.d(TAG, "Disk cache hit (existing file)");
					}
					return BitmapFactory.decodeFile(existingFile);
				}
			}
			return null;
		}
	}

	/**
	 * ����Ƿ�һ���ض��ļ������ڻ�����
	 * 
	 * @param key
	 *            λͼ��Ψһ��ʶ��
	 * @return true ����, false otherwise
	 */
	public boolean containsKey(String key) {
		// ��key�Ƿ������ǵ�HashMap
		if (mLinkedHashMap.containsKey(key)) {
			return true;
		}

		// ���ڼ�������һ��ʵ�ʵ��ļ����ڻ���key
		final String existingFile = createFilePath(mCacheDir, key);
		if (new File(existingFile).exists()) {
			// �ҵ����ļ�,������ӵ�HashMap,�Թ�����ʹ��
			put(key, existingFile);
			return true;
		}
		return false;
	}

	/**
	 * ɾ�����д��̻�����Ŀ�����ʵ���Ļ���Ŀ¼
	 */
	public void clearCache() {
		DiskLruCache.clearCache(mCacheDir);
	}

	/**
	 * ɾ�����д��̻�����Ŀ��Ӧ�ó��򻺴�Ŀ¼�е�uniqueName��Ŀ¼��
	 * 
	 * @param context
	 *            The context to use
	 * @param uniqueName
	 *           һ�����صĻ���Ŀ¼���Ƹ��ӵ�Ӧ�ó��򻺴�Ŀ¼
	 */
	public static void clearCache(Context context, String uniqueName) {
		File cacheDir = getDiskCacheDir(context, uniqueName);
		clearCache(cacheDir);
	}

	/**
	 * Removes all disk cache entries from the given directory. This should not
	 * be called directly, call {@link DiskLruCache#clearCache(Context, String)}
	 * or {@link DiskLruCache#clearCache()} instead.
	 * 
	 * @param cacheDir
	 *            Ŀ¼ɾ�������ļ�
	 */
	private static void clearCache(File cacheDir) {
		final File[] files = cacheDir.listFiles(cacheFileFilter);
		for (int i = 0; i < files.length; i++) {
			files[i].delete();
		}
	}

	/**
	 * �õ�һ�����õĻ���Ŀ¼(�ⲿ�������,�ڲ�����)��
	 * @param context
	 *            The context to use
	 * @param uniqueName
	 *            һ�����ص�Ŀ¼������ӵ�����Ŀ¼
	 * @return The cache dir
	 */
	public static File getDiskCacheDir(Context context, String uniqueName) {

		//����Ƿ�װ��洢ý�������õ�,���������,����ʹ���ⲿ����Ŀ¼ ����ʹ���ڲ�����Ŀ¼
		final String cachePath = Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED || !Utils.isExternalStorageRemovable() ? Utils
				.getExternalCacheDir(context).getPath() : context.getCacheDir().getPath();

		return new File(cachePath + File.separator + uniqueName);
	}

	/**
	 * ����һ�����������ļ�·������Ŀ¼����һ��Ŀ���һ��ͼ��KEY
	 * 
	 * @param cacheDir
	 * @param key
	 * @return
	 */
	public static String createFilePath(File cacheDir, String key) {
		try {
			//ʹ��URLEncoderȷ��������һ����Ч���ļ���,����������Ϊ���EXAMPLE
			return cacheDir.getAbsolutePath() + File.separator + CACHE_FILENAME_PREFIX + URLEncoder.encode(key.replace("*", ""), "UTF-8");
		} catch (final UnsupportedEncodingException e) {
			Log.e(TAG, "createFilePath - " + e);
		}

		return null;
	}

	/**
	 * ������һ�����������ļ�·��ʹ�õ�ǰ�Ļ���Ŀ¼, һ��ͼ��ؼ���
	 * @param key
	 * @return
	 */
	public String createFilePath(String key) {
		return createFilePath(mCacheDir, key);
	}

	/**
	 * ����Ŀ��ѹ����ʽ������ͼ��д����̻���
	 * @param compressFormat
	 * @param quality
	 */
	public void setCompressParams(CompressFormat compressFormat, int quality) {
		mCompressFormat = compressFormat;
		mCompressQuality = quality;
	}

	/**
	 * ����ͼƬ��Ŀ¼�ļ�
	 * @param bitmap
	 * @param file
	 * @return
	 */
	private boolean writeBitmapToFile(Bitmap bitmap, String file) throws IOException, FileNotFoundException {

		OutputStream out = null;
		try {
			out = new BufferedOutputStream(new FileOutputStream(file), Utils.IO_BUFFER_SIZE);
			return bitmap.compress(mCompressFormat, mCompressQuality, out);
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}
}
