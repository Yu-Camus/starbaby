package com.starbaby_03.utils;

import android.content.SharedPreferences;

public class contentUtils {
	
	public static String enterUrl="http://www.starbaby.cn/appApi/validateUser";//登入接口
	public static String registerImgUrl="http://www.starbaby.cn/appApi/imageup";//本地上传图片 反馈的url
	public static String registerUrl="http://www.starbaby.cn/appApi/register";//注册接口
	public static String changeImgUrl="http://www.starbaby.cn/appApi/editAvatar";//修改头像接口
	public static String getImgUrl="http://www.starbaby.cn/appApi/avatar_api";//获取用户头像接口
	public static String hotUrl="http://www.starbaby.cn/appApi/cameraApi/hot_pics";//最热照片接口
	public static String newUrl="http://www.starbaby.cn/appApi/cameraApi/new_pics/";//最新照片接口		
	public static String frameList="http://www.starbaby.cn/appApi/cameraApi/user_albums/";//我/Ta的相册列表
	public static String photoList="http://www.starbaby.cn/appApi/cameraApi/album_pics/";//相册照片接口
	public static String sendPicUrl="http://www.starbaby.cn/appApi/cameraApi/add_pic";//上传照片接口
	
	public static String headImgUrl="";
	public static int uid=0;//用户头像外部UID
	public static int imgSize=1;//头像尺寸 :1-48*48px；2-120*120px；3-200*200px
	
	public static int msg=0;
	public static String username=null;
	public static String email=null;
	public static String avatar=null;
	public static String imageurl=null;
	public static String psw=null;
	
	public static SharedPreferences sp=null;//记住登入：保存密码和账号
	public static SharedPreferences spinfo=null;//记住个人信息
	public static SharedPreferences spGetInfo = null;//登入后 接口反馈的个人信息
}
