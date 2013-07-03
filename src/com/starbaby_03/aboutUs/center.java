package com.starbaby_03.aboutUs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.starbaby_03.R;
import com.starbaby_03.camera.mCamera;
import com.starbaby_03.info.user_enter;
import com.starbaby_03.main.appMain;
import com.starbaby_03.net.AsyncHttpPost;
import com.starbaby_03.net.DefaultThreadPool;
import com.starbaby_03.net.RequestParameter;
import com.starbaby_03.net.RequestResultCallback;
import com.starbaby_03.upDate.NetWork;
import com.starbaby_03.upDate.getVer;
import com.starbaby_03.utils.JsonObject;
import com.starbaby_03.utils.MyData;
import com.starbaby_03.utils.Utils;
import com.starbaby_03.utils.contentUtils;
import com.starbaby_03.utils.meshImgUrl;
import com.starbaby_03.utils.weiboUtils;
import com.starbaby_03.view.CircleImageView;
import com.starbaby_03.view.TextviewView;
import com.starbaby_03.weibo.PullDownActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class center extends Activity implements OnTouchListener,
		OnClickListener {
	private ImageView imgView1;
	private TextviewView textView2, textView3, textView4;
	private ImageButton iBnt1;
	private Button bnt1;
	private String avatar = null;
	private static int allow_minCode = 0;
	private int newVerCode = 0;
	private String newVerName = "";
	public ProgressDialog pBar;
	private Bitmap headbit;
	private String customid;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 1:
				imgView1.setImageBitmap(headbit);
				break;
			case 2:
				Toast.makeText(center.this, "connect", 1000).show();
				break;
			case 3:
				Toast.makeText(center.this, "NoConnect", 1000).show();
				break;
			}
			super.handleMessage(msg);
		}

	};

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.aboutus_center);
//		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
//				.detectDiskReads().detectDiskWrites().detectNetwork()
//				.penaltyLog().build());
//		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
//				.detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
//				.penaltyLog().penaltyDeath().build());
		init();
		listener();
	}

	private void init() {
		// TODO Auto-generated method stub
		iBnt1 = (ImageButton) findViewById(R.id.aboutus_center_imagebutton);
		bnt1 = (Button) findViewById(R.id.aboutus_center_button1);
		imgView1 = (ImageView) findViewById(R.id.aboutus_center_imageview2);
		avatar = contentUtils.spinfo.getString("avatar", "");
		Log.e("avatar=", "4");
		Log.e("avatar=", avatar + "8");
		if (avatar.length() == 0) {
			imgView1.setBackgroundResource(R.drawable.info_head);

		} else {
			Thread thread = new Thread() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					Message msg = new Message();
					msg.what = 1;
					headbit = new meshImgUrl().returnBitMap(contentUtils.spinfo
							.getString("avatar", ""));
					handler.sendMessage(msg);
					super.run();
				}

			};
			thread.start();

		}
		textView2 = (TextviewView) findViewById(R.id.aboutus_center_textview2);
		textView3 = (TextviewView) findViewById(R.id.aboutus_center_textview3);
		textView4 = (TextviewView) findViewById(R.id.aboutus_center_textview4);
		textView2.setText("个人资料");
		textView2.setImg1(BitmapFactory.decodeResource(getResources(),
				R.drawable.userinfo));
		textView2.setImg2(BitmapFactory.decodeResource(getResources(),
				R.drawable.arrow));
		textView3.setText("关于我们");
		textView3.setImg1(BitmapFactory.decodeResource(getResources(),
				R.drawable.aboutus));
		textView3.setImg2(BitmapFactory.decodeResource(getResources(),
				R.drawable.arrow));
		textView4.setText("检查更新");
		textView4.setImg1(BitmapFactory.decodeResource(getResources(),
				R.drawable.checkupdata));
		textView4.setImg2(BitmapFactory.decodeResource(getResources(),
				R.drawable.arrow));
	}

	private void listener() {
		// TODO Auto-generated method stub
		textView2.setOnTouchListener(this);
		textView2.setOnClickListener(this);
		textView3.setOnTouchListener(this);
		textView3.setOnClickListener(this);
		textView4.setOnTouchListener(this);
		textView4.setOnClickListener(this);
		iBnt1.setOnClickListener(this);
		bnt1.setOnClickListener(this);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			if (v.getId() == R.id.aboutus_center_textview2) {
				textView2.setBackgroundResource(R.drawable.info_shape_in);
			} else if (v.getId() == R.id.aboutus_center_textview3) {
				textView3.setBackgroundResource(R.drawable.info_shape2_in);
			} else if (v.getId() == R.id.aboutus_center_textview4) {
				textView4.setBackgroundResource(R.drawable.info_shape4_in);
			}
			break;
		case MotionEvent.ACTION_UP:
			if (v.getId() == R.id.aboutus_center_textview2) {
				Log.e("TAG", "Up");
				textView2.setBackgroundResource(R.drawable.info_shape);
				startActivity(new Intent(this, centerUs.class));
			} else if (v.getId() == R.id.aboutus_center_textview3) {
				textView3.setBackgroundResource(R.drawable.info_shape2);
			} else if (v.getId() == R.id.aboutus_center_textview4) {
				textView4.setBackgroundResource(R.drawable.info_shape4);
				new Thread(new MyThread()).start();
			}
			break;
		}
		return true;
	}
	class MyThread implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
			NetworkInfo info = manager.getActiveNetworkInfo();
			Message msg=new Message();
			if (info != null && info.isConnected()) {
				Judge_allow_minCode();
				msg.what=2;
			} else {
				Judge_allow_minCode2();
				msg.what=3;
			}
			handler.sendMessage(msg);
		}
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Log.e("TAG", "click");
		switch (v.getId()) {
		case R.id.aboutus_center_imagebutton:
			startActivity(new Intent(this, appMain.class));
			this.finish();
			break;
		case R.id.aboutus_center_button1:
			contentUtils.sp.edit().clear().commit();
			startActivity(new Intent(this, user_enter.class));
			this.finish();
			break;
		}
	}

	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		startActivity(new Intent(this, appMain.class));
		this.finish();
	}

	/**
	 * 开启APP前检测网络
	 */
	public void Check() {

		ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo info = manager.getActiveNetworkInfo();
		if (info != null && info.isConnected()) {
			Judge_allow_minCode();
//			Toast.makeText(center.this, "connect", 1000).show();
			Log.e("connect=", "connect");
		} else {
			Judge_allow_minCode2();
//			Toast.makeText(center.this, "NoConnect", 1000).show();
			Log.e("NoConnect=", "NoConnect");
		}
	}

	// 未连接网络的前提下，开启app自检服务器最新版本
	public void Judge_allow_minCode2() {
//		Toast.makeText(center.this, "当前网络不可用", 1000).show();
		Log.e("当前网络不可用=", "当前网络不可用");
	}

	// 连接网络的前提下，开启app自检服务器最新版本
	public void Judge_allow_minCode() {
		try {
			String versio = NetWork.getContent(getVer.UPDATE_SERVER
					+ getVer.UPDATE_VERJSON);
			JSONArray array = new JSONArray(versio);
			if (array.length() > 0) {
				Log.i("TAG", ">0");
				JSONObject obj = array.getJSONObject(0);
				allow_minCode = Integer.parseInt(obj.getString("allow_min"));
				String Newupdate_link = obj.getString("update_link");
				if (Newupdate_link != null) {
					int verCode = getVer.getVerCode(this);

					newVerCode = Integer.parseInt(obj.getString("version_num"));
					// newVerName = obj.getString("verName");
					if (newVerCode > verCode) {
						doNewVersionShow(Newupdate_link);
					} else {
						Toast.makeText(center.this, "当前版本是最新的", 3000).show();
					}

				} else {
					Log.i("TAG", "<0");
					Toast.makeText(center.this, "当前版本是最新的", 1000).show();
				}
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// return allow_minCode;

	}

	public void doNewVersionShow(final String updata_link) {
		int verCode = getVer.getVerCode(this);
		String verName = getVer.getVerName(this);
		StringBuffer sb = new StringBuffer();
		sb.append("当前版本:");
		sb.append(verName);
		sb.append(" Code:");
		sb.append(verCode);
		sb.append(",发现新版本:");
		sb.append(newVerName);
		sb.append(",是否更新?");
		new AlertDialog.Builder(this)
				.setTitle("软件更新")
				.setMessage(sb.toString())
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						pBar = new ProgressDialog(center.this);
						pBar.setTitle("正在下载");
						pBar.setMessage("请稍后...");
						pBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
						downFile(updata_link);
					}
				})
				.setNegativeButton("暂时不更新",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								init();
							}
						}).create().show();
	}

	public void downFile(final String updata_link) {
		pBar.show();
		new Thread() {
			public void run() {
				HttpClient client = new DefaultHttpClient();
				// HttpGet get=new
				// HttpGet(getVer.UPDATE_SERVER+getVer.UPDATE_APKNAME);
				HttpGet get = new HttpGet(updata_link + getVer.UPDATE_APKNAME);
				Log.e("info", "net" + get);
				HttpResponse response;
				try {
					response = client.execute(get);
					Log.e("info", "back7" + response);
					HttpEntity entity = response.getEntity();
					long length = entity.getContentLength();
					InputStream is = entity.getContent();
					FileOutputStream fileoutputstream = null;
					// 下载后生成的apk.name
					File file = new File(
							Environment.getExternalStorageDirectory(),
							getVer.UPDATE_SAVENAME);
					if (is != null) {
						Log.e("info", "back8" + file);
						fileoutputstream = new FileOutputStream(file);
						byte[] buf = new byte[1024];
						int ch = -1;
						int count = 0;
						while ((ch = is.read(buf)) != -1) {
							fileoutputstream.write(buf, 0, ch);
							count += ch;
							Log.e("info", "back10" + count);
						}
					}
					fileoutputstream.flush();
					if (fileoutputstream != null) {
						fileoutputstream.close();
						is.close();
					}
					down();
					Log.e("info", "back11");
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();
	}

	protected void down() {
		handler.post(new Runnable() {
			public void run() {
				pBar.cancel();
				Log.e("info", "done");
				update();
				Log.e("info", "do");
			}
		});
	}

	public void update() {
		File apkFile = new File(Environment.getExternalStorageDirectory(),
				getVer.UPDATE_SAVENAME);
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.parse("file://" + apkFile.toString()),
				"application/vnd.android.package-archive");
		startActivity(intent);
	}

}
