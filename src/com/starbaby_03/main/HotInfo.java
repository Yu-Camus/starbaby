package com.starbaby_03.main;

import java.util.ArrayList;

import com.starbaby_03.utils.beautyUtils;

public class HotInfo {

	private int height;
	private ArrayList<String> authorList = new ArrayList<String>();
	private ArrayList<String> commentList = new ArrayList<String>();
	private String isrc = "";

	// ͼƬչʾ�Ŀ�� < ��Ļ���/2 �ȱ�������
	public int getWidth() {
		return beautyUtils.layoutWidth / 2 - 20;
	}

	public String getIsrc() {
		return isrc;
	}

	public ArrayList<String> getAuthorList() {
		return authorList;
	}

	public void setAuthorList(ArrayList<String> authorList) {
		this.authorList = authorList;
	}

	public ArrayList<String> getCommentList() {
		return commentList;
	}

	public void setCommentList(ArrayList<String> commentList) {
		this.commentList = commentList;
	}

	public void setIsrc(String isrc) {
		this.isrc = isrc;
	}

	// ͼƬչʾ�ĸ߶� �ȱ�������
	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

}
