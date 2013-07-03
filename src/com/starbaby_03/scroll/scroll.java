package com.starbaby_03.scroll;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.Timer;
import java.util.TimerTask;

import com.example.starbaby_03.R;
import com.starbaby_03.beautify.addFrame;
import com.starbaby_03.beautify.addWords;
import com.starbaby_03.main.appMain;
import com.starbaby_03.saveAndSearch.savePhoto;
import com.starbaby_03.utils.Utils;
import com.starbaby_03.utils.beautyUtils;
import com.starbaby_03.utils.saveFile;
import com.starbaby_03.view.MainActivity_ImgBnt;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Printer;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

public class scroll extends Activity implements OnTouchListener,
		OnClickListener {

	private ImageView iv;
	private MainActivity_ImgBnt iBnt1, iBnt2, 
			cutBnt, addPhotoBnt, addWordsBnt;
	private Bitmap myBitmap;
	private Uri uri = null;
	private int imgBnt1_textcolor = Color.WHITE;
	private float imgBnt_textsize = 14f;
	private File tempFile = new File(saveFile.operateName);
	private int BitW, BitH;
	private RelativeLayout relativelayout;
	private RelativeLayout RelativeLayout2;
	private Button bnt1,bnt2;
	private AlertDialog.Builder bulider;
	private AlertDialog alert;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.scroll_scroll);
		init();
	}

	void listener() {
		cutBnt.setOnClickListener(this);
		cutBnt.setOnTouchListener(this);
		addPhotoBnt.setOnClickListener(this);
		addPhotoBnt.setOnTouchListener(this);
		addWordsBnt.setOnClickListener(this);
		addWordsBnt.setOnTouchListener(this);
		iBnt1.setOnClickListener(this);
		iBnt2.setOnClickListener(this);
		iBnt1.setOnTouchListener(this);
		iBnt2.setOnTouchListener(this);
		
	}

	void init() {
		cutBnt = (MainActivity_ImgBnt) findViewById(R.id.scroll_imgBnt1);
		addPhotoBnt = (MainActivity_ImgBnt) findViewById(R.id.scroll_imgBnt2);
		addWordsBnt = (MainActivity_ImgBnt) findViewById(R.id.scroll_imgBnt3);
		iBnt1 = (MainActivity_ImgBnt) findViewById(R.id.RetureBnt);
		iBnt2 = (MainActivity_ImgBnt) findViewById(R.id.SaveBnt);
		relativelayout = (RelativeLayout) findViewById(R.id.relativelayout);
		RelativeLayout2 = (RelativeLayout) findViewById(R.id.RelativeLayout2);
		cutBnt.setText("����ͼƬ");
		cutBnt.setColor(imgBnt1_textcolor);
		cutBnt.setTextsize(imgBnt_textsize);
		addPhotoBnt.setText("��ӱ߿�");
		addPhotoBnt.setColor(imgBnt1_textcolor);
		addPhotoBnt.setTextsize(imgBnt_textsize);
		addWordsBnt.setText("�������");
		addWordsBnt.setTextsize(imgBnt_textsize);
		addWordsBnt.setColor(imgBnt1_textcolor);
		iv = (ImageView) findViewById(R.id.iv);
		ViewTreeObserver vot = RelativeLayout2.getViewTreeObserver();
		vot.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			@Override
			public void onGlobalLayout() {
				RelativeLayout2.getViewTreeObserver()
						.removeGlobalOnLayoutListener(this);
				beautyUtils.layoutHight = RelativeLayout2.getHeight();
				beautyUtils.layoutWidth = getWindowManager()
						.getDefaultDisplay().getWidth();
				try {
					showPhoto();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	void showPhoto() throws IOException {
		// �������ͼƬ
		if (getIntent().getExtras().getInt(Utils.INTENT_FLAG) == Utils.CROP_IMAGE) {
			iv.setImageDrawable(Drawable.createFromPath(tempFile
					.getAbsolutePath()));
			listener();
		}// ��ʾ�����κβ�����ͼƬ
		else if (getIntent().getExtras().getInt(Utils.INTENT_FLAG) == Utils.FULL_IMAGE) {
			// ��һ�α���ͼƬ����ͼƬ���д����档�Ա��������
			uri = Uri.parse(scroll.this.getIntent().getStringExtra(
					"originalUri"));
			String[] proj = { MediaStore.Images.Media.DATA };
			Cursor actualimagecursor = managedQuery(uri, proj, null, null, null);
			int actual_image_column_index = actualimagecursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			actualimagecursor.moveToFirst();
			String img_path = actualimagecursor
					.getString(actual_image_column_index);
			File file = new File(img_path);
			// ���Ŵ����ֵΪaddframe���imgeview��ֵ��ȡscroll��addframe��addword����С�ġ�
			new savePhoto().scale(file, beautyUtils.layoutWidth,
					beautyUtils.layoutHight);
			myBitmap = BitmapFactory.decodeFile(tempFile.toString());
			iv.setImageBitmap(myBitmap);
			listener();
		} else if (getIntent().getExtras().getInt(Utils.INTENT_FLAG) == Utils.CAMERA_IMAGE) {
			new savePhoto().scale(new File(saveFile.operatePath+saveFile.operateName), beautyUtils.layoutWidth,
					beautyUtils.layoutHight);
			myBitmap = BitmapFactory.decodeFile(tempFile.toString());
			iv.setImageBitmap(myBitmap);
			listener();
		}
	}

	// ͼƬת���ֽ���
	public static Bitmap getPicFromBytes(byte[] bytes,
			BitmapFactory.Options opts) {
		if (bytes != null)
			if (opts != null)
				return BitmapFactory.decodeByteArray(bytes, 0, bytes.length,
						opts);
			else
				return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		return null;
	}

	public static byte[] readStream(InputStream inStream) throws Exception {
		byte[] buffer = new byte[1024];
		int len = -1;
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		byte[] data = outStream.toByteArray();
		outStream.close();
		inStream.close();
		return data;

	}

	// ��ťtouch�¼����ı䰴��ʱ�ı���
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.RetureBnt:
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				v.setBackgroundResource(R.drawable.home_in);
			} else if (event.getAction() == MotionEvent.ACTION_UP) {
				v.setBackgroundResource(R.drawable.home_over);
			}
			break;
		case R.id.SaveBnt:
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				v.setBackgroundResource(R.drawable.ensuer_in);
			} else if (event.getAction() == MotionEvent.ACTION_UP) {
				v.setBackgroundResource(R.drawable.ensuer_over);
			}
			break;
		case R.id.scroll_imgBnt1:
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				v.setBackgroundResource(R.drawable.cut_in);
			} else if (event.getAction() == MotionEvent.ACTION_UP) {
				v.setBackgroundResource(R.drawable.cut_over);
			}
			break;
		case R.id.scroll_imgBnt3:
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				v.setBackgroundResource(R.drawable.word_in);
			} else if (event.getAction() == MotionEvent.ACTION_UP) {
				v.setBackgroundResource(R.drawable.word_over);
			}
			break;
		case R.id.scroll_imgBnt2:
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				v.setBackgroundResource(R.drawable.frame_in);
			} else if (event.getAction() == MotionEvent.ACTION_UP) {
				v.setBackgroundResource(R.drawable.frame_over);
			}
			break;
		default:
			break;
		}
		return false;
	}

	// ��ť�ļ����¼�
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.RetureBnt:
			Intent intent5 = new Intent(scroll.this, appMain.class);
			startActivity(intent5);
			this.finish();
			break;
		case R.id.SaveBnt://������߷���
			showAlertDialog();
			break;
		case R.id.scroll_dialog_button1://���� ��ת������ ����
			Intent intent = new Intent(this,scrollOperate.class);
			intent.putExtra("scroll", "save");
			startActivity(intent);
			alert.dismiss();
			break;
		case R.id.scroll_dialog_button2:// ��������ת������ ����
			Intent intent2 = new Intent(this,scrollOperate.class);
			intent2.putExtra("scroll", "release");
			startActivity(intent2);
			alert.dismiss();
			break;
		case R.id.scroll_imgBnt1:
			startPhotoZoom(Uri.fromFile(tempFile));
			break;
		case R.id.scroll_imgBnt2:
			Intent intent3 = new Intent(scroll.this, addFrame.class);
			startActivity(intent3);
			this.finish();
			break;
		case R.id.scroll_imgBnt3:
			Intent intent4 = new Intent(scroll.this, addWords.class);
			intent4.putExtra(Utils.INTENT_FLAG, Utils.NO_WORDS);
			startActivity(intent4);
			this.finish();
			break;
		default:
			break;
		}
	}

	private void showAlertDialog() {
		LayoutInflater inflate =(LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
		View title = inflate.inflate(R.layout.scroll_dialog_title, null);
		View button = inflate.inflate(R.layout.scroll_dialog_button, null);
		bnt1 = (Button) button.findViewById(R.id.scroll_dialog_button1);
		bnt2 = (Button) button.findViewById(R.id.scroll_dialog_button2);
		bnt1.setOnClickListener(this);
		bnt2.setOnClickListener(this);
		bulider= new AlertDialog.Builder(scroll.this);
		bulider.setCustomTitle(title);
		bulider.setView(button);
		alert = bulider.create();
		alert.show();
	}

	// ���ò���
	public void startPhotoZoom(Uri uri) {
		/*
		 * ֱ�ӵ���androidϵͳ�Դ�ͼƬ�ü�����, ��ֱ�ӵ����ؿ��
		 */
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		Log.e("URI", uri.toString());
		// �������crop=true�������ڿ�����Intent��������ʾ��VIEW�ɲü�
		intent.putExtra("crop", "true");
		// aspectX aspectY �ǿ�ߵı���
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY �ǲü�ͼƬ����� �����������ѹ��Ϊ300*300������ͼ�İ����������ı��С�
		intent.putExtra("outputX", 300);
		intent.putExtra("outputY", 300);
		intent.putExtra("return-data", true);
		// ����ͼƬ
		new savePhoto().savephoto(intent);
		startActivityForResult(intent, 3);
	}

	protected void onActivityResult(int requestCode, int resultCode,
			final Intent data) {
		if (data == null)
			return;
		if (requestCode == 3) {
			Bundle extras = data.getExtras();
			if (extras != null) {
				Bitmap photo = extras.getParcelable("data");
				// ����ͷ�񵽱���
				try {
					new savePhoto().saveImg(photo);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			iv.setImageBitmap(BitmapFactory.decodeFile(tempFile.toString()));
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}