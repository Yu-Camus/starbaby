package com.starbaby_03.utils;

public class aboutUsUtils {
	public  String name;//�������
	public  String picUrl;//�������ͼƬurl
	public int albumid;//���uid
	public int getAlbumid() {
		return albumid;
	}
	public void setAlbumid(int albumid) {
		this.albumid = albumid;
	}
	public  String getName() {
		return name;
	}
	public  void setName(String name) {
		this.name = name;
	}
	public  String getPicUrl() {
		return picUrl;
	}
	public  void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	public aboutUsUtils(String name, String picUrl,int albumid) {
		super();
		this.name = name;
		this.picUrl = picUrl;
		this.albumid=albumid;
	}
	
	
}
