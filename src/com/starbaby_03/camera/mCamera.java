package com.starbaby_03.camera;

import static com.googlecode.javacv.cpp.opencv_core.IPL_DEPTH_8U;
import static com.googlecode.javacv.cpp.opencv_core.cvClearMemStorage;
import static com.googlecode.javacv.cpp.opencv_core.cvGetSeqElem;
import static com.googlecode.javacv.cpp.opencv_core.cvLoad;
import static com.googlecode.javacv.cpp.opencv_objdetect.CV_HAAR_DO_CANNY_PRUNING;
import static com.googlecode.javacv.cpp.opencv_objdetect.cvHaarDetectObjects;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.List;

import com.example.starbaby_03.R;
import com.googlecode.javacpp.Loader;
import com.googlecode.javacv.cpp.opencv_objdetect;
import com.googlecode.javacv.cpp.opencv_core.CvMemStorage;
import com.googlecode.javacv.cpp.opencv_core.CvRect;
import com.googlecode.javacv.cpp.opencv_core.CvSeq;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.googlecode.javacv.cpp.opencv_objdetect.CvHaarClassifierCascade;
import com.starbaby_03.main.appMain;
import com.starbaby_03.saveAndSearch.savePhoto;
import com.starbaby_03.scroll.scroll;
import com.starbaby_03.utils.Utils;
import com.starbaby_03.utils.cameraUtils;
import com.starbaby_03.view.photoFrame;

import android.R.color;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.SurfaceTexture;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.ToggleButton;

