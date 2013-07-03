package com.starbaby_03.Gallery;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.starbaby_03.R;
import com.starbaby_03.Gallery.mapShowMin.Filter;
import com.starbaby_03.aboutUs.infoCenter;
import com.starbaby_03.aboutUs.infoCenter.ContentTask;
import com.starbaby_03.aboutUs.infoCenter.StaggeredAdapter;
import com.starbaby_03.camera.mCamera;
import com.starbaby_03.main.HotInfo;
import com.starbaby_03.main.Helper;
import com.starbaby_03.main.ImageFetcher;
import com.starbaby_03.main.ScaleImageView;
import com.starbaby_03.main.XListView;
import com.starbaby_03.main.XListView.IXListViewListener;
import com.starbaby_03.scroll.showPic;
import com.starbaby_03.utils.beautyUtils;
import com.starbaby_03.utils.galleryUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.AsyncTask.Status;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class mapShowMin2 extends Activity implements
		LazyScrollView.OnScrollListener,OnClickListener {
	private LazyScrollView lazyScrollView;
	private LinearLayout waterfall_container;
	private ArrayList<LinearLayout> linearLayouts;// 列布局
	private LinearLayout progressbar;// 进度条
	private TextView loadtext;// 底部加载view
	private ImageDownLoadAsyncTask asyncTask;
	private int current_page = 0;// 页码
	private int count = 6;// 每页显示的个数
	private int column = 2;// 显示列数
	private int item_width;// 每一个item的宽度
	private ArrayList<String> pathList;// 存放相册下图片的url
	private ImageButton iBnt1,iBnt2,iBnt3;
	/***
	 * init view
	 */
	public void initView() {
		setContentView(R.layout.gallery_showmin2);
		lazyScrollView = (LazyScrollView) findViewById(R.id.waterfall_scroll);
		lazyScrollView.getView();
		lazyScrollView.setOnScrollListener(this);
		waterfall_container = (LinearLayout) findViewById(R.id.waterfall_container);
		progressbar = (LinearLayout) findViewById(R.id.progressbar);
		loadtext = (TextView) findViewById(R.id.loadtext);
		item_width = getWindowManager().getDefaultDisplay().getWidth() / column;
		linearLayouts = new ArrayList<LinearLayout>();
		iBnt1=(ImageButton) findViewById(R.id.gallery_showmin2_imagebutton1);//返回
		iBnt2=(ImageButton) findViewById(R.id.gallery_showmin2_imagebutton3);//拍照
		iBnt3 = (ImageButton) findViewById(R.id.gallery_showmin2_imagebutton2);//导入图片
		// 添加三列到waterfall_container
		for (int i = 0; i < column; i++) {
			LinearLayout layout = new LinearLayout(this);
			LinearLayout.LayoutParams itemParam = new LinearLayout.LayoutParams(
					item_width, LayoutParams.WRAP_CONTENT);
			layout.setOrientation(LinearLayout.VERTICAL);
			layout.setLayoutParams(itemParam);
			linearLayouts.add(layout);
			waterfall_container.addView(layout);
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		initView();
		listener();
		Intent intent = getIntent();
		String str = intent.getStringExtra("url");
		System.out.print(str);
		getAllFiles(new File(str));
		// 第一次加载
		addImage(current_page, count);
	}

	private void listener() {
		// TODO Auto-generated method stub
		iBnt1.setOnClickListener(this);
		iBnt2.setOnClickListener(this);
		iBnt3.setOnClickListener(this);
	}

	/***
	 * 加载更多
	 * 
	 * @param current_page
	 * @param count
	 */
	private void addImage(int current_page, int count) {
		int j = 0;
		int imagecount = pathList.size();
		for (int i = current_page * count; i < count * (current_page + 1)
				&& i < imagecount; i++) {
			addBitMapToImage(pathList.get(i), j, i);
			j++;
			if (j >= column)
				j = 0;
		}

	}

	/***
	 * 添加图片到相应image
	 * 
	 * @param string
	 *            图片名称
	 * @param j
	 *            列
	 * @param i
	 *            图片下标
	 */
	private void addBitMapToImage(String imageName, int j, int i) {
		String text = text(i);
		ImageView imageView = getImageview(imageName);
		TextView tv = new TextView(this);
		asyncTask = new ImageDownLoadAsyncTask(this, imageName, imageView,
				item_width, tv, text);
		asyncTask.setProgressbar(progressbar);
		asyncTask.setLoadtext(loadtext);
		asyncTask.execute();
		imageView.setTag(i);
		// 添加相应view
		ImageView img = new ImageView(this);
		Bitmap bit = BitmapFactory.decodeResource(getResources(),
				R.drawable.bt);
		img.setImageBitmap(bit);
		if (i == 0) {
			linearLayouts.get(j).addView(img);
		} else {
			linearLayouts.get(j).addView(imageView);
			linearLayouts.get(j).addView(tv);
		}
		imageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(mapShowMin2.this, "您点击了" + v.getTag() + "个Item",
						Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(mapShowMin2.this,showPic.class);
				intent.putExtra("mapShowMin2_url", pathList.get((Integer) v.getTag()).toString());
				startActivity(intent);
			}
		});
	}

	/**
	 * 获取imageview的描述，可以为空
	 * 
	 */
	public String text(int i) {
		String text = null;
		if(beautyUtils.spPic.contains("/mnt"+pathList.get(i).toString())){
			text = beautyUtils.spPic.getString("/mnt"+pathList.get(i).toString(), "");
		}
		return text;
	}

	/***
	 * 获取imageview
	 * 
	 * @param imageName
	 * @return
	 */
	public ImageView getImageview(String imageName) {
		BitmapFactory.Options options = getBitmapBounds(imageName);
		// 创建显示图片的对象
		ImageView imageView = new ImageView(this);
		LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.FILL_PARENT);
		imageView.setLayoutParams(layoutParams);
		imageView.setMinimumHeight(options.outHeight);
		imageView.setMinimumWidth(options.outWidth);
		Log.e("options.outHeight=", options.outHeight + "");
		Log.e("options.outWidth=", options.outWidth + "");
		imageView.setPadding(2, 0, 2, 2);
		// imageView.setBackgroundResource(R.drawable.image_border);
		if (options != null)
			options = null;
		return imageView;
	}

	/***
	 * 
	 * 获取相应图片的 BitmapFactory.Options
	 */
	public BitmapFactory.Options getBitmapBounds(String imageName) {
		int h, w;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;// 只返回bitmap的大小，可以减少内存使用，防止OOM.
		BufferedInputStream is = null;
		try {
			is = new BufferedInputStream(new FileInputStream(imageName));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BitmapFactory.decodeStream(is, null, options);
		return options;

	}

	@Override
	public void onBottom() {
		addImage(++current_page, count);

	}

	@Override
	public void onTop() {

	}

	@Override
	public void onScroll() {

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
			pathList.add("this is head");
			for (File f : files) {
				if (f.isDirectory()) {
					if (!f.getName().toString().matches("^\\..*")) {
						getAllFiles(f);
					}
				} else {
					pathList.add(f.getAbsolutePath());
				}
			}
			galleryUtils.picList = pathList;
			Log.e("picList=", pathList+"");
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.gallery_showmin2_imagebutton1:
			this.finish();
			break;
		case R.id.gallery_showmin2_imagebutton3:
			startActivity(new Intent(this,mCamera.class));
			this.finish();
			break;
		case R.id.gallery_showmin2_imagebutton2:
			Intent intent2 = new Intent(this, addPic.class);
			startActivity(intent2);
			this.finish();
			break;
		}
	}

}
