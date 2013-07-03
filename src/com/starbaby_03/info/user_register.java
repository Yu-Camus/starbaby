package com.starbaby_03.info;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.FileEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.starbaby_03.R;
import com.starbaby_03.Gallery.mapStorage;
import com.starbaby_03.camera.mCamera;
import com.starbaby_03.net.AsyncHttpPost;
import com.starbaby_03.net.DefaultThreadPool;
import com.starbaby_03.net.RequestParameter;
import com.starbaby_03.net.RequestResultCallback;
import com.starbaby_03.saveAndSearch.savePhoto;
import com.starbaby_03.scroll.scroll;
import com.starbaby_03.utils.EncodeUtil;
import com.starbaby_03.utils.JsonObject;
import com.starbaby_03.utils.MyData;
import com.starbaby_03.utils.UploadUtil;
import com.starbaby_03.utils.Utils;
import com.starbaby_03.utils.contentUtils;
import com.starbaby_03.utils.saveFile;
import com.starbaby_03.view.CircleImageView;
import com.starbaby_03.view.TextviewView2;
import com.starbaby_03.view.edittextView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.preference.PreferenceManager.OnActivityResultListener;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class user_register extends Activity implements OnClickListener,
		OnTouchListener {
	private ImageButton imgBnt1, imgBnt2, imgBnt3;
	private edittextView editText1, editText2, editText3;
	private TextviewView2 textview2;
	private String Flag = "man";// 1男,2女
	private String address, name, password;
	private String customid;
	private TextView info_register_textview1;
	private String headImgUrl = null;
	private String picPath = null;
	private ImageView imageview;
	private String contentUrl;
	private static final String TAG = "uploadImage";
	private Uri originalUri;
	@SuppressLint("NewApi")
	public Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 1:
				break;
			case 2:
				Toast.makeText(user_register.this, "请输入完整信息", 1000).show();
				break;
			}
			super.handleMessage(msg);
		}

	};

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.info_register);
		init();
		listener();
	}

	private void listener() {
		// TODO Auto-generated method stub
		textview2.setOnTouchListener(this);
		textview2.setOnClickListener(this);
		imgBnt1.setOnClickListener(this);
		imgBnt2.setOnClickListener(this);
		imageview.setOnClickListener(this);
		imageview.setOnTouchListener(this);
	}

	void init() {
		imgBnt1 = (ImageButton) findViewById(R.id.info_register_imagebutton1);
		imgBnt2 = (ImageButton) findViewById(R.id.info_register_imagebutton2);
		editText1 = (edittextView) findViewById(R.id.info_register_edittext1);
		editText2 = (edittextView) findViewById(R.id.info_register_edittext2);
		editText3 = (edittextView) findViewById(R.id.info_register_edittex3);
		editText1.setText("邮箱:");
		editText2.setText("昵称:");
		editText3.setText("密码:");
		textview2 = (TextviewView2) findViewById(R.id.info_register_textview2);
		textview2.setText("性别:");
		textview2.setBitmap(BitmapFactory.decodeResource(getResources(),
				R.drawable.info_man));
		imageview = (ImageView) findViewById(R.id.info_register_imageview1);
		info_register_textview1 = (TextView) findViewById(R.id.info_register_textview1);
		address = editText1.getText().toString();
		name = editText1.getText().toString();
		password = editText1.getText().toString();
		imageview.setBackgroundResource(R.drawable.info_head);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.info_register_imagebutton1:
			Intent intent = new Intent(this, user_enter.class);
			startActivity(intent);
			this.finish();
			break;
		case R.id.info_register_imagebutton2:

			// 前提：先发送头像给服务器
			Thread thread = new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					Message message = new Message();
					String userNick = editText2.getText().toString();// 昵称
					String userEmial = editText1.getText().toString();// 邮箱
					String userPwd = editText3.getText().toString();// 用户输入的密码
					String userSex = Flag;// 性别
					Integer userSys = 2;// android版本
					if (userNick == null || userEmial == null
							|| userPwd == null || userSex == null
							|| picPath == null) {
						message.what = 2;
						mHandler.sendMessage(message);
					} else {
						// 密码进行MD5加密
						String enPwd = EncodeUtil.getMD5(userPwd.getBytes());
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

							login(userNick, userEmial, enPwd, userSex, userSys,
									contentUrl);
							mHandler.sendMessage(message);
						}
					}
				}
			});
			thread.start();
			break;
		}
	}

	// 点击按钮，背景改变
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			break;
		case MotionEvent.ACTION_UP:
			if (v.getId() == R.id.info_register_textview2) {
				Log.e("TAG", "UP");
				if (Flag.equals("man")) {
					Log.e("TAG", "woman");
					textview2.invalidate();
					textview2.setText("性别:");
					textview2.setBitmap(BitmapFactory.decodeResource(
							getResources(), R.drawable.info_woman));
					Flag = "woman";
				} else if (Flag.equals("woman")) {
					Log.e("TAG", "man");
					textview2.invalidate();
					textview2.setText("性别:");
					textview2.setBitmap(BitmapFactory.decodeResource(
							getResources(), R.drawable.info_man));
					Flag = "man";
				}
			} else if (v.getId() == R.id.info_register_imageview1) {
				ShowHeadPhoto();
			}
			break;
		}
		return false;
	}

	private void alert() {
		Dialog dialog = new AlertDialog.Builder(this).setTitle("提示")
				.setMessage("您选择的不是有效的图片")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						picPath = null;
					}
				}).create();
		dialog.show();
	}

	// 注册操作,首先获取传送图片后获取的url
	private void login(String userNick, String userEmial, String userPwd,
			final String userSex, Integer userSys, String userAvatar) {
		// TODO Auto-generated method stub
		List<RequestParameter> parameterList = new ArrayList<RequestParameter>();
		parameterList.add(new RequestParameter("nick", userNick));
		parameterList.add(new RequestParameter("email", userEmial));
		parameterList.add(new RequestParameter("pwd", userPwd));
		parameterList.add(new RequestParameter("sex", userSex));
		parameterList.add(new RequestParameter("sys", userSys));
		parameterList.add(new RequestParameter("avatar", userAvatar));
		AsyncHttpPost httpost = new AsyncHttpPost(null,
				contentUtils.registerUrl, parameterList,
				new RequestResultCallback() {

					@Override
					public void onSuccess(Object o) {
						// TODO Auto-generated method stub
						final String result = (String) o;
						user_register.this.runOnUiThread(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								try {
									contentUtils.msg = new JsonObject().getMSG(result);
									if(contentUtils.msg==1){
										Intent intent2 = new Intent(user_register.this,
												user_enter.class);
										startActivity(intent2);
										user_register.this.finish();
										contentUtils.spinfo.edit().putString("sex", userSex)
												.commit();
										Log.e("result", result);
										Toast.makeText(user_register.this, "注册成功", 1000).show();
									}else if(contentUtils.msg==-1){
										Toast.makeText(user_register.this, "用户名为空", 1000).show();
									}else if(contentUtils.msg==-2){
										Toast.makeText(user_register.this, "用户密码为空", 1000).show();
									}else if(contentUtils.msg==-6){
										Toast.makeText(user_register.this, "邮箱为空", 1000).show();
									}else if(contentUtils.msg==-7){
										Toast.makeText(user_register.this, "邮箱格式错误", 1000).show();
									}else if(contentUtils.msg==-8){
										Toast.makeText(user_register.this, "用户名应为3-15个字符", 1000).show();
									}else if(contentUtils.msg==-9){
										Toast.makeText(user_register.this, "用户名已存在", 1000).show();
									}else if(contentUtils.msg==-10){
										Toast.makeText(user_register.this, "邮箱已存在", 1000).show();
									}else if(contentUtils.msg==-11){
										Toast.makeText(user_register.this, "注册失败", 1000).show();
									}else if(contentUtils.msg==-12){
										Toast.makeText(user_register.this, "未知错误", 1000).show();
									}else if(contentUtils.msg==-13){
										Toast.makeText(user_register.this, "请先上传头像", 1000).show();
									}
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
							}
						});
					}

					@Override
					public void onFail(Exception e) {
						// TODO Auto-generated method stub
					}
				});
		DefaultThreadPool.getInstance().execute(httpost);
	}

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
				// 设置头像图片为圆角
				imageview.invalidate();
//				imageview.setImageBitmap(headImgBitmap);
				imageview.setBackgroundDrawable(getWallpaper().createFromPath(picPath));
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
		intent.putExtra("outputX", 150);
		intent.putExtra("outputY", 150);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 3);
	}
}
