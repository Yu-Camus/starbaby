package com.starbaby_03.Gallery;

import android.util.Log;

public class AbenLog {

	private static final String _msg="------- aben --- i ---:";
	
	/**��ʾ����Ϣ
	 * @param tag ��־��¼��
	 * @param msg ��־��Ϣ����
	 */
	public static void i(String tag,String msg){
		Log.i(tag, _msg+msg);
	}
	
	/**������Ϣ
	 * @param tag �����������
	 * @param msg ������ʾ����
	 */
	public static void e(String tag,String msg){
		Log.e(tag, _msg+msg);
	}
	
//	 һ��Log.v �ĵ�����ɫΪ��ɫ�ģ��κ���Ϣ��������������v����verbose���µ���˼��ƽʱʹ�þ���Log.v("","");
//	 ����Log.d�������ɫ����ɫ�ģ������debug���Ե���˼������������ϲ����Ϣ��������������ͨ��DDMS��Logcat��ǩ��ѡ��
//	 ����Log.i�����Ϊ��ɫ��һ����ʾ�Ե���Ϣinformation�����������Log.v��Log.d����Ϣ��������ʾi��w��e����Ϣ
//	 �ġ�Log.w����˼Ϊ��ɫ�����Կ���Ϊwarning���棬һ����Ҫ����ע���Ż�Android���룬ͬʱѡ�����󻹻����Log.e����Ϣ��
//	 �塢Log.eΪ��ɫ�������뵽error�����������ʾ��ɫ�Ĵ�����Ϣ����Щ�������Ҫ��������ķ������鿴ջ����Ϣ�ˡ�
}
