package com.starbaby_03.info;

/**
 * info:登入账号
 */
import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import com.example.starbaby_03.R;
import com.starbaby_03.aboutUs.DBSharedPreference;
import com.starbaby_03.aboutUs.center;
import com.starbaby_03.aboutUs.infoCenter;
import com.starbaby_03.main.appMain;
import com.starbaby_03.net.AsyncHttpGet;
import com.starbaby_03.net.AsyncHttpPost;
import com.starbaby_03.net.DefaultThreadPool;
import com.starbaby_03.net.RequestParameter;
import com.starbaby_03.net.RequestResultCallback;
import com.starbaby_03.utils.EncodeUtil;
import com.starbaby_03.utils.JsonObject;
import com.starbaby_03.utils.MyData;
import com.starbaby_03.utils.Utils;
import com.starbaby_03.utils.contentUtils;
import com.starbaby_03.utils.weiboUtils;
import com.starbaby_03.view.CircleImageView;
import com.starbaby_03.weibo.PullDownActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.Color;
import android.graphics.Paint;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class user_enter extends Activity implements OnClickListener {
	private ImageButton imgBnt;
	private Button Bnt1, Bnt2;
	private EditText editText1, editText2;
	private String customid;
	private TextView info_enter_textview5, info_enter_textview3;
	private ProgressBar progressbar;
	// public static SharedPreferences sp;
	private String ename;
	private String epsw;
	private Boolean nameFlag = false;
	private Boolean pswFlag = false;
	private ImageView info_enter_imageview1;
	private ImageView imgBnt1;
	private CheckBox ck;
	private String avatar = null;
	public Handler mHandler = new Handler() {
		@SuppressLint("NewApi")
		@Override
		public String getMessageName(Message message) {
			// TODO Auto-generated method stub
			message.what = 1;
			startActivity(new Intent(user_enter.this, PullDownActivity.class));
			progressbar.setVisibility(View.GONE);
			user_enter.this.finish();
			return super.getMessageName(message);
		}

	};

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.info_enter);

		init();
		listener();
	}

	private void listener() {
		// TODO Auto-generated method stub
		Bnt1.setOnClickListener(this);
		Bnt2.setOnClickListener(this);
		info_enter_textview5.setOnClickListener(this);

		ck.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			// @Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (ck.isChecked()) {
					contentUtils.sp.edit().putBoolean("ISCHECK", true).commit();
					contentUtils.sp.edit().putBoolean("AUTO_ISCHECK", true)
							.commit();
					imgBnt1.setBackgroundResource(R.drawable.psw_lock);
					System.out.println("记住密码已选中");
				} else {
					contentUtils.sp.edit().putBoolean("ISCHECK", false)
							.commit();
					contentUtils.sp.edit().putBoolean("AUTO_ISCHECK", false)
							.commit();
					imgBnt1.setBackgroundResource(R.drawable.psw_unlock);
					System.out.println("记住密码没有被选中");
				}
			}

		});

	}

	void init() {
		ck = (CheckBox) findViewById(R.id.box);
		Bnt1 = (Button) findViewById(R.id.info_enter_imagebutton2);
		Bnt2 = (Button) findViewById(R.id.info_enter_imagebutton3);
		imgBnt1 = (ImageView) findViewById(R.id.info_enter_imagebutton);
		editText1 = (EditText) findViewById(R.id.info_enter_edittext1);
		editText2 = (EditText) findViewById(R.id.info_enter_edittext2);

		info_enter_imageview1 = (ImageView) findViewById(R.id.info_enter_imageview1);
		progressbar = (ProgressBar) findViewById(R.id.info_enter_progressbar);
		progressbar.setVisibility(View.GONE);
		info_enter_textview5 = (TextView) findViewById(R.id.info_enter_textview);
		avatar = contentUtils.spinfo.getString("avatar", "");
		if (avatar.length() == 0) {
			info_enter_imageview1.setBackgroundResource(R.drawable.info_head);
		} else {
			info_enter_imageview1.setBackgroundDrawable(getWallpaper()
					.createFromPath(avatar));
		}
		Checked();
	}

	// 判断：是否记住密码，记住登入状态
	public void Checked() {
		ck.setEnabled(false);

		// 监听edittext内是否有文字输入。逻辑判断
		editText1.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
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
				if (editText1.getText() == null
						|| "".equals(editText1.getText().toString().trim())) {
					ck.setEnabled(false);
				} else {
					editText2.addTextChangedListener(new TextWatcher() {

						@Override
						public void onTextChanged(CharSequence s, int start,
								int before, int count) {
							// TODO Auto-generated method stub

						}

						@Override
						public void beforeTextChanged(CharSequence s,
								int start, int count, int after) {
							// TODO Auto-generated method stub

						}

						@Override
						public void afterTextChanged(Editable s) {
							// TODO Auto-generated method stub
							if (editText2.getText() == null
									|| "".equals(editText2.getText().toString()
											.trim())) {
								ck.setEnabled(false);
							} else {
								ck.setEnabled(true);
								// 默认记住密码

							}
						}
					});
				}
			}
		});
		if (contentUtils.sp.getBoolean("ISCHECK", false)) {
			ck.setChecked(true);
			Log.e("TAG", "1");
			editText1.setText(contentUtils.sp.getString("userName", ""));
			editText2.setText(contentUtils.sp.getString("psw", ""));

			// 默认自动登入
			if (contentUtils.sp.getBoolean("AUTO_ISCHECK", false)) {
				Log.e("TAG", "2");
				RelativeLayout relativelayout7 = (RelativeLayout) findViewById(R.id.info_enter_relativelayout7);
				RelativeLayout relativelayout8 = (RelativeLayout) findViewById(R.id.info_enter_relativelayout8);
				relativelayout7.setVisibility(4);
				relativelayout8.setVisibility(1);
				progressbar.setVisibility(View.VISIBLE);
				enter();
			}
		}

	}

	void enter() {
		ename = editText1.getText().toString();
		epsw = editText2.getText().toString();
		if (TextUtils.isEmpty(ename)) {
			Log.e("TAG", "3");
			Toast.makeText(user_enter.this, "账号不能为空", Toast.LENGTH_SHORT)
					.show();
			return;
		}
		if (TextUtils.isEmpty(epsw)) {
			Log.e("TAG", "4");
			Toast.makeText(user_enter.this, "密码不能为空", Toast.LENGTH_SHORT)
					.show();
			return;
		}
		// 登录成功和记住密码框为选中状态才保存用户信息
		if (ck.isChecked()) {
			// 记住用户名、密码、
			Log.e("TAG", "5");
			Editor editor = contentUtils.sp.edit();
			editor.putString("userName", ename);
			editor.putString("psw", epsw);
			editor.commit();
		}
		Log.e("TAG", "6");
		progressbar.setVisibility(View.VISIBLE);
		// 密码MD5一次
		contentUtils.psw = EncodeUtil.getMD5(epsw.getBytes());
		contentUtils.spGetInfo.edit().putString("psw", contentUtils.psw).commit();
		System.out.print("MD5" + contentUtils.psw);
		login(ename, contentUtils.psw);
	}

	// 登入判断
	public void login(String userName, String userPasword) {

		List<RequestParameter> parameterList = new ArrayList<RequestParameter>();
		parameterList.add(new RequestParameter("username", userName));
		parameterList.add(new RequestParameter("pwd", userPasword));
		AsyncHttpPost httpost = new AsyncHttpPost(null, contentUtils.enterUrl,
				parameterList, new RequestResultCallback() {

					@Override
					public void onSuccess(Object o) {
						// TODO Auto-generated method stub
						final String result = (String) o;
						customid = result.replace("\"", " ");
						MyData.getInstance().setCustomerid(customid);
						user_enter.this.runOnUiThread(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								progressbar.setVisibility(View.GONE);
								try {
									contentUtils.msg = new JsonObject()
											.getMSG(result);
									if (contentUtils.msg == 1) {
										contentUtils.psw = editText2.getText()
												.toString();
										contentUtils.uid = new JsonObject()
												.getUID(result);
										contentUtils.spGetInfo.edit().putInt("uid", contentUtils.uid).commit();
										contentUtils.username = new JsonObject()
												.getUSERNAME(result);
										contentUtils.spGetInfo.edit().putString("username", contentUtils.username).commit();
										contentUtils.avatar = new JsonObject()
												.getAVATAR(result);
										contentUtils.spinfo
												.edit()
												.putString("username",
														contentUtils.username)
												.commit();
										contentUtils.spinfo
												.edit()
												.putString("avatar",
														contentUtils.avatar)
												.commit();
										Log.i("json", contentUtils.msg + ":"
												+ contentUtils.uid + ":"
												+ contentUtils.username + ":"
												+ contentUtils.avatar + ":");
										Intent intent = new Intent(
												user_enter.this,
												infoCenter.class);
										intent.putExtra(
												weiboUtils.weibo_sharePicSucc_key,
												weiboUtils.weibo_return_Flag3);
										weiboUtils.FLAG = 1;
										startActivity(intent);
										user_enter.this.finish();
									} else if (contentUtils.msg == -4) {
										Toast.makeText(user_enter.this,
												"用户信息不存在", 1000).show();
									} else if (contentUtils.msg == -5) {
										Toast.makeText(user_enter.this, "密码错误",
												1000).show();
									} else {
										Toast.makeText(user_enter.this, "未知错误",
												1000).show();
									}

								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

								Log.e("result", result);

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

	// get方法获取头像
	public void getHeadImg(int uid, int size, int choose) {

		List<RequestParameter> parameterList = new ArrayList<RequestParameter>();
		AsyncHttpPost httpget = new AsyncHttpPost(null, contentUtils.getImgUrl
				+ "/" + uid + "/" + size + "/" + choose, parameterList,
				new RequestResultCallback() {

					@Override
					public void onSuccess(Object o) {
						// TODO Auto-generated method stub
						final String result = (String) o;
						customid = result.replace("\"", " ");
						MyData.getInstance().setCustomerid(customid);
						user_enter.this.runOnUiThread(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								contentUtils.headImgUrl = result;
								Log.e("headUrl=", result);
							}
						});
					}

					@Override
					public void onFail(Exception e) {
						// TODO Auto-generated method stub

					}
				});
		DefaultThreadPool.getInstance().execute(httpget);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.info_enter_imagebutton2:
			enter();
			break;
		case R.id.info_enter_imagebutton3:
			Intent intent2 = new Intent(this, user_register.class);
			startActivity(intent2);
			this.finish();
			break;
		case R.id.info_enter_imagebutton:
			if (ck.isChecked()) {
				imgBnt1.setBackgroundResource(R.drawable.psw_lock);
			} else {
				imgBnt1.setBackgroundResource(R.drawable.psw_unlock);
			}
			break;
		}
	}

	public void onBackPressed() {
		super.onBackPressed();
		startActivity(new Intent(user_enter.this, appMain.class));
		user_enter.this.finish();
	}
}
