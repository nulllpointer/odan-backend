package com.odan.billing.customer.api;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

import com.odan.billing.customer.CustomerQueryHandler;
import com.odan.billing.customer.command.CreateCustomer;
import com.odan.billing.customer.command.UpdateCustomer;
import com.odan.billing.customer.model.Customer;
import com.odan.common.api.RestAction;
import com.odan.common.cqrs.CommandRegister;
import com.odan.common.cqrs.Query;
import com.odan.common.utils.APILogger;

@ParentPackage("jsonPackage")
@Namespace(value = "/v1/billing")
public class CustomerResource extends RestAction {

	@Action(value = "customer", results = { @Result(type = "json") })
	public String actionCustomer() {
		String response = SUCCESS;
		HttpServletRequest httpRequest = ServletActionContext.getRequest();
		if (httpRequest.getMethod().equals("POST")) {
			createCustomer();
		} else if (httpRequest.getMethod().equals("PUT")) {
			updateCustomer();
		} else if (httpRequest.getMethod().equals("GET")) {
			getCustomer();
		} else {
			response = "HttpMethodNotAccepted";
		}

		return response;
	}

	public String createCustomer() {
		String responseStatus = SUCCESS;
		System.out.println("..Create Customer Request");
		HashMap<String, Object> requestData = (HashMap<String, Object>) getRequest();
		CreateCustomer command = new CreateCustomer(requestData);
		CommandRegister.getInstance().process(command);
		Customer c = (Customer) command.getObject();

		if (c != null) {
			responseStatus = SUCCESS;
			setSuccess("System Customer synced successfully.");
			getData().put("customerId", c.getId().toString());
		} else {
			setError("System Customer sync failed.");
			getData().put("log", APILogger.getList());
			APILogger.clear();
		}

		return responseStatus;
	}

	public String updateCustomer() {
		String responseStatus = SUCCESS;
		HashMap<String, Object> requestData = (HashMap<String, Object>) getRequest();
		UpdateCustomer command = new UpdateCustomer(requestData);
		CommandRegister.getInstance().process(command);
		Customer c = (Customer) command.getObject();

		if (c != null) {
			responseStatus = SUCCESS;
			setSuccess("System Customer synced successfully.");
			getData().put("customerId", c.getId().toString());
		} else {
			setError("System Customer sync failed.");
			getData().put("log", APILogger.getList());
			APILogger.clear();
		}

		return responseStatus;
	}

	public String getCustomer() {
		String responseStatus = SUCCESS;
		HashMap<String, Object> requestData = (HashMap<String, Object>) getRequest();
		Query q = new Query(requestData);

		List<Object> customers = (new CustomerQueryHandler()).get(q);

		if (q.validate()) {
			responseStatus = SUCCESS;
			getData().put("customers", customers);
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
