package com.starbaby_03.Gallery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import com.example.starbaby_03.R;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class MyAdapter1 extends BaseAdapter {
	private Context context;
	private List<String> pList;
	private LayoutInflater mInflater;
	private View view;
	private Boolean currentFlag;
	private List<Bitmap> mapList = new ArrayList<Bitmap>();
	public List<String> indexList = new ArrayList<String>();
	// 定义一个向量作为选中与否容器
	private Vector<Boolean> mImage_bs = new Vector<Boolean>();
	// 记录上一次选中的图片位置，-1表示未选中任何图片
	private int lastPosition = -1;
	
	public MyAdapter1(Context ct,List<String> list){
		context=ct;
		pList=list;
		mInflater=LayoutInflater.from(context);
//		for (int i = 0; i < pList.size(); i++) {
//			mapList.add(BitmapFactory.decodeResource(ct.getResources(), Integer.parseInt(list.get(i))));
			mImage_bs.add(false);
//		}
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(pList!=null)
			return pList.size();
		else
			return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if(pList!=null)
			return pList.get(position);
		else
			return null;
		
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.gallery_showmin_view, null);
		}
		ESImageView ivMain = (ESImageView) convertView
				.findViewById(R.id.ivshow);
//		ImageView ivSelect = (ImageView) convertView
//				.findViewById(R.id.imageview_ivSelect);
		ivMain.setScaleType(ScaleType.CENTER);
		ivMain.invalidate();
		ivMain.setImageUrl(getItem(position).toString());
		currentFlag=mapShowMin.Flag;
//		if (currentFlag==false)
//			ivSelect.setImageResource(R.drawable.btncheck_no);
//		else {
//			ivSelect.setImageResource(R.drawable.btncheck_yes);
//		}
		return convertView;
	}
	private String getName(String path){
		if(path!=null && path.length()>5){
			return path.substring(path.lastIndexOf("/")+1,path.length());
		}
		else{
			return "";
		}
	}
	// 修改选中的状态
		public void changeState(int position) {
			if (indexList.contains(position + "")) {
				indexList.remove(position + "");
			} else {
				indexList.add(position + "");
			}
			// 多选时
			mImage_bs.setElementAt(!mImage_bs.elementAt(position), position); // 直接
			notifyDataSetChanged(); // 通知适配器进行更新
		}
}
