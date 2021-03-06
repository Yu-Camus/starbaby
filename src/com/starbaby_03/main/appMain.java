package com.starbaby_03.main;

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

import com.baidu.mobstat.StatService;
import com.example.starbaby_03.R;
import com.starbaby_03.Gallery.mapStorage;
import com.starbaby_03.aboutUs.center;
import com.starbaby_03.camera.mCamera;
import com.starbaby_03.info.user_enter;
import com.starbaby_03.net.AsyncHttpGet;
import com.starbaby_03.net.AsyncHttpPost;
import com.starbaby_03.net.DefaultThreadPool;
import com.starbaby_03.net.RequestParameter;
import com.starbaby_03.net.RequestResultCallback;
import com.starbaby_03.scroll.scroll;
import com.starbaby_03.upDate.NetWork;
import com.starbaby_03.upDate.getVer;
import com.starbaby_03.utils.MyData;
import com.starbaby_03.utils.Utils;
import com.starbaby_03.utils.beautyUtils;
import com.starbaby_03.utils.contentUtils;
import com.starbaby_03.utils.meshImgUrl;
import com.starbaby_03.utils.weiboUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class appMain extends Activity implements OnTouchListener,
		OnClickListener {
	private ImageButton iBnt1, iBnt2, iBnt3, iBnt4, iBnt5;
	private Uri originalUri;
	private ImageView imgView, imgView2;
	public ProgressDialog pBar;
	private Handler handler = new Handler();
	private static int allow_minCode = 0;
	private int newVerCode = 0;
	private String newVerName = "";
	private Bitmap headBit;
	Handler mHandler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch(msg.what){
			case 1:
				imgView.setImageBitmap(headBit);
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
		setContentView(R.layout.main_activity_main);
		beautyUtils.layoutWidth = getWindowManager()
				.getDefaultDisplay().getWidth();
		init();
		listener();
	}

	/**
	 * APP版本升级模块
	 */
	// 开启APP前检测网络
	public void Check() {

		ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo info = manager.getActiveNetworkInfo();
		if (info != null && info.isConnected()) {
			Judge_allow_minCode();
			Toast.makeText(appMain.this, "connect", 1000).show();
		} else {
			Judge_allow_minCode2();
			Toast.makeText(appMain.this, "NoConnect", 1000).show();
		}
	}

	// 未连接网络的前提下，开启app自检服务器最新版本
	public void Judge_allow_minCode2() {
		int verCde = getVer.getVerCode(this);
		if (verCde < allow_minCode) {
			StringBuffer sb = new StringBuffer();
			sb.append("网络连接错误");
			new AlertDialog.Builder(this)
					.setTitle("你的版本过低")
					.setMessage(sb.toString())
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									finish();
								}
							}).create().show();
		} else {
			init();
		}
		Toast.makeText(appMain.this, "请检查你的网络", 1000).show();
	}
	// 连接网络的前提下，开启app自检服务器最新版本
	public void Judge_allow_minCode() {
		try {
			String versio = NetWork.getContent(getVer.UPDATE_SERVER
					+ getVer.UPDATE_VERJSON);
			JSONArray array = new JSONArray(versio);
			if (array.length() > 0) {
				JSONObject obj = array.getJSONObject(0);
				allow_minCode = Integer.parseInt(obj.getString("allow_min"));
				String Newupdate_link = obj.getString("update_link");
				if (Newupdate_link != null) {
					int verCode = getVer.getVerCode(this);

					if (verCode < allow_minCode) {
						forceNewVersionShow(Newupdate_link);
					} else {
						newVerCode = Integer.parseInt(obj
								.getString("version_num"));
						// newVerName = obj.getString("verName");
						if (newVerCode > verCode) {
							doNewVersionShow(Newupdate_link);
						} else {
							init();
						}
					}
				} else {
					init();
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

	// 强制更新操作
	private void forceNewVersionShow(final String updata_link) {
		// TODO Auto-generated method stub
		int verCode = getVer.getVerCode(this);
		String verName = getVer.getVerName(this);
		StringBuffer sb = new StringBuffer();
		sb.append("当前版本:");
		sb.append(verName);
		sb.append("verCode:");
		sb.append(verCode);
		sb.append("低于运行该软件的最低版本:");
		sb.append(allow_minCode);
		sb.append("是否更新");
		new AlertDialog.Builder(this).setTitle("更新软件")
				.setMessage(sb.toString())
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						pBar = new ProgressDialog(appMain.this);
						pBar.setTitle("正在下载");
						pBar.setMessage("请稍后...");
						pBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
						downFile(updata_link);
					}
				}).create().show();

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
						pBar = new ProgressDialog(appMain.this);
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

	/**
	 * 自检完后。对app的操作
	 */
	
	private void init() {
		// TODO Auto-generated method stub
		
		iBnt5 = (ImageButton) findViewById(R.id.main_activity_main_bnt);
		imgView2 = (ImageView) findViewById(R.id.main_activity_main_imageview2);
		imgView = (ImageView) findViewById(R.id.main_activity_main_imageview3);
		if (contentUtils.spinfo.getString("avatar", "").length() == 0) {
			imgView.setBackgroundResource(R.drawable.info_head);
		} else {
			Thread thread=new Thread(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					Message msg= new Message();
					msg.what=1;
					headBit=new meshImgUrl().returnBitMap(contentUtils.spinfo.getString("avatar", ""));
					mHandler.sendMessage(msg);
					super.run();
					
				}
				
			};
			thread.start();
			Log.e("TAG", "done");
		}
	}

	private void listener() {
		// TODO Auto-generated method stub
		iBnt5.setOnClickListener(this);
		imgView2.setOnTouchListener(this);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub

		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			if (v.getId() == R.id.main_activity_main_imageview2) {
				Bitmap bit = BitmapFactory.decodeResource(getResources(),
						R.drawable.appmain_menu);
				if (event.getX() > 102 && event.getX() < 242
						&& event.getY() > 21 && event.getY() < 97) {
					imgView2.setBackgroundResource(R.drawable.appmain_takephoto);
					return true;
				} else if (event.getX() > 247 && event.getX() < 323
						&& event.getY() > 102 && event.getY() < 243) {
					imgView2.setBackgroundResource(R.drawable.appmain_store);
					return true;
				} else if (event.getX() > 102 && event.getX() < 242
						&& event.getY() > 246 && event.getY() < 322) {
					imgView2.setBackgroundResource(R.drawable.appmain_share);
					return true;
				} else if (event.getX() > 21 && event.getX() < 97
						&& event.getY() > 102 && event.getY() < 243) {
					imgView2.setBackgroundResource(R.drawable.appmain_beauty);
					return true;
				} else {
					Log.e("tag", "do nothing");
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			if (v.getId() == R.id.main_activity_main_imageview2) {
				Bitmap bit = BitmapFactory.decodeResource(getResources(),
						R.drawable.appmain_menu);
				if (event.getX() > 102 && event.getX() < 242
						&& event.getY() > 21 && event.getY() < 97) {
					// 拍照
					Toast toast = Toast.makeText(getApplicationContext(),
							"正在打开摄像头", 0);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
					Intent intent = new Intent(appMain.this, mCamera.class);
					startActivity(intent);
					imgView2.setBackgroundResource(R.drawable.appmain_menu);
					finish();
					return true;
				} else if (event.getX() > 247 && event.getX() < 323
						&& event.getY() > 102 && event.getY() < 243) {
					// 图库
					Intent intent = new Intent(appMain.this, mapStorage.class);
					intent.putExtra(weiboUtils.weibo_sharePic_key,
							weiboUtils.weibo_showPic_Flag);
					startActivity(intent);
					imgView2.setBackgroundResource(R.drawable.appmain_menu);
					finish();
					return true;
				} else if (event.getX() > 102 && event.getX() < 242
						&& event.getY() > 246 && event.getY() < 322) {
					Intent intent = new Intent(appMain.this, user_enter.class);
					startActivity(intent);
					imgView2.setBackgroundResource(R.drawable.appmain_menu);
					finish();
					return true;
				} else if (event.getX() > 21 && event.getX() < 97
						&& event.getY() > 102 && event.getY() < 243) {
					ShowPickDialog();
					imgView2.setBackgroundResource(R.drawable.appmain_menu);
					return true;
				} else {
					imgView2.setBackgroundResource(R.drawable.appmain_menu);
				}
			}
			break;
		}
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.main_activity_main_bnt:
			startActivity(new Intent(this, center.class));
			this.finish();
			break;
		}
	}

	/**
	 * 选择提示对话框 ACTION_PICK的使用 重写onActivityResult方法 美化
	 */
	private void ShowPickDialog() {
		new AlertDialog.Builder(this).setTitle("获取图片...")
				.setNegativeButton("相册", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						Intent intent = new Intent(Intent.ACTION_PICK, null);
						// 设置数据类型
						intent.setDataAndType(
								MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
								"image/*");
						startActivityForResult(intent, 5);
					}
				}).show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode == 0)
			return;
		// 美化。打开相册操作
		if (requestCode == 5) {
			// 获得图片的uri
			originalUri = data.getData();
			Log.e("originalUri", originalUri.toString());
			Intent intentUri = new Intent(appMain.this, scroll.class);
			intentUri.putExtra("originalUri", originalUri.toString());
			intentUri.putExtra(Utils.INTENT_FLAG, Utils.FULL_IMAGE);
			startActivity(intentUri);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		this.finish();
	}
	 /**
     * 百度统计模块
     */
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		StatService.onPause(this);
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		StatService.onResume(this);
	}
}
