package com.starbaby_03.utils;

import java.util.ArrayList;

public class weiboUtils {
	public static int weibo_sharePic_Flag=1;//从mapshoworginal处进入sharebaby
	public static int weibo_sharePic_Flag2=2;//从follow_title处进入sharebaby
	public static int weibo_showPic_Flag=3;
	public static String weibo_sharePic_key="weibo_map";
	public static String weibo_return_key="weibo_return";
	public static int weibo_return_Flag=1;//从sharebaby返回PullDownActivity
	public static int weibo_return_Flag2=2;//从sharebaby返回mapshoworginal
	public static int weibo_return_Flag3=3;//从appmain进入PullDownActivity
	public static String weibo_picUrl=null;//分享本地图片的URL
	public static String weibo_sharePicSucc_key="weibo_succed";
	public static int weibo_sharePicSucc_Flag=1;//发布帖子，刷新listview
	public static int weibo_sharePicSucc_Flag2=2;//进入帖子列表，刷新listview
	public static ArrayList<String> pathList_1=null;//相册里的图片
	public static int FLAG;
}
