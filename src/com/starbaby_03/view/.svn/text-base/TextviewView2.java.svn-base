package com.starbaby_03.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.util.AttributeSet;
import android.widget.TextView;

public class TextviewView2 extends TextView{
	private String string;
	private Bitmap bitmap;
	public TextviewView2(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	public void setText(String string){
		this.string = string;
	}
	public void setBitmap(Bitmap bitmap){
		this.bitmap=bitmap;
	}
	protected void onDraw(Canvas canvas){
		Paint paint=new Paint();
		paint.setColor(0xffbc8e33);
		paint.setTextSize(25f);
		paint.setTextAlign(Align.CENTER);
		canvas.drawText(string,40,40, paint);
		canvas.drawBitmap(bitmap, 90, 10, null);
		canvas.save(Canvas.ALL_SAVE_FLAG);
		super.onDraw(canvas);
	}
}
