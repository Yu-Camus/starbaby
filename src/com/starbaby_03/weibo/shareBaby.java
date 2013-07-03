package com.starbaby_03.weibo;

import com.example.starbaby_03.R;
import com.starbaby_03.Gallery.mapShowOrginal;
import com.starbaby_03.Gallery.mapStorage;
import com.starbaby_03.utils.weiboUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class shareBaby extends Activity implements OnClickListener,
		OnTouchListener {
	private ImageButton imgBnt1, imgBnt2;
	private EditText edittext1, edittext2;
	private ImageView imageview;
	private String Url;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weibo_sharebaby);
		init();
		listener();
	}

	private void listener() {
		// TODO Auto-generated method stub
		imgBnt1.setOnClickListener(this);
		imgBnt2.setOnClickListener(this);
		imageview.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(shareBaby.this, mapStorage.class);
				intent.putExtra(weiboUtils.weibo_sharePic_key,
						weiboUtils.weibo_sharePic_Flag);
				startActivity(intent);
				shareBaby.this.finish();
				return false;
			}
		});
	}

	private void init() {
		// TODO Auto-generated method stub
		imgBnt1 = (ImageButton) findViewById(R.id.weibo_sharebaby_imagebutton1);
		imgBnt2 = (ImageButton) findViewById(R.id.weibo_sharebaby_imagebutton2);
		edittext1 = (EditText) findViewById(R.id.weibo_sharebaby_edittext1);
		edittext2 = (EditText) findViewById(R.id.weibo_sharebaby_edittext2);
		imageview = (ImageView) findViewById(R.id.weibo_sharebaby_imageview);

		if (getIntent().getExtras().getInt(weiboUtils.weibo_sharePic_key) == weiboUtils.weibo_sharePic_Flag) {
			Url = weiboUtils.weibo_picUrl;
			Bitmap bit = BitmapFactory.decodeFile(Url);
			imageview.setImageBitmap(bit);
		} else if (getIntent().getExtras()
				.getInt(weiboUtils.weibo_sharePic_key) == weiboUtils.weibo_sharePic_Flag2) {
			imageview.setImageResource(R.drawable.info_head);
		}
	}

	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.weibo_sharebaby_imagebutton1:
			if (getIntent().getExtras().getInt(weiboUtils.weibo_return_key) == weiboUtils.weibo_return_Flag2) {
				this.finish();
			} else if (getIntent().getExtras().getInt(
					weiboUtils.weibo_return_key) == weiboUtils.weibo_return_Flag) {
				Intent intent = new Intent(this, PullDownActivity.class);
				 intent.putExtra(weiboUtils.weibo_sharePicSucc_key,
						 weiboUtils.weibo_return_Flag3);
				startActivity(intent);
				this.finish();
			}
			break;
		case R.id.weibo_sharebaby_imagebutton2:
			String strTitle = edittext1.getText().toString();
			if (strTitle.length()==0) {
				Toast.makeText(this, "标题不能为空", 1000).show();
			} else {
				String strUrl = weiboUtils.weibo_picUrl;
				long strTime = System.currentTimeMillis();
				int strVist = 0;
				String strPicUrl = "/sdcard/ll1x/" + "starbaby" + ".jpg";
				Intent intent = new Intent(shareBaby.this,
						PullDownActivity.class);
				intent.putExtra(weiboUtils.weibo_sharePicSucc_key,
						weiboUtils.weibo_sharePicSucc_Flag);
				intent.putExtra("text", strTitle);
//				intent.putExtra("vist", strVist);
//				intent.putExtra("time", strTime + "");
//				intent.putExtra("imghead", strPicUrl);
				intent.putExtra("url", "R.drawable.img_2");
				startActivity(intent);
				this.finish();
			}

			break;
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			break;
		case MotionEvent.ACTION_UP:
			break;

		}
		return false;
	}

}
