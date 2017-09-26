package com.odan.common.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opensymphony.xwork2.Action;

public abstract class RestAction extends BaseAction {

	private static final long serialVersionUID = 8653391256322997267L;

	public Map<String, Object> getJSONRequest(HttpServletRequest req) {
		StringBuilder buffer = new StringBuilder();
		String json = "";
		Map<String, Object> map = new HashMap<>();
		BufferedReader reader;
		try {
			reader = req.getReader();

			String line;
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			json = buffer.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (json != null) {
			try {
				ObjectMapper mapper = new ObjectMapper();
				map = mapper.readValue(json, new TypeReference<HashMap<String, Object>>() {
				});

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return map;
	}

	public Map<String, Object> getQueryRequest(HttpServletRequest req) {
		Map<String, Object> map = new HashMap<>();
		Map<String, String[]> query = req.getParameterMap();
		for (String key : query.keySet()) {
			String[] arr = query.get(key);
			if (arr.length >= 1) {
				map.put(key, arr[0]);
			}
		}
		return map;
	}

	public RestAction() {
		HttpServletRequest req = ServletActionContext.getRequest();
		String method = req.getMethod();
		if (method.equals("GET")) {
			this.setRequest(getQueryRequest(req));
		} else {
			this.setRequest(getJSONRequest(req));
		}
	}

	public String execute() {
		return Action.SUCCESS;
	}
}
