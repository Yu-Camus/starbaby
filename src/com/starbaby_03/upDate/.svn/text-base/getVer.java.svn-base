package com.starbaby_03.upDate;

import com.example.starbaby_03.R;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;

/**
 * 获取当前app版本信息
 * @author  umauser 
 */
public class getVer
{
    public static final String UPDATE_SERVER = "http://www.hao123.com/";
//    		"http://10.0.2.2:8080/web/";
    //闇?崌绾х殑鏂囦欢apk.name
    public static final String UPDATE_APKNAME = "starbaby_02.apk";
    public static final String UPDATE_VERJSON = "ver.json.txt";
    public static final String UPDATE_SAVENAME = "starbaby.apk";
    public static final String UPDATE_PATH = "Environment.getExternalStorageDirectory()";
    public static int getVerCode(Context context)
    {
        int verCode=-1;
        try
        {
            verCode=context.getPackageManager().getPackageInfo("com.example.starbaby_03", 0).versionCode;
        }
        catch (NameNotFoundException e)
        {
            e.printStackTrace();
        }
        return verCode;  
    }
    public static String getVerName(Context context)
    {
        String verName="";
        try
        {
            verName=context.getPackageManager().getPackageInfo("com.example.starbaby_03", 0).versionName;
        }
        catch (NameNotFoundException e)
        {
            e.printStackTrace();
        }
        return verName;   
    }
    public static String getApkName(Context context)
    {
        String apkName=context.getResources().getDrawable(R.string.app_name).toString();
        return apkName;     
    }
  }
