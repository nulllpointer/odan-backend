package com.odan.security.accountingperiod.api;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import com.odan.security.accountingperiod.command.CloseAccountingPeriod;
import com.odan.security.accountingperiod.command.CreateAccountingPeriod;
import com.odan.security.accountingperiod.command.ReopenAccountingPeriod;
import com.odan.security.accountingperiod.command.UpdateAccountingPeriod;
import com.odan.security.accountingperiod.model.AccountingPeriod;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

import com.odan.common.api.RestAction;
import com.odan.common.cqrs.CommandRegister;
import com.odan.common.utils.APILogger;

@ParentPackage("jsonPackage")
@Namespace(value = "/v1/accounting")
public class AccountingPeriodResource extends RestAction {
	
	private static final long serialVersionUID = 1L;

	@Action(value = "period", results = { @Result(type = "json") })
	public String actionAccountingPeriod() {
		String response = SUCCESS;
		HttpServletRequest httpRequest = ServletActionContext.getRequest();
		if (httpRequest.getMethod().equals("PUT")) {
			createAccountingPeriod();
		} else {
			response = "HttpMethodNotAccepted";
		}

		return response;
	}

	public String createAccountingPeriod() {
		String responseStatus = SUCCESS;
		HashMap<String, Object> requestData = (HashMap<String, Object>) getRequest();
		CreateAccountingPeriod command = new CreateAccountingPeriod(requestData);
		CommandRegister.getInstance().process(command);
		AccountingPeriod ap = (AccountingPeriod) command.getObject();

		/*if (ap != null) {
			responseStatus = SUCCESS;
			setSuccess("Accounting period created successfully.");
			getData().put("accountingPeriodId", ap.getId().toString());
		} else {
			setError("Accounting period creation failed.");
			getData().put("log", APILogger.getList());
			APILogger.clear();
		}*/

		return responseStatus;
	}

	public String updateAccountingPeriod() {
		String responseStatus = SUCCESS;
		HashMap<String, Object> requestData = (HashMap<String, Object>) getRequest();
		UpdateAccountingPeriod command = new UpdateAccountingPeriod(requestData);
		CommandRegister.getInstance().process(command);
		AccountingPeriod ap = (AccountingPeriod) command.getObject();

		/*if (ap != null) {
			responseStatus = SUCCESS;
			setSuccess("Accounting period update successfully.");
			getData().put("accountingPeriodId", ap.getId().toString());
		} else {
			setError("Accounting period update failed.");
			getData().put("log", APILogger.getList());
			APILogger.clear();
		}
*/
		return responseStatus;
	}
	
	public String closeAccountingPeriod() {
		String responseStatus = SUCCESS;
		HashMap<String, Object> requestData = (HashMap<String, Object>) getRequest();
		CloseAccountingPeriod command = new CloseAccountingPeriod(requestData);
		CommandRegister.getInstance().process(command);
		AccountingPeriod ap = (AccountingPeriod) command.getObject();

	/*	if (ap != null) {
			responseStatus = SUCCESS;
			setSuccess("Accounting period closed successfully.");
			getData().put("accountingPeriodId", ap.getId().toString());
		} else {
			setError("Accounting period close failed.");
			getData().put("log", APILogger.getList());
			APILogger.clear();
		}*/

		return responseStatus;
	}
	
	public String reopenAccountingPeriod() {
		String responseStatus = SUCCESS;
		HashMap<String, Object> requestData = (HashMap<String, Object>) getRequest();
		ReopenAccountingPeriod command = new ReopenAccountingPeriod(requestData);
		CommandRegister.getInstance().process(command);
		AccountingPeriod ap = (AccountingPeriod) command.getObject();

		/*if (ap != null) {
			responseStatus = SUCCESS;
			setSuccess("Accounting period reopened successfully.");
			getData().put("accountingPeriodId", ap.getId().toString());
		} else {
			setError("Accounting period reopen failed.");
			getData().put("log", APILogger.getList());
			APILogger.clear();
		}*/

		return responseStatus;
	}
	
	
}
