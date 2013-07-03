package com.starbaby_03.utils;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonObject {

	JSONObject json;

	public int getMSG(String request) throws JSONException {
		int MSG = 0;
		json = new JSONObject(request);
		MSG = json.getInt("msg");
		return MSG;

	}

	public int getUID(String request) throws JSONException {
		int UID = 0;
		json = new JSONObject(request);
		UID = json.getInt("uid");
		return UID;

	}

	public String getUSERNAME(String request) throws JSONException {
		String USERNAME = null;
		json = new JSONObject(request);
		USERNAME = json.getString("username");
		return USERNAME;

	}

	public String getEMAIL(String request) throws JSONException {
		String EMAIL = null;
		json = new JSONObject(request);
		EMAIL = json.getString("email");
		return EMAIL;

	}

	public String getAVATAR(String request) throws JSONException {
		String AVATAR = null;
		json = new JSONObject(request);
		AVATAR = json.getString("avatar");
		return AVATAR;

	}

	public String getIMAGEURl(String request) throws JSONException {
		String IMAGEURl = null;
		json = new JSONObject(request);
		IMAGEURl = json.getString("imageurl");
		return IMAGEURl;

	}

	/**
	 * �������
	 * 
	 * @param request
	 * @return
	 * @throws JSONException
	 */
	// ����������
	public int albumsize(String request) throws JSONException {
		int albumsize = 0;
		json = new JSONObject(request);
		albumsize = json.getInt("albumsize");
		return albumsize;

	}

	// ����������url
	public String getFrameURl(String request) throws JSONException {
		String FrameURl = null;
		json = new JSONObject(request);
		FrameURl = json.getString("img");
		return FrameURl;
	}

	// �������id
	public String getFrameId(String request) throws JSONException {
		String FrameId = null;
		json = new JSONObject(request);
		FrameId = json.getString("img");
		return FrameId;
	}
	//��������
}
