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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.example.starbaby_03.BuildConfig;


/**
 * һ���򵥵�����{ @link ImageResizer },��ȡ������ͼ���һ��URL��ȡ��
 */
public class ImageFetcher extends ImageResizer {
    private static final String TAG = "ImageFetcher";
    private static final int HTTP_CACHE_SIZE = 10 * 1024 * 1024; // 10MB
    public static final String HTTP_CACHE_DIR = "http";

    /**
     * ��ʼ���ṩһ��Ŀ��ͼ��Ŀ�Ⱥ͸߶ȶԴ���ͼ��
     *
     */
    public ImageFetcher(Context context, int imageWidth, int imageHeight) {
        super(context, imageWidth, imageHeight);
        init(context);
    }

    /**
     *��ʼ���ṩһ����һ��Ŀ��ͼ���С(���ڿ�Ⱥ͸߶�);
     *
     */
    public ImageFetcher(Context context, int imageSize) {
        super(context, imageSize);
        init(context);
    }

    private void init(Context context) {
        checkConnection(context);
    }

    /**
     * �򵥵��������Ӽ�顣
     *
     * @param context
     */
    private void checkConnection(Context context) {
        final ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnectedOrConnecting()) {
            Toast.makeText(context, "No network connection found.", Toast.LENGTH_LONG).show();
            Log.e(TAG, "checkConnection - no connection found");
        }
    }

    /**
     *��Ҫ�Ĺ��շ���,������ImageWorker AsyncTask�ı����̡߳�
     * @param �������ݼ���λͼ,�����������,һ����ͨ��http URL
     * @return ���ز�����λͼ
     */
    private Bitmap processBitmap(String data) {
        if (BuildConfig.DEBUG) {
        }

        // ����һ��λͼ,����д��һ���ļ�
        final File f = downloadBitmap(mContext, data);

        if (f != null) {
            // ����һ�����������汾
            return decodeSampledBitmapFromFile(f.toString(), mImageWidth, mImageHeight);
        }

        return null;
    }

    @Override
    protected Bitmap processBitmap(Object data) {
        return processBitmap(String.valueOf(data));
    }

    /**
  		��һ��λͼ��һ��URL,����д����̺ͷ����ļ�ָ�롣��ʵ��ʹ��һ���򵥵Ĵ��̻��档	
     */
    public static File downloadBitmap(Context context, String urlString) {
        final File cacheDir = DiskLruCache.getDiskCacheDir(context, HTTP_CACHE_DIR);

        final DiskLruCache cache =
                DiskLruCache.openCache(context, cacheDir, HTTP_CACHE_SIZE);

        final File cacheFile = new File(cache.createFilePath(urlString));

        if (cache.containsKey(urlString)) {
            if (BuildConfig.DEBUG) {
            }
            return cacheFile;
        }

        if (BuildConfig.DEBUG) {
        }

        Utils.disableConnectionReuseIfNecessary();
        HttpURLConnection urlConnection = null;
        BufferedOutputStream out = null;

        try {
            final URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            final InputStream in =
                    new BufferedInputStream(urlConnection.getInputStream(), Utils.IO_BUFFER_SIZE);
            out = new BufferedOutputStream(new FileOutputStream(cacheFile), Utils.IO_BUFFER_SIZE);

            int b;
            while ((b = in.read()) != -1) {
                out.write(b);
            }

            return cacheFile;

        } catch (final IOException e) {
            Log.e(TAG, "Error in downloadBitmap - " + e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (out != null) {
                try {
                    out.close();
                } catch (final IOException e) {
                    Log.e(TAG, "Error in downloadBitmap - " + e);
                }
            }
        }

        return null;
    }
}
