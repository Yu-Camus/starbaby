package com.starbaby_03.scroll;

import com.example.starbaby_03.R;
import com.starbaby_03.utils.beautyUtils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class showPic extends Activity implements OnClickListener {
	private ImageButton iBnt1, iBnt2;
	private ImageView iv1;
	private TextView tv1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scroll_showpic);
		init();
		listener();
	}

	private void listener() {
		// TODO Auto-generated method stub
		iBnt1.setOnClickListener(this);
		iBnt2.setOnClickListener(this);
	}

	private void init() {
		// TODO Auto-generated method stub
		iBnt1 = (ImageButton) findViewById(R.id.scroll_showpic_ibnt1);
		iBnt2 = (ImageButton) findViewById(R.id.scroll_showpic_ibnt1);
		iv1 = (ImageView) findViewById(R.id.srcoll_showpic_iv1);
		tv1 = (TextView) findViewById(R.id.scroll_showpic_tv1);
		getBitmap();
	}
	private void getBitmap(){
		Intent intent= getIntent();
		String path = intent.getStringExtra("mapShowMin2_url");
		Bitmap bitmap = BitmapFactory.decodeFile(path);
		iv1.setImageBitmap(bitmap);
		String text = "";
		if(beautyUtils.spPic.contains("/mnt"+path)){
			text = beautyUtils.spPic.getString("/mnt"+path, "");
		}
		tv1.setText(text);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.scroll_showpic_ibnt1:
			this.finish();
			break;
		case R.id.scroll_showpic_ibnt2:
			break;
		}
	}

}
