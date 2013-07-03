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
 * 缓存
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
	private final int maxCacheItemSize = 64; // 默认最大缓存数为64
	private long maxCacheByteSize = 1024 * 1024 * 5; // 5MB default 最大缓存 所占内存为5M
	private CompressFormat mCompressFormat = CompressFormat.JPEG;
	private int mCompressQuality = 70;

	private final Map<String, String> mLinkedHashMap = Collections.synchronizedMap(new LinkedHashMap<String, String>(INITIAL_CAPACITY,
			LOAD_FACTOR, true));

	/**
	 * A filename filter to use to identify the cache filenames which have
	 * CACHE_FILENAME_PREFIX prepended.
	 * 一个文件名过滤器用来标识缓存文件名，那些哟缓存文件名的。
	 */
	private static final FilenameFilter cacheFileFilter = new FilenameFilter() {
		@Override
		public boolean accept(File dir, String filename) {
			return filename.startsWith(CACHE_FILENAME_PREFIX);
		}
	};

	/**
	 * 用于获取DiskLruCache的一个实例。
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
	 * 构造函数,不应该直接调用,应该
	 * {@link DiskLruCache#openCache(Context, File, long)}
	 * 运行一些额外的检查DiskLruCache之前创建一个实例。
	 */
	private DiskLruCache(File cacheDir, long maxByteSize) {
		mCacheDir = cacheDir;
		maxCacheByteSize = maxByteSize;
	}

	/**
	 * 添加一个位图到磁盘缓存。
	 * 一个独特的标识符为位图。
	 * 位图存储。
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
	 * 刷新缓存，移除那些缓存大小超过规定的最大缓存值
	 * 如果图片和key老是改变。就不会remove（经常使用）
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
	 * 得到一个图像从磁盘缓存
	 * 
	 * @param key
	 *            位图的唯一标识符
	 * @return 位图或null如果没有找到
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
	 * 检查是否一个特定的键存在于缓存中
	 * 
	 * @param key
	 *            位图的唯一标识符
	 * @return true 存在, false otherwise
	 */
	public boolean containsKey(String key) {
		// 看key是否在我们的HashMap
		if (mLinkedHashMap.containsKey(key)) {
			return true;
		}

		// 现在检查如果有一个实际的文件存在基于key
		final String existingFile = createFilePath(mCacheDir, key);
		if (new File(existingFile).exists()) {
			// 找到的文件,将它添加到HashMap,以供将来使用
			put(key, existingFile);
			return true;
		}
		return false;
	}

	/**
	 * 删除所有磁盘缓存条目从这个实例的缓存目录
	 */
	public void clearCache() {
		DiskLruCache.clearCache(mCacheDir);
	}

	/**
	 * 删除所有磁盘缓存条目从应用程序缓存目录中的uniqueName子目录。
	 * 
	 * @param context
	 *            The context to use
	 * @param uniqueName
	 *           一个独特的缓存目录名称附加到应用程序缓存目录
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
	 *            目录删除缓存文件
	 */
	private static void clearCache(File cacheDir) {
		final File[] files = cacheDir.listFiles(cacheFileFilter);
		for (int i = 0; i < files.length; i++) {
			files[i].delete();
		}
	}

	/**
	 * 得到一个可用的缓存目录(外部如果可用,内部其他)。
	 * @param context
	 *            The context to use
	 * @param uniqueName
	 *            一个独特的目录名称添加到缓存目录
	 * @return The cache dir
	 */
	public static File getDiskCacheDir(Context context, String uniqueName) {

		//检查是否安装或存储媒体是内置的,如果是这样,试着使用外部缓存目录 否则使用内部缓存目录
		final String cachePath = Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED || !Utils.isExternalStorageRemovable() ? Utils
				.getExternalCacheDir(context).getPath() : context.getCacheDir().getPath();

		return new File(cachePath + File.separator + uniqueName);
	}

	/**
	 * 创建一个常数缓存文件路径缓存目录给出一个目标和一个图像KEY
	 * 
	 * @param cacheDir
	 * @param key
	 * @return
	 */
	public static String createFilePath(File cacheDir, String key) {
		try {
			//使用URLEncoder确保我们有一个有效的文件名,　　但它能为这个EXAMPLE
			return cacheDir.getAbsolutePath() + File.separator + CACHE_FILENAME_PREFIX + URLEncoder.encode(key.replace("*", ""), "UTF-8");
		} catch (final UnsupportedEncodingException e) {
			Log.e(TAG, "createFilePath - " + e);
		}

		return null;
	}

	/**
	 * 　创建一个常数缓存文件路径使用当前的缓存目录, 一个图像关键。
	 * @param key
	 * @return
	 */
	public String createFilePath(String key) {
		return createFilePath(mCacheDir, key);
	}

	/**
	 * 　集目标压缩格式和质量图像写入磁盘缓存
	 * @param compressFormat
	 * @param quality
	 */
	public void setCompressParams(CompressFormat compressFormat, int quality) {
		mCompressFormat = compressFormat;
		mCompressQuality = quality;
	}

	/**
	 * 输入图片到目录文件
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
