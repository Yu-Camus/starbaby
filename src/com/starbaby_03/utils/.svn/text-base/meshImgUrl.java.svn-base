package com.starbaby_03.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class meshImgUrl {
	public Bitmap returnBitMap(String url) { 
		URL myFileUrl = null; 
		Bitmap bitmap = null; 
		try { 
		myFileUrl = new URL(url); 
		} catch (MalformedURLException e) { 
		e.printStackTrace(); 
		} 
		try { 
		HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection(); 
		conn.setDoInput(true); 
		conn.connect(); 
		InputStream is = conn.getInputStream(); 
		bitmap = BitmapFactory.decodeStream(is); 
		is.close(); 
		} catch (IOException e) { 
		e.printStackTrace(); 
		} 
		return bitmap; 
		} 
}
