package com.starbaby_03.beautify;


import com.example.starbaby_03.R;
import com.starbaby_03.utils.Utils;
import com.starbaby_03.view.MainActivity_ImgBnt;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.location.GpsStatus.Listener;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
/*
 * 添加文字View
 */
public class addWords_01 extends Activity implements OnClickListener,OnTouchListener{
	
	private MainActivity_ImgBnt iBnt1,iBnt2;
	private TextView tv1;
	private EditText ed1;
	private String str=null;
	private int Intent_Flag=2;
	private int imgBnt1_textcolor = Color.WHITE;
	private int imgBnt2_textcolor=Color.GRAY;
	private float imgBnt_textsize=24f;
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.beautify_addwords_01);
		init();
		Listener();
	}
	private void Listener() {
		// TODO Auto-generated method stub
		iBnt1.setOnClickListener(this);
		iBnt1.setOnTouchListener(this);
		iBnt2.setOnClickListener(this);
		str=ed1.getText().toString();
		iBnt2.setClickable(false);
		//监听edittext内是否有文字输入。逻辑判断
			ed1.addTextChangedListener(new TextWatcher() {
				
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub
					if("".equals(ed1.getText().toString().trim()))
					{
						iBnt2.setClickable(false);
						iBnt2.setBackgroundResource(R.drawable.ensuer_lock);
						iBnt2.setOnTouchListener(new OnTouchListener() {
							
							@Override
							public boolean onTouch(View v, MotionEvent event) {
								// TODO Auto-generated method stub
								if(event.getAction() == MotionEvent.ACTION_DOWN){
									v.setBackgroundResource(R.drawable.ensuer_in);
								}
								else if(event.getAction() == MotionEvent.ACTION_UP){
									v.setBackgroundResource(R.drawable.ensuer_over);
								}
								return false;
							}
						});
					}else {
						iBnt2.setClickable(true);
						iBnt2.setBackgroundResource(R.drawable.ensuer_over);
						iBnt2.setOnTouchListener(new OnTouchListener() {
							
							@Override
							public boolean onTouch(View v, MotionEvent event) {
								// TODO Auto-generated method stub
								if(event.getAction() == MotionEvent.ACTION_DOWN){
									v.setBackgroundResource(R.drawable.ensuer_in);
								}
								else if(event.getAction() == MotionEvent.ACTION_UP){
									v.setBackgroundResource(R.drawable.ensuer_over);
								}
								return false;
							}
						});
					}
				}
			});
		
	}
	void init()
	{
		iBnt1=(MainActivity_ImgBnt) findViewById(R.id.addword_01_ib1);
		iBnt2=(MainActivity_ImgBnt) findViewById(R.id.addword_01_ib2);
		tv1=(TextView) findViewById(R.id.addword_01_info);
		ed1=(EditText) findViewById(R.id.addword_01_editText1);
		//设置最多输入10个字
		ed1.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)}); 
	}
	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId())
		{
		//返回操作
		case R.id.addword_01_ib1:
			Intent intent=new Intent(addWords_01.this,addWords.class);
			intent.putExtra(Utils.INTENT_FLAG, Utils.NO_WORDS);
			startActivity(intent);
			this.finish();
			break;
		//完成操作
		case R.id.addword_01_ib2:
			str=ed1.getText().toString();
			Intent intent2=new Intent(addWords_01.this,addWords.class);
			intent2.putExtra("Words", str);
			intent2.putExtra(Utils.INTENT_FLAG, Utils.ADD_WORDS);
			startActivity(intent2);
			this.finish();
			break;
		default:
			break;
		}
	}
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case R.id.addword_01_ib1:
			if(event.getAction() == MotionEvent.ACTION_DOWN){
				v.setBackgroundResource(R.drawable.back_in);
				Log.e("Down", "Down");
			}
			else if(event.getAction() == MotionEvent.ACTION_UP){
				v.setBackgroundResource(R.drawable.back_over);
				Log.e("Up", "Up");
			}
			break;
		default:
				break;
		}
		return false;
	}
}
