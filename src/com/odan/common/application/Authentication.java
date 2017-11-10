package com.odan.common.application;

import com.odan.security.user.model.User;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;

public class Authentication {

	private Authentication() {

	}

	public synchronized static Long getUserId() {
	/*	System.out.println("++ Get User Id");
		HttpServletRequest request = ServletActionContext.getRequest();
//		String userId = request.getHeader("REW3-UserId");
		User u = (User) request.getAttribute("user");
		//return u.getId();*/
		return 1L;
	}

	public synchronized static User getUser() {
		System.out.println("++ Get User");
		HttpServletRequest request = ServletActionContext.getRequest();
		User u = (User) request.getAttribute("user");
		return u;
	}
	public synchronized static String getuserName() {
		HttpServletRequest request = ServletActionContext.getRequest();
		String username = request.getHeader("userName");
		return username ;
	}
	public synchronized static String getpassword() {
		HttpServletRequest request = ServletActionContext.getRequest();
		String password = request.getHeader("Password");
		return password;
	}
}
