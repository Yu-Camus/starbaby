package com.starbaby_03.net;

import java.io.File;

public class RequestParameter implements java.io.Serializable,
		Comparable<Object> {

	private static final long serialVersionUID = 1274906854152052510L;

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	private String value;

	public RequestParameter(String name, String value) {
		this.name = name;
		this.value = value;
	}
	private Integer value2;
	public Integer getValue2()
	{
		return value2;
		
	}
	public void setValue2(Integer value2)
	{
		this.value2=value2;
	}
	public RequestParameter(String name, Integer value2) {
		this.name = name;
		this.value2 = value2;
	}
	private File file;
	public File getFile()
	{
		return file;
		
	}
	public void setFile()
	{
		this.file=file;
	}
	public RequestParameter(String name, File file) {
		this.name = name;
		this.file = file;
	}
	@Override
	public boolean equals(Object o) {
		if (null == o) {
			return false;
		}

		if (this == o) {
			return true;
		}

		if (o instanceof RequestParameter) {
			RequestParameter parameter = (RequestParameter) o;
			return name.equals(parameter.name) && value.equals(parameter.value);
		}

		return false;
	}

	@Override
	public int compareTo(Object another) {
		int compared;
		RequestParameter parameter = (RequestParameter) another;
		compared = name.compareTo(parameter.name);
		if (compared == 0) {
			compared = value.compareTo(parameter.value);
		}
		return compared;
	}
}
