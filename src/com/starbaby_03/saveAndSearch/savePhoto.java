package com.starbaby_03.saveAndSearch;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

import com.starbaby_03.utils.beautyUtils;
import com.starbaby_03.utils.cameraUtils;
import com.starbaby_03.utils.saveFile;

import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.util.Log;


public class savePhoto {
	private File tempFile;
	private float w,h;
	private String str=null;
    private Date date=null;
	//������ȡ�������
	public void savephoto(Intent intent)
	{
		  tempFile=new File(saveFile.operateName);
		  File temp = new File(saveFile.operatePath);//������Ŀ �ļ���
			if (!temp.exists()) {
				temp.mkdir();
			}
			intent.putExtra("output", Uri.fromFile(tempFile));  // ר��Ŀ���ļ�   
			intent.putExtra("outputFormat", "JPEG"); //�����ļ���ʽ  
	}
	//����ÿ�μ��ã��߿����ֲ������ͼƬ
	public void saveImg(Bitmap bmp) throws IOException
	{
		String photoOperatePath = saveFile.operatePath;
		File folder = new File(photoOperatePath);
		if(!folder.exists()) //����ļ��в������򴴽�
		{
			folder.mkdir();
		}
		File myCaptureFile = new File(saveFile.operateName);
	        BufferedOutputStream bos = new BufferedOutputStream(
	                                                 new FileOutputStream(myCaptureFile));
	        bmp.compress(Bitmap.CompressFormat.JPEG, 80, bos);
	        bos.flush();
	        bos.close();
	}
	//�������ղ�����ɵ�ͼƬ
		public void saveEndImg(Bitmap bmp) throws IOException
		{	
			
			String saveImgPath = saveFile.finishPath;
			File folder = new File(saveImgPath);
			if(!folder.exists()) //����ļ��в������򴴽�
			{
				folder.mkdir();
			}
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");//��ȡ��ǰʱ�䣬��һ��ת��Ϊ�ַ���
            date =new Date();
            str=format.format(date);
			File myCaptureFile = new File(saveFile.finishName+str+".jpg");
		        BufferedOutputStream bos = new BufferedOutputStream(
		                                                 new FileOutputStream(myCaptureFile));
		        bmp.compress(Bitmap.CompressFormat.JPEG, 80, bos);
		        bos.flush();
		        bos.close();
		}
	//����ͷ��
	public void saveHeadImg(Bitmap bitmap) throws IOException
	{
		String saveHeadImgPath = saveFile.operatePath;
		File folder = new File(saveHeadImgPath);
		if(!folder.exists()) //����ļ��в������򴴽�
		{
			folder.mkdir();
		}
		File headImgFile=new File( saveFile.headName);
		BufferedOutputStream boStream=new BufferedOutputStream(new FileOutputStream(headImgFile));
		bitmap.compress(Bitmap.CompressFormat.JPEG,80,boStream);
		boStream.flush();
		boStream.close();
	}
	//ѹ��ͼƬ����
	public void scale(File filepath,int W,int H) throws IOException
	{
		String photoOperatePath = saveFile.operatePath;
		File folder = new File(photoOperatePath);
		if(!folder.exists()) //����ļ��в������򴴽�
		{
			folder.mkdir();
		}
		File headImgFile=new File( saveFile.operateName);
		Matrix matrix=new Matrix();
		Bitmap bitmap=BitmapFactory.decodeFile(filepath.toString());
		Bitmap newBitmap=null;
		int width=bitmap.getWidth();
		int height=bitmap.getHeight();
		float m=0;
		Log.e("width+", width+"");
		Log.e("height+",height+"");
		if(width>W||height>H)
		{
			w=((float)W)/width;//480
			h=((float)H)/height;//597
			Log.e("w+", w+"");
			Log.e("h+",h+"");
			if(w>h)
			{
				m=h;
				
			}else{
				m=w;
			}
				
		}else{
			m=1;
		}
		Log.e("m+", m+"");
		matrix.postScale(m, m);
		newBitmap=Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
		Log.e("matrix+", matrix+"");
		Log.e("W+", newBitmap.getWidth()+"");
		Log.e("H+",newBitmap.getHeight()+"");
		BufferedOutputStream bos=new BufferedOutputStream(new FileOutputStream(headImgFile));
		newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        bos.flush();
        bos.close();
	}
	//�����ա���������ǰ ��ͼƬ���в���
	public void takePhoto(Bitmap bmp) throws IOException{
		String photoOperatePath = saveFile.operatePath;
		File folder = new File(photoOperatePath);
		if(!folder.exists()) //����ļ��в������򴴽�
		{
			folder.mkdir();
		}
		File myCaptureFile = new File(saveFile.operateName);
	        BufferedOutputStream bos = new BufferedOutputStream(
	                                                 new FileOutputStream(myCaptureFile));
	        bmp.compress(Bitmap.CompressFormat.JPEG, 80, bos);
	        bos.flush();
	        bos.close();
	}
	//����,�����ѡ����ᱣ�����
	public void takePhoto(Bitmap bmp,String file) throws IOException{
		File folder = new File(file);
		if(!folder.exists()) //����ļ��в������򴴽�
		{
			folder.mkdir();
		}
		long dataTake = System.currentTimeMillis();
		beautyUtils.spPicPath=file+"/"+dataTake+".jpg";
		File myCaptureFile = new File(file+"/"+dataTake+".jpg");
		Log.e("Tag", myCaptureFile.toString());
	        BufferedOutputStream bos = new BufferedOutputStream(
	                                                 new FileOutputStream(myCaptureFile));
	        bmp.compress(Bitmap.CompressFormat.JPEG, 80, bos);
	        bos.flush();
	        bos.close();
	        Log.e("TAG","save");
	}
	/**
	 * cameraͼƬ����
	 * @param bm
	 */
	/*����һ��Bitmap�����б���*/
	public void saveJpeg(Bitmap bm){
		String savePath = saveFile.cameraPath;
		File folder = new File(savePath);
		if(!folder.exists()) //����ļ��в������򴴽�
		{
			folder.mkdir();
		}
		long dataTake = System.currentTimeMillis();
		String jpegName = savePath + dataTake +".jpg";
		Log.i("tag", "saveJpeg:jpegName--" + jpegName);
		//File jpegFile = new File(jpegName);
		try {
			FileOutputStream fout = new FileOutputStream(jpegName);
			BufferedOutputStream bos = new BufferedOutputStream(fout);

			//			//�����Ҫ�ı��С(Ĭ�ϵ��ǿ�960����1280),��ĳɿ�600����800
						Bitmap newBM = bm.createScaledBitmap(bm, (int)cameraUtils.width, (int)cameraUtils.height, false);
						newBM.compress(Bitmap.CompressFormat.JPEG, 80, bos);
			bos.flush();
			bos.close();
			Log.i("tag", "saveJpeg���洢��ϣ�");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.i("tag", "saveJpeg:�洢ʧ�ܣ�");
			e.printStackTrace();
		}
	}
}
