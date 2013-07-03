package com.starbaby_03.beautify;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.example.starbaby_03.R;
import com.starbaby_03.saveAndSearch.savePhoto;
import com.starbaby_03.scroll.scroll;
import com.starbaby_03.utils.Utils;
import com.starbaby_03.utils.beautyUtils;
import com.starbaby_03.utils.saveFile;
import com.starbaby_03.view.MainActivity_ImgBnt;
import com.starbaby_03.view.addWordsView;
import com.starbaby_03.view.frameView;

import android.R.color;
import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ReceiverCallNotAllowedException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Paint.Style;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.Layout.Alignment;
import android.util.FloatMath;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class addWords extends Activity implements OnClickListener,
		OnTouchListener {

	private ImageView imageView, imageFrame;
	private Button addButton;
	private EditText editText;
	public String string = null;
	public int width, height;
	private Bitmap icon = null;
	private Bitmap icon2 = null;
	private Bitmap bitmapPhoto = null;
	private Bitmap bitmapResult = null;
	private ImageButton ib5, ibb1, ibb2, ibb3, ibb4, ibb5, ibb6, ibb7, ibb8;
	private MainActivity_ImgBnt ib1, ib2, ib3, ib4;
	private HorizontalScrollView hSC;
	private TextView tv1;
	private int INTENT_FLAG = 0;
	private int Flag = 0;
	private Uri uri;
	private byte[] mContent;
	private Bitmap myBitmap;
	private static final int LAYER_FLAGS = Canvas.MATRIX_SAVE_FLAG
			| Canvas.CLIP_SAVE_FLAG | Canvas.HAS_ALPHA_LAYER_SAVE_FLAG
			| Canvas.FULL_COLOR_LAYER_SAVE_FLAG
			| Canvas.CLIP_TO_LAYER_SAVE_FLAG;
	private Matrix matrix = new Matrix();
	private Matrix savedMatrix = new Matrix();
	private Matrix matrix2 = new Matrix();
	private String TAG = this.getClass().getSimpleName();
	private int imgBnt1_textcolor =	0xffffffff;
	private int imgBnt2_textcolor = 0xff999999;
	private float imgBnt_textsize = 14f;
	private RelativeLayout relativelayout, addwords_relativelayout,
			addword_relativelayout;
	// 进行缩放
	private PointF start = new PointF();
	private PointF mid = new PointF();
	float oldDist = 1f;
	private int rotate = 0;
	private static final int HORIZONTAL = 0;
	private static final int VERTICAL = 1;
	private static final int NONE = 0;
	private static final int DRAG = 1;
	private static final int ZOOM = 2;
	private int mode = NONE;
	private float scale = 0;
	private float newDist = 0;
	private float w = 0;
	private float h = 0;
	private int imgPhotoHeight = 0;
	private int imgPhotoWidth = 0;
	private Boolean clickable = false;
	private static int Bubbles = 0;
	private Bitmap mapBitmap = null;
	public static int[] ListOfBubbles = { R.drawable.empty, R.drawable.word1,
			R.drawable.word2, R.drawable.word3, R.drawable.word4,
			R.drawable.word5, R.drawable.word1, R.drawable.word7,
			R.drawable.word8 };

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.beautify_addwords);
		init();
		addWord();
		listener();
	}

	void init() {
		ib1 = (MainActivity_ImgBnt) findViewById(R.id.addword_but7);
		ib2 = (MainActivity_ImgBnt) findViewById(R.id.addword_but8);
		ib3 = (MainActivity_ImgBnt) findViewById(R.id.addword_ib3);
		ib4 = (MainActivity_ImgBnt) findViewById(R.id.addword_ib4);
		ib3.setText("添加文字");
		ib3.setColor(imgBnt1_textcolor);
		ib3.setTextsize(imgBnt_textsize);
		ib4.setText("添加气泡");
		ib4.setColor(imgBnt2_textcolor);
		ib4.setTextsize(imgBnt_textsize);
		relativelayout = (RelativeLayout) findViewById(R.id.addword_RelativeLayout2);
		addwords_relativelayout = (RelativeLayout) findViewById(R.id.addwords_relativelayout);
		addword_relativelayout = (RelativeLayout) findViewById(R.id.addword_relativelayout);
		tv1 = (TextView) findViewById(R.id.addword_tv);

		imageView = (ImageView) findViewById(R.id.addword_imageView1);// 添加文字和气泡的图层
		imageFrame = (ImageView) findViewById(R.id.addword_heyelloFrontImageViewFrame);// 底层照片
		File file = new File(saveFile.operateName);
		mapBitmap = BitmapFactory.decodeFile(file.toString());
		imageFrame.setImageBitmap(mapBitmap);
		ViewTreeObserver vot = imageView.getViewTreeObserver();
		vot.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			@Override
			public void onGlobalLayout() {
				// TODO Auto-generated method stub
				imageView.getViewTreeObserver().removeGlobalOnLayoutListener(
						this);
				Log.e("TAG", imageView.getHeight() + "");
			}
		});
	}

	void listener() {
		ib1.setOnClickListener(this);
		ib1.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					v.setBackgroundResource(R.drawable.back_in);
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					v.setBackgroundResource(R.drawable.back_over);
				}
				return false;
			}
		});
		ib2.setOnClickListener(this);
		ib2.setClickable(false);
		ib3.setOnClickListener(this);
		ib3.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					v.setBackgroundResource(R.drawable.word_in);
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					v.setBackgroundResource(R.drawable.word_over);
				}
				return false;
			}
		});
		ib4.setOnClickListener(this);
		ib4.setClickable(clickable);
		//ib4.setColor(imgBnt2_textcolor);
		imageView.setOnTouchListener(this);
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.addword_but7:
			// 返回scroll操作。不做文字添加
			Intent intent1 = new Intent(addWords.this, scroll.class);
			intent1.putExtra(Utils.INTENT_FLAG, Utils.CROP_IMAGE);
			startActivity(intent1);
			Bubbles = 0;
			this.finish();
			break;
		case R.id.addword_but8:
			// 完成添加文字
			savaWithFrame();
			seavImg();
			Log.e("Tag", "save");
			Intent intent2 = new Intent(addWords.this, scroll.class);
			intent2.putExtra(Utils.INTENT_FLAG, Utils.CROP_IMAGE);
			startActivity(intent2);
			Bubbles = 0;
			this.finish();
			break;
		case R.id.addword_ib3:
			// 添加文字
			Intent intent3 = new Intent(addWords.this, addWords_01.class);
			startActivity(intent3);
			Bubbles = 0;
			this.finish();
			break;
		case R.id.addword_ib4:
			popwindowword();
			Draw();
			Bubbles = 0;
			break;
		case R.id.addword_but1:
			showib2();
			Bubbles = 0;
			Flag = 1;
			Draw();
			break;
		case R.id.addword_but2:
			showib2();
			Bubbles = 1;
			Flag = 1;
			Draw();
			break;
		case R.id.addword_but3:
			showib2();
			Bubbles = 2;
			Flag = 1;
			Draw();
			break;
		case R.id.addword_but4:
			showib2();
			Bubbles = 3;
			Flag = 1;
			Draw();
			break;
		default:
			break;
		}

	}

	void showib2() {
		ib2.setBackgroundResource(R.drawable.ensuer_over);
		ib2.setClickable(true);
		ib2.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					v.setBackgroundResource(R.drawable.ensuer_in);
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					v.setBackgroundResource(R.drawable.ensuer_over);
				}
				return false;
			}
		});
	}

	void addWord() {
		if (getIntent().getExtras().getInt(Utils.INTENT_FLAG) == Utils.NO_WORDS) {
			imageFrame.setVisibility(1);// 只显示底层图片
			imageView.setVisibility(8);// 不加载修改文字和气泡的图层
		} else if (getIntent().getExtras().getInt(Utils.INTENT_FLAG) == Utils.ADD_WORDS) {
			Draw();
		}

	}

	void Draw() {
		Log.e("color=", "imgBnt2_textcolor");
		clickable = true;
		ib4.setClickable(clickable);
		ib4.invalidate();
		ib4.setBackgroundResource(R.drawable.pool_over);
		ib4.setColor(imgBnt1_textcolor);
		ib4.invalidate();
		Log.e("color=", "imgBnt1_textcolor");
		ib4.setOnTouchListener(new OnTouchListener() {
			
			
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				Log.e("color=", "imgBnt3_textcolor");
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					v.setBackgroundResource(R.drawable.pool_in);
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					v.setBackgroundResource(R.drawable.pool_over);
				}
				return false;
			}
		});
		Intent intent = getIntent();
		string = intent.getStringExtra("Words");

		Bitmap photo = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.addframebackground);
		// 建立一个空的BItMap
		icon = Bitmap.createBitmap(beautyUtils.layoutWidth,
				beautyUtils.layoutHight, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(icon);
		// 建立画笔
		Paint paint = new Paint();
		paint.setColor(Color.YELLOW);
		paint.setDither(true);// 设置更清晰的图形采集
		paint.setFilterBitmap(true);// 过滤一些
		canvas.drawBitmap(photo, 0, 0, paint);
		canvas.drawColor(Color.TRANSPARENT, Mode.CLEAR);
		int area = 10000;
		int size = (int) Math.sqrt(area / string.length());
		TextPaint textPaint = new TextPaint();
		textPaint.setTextSize(size);
		textPaint.setColor(Color.RED);
		StaticLayout layout = new StaticLayout(string, textPaint,
				100 + size / 2, Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);
		int num = (area / string.length());
		// 设置文字显示的位置
		canvas.translate(beautyUtils.layoutWidth / 2 - 55,
				beautyUtils.layoutHight / 2 - 65);

		layout.draw(canvas);
		canvas.save(Canvas.ALL_SAVE_FLAG);

		icon2 = Bitmap.createBitmap(beautyUtils.layoutWidth,
				beautyUtils.layoutHight, Bitmap.Config.ARGB_8888);
		Canvas canvas2 = new Canvas(icon2);
		Paint paint3 = new Paint();
		paint3.setAlpha(180);
		Bitmap photo2 = null;
		if (Flag == 1) {
			photo2 = BitmapFactory.decodeResource(this.getResources(),
					ListOfBubbles[Bubbles]);
		}
		if (Flag == 0) {
			photo2 = BitmapFactory.decodeResource(this.getResources(),
					ListOfBubbles[0]);
			Log.e("Tag", "Flag==0");
		}
//		Bitmap bb = Bitmap.createScaledBitmap(photo2,
//				(int) (photo2.getWidth() * 4 / 3),
//				(int) (photo2.getHeight() * 4 / 3), true);
		Bitmap bb = Bitmap.createScaledBitmap(photo2,
				beautyUtils.layoutWidth,
				beautyUtils.layoutHight, true);
		canvas2.drawBitmap(bb, 0, 0, paint3);
		canvas2.drawBitmap(icon, 0, 0, null);
		canvas2.save(Canvas.ALL_SAVE_FLAG);
		canvas2.restore();

		imageView.setImageBitmap(icon2);
		imageView.setVisibility(1);
		imageFrame.setVisibility(1);
	}

	// 移动伸缩文字
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		ImageView view = (ImageView) v;
		view.setScaleType(ImageView.ScaleType.MATRIX);
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		// 设置拖拉模式
		case MotionEvent.ACTION_DOWN: // first finger down only
			savedMatrix.set(matrix);
			start.set(event.getX(), event.getY());
			Log.d(TAG, "mode=DRAG");
			mode = DRAG;
			break;
		case MotionEvent.ACTION_UP: // first finger lifted
			// 设置多点触摸模式
		case MotionEvent.ACTION_POINTER_UP: // second finger lifted
			mode = NONE;
			Log.d(TAG, "mode=NONE");
			break;
		// 图片伸缩功能
		case MotionEvent.ACTION_POINTER_DOWN: // second finger down
			oldDist = spacing(event);
			Log.d(TAG, "oldDist=" + oldDist);
			if (oldDist > 5f) {
				savedMatrix.set(matrix);
				midPoint(mid, event);
				mode = ZOOM;
				Log.d(TAG, "mode=ZOOM");
			}
			break;
		// 若为DRAG模式，则点击移动图片
		case MotionEvent.ACTION_MOVE:
			if (mode == DRAG) { // movement of first finger
				matrix.set(savedMatrix);
				if (view.getLeft() >= -392) {
					matrix.postTranslate(event.getX() - start.x, event.getY()
							- start.y);
				}
			}
			// 若为ZOOM模式，则点击触摸缩放
			else if (mode == ZOOM) { // pinch zooming
				newDist = spacing(event);
				Log.d(TAG, "newDist=" + newDist);
				if (newDist > 5f) {
					matrix.set(savedMatrix);
					scale = newDist / oldDist; // **//thinking i need to
					// 设置硕放比例和图片的中点位置
					matrix.postScale(scale, scale, mid.x, mid.y);
					Log.e(TAG, "ScaleValue" + scale + "\t\tMidX-" + mid.x
							+ "\t\tMidy-" + mid.y);
				}
			}
			break;
		}
		view.setImageMatrix(matrix);
		Utils.photoMatrix = matrix;
		return true;
	}

	// 计算移动距离
	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return FloatMath.sqrt(x * x + y * y);
	}

	private void midPoint(PointF point, MotionEvent event) {
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x / 2, y / 2);
	}

	// 图层合并
	private Bitmap savaWithFrame() {
		// TODO Auto-generated method stub
		Log.e("Tag", "Ico2");
		Bitmap srcImage = icon2;
		bitmapResult = Bitmap.createBitmap(mapBitmap.getWidth(),
				mapBitmap.getHeight(), Bitmap.Config.ARGB_8888);

		// 自定义边界
		Paint paint = new Paint();
		paint.setStyle(Style.STROKE);
		paint.setColor(Color.RED);
		paint.setStrokeWidth(2);
		paint.setAntiAlias(true);
		Canvas canvas = new Canvas(bitmapResult);
		canvas.drawBitmap(mapBitmap, 0, 0, null);
		Matrix saveMatrix = new Matrix();
		saveMatrix.set(matrix);
		// 对图片进行缩放处理,9种情况
		int BitW = mapBitmap.getWidth();
		int BitH = mapBitmap.getHeight();
		int LlW = relativelayout.getWidth();
		int LlH = relativelayout.getHeight();
		int scale = 1;
		final int heightRatio = Math.round((float) BitH / (float) LlH);
		final int widthRatio = Math.round((float) BitW / (float) LlW);
		if (BitW == LlW & BitH == LlH) {
			saveMatrix.set(matrix);
		} else if (BitW == LlW & BitH < LlH) {
			saveMatrix.postTranslate(0, (BitH - LlH) / 2);
		} else if (BitH == LlH && BitW < LlW) {
			saveMatrix.postTranslate((BitW - LlW) / 2, 0);
		} else if (BitW == LlW && BitH > LlH) {
			scale = heightRatio;
			float tslat = (LlH / BitH - 1);
			saveMatrix.postTranslate((imageFrame.getWidth() - BitW) / 2, 0);
			System.out.print(LlW + ":" + LlH + ":"
					+ addword_relativelayout.getWidth() + ":"
					+ addword_relativelayout.getHeight());
		} else if (BitH == LlH && BitW > LlW) {
			saveMatrix.postTranslate((BitW - LlW) / 2, 0);
		} else if (BitW < LlW && BitH > LlH) {
			saveMatrix.postTranslate((BitW - LlW) / 2, (BitH - LlH) / 2);
		} else if (BitW > LlW && BitH < LlH) {
			saveMatrix.postTranslate((BitW - LlW) / 2, (BitH - LlH) / 2);
		} else if (BitW > LlW && BitH > LlH) {
			saveMatrix.postTranslate((BitW - LlW) / 2, (BitH - LlH) / 2);
		} else if (BitW < LlW && BitH < LlH) {
			saveMatrix.postTranslate((BitW - LlW) / 2, (BitH - LlH) / 2);
		}
		System.out.print("relativelayout.getHeight()"
				+ relativelayout.getHeight());
		canvas.drawBitmap(srcImage, saveMatrix, paint);
		return bitmapResult;
	}

	// 保存修改的图片到sd卡
	private void seavImg() {
		OutputStream fOut = null;
		File file = new File(saveFile.operateName);
		try {
			fOut = new FileOutputStream(file);
			bitmapResult.compress(Bitmap.CompressFormat.JPEG, 90, fOut);
			fOut.flush();
			fOut.close();
			Log.v("save ", "done");
			MediaStore.Images.Media.insertImage(getContentResolver(),
					file.getAbsolutePath(), file.getName(), file.getName());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 气泡栏
	void popwindowword() {
		Context mContext = addWords.this;
		LayoutInflater mLayoutInflater = (LayoutInflater) mContext
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		View popwimdowView = mLayoutInflater.inflate(
				R.layout.beautif_popaddword, null);
		final PopupWindow mPopupWindow = new PopupWindow(popwimdowView,
				android.app.ActionBar.LayoutParams.FILL_PARENT,
				android.app.ActionBar.LayoutParams.WRAP_CONTENT);
		mPopupWindow.setFocusable(true);
		mPopupWindow.setOutsideTouchable(false);
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		hSC = (HorizontalScrollView) findViewById(R.id.addword_horizontalScrollView1);
		ibb1 = (ImageButton) popwimdowView.findViewById(R.id.addword_but1);
		ibb2 = (ImageButton) popwimdowView.findViewById(R.id.addword_but2);
		ibb3 = (ImageButton) popwimdowView.findViewById(R.id.addword_but3);
		ibb4 = (ImageButton) popwimdowView.findViewById(R.id.addword_but4);
		ibb5 = (ImageButton) popwimdowView.findViewById(R.id.addword_but5);
		ibb6 = (ImageButton) popwimdowView.findViewById(R.id.addword_but6);
		ibb7 = (ImageButton) popwimdowView.findViewById(R.id.addword_but7);
		ibb8 = (ImageButton) popwimdowView.findViewById(R.id.addword_but8);
		ibb1.setFocusable(true);
		ibb1.setOnClickListener(this);
		ibb2.setOnClickListener(this);
		ibb3.setOnClickListener(this);
		ibb4.setOnClickListener(this);
		mPopupWindow.showAtLocation(findViewById(R.id.addwords_relativelayout),
				Gravity.LEFT | Gravity.BOTTOM, 0, 65);
	}
}
