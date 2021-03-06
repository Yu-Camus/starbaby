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
	
	private Context mContext; // 定义Context
	private Vector<Bitmap> mImageIds; // 定义一个向量作为图片源
	private Vector<Boolean> mImage_bs = new Vector<Boolean>(); // 定义一个向量作为选中与否容器
	private boolean multiChoose; // 表示当前适配器是否允许多选
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
			imageView = new ImageView(mContext); // 给ImageView设置资源
			imageView.setLayoutParams(new GridView.LayoutParams(150, 150)); // 设置布局图片
			imageView.setScaleType(ImageView.ScaleType.FIT_CENTER); // 设置显示比例类型
		} else {
			imageView = (ImageView) convertView;
		}
		imageView.setImageDrawable(makeBmp(mImageIds.elementAt(position),
				mImage_bs.elementAt(position), position));
		return imageView;
	}

	private LayerDrawable makeBmp(Bitmap mainBmp, boolean isChosen, int position) {
		// 根据isChosen来选取对勾的图片
		Bitmap seletedBmp;
		if(imageIndex == null){
			imageIndex = new TreeSet<Integer>();
		}
		if (isChosen == true) {
			seletedBmp = BitmapFactory.decodeResource(mContext.getResources(),
					R.drawable.grid_check_on);
			imageIndex.add(position);
//			Log.i("选中",
//					"第" + position + "项目被选中" + "set大小 ：" + imageIndex.size());
		} else {
			seletedBmp = BitmapFactory.decodeResource(mContext.getResources(),
					R.drawable.grid_check_off);
			if (imageIndex.size() > 0) {
				imageIndex.remove(position);
//				Log.i("取消",
//						"第" + position + "项选中被取消" + "set大小 ："
//								+ imageIndex.size());
			}
		}
		// 产生叠加图
		Drawable[] array = new Drawable[2];
		array[0] = new BitmapDrawable(mainBmp);
		array[1] = new BitmapDrawable(seletedBmp);
		LayerDrawable la = new LayerDrawable(array);
		la.setLayerInset(0, 0, 0, 0, 0);
		la.setLayerInset(1, 10, 10, 100, 100);	//int index, int l, int t, int r, int b
		return la; // 返回叠加后的图
	}

	// 修改选中的状态
	public void changeState(int position) {
		// 多选时
		if (multiChoose == true) {
			mImage_bs.setElementAt(!mImage_bs.elementAt(position), position); // 直接取反即可
		}
		notifyDataSetChanged(); // 通知适配器进行更新
	}

	/**
	 * 读取SD卡上指定目录下指定扩展名的图片文件
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
	 * 处理图片的缩略图大小 
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
	 * 删除图片
	 * 
	 * @Title: deleteImage
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param @param p
	 * @param @return 设定文件
	 * @return boolean 返回类型
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
					mImageIds.remove(index);		//将图片源中的图片删除
					File file = new File(pathList.get(index));
					if (file.exists()) {
						file.delete();				//根据路径将SD卡上的图片删除
						pathList.remove(index);		//将路径集合中相应的内容删除
						i++;
					}
					mImage_bs.remove(index);	//将选中与否容器中相应的项目删除
					//Log.i("imageIndex---->","删除后imageIndex大小 ："+imageIndex.size());
				}
			}
			imageIndex.clear();
		} catch (Exception ex) {
			Log.i("ex.printStackTrace",ex.toString());
		}
		return i;
	}
	
	
	/**
	 * 获取要上传图片的资源
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
