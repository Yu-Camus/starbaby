package com.starbaby_03.weibo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.example.starbaby_03.R;
import com.starbaby_03.Gallery.MyAdapter1;
import com.starbaby_03.Gallery.mapStorage;
import com.starbaby_03.main.appMain;
import com.starbaby_03.utils.weiboUtils;
import com.starbaby_03.weibo.PullDownView.OnPullDownListener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.HeaderViewListAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class PullDownActivity extends Activity implements OnPullDownListener,
		OnItemClickListener, OnClickListener {

	private static final int WHAT_DID_LOAD_DATA = 0;
	private static final int WHAT_DID_REFRESH = 1;
	private static final int WHAT_DID_MORE = 2;

	private ListView mListView;
	private ArrayAdapter<String> mAdapter;
	private PullDownView mPullDownView;
	private List<String> mStrings = new ArrayList<String>();
	private List<Map<String, Object>> strings = new ArrayList<Map<String, Object>>();
	private ArrayList<weibo_list> weiboLists;
	private ImageButton imgBnt1, imgBnt2;
	private TextView tv;
	private SimpleDateFormat format = new SimpleDateFormat(
			"yyyy-MM-dd  hh:mm:ss");
	private Date curtime = new Date();
	private String date = format.format(curtime);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weibo_pulldown);

		/*
		 * 1.使用PullDownView 2.设置OnPullDownListener 3.从mPullDownView里面获取ListView
		 */
		mPullDownView = (PullDownView) findViewById(R.id.pull_down_view);
		imgBnt1 = (ImageButton) findViewById(R.id.weibo_pulldown_imagenutton1);
		imgBnt2 = (ImageButton) findViewById(R.id.weibo_pulldown_imagenutton2);
		tv = (TextView) findViewById(R.id.weibo_pulldown_textview);
		mPullDownView.setOnPullDownListener(this);
		imgBnt1.setOnClickListener(this);
		imgBnt2.setOnClickListener(this);
		mListView = mPullDownView.getListView();
		mListView.setDivider(null);
		mListView.setOnItemClickListener(this);
		getData();
		loadData();

		mListView.setAdapter(new MyAdapter(weiboLists, this));

		mPullDownView.enableAutoFetchMore(true, 1);

	}

	private ArrayList<weibo_list> getData() {
		weibo_list op1 = new weibo_list("路飞的船员", date, 1, R.drawable.img_1,
				R.drawable.test2, "路飞");
		weibo_list op2 = new weibo_list("索隆的船员", date, 2, R.drawable.img_2,
				R.drawable.test2, "路飞");
		weibo_list op3 = new weibo_list("索隆的船员", date, 2, R.drawable.img_2,
				R.drawable.test1, "路飞");
		weibo_list op4 = new weibo_list("索隆的船员", date, 2, R.drawable.img_2,
				R.drawable.test2, "路飞");
		weibo_list op5 = new weibo_list("索隆的船员", date, 2, R.drawable.img_2,
				R.drawable.test1, "路飞");
		weibo_list op6 = new weibo_list("索隆的船员", date, 2, R.drawable.img_2,
				R.drawable.test2, "路飞");
		weibo_list op7 = new weibo_list("索隆的船员", date, 2, R.drawable.img_2,
				R.drawable.test1, "路飞");
		weibo_list op9 = new weibo_list("索隆的船员", date, 2, R.drawable.img_2,
				R.drawable.test2, "路飞");
		weibo_list op10 = new weibo_list("索隆的船员", date, 2, R.drawable.img_2,
				R.drawable.test1, "路飞");
		weibo_list op11 = new weibo_list("索隆的船员", date, 2, R.drawable.img_2,
				R.drawable.test2, "路飞");
		weibo_list op12 = new weibo_list("索隆的船员", date, 2, R.drawable.img_2,
				R.drawable.test1, "路飞");
		weiboLists = new ArrayList<weibo_list>();
		weiboLists.add(op1);
		weiboLists.add(op2);
		weiboLists.add(op3);
		weiboLists.add(op4);
		weiboLists.add(op5);
		weiboLists.add(op6);
		weiboLists.add(op7);
		weiboLists.add(op11);
		weiboLists.add(op9);
		weiboLists.add(op10);
		weiboLists.add(op12);
		return weiboLists;

	}

	private void loadData() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				String text="";
				int url=0;
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Message msg = mUIHandler.obtainMessage(WHAT_DID_LOAD_DATA);
				ArrayList<weibo_list> weiboListss = new ArrayList<weibo_list>();
				Intent intent=getIntent();
				if(intent.getExtras().getInt(weiboUtils.weibo_sharePicSucc_key)==weiboUtils.weibo_return_Flag3){
					Log.e("TAG","你第一次进入");
				}else if(intent.getExtras().getInt(weiboUtils.weibo_sharePicSucc_key)==weiboUtils.weibo_sharePicSucc_Flag){
					text=intent.getStringExtra("text");
//					url=Integer.parseInt(intent.getStringExtra("url"));
					weibo_list weibo_list1 = new weibo_list(text, date, 2,
							R.drawable.img_4,R.drawable.test2 , "路飞");
//					weibo_list weibo_list2 = new weibo_list(text, date, 2,
//							R.drawable.img_6, R.drawable.test2, "路飞");
					weiboListss.add(weibo_list1);
//					weiboListss.add(weibo_list2);
					msg.obj = weiboListss;
				}
				
				
				
				msg.sendToTarget();
			}
		}).start();
	}

	// 下拉刷新新内容
	@Override
	public void onRefresh() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Message msg = mUIHandler.obtainMessage(WHAT_DID_REFRESH);
				weibo_list weiboLists1 = new weibo_list("落落怒焰", date, 4,
						R.drawable.img_4, R.drawable.test1, "路飞");
				weibo_list weiboLists2 = new weibo_list("乔巴", date, 4,
						R.drawable.img_5, R.drawable.test2, "路飞");
				ArrayList<weibo_list> weiboLists = new ArrayList<weibo_list>();
				weiboLists.add(weiboLists1);
				weiboLists.add(weiboLists2);
				msg.obj = weiboLists;
				msg.sendToTarget();
			}
		}).start();
	}

	// 上拉，显示更多内容
	@Override
	public void onMore() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Message msg = mUIHandler.obtainMessage(WHAT_DID_MORE);
				weibo_list weiboLists1 = new weibo_list("布鲁克", date, 4,
						R.drawable.img_4, R.drawable.test2, "路飞");
				weibo_list weiboLists2 = new weibo_list("娜米", date, 4,
						R.drawable.img_5, R.drawable.test2, "路飞");
				ArrayList<weibo_list> weiboLists = new ArrayList<weibo_list>();
				weiboLists.add(weiboLists1);
				weiboLists.add(weiboLists2);
				msg.obj = weiboLists;
				msg.sendToTarget();
			}
		}).start();
	}

	private Handler mUIHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case WHAT_DID_LOAD_DATA: {
				if (msg.obj != null) {
					ArrayList<weibo_list> body = (ArrayList<weibo_list>) msg.obj;
					weiboLists.addAll(0, body);
					((MyAdapter) mListView.getAdapter()).notifyDataSetChanged();
				}
				// 诉它数据加载完毕;
				mPullDownView.notifyDidLoad();
				break;
			}
			case WHAT_DID_REFRESH: {
				ArrayList<weibo_list> body = (ArrayList<weibo_list>) msg.obj;
				weiboLists.addAll(0, body);
				((MyAdapter) ((HeaderViewListAdapter) mListView.getAdapter())
						.getWrappedAdapter()).notifyDataSetChanged();
				// 告诉它更新完毕
				mPullDownView.notifyDidRefresh();
				break;
			}

			case WHAT_DID_MORE: {
				ArrayList<weibo_list> body = (ArrayList<weibo_list>) msg.obj;
				weiboLists.addAll(body);
				((MyAdapter) ((HeaderViewListAdapter) mListView.getAdapter())
						.getWrappedAdapter()).notifyDataSetChanged();
				// 告诉它获取更多完毕
				mPullDownView.notifyDidMore();
				break;
			}
			}

		}

	};

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Toast.makeText(this, "啊，你点中我了 " + position, Toast.LENGTH_SHORT).show();
		weiboUtils.FLAG=2;
		Intent intent = new Intent(PullDownActivity.this, titleActivity.class);
		startActivity(intent);
