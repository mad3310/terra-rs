package com.le.matrix.redis.model;

import java.io.Serializable;

public class RESTfulResult implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String body;//http调用后返回的body
	private String url;//http调动url
	private int status;//http调用返回的状态码
	
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "RESTfulResult [body=" + body + ", url=" + url + ", status="
				+ status + "]";
	}
	
}