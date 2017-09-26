package com.odan.common.interceptor;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

public class HTTPPutRequestInterceptor implements Interceptor {

	private static final long serialVersionUID = 1L;

	public String intercept(ActionInvocation invocation) throws Exception {

		HttpServletRequest request = ServletActionContext.getRequest();
		if (!request.getMethod().equals("PUT")) {
			
			return "PutOnlyMethod";
		}
		return invocation.invoke();
	}

	public void destroy() {
		System.out.println("Security Interceptor Destroyed");
	}

	public void init() {
		System.out.println("Security Interceptor Init");
	}
}