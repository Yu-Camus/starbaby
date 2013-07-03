package com.starbaby_03.scroll;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.starbaby_03.R;
import com.starbaby_03.aboutUs.infoCenter;
import com.starbaby_03.info.user_enter;
import com.starbaby_03.info.user_register;
import com.starbaby_03.net.AsyncHttpGet;
import com.starbaby_03.net.AsyncHttpPost;
import com.starbaby_03.net.DefaultThreadPool;
import com.starbaby_03.net.RequestParameter;
import com.starbaby_03.net.RequestResultCallback;
import com.starbaby_03.saveAndSearch.savePhoto;
import com.starbaby_03.saveAndSearch.serach;
import com.starbaby_03.utils.JsonObject;
import com.starbaby_03.utils.MyData;
import com.starbaby_03.utils.UploadUtil;
import com.starbaby_03.utils.aboutUsUtils;
import com.starbaby_03.utils.beautyUtils;
import com.starbaby_03.utils.contentUtils;
import com.starbaby_03.utils.saveFile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class scrollOperate extends Activity implements android.view.View.OnClickListener {
	private ImageButton iBnt1,iBnt2;
	private ImageView iv1;
	private EditText et1;
	public static ArrayList<File> fileList;
	private ArrayList<String> listName;
	private String path;
	private String name;
	private ArrayAdapter<List> adapter = null;
	private String storeName;//选择保存图片的相册名称
	private int storeUid;//选择保存图片的uid
	private int length2 = 0;//当前edittext输入的字符长度
	private String editStoreName;//输入的新建相册名(区分于没有相册的前提)
	private String picDescribe="";//照片的描述
	private ArrayList<aboutUsUtils> aboutUsUtils_list;
	private ArrayList<String> albumname =null;
	private ArrayList<Integer> albumid = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scrolloperate);
		init();
		listener();
	}

	private void listener() {
		iBnt1.setOnClickListener(this);
		iBnt2.setOnClickListener(this);
	}

	private void init() {
		iBnt1=(ImageButton) findViewById(R.id.scrolloperate_iBnt1);
		iBnt2=(ImageButton) findViewById(R.id.scrolloperate_iBnt2);
		iv1=(ImageView) findViewById(R.id.scrolloperate_iv1);
		et1=(EditText) findViewById(R.id.scrolloperate_et1);
		iv1.setImageBitmap(BitmapFactory.decodeFile(saveFile.operateName));
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.scrolloperate_iBnt1://返回操作
			this.finish();
			break;
		case R.id.scrolloperate_iBnt2://对完成的图片就行 保存或者分享操作
			Intent intent = getIntent();
			String str = intent.getStringExtra("scroll");
			if(str.equals("save") ){//保存本地
				searchFile();
			}else if(str.equals("release")){//分享出去
				//首先获取线上所有相册
				getFrame(191206388,1);
			}
			break;	
		}
	}
	/**
	 * JSON解析 读取线上相册
	 * 
	 * @param uid
	 *            用户uid
	 * @param cur_page
	 *            对应线上相册的当前页
	 * @return
	 */
	private void getFrame(int uid, int cur_page) {
		aboutUsUtils_list = new ArrayList<aboutUsUtils>();
		albumname = new ArrayList<String>();
		albumid = new ArrayList<Integer>();
		List<RequestParameter> parameterList = new ArrayList<RequestParameter>();
		AsyncHttpGet get = new AsyncHttpGet(null, contentUtils.frameList + uid
				+ "/" + cur_page, parameterList, new RequestResultCallback() {
			public void onSuccess(Object o) {
				final String result = (String) o;
				scrollOperate.this.runOnUiThread(new Runnable() {
					public void run() {
						try {
							JSONObject json = new JSONObject(result);
							int albumsize = new JsonObject().albumsize(result);
							JSONArray img = json.getJSONArray("datalist");
								for (int i = 0; i < img.length(); i++) {
									JSONObject imgUrl = img.getJSONObject(i);
									String albumnameList = imgUrl.getString("albumname");
									int albumidList = imgUrl.getInt("albumid");
									albumname.add(albumnameList);
									albumid.add(albumidList);
								}
								showDialog3();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

				});
			}

			@Override
			public void onFail(Exception e) {
				// TODO Auto-generated method stub

			}
		});
		DefaultThreadPool.getInstance().execute(get);
	}
	
	//获取目录下所有相册的路径path
	void searchFile() {
		File file = new File(saveFile.file);
		if (!file.exists()) {
			file.mkdir();
		}
		fileList = new ArrayList<File>();
		new serach().getFile2(file);
		if (fileList.size() == 0 || fileList == null) {
			showDialog1();
		}
		getMapData(fileList);
		showDialog2();
	}
	//获取目下下所有相册ID
		public void getMapData(ArrayList<File> list) {
			ArrayList<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
			HashMap<String, Object> item;
			listName = new ArrayList<String>();
			int i = 0;
			for (i = 0; i < list.size(); i++) {
				item = new HashMap<String, Object>();
				path = list.get(i).toString();
				Log.e("path=", path);
				// 相册名称
				name = path.substring(path.lastIndexOf("/") + 1, path.length());
				Log.e("name=", name);
				listName.add(name);
			}
		}
		/**
		 * 
		 *  自定义alterdialog
		 *  1.选择相册进行存储 
		 *  2.自行创建新相册
		 */
		private void showDialog2() {
			LayoutInflater inflate = LayoutInflater.from(this);
			View title = inflate.inflate(R.layout.scroll_dialog_title, null);
			TextView tv = (TextView) title
					.findViewById(R.id.scroll_dialog_title_tv1);
			tv.invalidate();
			tv.setText("选择相册");
			View view = inflate.inflate(R.layout.scroll_dialog_spiner, null);
			Spinner sp = (Spinner) view.findViewById(R.id.scroll_dialog_note_sp);
			AlertDialog.Builder builder = new AlertDialog.Builder(scrollOperate.this);
			Log.e("mapStorage.listName=", listName + "");
			adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,
					listName);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			sp.setAdapter(adapter);
			sp.setOnItemSelectedListener(new OnItemSelectedListener() {
				//保存图片
				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					storeName = listName.get(arg2).toString();
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub
				}

			});
			final EditText et = (EditText)view. findViewById(R.id.gallery_dialog_spiner_edittext);
			final TextView tv2 = (TextView) view.findViewById(R.id.gallery_dialog_spiner_textview2);
			et.setFilters(new InputFilter[] { new InputFilter.LengthFilter(5) });
			et.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int start, int before,
						int count) {
					// TODO Auto-generated method stub

				}

				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
					// TODO Auto-generated method stub

				}

				@Override
				public void afterTextChanged(Editable s) {
					editStoreName=et.getText().toString();
					length2 = et.getText().length();
					tv2.invalidate();
					tv2.setText(length2 + "/" + "5");
				}
			});
			builder.setCustomTitle(title);
			builder.setView(view);
			builder.setNegativeButton("取消", new OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub

				}
			});
			final Bitmap bitmap = BitmapFactory.decodeFile(saveFile.operateName);
			builder.setPositiveButton("确定", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					if(length2==0){//选择相册保存
						String strPath=saveFile.file + storeName;
						try {
							new savePhoto().takePhoto(bitmap,strPath);
							Toast.makeText(scrollOperate.this, "保存成功", 1000).show();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}else{//新建相册保存
						File file = new File("/sdcard/starbaby/" + editStoreName);
						if(!file.exists()){
							file.mkdir();
							try {
								new savePhoto().takePhoto(bitmap,file.toString());
								Toast.makeText(scrollOperate.this, "保存成功", 1000).show();
								// 按下按钮。alterdialog消失
								Field field = dialog.getClass().getSuperclass()
										.getDeclaredField("mShowing");
								field.setAccessible(true);
								field.set(dialog, true);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}else{
							Toast.makeText(scrollOperate.this, "该相册已存在", 1000).show();
							// 按下按钮。alterdialog不消失
							try {
								Field field = dialog.getClass().getSuperclass()
										.getDeclaredField("mShowing");
								field.setAccessible(true);
								field.set(dialog, false);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						
					}
					//把图片的描述存入到spPic
					picDescribe = et1.getText().toString();
					Log.e("picDescribe=", picDescribe);
					beautyUtils.spPic.edit().putString(beautyUtils.spPicPath, picDescribe).commit();
				}
			});
			AlertDialog alert = builder.create();
			alert.show();
		}

		/**
		 *  没有相册，创建新相册
		 */
		void showDialog1() {
			LayoutInflater inflate = LayoutInflater.from(this);
			View title = inflate.inflate(R.layout.scroll_dialog_title, null);
			View view = inflate.inflate(R.layout.gallery_addpicstore, null);
			final TextView textview = (TextView) view
					.findViewById(R.id.gallery_dialog_view_textview2);
			final EditText editview = (EditText) view
					.findViewById(R.id.gallery_dialog_view_edittext);
			editview.setFilters(new InputFilter[] { new InputFilter.LengthFilter(5) });
			editview.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int start, int before,
						int count) {
					// TODO Auto-generated method stub

				}

				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
					// TODO Auto-generated method stub

				}

				@Override
				public void afterTextChanged(Editable s) {
					int length = 0;
					length = editview.getText().length();
					textview.invalidate();
					textview.setText(length + "/" + "5");
				}
			});
			AlertDialog.Builder builder = new AlertDialog.Builder(scrollOperate.this);
			builder.setCustomTitle(title);
			builder.setView(view);
			builder.setNegativeButton("取消", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub

				}
			});
			builder.setPositiveButton("确定", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					String str = editview.getText().toString();
					File destDir = new File("/sdcard/starbaby/" + str);
					if (destDir.exists()) {
						Toast.makeText(scrollOperate.this, "该相册已存在", 1000).show();
						// 按下按钮。alterdialog不消失
						try {
							Field field = dialog.getClass().getSuperclass()
									.getDeclaredField("mShowing");
							field.setAccessible(true);
							field.set(dialog, false);
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
						destDir.mkdir();
						// 按下按钮。alterdialog消失
						try {
							Field field = dialog.getClass().getSuperclass()
									.getDeclaredField("mShowing");
							field.setAccessible(true);
							field.set(dialog, true);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					init();
				}
			});
			AlertDialog alert = builder.create();
			alert.show();
		}
		void showDialog3(){
			LayoutInflater inflate = LayoutInflater.from(this);
			View title = inflate.inflate(R.layout.scroll_dialog_title, null);
			TextView tv = (TextView) title
					.findViewById(R.id.scroll_dialog_title_tv1);
			tv.invalidate();
			tv.setText("选择相册");
			View view = inflate.inflate(R.layout.scroll_dialog_spiner, null);
			Spinner sp = (Spinner) view.findViewById(R.id.scroll_dialog_note_sp);
			AlertDialog.Builder builder = new AlertDialog.Builder(scrollOperate.this);
			adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,
					albumname);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			sp.setAdapter(adapter);
			sp.setOnItemSelectedListener(new OnItemSelectedListener() {
				//保存图片
				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					storeName = albumname.get(arg2).toString();
					storeUid = albumid.get(arg2);
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub
				}

			});
			builder.setCustomTitle(title);
			builder.setView(view);
			builder.setNegativeButton("取消", new OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub

				}
			});
			final Bitmap bitmap = BitmapFactory.decodeFile(saveFile.operateName);
			builder.setPositiveButton("确定", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				Toast.makeText(scrollOperate.this, "保存成功", 1000).show();
				picDescribe = et1.getText().toString();
				String contentUrl = null;
				//先上传照片服务端 获取照片url
				File file = new File(saveFile.operateName);
				if(file != null){
					String request = UploadUtil.uploadFile(file,contentUtils.registerImgUrl);
					// json解析获取的url
					try {
						contentUrl = new JsonObject().getIMAGEURl(request);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(contentUrl !=null && contentUrl !=""){
						Login( contentUtils.spGetInfo.getInt("uid", 0), contentUtils.spGetInfo.getString("psw", ""), storeUid,contentUrl, picDescribe, 2);
					}
							
				}
				
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
		}
		void Login(int uid,String pwd,int Albumid,String Picurl,String Txt ,int Sys){
			List<RequestParameter> parameterList = new ArrayList<RequestParameter>();
			parameterList.add(new RequestParameter("uid", uid));
			parameterList.add(new RequestParameter("pwd", pwd));
			parameterList.add(new RequestParameter("albumid", Albumid));
			parameterList.add(new RequestParameter("picurl", Picurl));
			parameterList.add(new RequestParameter("txt", Txt));
			parameterList.add(new RequestParameter("sys", Sys));
			Log.e("info=", uid+":"+pwd+":"+Albumid+":"+Picurl+":"+Txt+":"+Sys);
			AsyncHttpPost httpost = new AsyncHttpPost(null,
					contentUtils.sendPicUrl, parameterList,
					new RequestResultCallback() {

						@Override
						public void onSuccess(Object o) {
							// TODO Auto-generated method stub
							final String result = (String) o;
							scrollOperate.this.runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									try {
										JSONObject obj = new JSONObject(result);
										int info = obj.getInt("msg");
										Log.e("contentUtils.sendPicUrl=", contentUtils.sendPicUrl);
										Log.e("info=", info+"");
										switch(info){
										case 1:
											Toast.makeText(scrollOperate.this, "success",1000).show();
											
											break;
										default:
											Toast.makeText(scrollOperate.this, info,1000).show();
											break;
										}
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							});
						}

						@Override
						public void onFail(Exception e) {
							// TODO Auto-generated method stub
						}
					});
			DefaultThreadPool.getInstance().execute(httpost);
		}
}
