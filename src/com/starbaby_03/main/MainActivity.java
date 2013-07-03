package com.starbaby_03.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.starbaby_03.R;
import com.starbaby_03.aboutUs.infoCenter;
import com.starbaby_03.camera.mCamera;
import com.starbaby_03.info.user_enter;
import com.starbaby_03.main.XListView.IXListViewListener;
import com.starbaby_03.main.XListView2.IXListViewListener2;
import com.starbaby_03.scroll.scroll;
import com.starbaby_03.utils.Utils;
import com.starbaby_03.utils.beautyUtils;
import com.starbaby_03.utils.contentUtils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * Tab页面手势滑动切换以及动画效果
 * 
 * @author D.Winter
 * 
 */
public class MainActivity extends FragmentActivity implements
		IXListViewListener, IXListViewListener2, OnClickListener {
	// ViewPager是google SDk中自带的一个附加包的一个类，可以用来实现屏幕间的切换。
	// android-support-v4.jar
	private ViewPager mPager;// 页卡内容
	private List<View> listViews; // Tab页面列表
	private ImageView cursor;// 动画图片
	private TextView t1, t2;// 页卡头标
	private int offset = 0;// 动画图片偏移量
	private int currIndex = 0;// 当前页卡编号
	private int bmpW;// 动画图片宽度
	private ImageButton iBnt1, iBnt2, iBnt3;
	int scroll_height;
	LayoutInflater inflate;
	private ImageFetcher mImageFetcher;
	private XListView mAdapterView = null;
	private XListView2 mAdapterView2 = null;
	private StaggeredAdapter mAdapter = null;
	private StaggeredAdapter2 mAdapter2 = null;
	private int currentPage = 0;
	private Uri originalUri;
	private PopupWindow pop;
	public View view;
	private View view2;
	private int new_current_page = 1;//最新 ：当前页面，用于下拉，上拉，向接口获取数据。
	private class ContentTask extends AsyncTask<String, Integer, List<HotInfo>> {

		private Context mContext;
		private int mType = 1;

		public ContentTask(Context context, int type) {
			super();
			mContext = context;
			mType = type;
		}

		@Override
		protected List<HotInfo> doInBackground(String... params) {
			try {
				return parseNewsJSON(params[0]);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(List<HotInfo> result) {
			if (mType == 1) {// 下拉
				mAdapter.addItemTop(result);
				mAdapter.notifyDataSetChanged();
				mAdapterView.stopRefresh();
			} else if (mType == 2) {// 加载更多
				mAdapterView.stopLoadMore();
				mAdapter.addItemLast(result);
				mAdapter.notifyDataSetChanged();
			}

		}

		@Override
		protected void onPreExecute() {
		}

		public List<HotInfo> parseNewsJSON(String url) throws IOException {
			List<HotInfo> duitangs = new ArrayList<HotInfo>();
			String json = "";
			JSONArray picJson = null;
			if (Helper.checkConnection(mContext)) {
				try {
					json = Helper.getStringFromUrl(url);
					Log.e("json=", json);
				} catch (IOException e) {
					Log.e("IOException is : ", e.toString());
					e.printStackTrace();
					return duitangs;
				}
			}
			try {
				if (null != json) {
					picJson = new JSONArray(json);
					/**
					 * 最热照片JSON
					 * 
					 */
					for (int i = 0; i < picJson.length(); i++) {
						HotInfo info = new HotInfo();
						JSONObject picObj = picJson.getJSONObject(i);
						JSONArray commentArray = picObj
								.getJSONArray("commentlist");
						Log.e("commentArray=", commentArray + "");
						ArrayList<String> authorList = new ArrayList<String>();
						ArrayList<String> commentList = new ArrayList<String>();
						for (int j = 0; j < commentArray.length(); j++) {
							JSONObject commentObj = commentArray
									.getJSONObject(j);
							authorList.add(commentObj.isNull("author") ? ""
									: commentObj.getString("author"));
							commentList.add(commentObj.isNull("message") ? ""
									: commentObj.getString("message"));
						}
						info.setAuthorList(authorList);
						info.setCommentList(commentList);
						info.setIsrc(picObj.isNull("img") ? "" : picObj
								.getString("img"));
						info.setHeight(170);
						duitangs.add(info);
					}
				}
				// }
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return duitangs;
		}
	}
	//页卡2 最新接口
	private class ContentTask2 extends
			AsyncTask<String, Integer, List<NewInfo>> {

		private Context mContext;
		private int mType = 1;
		public ContentTask2(Context context, int type) {
			super();
			mContext = context;
			mType = type;
		}

		@Override
		protected List<NewInfo> doInBackground(String... params) {
			try {
				return parseNewsJSON(params[0]);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(List<NewInfo> result) {
			if (mType == 1) {// 下拉
				mAdapter2.addItemTop(result);
				mAdapter2.notifyDataSetChanged();
				mAdapterView2.stopRefresh();
			} else if (mType == 2) {// 加载更多
				mAdapterView2.stopLoadMore();
				mAdapter2.addItemLast(result);
				mAdapter2.notifyDataSetChanged();
			}
		}

		@Override
		protected void onPreExecute() {
		}

		public List<NewInfo> parseNewsJSON(String url) throws IOException {
			List<NewInfo> duitangs = new ArrayList<NewInfo>();
			String json = "";
			// 最热
			JSONObject newObject = null;
			JSONArray newArray = null;
			if (Helper.checkConnection(mContext)) {
				try {
					json = Helper.getStringFromUrl(url);
					Log.e("json2=", json);
				} catch (IOException e) {
					Log.e("IOException is : ", e.toString());
					e.printStackTrace();
					return duitangs;
				}
			}
			try {
				if (null != json) {
					/**
					 * 最新
					 */
					newObject = new JSONObject(json);
					newArray = newObject.getJSONArray("datalist");
					for (int i = 0; i < newArray.length(); i++) {
						NewInfo info = new NewInfo();
						JSONObject picObj = newArray.getJSONObject(i);
						JSONArray commentArray = picObj
								.getJSONArray("commentlist");
						ArrayList<String> authorList = new ArrayList<String>();
						ArrayList<String> messageList = new ArrayList<String>();
						for (int j = 0; j < commentArray.length(); j++) {
							JSONObject commentObj = commentArray
									.getJSONObject(j);
							authorList.add(commentObj.isNull("author") ? ""
									: commentObj.getString("author"));
							messageList.add(commentObj.isNull("message") ? ""
									: commentObj.getString("message"));
						}
						info.setAuthorList(authorList);
						info.setCommentList(messageList);
						info.setIsrc(picObj.isNull("img") ? "" : picObj
								.getString("img"));
						info.setHeight(300);
						duitangs.add(info);
						Log.e("duitangs=", duitangs + "");
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return duitangs;
		}
	}

	/**
	 * 添加内容
	 * 
	 * @param pageindex
	 * @param type
	 *            1为下拉刷新 2为加载更多
	 */
	private void AddItemToContainer(int pageindex, int type , int page) {
		// 接口地址
		String newurl = null;
		String hoturl = null;
		// 页卡为2 最新照片
		newurl = contentUtils.newUrl + page;
		ContentTask2 task2 = new ContentTask2(this, type);
		task2.execute(newurl);
		// 页卡为1 最热照片
		hoturl = contentUtils.hotUrl;
		ContentTask task = new ContentTask(this, type);
		task.execute(hoturl);
	}

	public class StaggeredAdapter extends BaseAdapter {
		private Context mContext;
		private LinkedList<HotInfo> mInfos;
		private XListView mListView;

		public StaggeredAdapter(Context context, XListView xListView) {
			mContext = context;
			mInfos = new LinkedList<HotInfo>();
			mListView = xListView;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder;
			HotInfo duitangInfo = mInfos.get(position);

			if (convertView == null) {
				LayoutInflater layoutInflator = LayoutInflater.from(parent
						.getContext());
				convertView = layoutInflator.inflate(R.layout.main_infos_list,
						null);
				holder = new ViewHolder();
				holder.imageView = (ScaleImageView) convertView
						.findViewById(R.id.news_pic);
				holder.contentView = (TextView) convertView
						.findViewById(R.id.news_name);
				holder.timeView = (TextView) convertView
						.findViewById(R.id.news_note);
				convertView.setTag(holder);
			}
			holder = (ViewHolder) convertView.getTag();
			holder.imageView.setImageWidth(duitangInfo.getWidth());
			holder.imageView.setImageHeight(duitangInfo.getHeight());
			if (duitangInfo.getAuthorList().size() == 1
					&& duitangInfo.getCommentList().size() == 1) {
				holder.contentView.setText(duitangInfo.getAuthorList().get(0)
						+ ":" + duitangInfo.getCommentList().get(0));
			} else if (duitangInfo.getAuthorList().size() > 1
					&& duitangInfo.getCommentList().size() > 1) {
				holder.contentView.setText(duitangInfo.getAuthorList().get(0)
						+ ":" + duitangInfo.getCommentList().get(0));
				holder.timeView.setText(duitangInfo.getAuthorList().get(1)
						+ ":" + duitangInfo.getCommentList().get(1));
			}
			if(duitangInfo.getIsrc().length()!=0){
				mImageFetcher.loadImage(duitangInfo.getIsrc(), holder.imageView);
			}
			return convertView;
		}

		class ViewHolder {
			ScaleImageView imageView;
			TextView contentView;
			TextView timeView;

		}

		@Override
		public int getCount() {
			return mInfos.size();
		}

		@Override
		public Object getItem(int arg0) {
			return mInfos.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		public void addItemLast(List<HotInfo> datas) {
			mInfos.clear();
			mInfos.addAll(datas);
		}

		public void addItemTop(List<HotInfo> datas) {
			mInfos.clear();
			mInfos.addAll(datas);
		}
	}

	public class StaggeredAdapter2 extends BaseAdapter {
		private Context mContext;
		private LinkedList<NewInfo> mInfos;
		private XListView2 mListView2;

		public StaggeredAdapter2(Context context, XListView2 xListView2) {
			mContext = context;
			mInfos = new LinkedList<NewInfo>();
			mListView2 = xListView2;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder;
			NewInfo duitangInfo = mInfos.get(position);

			if (convertView == null) {
				LayoutInflater layoutInflator = LayoutInflater.from(parent
						.getContext());
				convertView = layoutInflator.inflate(R.layout.main_infos_list,
						null);
				holder = new ViewHolder();
				holder.imageView = (ScaleImageView) convertView
						.findViewById(R.id.news_pic);
				holder.contentView = (TextView) convertView
						.findViewById(R.id.news_name);
				holder.timeView = (TextView) convertView
						.findViewById(R.id.news_note);
				convertView.setTag(holder);
			}

			holder = (ViewHolder) convertView.getTag();
			holder.imageView.setImageWidth(duitangInfo.getWidth());
			holder.imageView.setImageHeight(duitangInfo.getHeight());
			if (duitangInfo.getAuthorList().size() == 1
					&& duitangInfo.getCommentList().size() == 1) {
				holder.contentView.setText(duitangInfo.getAuthorList().get(0)
						+ ":" + duitangInfo.getCommentList().get(0));
			} else if (duitangInfo.getAuthorList().size() > 1
					&& duitangInfo.getCommentList().size() > 1) {
				holder.contentView.setText(duitangInfo.getAuthorList().get(0)
						+ ":" + duitangInfo.getCommentList().get(0));
				holder.timeView.setText(duitangInfo.getAuthorList().get(1)
						+ ":" + duitangInfo.getCommentList().get(1));
			}
			if(duitangInfo.getIsrc().length()!=0){
				mImageFetcher.loadImage(duitangInfo.getIsrc(), holder.imageView);
			}
			return convertView;
		}

		class ViewHolder {
			ScaleImageView imageView;
			TextView contentView;
			TextView timeView;

		}

		@Override
		public int getCount() {
			return mInfos.size();
		}

		@Override
		public Object getItem(int arg0) {
			return mInfos.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		public void addItemLast(List<NewInfo> datas) {
			mInfos.addAll(datas);
		}

		public void addItemTop(List<NewInfo> datas) {
			for (NewInfo info : datas) {
				mInfos.addFirst(info);
			}
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main_appmain);
		beautyUtils.layoutWidth = getWindowManager().getDefaultDisplay()
				.getWidth();
		InitImageView();
		InitTextView();
		InitViewPager();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return true;
	}

	// 自动刷新
	@Override
	protected void onResume() {
		super.onResume();
		mImageFetcher.setExitTasksEarly(false);
		mAdapterView2.setAdapter(mAdapter2);
		mAdapterView.setAdapter(mAdapter);
		AddItemToContainer(currentPage, 2 ,new_current_page);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

	}

	// 下拉刷新
	@Override
	public void onRefresh() {
		new_current_page = new_current_page+1;
		AddItemToContainer(++currentPage, 1 ,new_current_page);
		Log.e("currentPage=", currentPage + "");
	}

	// 加载更多
	@Override
	public void onLoadMore() {
		new_current_page = new_current_page-1;
		AddItemToContainer(++currentPage, 2 ,new_current_page);
		Log.e("currentPage2=", currentPage + "");
	}

	/**
	 * 初始化头标
	 */
	private void InitTextView() {
		t1 = (TextView) findViewById(R.id.text1);
		t2 = (TextView) findViewById(R.id.text2);
		iBnt1 = (ImageButton) findViewById(R.id.main_appmain_imagebutton1);
		iBnt2 = (ImageButton) findViewById(R.id.main_appmain_imagebutton2);
		iBnt3 = (ImageButton) findViewById(R.id.main_appmain_imagebutton3);
		t1.setOnClickListener(new MyOnClickListener(0));
		t2.setOnClickListener(new MyOnClickListener(1));
		iBnt1.setOnClickListener(this);
		iBnt2.setOnClickListener(this);
		iBnt3.setOnClickListener(this);
	}

	/**
	 * 初始化ViewPager
	 */
	private void InitViewPager() {
		inflate = (LayoutInflater) this
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		view = inflate.inflate(R.layout.main_lay1, null);// 最热
		view2 = inflate.inflate(R.layout.main_lay2, null);// 最新

		mAdapterView = (XListView) view.findViewById(R.id.list1);
		mAdapterView.setPullLoadEnable(true);
		mAdapterView.setXListViewListener(this);
		mAdapter = new StaggeredAdapter(this, mAdapterView);
		mImageFetcher = new ImageFetcher(this, 240);
		mImageFetcher.setLoadingImage(R.drawable.empty_photo);

		mAdapterView2 = (XListView2) view2.findViewById(R.id.list2);
		mAdapterView2.setPullLoadEnable(true);
		mAdapterView2.setXListViewListener(this);
		mAdapter2 = new StaggeredAdapter2(this, mAdapterView2);
		mImageFetcher = new ImageFetcher(this, 240);
		mImageFetcher.setLoadingImage(R.drawable.empty_photo);

		mPager = (ViewPager) findViewById(R.id.vPager);
		listViews = new ArrayList<View>();
		listViews.add(view);
		listViews.add(view2);
		mPager.setAdapter(new MyPagerAdapter(listViews));
		mPager.setCurrentItem(currIndex);
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	/**
	 * 初始化动画
	 */
	private void InitImageView() {
		cursor = (ImageView) findViewById(R.id.cursor);
		Log.e("bmpW=", bmpW + "");
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;// 获取分辨率宽度
		Bitmap bit1 = BitmapFactory.decodeResource(getResources(),
				R.drawable.app_main_course);
		Bitmap bit2 = Bitmap.createScaledBitmap(bit1, screenW / 2,
				bit1.getHeight(), false);
		cursor.setImageBitmap(bit2);
		bmpW = bit2.getWidth();
		offset = (screenW / 2 - bmpW) / 2;// 计算偏移量
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		cursor.setImageMatrix(matrix);// 设置动画初始位置
	}

	/**
	 * ViewPager适配器
	 */
	public class MyPagerAdapter extends PagerAdapter {
		public List<View> mListViews;

		public MyPagerAdapter(List<View> mListViews) {
			this.mListViews = mListViews;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(mListViews.get(arg1));
		}

		@Override
		public void finishUpdate(View arg0) {
		}

		@Override
		public int getCount() {
			return mListViews.size();
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(mListViews.get(arg1), 0);
			return mListViews.get(arg1);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == (arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}
	}

	/**
	 * 头标点击监听
	 */
	public class MyOnClickListener implements View.OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			mPager.setCurrentItem(index);
		}
	};

	/**
	 * 页卡切换监听
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {

		int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量

		// int two = one * 2;// 页卡1 -> 页卡3 偏移量

		@Override
		public void onPageSelected(int arg0) {
			Animation animation = null;
			switch (arg0) {
			case 0:
				if (currIndex == 1) {
					animation = new TranslateAnimation(one, 0, 0, 0);
				}
				break;
			case 1:
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, one, 0, 0);
				}
				break;
			}
			Log.e("one=", one + "");
			Log.e("offset=", offset + "");
			currIndex = arg0;
			animation.setFillAfter(true);// True:图片停在动画结束位置
			animation.setDuration(300);
			cursor.startAnimation(animation);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.main_appmain_imagebutton1:
			showPopWindows();
			break;
		case R.id.main_appmain_imagebutton2:// 个人info
			startActivity(new Intent(this, user_enter.class));
			break;
		case R.id.main_appmain_imagebutton3:// 拍照
			startActivity(new Intent(this, mCamera.class));
			break;
		case R.id.main_popwindows_button1: // 拍照
			Intent intent = new Intent(MainActivity.this, mCamera.class);
			startActivity(intent);
			pop.dismiss();
			break;
		case R.id.main_popwindows_button2:// 从本地相册获取
			ShowPickDialog();
			pop.dismiss();
			break;
		case R.id.main_popwindows_button3:// 取消
			pop.dismiss();
			break;
		}
	}

	public void showPopWindows() {
		Button bnt1, bnt2, bnt3;
		Context mContext = MainActivity.this;
		LayoutInflater inflate = (LayoutInflater) mContext
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		View popWindows = inflate.inflate(R.layout.main_popwindows, null);
		pop = new PopupWindow(popWindows,
				android.app.ActionBar.LayoutParams.WRAP_CONTENT,
				android.app.ActionBar.LayoutParams.WRAP_CONTENT);
		pop.setFocusable(true);// 设置PopupWindow可获得焦点
		pop.setOutsideTouchable(true);// 设置非PopupWindow区域不可触摸
		pop.setBackgroundDrawable(new BitmapDrawable());
		bnt1 = (Button) popWindows.findViewById(R.id.main_popwindows_button1);
		bnt2 = (Button) popWindows.findViewById(R.id.main_popwindows_button2);
		bnt3 = (Button) popWindows.findViewById(R.id.main_popwindows_button3);
		pop.showAtLocation(findViewById(R.id.main_appmain_rl), Gravity.BOTTOM
				| Gravity.CENTER_HORIZONTAL, 0, 0);
		bnt1.setFocusable(true);
		bnt2.setFocusable(true);
		bnt3.setFocusable(true);
		bnt1.setOnClickListener(this);
		bnt2.setOnClickListener(this);
		bnt3.setOnClickListener(this);
	}

	/**
	 * 选择提示对话框 ACTION_PICK的使用 重写onActivityResult方法 美化
	 */
	private void ShowPickDialog() {
		Intent intent = new Intent(Intent.ACTION_PICK, null);
		// 设置数据类型
		intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				"image/*");
		startActivityForResult(intent, 5);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == 0)
			return;
		// 美化。打开相册操作
		if (requestCode == 5) {
			// 获得图片的uri
			originalUri = data.getData();
			Log.e("originalUri", originalUri.toString());
			Intent intentUri = new Intent(MainActivity.this, scroll.class);
			intentUri.putExtra("originalUri", originalUri.toString());
			intentUri.putExtra(Utils.INTENT_FLAG, Utils.FULL_IMAGE);
			startActivity(intentUri);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}