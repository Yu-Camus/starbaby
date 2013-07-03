package com.starbaby_03.Gallery;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import com.example.starbaby_03.R;
import com.starbaby_03.Gallery.mapShowMin.Filter;
import com.starbaby_03.Gallery.mapShowMin.SearchImage;
import com.starbaby_03.utils.galleryUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class addPic_show extends Activity implements OnItemClickListener,OnClickListener{
	private GridView gv;
	private ArrayList<String> pathList;//图片list
	private HashMap<Integer ,String > addPath=new HashMap<Integer , String>();
	private boolean flag=false;
	private ImageButton iBnt1,iBnt2;
	private ImageAdapter adapter;
	private String str;
	private TextView tv;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			adapter = new ImageAdapter(addPic_show.this,true,str);
			gv.setAdapter(adapter);
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		listener();
		new SearchImage();
	}
	void init(){
		setContentView(R.layout.gallery_addpic_min);
		gv=(GridView) findViewById(R.id.gallery_addpic_min_grideview);
		iBnt1=(ImageButton) findViewById(R.id.gallery_addpic_min_imagebutton1);
		iBnt2=(ImageButton) findViewById(R.id.gallery_addpic_min_imagebutton2);
		tv=(TextView) findViewById(R.id.gallery_addpic_min_textview);
	}
	void listener(){
		gv.setOnItemClickListener(this);
		iBnt1.setOnClickListener(this);
		iBnt2.setOnClickListener(this);
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
		Filter pf = new Filter();
		File files[] = root.listFiles(pf);
		if (files != null) {
			if (pathList == null)
				pathList = new ArrayList<String>();
			for (File f : files) {
				if (f.isDirectory()) {
					AbenLog.i("tag", "path:" + f.getAbsolutePath());
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

	public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
		adapter.changeState(position);
		if(addPath.containsKey(position)){
			addPath.remove(position);
			Log.e("addPath=",addPath+"");
		}else{
			addPath.put(position,adapter.pathList.get(position).toString());
			Log.e("addPath2=",addPath+"");
		}
		int length = 0;
		length = addPath.size();
		tv.invalidate();
		tv.setText("当前选择了"+length+"张");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
		case R.id.gallery_addpic_min_imagebutton1:
			this.finish();
			break;
		case R.id.gallery_addpic_min_imagebutton2:
			for(Integer key:addPath.keySet()){
				String picPath=addPath.get(key);
				Log.e("picPath=", picPath);
				String picName=addPath.get(key).substring(addPath.get(key).lastIndexOf("/")+1);
				Log.e("picName=", picName);
				File file=new File(mapShowMin.str);
				Log.e("file=", file+"");
				if(!file.exists()){
					file.mkdir();
				}else{
					try {
						Bitmap bmp=BitmapFactory.decodeFile(picPath);
						File saveFile=new File(mapShowMin.str+"/"+System.currentTimeMillis()+".jpg");
						BufferedOutputStream bos;
						bos = new BufferedOutputStream(
						        new FileOutputStream(saveFile));
						bmp.compress(Bitmap.CompressFormat.JPEG, 80, bos);
						bos.flush();
						bos.close();
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
			Toast.makeText(this, "添加完成", 1000).show();
			break;
		}
	}
}
