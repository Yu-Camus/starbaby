package com.starbaby_03.net;

import java.io.Serializable;

import org.apache.http.client.methods.HttpUriRequest;
/**
 * 目标�?
 * 1、安全有�?
 * 2、高�?
 * 3、易用�?易控�?
 * 4、activity停止后停止该activity�?��的线程�?
 * 5、监测内存，当内存溢出的时�?自动垃圾回收，清理资�?，当程序�?��之后终止线程�?
 *
 */
public class BaseRequest  implements   Runnable, Serializable {
	//static HttpClient httpClient = null;
	HttpUriRequest request = null;
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected ParseHandler handler = null;
	protected String url = null;
	/**
	 * default is 5 ,to set .
	 */
	protected int connectTimeout = 20000;
	/**
	 * default is 5 ,to set .
	 */
	protected int readTimeout = 20000;
	protected RequestResultCallback requestCallback = null;
	
	
	@Override
	public void run() {
		
	}
	
	protected void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}
	
	protected void setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
	}
	
	public HttpUriRequest getRequest() {
		return request;
	}
	
}
