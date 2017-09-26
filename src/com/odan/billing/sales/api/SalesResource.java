package com.odan.billing.sales.api;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

import com.odan.billing.sales.SalesQueryHandler;
import com.odan.billing.sales.command.CreateSales;
import com.odan.billing.sales.model.Sales;
import com.odan.common.api.RestAction;
import com.odan.common.cqrs.CommandRegister;
import com.odan.common.cqrs.Query;
import com.odan.common.utils.APILogger;

@ParentPackage("jsonPackage")
@Namespace(value = "/v1")
public class SalesResource extends RestAction {

	@Action(value = "sales", results = { @Result(type = "json") })
	public String actionSales() {
		String response = SUCCESS;
		HttpServletRequest httpRequest = ServletActionContext.getRequest();
		if (httpRequest.getMethod().equals("GET")) {
			getSales();
		} else if (httpRequest.getMethod().equals("POST")) {
			createSales();
		} else {
			response = "HttpMethodNotAccepted";
		}

		return response;
	}

	public String createSales() {
		String responseStatus = SUCCESS;
		HashMap<String, Object> requestData = (HashMap<String, Object>) getRequest();
		CreateSales command = new CreateSales(requestData);
		CommandRegister.getInstance().process(command);
		Sales s = (Sales) command.getObject();

		if (s != null) {
			responseStatus = SUCCESS;
			setSuccess("Sales created successfully.");
			getData().put("salesId", s.getId().toString());
		} else {
			setError("Sales creation failed.");
			getData().put("log", APILogger.getList());
			APILogger.clear();
		}

		return responseStatus;
		
	}

	public String getSales() {
		String responseStatus = SUCCESS;
		HashMap<String, Object> requestData = (HashMap<String, Object>) getRequest();
		Query q = new Query(requestData, "billing/sales/get");

		List<Object> sales = (new SalesQueryHandler()).get(q);

		if (q.validate()) {
			responseStatus = SUCCESS;
			getData().put("sales", sales);
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
