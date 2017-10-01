package com.odan.billing.contact.api;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

import com.odan.billing.contact.ContactQueryHandler;
import com.odan.billing.contact.command.CreateContact;
import com.odan.billing.contact.command.UpdateContact;
import com.odan.billing.contact.model.Contact;
import com.odan.common.api.RestAction;
import com.odan.common.cqrs.CommandRegister;
import com.odan.common.cqrs.Query;
import com.odan.common.utils.APILogger;

@ParentPackage("jsonPackage")
@Namespace(value = "/v1/billing")
public class ContactResource extends RestAction {

	@Action(value = "contact", results = { @Result(type = "json") })
	public String actionContact() {
		String response = SUCCESS;
		HttpServletRequest httpRequest = ServletActionContext.getRequest();
		if (httpRequest.getMethod().equals("POST")) {
			createContact();
		} else if (httpRequest.getMethod().equals("PUT")) {
			updateContact();
		} else if (httpRequest.getMethod().equals("GET")) {
			getContact();
		} else {
			response = "HttpMethodNotAccepted";
		}

		return response;
	}

	public String createContact() {
		String responseStatus = SUCCESS;
		System.out.println("..Create Customer Request");
		HashMap<String, Object> requestData = (HashMap<String, Object>) getRequest();
		CreateContact command = new CreateContact(requestData);
		CommandRegister.getInstance().process(command);
		Contact c = (Contact) command.getObject();

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

	public String updateContact() {
		String responseStatus = SUCCESS;
		HashMap<String, Object> requestData = (HashMap<String, Object>) getRequest();
		UpdateContact command = new UpdateContact(requestData);
		CommandRegister.getInstance().process(command);
		Contact c = (Contact) command.getObject();

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

	public String getContact() {
		String responseStatus = SUCCESS;
		HashMap<String, Object> requestData = (HashMap<String, Object>) getRequest();
		Query q = new Query(requestData);

		List<Object> customers = (new ContactQueryHandler()).get(q);

		if (q.validate()) {
			responseStatus = SUCCESS;
			getData().put("contacts", customers);
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
