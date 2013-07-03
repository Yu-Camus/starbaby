package com.starbaby_03.aboutUs;


import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import com.example.starbaby_03.R;
import com.starbaby_03.Gallery.mapStorage;
import com.starbaby_03.info.user_enter;
import com.starbaby_03.main.appMain;
import com.starbaby_03.net.AsyncHttpPost;
import com.starbaby_03.net.DefaultThreadPool;
import com.starbaby_03.net.RequestParameter;
import com.starbaby_03.net.RequestResultCallback;
import com.starbaby_03.saveAndSearch.savePhoto;
import com.starbaby_03.utils.EncodeUtil;
import com.starbaby_03.utils.JsonObject;
import com.starbaby_03.utils.MyData;
import com.starbaby_03.utils.UploadUtil;
import com.starbaby_03.utils.Utils;
import com.starbaby_03.utils.contentUtils;
import com.starbaby_03.utils.meshImgUrl;
import com.starbaby_03.utils.saveFile;
import com.starbaby_03.view.CircleImageView;
import com.starbaby_03.view.TextviewView;
import com.starbaby_03.view.TextviewView3;
import com.starbaby_03.view.TextviewView4;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class centerUs extends Activity implements OnClickListener {
	private ImageButton iBnt1,iBnt2;
	private ImageView imgView1;
	private TextviewView3 textview1, textview2, textview4;
	private TextviewView4 textview3;
	private String name = "";
	private String time = "";
	private String address = "上海市";
	private String sex = null;
	private String avatar = null;
	private Bitmap headbit;
	private String picPath = null;
	private String contentUrl;
	private String customid;
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch(msg.what){
			case 1:
				imgView1.setImageBitmap(headbit);
				break;
			}
			super.handleMessage(msg);
		}
		
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.aboutus_centerus);
		init();
		listener();
	}

	private void listener() {
		// TODO Auto-generated method stub
		iBnt1.setOnClickListener(this);
		iBnt2.setOnClickListener(this);
		imgView1.setOnClickListener(this);
	}

	private void init() {
		// TODO Auto-generated method stub
		iBnt1 = (ImageButton) findViewById(R.id.aboutus_centerus_imagebutton);
		iBnt2 = (ImageButton) findViewById(R.id.aboutus_centerus_imagebutton2);
		imgView1 = (ImageView) findViewById(R.id.aboutus_centerus_imageview2);
		avatar = contentUtils.spinfo.getString("avatar", "");
		if (avatar .length()==0) {
			imgView1.setBackgroundResource( R.drawable.info_head);
			
		} else {
			Thread thread= new Thread(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					Message msg=new Message();
					msg.what=1;
					headbit=new meshImgUrl().returnBitMap(contentUtils.spinfo.getString("avatar", ""));
					mHandler.sendMessage(msg);
					super.run();
				}
				
			};
			thread.start();
			
		}
		textview1 = (TextviewView3) findViewById(R.id.aboutus_centerus_textview2);
		textview1.setHead("爱称");
		textview1.setText(contentUtils.spinfo.getString("username", ""));
		textview2 = (TextviewView3) findViewById(R.id.aboutus_centerus_textview3);
		textview2.setHead("生日");
		textview3 = (TextviewView4) findViewById(R.id.aboutus_centerus_textview4);
		sex = contentUtils.spinfo.getString("sex", "");
		if (sex.equals("man")) {
			textview3.setText("性别");
			textview3.setBitmap(BitmapFactory.decodeResource(getResources(),
					R.drawable.info_man));
		} else {
			textview3.setText("性别");
			textview3.setBitmap(BitmapFactory.decodeResource(getResources(),
					R.drawable.info_woman));
		}

		textview4 = (TextviewView3) findViewById(R.id.aboutus_centerus_textview5);
		textview4.setHead("所在城市");
		textview4.setText(address);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.aboutus_centerus_imagebutton:
			startActivity(new Intent(centerUs.this, center.class));
			centerUs.this.finish();
			break;
		case R.id.aboutus_centerus_imagebutton2:
			// 密码进行MD5加密
			if(contentUtils.psw==null){
				Toast.makeText(centerUs.this, "请先登录", 1000).show();
			}
			String enPwd = EncodeUtil.getMD5(contentUtils.psw.getBytes());
			File file = new File(picPath);
			if (file != null) {
				String request = UploadUtil.uploadFile(file,
						contentUtils.registerImgUrl);
				// json解析获取的url
				try {
					contentUrl = new JsonObject()
							.getIMAGEURl(request);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Log.e("uid=", contentUtils.uid+"");
				changeHeadImg(contentUtils.uid,enPwd,contentUrl);
			}
			break;
		case R.id.aboutus_centerus_imageview2:
			ShowHeadPhoto();
			break;
		}
	}
	
