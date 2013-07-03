package com.starbaby_03.camera;

import java.io.IOException;

import com.example.starbaby_03.R;
import com.starbaby_03.saveAndSearch.savePhoto;
import com.starbaby_03.scroll.scroll;
import com.starbaby_03.scroll.scrollOperate;
import com.starbaby_03.utils.cameraUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.AlteredCharSequence;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
	
public class operatePic extends Activity implements
OnClickListener{
	private ImageButton iBnt1,iBnt2,iBnt3;
	private ImageView iv1;
	private Button bnt1,bnt2;
	private AlertDialog.Builder bulider;
	private AlertDialog alert; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camera_operatepic);
		init();
		listener();
	}

	private void listener() {
		iBnt1.setOnClickListener(this);
		iBnt2.setOnClickListener(this);
		iBnt3.setOnClickListener(this);
	}

	private void init() {
		iBnt1 = (ImageButton) findViewById(R.id.camera_operatepic_ibnt1);
		iBnt2 = (ImageButton) findViewById(R.id.camera_operatepic_ibnt2);
		iBnt3 = (ImageButton) findViewById(R.id.camera_operatepic_ibnt3);
		iv1 = (ImageView) findViewById(R.id.camera_operatepic_iv1);
		iv1.setImageBitmap(cameraUtils.bitmap);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.camera_operatepic_ibnt1:
			this.finish();
			startActivity(new Intent(operatePic.this,mCamera.class));
			break;
		case R.id.camera_operatepic_ibnt2://保存
			new savePhoto().saveJpeg(cameraUtils.bitmap);
			break;
		case R.id.camera_operatepic_ibnt3://美化
			try {
				new savePhoto().takePhoto(cameraUtils.bitmap);
				startActivity(new Intent(operatePic.this,scroll.class));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case R.id.scroll_dialog_button1://保存 跳转到心情 界面
			Intent intent = new Intent(this,scrollOperate.class);
			startActivity(intent);
			alert.dismiss();
			break;
		}
	}
	/**
	 * dialog 用来对图片进行操作
	 */
	private void showAlertDialog() {
		LayoutInflater inflate =(LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
		View title = inflate.inflate(R.layout.scroll_dialog_title, null);
		View button = inflate.inflate(R.layout.scroll_dialog_button, null);
		bnt1 = (Button) button.findViewById(R.id.scroll_dialog_button1);
		bnt2 = (Button) button.findViewById(R.id.scroll_dialog_button2);
		bnt1.setOnClickListener(this);
		bnt2.setOnClickListener(this);
		bulider= new AlertDialog.Builder(operatePic.this);
		bulider.setCustomTitle(title);
		bulider.setView(button);
		alert = bulider.create();
		alert.show();
	}
}
