package com.starbaby_03.saveAndSearch;

import java.io.File;
import java.io.FileFilter;

import android.util.Log;

import com.starbaby_03.Gallery.mapStorage;
import com.starbaby_03.Gallery.mapShowMin.Filter;
import com.starbaby_03.aboutUs.infoCenter;
import com.starbaby_03.scroll.scrollOperate;

public class serach {
	public void getFile(File root)
	{
		File file[]=root.listFiles();
		if(file!=null)
		{
			for(File f:file)
			{
				if(f.isFile())
				{
					getFile(f);
				}else{
					mapStorage.listFile.add(f);
//					infoCenter.listFile.add(f);
				}
			}
		}
	}
	public void getFile2(File root)
	{
		File file[]=root.listFiles();
		if(file!=null)
		{
			for(File f:file)
			{
				if(f.isFile())
				{
					getFile(f);
				}else{
					scrollOperate.fileList.add(f);
				}
			}
		}
	}
	public void getWord(File root)
	{
		Filter pf = new Filter();
		File file[]=root.listFiles(pf);
		if(file!=null)
			for(File f:file)
			{
				if(f.isDirectory())
				{
					if(!f.getName().toString().matches("^\\..*")){
						getWord(f);
					}
					
				}else{
					mapStorage.listword.add(f.getAbsolutePath());
					System.out.print(f.getAbsolutePath());
				}
			}
	}
	public class Filter implements FileFilter {
		public boolean accept(File f) {
			return f.isDirectory()
					|| f.getName().matches("^.*?\\.(jpg|png|bmp|gif)$");
		}
	}
	public void searchAllPic(File root){
		File file[]=root.listFiles();
		if(file!=null)
		{
			for(File f:file)
			{
				if(f.isFile())
				{
					
				}else{
					getFile(f);
//					lstFile.add(f.toString());
					Log.e("TAG","2");
				}
			}
		}
	}
}
