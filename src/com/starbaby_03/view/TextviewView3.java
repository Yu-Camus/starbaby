package com.starbaby_03.view;

import com.example.starbaby_03.R.color;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.util.AttributeSet;
import android.widget.TextView;

public class TextviewView3 extends TextView{
	private String head,info;
	public TextviewView3(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	public void setHead(String head){
		this.head=head;
	}
//	public void setInfo(String info){
//		this.info=info;
//	}
	protected void onDraw(Canvas canvas){
		Paint paint = new Paint();
		paint.setColor(0xffbc8e33);
		paint.setTextAlign(Align.CENTER);
		paint.setTextSize(25f);
		Paint paint2 = new Paint();
		paint2.setColor(0xfff7d383);
		paint2.setTextSize(2);
		Paint paint3=new Paint();
		paint3.setTextSize(25f);
		canvas.drawText(head, 60,  40, paint);
		canvas.drawLine(120, 0, 120, 60, paint2);
//		canvas.drawText(info,130, canvas.getHeight()*2/3, paint3);
		canvas.save(Canvas.ALL_SAVE_FLAG);
		super.onDraw(canvas);
	}
}
