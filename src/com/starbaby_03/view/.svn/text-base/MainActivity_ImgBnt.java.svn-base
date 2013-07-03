package com.starbaby_03.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
//import android.graphics.Paint.Align;
import android.util.AttributeSet;
import android.widget.ImageButton;

public class MainActivity_ImgBnt extends ImageButton{
	private String _text = "";
    private int _color = 0;
    private float _textsize = 0f;
	public MainActivity_ImgBnt(Context context,AttributeSet attrs) {
		super(context,attrs);
		// TODO Auto-generated constructor stub
	}
	public void setText(String text)
	{
		this._text=text;
	}
	public void setColor(int color)
	{
		this._color=color;
		invalidate();
	}
	public void setTextsize(float textsize)
	{
		this._textsize=textsize;
	}
	public void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		Paint paint=new Paint();
		paint.setTextAlign(Align.CENTER);
		paint.setColor(_color);
		paint.setTextSize(_textsize);
//		canvas.drawText(_text,  canvas.getWidth()/2, canvas.getHeight()*7/8, paint);
		canvas.drawText(_text,  41,68, paint);
	}
}
