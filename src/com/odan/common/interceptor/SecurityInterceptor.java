package com.odan.common.interceptor;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

public class SecurityInterceptor implements Interceptor {

	private static final long serialVersionUID = 1L;

	public String intercept(ActionInvocation invocation) throws Exception {

//		HttpServletRequest request = ServletActionContext.getRequest();
//		System.out.println("CUSTOM HEADER" + request.getHeader("custom"));
//		InputStream in = request.getInputStream();
//		System.out.println(in.markSupported());
//		
//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		org.apache.commons.io.IOUtils.copy(in, baos);
//		byte[] bytes = baos.toByteArray();
//		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
//		while (needToReadAgain) {
//		    bais.reset();
//		    read(bais);
//		}
		
		String result = invocation.invoke();

		return result;
	}

	public void destroy() {
		System.out.println("Security Interceptor Destroyed");
	}

	public void init() {
		System.out.println("Security Interceptor Init");
	}
}