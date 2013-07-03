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
	private String storeName;//ѡ�񱣴�ͼƬ���������
	private int storeUid;//ѡ�񱣴�ͼƬ��uid
	private int length2 = 0;//��ǰedittext������ַ�����
	private String editStoreName;//������½������(������û������ǰ��)
	private String picDescribe="";//��Ƭ������
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
		case R.id.scrolloperate_iBnt1://���ز���
			this.finish();
			break;
		case R.id.scrolloperate_iBnt2://����ɵ�ͼƬ���� ������߷������
			Intent intent = getIntent();
			String str = intent.getStringExtra("scroll");
			if(str.equals("save") ){//���汾��
				searchFile();
			}else if(str.equals("release")){//�����ȥ
				//���Ȼ�ȡ�����������
				getFrame(191206388,1);
			}
			break;	
		}
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
	
	//��ȡĿ¼����������·��path
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
	//��ȡĿ�����������ID
		public void getMapData(ArrayList<File> list) {
			ArrayList<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
			HashMap<String, Object> item;
			listName = new ArrayList<String>();
			int i = 0;
			for (i = 0; i < list.size(); i++) {
				item = new HashMap<String, Object>();
				path = list.get(i).toString();
				Log.e("path=", path);
				// �������
				name = path.substring(path.lastIndexOf("/") + 1, path.length());
				Log.e("name=", name);
				listName.add(name);
			}
		}
		/**
		 * 
		 *  �Զ���alterdialog
		 *  1.ѡ�������д洢 
		 *  2.���д��������
		 */
		private void showDialog2() {
			LayoutInflater inflate = LayoutInflater.from(this);
			View title = inflate.inflate(R.layout.scroll_dialog_title, null);
			TextView tv = (TextView) title
					.findViewById(R.id.scroll_dialog_title_tv1);
			tv.invalidate();
			tv.setText("ѡ�����");
			View view = inflate.inflate(R.layout.scroll_dialog_spiner, null);
			Spinner sp = (Spinner) view.findViewById(R.id.scroll_dialog_note_sp);
			AlertDialog.Builder builder = new AlertDialog.Builder(scrollOperate.this);
			Log.e("mapStorage.listName=", listName + "");
			adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,
					listName);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			sp.setAdapter(adapter);
			sp.setOnItemSelectedListener(new OnItemSelectedListener() {
				//����ͼƬ
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
			builder.setNegativeButton("ȡ��", new OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub

				}
			});
			final Bitmap bitmap = BitmapFactory.decodeFile(saveFile.operateName);
			builder.setPositiveButton("ȷ��", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					if(length2==0){//ѡ����ᱣ��
						String strPath=saveFile.file + storeName;
						try {
							new savePhoto().takePhoto(bitmap,strPath);
							Toast.makeText(scrollOperate.this, "����ɹ�", 1000).show();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}else{//�½���ᱣ��
						File file = new File("/sdcard/starbaby/" + editStoreName);
						if(!file.exists()){
							file.mkdir();
							try {
								new savePhoto().takePhoto(bitmap,file.toString());
								Toast.makeText(scrollOperate.this, "����ɹ�", 1000).show();
								// ���°�ť��alterdialog��ʧ
								Field field = dialog.getClass().getSuperclass()
										.getDeclaredField("mShowing");
								field.setAccessible(true);
								field.set(dialog, true);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}else{
							Toast.makeText(scrollOperate.this, "������Ѵ���", 1000).show();
							// ���°�ť��alterdialog����ʧ
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
					//��ͼƬ���������뵽spPic
					picDescribe = et1.getText().toString();
					Log.e("picDescribe=", picDescribe);
					beautyUtils.spPic.edit().putString(beautyUtils.spPicPath, picDescribe).commit();
				}
			});
			AlertDialog alert = builder.create();
			alert.show();
		}

		/**
		 *  û����ᣬ���������
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
			builder.setNegativeButton("ȡ��", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub

				}
			});
			builder.setPositiveButton("ȷ��", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					String str = editview.getText().toString();
					File destDir = new File("/sdcard/starbaby/" + str);
					if (destDir.exists()) {
						Toast.makeText(scrollOperate.this, "������Ѵ���", 1000).show();
						// ���°�ť��alterdialog����ʧ
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
						// ���°�ť��alterdialog��ʧ
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
			tv.setText("ѡ�����");
			View view = inflate.inflate(R.layout.scroll_dialog_spiner, null);
			Spinner sp = (Spinner) view.findViewById(R.id.scroll_dialog_note_sp);
			AlertDialog.Builder builder = new AlertDialog.Builder(scrollOperate.this);
			adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,
					albumname);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			sp.setAdapter(adapter);
			sp.setOnItemSelectedListener(new OnItemSelectedListener() {
				//����ͼƬ
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
			builder.setNegativeButton("ȡ��", new OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub

				}
			});
			final Bitmap bitmap = BitmapFactory.decodeFile(saveFile.operateName);
			builder.setPositiveButton("ȷ��", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				Toast.makeText(scrollOperate.this, "����ɹ�", 1000).show();
				picDescribe = et1.getText().toString();
				String contentUrl = null;
				//���ϴ���Ƭ����� ��ȡ��Ƭurl
				File file = new File(saveFile.operateName);
				if(file != null){
					String request = UploadUtil.uploadFile(file,contentUtils.registerImgUrl);
					// json������ȡ��url
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
