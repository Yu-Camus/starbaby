package com.starbaby_03.Gallery;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.starbaby_03.R;
import com.starbaby_03.utils.gallery_addview;

public class addPic extends Activity implements OnItemClickListener ,OnClickListener{
	private List<String> lstFile = new ArrayList<String>(); // ½á¹û List
	HashMap<String,String> m = new HashMap<String,String>();
	public String path = null;
	int i = 0;
	int j = 0;
	GridView gv;
	ImageButton iBnt;
	public ArrayList<gallery_addview> gallery_addview = new ArrayList<gallery_addview>();;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gallery_addpic);
		getFile(new File("/sdcard/starbaby/"));
		for (String key : m.keySet()) {
		   System.out.println("key= "+ key + " and value= " + m.get(key));
		   gallery_addview gallery_addview1 = new gallery_addview(m.get(key), key.substring(key.lastIndexOf("/")+1));
		   gallery_addview.add(gallery_addview1);
		}
		init();
		listener();
		gv.setAdapter(new MyAdapter(gallery_addview, this));
	}
	void init(){
		gv = (GridView) findViewById(R.id.gallery_addpic_grideview);
		iBnt=(ImageButton) findViewById(R.id.gallery_addpic_imagebutton);
	}
	void listener(){
		gv.setOnItemClickListener(this);
		iBnt.setOnClickListener(this);
	}
	public void getFile(File root) {
//		while(i<3){
		String path = null;
		File file[] = root.listFiles();
		for (File f : file) {
			if (f.isFile()) {
				String str = f.getPath().substring(	f.getPath().lastIndexOf(".")+1);
				if ("png".equals(str) || "jpg".equals(str)){
					path = f.getParent();
					if(!m.containsKey(path)){
						m.put(path, f.toString());
					}
				}
			} else {
//				i++;
				getFile(f);
//				i--;
			}
		}
	}

	class MyAdapter extends BaseAdapter {
		public ArrayList<gallery_addview> gallery_addview;
		public Context mContext;
		private LayoutInflater inflate;

		public MyAdapter(ArrayList<gallery_addview> gallery_addview,
				Context context) {
			this.gallery_addview = gallery_addview;
			this.mContext = context;
			inflate = (LayoutInflater) mContext
					.getSystemService(LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return gallery_addview.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View view, ViewGroup arg2) {
			// TODO Auto-generated method stub
			view = inflate.inflate(R.layout.gallery_addpic_view, null);
			ImageView imgView = (ImageView) view
					.findViewById(R.id.gallery_addpic_view_imageview);
			TextView txtView = (TextView) view
					.findViewById(R.id.gallery_addpic_view_textview);
			imgView.setImageBitmap(BitmapFactory.decodeFile(gallery_addview
					.get(position).url));
			txtView.setText(gallery_addview.get(position).text);
			return view;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		// TODO Auto-generated method stub
		lstFile.addAll(m.keySet());
		path = lstFile.get(position).toString();
		Intent intent = new Intent(this, addPic_show.class);
		intent.putExtra("url", path);
		Log.e("path=", path);
		startActivity(intent);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()){
		case R.id.gallery_addpic_imagebutton:
			Intent intent=new Intent(this,mapShowMin.class);
			intent.putExtra("url", mapStorage.path2);
			startActivity(intent);
			this.finish();
			break;
		}
	}
}
