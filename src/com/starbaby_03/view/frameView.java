package com.starbaby_03.view;

import com.example.starbaby_03.R;
import com.starbaby_03.beautify.addFrame;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;



public class frameView extends ImageView{


	private Context cont;
	private WindowManager windowManager;
	public static int width, height;
	public static int w, h;
	public static int scaleheigth;
	public static int drawheight;
	public static Bitmap mOriginalBitmap = null;
	public static int[] listOfFrame = {
			R.drawable.f011024x768,
			R.drawable.f051024x768, R.drawable.f081024x768,
			R.drawable.f091024x768, R.drawable.f1001024x768,
			R.drawable.f1011024x768, R.drawable.f1021024x768,
			R.drawable.f221024x768
			};


	// private ArrayList<Integer> points;

	public frameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.cont = context;
		// TODO Auto-generated constructor stub
	}

	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		windowManager = (WindowManager) this.cont
				.getSystemService(Context.WINDOW_SERVICE);
		width = windowManager.getDefaultDisplay().getWidth();
		height = windowManager.getDefaultDisplay().getHeight();

		BitmapFactory.Options options = new BitmapFactory.Options();
		// Load image from resource
		mOriginalBitmap = BitmapFactory.decodeResource(getResources(),
				listOfFrame[addFrame.framePos], options);
//		mOriginalBitmap=BitmapFactory.decodeResource(getResources(),R.drawable.back_and_gold_frame,options);
		// Scale to target size
		int iW = mOriginalBitmap.getWidth();
		int iH = mOriginalBitmap.getHeight();
		Log.v("iW", iW + "");
		Log.v("iH", iH + "");

		if (width <= iW) {
			w = width;
		} else {
			w = iW;
		}
		if (height <= iH) {
			h = height;
		} else {
			h = iH;
		}
		scaleheigth=height-168;
//		drawheight=(height-200)/6;
		mOriginalBitmap = Bitmap
				.createScaledBitmap(mOriginalBitmap, windowManager.getDefaultDisplay().getWidth(),scaleheigth, true);
		Log.v("scaleheigth", scaleheigth + "");
		Log.v("drawheight", drawheight + "");
		canvas.drawBitmap(mOriginalBitmap, 0, 0, null);
//
		final int[] pixels = new int[w * scaleheigth];
		mOriginalBitmap.getPixels(pixels, 0, w, 0, 0, w, scaleheigth);


	}
}
