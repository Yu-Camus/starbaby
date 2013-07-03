package com.starbaby_03.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.util.AttributeSet;
import android.widget.EditText;

public class edittextView extends EditText {
	private String string;

	public edittextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public void setText(String string) {
		this.string = string;
	}

	protected void onDraw(Canvas canvas) {
		Paint paint = new Paint();
		paint.setColor(0xffbc8e33);
		paint.setTextAlign(Align.CENTER);
		paint.setTextSize(25f);
		canvas.drawText(string, 40, 40, paint);
		super.onDraw(canvas);
	}
}
