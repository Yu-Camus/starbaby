package com.starbaby_03.utils;

import android.content.SharedPreferences;

public class contentUtils {
	
	public static String enterUrl="http://www.starbaby.cn/appApi/validateUser";//����ӿ�
	public static String registerImgUrl="http://www.starbaby.cn/appApi/imageup";//�����ϴ�ͼƬ ������url
	public static String registerUrl="http://www.starbaby.cn/appApi/register";//ע��ӿ�
	public static String changeImgUrl="http://www.starbaby.cn/appApi/editAvatar";//�޸�ͷ��ӿ�
	public static String getImgUrl="http://www.starbaby.cn/appApi/avatar_api";//��ȡ�û�ͷ��ӿ�
	public static String hotUrl="http://www.starbaby.cn/appApi/cameraApi/hot_pics";//������Ƭ�ӿ�
	public static String newUrl="http://www.starbaby.cn/appApi/cameraApi/new_pics/";//������Ƭ�ӿ�		
	public static String frameList="http://www.starbaby.cn/appApi/cameraApi/user_albums/";//��/Ta������б�
	public static String photoList="http://www.starbaby.cn/appApi/cameraApi/album_pics/";//�����Ƭ�ӿ�
	public static String sendPicUrl="http://www.starbaby.cn/appApi/cameraApi/add_pic";//�ϴ���Ƭ�ӿ�
	
	public static String headImgUrl="";
	public static int uid=0;//�û�ͷ���ⲿUID
	public static int imgSize=1;//ͷ��ߴ� :1-48*48px��2-120*120px��3-200*200px
	
	public static int msg=0;
	public static String username=null;
	public static String email=null;
	public static String avatar=null;
	public static String imageurl=null;
	public static String psw=null;
	
	public static SharedPreferences sp=null;//��ס���룺����������˺�
	public static SharedPreferences spinfo=null;//��ס������Ϣ
	public static SharedPreferences spGetInfo = null;//����� �ӿڷ����ĸ�����Ϣ
}