public class mCamera extends Activity implements OnClickListener,
		OnTouchListener {
	private RelativeLayout camerar_elativeLayout3, camerarelativelayout2,
			camera_relativelayout, camera_linearlayout;
	private FrameLayout camera_framelayout, layout;
	private static HorizontalScrollView cameraHorizointalScrollview;
	private ScrollView scrollview;
	private LinearLayout cameralinearlayout2, cameralinearlayout3;
	private static ImageButton cameraBnt4, cameraBnt5, cameraBnt3, cameraBnt2,
			camerar_Bnt1, camerar_Bnt6, camerar_Bnt7, camerar_Bnt8,
			camerar_Bnt9, camerar_Bnt10, camerar_Bnt11, camerar_Bnt12,
			faceBnt1, faceBnt2, faceBnt3;
	private Camera mCamera = null;
	private boolean isPreview = false;
	private faceShow faceshow;// Ԥ��
	private FaceView faceView;// ��������
	private Bitmap cameraBitmap;
	private Bitmap rotaBitmap;
	private ImageView camera_imageview;
	private int frameNo;
	private TextView mTextView;
	private ImageButton imgBnt1, imgBnt2, imgBnt3;
	private static int FLAG = 0;// 0,1��ͨ����.3,�߿����㡣2,������������.
	private static int FFLAG = 0;
	private static int viewFlag = 2;// ������������������
	private PopupWindow mPopupWindow;
	float sX, sY;
	int sW, sH;
	int ssX, ssY;
	int cameraId;
	int cameraNo = 0;// 0�Ǻ��ã�1��ǰ��
	int flashNo = 0;// 0�ǹر������ ��1�ǿ���
	private ToggleButton toggleButton;
	/***
	 * ����ʱ��Ļص�����
	 */
	private ShutterCallback mShutterCallback = new ShutterCallback() {
		// ����ʱ�������
		@Override
		public void onShutter() {
			// TODO Auto-generated method stub
			Log.i("tag", "myShutterCallback:onShutter...");
		}
	};
	private PictureCallback myRawCallback = new PictureCallback() {
		// �����δѹ��ԭ���ݵĻص�,����Ϊnull
		// ������������Ϊ��ͨ���շ��ص�ͼ��
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			Log.i("tag", "myJpegCallback:onPictureTaken...");
			if (null != data) {
				cameraBitmap = BitmapFactory.decodeByteArray(data, 0,
						data.length);
				mCamera.stopPreview();
				isPreview = false;
			}
			// ����FOCUS_MODE_CONTINUOUS_VIDEO)֮��myParam.set("rotation",
			// 90)ʧЧ��ͼƬ��Ȼ������ת�ˣ�������Ҫ��ת��
			Matrix matrix = new Matrix();
			if (cameraNo == 0) {
				matrix.postRotate((float) 90.0);
			} else if (cameraNo == 1) {
				// ǰ������ͷ
				matrix.postRotate((float) -90.0);
			}
			rotaBitmap = Bitmap.createBitmap(cameraBitmap, 0, 0,
					cameraBitmap.getWidth(), cameraBitmap.getHeight(), matrix,
					false);
			// ����ͼƬ��sdcard
			if (null != rotaBitmap) {
//				new savePhoto().saveJpeg(rotaBitmap);
				cameraUtils.bitmap = rotaBitmap;
				startActivity(new Intent(mCamera.this,operatePic.class));
				mCamera.this.finish();
			}
//			mCamera.startPreview();
//			isPreview = true;
		}
	};
	private PictureCallback myJpegCallback = new PictureCallback()
	// ��jpegͼ�����ݵĻص�,����Ҫ��һ���ص�
	// ������������Ϊ��ͨͷ�����շ��ص�ͼ��
	{
		public void onPictureTaken(byte[] data, Camera camera) {
			Log.i("tag", "myJpegCallback:onPictureTaken...");
			if (null != data) {
				cameraBitmap = BitmapFactory.decodeByteArray(data, 0,
						data.length);// data���ֽ����ݣ����������λͼ
				mCamera.stopPreview();
				isPreview = false;
			} else {
				Log.i("tag", "���data�ǿյ�");
			}
			// ����FOCUS_MODE_CONTINUOUS_VIDEO)֮��myParam.set("rotation",
			// 90)ʧЧ��ͼƬ��Ȼ������ת�ˣ�������Ҫ��ת��
			Matrix matrix = new Matrix();
			if (cameraNo == 0) {
				matrix.postRotate((float) 90.0);
			} else if (cameraNo == 1) {
				matrix.postRotate((float) -90.0);
			}
			Bitmap monBM = Bitmap.createBitmap(cameraBitmap, 0, 0,
					cameraBitmap.getWidth(), cameraBitmap.getHeight(), matrix,
					false);
			// ������
			Bitmap frame = BitmapFactory.decodeResource(getResources(),
					photoFrame.list_MyCamera[frameNo]);
			rotaBitmap = montageBitmap(frame, rotaBitmap, 0, 0);
			// ����ͼƬ��sdcard
			if (null != rotaBitmap) {
				new savePhoto().saveJpeg(rotaBitmap);
			}
			mCamera.startPreview();
			isPreview = true;
		}
	};
	private PictureCallback myFaceJpegCallback = new PictureCallback()
	// ��jpegͼ�����ݵĻص�,����Ҫ��һ���ص�
	// ������������Ϊ�����������շ��ص�ͼ��
	{
		public void onPictureTaken(byte[] data, Camera camera) {
			Log.i("tag", "myJpegCallback:onPictureTaken...");
			if (null != data) {
				cameraBitmap = BitmapFactory.decodeByteArray(data, 0,
						data.length);// data���ֽ����ݣ����������λͼ
				mCamera.stopPreview();
				isPreview = false;
			} else {
				Log.i("tag", "���data�ǿյ�");
			}
			// ����FOCUS_MODE_CONTINUOUS_VIDEO)֮��myParam.set("rotation",
			// 90)ʧЧ��ͼƬ��Ȼ������ת�ˣ�������Ҫ��ת��
			Matrix matrix = new Matrix();
			// matrix.postRotate((float)90.0);
			Bitmap monBM = Bitmap.createBitmap(cameraBitmap, 0, 0,
					cameraBitmap.getWidth(), cameraBitmap.getHeight(), matrix,
					false);
			if (cameraUtils.bit != null) {
				// ������
				rotaBitmap = faceBitmap(cameraUtils.bit, rotaBitmap, 0, 0,
						cameraUtils.X, cameraUtils.Y);
				Log.e("cameraUtils.bit.getWidth()", cameraUtils.bit.getWidth()
						+ "");
				Log.e("cameraUtils.bit.getHeight()",
						cameraUtils.bit.getHeight() + "");
			} else {
				rotaBitmap = Bitmap.createBitmap(cameraBitmap, 0, 0,
						cameraBitmap.getWidth(), cameraBitmap.getHeight(),
						matrix, false);
			}
			// ����ͼƬ��sdcard
			if (null != rotaBitmap) {
				new savePhoto().saveJpeg(rotaBitmap);
				// �ٴν���Ԥ��
				mCamera.startPreview();
				isPreview = true;
				camera_framelayout.removeView(faceView);
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);// ͨ������ı���Ļ��ʾ�ķ���
				viewFlag = 2;// ����������������������
				FLAG = 2;
				done();
			}

		}
	};

	/* ������ͼƬ�����ںϣ�����һ��Bitmap */
	public Bitmap montageBitmap(Bitmap frame, Bitmap src, int x, int y) {
		int w = src.getWidth();
		int h = src.getHeight();
		// �ײ���Ȼ����������ͷš��ٻ���һ�㣬���ͷš����ٲ���Ҫ���ڴ������
		Bitmap newBM = Bitmap.createBitmap(w, h, Config.ARGB_8888);
		Canvas canvas = new Canvas(newBM);
		canvas.drawBitmap(src, x, y, null);
		src.recycle();
		Bitmap bit = Bitmap.createScaledBitmap(frame, w, h, false);
		frame.recycle();
		canvas.drawBitmap(bit, 0, 0, null);
		bit.recycle();
		return newBM;
	}

	/* ��face��ͼƬ�����ںϣ�����һ��Bitmap */
	public Bitmap faceBitmap(Bitmap frame, Bitmap src, int x, int y, float X,
			float Y) {
		int w = src.getWidth();
		int h = src.getHeight();
		// �ײ���Ȼ����������ͷš��ٻ���һ�㣬���ͷš����ٲ���Ҫ���ڴ������
		Bitmap newBM = Bitmap.createBitmap(w, h, Config.ARGB_8888);
		Canvas canvas = new Canvas(newBM);
		canvas.drawBitmap(src, x, y, null);
		src.recycle();
		canvas.drawBitmap(frame, X, Y, null);
		frame.recycle();
		return newBM;
	}

	/**
	 * oncreat����
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		Log.e("cameraId=", cameraId + "");
		done();
	}

	private void done() {
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		if (viewFlag == 1) {
			setContentView(R.layout.camera_camera);
			init();
			faceshow = new faceShow(this, null, null);
			camera_framelayout.addView(faceshow);
			camera_imageview = new ImageView(this);
			camera_imageview.setScaleType(ScaleType.FIT_XY);
			RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
			camera_framelayout.addView(camera_imageview, rl);
			camera_imageview.setVisibility(8);
			listener();
			Log.e("viewFlag=", viewFlag + "");
		} else if (viewFlag == 2) {
			setContentView(R.layout.camera_facepreview);
			init();
			try {
				faceView = new FaceView(this);
			} catch (IOException e) {
				e.printStackTrace();
			}
			faceshow = new faceShow(this, null, faceView);
			camera_framelayout.addView(faceshow);
			camera_framelayout.addView(faceView);
			listener();
			Log.e("viewFlag2=", viewFlag + "");
		}
	}

	// ʵ������ͼ
	void init() {
		if (viewFlag == 1) {
			camerar_Bnt1 = (ImageButton) findViewById(R.id.camerar_Bnt1);
			camerar_Bnt6 = (ImageButton) findViewById(R.id.camerar_Bnt6);
			camerar_Bnt7 = (ImageButton) findViewById(R.id.camerar_Bnt7);
			camerar_Bnt8 = (ImageButton) findViewById(R.id.camerar_Bnt8);
			camerar_Bnt9 = (ImageButton) findViewById(R.id.camerar_Bnt9);
			camerar_Bnt10 = (ImageButton) findViewById(R.id.camerar_Bnt10);
			camerar_Bnt11 = (ImageButton) findViewById(R.id.camerar_Bnt11);
			camerar_Bnt12 = (ImageButton) findViewById(R.id.camerar_Bnt12);
			camera_framelayout = (FrameLayout) findViewById(R.id.camera_framelayout);
			cameraBnt4 = (ImageButton) findViewById(R.id.cameraBnt4);// ǰ�û��������ͷ
			// cameraBnt5 = (ImageButton) findViewById(R.id.cameraBnt5);// ���ص�
			// toggleButton = (ToggleButton) findViewById(R.id.toggleButton);
			cameraBnt3 = (ImageButton) findViewById(R.id.cameraBnt3);// �л���ͷ
			cameraBnt2 = (ImageButton) findViewById(R.id.cameraBnt2);// ����
			camera_relativelayout = (RelativeLayout) findViewById(R.id.camera_relativelayout);
			cameraHorizointalScrollview = (HorizontalScrollView) findViewById(R.id.cameraHorizointalScrollview);
			if (FFLAG == 0) {
				cameraHorizointalScrollview.setVisibility(8);
			} else if (FFLAG == 2) {
				Log.e("viewFlag==1", "did");
				cameraHorizointalScrollview.setVisibility(1);
				cameraHorizointalScrollview
						.setOnTouchListener(new OnTouchListener() {

							@Override
							public boolean onTouch(View v, MotionEvent event) {
								// TODO Auto-generated method stub
								return false;
							}
						});
				horizontalscrollview_listener();
			}
		} else if (viewFlag == 2) {
			camera_framelayout = (FrameLayout) findViewById(R.id.camera_facepreview_framelayout);
			faceBnt1 = (ImageButton) findViewById(R.id.camera_facepreview_cameraBnt2);// ��������
			faceBnt2 = (ImageButton) findViewById(R.id.camera_facepreview_cameraBnt3);// �л���ͷ
			faceBnt3 = (ImageButton) findViewById(R.id.camera_facepreview_camera_relativelayout);// �л�ǰ������ͷ
			camera_linearlayout = (RelativeLayout) findViewById(R.id.camera_facepreview_linearlayout);
			scrollview = (ScrollView) findViewById(R.id.camera_facepreview_cameraHorizointalScrollview);
			scrollview.setVisibility(1);
		}
	}

	// ���ܰ�ť�����¼�
	void listener() {
		if (viewFlag == 1) {
			cameraBnt4.setOnClickListener(this);
			cameraBnt2.setOnClickListener(this);
			cameraBnt2.setOnTouchListener(this);
			cameraBnt3.setOnClickListener(this);
			cameraBnt3.setOnTouchListener(this);
		} else if (viewFlag == 2) {
			faceBnt1.setOnClickListener(this);
			faceBnt2.setOnClickListener(this);
			faceBnt3.setOnClickListener(this);
		}
	}

	// ��ͨͷ�����ļ����¼�
	private void horizontalscrollview_listener() {
		camerar_Bnt1.setOnClickListener(this);
		camerar_Bnt6.setOnClickListener(this);
		camerar_Bnt7.setOnClickListener(this);
		camerar_Bnt8.setOnClickListener(this);
		camerar_Bnt9.setOnClickListener(this);
		camerar_Bnt10.setOnClickListener(this);
		camerar_Bnt11.setOnClickListener(this);
		camerar_Bnt12.setOnClickListener(this);
	}

	// ��ť������̧���ǵĲ���
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (v.getId()) {
		case R.id.cameraBnt2:
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				v.setBackgroundResource(R.drawable.camera_control_btn_shutter_over);
			} else if (event.getAction() == MotionEvent.ACTION_UP) {
				v.setBackgroundResource(R.drawable.camera_control_btn_shutter);
			}
			break;
		case R.id.cameraBnt3:
			if (FLAG == 1 || FLAG == 0) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					v.setBackgroundResource(R.drawable.camera_control_btn_lens_normal_over);
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					v.setBackgroundResource(R.drawable.camera_control_btn_lens_normal);
				}
			}
			if (FLAG == 3) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					v.setBackgroundResource(R.drawable.camera_control_btn_lens_cartoon);
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					v.setBackgroundResource(R.drawable.camera_control_btn_lens_cartoon);
				}
			}
			if (FLAG == 2) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					v.setBackgroundResource(R.drawable.camera_control_btn_lens_quickcapture_over);
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					v.setBackgroundResource(R.drawable.camera_control_btn_lens_quickcapture);
				}
			}
		}
		return false;
	}

	// ��ť�ļ����¼�ʵ����
	@Override
	public void onClick(View v) {
		if (viewFlag == 1) {
			camera_imageview.setVisibility(8);
		}
		switch (v.getId()) {
		case R.id.cameraBnt2:
			// �߿�����
			if (FLAG == 3) {
				if (isPreview && mCamera != null) {
					mCamera.takePicture(mShutterCallback, null, myJpegCallback);
				}
			}
			// ��ͨ����,��һ�ν���cameraĬ��Ϊ��ͨ����
			else if (FLAG == 0 || FLAG == 1) {
				if (isPreview && mCamera != null) {
					mCamera.takePicture(mShutterCallback, null, myRawCallback);
				}
			}
			break;
		case R.id.cameraBnt3:
			showpopwindow();
			if (FFLAG == 5) {
				camera_imageview.setVisibility(1);
			}
			camera_imageview.setVisibility(8);
			break;
		case R.id.camerar_Bnt1:
			frameNo = 0;
			FFLAG = 5;
			Log.e("FFLAG=", FFLAG + "");
			break;
		case R.id.camerar_Bnt6:
			frameNo = 1;
			FFLAG = 5;
			Log.e("FFLAG=", FFLAG + "");
			break;
		case R.id.camerar_Bnt7:
			frameNo = 2;
			FFLAG = 5;
			Log.e("FFLAG=", FFLAG + "");
			break;
		case R.id.camerar_Bnt8:
			frameNo = 3;
			FFLAG = 5;
			break;
		case R.id.camerar_Bnt9:
			frameNo = 4;
			FFLAG = 5;
			break;
		case R.id.camerar_Bnt10:
			frameNo = 5;
			FFLAG = 5;
			break;
		case R.id.camerar_Bnt11:
			frameNo = 6;
			FFLAG = 5;
			break;
		case R.id.camerar_Bnt12:
			frameNo = 7;
			FFLAG = 5;
			break;
		case R.id.popcamera_imgBnt1:// ��ͨ����
			if (viewFlag == 1) {
				mPopupWindow.dismiss();
				camera_imageview.setVisibility(8);
				cameraHorizointalScrollview.setVisibility(8);
				cameraBnt3
						.setBackgroundResource(R.drawable.camera_control_btn_lens_normal);
				FLAG = 1;
				FFLAG = 4;
			} else if (viewFlag == 2) {
				mPopupWindow.dismiss();
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// ͨ������ı���Ļ��ʾ�ķ���
				viewFlag = 1;
				FFLAG = 0;
				FLAG = 1;
			}
			break;
		case R.id.popcamera_imgBnt3:// ��ͨ��������
			if (viewFlag == 1) {
				mPopupWindow.dismiss();
				cameraBnt3
						.setBackgroundResource(R.drawable.camera_control_btn_lens_cartoon);
				cameraHorizointalScrollview.setVisibility(1);
				Log.e("viewFlag==1", "done");
				cameraHorizointalScrollview
						.setOnTouchListener(new OnTouchListener() {

							@Override
							public boolean onTouch(View v, MotionEvent event) {
								// TODO Auto-generated method stub
								return false;
							}
						});
				FLAG = 3;
				horizontalscrollview_listener();
			} else if (viewFlag == 2) {
				mPopupWindow.dismiss();
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// ͨ������ı���Ļ��ʾ�ķ���
				viewFlag = 1;
				FFLAG = 2;
				FLAG = 3;
				Log.e("one piece", "����.D.·��");
			}
			break;
		case R.id.popcamera_imgBnt2:// ��������
			if (viewFlag == 1) {
				mPopupWindow.dismiss();
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);// ͨ������ı���Ļ��ʾ�ķ���
				camera_imageview.setVisibility(8);
				viewFlag = 2;// ����������������������
				FLAG = 2;
			} else if (viewFlag == 2) {
				mPopupWindow.dismiss();
			}
			break;
		case R.id.camera_facepreview_cameraBnt2:
			if (isPreview && mCamera != null) {
				mCamera.takePicture(mShutterCallback, null, myFaceJpegCallback);
			}
			break;
		case R.id.camera_facepreview_cameraBnt3:
			showpopwindow();
			FFLAG = 6;
			break;
		case R.id.cameraBnt4:// 0,1:ǰ������ͷ �л�
			cameraId = mCamera.getNumberOfCameras();
			Log.e("cameraId=", cameraId + "");
			if (cameraId > 1) {
				if (cameraNo == 1) {
					cameraNo = 0;
				} else {
					cameraNo = 1;
				}
			} else {
				cameraNo = 0;
			}
			mCamera.stopPreview();
			mCamera.release();
			mCamera = null;
			done();
			break;
		case R.id.camera_facepreview_camera_relativelayout: // 2:ǰ������ͷ �л�
			cameraId = mCamera.getNumberOfCameras();
			Log.e("cameraId=", cameraId + "");
			if (cameraId > 1) {
				if (cameraNo == 1) {
					cameraNo = 0;
				} else {
					cameraNo = 1;
				}
			} else {
				cameraNo = 0;
			}
			mCamera.stopPreview();
			mCamera.release();
			mCamera = null;
			done();
			break;
		}
		if (FFLAG == 5) {
			camera_imageview.setVisibility(1);
			Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
					photoFrame.list_MyCamera[frameNo]);
			camera_imageview.setImageBitmap(bitmap);
		}

	}

	/***
	 * surfaceview������ʾԤ������
	 * 
	 * @author Administrator 1.��ͨ�Ϳ�ͨ��ͷ 2.��������ͷ
	 */
	class faceShow extends SurfaceView implements SurfaceHolder.Callback {
		private SurfaceHolder mHolder;
		private Camera.PreviewCallback previewCallback;

		public faceShow(Context context, AttributeSet attrs,
				Camera.PreviewCallback previewCallback) {
			super(context, attrs);
			this.previewCallback = previewCallback;
			mHolder = getHolder();
			mHolder.addCallback(this);
			mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}

		// SurfaceView����ʱ/����ʵ������Ԥ�����汻����ʱ���÷��������á�
		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			mCamera = Camera.open(cameraNo);
			try {
				mCamera.setPreviewDisplay(holder);
			} catch (IOException e) {
				if (null != mCamera) {
					mCamera.release();
					mCamera = null;
				}
				e.printStackTrace();
			}
		}

		// ���汻�ı�
		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int w,
				int h) {
			if (isPreview) {
				mCamera.stopPreview();
			}
			if (null != mCamera) {
				double scale = Double.MAX_VALUE;
				double scaleScreen = Double.MAX_VALUE;
				// ����,��С����֪��,������������Ϳ�ʼԤ����
				Camera.Parameters parameters = mCamera.getParameters();
				// ����ú��ʵ�previewSizeȻ��ȥѡ��
				List<Size> sizes = parameters.getSupportedPreviewSizes();
				if (viewFlag == 1) {
					for (int i = 0; i < sizes.size(); i++) {
						Size fitsize = sizes.get(i);
						scale = (double) fitsize.width / fitsize.height;
						// ����״̬�£�framelayout��ȡ�Ŀ�߶Ե���
						scaleScreen = (double) camera_framelayout.getHeight()
								/ camera_framelayout.getWidth();
						if (scale == scaleScreen) {
							parameters.setPictureSize(fitsize.width,
									fitsize.height);
							parameters.setPreviewSize(fitsize.width,
									fitsize.height);
							Log.e("fitsize=", fitsize.width + ":"
									+ fitsize.height);
							// ����״̬�£�framelayout��ȡ�Ŀ�߶Ե���
							cameraUtils.width = fitsize.height;
							cameraUtils.height = fitsize.width;
							break;
						}
					}
					mCamera.setDisplayOrientation(90); // ��������״̬�£�ԭʼ�����뾵ͷ��90�㡣������Ҫ��ת�»���Ƕ�.
				} else if (viewFlag == 2) {
					// ���Ǻ���״̬�£�����Ҫ��ת.
					for (int i = 0; i < sizes.size(); i++) {
						Size fitsize = sizes.get(i);
						scale = (double) fitsize.width / fitsize.height;
						// ����״̬�£�framelayout��ȡ�Ŀ�߲���Ҫ�Ե���
						scaleScreen = (double) camera_framelayout.getWidth()
								/ camera_framelayout.getHeight();
						if (scale == scaleScreen) {
							parameters.setPictureSize(fitsize.width,
									fitsize.height);
							parameters.setPreviewSize(fitsize.width,
									fitsize.height);
							Log.e("fitsize=", fitsize.width + ":"
									+ fitsize.height);
							cameraUtils.width = fitsize.width;
							cameraUtils.height = fitsize.height;
							break;
						}
					}
				}
				// �����
				if (flashNo == 1) {
					parameters.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
				} else if (flashNo == 0) {
					parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
				}
				mCamera.setParameters(parameters);
				if (previewCallback != null) {
					mCamera.setPreviewCallbackWithBuffer(previewCallback);
					Camera.Size size = parameters.getPreviewSize();
					Log.e("size.height=", size.height + "");
					Log.e("size.width=", size.width + "");
					byte[] data = new byte[size.height
							* size.width
							* ImageFormat.getBitsPerPixel(parameters
									.getPreviewFormat()) / 8];
					mCamera.addCallbackBuffer(data);
					Log.e("parameters=", "refresh");
				}
				mCamera.startPreview();
				isPreview = true;
			}
		}

		// ���汻�ݻ�
		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			Log.e("this", "13");
			if (null != mCamera) {
				mCamera.setPreviewCallback(null);
				/*
				 * ������PreviewCallbackʱ���������ǰ��Ȼ�˳����� ����ʵ����ע�͵�Ҳû��ϵ
				 */
				mCamera.stopPreview();
				isPreview = false;
				mCamera.release();
				mCamera = null;
			}
		}
	}

	/***
	 * 
	 * ������⣬���װ����
	 * 
	 * @author Administrator
	 * 
	 */
	class FaceView extends SurfaceView implements Camera.PreviewCallback,
			SurfaceHolder.Callback {
		public static final int SUBSAMPLING_FACTOR = 4;
		public int[] listheadwear = { R.drawable.frame1, R.drawable.frame2 };
		private int RandomNo;
		private IplImage grayImage;// intelͼ����
		private CvHaarClassifierCascade classifier;// ��ȡCvHaarClassifierCascade�������ļ�����������⣨facedetect��
		private CvMemStorage storage;// ��̬�ڴ�洢����������
		private CvSeq faces;
		private SurfaceHolder mHolder;
		private int x, y, w, h;
		private float scaleX, scaleY;

		public FaceView(mCamera context) throws IOException {
			super(context);
			// ʵ����SurfaceHolder
			mHolder = getHolder();
			mHolder.addCallback(this);
			mHolder.setFormat(PixelFormat.TRANSPARENT); // ����Ϊ͸��
			setZOrderOnTop(true);// ����Ϊ����
			// ���ط������ļ���Java��Դ��
			File classifierFile = Loader.extractResource(getClass(),
					"/com/starbaby_03/camera/haarcascade_frontalface_alt.xml",
					context.getCacheDir(), "classifier", ".xml");
			if (classifierFile == null || classifierFile.length() <= 0) {
				throw new IOException(
						"Could not extract the classifier file from Java resource.");
			}
			// Ԥ���ص�opencv_objdetectģ�鹤����һ����֪��ȱ�ݡ�
			Loader.load(opencv_objdetect.class);
			classifier = new CvHaarClassifierCascade(
					cvLoad(classifierFile.getAbsolutePath()));
			classifierFile.delete();
			if (classifier.isNull()) {
				throw new IOException("Could not load the classifier file.");
			}
			storage = CvMemStorage.create();
		}

		Camera.Size size;
		public void onPreviewFrame(final byte[] data, final Camera camera) {
			try {
				size = camera.getParameters().getPreviewSize();
				processImage(data, size.width, size.height);// 800 480
				camera.addCallbackBuffer(data);
			} catch (RuntimeException e) {
				// ����п���ֻ�Ǳ��ͷ�,���ԡ�
			}
		}

		int imageWidth, imageHeight, dataStride, imageStride, f, dataLine,
				imageLine;

		protected void processImage(byte[] data, int width, int height) {
			// ����,downsample���ǵ�image,������ת���ɻҶ�IplImage
			f = SUBSAMPLING_FACTOR;// 4
			if (grayImage == null || grayImage.width() != width / f
					|| grayImage.height() != height / f) {
				grayImage = IplImage.create(width / f, height / f,
						IPL_DEPTH_8U, 1);// ���ɻҶ�ͼƬ�����ڲ�׽����
			}
			imageWidth = grayImage.width();// 200
			imageHeight = grayImage.height();
			dataStride = f * width;
			imageStride = grayImage.widthStep();// 200
			ByteBuffer imageBuffer = grayImage.getByteBuffer();
			for (int y = 0; y < imageHeight; y++) {
				dataLine = y * dataStride;// 380800
				imageLine = y * imageStride;// 23800
				for (int x = 0; x < imageWidth; x++) {
					imageBuffer.put(imageLine + x, data[dataLine + f * x]);
				}
			}
			faces = cvHaarDetectObjects(grayImage, classifier, storage, 1.1, 3,
					CV_HAAR_DO_CANNY_PRUNING);
			postInvalidate();
			cvClearMemStorage(storage);
			onDraw();
		}

		// ������Ҫ���� �������Ĳ��񣬲����ڲ��������������canvas����װ���ͼƬ��ʱ����Ϊ300*300������200*200����ƥ��
		// ��ɫ������������

		public void onDraw() {
			scaleX = (float) getWidth() / grayImage.width();
			scaleY = (float) getHeight() / grayImage.height();
			int total = faces.total();
			if (total == 0) {
				new Thread(new thread()).start();
			}
			for (int i = 0; i < total; i++) {
				CvRect r = new CvRect(cvGetSeqElem(faces, i));
				// ���������������Ͻǵ�(X,Y)���꣬�����Ͻ����꿪ʼ�����W�͸�H
				x = r.x();
				y = r.y();
				w = r.width();
				h = r.height();
				new Thread(new MyThread()).start();
			}
		}

		// û������ʱ������
		class thread implements Runnable {

			@Override
			public void run() {
				Log.e("this", "9");
				Canvas canvas = mHolder.lockCanvas();
				Paint paint = new Paint();
				paint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
				canvas.drawPaint(paint);
				paint.setXfermode(new PorterDuffXfermode(Mode.SRC));
				mHolder.unlockCanvasAndPost(canvas);
			}

		}

		// ��������ʱ����canvas����װ�������� ��0���Ǻ�������ͷ��2����ǰ������ͷ
		class MyThread implements Runnable {
			@Override
			public void run() {
				// ���ϲ�surfaceview�ϻ�ͼ����װ����
				Canvas canvas = mHolder.lockCanvas();
				Paint paint = new Paint();
				paint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
				canvas.drawPaint(paint);
				paint.setXfermode(new PorterDuffXfermode(Mode.SRC));
				Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
						R.drawable.frame1);
				int width = bitmap.getWidth();
				int hight = bitmap.getHeight();
				Bitmap bitmap2 = bitmap.createScaledBitmap(bitmap,
						(int) ((width) * (float) (scaleX * w / 200)),
						(int) (hight * (float) (scaleY * h / 200)), false);
				cameraUtils.bit = bitmap
						.createScaledBitmap(
								bitmap2,
								(int) ((width) * (float) (scaleX * w / 200) * f / scaleX),
								(int) (hight * (float) (scaleY * h / 200) * f / scaleY),
								false);
				if (cameraNo == 0) {
					canvas.drawBitmap(
							bitmap2,
							x
									* scaleX
									- (((int) ((width) * (float) (scaleX * w / 200)) - w
											* scaleX)) / 2,
							y
									* scaleY
									- (((int) (hight * (float) (scaleY * h / 200)) - h
											* scaleY)) / 2, paint);
					Paint paint2 = new Paint();
					paint2.setColor(Color.BLUE);
					paint2.setTextSize(40f);
					canvas.drawText(
							scaleX + ":" + scaleY + ":" + bitmap2.getWidth()
									+ ":" + bitmap2.getHeight(), 100, 200,
							paint2);
					// ��������ĺ�ɫ�����׼�ġ�
					// Paint paint4 = new Paint();
					// paint4.setColor(Color.RED);
					// paint4.setTextSize(20);
					// paint4.setStrokeWidth(2);
					// paint4.setStyle(Paint.Style.STROKE);
					// canvas.drawRect(x * scaleX, y * scaleY, (x+w)*scaleX,
					// (y+h)*scaleY, paint4);
				} else if (cameraNo == 1) {
					int XW = getWindowManager().getDefaultDisplay().getWidth();
					// Log.e("cameraNo=", cameraNo+"");
					canvas.translate(-((x * scaleX - XW / 2) * 2 + w * scaleX),
							0);
					canvas.drawBitmap(
							bitmap2,
							x
									* scaleX
									- (((int) ((width) * (float) (scaleX * w / 200)) - w
											* scaleX)) / 2,
							y
									* scaleY
									- (((int) (hight * (float) (scaleY * h / 200)) - h
											* scaleY)) / 2, paint);
					// Paint paint2=new Paint();
					// paint2.setColor(Color.BLUE);
					// paint2.setTextSize(40f);
					// canvas.drawText(w+":"+scaleX+":"+scaleY+":"+w*scaleX+":"+h*scaleY,
					// 100, 200, paint2);
					// ��������ĺ�ɫ�����׼�ġ�
					Paint paint4 = new Paint();
					paint4.setColor(Color.RED);
					paint4.setTextSize(20);
					paint4.setStrokeWidth(2);
					paint4.setStyle(Paint.Style.STROKE);
					canvas.drawRect(x * scaleX, y * scaleY, (x + w) * scaleX,
							(y + h) * scaleY, paint4);
				}
				cameraUtils.X = (x * scaleX - (((int) ((width) * (float) (scaleX
						* w / 200)) - w * scaleX)) / 2)
						* (float) (1 + (1 - scaleX / f) * 1.5);
				cameraUtils.Y = (y * scaleY - (((int) (hight * (float) (scaleY
						* h / 200)) - h * scaleY)) / 2)
						* (float) (1 + (1 - scaleY / f) * 1.5);
				mHolder.unlockCanvasAndPost(canvas);
			}
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			// TODO Auto-generated method stub

		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			// TODO Auto-generated method stub

		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			// TODO Auto-generated method stub

		}
	}

	/**
	 * 
	 * POPUPWINDOW��ʾ�������� 1.��ͨ���� 2.��ͨ���� 3.������������
	 */
	void showpopwindow() {
		Context mContext = mCamera.this;
		LayoutInflater mLayoutInflater = (LayoutInflater) mContext
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		View popwimdowView = mLayoutInflater.inflate(R.layout.camera_showpop,
				null);
		mPopupWindow = new PopupWindow(popwimdowView,
				android.app.ActionBar.LayoutParams.WRAP_CONTENT,
				android.app.ActionBar.LayoutParams.WRAP_CONTENT);
		mPopupWindow.setFocusable(true);
		mPopupWindow.setOutsideTouchable(false);
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		mTextView = (TextView) popwimdowView
				.findViewById(R.id.popupcamera_textview);
		imgBnt1 = (ImageButton) popwimdowView
				.findViewById(R.id.popcamera_imgBnt1);
		imgBnt2 = (ImageButton) popwimdowView
				.findViewById(R.id.popcamera_imgBnt2);
		imgBnt3 = (ImageButton) popwimdowView
				.findViewById(R.id.popcamera_imgBnt3);
		imgBnt1.setFocusable(true);
		imgBnt2.setFocusable(true);
		imgBnt3.setFocusable(true);
		// ��ͨ����
		imgBnt1.setOnClickListener(this);
		// ��ͨ��ͷ
		imgBnt3.setOnClickListener(this);
		imgBnt2.setOnClickListener(this);
		if (viewFlag == 1) {
			mPopupWindow.showAtLocation(
					findViewById(R.id.camera_relativelayout), Gravity.LEFT
							| Gravity.BOTTOM, 20, 80);
		} else if (viewFlag == 2) {
			mPopupWindow.showAtLocation(
					findViewById(R.id.camera_facepreview_linearlayout),
					Gravity.RIGHT | Gravity.BOTTOM, 80, 20);
		}
	}

	/**
	 * ��BACK��
	 */
	@Override
	public void onBackPressed()
	// �����а����ؼ�ʱҪ�ͷ��ڴ�
	{
		super.onBackPressed();
		mCamera.this.finish();
		viewFlag = 1;
		FFLAG = 0;
		FLAG = 0;
		Intent intent = new Intent(this, appMain.class);
		startActivity(intent);
	}
}
