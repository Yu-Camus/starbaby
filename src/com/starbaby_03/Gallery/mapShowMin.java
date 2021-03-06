package com.starbaby_03.Gallery;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.example.starbaby_03.R;
import com.starbaby_03.camera.mCamera;
import com.starbaby_03.utils.galleryUtils;
import com.starbaby_03.utils.weiboUtils;
import com.starbaby_03.weibo.shareBaby;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.renderscript.ProgramFragmentFixedFunction.Builder.Format;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageButton;

public class mapShowMin extends Activity implements OnItemClickListener,
		OnClickListener {
	/** Called when the activity is first created. */
	private static final String tag = mapShowMin.class.getSimpleName();
	private ArrayList<String> pathList;
	private ProgressDialog dialog;
	private GridView gv;
	public static String str;
	private MyAdapter1 adapter;
	private ImageButton imgbnt1, imgbnt2, imgbnt3, imgbnt4,addbnt,takephoto;
	private TextView mActionText;
	private static final int MENU_SELECT_ALL = 0;
	private static final int MENU_UNSELECT_ALL = MENU_SELECT_ALL + 1;
	private Map<Integer, Boolean> mSelectMap = new HashMap<Integer, Boolean>();
	public static Boolean Flag = false;
	private Boolean isOnClick = false;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			dialog.cancel();
			adapter = new MyAdapter1(mapShowMin.this, pathList);
			gv.setAdapter(adapter);
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.gallery_showmin);
		gv = (GridView) findViewById(R.id.gv);
		gv.setFastScrollEnabled(true);
		dialog = new ProgressDialog(mapShowMin.this);
		dialog.show();
		new SearchImage();
		gv.setOnItemClickListener(this);
		init();
		listener();
	}

	private void listener() {
		// TODO Auto-generated method stub
		imgbnt1.setOnClickListener(this);
		addbnt.setOnClickListener(this);
		takephoto.setOnClickListener(this);
	}

	public void init() {
		imgbnt1 = (ImageButton) findViewById(R.id.main_imagebutton1);
		mActionText = (TextView) findViewById(R.id.main_textview);
		mActionText.setText(galleryUtils.picName);
		addbnt=(ImageButton) findViewById(R.id.main_imagebutton2);
		takephoto=(ImageButton) findViewById(R.id.gallery_showmin_imagebutton2);
	}

	public class SearchImage extends Thread {
		public SearchImage() {
			start();
		}

		@Override
		public void run() {
			Intent intent = getIntent();
			str = intent.getStringExtra("url");
			System.out.print(str);
			getAllFiles(new File(str));
			Message msg = new Message();
			msg.what = 0;
			handler.sendMessage(msg);
		}
	}

	/**
	 * 获取目录下所有的图片文件名称列表
	 * 
	 * @param root目录
	 * @return
	 */
	public ArrayList<String> getAllFiles(File root) {
		// fileList = new ArrayList<File>();
		Filter pf = new Filter();
		File files[] = root.listFiles(pf);
		if (files != null) {
			if (pathList == null)
				pathList = new ArrayList<String>();
			for (File f : files) {
				if (f.isDirectory()) {
					AbenLog.i(tag, "path:" + f.getAbsolutePath());
					if (!f.getName().toString().matches("^\\..*")) {
						getAllFiles(f);
					}
				} else {
					pathList.add(f.getAbsolutePath());
				}
			}
			galleryUtils.picList = pathList;
			return pathList;
		} else {
			return null;
		}
	}

	public class Filter implements FileFilter {
		public boolean accept(File f) {
			return f.isDirectory()
					|| f.getName().matches("^.*?\\.(jpg|png|bmp|gif)$");
		}
	}

	public void onBackPressed() {
		super.onBackPressed();
		mapShowMin.this.finish();
		Intent intent = new Intent(this, mapStorage.class);
		intent.putExtra(weiboUtils.weibo_sharePic_key,
				weiboUtils.weibo_showPic_Flag);
		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.main_imagebutton1:
			mapShowMin.this.finish();
			Intent intent = new Intent(this, mapStorage.class);
			if (getIntent().getExtras().getInt(weiboUtils.weibo_sharePic_key) == weiboUtils.weibo_sharePic_Flag) {
				// 直接返回晒图
				intent.putExtra(weiboUtils.weibo_sharePic_key,
						weiboUtils.weibo_sharePic_Flag);
			} else {
				intent.putExtra(weiboUtils.weibo_sharePic_key,
						weiboUtils.weibo_showPic_Flag);
			}
			startActivity(intent);
			break;
		case R.id.main_imagebutton2://向该相册导入相片
			Intent intent2 = new Intent(this, addPic.class);
			startActivity(intent2);
			this.finish();
			break;
		case R.id.gallery_showmin_imagebutton2:
			Intent intent3 = new Intent(mapShowMin.this, mCamera.class);
			startActivity(intent3);
			finish();
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		// TODO Auto-generated method stub
		weiboUtils.pathList_1 = pathList;
		String url = pathList.get(position).toString();
		Intent intent = null;
		weiboUtils.weibo_picUrl = url;
		galleryUtils.position = position;
		if (getIntent().getExtras().getInt(weiboUtils.weibo_sharePic_key) == weiboUtils.weibo_sharePic_Flag) {
			// 直接返回晒图
			intent = new Intent(this, shareBaby.class);
			intent.putExtra(weiboUtils.weibo_sharePic_key,
					weiboUtils.weibo_sharePic_Flag);
			intent.putExtra(weiboUtils.weibo_return_key,
					weiboUtils.weibo_return_Flag);
		} else if (getIntent().getExtras()
				.getInt(weiboUtils.weibo_sharePic_key) == weiboUtils.weibo_showPic_Flag) {
			// 显示原图片
			intent = new Intent(this, mapShowOrginal.class);
		}
		startActivity(intent);
		this.finish();
	}
}