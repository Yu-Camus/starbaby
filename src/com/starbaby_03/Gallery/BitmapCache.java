package com.starbaby_03.Gallery;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import android.graphics.Bitmap;

/***
 * ͼƬ����
 * 
 * (����ģʽ)
 * 
 * @author zhangjia
 * 
 */
public class BitmapCache {

	static private BitmapCache cache;
	// ������
	private HashMap<String, SoftReference<Bitmap>> imageCache;

	public BitmapCache() {
		imageCache = new HashMap<String, SoftReference<Bitmap>>();
	}

	/**
	 * ȡ�û�����ʵ��
	 */
	public static BitmapCache getInstance() {
		if (cache == null) {
			cache = new BitmapCache();
		}
		return cache;

	}

	/***
	 * ��ȡ����ͼƬ
	 * 
	 * @param key
	 *            image name
	 * @return
	 */
	public Bitmap getBitmap(String key) {
		if (imageCache.containsKey(key)) {
			SoftReference<Bitmap> reference = imageCache.get(key);
			Bitmap bitmap = reference.get();
			if (bitmap != null)
				return bitmap;
		}
		return null;

	}

	/***
	 * ��ͼƬ��ӵ���������
	 * 
	 * @param bitmap
	 * @param key
	 */
	public void putSoftReference(Bitmap bitmap, String key) {
		imageCache.put(key, new SoftReference<Bitmap>(bitmap));

	}

}
