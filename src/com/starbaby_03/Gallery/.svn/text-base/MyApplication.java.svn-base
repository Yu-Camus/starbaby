package com.starbaby_03.Gallery;

import android.app.Application;

public class MyApplication extends Application {

	private static MyApplication instance;
	private ImageCache mImageCache;

	private MyApplication() {
		mImageCache = new ImageCache();
	}

	// 单例模式中获取唯一的MyApplication实例
	public static MyApplication getInstance() {
		if (null == instance) {
			instance = new MyApplication();
		}
		return instance;

	}

	public ImageCache getImageCache() {
		return mImageCache;
	}
}
