package com.starbaby_03.beautify;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.example.starbaby_03.R;
import com.example.starbaby_03.R.id;

import com.starbaby_03.scroll.scroll;
import com.starbaby_03.utils.Utils;
import com.starbaby_03.utils.beautyUtils;
import com.starbaby_03.utils.saveFile;
import com.starbaby_03.view.MainActivity_ImgBnt;
import com.starbaby_03.view.frameView;

import android.R.integer;
import android.app.Activity;
import android.app.DownloadManager.Request;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Paint.Style;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.StaticLayout;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class addFrame extends Activity implements OnClickListener,
		OnTouchListener {
	/** Called when the activity is first created. */
	/** Called when the activity is first created. */
	private Button btnSetImg, btnOrientation, btnCrop, btnSave, btnFramList,
			btnSaveFull, translateButton;
	private Bitmap bitmapPhoto = null, bitmapResult = null, srcImage = null,
			icon = null, backIcon = null;
	private ImageView imgPhoto, imgFrame;
	// 这些矩阵将被用来移动和缩放图像
	private Matrix matrix = new Matrix();
	private Matrix matrix2 = new Matrix();
	private Matrix savedMatrix = new Matrix();
	public static String fileNAME;
	public static int framePos = 0;
	public static int Flag = 0;
	private float scale = 0;
	private float newDist = 0;

	private String TAG = this.getClass().getSimpleName();

	private static final int NONE = 0;
	private static final int DRAG = 1;
	private static final int ZOOM = 2;
	private int mode = NONE;

	// 进行缩放
	private PointF start = new PointF();
	private PointF mid = new PointF();
	float oldDist = 1f;
	private int rotate = 0;
	private static final int HORIZONTAL = 0;
	private static final int VERTICAL = 1;

	// 图像查看和图像的高度和宽度
	private HorizontalScrollView hScrollView;
	private LinearLayout linearLayout;
	private ImageButton iBnt1, iBnt2, iBnt3, iBnt4, iBnt5, iBnt6;
	private MainActivity_ImgBnt iBnt7, iBnt8;
	private int imgBnt1_textcolor = Color.WHITE;
	private int imgBnt2_textcolor = Color.GRAY;
	private float imgBnt_textsize = 24f;
	private int Frame1 = 1;

	private int imgPhotoHeight = 0;
	private int imgPhotoWidth = 0;

	private float w = 0;
	private float h = 0;
	private RelativeLayout relativeFinalImage, LL;
	private TextView tv1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.beautify_addframe);

		hScrollView = (HorizontalScrollView) findViewById(R.id.addFrame_horizontalScrollView1);
		linearLayout = (LinearLayout) findViewById(R.id.addFrame_linearlayout);
		relativeFinalImage = (RelativeLayout) findViewById(R.id.addFrame_RelativeLayout2);// 显示图片区域
		imgPhoto = (ImageView) findViewById(R.id.addFrame_heyelloFrontImageViewPhoto);// 底层图片
		imgFrame = (ImageView) findViewById(R.id.addFrame_heyelloFrontImageViewFrame);// 相框
		tv1 = (TextView) findViewById(R.id.addFrame_tv);
		iBnt1 = (ImageButton) findViewById(R.id.addFrame_but1);
		iBnt2 = (ImageButton) findViewById(R.id.addFrame_but2);
		iBnt3 = (ImageButton) findViewById(R.id.addFrame_but3);
		iBnt4 = (ImageButton) findViewById(R.id.addFrame_but4);
		iBnt5 = (ImageButton) findViewById(R.id.addFrame_but5);
		iBnt6 = (ImageButton) findViewById(R.id.addFrame_but6);
		iBnt7 = (MainActivity_ImgBnt) findViewById(R.id.addFrame_but7);
		iBnt8 = (MainActivity_ImgBnt) findViewById(R.id.addFrame_but8);
		imgFrame.setVisibility(8);
		setPhoto();
		listener();

	}

	void listener() {

		hScrollView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_MOVE) {
					int x = (int) event.getX();
					int y = (int) event.getY();
					Log.e("Coordinates= ", "(" + x + ", " + y + ")");

				}
				return false;
			}
		});
		iBnt1.setOnClickListener(this);
		iBnt2.setOnClickListener(this);
		iBnt3.setOnClickListener(this);
		iBnt4.setOnClickListener(this);
		iBnt5.setOnClickListener(this);
		iBnt6.setOnClickListener(this);
		iBnt7.setOnClickListener(this);
		iBnt7.setOnTouchListener(new OnTouchListener() {

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
		iBnt8.setOnClickListener(this);
		iBnt8.setClickable(false);
	}

	public void setPhoto() {
		imgPhoto.setVisibility(1);
		File file = new File(saveFile.operateName);
		if (bitmapPhoto != null)
			// //回收原图
			bitmapPhoto.recycle();
		bitmapPhoto = null;
		try {
			bitmapPhoto = BitmapFactory.decodeStream(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 给图片添加透明背景。适合frame边框的布局，防止手指触摸发生变回原图大小，并错位
		backIcon = BitmapFactory.decodeResource(getResources(),
				R.drawable.addframebackground);
		icon = Bitmap.createBitmap(beautyUtils.layoutWidth,
				beautyUtils.layoutHight, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(icon);
		canvas.drawBitmap(backIcon, 0, 0, null);
		canvas.drawBitmap(bitmapPhoto,
				(beautyUtils.layoutWidth - bitmapPhoto.getWidth()) / 2,
				(beautyUtils.layoutHight - bitmapPhoto.getHeight()) / 2, null);
		canvas.save(Canvas.ALL_SAVE_FLAG);
		canvas.restore();

		// center(true, true, imgPhoto);
		imgPhoto.setImageBitmap(icon);
		System.out.print("imgPhotoHeight" + imgPhoto.getPaddingTop());
		// matrix();
	}

	public Matrix matrix() {
		imgPhoto.setVisibility(1);
		File file = new File(saveFile.operateName);
		if (bitmapPhoto != null)
			// 回收原图
			bitmapPhoto.recycle();
		bitmapPhoto = null;
		imgPhotoHeight = imgPhoto.getHeight() - imgPhoto.getPaddingTop()
				- imgPhoto.getPaddingBottom();
		imgPhotoWidth = imgPhoto.getWidth() - imgPhoto.getPaddingLeft()
				- imgPhoto.getPaddingRight();
		scale = 1;
		Matrix mat = new Matrix();
		matrix.reset();
		try {
			final BitmapFactory.Options o = new BitmapFactory.Options();
			// 由于图片太大，会产生内存溢出的问题
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(file), null, o);

			if (o.outHeight >= 1944 || o.outWidth >= 1944) {
				scale = 2;

			} else if (o.outWidth * imgPhotoHeight > imgPhotoWidth
					* o.outHeight) {
				scale = (float) imgPhotoHeight / (float) o.outHeight;
			} else {
				scale = (float) imgPhotoWidth / (float) o.outWidth;
			}
			final BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = (int) scale;
			bitmapPhoto = BitmapFactory.decodeStream(new FileInputStream(file),
					null, o2);
			// imgPhoto.setImageBitmap(bitmapPhoto);
			w = bitmapPhoto.getWidth();
			h = bitmapPhoto.getHeight();
		} catch (Exception e) {
			e.printStackTrace();
		}

		float widthScale = Math.min(imgPhotoWidth / w, scale);
		float heightScale = Math.min(imgPhotoHeight / h, scale);
		float scale1 = Math.min(widthScale, heightScale);

		matrix.postConcat(mat);
		matrix.postScale(scale1, scale1);
		matrix.postTranslate((imgPhotoWidth - w * scale) / 2F,
				(imgPhotoHeight - h * scale) / 2F);
		savedMatrix.set(matrix);
		// 设置获取的图片在屏幕中间
		return matrix;

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		iBnt8.setClickable(true);
		iBnt8.setBackgroundResource(R.drawable.ensuer_over);

		// 获取图片
		if (v == iBnt1) {
			imgPhoto.setOnTouchListener(this);
			imgFrame.invalidate();
			setPhoto();
			// imgPhoto.invalidate();
			framePos = 1;
			imgFrame.setVisibility(1);
			Flag = 1;

			iBnt8.setOnTouchListener(new OnTouchListener() {

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
		} else if (v == iBnt2) {
			imgPhoto.setOnTouchListener(this);
			imgFrame.invalidate();
			setPhoto();
			framePos = 2;
			imgFrame.setVisibility(1);
			Flag = 1;
			iBnt8.setOnTouchListener(new OnTouchListener() {

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
		} else if (v == iBnt3) {
			imgPhoto.setOnTouchListener(this);
			imgFrame.invalidate();
			setPhoto();
			framePos = 3;
			imgFrame.setVisibility(1);
			Flag = 1;
			iBnt8.setOnTouchListener(new OnTouchListener() {

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
		} else if (v == iBnt4) {
			imgPhoto.setOnTouchListener(this);
			imgFrame.invalidate();
			setPhoto();
			framePos = 4;
			imgFrame.setVisibility(1);
			Flag = 1;
			iBnt8.setOnTouchListener(new OnTouchListener() {

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
		} else if (v == iBnt5) {
			imgPhoto.setOnTouchListener(this);
			imgFrame.invalidate();
			setPhoto();
			framePos = 5;
			imgFrame.setVisibility(1);
			Flag = 1;
			iBnt8.setOnTouchListener(new OnTouchListener() {

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
		} else if (v == iBnt6) {
			imgPhoto.setOnTouchListener(this);
			imgFrame.invalidate();
			setPhoto();
			framePos = 6;
			imgFrame.setVisibility(1);
			Flag = 1;
			iBnt8.setOnTouchListener(new OnTouchListener() {

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
		} else if (v == iBnt7) {
			Intent intent2 = new Intent(this, scroll.class);
			intent2.putExtra(Utils.INTENT_FLAG, Utils.CROP_IMAGE);
			startActivity(intent2);
			this.finish();
		}

		else if (v == iBnt8) {
			Intent intent = new Intent(this, scroll.class);
			intent.putExtra(Utils.INTENT_FLAG, Utils.CROP_IMAGE);
			savaWithFrame();
			seavImg();
			startActivity(intent);
			this.finish();
		} else if (v == translateButton) {
			doHorizontal();
		}
	}

	private Bitmap savaWithFrame() {
		// TODO Auto-generated method stub
		Bitmap srcImage = icon;
		bitmapResult = Bitmap.createBitmap(frameView.width,
				frameView.scaleheigth, Bitmap.Config.ARGB_8888);
		// 自定义边界
		Paint paint = new Paint();
		paint.setStyle(Style.STROKE);
		paint.setColor(Color.RED);
		paint.setStrokeWidth(2);
		paint.setAntiAlias(true);

		Canvas canvas = new Canvas(bitmapResult);
		canvas.drawBitmap(srcImage, matrix, paint);
		canvas.drawBitmap(frameView.mOriginalBitmap, 0, 0, null);
		return bitmapResult;

	}

	private void seavImg() {
		OutputStream fOut = null;
		File file = new File(saveFile.operateName);
		try {
			fOut = new FileOutputStream(file);
			bitmapResult.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
			fOut.flush();
			fOut.close();
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

	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		ImageView view = (ImageView) v;
		view.setScaleType(ImageView.ScaleType.MATRIX);
		// Handle touch events here...
		switch (event.getAction() & MotionEvent.ACTION_MASK) {

		case MotionEvent.ACTION_DOWN: // first finger down only
			savedMatrix.set(matrix);
			start.set(event.getX(), event.getY());
			Log.d(TAG, "mode=DRAG");
			mode = DRAG;
			break;
		case MotionEvent.ACTION_UP: // first finger lifted
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

		case MotionEvent.ACTION_MOVE:
			if (mode == DRAG) { // movement of first finger
				matrix.set(savedMatrix);
				if (view.getLeft() >= -392) {
					matrix.postTranslate(event.getX() - start.x, event.getY()
							- start.y);
				}
			} else if (mode == ZOOM) { // pinch zooming
				newDist = spacing(event);
				Log.d(TAG, "newDist=" + newDist);
				if (newDist > 5f) {
					matrix.set(savedMatrix);
					scale = newDist / oldDist; // **//thinking i need to
												// play around with this
												// value to limit it**
					matrix.postScale(scale, scale, mid.x, mid.y);
					// 保证每次有2只手指时让图片展示在中间
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

	private void doHorizontal() {

		// matrix.postRotate(-90);
		// center(true, true, imgFrame);
		// rotate = HORIZONTAL;
		// imgFrame.setImageMatrix(matrix);

	}

	protected void center(boolean horizontal, boolean vertical, ImageView view) {

		final Matrix m = matrix;

		RectF rect = new RectF(0, 0, w, h);

		m.mapRect(rect);

		float height = rect.height();
		float width = rect.width();

		float deltaX = 0, deltaY = 0;

		if (vertical) {
			int viewHeight = imgPhotoHeight;
			if (height < viewHeight) {
				deltaY = (viewHeight - height) / 2 - rect.top;
			} else if (rect.top > 0) {
				deltaY = -rect.top;
			} else if (rect.bottom < viewHeight) {
				deltaY = imgPhotoHeight - rect.bottom;
			}
		}

		if (horizontal) {
			int viewWidth = imgPhotoWidth;
			if (width < viewWidth) {
				deltaX = (viewWidth - width) / 2 - rect.left;
			} else if (rect.left > 0) {
				deltaX = -rect.left;
			} else if (rect.right < viewWidth) {
				deltaX = viewWidth - rect.right;
			}
		}
		matrix.postTranslate(deltaX, deltaY);
		Utils.photoMatrix = matrix;
	}

}