//		this.finish();
	}

	class MyAdapter extends BaseAdapter {
		private Context mContxet;
		private LayoutInflater inflate;
		private ArrayList<weibo_list> weiboLists;

		public MyAdapter(ArrayList<weibo_list> weiboLists, Context context) {
			this.weiboLists = weiboLists;
			this.mContxet = context;
			inflate = (LayoutInflater) mContxet
					.getSystemService(LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return weiboLists.size();
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
			// TODO Auto-generated method stub
			convertView = inflate.inflate(R.layout.weibo_pulldown_item2, null);
			TextView title = (TextView) convertView
					.findViewById(R.id.pulldown_item2_title);
			TextView time = (TextView) convertView
					.findViewById(R.id.pulldown_item2_time);
			TextView vist = (TextView) convertView
					.findViewById(R.id.pulldown_item2_browse);
			ImageView headIcon = (ImageView) convertView
					.findViewById(R.id.pulldown_item2_image1);
			ImageView photoIcon = (ImageView) convertView
					.findViewById(R.id.pulldown_item2_image2);
			TextView name = (TextView) convertView
					.findViewById(R.id.pulldown_item2_name);
			title.setText(weiboLists.get(position).title);
			time.setText(weiboLists.get(position).time);
			vist.setText(weiboLists.get(position).vist + "");
			name.setText(weiboLists.get(position).name);
			headIcon.setBackgroundResource(weiboLists.get(position).imgHeadId);
			if (weiboLists.get(position).imgId == 0) {
				photoIcon.setVisibility(8);
			}
			photoIcon.setBackgroundResource(weiboLists.get(position).imgId);

			return convertView;
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.weibo_pulldown_imagenutton1:
			startActivity(new Intent(PullDownActivity.this, appMain.class));
			PullDownActivity.this.finish();
			break;
		case R.id.weibo_pulldown_imagenutton2:
			Intent intent = new Intent(PullDownActivity.this, shareBaby.class);
			intent.putExtra(weiboUtils.weibo_sharePic_key,
					weiboUtils.weibo_sharePic_Flag2);
			intent.putExtra(weiboUtils.weibo_return_key,
					weiboUtils.weibo_return_Flag);
			startActivity(intent);
			this.finish();
			break;
		}
	}

	public void onBackPressed() {
		super.onBackPressed();
		PullDownActivity.this.finish();
		Intent intent = new Intent(this, appMain.class);
		startActivity(intent);
	}
}
