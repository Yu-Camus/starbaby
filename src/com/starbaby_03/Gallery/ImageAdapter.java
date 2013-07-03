package com.starbaby_03.Gallery;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import com.example.starbaby_03.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
	
	private Context mContext; // ����Context
	private Vector<Bitmap> mImageIds; // ����һ��������ΪͼƬԴ
	private Vector<Boolean> mImage_bs = new Vector<Boolean>(); // ����һ��������Ϊѡ���������
	private boolean multiChoose; // ��ʾ��ǰ�������Ƿ�������ѡ
	public List<String> pathList = null;
	private Set<Integer> imageIndex = null;

	public ImageAdapter(Context c, boolean isMulti, String path) {
		mContext = c;
		multiChoose = isMulti;
		mImageIds = this.readSDCard(path);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mImageIds.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView;
		if (convertView == null) {
			imageView = new ImageView(mContext); // ��ImageView������Դ
			imageView.setLayoutParams(new GridView.LayoutParams(150, 150)); // ���ò���ͼƬ
			imageView.setScaleType(ImageView.ScaleType.FIT_CENTER); // ������ʾ��������
		} else {
			imageView = (ImageView) convertView;
		}
		imageView.setImageDrawable(makeBmp(mImageIds.elementAt(position),
				mImage_bs.elementAt(position), position));
		return imageView;
	}

	private LayerDrawable makeBmp(Bitmap mainBmp, boolean isChosen, int position) {
		// ����isChosen��ѡȡ�Թ���ͼƬ
		Bitmap seletedBmp;
		if(imageIndex == null){
			imageIndex = new TreeSet<Integer>();
		}
		if (isChosen == true) {
			seletedBmp = BitmapFactory.decodeResource(mContext.getResources(),
					R.drawable.grid_check_on);
			imageIndex.add(position);
//			Log.i("ѡ��",
//					"��" + position + "��Ŀ��ѡ��" + "set��С ��" + imageIndex.size());
		} else {
			seletedBmp = BitmapFactory.decodeResource(mContext.getResources(),
					R.drawable.grid_check_off);
			if (imageIndex.size() > 0) {
				imageIndex.remove(position);
//				Log.i("ȡ��",
//						"��" + position + "��ѡ�б�ȡ��" + "set��С ��"
//								+ imageIndex.size());
			}
		}
		// ��������ͼ
		Drawable[] array = new Drawable[2];
		array[0] = new BitmapDrawable(mainBmp);
		array[1] = new BitmapDrawable(seletedBmp);
		LayerDrawable la = new LayerDrawable(array);
		la.setLayerInset(0, 0, 0, 0, 0);
		la.setLayerInset(1, 10, 10, 100, 100);	//int index, int l, int t, int r, int b
		return la; // ���ص��Ӻ��ͼ
	}

	// �޸�ѡ�е�״̬
	public void changeState(int position) {
		// ��ѡʱ
		if (multiChoose == true) {
			mImage_bs.setElementAt(!mImage_bs.elementAt(position), position); // ֱ��ȡ������
		}
		notifyDataSetChanged(); // ֪ͨ���������и���
	}

	/**
	 * ��ȡSD����ָ��Ŀ¼��ָ����չ����ͼƬ�ļ�
	 * 
	 * @param path
	 * @return
	 */
	public Vector<Bitmap> readSDCard(String path) {
		Vector<Bitmap> vector = new Vector<Bitmap>();
		BitmapFactory.Options options = new BitmapFactory.Options();
		pathList = new ArrayList<String>();
		options.inSampleSize = 2;
		File file = new File(path);
		if (file != null) {
			File[] files = file.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String filename) {
					// TODO Auto-generated method stub
					if (filename.endsWith(".jpg")) {
						return true;
					}
					return false;
				}
			});
			if (files != null) {
				for (File f : files) {
					Bitmap bitmap = BitmapFactory.decodeFile(f.getPath(), options);
					vector.add(zoomBitmap(bitmap, 200, 200));
					pathList.add(f.getAbsolutePath());
					mImage_bs.add(false);
				}
			}
		}
		return vector;
	}

	/**
	 * ����ͼƬ������ͼ��С 
	 * @param bitmap
	 * @param w
	 * @param h
	 * @return
	 */
	public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		Matrix matrix = new Matrix();
		float scaleWidht = ((float) w / width);
		float scaleHeight = ((float) h / height);
		matrix.postScale(scaleWidht, scaleHeight);
		Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
//		Bitmap newbmp=Bitmap.createScaledBitmap(bitmap, w, h, false);
		return newbmp;
	}
	
	/**
	 * ɾ��ͼƬ
	 * 
	 * @Title: deleteImage
	 * @Description: TODO(������һ�仰�����������������)
	 * @param @param p
	 * @param @return �趨�ļ�
	 * @return boolean ��������
	 * @throws
	 */
	public int deleteImage() {
		int i=0;
		@SuppressWarnings("rawtypes")
		Iterator it = imageIndex.iterator();
		try {
			while (it.hasNext()) {
				int index = (Integer) it.next();
				if(i > 0){
					index = index -i;
				}
				if (mImageIds != null && index < mImageIds.size()
						&& mImageIds.size() > 0) {
					mImageIds.remove(index);		//��ͼƬԴ�е�ͼƬɾ��
					File file = new File(pathList.get(index));
					if (file.exists()) {
						file.delete();				//����·����SD���ϵ�ͼƬɾ��
						pathList.remove(index);		//��·����������Ӧ������ɾ��
						i++;
					}
					mImage_bs.remove(index);	//��ѡ�������������Ӧ����Ŀɾ��
					//Log.i("imageIndex---->","ɾ����imageIndex��С ��"+imageIndex.size());
				}
			}
			imageIndex.clear();
		} catch (Exception ex) {
			Log.i("ex.printStackTrace",ex.toString());
		}
		return i;
	}
	
	
	/**
	 * ��ȡҪ�ϴ�ͼƬ����Դ
	 * @return
	 */
	public List<File> getUploadImage(){
		List<File> fileList = new ArrayList<File>();
		@SuppressWarnings("rawtypes")
		Iterator it = imageIndex.iterator();
		while (it.hasNext()) {
			int index = (Integer) it.next();
			File file = new File(pathList.get(index));
			fileList.add(file);
		}
		return fileList;
	}

}