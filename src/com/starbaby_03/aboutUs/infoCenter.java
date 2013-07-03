package com.starbaby_03.aboutUs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.starbaby_03.R;
import com.starbaby_03.aboutUs.infoCenter.StaggeredAdapter.ViewHolder;
import com.starbaby_03.main.HotInfo;
import com.starbaby_03.main.Helper;
import com.starbaby_03.main.ImageFetcher;
import com.starbaby_03.main.MainActivity;
import com.starbaby_03.main.ScaleImageView;
import com.starbaby_03.main.XListView;
import com.starbaby_03.main.XListView.IXListViewListener;
import com.starbaby_03.net.AsyncHttpGet;
import com.starbaby_03.net.DefaultThreadPool;
import com.starbaby_03.net.RequestParameter;
import com.starbaby_03.net.RequestResultCallback;
import com.starbaby_03.utils.JsonObject;
import com.starbaby_03.utils.aboutUsUtils;
import com.starbaby_03.utils.beautyUtils;
import com.starbaby_03.utils.contentUtils;
import com.starbaby_03.utils.meshImgUrl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.AsyncTask.Status;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class infoCenter extends Activity implements IXListViewListener,
		OnClickListener, OnItemClickListener {
	private LayoutInflater inflate;
	private ViewPager pager;
	private List<View> list;
	private View view1, view2;
	private ImageButton iBnt1;
	public static ArrayList<File> listFile;
	private static ArrayList<aboutUsUtils> aboutUsUtils_list;
	private int albumsize;// ���Ϸ��ص�������
	private int currentPag = 0;// 0:��һ��viewpage ��ʾ��� 1:�ڶ���viewpage ��ʾͼƬ
	private GridView gv;
	private XListView mAdapterView = null;
	private StaggeredAdapter mAdapter = null;
	ContentTask task = new ContentTask(this, 2);
	private int currIndex = 0;// ��ǰҳ�����
	private ImageFetcher mImageFetcher;
	private int currentPage = 0;
	private String currentUrl = "";// ��ǰ���� ���͵Ľӿ�
	private String shortUrl = "";
	public ArrayList<HotInfo> duitangs;
	private int new_current_page_down = 1;//���� ����ǰҳ�棬������������ӿڻ�ȡ���ݡ�
	private int new_current_page_up = 1;//���� ����ǰҳ�棬������������ӿڻ�ȡ���ݡ�
	private int FLAG = 1;
	/**
	 * ����µ�ͼƬ��ȡ
	 * 
	 * @author Administrator
	 * 
	 */
	public class ContentTask extends AsyncTask<String, Integer, List<HotInfo>> {
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
			if (mType == 1) {// ����
				mAdapter.addItemTop(result);
				mAdapter.notifyDataSetChanged();
				mAdapterView.stopRefresh();
			} else if (mType == 2) {//���ظ���
				mAdapterView.stopLoadMore();
				mAdapter.addItemLast(result);
				mAdapter.notifyDataSetChanged();
			} else if(mType == 3){//refresh
				mAdapterView.stopLoadMore();
				mAdapter.addItemRefresh(result);
				mAdapter.notifyDataSetChanged();
			}
		}

		@Override
		protected void onPreExecute() {
		}

		public List<HotInfo> parseNewsJSON(String url) throws IOException {
			duitangs = new ArrayList<HotInfo>();
			String result = "";
			JSONArray picJson = null;
			JSONArray blogsJson2 = null;
			if (Helper.checkConnection(mContext)) {
				try {
					result = Helper.getStringFromUrl(url);
					Log.e("json222=", result);
				} catch (IOException e) {
					Log.e("IOException is : ", e.toString());
					e.printStackTrace();
					return duitangs;
				}
			}
			if (null != result) {
				try {
					JSONObject json = new JSONObject(result);
					JSONArray datalistArray = json.getJSONArray("datalist");
					for (int i = 0; i < datalistArray.length(); i++) {
						HotInfo info = new HotInfo();
						JSONObject imgObject = datalistArray.getJSONObject(i);
						JSONArray commentlistArray = imgObject
								.getJSONArray("commentlist");
						ArrayList<String> authorList = new ArrayList<String>();
						ArrayList<String> commentList = new ArrayList<String>();
						for (int j = 0; j < commentlistArray.length(); j++) {
							JSONObject lastJson = commentlistArray
									.getJSONObject(j);
							authorList.add(lastJson.isNull("author") ? ""
									: lastJson.getString("author"));
							commentList.add(lastJson.isNull("message") ? ""
									: lastJson.getString("message"));
						}
						info.setAuthorList(authorList);
						info.setCommentList(commentList);
						info.setIsrc(imgObject.isNull("img") ? "" : imgObject
								.getString("img"));
						info.setHeight(170);
						duitangs.add(info);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return duitangs;
		}
	}

	/**
	 * �������
	 * 
	 * @param pageindex
	 * @param type
	 *            1Ϊ����ˢ�� 2Ϊ���ظ���
	 */
	private void AddItemToContainer(int pageindex, int type,int page) {
		if (task.getStatus() != Status.RUNNING) {
			// �ӿڵ�ַ
			String url = shortUrl+page;
			ContentTask task = new ContentTask(this, type);
			Log.e("url=", url);
			task.execute(url);
		}
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
				holder.imageView.setOnClickListener(infoCenter.this);
				holder.contentView = (TextView) convertView
						.findViewById(R.id.news_name);
				holder.timeView = (TextView) convertView
						.findViewById(R.id.news_note);
				convertView.setTag(holder);
			}
			holder = (ViewHolder) convertView.getTag();
			holder.imageView.setImageWidth(duitangInfo.getWidth());
			holder.imageView.setImageHeight(duitangInfo.getHeight());
			if (duitangInfo.getAuthorList().size() > 1) {
				holder.contentView.setText(duitangInfo.getAuthorList().get(0)
						+ duitangInfo.getCommentList().get(0));
				holder.timeView.setText(duitangInfo.getAuthorList().get(1)
						+ duitangInfo.getCommentList().get(1));
			} else if (duitangInfo.getAuthorList().size() == 1) {
				holder.contentView.setText(duitangInfo.getAuthorList().get(0)
						+ duitangInfo.getCommentList().get(0));
				holder.timeView.setVisibility(8);
			} else if (duitangInfo.getAuthorList().size() == 0) {
				holder.contentView.setVisibility(8);
				holder.timeView.setVisibility(8);
			}
			mImageFetcher.loadImage(duitangInfo.getIsrc(), holder.imageView);
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
		public void addItemRefresh(List<HotInfo> datas){
			if(FLAG == 2){
				mInfos.clear();
				mInfos.addAll(datas);
			}else if(FLAG == 1){
				mInfos.addAll(datas);
				mInfos.clear();
			}
			
		}
		public void addItemLast(List<HotInfo> datas) {
			mInfos.addAll(datas);
		}

		public void addItemTop(List<HotInfo> datas) {
			for (HotInfo info : datas) {
				mInfos.addFirst(info);
			}
		}
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.aboutus_infocenter);
		beautyUtils.layoutWidth = getWindowManager().getDefaultDisplay()
				.getWidth();
		inflate = (LayoutInflater) this
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		init();
		listener();
		InitViewPager();
		gv.setOnItemClickListener(this);
		ViewHolder holder = null;
	}

	private void listener() {
		// TODO Auto-generated method stub
		iBnt1.setOnClickListener(this);
	}

	private void init() {
		// TODO Auto-generated method stub
		iBnt1 = (ImageButton) findViewById(R.id.aboutus_infocenter_ibnt1);
	}

	// ��һ�ε���ˢ��
	@Override
	protected void onResume() {
		super.onResume();
		mImageFetcher.setExitTasksEarly(false);
		mAdapterView.setAdapter(mAdapter);
		int new_current_page = 1;
		AddItemToContainer(currentPage, 3,new_current_page);
	}

	// ����ˢ��
	@Override
	public void onRefresh() {
		new_current_page_down = new_current_page_down-1;
		AddItemToContainer(++currentPage, 1,new_current_page_down);
	}

	// ��������
	@Override
	public void onLoadMore() {
		new_current_page_up = new_current_page_up+1;
		AddItemToContainer(++currentPage, 2, new_current_page_up);
	}

	private void InitViewPager() {
		inflateGrideView();
		
		view2 = inflate.inflate(R.layout.main_lay1, null);//����չʾͼƬ
		mAdapterView = (XListView) view2.findViewById(R.id.list1);
		mAdapterView.setPullLoadEnable(true);
		mAdapterView.setXListViewListener(this);
		mAdapter = new StaggeredAdapter(this, mAdapterView);
		mImageFetcher = new ImageFetcher(this, 240);
		mImageFetcher.setLoadingImage(R.drawable.empty_photo);

		pager = (ViewPager) findViewById(R.id.vPager);
		list = new ArrayList<View>();
		list.add(view1);
		list.add(view2);
		pager.setAdapter(new MyPagerAdapter(list));
		Log.e("albumsize=", albumsize+"");
		if (albumsize > 1) {
			currentPag = 0;
		} else {
			currentPag = 1;
		}
		pager.setCurrentItem(0);
	}

	/**
	 * JSON���� ��ȡ�������
	 * 
	 * @param uid
	 *            �û�uid
	 * @param cur_page
	 *            ��Ӧ�������ĵ�ǰҳ
	 * @return
	 */
	private void getFrame(int uid, int cur_page) {
		aboutUsUtils_list = new ArrayList<aboutUsUtils>();
		List<RequestParameter> parameterList = new ArrayList<RequestParameter>();
		AsyncHttpGet get = new AsyncHttpGet(null, contentUtils.frameList + uid
				+ "/" + cur_page, parameterList, new RequestResultCallback() {
			public void onSuccess(Object o) {
				final String result = (String) o;
				infoCenter.this.runOnUiThread(new Runnable() {
					public void run() {
						try {
							JSONObject json = new JSONObject(result);
							Log.e("json=", json + "");
							Log.e("result=", result + "");
							albumsize = new JsonObject().albumsize(result);
							JSONArray img = json.getJSONArray("datalist");
							if (albumsize > 1) {
								for (int i = 0; i < img.length(); i++) {
									JSONObject imgUrl = img.getJSONObject(i);
									String imgList = imgUrl.getString("img");
									String albumnameList = imgUrl
											.getString("albumname");
									int albumidList = imgUrl.getInt("albumid");
									aboutUsUtils list = new aboutUsUtils(
											albumnameList, imgList, albumidList);
									aboutUsUtils_list.add(list);
									pager.setCurrentItem(0);// չʾ���
								}
							} else {
								int albumid = json.getInt("json");
								currentUrl = contentUtils.photoList + albumid
										+ "/" + 1;
								onResume();
								pager.setCurrentItem(1);// ���Ϊ1����չʾ����ͼƬ���������Ϊ��
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
						Log.e("aboutUsUtils_list.get(0).getAlbumid()=",aboutUsUtils_list.get(0).getAlbumid()+"");
						gv.setAdapter(new MyGridView(infoCenter.this,
								aboutUsUtils_list));
					}
				});
			}

			@Override
			public void onFail(Exception e) {
				// TODO Auto-generated method stub

			}
		});
		DefaultThreadPool.getInstance().execute(get);
		// return aboutUsUtils_list;
	}

	/**
	 * ��gridview��Ӷ�Ӧ������ : name picurl
	 */
	private void inflateGrideView() {
		view1 = inflate.inflate(R.layout.aboutus_infocenter_store, null);
		gv = (GridView) view1
				.findViewById(R.id.aboutus_infocenter_store_gridview);
		
		getFrame(contentUtils.uid, 1);
		shortUrl = contentUtils.photoList + "323485" + "/";

	}

	/**
	 * GridView������
	 */
	class MyGridView extends BaseAdapter {
		private ArrayList<aboutUsUtils> aboutUsUtils;
		private Context mContext;
		private LayoutInflater inflate;

		public MyGridView(Context mContext, ArrayList<aboutUsUtils> aboutUsUtils) {
			this.mContext = mContext;
			this.aboutUsUtils = aboutUsUtils;
			inflate = (LayoutInflater) mContext
					.getSystemService(LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return aboutUsUtils.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = inflate.inflate(
					R.layout.aboutus_infocenter_store_frame, null);
			TextView name = (TextView) convertView
					.findViewById(R.id.aboutus_infocenter_store_frame_textview);
			ImageView frameView = (ImageView) convertView
					.findViewById(R.id.aboutus_infocenter_store_frame_imageview);
			name.setText(aboutUsUtils.get(position).name);
			Bitmap bit = new meshImgUrl().returnBitMap(aboutUsUtils
					.get(position).picUrl);
			frameView.setImageBitmap(bit);
			return convertView;
		}

	}

	/**
	 * ViewPager������
	 */
	class MyPagerAdapter extends PagerAdapter {
		public List<View> list;

		public MyPagerAdapter(List<View> list) {
			this.list = list;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(list.get(arg1));
		}

		@Override
		public void finishUpdate(View arg0) {
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(list.get(arg1), 0);
			return list.get(arg1);
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
	 * �����¼� GridViewClick gridview�����¼�
	 */

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.news_pic:
			Toast.makeText(this, "��������", 1000).show();
			break;
		case R.id.aboutus_infocenter_ibnt1:
			startActivity(new Intent(this, MainActivity.class));
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		duitangs.clear();
		int albumid = aboutUsUtils_list.get(arg2).getAlbumid();
		shortUrl = contentUtils.photoList + albumid + "/";
		FLAG = 2;
		onResume();
		pager.setCurrentItem(1);
	}
}
