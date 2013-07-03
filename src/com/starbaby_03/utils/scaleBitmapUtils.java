package com.starbaby_03.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.starbaby_03.R;
import com.starbaby_03.Gallery.AbenLog;
import com.starbaby_03.Gallery.MyApplication;

public class scaleBitmapUtils {
	/**
	 * 返回本地图片的缩略图
	 * 
	 * @param imagePath
	 *            SDCARD本地图片路径
	 * @return
	 */
	private static Context mContext;
	public static Bitmap getThumbnail(String imagePath) {
		Bitmap bitmap = null;
		try {
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(imagePath, opts);

			opts.inSampleSize = computeSampleSize(opts, -1, 320 * 320);
			opts.inJustDecodeBounds = false;

			bitmap = BitmapFactory.decodeFile(imagePath, opts);
			MyApplication.getInstance().getImageCache()
					.put(imagePath, bitmap);
		} catch (Exception ex) {
			AbenLog.e("tag", "get thumbnail ");
			bitmap = BitmapFactory.decodeResource(mContext.getResources(),
					R.drawable.icon);
		}
		return bitmap;
	}

	public static int computeSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);

		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}

		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 80 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}
}
