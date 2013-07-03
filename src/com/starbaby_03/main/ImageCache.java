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
import android.support.v4.app.FragmentActivity;
import android.support.v4.util.LruCache;
import android.util.Log;


import java.io.File;

import com.example.starbaby_03.BuildConfig;


/**
 * This class holds our bitmap caches (memory and disk).
 */
public class ImageCache {
    private static final String TAG = "ImageCache";

    //默认内存缓存大小
    private static final int DEFAULT_MEM_CACHE_SIZE = 1024 * 1024 * 5; // 5MB

    // 默认的磁盘缓存大小
    private static final int DEFAULT_DISK_CACHE_SIZE = 1024 * 1024 * 10; // 10MB

    // 当编写图像压缩设置磁盘高速缓存
    private static final CompressFormat DEFAULT_COMPRESS_FORMAT = CompressFormat.JPEG;
    private static final int DEFAULT_COMPRESS_QUALITY = 70;

    // 常量轻松切换不同的缓存
    private static final boolean DEFAULT_MEM_CACHE_ENABLED = true;
    private static final boolean DEFAULT_DISK_CACHE_ENABLED = true;
    private static final boolean DEFAULT_CLEAR_DISK_CACHE_ON_START = false;

    private DiskLruCache mDiskCache;
    private LruCache<String, Bitmap> mMemoryCache;

    /**
     *创建一个新的ImageCache对象使用指定的参数。
     * @param context The context to use
     * @param cacheParams缓存参数使用初始化缓存
     */
    public ImageCache(Context context, ImageCacheParams cacheParams) {
        init(context, cacheParams);
    }

    /**
     * 创建一个新的ImageCache对象使用默认参数
     *
     * @param context The context to use
     * @param 一个唯一的名称,uniqueName将附加到缓存目录
     */
    public ImageCache(Context context, String uniqueName) {
        init(context, new ImageCacheParams(uniqueName));
    }

    /**
     * Find and return an existing ImageCache stored in a {@link RetainFragment}, if not found a new
     * one is created with defaults and saved to a {@link RetainFragment}.
     *
     * @param activity The calling {@link FragmentActivity}
     * @param uniqueName A unique name to append to the cache directory
     * @return An existing retained ImageCache object or a new one if one did not exist.
     */
    public static ImageCache findOrCreateCache(
            final FragmentActivity activity, final String uniqueName) {
        return findOrCreateCache(activity, new ImageCacheParams(uniqueName));
    }

    /**
     * Find and return an existing ImageCache stored in a {@link RetainFragment}, if not found a new
     * one is created using the supplied params and saved to a {@link RetainFragment}.
     *
     * @param activity The calling {@link FragmentActivity}
     * @param cacheParams The cache parameters to use if creating the ImageCache
     * @return An existing retained ImageCache object or a new one if one did not exist
     */
    public static ImageCache findOrCreateCache(
            final FragmentActivity activity, ImageCacheParams cacheParams) {

        // 搜索,或创建一个实例的非ui RetainFragment
        final RetainFragment mRetainFragment = RetainFragment.findOrCreateRetainFragment(
                activity.getSupportFragmentManager());

        // 看看我们已经有了一个RetainFragment ImageCache存储在
        ImageCache imageCache = (ImageCache) mRetainFragment.getObject();

        //没有现有的ImageCache,创建一个,并将它存储在RetainFragment
        if (imageCache == null) {
            imageCache = new ImageCache(activity, cacheParams);
            mRetainFragment.setObject(imageCache);
        }

        return imageCache;
    }

    /**
     * Initialize the cache, providing all parameters.
     *
     * @param context The context to use
     * @param cacheParams The cache parameters to initialize the cache
     */
    private void init(Context context, ImageCacheParams cacheParams) {
        final File diskCacheDir = DiskLruCache.getDiskCacheDir(context, cacheParams.uniqueName);

        // 设置磁盘缓存
        if (cacheParams.diskCacheEnabled) {
            mDiskCache = DiskLruCache.openCache(context, diskCacheDir, cacheParams.diskCacheSize);
            mDiskCache.setCompressParams(cacheParams.compressFormat, cacheParams.compressQuality);
            if (cacheParams.clearDiskCacheOnStart) {
                mDiskCache.clearCache();
            }
        }

        // 设置内存缓存
        if (cacheParams.memoryCacheEnabled) {
            mMemoryCache = new LruCache<String, Bitmap>(cacheParams.memCacheSize) {
                /**
                 * 测量项目大小字节而不是单位为位图更实用缓存
                 */
                @Override
                protected int sizeOf(String key, Bitmap bitmap) {
                    return Utils.getBitmapSize(bitmap);
                }
            };
        }
    }

    public void addBitmapToCache(String data, Bitmap bitmap) {
        if (data == null || bitmap == null) {
            return;
        }

        // Add to memory cache
        if (mMemoryCache != null && mMemoryCache.get(data) == null) {
            mMemoryCache.put(data, bitmap);
        }

        // Add to disk cache
        if (mDiskCache != null && !mDiskCache.containsKey(data)) {
            mDiskCache.put(data, bitmap);
        }
    }

    /**
     * Get from memory cache.
     *
     * @param data Unique identifier for which item to get
     * @return The bitmap if found in cache, null otherwise
     */
    public Bitmap getBitmapFromMemCache(String data) {
        if (mMemoryCache != null) {
            final Bitmap memBitmap = mMemoryCache.get(data);
            if (memBitmap != null) {
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "Memory cache hit");
                }
                return memBitmap;
            }
        }
        return null;
    }

    /**
     * Get from disk cache.
     *
     * @param data Unique identifier for which item to get
     * @return The bitmap if found in cache, null otherwise
     */
    public Bitmap getBitmapFromDiskCache(String data) {
        if (mDiskCache != null) {
            return mDiskCache.get(data);
        }
        return null;
    }

    public void clearCaches() {
        mDiskCache.clearCache();
        mMemoryCache.evictAll();
    }

    /**
     * 一个类,该类包含缓存参数。
     */
    public static class ImageCacheParams {
        public String uniqueName;
        public int memCacheSize = DEFAULT_MEM_CACHE_SIZE;
        public int diskCacheSize = DEFAULT_DISK_CACHE_SIZE;
        public CompressFormat compressFormat = DEFAULT_COMPRESS_FORMAT;
        public int compressQuality = DEFAULT_COMPRESS_QUALITY;
        public boolean memoryCacheEnabled = DEFAULT_MEM_CACHE_ENABLED;
        public boolean diskCacheEnabled = DEFAULT_DISK_CACHE_ENABLED;
        public boolean clearDiskCacheOnStart = DEFAULT_CLEAR_DISK_CACHE_ON_START;

        public ImageCacheParams(String uniqueName) {
            this.uniqueName = uniqueName;
        }
    }
}
