package com.starbaby_03.Gallery;

import android.util.Log;

public class AbenLog {

	private static final String _msg="------- aben --- i ---:";
	
	/**提示性信息
	 * @param tag 日志记录类
	 * @param msg 日志消息内容
	 */
	public static void i(String tag,String msg){
		Log.i(tag, _msg+msg);
	}
	
	/**错误信息
	 * @param tag 发生错误的类
	 * @param msg 错误提示内容
	 */
	public static void e(String tag,String msg){
		Log.e(tag, _msg+msg);
	}
	
//	 一、Log.v 的调试颜色为黑色的，任何消息都会输出，这里的v代表verbose啰嗦的意思，平时使用就是Log.v("","");
//	 二、Log.d的输出颜色是蓝色的，仅输出debug调试的意思，但他会输出上层的信息，过滤起来可以通过DDMS的Logcat标签来选择
//	 三、Log.i的输出为绿色，一般提示性的消息information，它不会输出Log.v和Log.d的信息，但会显示i、w和e的信息
//	 四、Log.w的意思为橙色，可以看作为warning警告，一般需要我们注意优化Android代码，同时选择它后还会输出Log.e的信息。
//	 五、Log.e为红色，可以想到error错误，这里仅显示红色的错误信息，这些错误就需要我们认真的分析，查看栈的信息了。
}
