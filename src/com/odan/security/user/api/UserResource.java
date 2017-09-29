package com.odan.security.user.api;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

import com.odan.security.user.UserQueryHandler;
import com.odan.security.user.command.CreateUser;
import com.odan.security.user.command.UpdateUser;
import com.odan.security.user.model.User;
import com.odan.common.api.RestAction;
import com.odan.common.cqrs.CommandRegister;
import com.odan.common.cqrs.Query;
import com.odan.common.utils.APILogger;

@ParentPackage("jsonPackage")
@Namespace(value = "/v1/billing")
public class UserResource extends RestAction {

	@Action(value = "user", results = { @Result(type = "json") })
	public String actionUser() {
		String response = SUCCESS;
		HttpServletRequest httpRequest = ServletActionContext.getRequest();
		if (httpRequest.getMethod().equals("POST")) {
			createUser();
		} else if (httpRequest.getMethod().equals("PUT")) {
			updateUser();
		} else if (httpRequest.getMethod().equals("GET")) {
			getUser();
		} else {
			response = "HttpMethodNotAccepted";
		}

		return response;
	}

	public String createUser() {
		String responseStatus = SUCCESS;
		HashMap<String, Object> requestData = (HashMap<String, Object>) getRequest();
		CreateUser command = new CreateUser(requestData);
		CommandRegister.getInstance().process(command);
		User su = (User) command.getObject();

		if (su != null) {
			responseStatus = SUCCESS;
			setSuccess("User created successfully.");
			getData().put("userId", su.getId().toString());
		} else {
			setError("User creation failed.");
			getData().put("log", APILogger.getList());
			APILogger.clear();
		}

		return responseStatus;
	}

	public String updateUser() {
		String responseStatus = SUCCESS;
		HashMap<String, Object> requestData = (HashMap<String, Object>) getRequest();
		UpdateUser command = new UpdateUser(requestData);
		CommandRegister.getInstance().process(command);
		User su = (User) command.getObject();

		if (su != null) {
			responseStatus = SUCCESS;
			setSuccess("User updated successfully.");
			getData().put("userId", su.getId().toString());
		} else {
			setError("User update failed.");
			getData().put("log", APILogger.getList());
			APILogger.clear();
		}

		return responseStatus;
	}

	public String getUser() {
		String responseStatus = SUCCESS;
		HashMap<String, Object> requestData = (HashMap<String, Object>) getRequest();
		Query q = new Query(requestData);

		List<Object> users = (new UserQueryHandler()).get(q);

		if (q.validate()) {
			responseStatus = SUCCESS;
			getData().put("users", users);
			getData().put("logs", APILogger.getList());
			setSuccess();
		} else {
			responseStatus = ERROR;
			setError("Error Occured");
			getData().put("logs", APILogger.getList());
			APILogger.clear();
		}

		return responseStatus;
	}
}
