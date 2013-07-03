package com.starbaby_03.net;

public interface RequestResultCallback {
	public void onSuccess(Object o);
	public void onFail(Exception e);
}
