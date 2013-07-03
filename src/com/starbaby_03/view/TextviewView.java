package com.starbaby_03.view;

import com.starbaby_03.utils.beautyUtils;

import android.R.color;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.provider.CalendarContract.Colors;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

public class TextviewView extends TextView{
	public Bitmap bit1;
	public Bitmap bit2;
	public String string;
	public TextviewView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	public void setText(String string){
		this.string=string;
	}
	public void setImg1(Bitmap bit1){
		this.bit1=bit1;
	}
	public void setImg2(Bitmap bit2){
		this.bit2=bit2;
	}
	protected void onDraw(Canvas canvas){
		Paint paint=new Paint();
		paint.setColor(0xffbc8e33);
		paint.setTextSize(25f);
		paint.setTextAlign(Align.CENTER);
		int w= beautyUtils.layoutWidth;
		canvas.drawText(string, 150, 40, paint);
		canvas.drawBitmap(bit1, 20, 15, null);
		canvas.drawBitmap(bit2, w-150, 15, null);
		canvas.save(Canvas.ALL_SAVE_FLAG);
		super.onDraw(canvas);
	}
}
