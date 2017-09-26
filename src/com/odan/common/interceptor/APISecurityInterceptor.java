package com.odan.common.interceptor;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;
import com.odan.billing.user.UserQueryHandler;
import com.odan.billing.user.model.User;

public class APISecurityInterceptor implements Interceptor {

	private static final long serialVersionUID = 1L;
	
	public String intercept(ActionInvocation invocation) throws Exception {
		
		System.out.println("INTERCEPTED");
		HttpServletRequest request = ServletActionContext.getRequest();
		String externalId = request.getHeader("REW3-UserId");
		request.setAttribute("userId", 1);
		System.out.print("REW3-UserId: ");
		System.out.println(externalId);
		
		User u = (new UserQueryHandler()).getByExternalId(externalId);

		//TODO
		
		/*if (externalId == null || u == null) {
			return "InvalidUser";
		}*/
		
		request.setAttribute("user", u);
		
		return invocation.invoke();
	}
	
	public boolean validateRequest(HttpServletRequest request) {
		boolean validated = true;
		try {
			String h = request.getHeader("HASH");
			Map<String, Object> mapObject = new HashMap<String, Object>();
			mapObject.put("request", request);
			JSONObject json = new JSONObject(mapObject);
			MessageDigest md = MessageDigest.getInstance("MD5");
			if (h != null && h.equals("KoderLabs")) {
				throw new Exception("Invalid Request Hash");
			}
		} catch (Exception e) {
			e.printStackTrace();
			validated = false;

		}
		return validated;
	}

	public void destroy() {
		System.out.println("Destroying MyLoggingInterceptor...");
	}

	public void init() {
		System.out.println("Initializing MyLoggingInterceptor...");
	}
}