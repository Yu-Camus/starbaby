package com.starbaby_03.share;

import java.util.ArrayList;


import com.example.starbaby_03.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

public class author extends Activity{
	private ListView lv;
	private ArrayList<author_list> list;
	private MyAdapter adapter;
	View loadingView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.share_authorlist);
		author_list list1 = new author_list(R.drawable.add_in, "xiaoming", "hi", "2012");
		author_list list2 = new author_list(R.drawable.add_in, "xiaoming2", "hi2", "2012");
		author_list list3 = new author_list(R.drawable.add_in, "xiaoming3", "hi3", "2012");
		author_list list4 = new author_list(R.drawable.add_in, "xiaoming3", "hi3", "2012");
		author_list list5 = new author_list(R.drawable.add_in, "xiaoming3", "hi3", "2012");
		author_list list6 = new author_list(R.drawable.add_in, "xiaoming3", "hi3", "2012");
		author_list list7 = new author_list(R.drawable.add_in, "xiaoming3", "hi3", "2012");
		
		list = new ArrayList<author_list>();
		list.add(list1);
		list.add(list2);
		list.add(list3);
		list.add(list4);list.add(list5);list.add(list6);list.add(list7);
		init();
		
	}
	int nextpage = 1;
    int currentpage = 0;
    private final class ScrollListener implements OnScrollListener{
    	private int number = 20;
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			Log.e("Test", "onScrollStateChanged(scrollState="+ scrollState + ")");
		}
		
		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			Log.e("Test", "onScroll(firstVisibleItem="+ firstVisibleItem+
					"visibleItemCount="+visibleItemCount+ "totalItemCount="+totalItemCount + ")");
			//listView.getLastVisiblePosition()
			if(firstVisibleItem + visibleItemCount == totalItemCount){//下一页
				if(totalItemCount>0) nextpage = totalItemCount / number + 1;
				Log.e("nextpage=", nextpage+"");
				if(currentpage!=nextpage){
					lv.addFooterView(loadingView);//显示数据正在加载
					//开线程{得到数据，然后发送消息}
					handler.sendMessageDelayed(handler.obtainMessage(1, totalItemCount), 3000);
					currentpage = nextpage;
				}
			}
		}
    }
    
    Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			author_list list6 = new author_list(R.drawable.add_in, "wangva3", "3", "2012");
			author_list list7 = new author_list(R.drawable.add_in, "wangva3", "wagba", "2012");
			author_list list8 = new author_list(R.drawable.add_in, "wangva3", "3", "2012");
			author_list list9 = new author_list(R.drawable.add_in, "wangva3", "wagba", "2012");
			author_list list10 = new author_list(R.drawable.add_in, "wangva3", "3", "2012");
			author_list list11 = new author_list(R.drawable.add_in, "wangva3", "wagba", "2012");
			author_list list12 = new author_list(R.drawable.add_in, "wangva3", "3", "2012");
			list.add(list6);
			list.add(list7);
			list.add(list8);
			list.add(list9);
			list.add(list10);
			list.add(list11);
			list.add(list12);
			adapter.notifyDataSetChanged();
			lv.removeFooterView(loadingView);//当数据加载完后，删除提示
		}
    };
	private void init() {
		// TODO Auto-generated method stub
		loadingView = getLayoutInflater().inflate(R.layout.share_loading, null);
		lv = (ListView) findViewById(R.id.share_authorlist_lv1);
		adapter = new MyAdapter(this,list);
		lv.setOnScrollListener(new ScrollListener());
		lv.setAdapter(adapter);
	}
	class MyAdapter extends BaseAdapter{
		private Context mContext;
		private ArrayList<author_list> totalList;
		public MyAdapter (Context mContext, ArrayList<author_list> totalList){
			this.mContext = mContext;
			this.totalList = totalList;
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return totalList.size();
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
			convertView = getLayoutInflater().inflate(R.layout.share_authorlist_view, null);
			ImageView headIv = (ImageView)convertView. findViewById(R.id.share_authorlist_view_iv1);
			TextView noteTv = (TextView)convertView. findViewById(R.id.share_authorlist_view_tv1);
			TextView timeTv = (TextView)convertView. findViewById(R.id.share_authorlist_view_tv2);
			headIv.setBackgroundResource(totalList.get(position).url);
			noteTv.setText(totalList.get(position).msg);
			timeTv.setText(totalList.get(position).time);
			return convertView;
		}
	}
}
