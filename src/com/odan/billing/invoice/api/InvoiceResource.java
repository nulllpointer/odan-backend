package com.odan.billing.invoice.api;

import com.odan.billing.invoice.InvoiceQueryHandler;
import com.odan.billing.invoice.command.CreateCustomerInvoice;
import com.odan.billing.invoice.command.CreateVendorBill;
import com.odan.billing.invoice.model.Invoice;
import com.odan.common.api.RestAction;
import com.odan.common.cqrs.CommandRegister;
import com.odan.common.cqrs.Query;
import com.odan.common.model.Flags;
import com.odan.common.model.Flags.InvoiceType;
import com.odan.common.utils.APILogger;
import com.odan.common.utils.Parser;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

@ParentPackage("jsonPackage")
@Namespace(value = "/v1")
public class InvoiceResource extends RestAction {

	private static final long serialVersionUID = 1290L;

	@Action(value = "invoice", results = { @Result(type = "json") })
	public String actionInvoice() {
		String responseStatus = SUCCESS;
		HttpServletRequest httpRequest = ServletActionContext.getRequest();
		HashMap<String, Object> requestData = (HashMap<String, Object>) getRequest();
		InvoiceType invoiceType = InvoiceType.CUSTOMER_INVOICE;
		if (requestData.containsKey("type")) {
			invoiceType = (InvoiceType) Flags.convertInputToEnum(requestData.get("type"), "InvoiceType");
		}
		if (httpRequest.getMethod().equals("GET")) {
			responseStatus = getInvoice();
		} else if (httpRequest.getMethod().equals("POST")) {
			if (invoiceType.equals(InvoiceType.CUSTOMER_INVOICE)) {
				responseStatus = createInternalInvoice();
			} else if (invoiceType.equals(InvoiceType.VENDOR_BILL)) {
				responseStatus = createExternalInvoice();
			}
		} else {
			responseStatus = "HttpMethodNotAccepted";
		}

		return responseStatus;
	}

	public String createInternalInvoice() {
		String responseStatus = SUCCESS;
		HashMap<String, Object> requestData = (HashMap<String, Object>) getRequest();
		CreateCustomerInvoice command = new CreateCustomerInvoice(requestData);
		CommandRegister.getInstance().process(command);
		List<Invoice> invoices = (List<Invoice>) command.getObject();

		if (invoices != null) {
			responseStatus = SUCCESS;
			setSuccess("Invoice(s) created successfully.");
			getData().put("invoiceId", invoices.get(0).getId().toString());
		} else {
			setError("Invoice creation failed.");
			getData().put("log", APILogger.getList());
			APILogger.clear();
		}

		return responseStatus;
	}

	public String createExternalInvoice() {
		String responseStatus = SUCCESS;
		HashMap<String, Object> requestData = (HashMap<String, Object>) getRequest();
		CreateVendorBill command = new CreateVendorBill(requestData);
		CommandRegister.getInstance().process(command);
		List<Invoice> invoices = (List<Invoice>) command.getObject();

		if (invoices != null) {
			responseStatus = SUCCESS;
			setSuccess("Invoice(s) created successfully.");
			getData().put("invoiceId", invoices.get(0).getId().toString());
		} else {
			setError("Invoice creation failed.");
			getData().put("log", APILogger.getList());
			APILogger.clear();
		}

		return responseStatus;
	}





	public String getInvoice() {

		String responseStatus = SUCCESS;
		HashMap<String, Object> requestData = (HashMap<String, Object>) getRequest();

		if (requestData.containsKey("customerId")) {
			requestData.put("customerId", Parser.csvToList(requestData.get("customerId")));
		}
		if (requestData.containsKey("userId")) {
			requestData.put("userId", Parser.csvToList(requestData.get("userId")));
		}
		if (requestData.containsKey("dueStatus")) {
			requestData.put("dueStatus", Parser.csvToList(requestData.get("dueStatus")));
		}
		if (requestData.containsKey("paymentStatus")) {
			requestData.put("paymentStatus", Parser.csvToList(requestData.get("paymentStatus")));
		}
		if (requestData.containsKey("customerStatus")) {
			requestData.put("customerStatus", Parser.csvToList(requestData.get("customerStatus")));
		}
		if (requestData.containsKey("userStatus")) {
			requestData.put("userStatus", Parser.csvToList(requestData.get("userStatus")));
		}
		if (requestData.containsKey("refundStatus")) {
			requestData.put("refundStatus", Parser.csvToList(requestData.get("refundStatus")));
		}
		if (requestData.containsKey("writeoffStatus")) {
			requestData.put("writeoffStatus", Parser.csvToList(requestData.get("writeoffStatus")));
		}

		Query q = new Query(requestData, "billing/invoice/get");

		List<Object> invoices = (new InvoiceQueryHandler()).get(q);

		if (q.validate()) {
			responseStatus = SUCCESS;
			getData().put("invoices", invoices);
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

	@Action(value = "invoice/refund", results = { @Result(type = "json") })
	public String actionRefundRequest() {
		String responseStatus = SUCCESS;
		HttpServletRequest httpRequest = ServletActionContext.getRequest();

		if (httpRequest.getMethod().equals("GET")) {

		} else if (httpRequest.getMethod().equals("POST")) {
 		} else {
			responseStatus = "HttpMethodNotAccepted";
		}

		return responseStatus;
	}



}