//	private void showDialog() {
//		// TODO Auto-generated method stub
//		LayoutInflater inflate= LayoutInflater.from(this);
//		View view = inflate.inflate(R.layout.aboutus_centerus_view, null);
//		AlertDialog.Builder builder = new AlertDialog.Builder(centerUs.this);
//		builder.setTitle("修改头像");
//		builder.setView(view);
////		iv=(ImageView) view. findViewById(R.id.aboutus_centerus_view_imageview1);
////		iv.setOnClickListener(new OnClickListener() {
////			
////			@Override
////			public void onClick(View v) {
////				// TODO Auto-generated method stub
////				ShowHeadPhoto();
////			}
////		});
//		builder.setNegativeButton("取消", new OnClickListener() {
//			
//			public void onClick(DialogInterface dialog, int which) {
//				// TODO Auto-generated method stub
//				
//			}
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
//		
//	}

	/**
	 ** 设置头像
	 */
	private void ShowHeadPhoto() {
		new AlertDialog.Builder(this)
				.setTitle("选择头像")
				.setNegativeButton("相册", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(Intent.ACTION_GET_CONTENT,
								null);
						intent.setDataAndType(
								MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
								"image/*");
						startActivityForResult(intent, 2);
					}
				})
				.setPositiveButton("拍照", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(
								MediaStore.ACTION_IMAGE_CAPTURE);
						intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri
								.fromFile(new File(Environment
										.getExternalStorageDirectory(),
										"temp.jpg")));
						System.out.println("============="
								+ Environment.getExternalStorageDirectory());
						startActivityForResult(intent, 1);
					}
				}).show();
	}
	// 个操作的相对相应事件
		protected void onActivityResult(int requestCode, int resultCode,
				final Intent data) {
			if (resultCode == 0)
				return;
			// 拍照
			if (requestCode == 1) {
				// 设置文件保存路径这里放在跟目录下
				File picture = new File(Environment.getExternalStorageDirectory()
						+ "/temp.jpg");
				System.out.println("------------------------" + picture.getPath());
				startPhotoZoom(Uri.fromFile(picture));
			}
			if (data == null)
				return;
			// 读取相册缩放图片
			if (requestCode == 2) {
				startPhotoZoom(data.getData());
			}
			// 处理结果
			if (requestCode == 3) {
				Bundle extras = data.getExtras();
				if (extras != null) {
					Bitmap photo = extras.getParcelable("data");
					// 保存头像到本地
					try {
						new savePhoto().saveHeadImg(photo);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					picPath = saveFile.headName;
					File tempFile = new File(saveFile.headName);
					Bitmap headImgBitmap = BitmapFactory.decodeFile(tempFile
							.getAbsolutePath());
					imgView1.invalidate();
//					imgView1.setBackgroundDrawable(getWallpaper().createFromPath(picPath));
					imgView1.setImageBitmap(BitmapFactory.decodeFile(picPath));
				}
			}
			super.onActivityResult(requestCode, resultCode, data);
		}

		// 头像剪裁操作
		public void startPhotoZoom(Uri uri) {
			Intent intent = new Intent("com.android.camera.action.CROP");
			intent.setDataAndType(uri, "image/*");
			intent.putExtra("crop", "true");
			// aspectX aspectY 是宽高的比例
			intent.putExtra("aspectX", 1);
			intent.putExtra("aspectY", 1);
			// outputX outputY 是裁剪图片宽高
			intent.putExtra("outputX", 200);
			intent.putExtra("outputY", 200);
			intent.putExtra("return-data", true);
			startActivityForResult(intent, 3);
		}
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		centerUs.this.finish();
		Intent intent = new Intent(this, center.class);
		startActivity(intent);
	}

	// post方法改变头像
	public void changeHeadImg(int uid, String psw, String url) {

		List<RequestParameter> parameterList = new ArrayList<RequestParameter>();
		parameterList.add(new RequestParameter("uid", uid));
		parameterList.add(new RequestParameter("pwd", psw));
		parameterList.add(new RequestParameter("avatar", url));
		AsyncHttpPost httppost = new AsyncHttpPost(null, contentUtils.changeImgUrl
				, parameterList,
				new RequestResultCallback() {

					@Override
					public void onSuccess(Object o) {
						// TODO Auto-generated method stub
						final String result = (String) o;
						customid = result.replace("\"", " ");
						MyData.getInstance().setCustomerid(customid);
						centerUs.this.runOnUiThread(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								String msg = result;
								Log.e("msg=", result);
							}
						});
					}

					@Override
					public void onFail(Exception e) {
						// TODO Auto-generated method stub

					}
				});
		DefaultThreadPool.getInstance().execute(httppost);
	}
}
