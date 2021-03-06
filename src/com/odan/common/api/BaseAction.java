package com.odan.common.api;

import java.util.HashMap;
import java.util.Map;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public abstract class BaseAction extends ActionSupport {

	private static final long serialVersionUID = 8653391256322997267L;

	private String id;
	private Map<String, Object> request = new HashMap<String, Object>();
	private Map<String, Object> data = new HashMap<String, Object>();

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

	protected Map<String, Object> getSession() {
		return ActionContext.getContext().getSession();
	}

	public void setError(String msg) {
		data.put("result", ERROR);
		data.put("message", msg);
	}

	public void setSuccess(String msg) {
		data.put("result", SUCCESS);
		data.put("message", msg);
	}

	public void setSuccess() {
		data.put("result", SUCCESS);
	}

	public Map<String, Object> getRequest() {
		return request;
	}

	public void setRequest(Map<String, Object> request) {
		this.request = request;
	}
	
	public BaseAction() {

	}
}
