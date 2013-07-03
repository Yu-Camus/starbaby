package com.starbaby_03.view;

import java.io.File;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.starbaby_03.R;
import com.starbaby_03.beautify.addFrame;

public class addWordsView extends ImageView{

	private Context cont;
	private WindowManager windowManager;
	private int width, height;
	public static int w, h;
	public static Bitmap mOriginalBitmap = null;


	// private ArrayList<Integer> points;

	public addWordsView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.cont = context;
		// TODO Auto-generated constructor stub
	}

	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		windowManager = (WindowManager) this.cont
				.getSystemService(Context.WINDOW_SERVICE);
		width = windowManager.getDefaultDisplay().getWidth();
		height = windowManager.getDefaultDisplay().getHeight()-180;

		BitmapFactory.Options options = new BitmapFactory.Options();
		// Load image from resource
//		mOriginalBitmap = BitmapFactory.decodeResource(getResources(),
//				listOfFrame[addFrame.framePos], options);
//		mOriginalBitmap=BitmapFactory.decodeResource(getResources(),R.drawable.back_and_gold_frame,options);
		// Scale to target size
		File file=new File("/sdcard/ll1x/"+"starbaby"+".jpg");
		mOriginalBitmap=BitmapFactory.decodeFile(file.toString());
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
		
		mOriginalBitmap = Bitmap
				.createScaledBitmap(mOriginalBitmap, w, h, true);

		canvas.drawBitmap(mOriginalBitmap, 0, 0, null);
//
		final int[] pixels = new int[w * h];
		mOriginalBitmap.getPixels(pixels, 0, w, 0, 0, w, h);


	}
}
